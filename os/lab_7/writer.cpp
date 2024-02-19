#include <iostream>
#include <unistd.h>
#include <mqueue.h>
#include <sys/types.h>
#include <sys/ipc.h>
#include <sys/msg.h>
#include <csignal>
#include <cstring>
#include <pthread.h>

#define BUFFLEN 256

typedef struct {
    long type;
    char buff[BUFFLEN];
} message;

int exitflag = 0;
int msqid;

void sig_handler(int signo) {
    printf("\nget SIGNINT; %d\n", signo);
    printf("Closing the mq...\n");

    msgctl(msqid, IPC_RMID, nullptr);    
}

static void* writer(void* args) {
    printf("Enterting the writer function\n");
    printf("=============================\n");
    message msg;
    char hostname[BUFFLEN];

    while (exitflag == 0) {
        gethostname(hostname, BUFFLEN);
        printf("Host name: %s\n", hostname);

        // collecting message data
        msg.type = 1;
        
        memcpy(&msg.buff, &hostname, BUFFLEN);
        int result = msgsnd(msqid, &msg, BUFFLEN, IPC_NOWAIT);
        printf("Adding host name to the queue...\n");

        if (result == 0) {
            printf("Message has been added!\n");
        } else {
            printf("Error while adding the message: reader has closed the queue\n");
        } 
        printf("=============================\n");

        sleep(1);
    }

    return 0;
}

int main() {
    printf("Entering the writer program\n");
    signal(SIGINT, sig_handler);

    pthread_t writer_thread;

    int key = ftok("writer", 228);
    if (key == -1) {
        perror("Error when generating a key\n:");
    }

    // flag = 0 && key != IPC_PRIVATE , => obtain prevoiusly created mq
    // else creates a new mq
    printf("Trying to open an existing mq...\n");
    msqid = msgget(key, 0666);
    if (msqid < 0) {
        printf("Failed to open an mq, => creating a new one\n");
        msqid = msgget(key, IPC_CREAT | S_IWUSR | S_IRUSR);
    }

    pthread_create(&writer_thread, NULL, writer, NULL);

    printf("Main writer thread has been locked...\n");
    getchar();
    printf("Main thread has been unlocked\n");

    exitflag = 1;

    pthread_join(writer_thread, NULL);

    printf("Closing the mq\n");
    msgctl(msqid, IPC_RMID, nullptr);

    printf("Exiting the writer program\n");
    return 0;
}
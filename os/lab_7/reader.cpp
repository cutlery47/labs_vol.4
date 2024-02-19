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

static void* reader(void* args) {
    printf("Enterting the reader function\n");
    printf("=============================\n");
    message msg;

    while (exitflag == 0) {
        msg.type = 1;
        memset(msg.buff, 0, sizeof(msg.buff));

        int result = msgrcv(msqid, &msg, sizeof(msg.buff), msg.type, IPC_NOWAIT);
        printf("Recieving the message\n");

        if (result > 0) {
            printf("Message: %s\n", msg.buff);
            printf("Message size: %d\n", result);
        } else {
            printf("Error while recieving the message: writer has closed the queue\n");
        } 
        printf("=============================\n");

        sleep(1);
    }

    return 0;
}

int main() {
    printf("Entering the writer program\n");
    signal(SIGINT, sig_handler);

    pthread_t reader_thread;

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

    pthread_create(&reader_thread, NULL, reader, NULL);

    printf("Main reader thread has been locked...\n");
    getchar();
    printf("Main thread has been unlocked\n");

    exitflag = 1;

    pthread_join(reader_thread, NULL);

    printf("Closing the mq\n");
    msgctl(msqid, IPC_RMID, nullptr);

    printf("Exiting the reader program\n");
    return 0;
}
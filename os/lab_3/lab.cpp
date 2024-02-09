#include <iostream>
#include <unistd.h>
#include <pthread.h>
#include <stdio.h>
#include <fcntl.h>

int exitflag_1 = 1;
int exitflag_2 = 1;


// function - int gethostname(char* name, size_t len)

// fd[0] - reading descriptor
// fd[1] - writing descriptor
int fd[2];

bool areEqual(const char* first, const char* second) {
    if (first == nullptr || second == nullptr) {
        return false;
    }

    int length = sizeof(first) ? sizeof(first) > sizeof(second) : sizeof(second);

    for (int i = 0; i < length; ++i) {
        if (first[i] != second[i]) {
            return false;
        }
    }

    return true;
}

static void* reader(void* args) {
    printf("==========================\n");
    printf("Entering the reader thread\n");
    size_t bufflen = 16;
    char buffer[bufflen];

    while (exitflag_1 == 1) {
        buffer[0] = '\0';

        printf("Reading data from the pipe\n");
        size_t status = read(fd[0], buffer, bufflen);

        if (status > 0) {
            printf("Data has been acquired: %s\n", buffer);
        } else if (status == 0) {
            printf("Data was not found\n");
        } else {
            perror("Reading error");
        }

        printf("==========================\n");
        sleep(1);
    }

    return 0;
}

static void* writer(void* args) {
    printf("==========================\n");
    printf("Entering the writer thread\n");
    size_t namelen = 16;
    char name[namelen];

    while (exitflag_2 == 1) {
        gethostname(name, namelen);

        printf("Writing data to the pipe\n");
        size_t status = write(fd[1], name, namelen);

        if (status > 0) {
            printf("Success: the amount of data written is: %zu bits\n", status);
        } else {
            perror("Writing error");
        }

        printf("==========================\n");
        sleep(1);
    }

    return 0;
}

int main(int args, char* argv[]) {
    pthread_t worker_1;
    pthread_t worker_2;

    const char* mode = argv[1];
    int id;

    printf("Input = %s\n", mode);

    if (areEqual(mode, "blocking")) {
        id = pipe(fd);
    } else if (areEqual(mode, "unblocking_1")) {
        id = pipe2(fd, O_NONBLOCK);
    } else if (areEqual(mode, "unblocking_2")) {
        id = pipe(fd);
        fcntl(fd[0], F_SETFL, O_NONBLOCK);
        fcntl(fd[1], F_SETFL, O_NONBLOCK);
    } else {
        printf("Invalid argument: Exiting the program\n");
        return 0;
    }


    pthread_create(&worker_1, NULL, reader, NULL);
    pthread_create(&worker_2, NULL, writer, NULL);

    printf("Main thread is waiting for any key to be pressed... \n");
    getchar();
    printf("\nMain thread has been unlocked... \n");   

    exitflag_1 = 0;
    exitflag_2 = 0;

    pthread_join(worker_1, NULL);
    pthread_join(worker_2, NULL);
    printf("Both threads have finished their procedures... \n");

    printf("Closing file descriptors\n");
    close(fd[0]);
    close(fd[1]);

    printf("Exiting the program\n");
    return 0;
}
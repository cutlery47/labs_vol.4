#include <iostream>
#include <pthread.h>
#include <unistd.h>
#include <csignal>
#include <errno.h>

typedef struct {
    int flag;
} thread_data;

pthread_mutex_t mutex;

const char* errcodeIdentifier(int code) {
    switch(code) {
        case EINVAL:
            return "EINVAL";
        case ETIMEDOUT:
            return "ETIMEDOUT";
        case EAGAIN:
            return "EAGAIN";
        case EDEADLK:
            return "EDEADLK";
    }
    return "HOW";
}

void sig_handler(int signo) {
    printf("\nget SIGNINT; %d\n", signo);
    exit(0);
}

static void* coroutine_1(void* arg) {
    printf("Entering the first thread... \n");
    thread_data* data = (thread_data*) arg;   
    timespec tp; 

    while (1) {
        while(1) {
            clock_gettime(CLOCK_REALTIME, &tp);
            tp.tv_sec += 1;

            int status = pthread_mutex_timedlock(&mutex, &tp);

            if (data->flag == 0) {
                printf("Exiting the first thread... \n");
                pthread_exit((void*)0);
                return 0;
            }

            if (status != 0) {
                const char* errcode = errcodeIdentifier(status);
                printf("\nBlocked thread error message: %s\n", errcode);
            } else {
                break;
            }
        };

        for (int i = 0; i < 10; ++i) {
            putchar('1');
            fflush(stdout);
            // time in seconds
            sleep(1);
        }

        pthread_mutex_unlock(&mutex);
        sleep(1);
    }
}

static void* coroutine_2(void* arg) {
    printf("Entering the second thread... \n");
    thread_data* data = (thread_data*) arg;
    timespec tp;
    
    while (1) {
        while(1) {
            clock_gettime(CLOCK_REALTIME, &tp);
            tp.tv_sec += 1;

            int status = pthread_mutex_timedlock(&mutex, &tp);

            if (data->flag == 0) {
                printf("Exiting the second thread... \n");
                pthread_exit((void*)0);
                return 0;
            }

            if (status != 0) {
                const char* errcode = errcodeIdentifier(status);
                printf("\nBlocked thread error message: %s\n", errcode);
            } else {
                break;
            }
        };

        for (int i = 0; i < 10; ++i) {
            putchar('2');
            fflush(stdout);
            // time in seconds
            sleep(1);
        }

        pthread_mutex_unlock(&mutex);
        sleep(1);
    }
}

int main() {
    printf("Executing main thread... \n");

    // switching the exit signal handler
    signal(SIGINT, sig_handler);

    pthread_t first_thread;
    pthread_t second_thread;

    pthread_mutex_t first_mutex;

    thread_data first_data;
    thread_data second_data;

    first_data.flag = 1;
    second_data.flag = 1;

    printf("Initialization of the mutex... \n");
    pthread_mutex_init(&mutex, NULL);
    
    pthread_create(&first_thread, NULL, coroutine_1, &first_data);
    pthread_create(&second_thread, NULL, coroutine_2, &second_data);

    printf("Main thread is waiting for any key to be pressed... \n");
    getchar();
    printf("\nMain thread has been unlocked... \n");

    // setting up the exit flag
    first_data.flag = 0;
    second_data.flag = 0;
    
    pthread_join(first_thread, NULL);
    pthread_join(second_thread, NULL);
    printf("Both threads have finished their procedures... \n");

    printf("Uninitializing the mutex... \n");
    pthread_mutex_destroy(&mutex);

    printf("Exiting the program...\n");
    return 0;
}
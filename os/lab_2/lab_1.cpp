#include <iostream>
#include <pthread.h>
#include <unistd.h>

typedef struct {
    int flag;
} thread_data;

static void* coroutine_1(void* arg) {
    printf("Entering the first thread... \n");
    int i = 0;

    thread_data* data = (thread_data*) arg;
    while (data->flag != 0) {
        while (i < 10) {
            putchar('1');
            fflush(stdout);
            // time in seconds
            sleep(1);
        }
        sleep(1);
        i = 0;
        
    }

    printf("Exiting the first thread... \n");
    pthread_exit((void*)0);
}

static void* coroutine_2(void* arg) {
    printf("Entering the second thread... \n");
    int i = 0;

    thread_data* data = (thread_data*) arg;
    while (data->flag != 0) {
        while(i < 10) {
            putchar('2');
            fflush(stdout);
            // time in seconds
            sleep(1);
        }
        sleep(1);
        i = 0;
    }

    printf("Exiting the second thread... \n");
    pthread_exit((void*)0);
}

int main() {
    printf("Executing main thread... \n");

    pthread_t first_thread;
    pthread_t second_thread;

    thread_data first_data;
    thread_data second_data;

    first_data.flag = 1;
    second_data.flag = 1;
 
    pthread_create(&first_thread, NULL, coroutine_1, &first_data);
    pthread_create(&second_thread, NULL, coroutine_2, &second_data);

    printf("Main thread is waiting for any key to be pressed... \n");
    getchar();
    printf("\nMain thread has been unlocked... \n");

    first_data.flag = 0;
    second_data.flag = 0;
    
    pthread_join(first_thread, NULL);
    pthread_join(second_thread, NULL);

    printf("Both threads have finished their procedures... \n");
    printf("Exiting the program...\n");
    return 0;
}
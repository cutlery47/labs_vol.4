#include <iostream>
#include <pthread.h>
#include <unistd.h>

typedef struct {
    int flag;
    int exitcode;
} thread_data;

static void* coroutine_1(void* arg) {
    printf("Entering the first thread... \n");

    thread_data* data = (thread_data*) arg;
    while (data->flag != 0) {
        printf("1\n");
        // time in seconds
        sleep(1);
    }

    printf("Exiting the first thread... \n");
    pthread_exit(&data->exitcode);
}

static void* coroutine_2(void* arg) {
    printf("Entering the second thread... \n");

    thread_data* data = (thread_data*) arg;
    while (data->flag != 0) {
        printf("2\n");
        // time in seconds
        sleep(1);
    }

    printf("Exiting the second thread... \n");
    pthread_exit(&data->exitcode);
}

int main() {
    printf("Executing main thread... \n");

    pthread_t first_thread;
    pthread_t second_thread;

    thread_data first_data;
    thread_data second_data;

    first_data.flag = 1337;
    first_data.exitcode = 2674;
    second_data.flag = 228;
    second_data.exitcode = 456;

    pthread_create(&first_thread, NULL, coroutine_1, &first_data);
    pthread_create(&second_thread, NULL, coroutine_2, &second_data);
    
    printf("Main thread is waiting for any key to be pressed... \n");
    getchar();
    printf("Main thread has been unlocked... \n");

    first_data.flag = 0;
    second_data.flag = 0;
    
    int* exitcode_1; 
    int* exitcode_2; 

    pthread_join(first_thread, (void**)&exitcode_1);
    pthread_join(second_thread, (void**)&exitcode_2);

    printf("Both threads have finished their procedures... \n");
    printf("First thread's exit code: %d \n", *exitcode_1);
    printf("Second thread's exit code: %d \n", *exitcode_2);

    return 0;
}
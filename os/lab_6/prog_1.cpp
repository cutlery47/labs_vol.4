#include <pthread.h>
#include <iostream>
#include <unistd.h>
#include <semaphore.h>
#include <fcntl.h>
#include <sys/stat.h>
#include <sys/mman.h>
#include <cstring>
#include <csignal>

#define DATASIZE 256

int exitflag = 0;
const char* sem_read_name = "/read";
const char* sem_write_name = "/write";
const char* shared_name = "/shared";
void* addr = malloc(DATASIZE);

typedef struct {
    sem_t* writer;
    sem_t* reader;
} thread_data;

void sig_handler(int signo) {
    printf("\nget SIGNINT; %d\n", signo);
    exit(0);
}

void* thread_func(void* args) {
    printf("Entering the writer worker thread\n");
    thread_data* data = (thread_data*) args;
    sem_t* writer = data->writer;
    sem_t* reader = data->reader;

    char buffer[DATASIZE];
    while(exitflag == 0) {
        printf("================\n");
        gethostname(buffer, DATASIZE);
        printf("Written message: %s\n", buffer);
        fflush(stdout);
        memcpy(addr, &buffer, DATASIZE);
        sem_post(writer);
        sem_wait(reader);
        sleep(1);
    }

    return 0;
}

int main() {
    printf("Entering the writer program\n");
    pthread_t worker;
    
    signal(SIGINT, sig_handler);

    printf("Creating and mapping shared memory\n");
    int shared_df = shm_open(shared_name, O_RDWR | O_CREAT, S_IRWXU);
    ftruncate(shared_df, DATASIZE);
    addr = mmap(addr, DATASIZE, PROT_READ | PROT_WRITE, MAP_SHARED, shared_df, 0);
    
    sem_t* sem_write = sem_open(sem_write_name, O_CREAT, S_IWUSR | S_IRUSR, 1);
    sem_t* sem_read = sem_open(sem_read_name, O_CREAT, S_IWUSR | S_IRUSR, 1);

    thread_data args;
    args.writer = sem_write;
    args.reader = sem_read;

    pthread_create(&worker, NULL, thread_func, &args);

    getchar();
    exitflag = 1;
    printf("Main thread has been unlocked\n");

    int* exitcode;
    pthread_join(worker, (void**)&exitcode);

    printf("Closing semaphores\n");
    sem_close(sem_write);
    sem_unlink(sem_write_name);

    sem_close(sem_read);
    sem_unlink(sem_write_name);

    munmap(addr, DATASIZE);
    close(shared_df);

    shm_unlink(shared_name);

    printf("Exiting the writer program\n");
    return 0;
}
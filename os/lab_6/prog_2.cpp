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

sem_t* sem_write;
sem_t* sem_read;
int shared_df;

typedef struct {
    sem_t* writer;
    sem_t* reader;
} thread_data;

void* thread_func(void* args) {
    thread_data* data = (thread_data*) args;
    sem_t* writer = data->writer;
    sem_t* reader = data->reader;

    char buffer[DATASIZE];
    while(exitflag == 0) {
        printf("================\n");
        sem_wait(writer);
        memcpy(&buffer, addr, DATASIZE);
        printf("Read data: %s\n", buffer);
        fflush(stdout);
        sem_post(reader);
        sleep(1);
    }

    return 0;
}

void sig_handler(int signo) {
    printf("\nget SIGNINT; %d\n", signo);
    printf("Closing semaphores\n");
    sem_close(sem_write);
    sem_unlink(sem_write_name);

    sem_close(sem_read);
    sem_unlink(sem_read_name);

    munmap(addr, DATASIZE);
    close(shared_df);

    shm_unlink(shared_name);
    exit(0);
}

int main() {
    printf("Entering the reader program\n");
    pthread_t worker;

    signal(SIGINT, sig_handler);
    
    printf("Creating and mapping shared memory\n");
    shared_df = shm_open(shared_name, O_CREAT | O_RDWR, S_IRWXU);
    ftruncate(shared_df, DATASIZE);
    addr = mmap(addr, DATASIZE, PROT_READ | PROT_WRITE, MAP_SHARED, shared_df, 0);
    
    sem_write = sem_open(sem_write_name, O_CREAT, S_IWUSR | S_IRUSR, 0);
    sem_read = sem_open(sem_read_name, O_CREAT, S_IWUSR | S_IRUSR, 0);

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
    sem_unlink(sem_read_name);

    munmap(addr, DATASIZE);
    close(shared_df);

    shm_unlink(shared_name);

    printf("Exiting the writer program\n");
    return 0;
}
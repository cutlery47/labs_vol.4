#include <unistd.h>
#include <pthread.h>
#include <iostream>
#include <semaphore.h>
#include <fcntl.h>
#include <termios.h>

int my_kbhit(void)
{
    struct termios oldt, newt;
    int ch;
    int oldf;
    tcgetattr(STDIN_FILENO, &oldt);
    newt = oldt;
    newt.c_lflag &= ~(ICANON | ECHO);
    tcsetattr(STDIN_FILENO, TCSANOW, &newt);
    oldf = fcntl(STDIN_FILENO, F_GETFL, 0);
    fcntl(STDIN_FILENO, F_SETFL, oldf | O_NONBLOCK);
    ch = getchar();
    tcsetattr(STDIN_FILENO, TCSANOW, &oldt);
    fcntl(STDIN_FILENO, F_SETFL, oldf);
    if(ch != EOF)
    {
        ungetc(ch, stdin);
        return 1;
    }
    return 0;
}

int main() {
    printf("Entering the first program\n");
    const char* semaphore_name = "LeBron_James";
    const char* file_name = "file.txt";

    // unlinking the semaphore, if previous program exited with an error
    sem_unlink(semaphore_name);

    printf("Connecting / openning a semaphore\n");
    sem_t* semaphore = sem_open(semaphore_name, O_CREAT, S_IWUSR | S_IRUSR, 1);
    FILE* file = fopen(file_name, "a+");

    int exit = 0;

    while(exit == 0) {
        sem_wait(semaphore);

        for (int i = 0; i < 10; ++i) {
            fprintf(file, "1");
            printf("1");
            fflush(stdout);
            fflush(file);
            sleep(1);
        }

        sem_post(semaphore);
        exit = my_kbhit();
    }

    printf("Exiting the first program\n");
    fclose(file);
    sem_close(semaphore);
    sem_unlink(semaphore_name);
}
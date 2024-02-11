#include <unistd.h>
#include <iostream>
#include <cstring>
#include <sys/wait.h>
#include <stdlib.h>

extern char** environ;

int main(int argc, char* argv[]) {
    printf("==========================\n");
    printf("Entering the main process\n");

    printf("Forking a new process\n");
    pid_t id = fork();

    if (id == 0) {
        int errcode = setenv("KANYE_WEST", "MUSICAL_GENIUS", true);

        if (errcode != 0) {
            printf("There was an error, when adding a new env. variable...\n");
        }

        execve("./child", argv, environ);
    } else if (id > 0) {
        pid_t self = getpid();
        pid_t parent = getppid();
        printf("Parent's parent process id: %d\n", parent);
        printf("Parent's process id: %d\n", self);
        printf("Parent's child process id: %d\n", id);

        int status = 0;
        while (waitpid(id, &status, WNOHANG) == 0){
            usleep(500000);
        }
        
        printf("Child process has returned with exit code: %d\n", WEXITSTATUS(status));
    } else {
        perror("Error when duplicating processes\n");
    }

    
    printf("Exiting the main process\n");
    return 0;
}
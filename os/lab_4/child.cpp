#include <unistd.h>
#include <iostream>

int main(int argc, char* argv[], char* envp[]) {
    printf("==========================\n");
    printf("Entering the child process\n");
    pid_t self = getpid();
    pid_t parent = getppid();
    int exitcode = 52;

    printf("Child's process id: %d\n", self);
    printf("Child's parent process id: %d\n", parent);

    printf("Printing out the arguments array... \n");
    while (*argv) {
        printf("%s\n", *argv++);
    }

    printf("Printing out the environment variables\n");
    while (*envp) {
        printf("%s\n", *envp++);
    }
    
    printf("Exititng the child process\n");
    printf("==========================\n");
    exit(exitcode);
}
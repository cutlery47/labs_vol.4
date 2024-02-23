#include <iostream>
#include <pthread.h>
#include <vector>
#include <sys/socket.h>
#include <sys/un.h>
#include <unistd.h>
#include <cstring>
#include <cerrno>
#include <csignal>
#include <fcntl.h>

int server_sock;
int client_sock;

int connector_exitflag = 0;
int transmitter_exitflag = 0;
int reader_exitflag = 0;

const char* CLIPATH = "/tmp/cli";
const char* SERPATH = "/tmp/ser";

struct sockaddr_un client_conf;
struct sockaddr_un server_conf;

socklen_t client_confsize;
socklen_t server_confsize;

pthread_t connector_thread;
pthread_t transmitter_thread;
pthread_t reader_thread;


static void* transmit(void* args) {
    int counter = 1;
    
    while (transmitter_exitflag == 0) {
        std::string str_buffer("Message from client: number " + std::to_string(counter));
        send(client_sock, str_buffer.c_str(), str_buffer.length(), 0);

        printf("=====================\n");
        printf("INITIAL REQUEST:\n");
        printf("%s\n", str_buffer.c_str());

        ++counter;
        sleep(1);
    }

    pthread_exit((void *)0);
    return 0;
}

static void* _read(void* args) {
    char buffer[256];

    while (reader_exitflag == 0) {
        int count = recv(client_sock, buffer, sizeof(buffer), MSG_DONTWAIT);
        if (count == -1) {
            sleep(1);
        } else if (count == 0) {
            // shutdown(client_sock, SHUT_RDWR);
            sleep(1);
        } else {
            printf("=====================\n");
            printf("RESPONSE FROM SERVER:\n");
            printf("%s\n", buffer);
        }
    }

    pthread_exit((void *)0);
    return 0;
}

static void* _connect(void* args) {
    socklen_t server_socksize = sizeof(server_conf);
    server_conf.sun_family = AF_UNIX;
    memcpy(server_conf.sun_path, SERPATH, sizeof(SERPATH) + 2);

    while (connector_exitflag == 0) {
        int status = connect(client_sock, (struct sockaddr*)&server_conf, server_socksize);
        perror("connect: ");
        if (status == 0) {
            pthread_create(&transmitter_thread, NULL, transmit, NULL);
            pthread_create(&reader_thread, NULL, _read, NULL);
            pthread_exit((void *)0);
        }
        sleep(1);
    }

    return 0;
}

void sig_handler(int signo) {
    printf("\nget SIGNINT; %d\n", signo);
    printf("Closing the cient...\n");

    shutdown(client_sock, SHUT_RDWR);
    perror("shutdown: ");

    close(client_sock);
    perror("close: ");

    unlink(CLIPATH); 
    perror("unlink:");
    exit(0);
}

int main() {
    printf("Entering the server program\n");
    signal(SIGINT, sig_handler);

    printf("Configuring client socket\n");
    client_confsize = sizeof(client_conf);
    memset(&client_conf, 0, client_confsize);
    client_conf.sun_family = AF_UNIX;
    memcpy(client_conf.sun_path, CLIPATH, sizeof(CLIPATH));

    client_sock = socket(AF_UNIX, SOCK_STREAM, 0);

    // setting up socket parameters
    int optval = 1;
    setsockopt(client_sock, SOL_SOCKET, SO_REUSEADDR, &optval, sizeof(optval));
    fcntl(client_sock, F_SETFL, O_NONBLOCK);

    bind(client_sock, (struct sockaddr*)&client_conf, client_confsize);
    perror("bind: ");

    // just for a confirmation that socket is set up propetly
    getsockname(client_sock, (struct sockaddr*)&client_conf, &client_confsize);
    printf("client addr: %s\n", client_conf.sun_path);

    pthread_create(&connector_thread, NULL, _connect, NULL);

    printf("The main thread is waiting for any key to be pressed...\n");
    getchar();
    printf("The main thread has been unblocked!\n");

    connector_exitflag = 1;
    transmitter_exitflag = 1;
    reader_exitflag = 1;

    printf("Terminating connector\n");
    pthread_join(connector_thread, NULL);
    printf("Terminating reader\n");
    pthread_join(reader_thread, NULL);
    printf("Terminating transmitter\n");
    pthread_join(transmitter_thread, NULL);

    shutdown(client_sock, SHUT_RDWR);
    //close(client_sock);
    unlink(CLIPATH);

    printf("Exiting the program\n");
    sleep(1);

    return 0;
}

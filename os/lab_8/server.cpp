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
#include <queue>

std::queue<const char*> queue;

int client_sock;
int acceptor_sock;

int listener_exitflag = 0;
int handler_exitflag = 0;
int acceptor_exitflag = 0;

const char* SOCKPATH = "/tmp/ser";

struct sockaddr_un client_conf;
struct sockaddr_un acceptor_conf;

pthread_mutex_t queue_mutex;

pthread_t listener_thread;
pthread_t handler_thread;
pthread_t acceptor_thread;

static void* listen(void* args) {
    char buffer[256];
    while (listener_exitflag == 0) {
        int count = recv(client_sock, buffer, 256, MSG_DONTWAIT);
        if (count <= 0) {
            if (count == 0) {
                printf("fucking here");
                sleep(1);
            }
            sleep(1);
        } else {
            pthread_mutex_lock(&queue_mutex);
            queue.push(buffer);
            pthread_mutex_unlock(&queue_mutex);
        }
    }
    
    pthread_exit((void *)0);
    return 0;
}

static void* handle(void* args) {
    char hostname[256];
    while (handler_exitflag == 0) {
        pthread_mutex_lock(&queue_mutex);
        if (!queue.empty()) {
            std::string request(queue.front());
            pthread_mutex_unlock(&queue_mutex);

            printf("=====================\n");
            printf("REQUEST FROM CLIENT:\n");
            printf("%s\n", request.c_str());

            gethostname(hostname, 256);
            std::string response(request + "\nResponse data: " + hostname);

            int count = send(client_sock, response.c_str(), response.length(), 0);
            //perror("send");            
        } 
        sleep(1);
        
    }
    pthread_exit((void *)0);
    return 0;
}

static void* _accept(void* args) {
    socklen_t client_confsize = sizeof(client_conf);

    while (acceptor_exitflag == 0) {
        client_sock = accept(acceptor_sock, (struct sockaddr*)&client_conf, &client_confsize);
        if (client_sock < 0) {
            sleep(1);
        } else {
            printf("client addr: %s\n", client_conf.sun_path);
            pthread_create(&listener_thread, NULL, listen, NULL);
            pthread_create(&handler_thread, NULL, handle, NULL);
            pthread_exit((void *)0);
        }
    }
    return 0;
}

void sig_handler(int signo) {
    printf("\nget SIGNINT; %d\n", signo);
    printf("Closing the server...\n");

    shutdown(acceptor_sock, SHUT_RDWR);
    perror("shutdown: ");

    close(acceptor_sock);
    perror("close: ");

    unlink(SOCKPATH); 
    perror("unlink:");
    exit(0);
}

int main() {
    printf("Entering the server program\n");
    signal(SIGINT, sig_handler);

    printf("Configuring acceptor socket\n");
    memset(&acceptor_conf, 0, sizeof(acceptor_conf));    
    acceptor_conf.sun_family = AF_UNIX;
    memcpy(acceptor_conf.sun_path, SOCKPATH, sizeof(SOCKPATH));

    acceptor_sock = socket(AF_UNIX, SOCK_STREAM, 0);
    perror("socket: ");

    int optval = 1;
    setsockopt(acceptor_sock, SOL_SOCKET, SO_REUSEADDR, &optval, sizeof(optval));
    perror("options: ");

    fcntl(acceptor_sock, F_SETFL, O_NONBLOCK);

    bind(acceptor_sock, (struct sockaddr*)&acceptor_conf, sizeof(acceptor_conf));
    perror("bind: ");

    listen(acceptor_sock, 2);
    perror("listen: ");

    pthread_create(&acceptor_thread, NULL, _accept, NULL);
    
    usleep(100000);
    printf("The main thread is waiting for any key to be pressed...\n");
    getchar();
    printf("The main thread has been unblocked\n");

    listener_exitflag = 1;
    acceptor_exitflag = 1;
    handler_exitflag = 1;

    printf("Terminating acceptor\n");
    pthread_join(acceptor_thread, NULL);
    printf("Terminating listener\n");
    pthread_join(listener_thread, NULL);
    printf("Terminating handler\n");
    pthread_join(handler_thread, NULL);

    shutdown(acceptor_sock, SHUT_RDWR);
    //close(acceptor_sock);
    unlink(SOCKPATH);

    printf("Exiting the program\n");
    sleep(1);
    
    return 0;
}
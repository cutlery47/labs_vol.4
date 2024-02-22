g++ -c client.cpp;

g++ -o client client.o -lpthread;

gnome-terminal -- ./client;

rm ./client.o;
# rm ./client;
#rm /tmp/cli;
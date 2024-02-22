g++ -c server.cpp;

g++ -o server server.o -lpthread;

gnome-terminal -- ./server;

rm ./server.o;
# rm ./server;
#rm /tmp/ser;
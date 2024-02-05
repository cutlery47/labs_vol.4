#! /bin/bash

echo "Compiling"
g++ -c lab.cpp 

echo "Adding libraries"
g++ -o lab lab.o -lpthread

echo "Adding permissions"
chmod +x ./lab.sh

echo "Starting up the program"
./lab

echo "Bye-bye"

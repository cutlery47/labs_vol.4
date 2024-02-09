#! /bin/bash

echo "Compiling"
g++ -c lab.cpp 

echo "Adding libraries"
g++ -o lab lab.o -lpthread

echo "Starting up the program" 
./lab $1

echo "Bye-Bye"
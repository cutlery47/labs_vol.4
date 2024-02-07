#! /bin/bash

echo "Compiling"
g++ -c lab_1.cpp 

echo "Adding libraries"
g++ -o lab_1 lab_1.o -lpthread

echo "Starting up the program"
./lab_1

echo "Bye-bye"
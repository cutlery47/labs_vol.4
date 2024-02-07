#! /bin/bash

echo "Compiling"
g++ -c lab_3.cpp 

echo "Adding libraries"
g++ -o lab_3 lab_3.o -lpthread

echo "Starting up the program"
./lab_3

echo "Bye-bye"
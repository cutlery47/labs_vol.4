#! /bin/bash

echo "Compiling"
g++ -c lab_2.cpp 

echo "Adding libraries"
g++ -o lab_2 lab_2.o -lpthread

echo "Starting up the program"
./lab_2

echo "Bye-bye"
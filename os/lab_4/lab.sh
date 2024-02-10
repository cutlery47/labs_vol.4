#!/bin/bash

echo "Compiling parent"
g++ -c parent.cpp 

echo "Compiling child"
g++ -c child.cpp

echo "Adding parent libraries"
g++ -o parent parent.o -lpthread

echo "Adding child libraries"
g++ -o child child.o -lpthread

echo "Please, enter some arguments (in a single line, using "space" as a separator) ..."
read args

echo "Starting up the program"
./parent $args

echo "Bye-Bye"
#! /bin/bash

echo "Compiling reader program"
g++ -c reader.cpp

echo "Compiling writer program"
g++ -c writer.cpp 

echo "Adding first program libraries"
g++ -o reader reader.o -lpthread

echo "Adding second program libraries"
g++ -o writer writer.o -lpthread

echo "Starting both programs"
gnome-terminal -- ./reader && gnome-terminal -- ./writer

echo "Cleaning up"
rm ./writer.o
rm ./reader.o
rm ./writer
rm ./reader


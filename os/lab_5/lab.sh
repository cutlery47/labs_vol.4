#! /bin/bash

echo "Compiling first program"
g++ -c prog_1.cpp

echo "Compiling second program"
g++ -c prog_2.cpp 

echo "Adding first program libraries"
g++ -o prog_1 prog_1.o -lpthread

echo "Adding second program libraries"
g++ -o prog_2 prog_2.o -lpthread

echo "Starting both programs"
gnome-terminal -- ./prog_2 && gnome-terminal -- ./prog_1 && gnome-terminal -- tail -f file.txt

echo "Bye-Bye"

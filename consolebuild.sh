#!/bin/bash
# build the command line version of PintoSim
dir1=consolebuild/maps
dir2=consolebuild/src
function build() {
    mkdir consolebuild/
    mkdir consolebuild/maps
    mkdir consolebuild/src
    for file in `echo "maps/"`; do
cp -r $file $dir1
    done
for file in `echo "src/pintosim/"`; do
cp -r $file $dir2
    done
javac -d consolebuild src/pintosim/*.java
}

function run() {
    cd consolebuild/
    java pintosim.PintoSim
}

build
run
source ../runwithanimation.sh

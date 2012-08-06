#!/bin/bash
# builds the graphical user interface version of PintoSim
# chmod + x and then source this file to run GUISim
dir1=guibuild/maps
dir2=guibuild/src
dir3=guibuild/graphics
function build() {
    mkdir guibuild/
    mkdir guibuild/maps
    mkdir guibuild/src
    for file in `echo "maps/"`; do
        cp -r $file $dir1
    done
    for file in `echo "src/pintosim/"`; do
        cp -r $file $dir2
    done
    for file in `echo "graphics/"`; do
        cp -r $file $dir3
    done
    cp config.properties guibuild/
    javac -d guibuild src/pintosim/*.java
}

function run() {
    cd guibuild/
    java pintosim.GUISim
}

build
run

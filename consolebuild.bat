rem    this file compiles the sources into a new directory, and copies other resources into that dir too
rem    its a build script, making it easy to run the project from a real command line terminal
rmdir consolebuild /s /q
mkdir consolebuild
mkdir consolebuild\maps
mkdir consolebuild\src
xcopy maps consolebuild\maps /E
xcopy src consolebuild\src /E
xcopy USAGE.txt consolebuild
javac -d consolebuild src\pintosim\*.java
cd consolebuild
java pintosim.PintoSim

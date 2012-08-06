rem    this file compiles the sources into a new directory, and copies other resources into that dir too
rem    its a build script, making it easy to run the project from a real command line terminal
rmdir consolebuild /s /q
mkdir consolebuild
mkdir consolebuild\maps
mkdir consolebuild\src
mkdir consolebuild\src\pintosim
mkdir consolebuild\graphics
xcopy maps consolebuild\maps /E
xcopy src consolebuild\src /E
xcopy src consolebuild\graphics /E
xcopy README.txt consolebuild
xcopy config.properties consolebuild
javac -d consolebuild -encoding utf8 src\pintosim\*.java


start "Animation" gui.bat

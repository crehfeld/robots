Team name: plz send teh codez
Project: My Friendly Pintos Assistive Robots


The core functionality of the system is based around:
-telling the system where an item is
-asking it to then retrieve it for you

In our system, pinto robots wait at a "pinto docking station", idling, waiting for an item retrieval request.
The robots will take the *shortest available path* to the location of the item, and then again to
 the location of the person, and then back to the docking station.

Because there's no gui yet, it would be difficult to show the progress of the robot as it
gradually moves along a path. Also, trying to show how multiple robots navigate without 
colliding is also difficult, as paths can cross(or be exactly the same) without robot collision.
These are best demonstrated when a gui is available, so for this version, we just trace out 
the path the robot takes. 


Sample Interaction:
If you want to compile it yourself, cd to the 'consolebuild' directory, then do
javac -d pintosim src/pintosim/*.java

run the program from the command line by cd'ing to the 'consolebuild' directory, then do
>java pintosim.PintoSim





>system, show map
(this will show you a map of the house. a legend is included)
>system, note the location of prunes at 1,5
(this will record the location of an item named "prunes")
>system, show map
(you can see the item was added at the indicated positon, where the "I" character now exists. coordinate system used is 0,0 is top left.)
>system, get me prunes
(asks the system to bring the prunes to you. it will take a short moment, but you can still interact while waiting)

after the prunes are delivered, you will be notified, and shown
1) a the map of the house,
2) a map that shows the path that the pinto robot took to complete the task. 

The path starts at the docking station, goes to the item, 
then goes to the elderly persons location, then back to the docking station. 
Note that the path is optimal for that sequence.

While waiting for the item to be delivered(you may have up to 30 seconds), 2 other command become interesting:
>system, what is the status of item prunes
(will show you the status)
>system, cancel item prunes
(will prevent the delivery of the item)





Another interaction is to send a message to the help desk, although this isnt very interesting.
>system, send a message to the help desk


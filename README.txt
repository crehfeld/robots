Team name: plz send teh codez
Project: My Friendly Pintos Assistive Robots

Because the purpose of assignment #4 was to implement smart algorithms, this demo is tailored to demonstrating that.
The system fully supports all commands detailed in the technical document, and in particular the use cases,
however, most of it is boring and wont be covered here because I know you dont care.


The core functionality of the system is based around having robots bring items to you.
In our system, pinto robots wait at a "pinto docking station", idling, waiting for an item retrieval request.
The robots will take the *shortest available path* to the location of the item, and then again to
 the location of the person, and then back to the docking station. Under near-collision scenarios,
 the alternate path used to avoid collision may not be optimal(shortest), but will generally be very close.
 
In our model, only one thing can be on any given space at a time. 
The exception is an Item- they simply don't occupy a space at all. In fact, multiple 
items can even be on the same space. 

 
First, to understand the map we print to your screen, this is the map legend:
	D = The pinto robot docking station(where the robots return to idle)
	P = A pinto robot
	E = A Person
	X = A wall or other permanent obstruction
	I = An item
	. = Unnocupied space
	
	


 

***HOW TO USE IT***
	Execute the consolebuild.bat script. This will compile the sources, and then execute the program. 
	    (java and javac must be in your path!)
		
	Two command windows will open:
	  1) A shell window for user interaction 
	  2) Another shell window is used to display an psuedo animated map.
	  
		  -We just keep reprinting the map and let the console window scroll to provide the animation effect.
		  You may need to enlarge the window in order to see the animation effect. 

		  
	**If you want to re execute the application, make sure to first close both shell windows!**
  




5 items are preloaded into the map for you. For brief typing, they're named a1, a2, a3, a4, a5

To tell a robot to bring you an item named a1:
	>system, get me a1

	You should see a robot move towards an item, pick it up, drop it off at the person, and then return home.
	If you quickly request many items, you can see how the robots avoid each other when they 
	naviagte through choke points in the map:
	>system, get me a1
	>system, get me a2
	>system, get me a3
	>system, get me a4
	>system, get me a5

	This demo uses 4 robots. If you issue 5 item requests rapidly, the 5th request will be queued, 
	and executed seamlessly as soon as one of the robots finishes its current task.

	Note- even after an item is delivered, you can still request its retrieval again. 
	We left this functionality in because it makes for a good way to see all the robots 
	cause congestion around the same location. Just keep in mind the robot basically 
	just walks up to the item, picks it up, and then realizes its at its destination and puts 
	the item down again, then goes home.




At any time, you can cancel an item retrieval request:
	>system, cancel item a1
	The system will respond with an appropriately tailored message depending on the MANY states of an 
	item retrieval request. Perhaps the most interesting is canceling while the robot is carrying the item.
	The system will ask for confirmation, and the robot in question will pause movement while awaiting your reply. 
	Other robots continue as normal during this confirmation state.


If you would like to add your own item named lazer gun:
	>system, note the location of lazer gun at 20,2
	top left of the map is coordinate 0,0

	Then you can retrieve it
	>system, get me lazer gun

	
	
	
	
***KNOWN PROBLEMS***
The command line animation window flickers sometimes. Its really annoying. Sorry! 
We didnt want to spend too much time forcing some archaic console shell into doing something it wasnt 
really designed to do. When we animate via a gui it will be nicer, we promise.
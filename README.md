#Project 3: Better Inverted Index

* Authors: Kyle Cummings, Zach Goodspeed
* Class: CS430

##Discussion

First off, we did not develop in Eclipse. We developed strictly using Bash and Vim,
code our program.

This project was fairly straight forward once we grasped the concept of Mapping
and Reducing within InvertedIndex.java. We didn't do anything to the mapper class,
instead we used a key value setup where the value was a string of the filename and
how many times that key showed up in the file. These were then sent to a HashMap as
a key value pair, and then the map was sorted with our own algorithm comparing the
string and count within the string to organize the formatting of the output.

The opitmizations sped up the program a bit. Everything was tuned in the config files
from the class resources repo with new parameters for optimization.

All in all it was fairly simple, we had our code functional against Amit's text
database before thanksgiving, and optimized and tested it in a couple hours
after break and after receiving the updated cluster scripts.

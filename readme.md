# FRC team 620's 2018 robot (Archived)

### Source code for the 2018 comp robot: SpearHead




## Game

The Game for 2018 is called Power Up, where teams are required to place milk crates, or "power cubes", on large balancing scales to tip the scale and gain ownership. Alliances can also trade power cubes for power ups, giving them a temporary advantage in a match. At the end of the match, robots can climb the tower attached to the center balancing scale using a rung attached to the tower, giving them additional points. Points are given by the amount of time each team has owned a switch.

## Unique features

This years robot's unique features include:

- Double elevator with climbing system
- Two stage pneumatic grabber
- West Coast Drive
- ~~Turn Table~~ (Snapped in Half at Comp, Replaced with fixed Grabber)
- Two high Volume low weight aluminum air tanks.
- Three custom CNC gearboxes. (drivetrain and elevator)

## Goals of the year

|Status|Goal|Additional Description|
|------|----|----------------------|
|Yes|Two Driver Control| Allow two driver control with hot swappable control schemes.|
|Yes|Module loading system| Custom core api that allows loading of Modules like the elevator or grabber. To allow us to run simulation without actual hardware by just adding or removing Modules.|
|Yes|cad based autonomous | Auto cords are field centric vs starting at the robotics location. Allowing us to create and view paths in cad and use the measurements in code.|
|Yes|Turn interpolation and waypoint setting | Auto is able to bank/drift turns without stopping. To improve auto speed. |
|Yes|Driver aid and automation | Use odometry to automate driver tasks and account for field imperfections. Examples being drive straight, intake powercube etc.)|
|Yes|Use Calculus to interpolate data between Navx sensor output. | used Calculus to get more detailed data from Navx and interpolate data based off of time. |

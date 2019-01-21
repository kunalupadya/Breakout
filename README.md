game
====

This project implements the game of Breakout.

Name: 

### Timeline

Start Date: 1/15/19

Finish Date: 1/21/19

Hours Spent: 30

### Resources Used
Used javafx documentation

### Running the Program

Main class: main is the main class, it depends on block, ball, paddle, and powerup.

Data files needed: The data files are located in resources. They include the bricks, powerups, and paddle.  

Key/Mouse inputs: This program takes a mouse input to start the game, takes left and right inputs to move the paddle, spacebar to start a new game when the previous one is finished, 1 to break all blocks and go to the next level, 2 to toggle increased paddle speed, 3 to increase ball size, 4 to have the bomb powerup, and 5 to activate double popper

Cheat keys:3 to increase ball size, 4 to have the bomb powerup, and 5 to activate double popper

Known Bugs: The ball sometimes gets stuck on the triangles and walls (an increase in ball size can cause this). The ball also sometimes instantly breaks a block when it hits from the side. I have implemented logic to make the ball bounce properly, but it still fails sometimes. When this happens, the ball gets stuck in the block, and pops the block repeatedly. Also, the final screen (game over) sometimes fails unexpectedly. I am unsure as to why this is, but it flashes and takes a while to activate. In this situation, you just have to hit the spacebar a few times and wait a second.

Extra credit: I added rotators to reverse the direction of the ball, and a score multiplier to increase the score when you have gone a while without dying. 


### Impressions
Generally, the game works quite well. The scene switches very well, and each individual level is well designed. The third level is quite difficult, and I like the way it plays. One of the main issues with the game is the predictability of bounces, this leads to situations with the ball bouncing off of rotators in an infinite loop. If given more time, I would fix this by slightly randomizing the bounces off of rotators. I would also fix the issues with the ball incorrectly bouncing off of blocks on the sides.
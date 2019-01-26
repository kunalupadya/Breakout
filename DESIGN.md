game
===
Kunal Upadya (ku16)

###Design Goals
My project was designed to create a version of the popular game Breakout. To do this, I separated the game into 6 different classes, all with different roles. 

* The Block class was used to store block objects (their location, powerup stored, and state), spawn powerups, hit blocks, and break blocks. 
* The Ball class was used to store the location and size of the ball. It was also used to move the ball and bounce it off of walls, blocks, and the paddle. This class also handles resetting the location of 
the ball when it is dead, as well as storing the strength of the ball's popping.
* The Paddle class stores the location of the paddle, and controls paddle movement.
* The PowerupSprite class stores the location and powerup type of an onscreen powerup as it floats to the bottom of the screen. It also controls powerup movement, and returns its powerup type when hit.
* The ActivePowerup class is used to store powerups after they have been created, so that they can be activated for a certain amount of seconds. The class stores its activation time and its powerup type.
* The main function is used to control aspects of the game simulation. It sets up levels, determines what is onscreen, switches scenes, and controls the instances of other objects.

###Adding New Features
To add new features, one must first think about what the new feature does. If the new feature is a new powerup, then the new powerup type must be added into the powerups available in the PowerupSprite class. Implementations of the powerup's effects must be added into the activatePowerups method in main. If the new feature is a new level, then it can be added by creating a new setupLevel method in main and changing changeSceneIfTriggered to switch to the new scene. To change the increase in multiplier, one must change the setmultiplier method.

###Justification of Major Design Choices
One major design choice was the use of a multiplier of a set parameter to implement the increase size powerup. Increasing powerup size could have been accomplished by inputting the specific size multiplier of the ball and multiplying by that, but instead I chose to have the two ball sizes as public static final methods which could be called as the parameter. I feel that this clears up any possible confusion resulting from whether the ball size is reverted back to normal by calling changeBallSize with a parameter of 1 or if it must be called by multiplying by the multiplicative inverse of its current size divided by the original size of the ball.

The choice to use two separate powerup classes was difficult, but necessary. The use of two powerup classes allows for better implementation of powerups, as the powerups that are onscreen are essentially only sprites storing a powerup type, while the ActivePowerups are not sprites and instead handle the activation and deactivation of powerups. By separating the two classes, I was able to more easily kill sprite instances after they hit the paddle, and to more easily track and kill ActivePowerups after they expire.

The last major design choice was the choice to handle bouncing in the main class. For the bouncing, it made sense to check the intersection of the ball against each other object individually, instead of storing all of the intersections in the ball, as this was something that changed constantly and I did not want to be feeding each individual object into the ball as parameters. I feel that this decision was the correct one as it leads to clean snippets of code such as 
```java
    private void handleBouncingOffPaddle() {
        if (myBall.getView().getBoundsInParent().intersects(myPaddle.getView().getBoundsInParent())) {
            myBall.bounceOffPaddle(myPaddle);
        }
    }
```
where the intersection between two objects was checked in Main, and if they intersected then the ball would be told to react appropriately. I feel that this code is also quite elegant and easy to understand.

###Assumptions Made
The main assumption I made while making this game was that the score should increase by one for every hit, instead of for every pop. This affects the game by making it much easier to get the multiplier that makes high scores possible. In my opinion, it makes the game much more fun. I also assumed that blocks were static, and did not move. There is no way to currently move blocks in my game. Further, the ball bounces in a very predictable manner as I did not code any randomness into bounces. In the future, this may be an interesting change to the game. 
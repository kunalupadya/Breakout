package sample;

import java.util.Random;

import javafx.geometry.Point2D;
import javafx.scene.Node;
import javafx.scene.image.Image;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;


/**
 * Creates a ball class to control the position and qualities of the ball
 *
 * @author Kunal Upadya
 */
public class Ball {
    public static final int START_SPEED = 8;
    public static final int SIZE = 10;
    public static final int MIN_ANGLE = 20;
    public static final int MAX_ANGLE = 160;
    public static final int SMALL_BALL_SIZE = 1;
    public static final int BIG_BALL_SIZE = 2;

    private Circle myView;
    private Point2D myVelocity;
    private Random dice = new Random();
    private int popsPerHit = 1;


    /**
     * This constructor makes the ball appear in the center of the screen, above the paddle height. It shoots at a random angle
     * @param image
     * @param screenWidth
     * @param screenHeight
     */
    public Ball(Image image, int screenWidth, int screenHeight){
        myView = new Circle();
        myView.setRadius(SIZE);
        myView.setFill(new ImagePattern(image));
        // sets initial ball position to center of stage, above paddle, with a random velocity
        resetBall(screenWidth,screenHeight);
    }

    private void setMyVelocity(double multiplier){
        myVelocity = new Point2D(myVelocity.getX()*multiplier, myVelocity.getY()*multiplier);
    }

    /**
     * moves the ball based on the velocity of the ball.
     * @param elapsedTime
     */
    public void move(double elapsedTime){
        myView.setCenterX(myView.getCenterX()+myVelocity.getX());
        myView.setCenterY(myView.getCenterY()+myVelocity.getY());
    }

    /**
     * bounce off the paddle, used when the ball hits the paddle in any fashion.
     * @param paddle
     */
    public void bounceOffPaddle(Paddle paddle){
        boolean toLeftOfPaddle = (myView.getCenterX()< paddle.getxPos()-myView.getRadius()*3/4);
        boolean toRightOfPaddle = (myView.getCenterX()> paddle.getxPos()+myView.getRadius()*3/4+Paddle.PADDLE_LENGTH);
        if (toRightOfPaddle||toLeftOfPaddle){
            myVelocity = new Point2D(-myVelocity.getX(), myVelocity.getY());
        }
        else {
            myVelocity = new Point2D(myVelocity.getX(), -myVelocity.getY());
        }
    }

    /**
     * bounce off the block, used when the block hits the ball in any fashion
     * @param block
     */
    public void bounceOffBlock(Block block){
        boolean toLeftOfBlock = (myView.getCenterX()< block.getXPosition()-myView.getRadius()*3/4);
        boolean toRightOfBlock = (myView.getCenterX()> block.getXPosition()+myView.getRadius()*3/4+block.getBlockLength());


        if (toLeftOfBlock||toRightOfBlock){
            myVelocity = new Point2D(-myVelocity.getX(), myVelocity.getY());
        }
        else {
            myVelocity = new Point2D(myVelocity.getX(), -myVelocity.getY());
        }
    }

    /**
     * bounce off the triangle, used when the ball touches the triangle
     * @param timeLastTriangleHit
     */
    public void bounceOffTriangle(long timeLastTriangleHit){
        if (System.currentTimeMillis() - timeLastTriangleHit >100) {
            myVelocity = new Point2D(myVelocity.getX(), -myVelocity.getY());
        }
    }

    /**
     * bounce off the rotator, reverse in the direction the ball came from, used when the ball touches the rotator
     * @param timeLastRotatorHit
     */
    public void bounceOffRotator(long timeLastRotatorHit){
        if (System.currentTimeMillis() - timeLastRotatorHit >100) {
            myVelocity = new Point2D(-myVelocity.getX(), -myVelocity.getY());
        }
    }

    /**
     * bounces off walls, used when the ball touches the wall
     * @param screenWidth
     * @param screenHeight
     */
    public void bounceOffWalls(double screenWidth, double screenHeight) {
        // collide all bouncers against the walls

        if (myView.getCenterX() - myView.getRadius()< 0 || myView.getCenterX() + myView.getRadius() > screenWidth) {
            myVelocity = new Point2D(-myVelocity.getX(), myVelocity.getY());
        }
        if (myView.getCenterY() - myView.getRadius() <= 0) {
            myVelocity = new Point2D(myVelocity.getX(), -myVelocity.getY());
        }
    }

    /**
     * resets the ball if dead, returns whether or not the ball was dead
     * @param screenWidth the width of the screen
     * @param screenHeight the height of the screen
     * @return boolean saying if
     */
    public boolean resetBallIfDead(double screenWidth, double screenHeight){
        if (myView.getCenterY() +myView.getRadius() > screenHeight) {
            resetBall(screenWidth, screenHeight);

            return true;
        }
        return false;
    }

    /**
     * changes the ball size to a specified multiplier of its original size, used when the increase ball size powerup is activated and deactivated
     * Use NORMAL_BALL_SIZE and BIG_BALL_SIZE as the ballSize param
     * @param ballSize
     */
    public void changeBallSize(double ballSize) {
        myView.setRadius(SIZE*ballSize);
    }

    /**
     * resets the ball location when the ball dies
     * @param screenWidth
     * @param screenHeight
     */
    private void resetBall(double screenWidth, double screenHeight){
        myView.setCenterX((int)(screenWidth/2));
        myView.setCenterY((int)(screenHeight-screenHeight/10)-40);
        double startAngle = getRandomAngleInRange(MIN_ANGLE,MAX_ANGLE);
        myVelocity = new Point2D(Math.cos(Math.toRadians(startAngle))*START_SPEED,
                -Math.sin(Math.toRadians(startAngle))*START_SPEED);
    }

    /**
     * change changes the number of blocks that are popped with a hit
     * @param popStrength
     */
    public void changePopStrength(int popStrength){
        popsPerHit = popStrength;
    }

    /**
     * returns a node object of the ball
     * @return
     */
    public Node getView(){
        return myView;
    }

    /**
     * returns a circle shape object of the ball for use with triangle object collision detection
     * @return
     */
    public Circle getShape(){
        return myView;
    }

    /**
     * gets the number of pops per hit of the ball, used when the block is hit
     * @return
     */
    public int getPopsPerHit() {
        return popsPerHit;
    }

    /**
     * Returns an int between the min and max
     * @param min
     * @param max
     * @return an int
     */
    private int getRandomAngleInRange(int min, int max){
        return min + dice.nextInt(max - min) + 1;
    }
}

package sample;

import java.util.Random;

import javafx.geometry.Point2D;
import javafx.scene.Node;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class Ball {
    public final int START_SPEED = 8;
    public final int SIZE = 20;
    public final int MIN_ANGLE = 20;
    public final int MAX_ANGLE = 160;




    private ImageView myView;
    private Point2D myVelocity;
    private Random dice = new Random();

    public Ball(Image image, int screenWidth, int screenHeight){
        myView = new ImageView(image);
        myView.setFitHeight(SIZE);
        myView.setFitWidth(SIZE);
        // sets initial ball position to center of stage, above paddle
        resetBall(screenWidth,screenHeight);
    }

    private void setMyVelocity(double multiplier){
        myVelocity = new Point2D(myVelocity.getX()*multiplier, myVelocity.getY()*multiplier);
    }

    public void move(double elapsedTime){
        myView.setX(myView.getX()+myVelocity.getX());
        myView.setY(myView.getY()+myVelocity.getY());
    }

    public void bounceOffPaddle(){
        myVelocity = new Point2D(myVelocity.getX(), -myVelocity.getY());
    }

    public void bounceOffWalls(double screenWidth, double screenHeight) {
        // collide all bouncers against the walls
//        System.out.print("X: ");
//        System.out.println(myView.getX());
//        System.out.print("Y: ");
//        System.out.println(myView.getY());
//        System.out.println(myVelocity.getY());
        if (myView.getX() < 0 || myView.getX() > screenWidth - myView.getBoundsInLocal().getWidth()) {
            myVelocity = new Point2D(-myVelocity.getX(), myVelocity.getY());
        }
        if (myView.getY() < 0) {
            myVelocity = new Point2D(myVelocity.getX(), -myVelocity.getY());
        }
    }

    public boolean isBallDead(double screenWidth, double screenHeight){
        if (myView.getY() > screenHeight - myView.getBoundsInLocal().getHeight()) {
            resetBall(screenWidth, screenHeight);
            return true;
        }
        return false;
    }

    public void changeBallSize(double multiplier) {
        myView.setFitHeight(SIZE*multiplier);
        myView.setFitWidth(SIZE*multiplier);
    }

    private void resetBall(double screenWidth, double screenHeight){
        myView.setX((int)(screenWidth/2));
        myView.setY((int)(screenHeight-screenHeight/10)-40);
        double startAngle = getRandomInRange(MIN_ANGLE,MAX_ANGLE);
        myVelocity = new Point2D(Math.cos(Math.toRadians(startAngle))*START_SPEED,
                -Math.sin(Math.toRadians(startAngle))*START_SPEED);
    }

    public Node getView(){
        return myView;
    }

    /**
     * Returns an int between the min and max
     * @param min
     * @param max
     * @return an int
     */
    private int getRandomInRange(int min, int max){
        return min + dice.nextInt(max - min) + 1;
    }
}

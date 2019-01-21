package sample;

import javafx.geometry.Point2D;
import javafx.scene.Node;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import static sample.Block.INCREASE_BALL_SIZE;
import static sample.Block.BOMB;
import static sample.Block.DOUBLE_POPPER;

/**
 * the powerup class is used to define the powerups as they move on the screen
 */
public class Powerup {

    public static final int SIZE = 20;


    private int powerupType;
    private ImageView myView;
    private Point2D myVelocity = new Point2D(0,5);

    /**
     * sets the location of the powerup to the center of the block it was popped from
     * @param image
     * @param xPos
     * @param yPos
     * @param powerupType
     */
    public Powerup(Image image, int xPos, int yPos, int powerupType){
        myView = new ImageView(image);
        myView.setFitHeight(SIZE);
        myView.setFitWidth(SIZE);
        myView.setX(xPos-myView.getBoundsInLocal().getWidth()/2);
        myView.setY(yPos-myView.getBoundsInLocal().getHeight()/2);
        this.powerupType = powerupType;
    }

    /**
     * moves the powerup down at constant speed
     * @param elapsedTime
     */
    public void move(double elapsedTime){
        myView.setX(myView.getX()+myVelocity.getX());
        myView.setY(myView.getY()+myVelocity.getY());
    }

    /**
     * returns the node object of the powerup
     * @return
     */
    public Node getView(){
        return myView;
    }

    /**
     * returns the type of powerup stored when it hits the paddle
     * @return
     */
    public int getPowerupType() {
        return powerupType;
    }

    /**
     * kills the powerup when it hits the paddle
     */
    public void killPowerup(){
        myView.setImage(null);
    }

    /**
     * checks if the powerup is off the screen
     * @param screenHeight
     * @return
     */
    public boolean isOffScreen(int screenHeight){
        if (myView.getY() > screenHeight - myView.getBoundsInLocal().getWidth()) {
            return true;
        }

        return false;
    }
}

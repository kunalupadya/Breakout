package sample;

import javafx.geometry.Point2D;
import javafx.scene.Node;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import static sample.Block.INCREASE_BALL_SIZE;
import static sample.Block.BOMB;
import static sample.Block.DOUBLE_POPPER;

public class Powerup {

    public final int SIZE = 20;


    private int powerupType;
    private ImageView myView;
    private Point2D myVelocity = new Point2D(0,5);

    public Powerup(Image image, int xPos, int yPos, int powerupType){
        myView = new ImageView(image);
        myView.setFitHeight(SIZE);
        myView.setFitWidth(SIZE);
        myView.setX(xPos-myView.getBoundsInLocal().getWidth()/2);
        myView.setY(yPos-myView.getBoundsInLocal().getHeight()/2);
        this.powerupType = powerupType;
    }

    public void move(double elapsedTime){
        myView.setX(myView.getX()+myVelocity.getX());
        myView.setY(myView.getY()+myVelocity.getY());
    }

    public Node getView(){
        return myView;
    }

    public int getPowerupType() {
        return powerupType;
    }
    public void killPowerup(){
        myView.setImage(null);
    }
    public boolean isOffScreen(int screenHeight){
        if (myView.getY() > screenHeight - myView.getBoundsInLocal().getWidth()) {
            return true;
        }

        return false;
    }
}

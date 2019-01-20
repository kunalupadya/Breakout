package sample;

import javafx.scene.Node;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import java.util.Random;

public class Block {

    private ImageView myView;
    public int hitsLeftToBreakBlock;
    private int xPos;
    private int yPos;
    private Random dice = new Random();
    private int powerup = 0;

    // Powerups

    public static final int INCREASE_BALL_SIZE = 0;
    public static final int BOMB = 1;
    public static final int DOUBLE_POPPER = 2;


    public Block(Image image, int hitsToBreakBlock, int xPos, int yPos){
        myView = new ImageView(image);
        hitsLeftToBreakBlock = hitsToBreakBlock;
        //sets the location of the block to be centered upon the x and y pixels specified
        myView.setX(xPos-myView.getBoundsInLocal().getWidth()/2);
        myView.setY(yPos-myView.getBoundsInLocal().getHeight()/2);
        powerup = dice.nextInt(2);
        this.xPos = xPos;
        this.yPos = yPos;

    }

    public Block(Image image, int hitsToBreakBlock, int xPos, int yPos, int powerup){
        this(image, hitsToBreakBlock, xPos, yPos);
        this.powerup = powerup;
    }

    public void changeImage(Image image){
        myView.setImage(image);
    }

    public int blockWasHit(int popStrength){
        int numPopped = Math.min(popStrength, hitsLeftToBreakBlock);
        hitsLeftToBreakBlock-=popStrength;
        return numPopped;
    }

    public boolean isBlockDead(){
        return hitsLeftToBreakBlock <= 0;
    }
    public int popBlock(){
        myView.setImage(null);
        return powerup;
    }

    public int getCenterxPos() {
        return xPos;
    }

    public int getCenteryPos() {
        return yPos;
    }

    public double getXPosition(){
        return myView.getX();
    }

    public Node getView(){
        return myView;
    }

    public double getBlockLength(){
        return myView.getBoundsInLocal().getWidth();
    }
}
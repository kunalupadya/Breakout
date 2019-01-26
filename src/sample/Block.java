package sample;

import javafx.scene.Node;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import java.util.Random;

/**
 * the block class, used to define the breakable objects
 *
 * @author Kunal Upadya
 */
public class Block {

    private ImageView myView;
    private int hitsLeftToBreakBlock;
    private int xPos;
    private int yPos;
    private Random dice = new Random();
    private int powerup = 0;

    // Powerups

    /**
     * Creates a breakable block
     * @param image the image for the block, based on its strength
     * @param hitsToBreakBlock the strength of the block
     * @param xPos the center of the block position
     * @param yPos the center of the block position
     */
    public Block(Image image, int hitsToBreakBlock, int xPos, int yPos){
        myView = new ImageView(image);
        hitsLeftToBreakBlock = hitsToBreakBlock;
        //sets the location of the block to be centered upon the x and y pixels specified
        myView.setX(xPos-myView.getBoundsInLocal().getWidth()/2);
        myView.setY(yPos-myView.getBoundsInLocal().getHeight()/2);
        powerup = dice.nextInt(10);
        this.xPos = xPos;
        this.yPos = yPos;

    }

    /**
     * second constructor to create a breakable block, allows for specification of the powerup (not used in this project, but could be useful)
     * @param image
     * @param hitsToBreakBlock
     * @param xPos
     * @param yPos
     * @param powerup
     */
    public Block(Image image, int hitsToBreakBlock, int xPos, int yPos, int powerup){
        this(image, hitsToBreakBlock, xPos, yPos);
        this.powerup = powerup;
    }

    /**
     * changes the image of the block, used when a block is popped
     * @param image
     */
    public void changeImage(Image image){
        myView.setImage(image);
    }

    /**
     * pops the block based on the strength of the ball's popping strength
     */
    public int blockWasHit(int popStrength){
        int numPopped = Math.min(popStrength, hitsLeftToBreakBlock);
        hitsLeftToBreakBlock-=popStrength;
        return numPopped;
    }

    /**
     * checks if the block has been broken
     */
    public boolean isBlockDead(){
        return hitsLeftToBreakBlock <= 0;
    }

    /**
     * make the block invisible and return its powerup, if any
     */
    public int popBlockAndGetPowerup(){
        myView.setImage(null);
        return powerup;
    }

    /**
     * get the x coordinate of the center of the block for powerup generation
     */
    public int getCenterxPos() {
        return xPos;
    }

    /**
     * gets the y coordinate of the center of the block for powerup generation
     * @return y coordinate
     */
    public int getCenteryPos() {
        return yPos;
    }


    /**
     * gets the x position of the top left corner of the block
     * @return x position
     */
    public double getXPosition(){
        return myView.getX();
    }

    /**
     * gets a node object representing the block
     * @return node objet
     */
    public Node getView(){
        return myView;
    }

    /**
     * get the number of hits left to break the block, in order to change the image.
     * @return
     */
    public int getHitsLeftToBreakBlock() {
        return hitsLeftToBreakBlock;
    }

    /**
     * returns the length (width) of the block
     * @return the width (double)
     */
    public double getBlockLength(){
        return myView.getBoundsInLocal().getWidth();
    }
}
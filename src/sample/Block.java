package sample;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class Block {

    private ImageView myView;
    private int hitsLeftToBreakBlock;
    private String powerup = "";

    public Block(Image image, int hitsToBreakBlock, int xPos, int yPos){
        myView = new ImageView(image);
        hitsLeftToBreakBlock = hitsToBreakBlock;
        //sets the location of the block to be centered upon the x and y pixels specified
        myView.setX(xPos-myView.getBoundsInLocal().getWidth()/2);
        myView.setY(yPos-myView.getBoundsInLocal().getHeight()/2);
    }

    public Block(Image image, int hitsToBreakBlock, int xPos, int yPos, String powerup){
        this(image, hitsToBreakBlock, xPos, yPos);
        this.powerup = powerup;
    }
}
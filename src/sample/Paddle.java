package sample;

import javafx.geometry.Point2D;
import javafx.scene.Node;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class Paddle {
    private final int SIZE = 10;
    private final int PADDLE_WIDTH = SIZE;

    private int PADDLE_LENGTH = SIZE*8;
    private int PADDLE_SPEED = 50;

    private ImageView myView;
    private Point2D myVelocity;

    public Paddle (Image image, int screenWidth, int screenHeight){
        myView = new ImageView(image);
        myView.setFitHeight(PADDLE_WIDTH);
        myView.setFitWidth(PADDLE_LENGTH);
        myView.setX((int)(screenWidth/2-myView.getBoundsInLocal().getWidth()/2));
        myView.setY((int)(screenHeight-screenHeight/10));

    }

    /**
     * Moves the paddle left or right
     * @param direction either -1 or 1, where negative moves the paddle left and positive moves the paddle right
     */
    public void move(int direction){
        myView.setX(myView.getX()+ PADDLE_SPEED *direction);
    }

    public Node getView(){
        return myView;
    }
}

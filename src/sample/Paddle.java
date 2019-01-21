package sample;

import javafx.scene.Node;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

/**
 * the paddle object that is used to bounce the ball
 * @author Kunal Upadya
 */
public class Paddle {
    public static final int PADDLE_HEIGHT = 10;
    public static final int PADDLE_LENGTH = PADDLE_HEIGHT*8;
    public static final int HIGH_PADDLE_SPEED = 100;
    public static final int NORMAL_PADDLE_SPEED = 50;


    private int paddleSpeed = 50;

    private ImageView myView;

    /**
     * creates the paddle in the middle of the screen at the bottom of the screen
     * @param image
     * @param screenWidth
     * @param screenHeight
     */
    public Paddle (Image image, int screenWidth, int screenHeight){
        myView = new ImageView(image);
        myView.setFitHeight(PADDLE_HEIGHT);
        myView.setFitWidth(PADDLE_LENGTH);
        myView.setX((int)(screenWidth/2-myView.getBoundsInLocal().getWidth()/2));
        myView.setY((int)(screenHeight-screenHeight/10));
    }

    /**
     * Moves the paddle left or right
     * @param direction either -1 or 1, where negative moves the paddle left and positive moves the paddle right
     */
    public void move(int direction){
        myView.setX(myView.getX()+ paddleSpeed *direction);
    }

    /**
     * gets the node object for the paddle
     * @return
     */
    public Node getView(){
        return myView;
    }

    /**
     *gets the x position of the paddle, used for object detection and collision
     * @return
     */
    public double getxPos() {
        return myView.getX();
    }

    /**
     * sets the speed of the paddle's movement
     * @param paddleSpeed
     */
    public void setPaddleSpeed(int paddleSpeed) {
        this.paddleSpeed = paddleSpeed;
    }
}

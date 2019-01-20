package sample;

import javafx.scene.Node;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class Paddle {
    public static final int PADDLE_HEIGHT = 10;
    public static final int PADDLE_LENGTH = PADDLE_HEIGHT*8;

    private int PADDLE_SPEED = 50;

    private ImageView myView;

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
        myView.setX(myView.getX()+ PADDLE_SPEED *direction);
    }

    public Node getView(){
        return myView;
    }

    public double getxPos() {
        return myView.getX();
    }

    public double getyPos() {
        return myView.getY();
    }
}

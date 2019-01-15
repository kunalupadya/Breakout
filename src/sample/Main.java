package sample;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.util.ArrayList;


public class Main extends Application {

    public static final String GAME_NAME = "Breakout";
    public static final Paint BACKGROUND = Color.WHITE;
    public static final int SCREEN_WIDTH = 1020;
    public static final int SCREEN_HEIGHT = 720;
    public static final int FRAMES_PER_SECOND = 60;
    public static final int MILLISECOND_DELAY = 1000/FRAMES_PER_SECOND;
    public static final double SECOND_DELAY = 1.0/FRAMES_PER_SECOND;
    public static final String BALL_IMAGE = "ball.gif";
    public static final String PADDLE_IMAGE = "paddle.gif";
    public static final int RIGHT = 1;
    public static final int LEFT = -1;
    public static final int[] rowsLevelOne = new int[]{SCREEN_HEIGHT/10, SCREEN_HEIGHT*2/10, SCREEN_HEIGHT*3/10};
    public static final int[] colsLevelOne = new int[]{SCREEN_WIDTH/10, SCREEN_WIDTH*2/10, SCREEN_WIDTH*3/10, SCREEN_WIDTH*4/10,
            SCREEN_WIDTH*5/10, SCREEN_WIDTH*6/10, SCREEN_WIDTH*7/10, SCREEN_WIDTH*8/10, SCREEN_WIDTH*9/10};

    public Ball myBall;
    public Paddle myPaddle;
    public ArrayList<Block> myBlocks = new ArrayList<>();
    public boolean gameInProgress = false;
    public int level = 0;

    @Override
    public void start(Stage primaryStage) throws Exception{
        //attach scene to stage and display it
        Scene myScene = setupLevelOne(SCREEN_WIDTH, SCREEN_HEIGHT, BACKGROUND);
        primaryStage.setTitle(GAME_NAME);
        primaryStage.setScene(myScene);
        primaryStage.show();
        //attach "game loop" to timeline and play it"
        var frame = new KeyFrame(Duration.millis(MILLISECOND_DELAY), event -> step(SECOND_DELAY));
        var animation = new Timeline();
        animation.setCycleCount(Timeline.INDEFINITE);
        animation.getKeyFrames().add(frame);
        animation.play();
    }

    private Scene setupLevelOne(int width, int height, Paint background){
        var root = new Group();
        var scene = new Scene(root,width,height,background);
        var ballImage = new Image(this.getClass().getClassLoader().getResourceAsStream(BALL_IMAGE));
        myBall = new Ball(ballImage,width,height);
        var paddleImage = new Image(this.getClass().getClassLoader().getResourceAsStream(PADDLE_IMAGE));
        myPaddle = new Paddle(paddleImage, width, height);
        root.getChildren().add(myBall.getView());
        root.getChildren().add(myPaddle.getView());
        // respond to input
        scene.setOnKeyPressed(e -> handleKeyInput(e.getCode()));
        int count = 0;
        for (int row:rowsLevelOne){
            //sets the number of hits to break each block
            int blockHitsToBreak = 3-count;
            Image blockImage = getBrickImage(blockHitsToBreak);
            for (int col:colsLevelOne){
                Block b = new Block(blockImage,blockHitsToBreak, col,row);
                root.getChildren().add(b.getView());
                myBlocks.add(b);
            }
            count++;
        }
//        var image =;
//        for (int k = 0;k<BLOCKS_PER_ROW)
//        root.getChildren().add()
        return scene;
    }

    private void step(double elapsedTime){
        myBall.move(elapsedTime);
        myBall.bounceOffWalls(SCREEN_WIDTH,SCREEN_HEIGHT);
        //if ball is dead, resets ball and sets game to paused
        if (myBall.isBallDead(SCREEN_WIDTH,SCREEN_HEIGHT)){
            gameInProgress = false;
        }
        //if ball and paddle intersect, ball bounces off
        if (myBall.getView().getBoundsInParent().intersects(myPaddle.getView().getBoundsInParent())){
            myBall.bounceOffPaddle();
        }
        for (Block b:myBlocks){
            if (myBall.getView().getBoundsInParent().intersects(b.getView().getBoundsInParent())){
                myBall.bounceOffPaddle();
            }
        }
    }
    private Image getBrickImage(int numHitsToBreak){
        String blockName = "brick"+String.valueOf(numHitsToBreak)+".gif";
        var blockImage = new Image(this.getClass().getClassLoader().getResourceAsStream(blockName));
        return blockImage;
    }

    private void handleKeyInput (KeyCode code){
        if (code == KeyCode.LEFT){
            myPaddle.move(LEFT);
        }
        if (code == KeyCode.RIGHT){
            myPaddle.move(RIGHT);
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}

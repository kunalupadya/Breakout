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

    public Ball myBall;
    public Paddle myPaddle;
    public boolean gameInProgress = false;

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

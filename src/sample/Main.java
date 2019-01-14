package sample;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Group;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.stage.Stage;
import javafx.util.Duration;


public class Main extends Application {

    public static final String GAME_NAME = "Breakout"
    public static final Paint BACKGROUND = Color.WHITE;
    public static final int SCREEN_WIDTH = 1020;
    public static final int SCREEN_HEIGHT = 720;
    public static final int FRAMES_PER_SECOND = 60;
    public static final int MILLISECOND_DELAY = 1000/FRAMES_PER_SECOND;
    public static final double SECOND_DELAY = 1.0/FRAMES_PER_SECOND;

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

    public static Scene setupLevelOne(int width, int height, Paint background){
        var root = new Group();
        var scene = new Scene(root,width,height,background);

//        var image =;
//        for (int k = 0;k<BLOCKS_PER_ROW)
//        root.getChildren().add()
        return scene;
    }

    private void step(double elapsedTime){

    }

    public static void main(String[] args) {
        launch(args);
    }
}

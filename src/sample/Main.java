package sample;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Group;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.stage.Stage;


public class Main extends Application {

    public static final Paint BACKGROUND = Color.WHITE;
    public static final int SCREEN_WIDTH = 1020;
    public static final int SCREEN_HEIGHT = 720;

    @Override
    public void start(Stage primaryStage) throws Exception{
        Scene myScene = setupLevelOne(SCREEN_WIDTH, SCREEN_HEIGHT, BACKGROUND);
        primaryStage.setTitle("Hello World");
        primaryStage.setScene(myScene);
        primaryStage.show();
    }

    public static Scene setupLevelOne(int width, int height, Paint background){
        var root = new Group();
        var scene = new Scene(root,width,height,background);
//        var image =;
//        for (int k = 0;k<BLOCKS_PER_ROW)
//        root.getChildren().add()
        return scene;
    }

    public static void main(String[] args) {
        launch(args);
    }
}

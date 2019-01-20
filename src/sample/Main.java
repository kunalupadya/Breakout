package sample;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Polygon;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Shape;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.util.ArrayList;
import java.util.LinkedList;


public class Main extends Application {

    public static final String GAME_NAME = "Breakout";
    public static final Paint BACKGROUND = Color.WHITE;
    public static final int SCREEN_WIDTH = 1020;
    public static final int SCREEN_HEIGHT = 720;
    public static final int ROTATOR_SIZE = 60;
    public static final int FRAMES_PER_SECOND = 60;
    public static final int MILLISECOND_DELAY = 1000/FRAMES_PER_SECOND;
    public static final double SECOND_DELAY = 1.0/FRAMES_PER_SECOND;
    public static final String BALL_IMAGE = "ball.gif";
    public static final String PADDLE_IMAGE = "paddle.gif";
    public static final int RIGHT = 1;
    public static final int LEFT = -1;
    public static final int BOMB_Y_SIZE = 200;
    public static final int BOMB_X_SIZE = 200;
    public static final int[] rowsLevelOne = new int[]{SCREEN_HEIGHT/10, SCREEN_HEIGHT*2/10, SCREEN_HEIGHT*3/10};
    public static final int[] colsLevelOne = new int[]{SCREEN_WIDTH/10, SCREEN_WIDTH*2/10, SCREEN_WIDTH*3/10, SCREEN_WIDTH*4/10,
            SCREEN_WIDTH*5/10, SCREEN_WIDTH*6/10, SCREEN_WIDTH*7/10, SCREEN_WIDTH*8/10, SCREEN_WIDTH*9/10};
    public static final int[] rowsLevelTwo = new int[]{SCREEN_HEIGHT*3/10, SCREEN_HEIGHT*2/10, SCREEN_HEIGHT/10,SCREEN_HEIGHT*2/10,SCREEN_HEIGHT*3/10, SCREEN_HEIGHT*2/10, SCREEN_HEIGHT/10,SCREEN_HEIGHT*2/10,SCREEN_HEIGHT*3/10, SCREEN_HEIGHT*4/10, SCREEN_HEIGHT*5/10, SCREEN_HEIGHT*4/10,SCREEN_HEIGHT*4/10,SCREEN_HEIGHT*5/10,SCREEN_HEIGHT*4/10};
    public static final int[] colsLevelTwo = new int[]{SCREEN_WIDTH/10, SCREEN_WIDTH*2/10, SCREEN_WIDTH*3/10, SCREEN_WIDTH*4/10,
            SCREEN_WIDTH*5/10, SCREEN_WIDTH*6/10, SCREEN_WIDTH*7/10, SCREEN_WIDTH*8/10, SCREEN_WIDTH*9/10, SCREEN_WIDTH*2/10,
            SCREEN_WIDTH*3/10, SCREEN_WIDTH*4/10, SCREEN_WIDTH*6/10, SCREEN_WIDTH*7/10, SCREEN_WIDTH*8/10,};
    public static final int[] rowsLevelThree = new int[]{SCREEN_HEIGHT/10, SCREEN_HEIGHT*2/10, SCREEN_HEIGHT*3/10,SCREEN_HEIGHT*4/10,
            SCREEN_HEIGHT*5/10, SCREEN_HEIGHT*4/10,SCREEN_HEIGHT*3/10, SCREEN_HEIGHT*2/10, SCREEN_HEIGHT/10};
    public static final int[] colsLevelThree = new int[]{SCREEN_WIDTH/10, SCREEN_WIDTH*2/10, SCREEN_WIDTH*3/10, SCREEN_WIDTH*4/10,
            SCREEN_WIDTH*5/10, SCREEN_WIDTH*6/10, SCREEN_WIDTH*7/10, SCREEN_WIDTH*8/10, SCREEN_WIDTH*9/10};


    private Ball myBall;
    private Paddle myPaddle;
    private ArrayList<Block> myBlocks = new ArrayList<>();
    private ArrayList<Block> blocksToRemove = new ArrayList<>();
    private ArrayList<Powerup> onscreenPowerups = new ArrayList<>();
    private ArrayList<Powerup> powerupsToRemove = new ArrayList<>();
    private ArrayList<Polygon> myTriangles = new ArrayList<>();
    private ArrayList<Rectangle> myRotators = new ArrayList<>();
    private LinkedList<ActivePowerup> powerupTimeTracker = new LinkedList<>();
    private boolean gameInProgress = false;
    private long lastTriangleHit;
    private long lastRotatorHit;
    private long lastPaddleHit;
    private int level = 0;
    private Group root;
    private Stage primaryStage;

    private void clearAllStorage(){
        myBlocks.clear();
        blocksToRemove.clear();
        onscreenPowerups.clear();
        powerupsToRemove.clear();
        powerupTimeTracker.clear();
        myTriangles.clear();
        gameInProgress = false;
    }

    boolean[] activePowerups = new boolean[]{false,false,false};

    public class ActivePowerup{
        int powerupType;
        long startTime = System.currentTimeMillis();
        public ActivePowerup(int powerupType){
            this.powerupType = powerupType;
            System.out.println(startTime);
        }

        public int getPowerupType() {
            return powerupType;
        }

        public long getStartTime() {
            return startTime;
        }
    }

    @Override
    public void start(Stage primaryStage) throws Exception{
        //attach scene to stage and display it
        this.primaryStage = primaryStage;
        Scene myScene = setupInstructionsScreen(SCREEN_WIDTH, SCREEN_HEIGHT, BACKGROUND);
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

    private Scene initializeScene(int width, int height, Paint background){
        clearAllStorage();
        root = new Group();
        var scene = new Scene(root,width,height,background);
        var ballImage = new Image(this.getClass().getClassLoader().getResourceAsStream(BALL_IMAGE));
        myBall = new Ball(ballImage,width,height);
        var paddleImage = new Image(this.getClass().getClassLoader().getResourceAsStream(PADDLE_IMAGE));
        myPaddle = new Paddle(paddleImage, width, height);
        Rectangle rotator1 = new Rectangle(width*3/4,height*7/10, ROTATOR_SIZE,ROTATOR_SIZE);
        root.getChildren().add(rotator1);
        Rectangle rotator2 = new Rectangle(width/4,height*7/10, ROTATOR_SIZE,ROTATOR_SIZE);
        root.getChildren().add(rotator2);
        myRotators.add(rotator1);
        myRotators.add(rotator2);
        root.getChildren().add(myBall.getView());
        root.getChildren().add(myPaddle.getView());
        scene.setOnKeyPressed(e -> handleKeyInput(e.getCode()));
        return scene;
    }

    private Scene setupInstructionsScreen(int width, int height, Paint background){
        clearAllStorage();
        root = new Group();
        Scene scene = new Scene(root, width,height,background);
        Text titleText = new Text("Welcome to Breakout!");
        titleText.setX(50);
        titleText.setY(50);
        Text instructions = new Text("The objective of the game is to pop all blocks while trying not to lose lives. If the ball hits the floor, you lose a life. To prevent this, move your paddle left and right to make the ball bounce up and break more blocks. Occasionally, a powerup will drop from a broken block. The red powerup increases the size of the ball, making it easier to bounce. The green powerup acts as a 'bomb', popping multiple adjacent blocks when you get a hit. The final powerup, the yellow powerup, is a double popper that doubles your pop rate. All powerups last for 15 seconds. This game also ");

        root.getChildren().add(text);
        return scene;
    }

    private Scene setupLevelOne(int width, int height, Paint background){
        Scene scene = initializeScene(width,height,background);
        // respond to input

//        scene.setOnMouseClicked(e -> switchScene());
        // setup blocks
        int count = 0;
        for (int row:rowsLevelOne){
            //sets the number of hits to break each block
            int blockHitsToBreak = 3-count;
            Image blockImage = getBrickImage(blockHitsToBreak);
            for (int col:colsLevelOne){
                createAndAddBlocks(blockImage,blockHitsToBreak, col,row);
            }
            count++;
        }
        return scene;
    }

    private Scene setupLevelTwo(int width, int height, Paint background){
        Scene scene = initializeScene(width,height,background);
        int blockHitsToBreak = 10;
        Image blockImage = getBrickImage(blockHitsToBreak);
        for (int k = 0;k<rowsLevelTwo.length;k++){
            createAndAddBlocks(blockImage, blockHitsToBreak, colsLevelTwo[k], rowsLevelTwo[k]);
        }
        return scene;
    }

    private Scene setupLevelThree(int width, int height, Paint background){
        Scene scene = initializeScene(width,height,background);
        int[] blockHitsToBreak = new int[]{5,4,3,2,1,2,3,4,5};
        for (int k = 0; k<blockHitsToBreak.length; k++){
            Image blockImage = getBrickImage(blockHitsToBreak[k]);
            createAndAddBlocks(blockImage, blockHitsToBreak[k], colsLevelThree[k], rowsLevelThree[k]);
        }
        createAndAddTriangle(new Double[]{0.0, 250.0, 0.0, 400.0, 400.0, 400.0});
        createAndAddTriangle(new Double[]{1020.0, 250.0, 1020.0, 400.0, 620.0, 400.0 });
        createAndAddTriangle(new Double[]{150.0, 0.0, 870.0, 0.0, 510.0, 100.0 });
        return scene;
    }

    private void createAndAddTriangle(Double[] points){
        Polygon triangle = new Polygon();
        triangle.getPoints().addAll(points);
        root.getChildren().add(triangle);
        myTriangles.add(triangle);
    }

    private void createAndAddBlocks(Image blockImage,int blockHitsToBreak, int col,int row){
        Block b = new Block(blockImage,blockHitsToBreak, col,row);
        root.getChildren().add(b.getView());
        myBlocks.add(b);
    }

    private void step(double elapsedTime){
//        if (gameInProgress) {
            myBall.move(elapsedTime);

            for (Rectangle r : myRotators) {
                var intersect = Shape.intersect(myBall.getShape(), r);
                if (intersect.getBoundsInLocal().getWidth() != -1) {
                    myBall.bounceOffRotator(lastRotatorHit);
                }
                r.setRotate(r.getRotate() + 1);
            }

            myBall.bounceOffWalls(SCREEN_WIDTH, SCREEN_HEIGHT);
            //if ball is dead, resets ball and sets game to paused
            if (myBall.resetBallIfDead(SCREEN_WIDTH, SCREEN_HEIGHT)) {
                gameInProgress = false;
            }
            //if ball and paddle intersect, ball bounces off
            if (myBall.getView().getBoundsInParent().intersects(myPaddle.getView().getBoundsInParent())) {
                myBall.bounceOffPaddle(myPaddle);
            }

            for (Block b : myBlocks) {
                if (myBall.getView().getBoundsInParent().intersects(b.getView().getBoundsInParent())) {
                    myBall.bounceOffBlock(b);
                    b.blockWasHit(myBall.getPopsPerHit());
                    if (activePowerups[Block.BOMB]) {
                        Node bomb = bombPowerup(b.getCenterxPos(), b.getCenteryPos());
                        root.getChildren().add(bomb);
                        System.out.println(bomb.getBoundsInParent());
                        for (Block c : myBlocks) {
                            if ((bomb.getBoundsInParent().intersects(c.getView().getBoundsInParent())) && (c != b)) {
                                c.blockWasHit(myBall.getPopsPerHit());
                                killBlockIfDead(c);
                            }
                        }
                    }
                    killBlockIfDead(b);
                }
            }
            for (Block b : blocksToRemove) {
                myBlocks.remove(b);
            }
            blocksToRemove.clear();
            for (Powerup p : onscreenPowerups) {
                p.move(elapsedTime);
                if (p.isOffScreen(SCREEN_HEIGHT)) {
                    p.killPowerup();
                    powerupsToRemove.add(p);
                }
                if (myPaddle.getView().getBoundsInParent().intersects(p.getView().getBoundsInParent())) {
                    activePowerups[p.getPowerupType()] = true;
                    powerupTimeTracker.add(new ActivePowerup(p.getPowerupType()));
                    p.killPowerup();
                    powerupsToRemove.add(p);
                }
            }
            for (Powerup p : powerupsToRemove) {
                onscreenPowerups.remove(p);
            }
            powerupsToRemove.clear();
            for (ActivePowerup a : powerupTimeTracker) {
                if ((System.currentTimeMillis() - a.startTime) > 15000) {
                    deactivatePowerups(a);
                    powerupTimeTracker.remove(a);
                } else {
                    activatePowerups(a);
                }
            }

            for (Polygon triangle : myTriangles) {
                var intersect = Shape.intersect(myBall.getShape(), triangle);
                if (intersect.getBoundsInLocal().getWidth() != -1) {
                    myBall.bounceOffTriangle(lastTriangleHit);
                    lastTriangleHit = System.currentTimeMillis();
                }
            }
            changeSceneIfTriggered();
        }
    }

    private void changeSceneIfTriggered(){
        if (myBlocks.size() == 0){
            switch (level){
                case 0:
                    switchScene(setupLevelOne(SCREEN_WIDTH,SCREEN_HEIGHT,BACKGROUND));
                case 1:
                    switchScene(setupLevelTwo(SCREEN_WIDTH,SCREEN_HEIGHT,BACKGROUND));
                case 2:
                    switchScene(setupLevelThree(SCREEN_WIDTH,SCREEN_HEIGHT,BACKGROUND));
            }
        }
    }

    private void killBlockIfDead(Block b){
        if (b.isBlockDead()){
            int powerupType = b.popBlock();
            if (powerupType <3){
                Powerup newestPowerup = new Powerup(getPowerupImage(powerupType),b.getCenterxPos(),b.getCenteryPos(),powerupType);
                root.getChildren().add(newestPowerup.getView());
                onscreenPowerups.add(newestPowerup);
            }
            blocksToRemove.add(b);
        }
        else{
            b.changeImage(getBrickImage(b.hitsLeftToBreakBlock));
        }
    }

    private void activatePowerups(ActivePowerup powerup){
        activePowerups[powerup.getPowerupType()] = true;
        if (powerup.getPowerupType()==Block.INCREASE_BALL_SIZE){
            myBall.changeBallSize(2);
        }
        if (powerup.getPowerupType()==Block.BOMB){
            // does nothing here, the powerup is activated by checking the
        }
        if (powerup.getPowerupType()==Block.DOUBLE_POPPER){
            myBall.changePopStrength(2);
        }
    }

    private void deactivatePowerups(ActivePowerup powerup){
        activePowerups[powerup.getPowerupType()] = false;
        if (powerup.getPowerupType()==Block.INCREASE_BALL_SIZE){
            myBall.changeBallSize(1);
        }
        if (powerup.getPowerupType()==Block.BOMB){

        }
        if (powerup.getPowerupType()==Block.DOUBLE_POPPER){
            myBall.changePopStrength(1);
        }
    }
    private Node bombPowerup(int xPos, int yPos){
        ImageView bombView = new ImageView();
        bombView.setFitWidth(BOMB_X_SIZE);
        bombView.setFitHeight(BOMB_Y_SIZE);
        bombView.setX(xPos-bombView.getBoundsInLocal().getWidth()/2);
        bombView.setY(yPos-bombView.getBoundsInLocal().getHeight()/2);
        return bombView;
    }

    private Image getBrickImage(int numHitsToBreak){
        String blockName = "brick"+String.valueOf(numHitsToBreak)+".gif";
        return loadImage(blockName);
    }

    private Image getPowerupImage(int powerupType){
        String powerupName = "powerup"+String.valueOf(powerupType)+".gif";
        return loadImage(powerupName);
    }

    private Image loadImage(String filename){
        return new Image(this.getClass().getClassLoader().getResourceAsStream(filename));
    }

    private void handleKeyInput (KeyCode code){
        if (code == KeyCode.LEFT){
            myPaddle.move(LEFT);
        }
        if (code == KeyCode.RIGHT){
            myPaddle.move(RIGHT);
        }
    }

    private void switchScene(Scene scene){
        primaryStage.setScene(scene);
    }

    public static void main(String[] args) {
        launch(args);
    }
}

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
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;
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
    public static final int NUM_LIVES = 5;

    private Ball myBall;
    private Paddle myPaddle;
    private ArrayList<Block> myBlocks = new ArrayList<>();
    private ArrayList<Block> blocksToRemove = new ArrayList<>();
    private ArrayList<Powerup> onscreenPowerups = new ArrayList<>();
    private ArrayList<Powerup> powerupsToRemove = new ArrayList<>();
    private ArrayList<Polygon> myTriangles = new ArrayList<>();
    private ArrayList<Rectangle> myRotators = new ArrayList<>();
    private LinkedList<ActivePowerup> powerupTimeTracker = new LinkedList<>();
    private ArrayList<ActivePowerup> powerupTimeTrackerToRemove = new ArrayList<>();
    private ArrayList<Text> scoreLabels = new ArrayList<>();
    private ArrayList<Text> livesLabels = new ArrayList<>();
    private ArrayList<Text> levelLabels = new ArrayList<>();
    private long lastTriangleHit;
    private long lastRotatorHit;
    private long lastPaddleHit;
    private int level = 0;
    private int multiplier = 1;
    private int popsSinceMultiplier;
    private int score;
    private int lives = NUM_LIVES;
    private boolean highPaddleSpeed = false;
    private boolean gameOver = false;
    private Group root;
    private Stage primaryStage;
    private boolean bombPowerupActive = false;

    /**
     *
     */
    public class ActivePowerup{
        int powerupType;
        long startTime = System.currentTimeMillis();
        public ActivePowerup(int powerupType){
            this.powerupType = powerupType;
        }

        public int getPowerupType() {
            return powerupType;
        }

        public long getStartTime() {
            return startTime;
        }
    }

    /**
     * clears the variables in storage that store the objects onscreen, as well as active powerups
     */
    private void clearAllStorage(){
        myRotators.clear();
        myBlocks.clear();
        blocksToRemove.clear();
        onscreenPowerups.clear();
        powerupsToRemove.clear();
        powerupTimeTracker.clear();
        myTriangles.clear();
    }

    /**
     * starts the animation
     * @param primaryStage the stage used to seat all animations
     * @throws Exception
     */
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

    /**
     * initializes
     * @param width
     * @param height
     * @param background
     * @return
     */
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
        titleText.setX(width/2-250);
        titleText.setY(50);
        titleText.setFont(Font.font("arial", FontWeight.BLACK, FontPosture.REGULAR, 40));
        Text instructions = new Text("The objective of the game is to pop all blocks while trying not to lose lives. If the ball hits the floor, you lose a life. To prevent this, move your paddle left and right to make the ball bounce up and break more blocks. Occasionally, a powerup will drop from a broken block. The red powerup increases the size of the ball, making it easier to bounce. The green powerup acts as a 'bomb', popping multiple adjacent blocks when you get a hit. The final powerup, the yellow powerup, is a double popper that doubles your pop rate. All powerups last for 15 seconds. This game also features multiple cheat keys. To instantly break all blocks, hit 1. To toggle increased paddle movement, hit 2. To activate powerups, hit 3,4,or 5 respectively. In terms of scoring, this game has a multiplier - the more blocks you pop without dying, the higher your multiplier. Have fun!\n \nTo play, click anywhere onscreen.");
        instructions.setX(width/8.0);
        instructions.setY(height/6.0);
        instructions.setFont(Font.font("arial", FontWeight.BLACK, FontPosture.REGULAR, 25));
        instructions.setWrappingWidth(765);
        root.getChildren().add(titleText);
        root.getChildren().add(instructions);
        scene.setOnMouseClicked(e -> changeSceneIfTriggered());
        return scene;
    }

    private Scene setupLevelOne(int width, int height, Paint background){
        Scene scene = initializeScene(width,height,background);
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

    private Scene setupFinalScene(int width, int height, Paint background){
        clearAllStorage();
        root = new Group();
        Scene scene = new Scene(root, width,height,background);
        Text titleText = new Text("Game Over");
        titleText.setX(width/2.0-250);
        titleText.setY(100);
        titleText.setFont(Font.font("arial", FontWeight.BLACK, FontPosture.REGULAR, 60));
        Text instructions = new Text("Your score: " + String.valueOf(score));
        instructions.setX(width/3.0);
        instructions.setY(height/4.0);
        instructions.setFont(Font.font("arial", FontWeight.BLACK, FontPosture.REGULAR, 25));
        instructions.setWrappingWidth(765);
        Text tryAgain = new Text("Try again? Press spacebar to restart!");
        tryAgain.setX(width/3.0);
        tryAgain.setY(height/3.0);
        tryAgain.setFont(Font.font("arial", FontWeight.BLACK, FontPosture.REGULAR, 25));
        tryAgain.setWrappingWidth(765);
        root.getChildren().add(instructions);
        root.getChildren().add(titleText);
        root.getChildren().add(tryAgain);
        scene.setOnKeyPressed(e -> startNewGame(e.getCode()));
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
        myBall.move(elapsedTime);

        handleRotatorRotationsAndBounces();

        handleWallInteractions();

        handleBouncingOffPaddle();

        detectBlockCollision();
        removeDeadBlocks();

        handleOnscreenPowerups(elapsedTime);
        removeDeadPowerups();

        trackActivePowerups();
        removeInactivePowerups();

        detectTriangleCollisions();

        generateLabel(scoreLabels, "Score: " + String.valueOf(score),
                Font.font("arial", FontWeight.BLACK, FontPosture.REGULAR, 25),850,50);
        generateLabel(livesLabels, "Lives: " + String.valueOf(lives),
                Font.font("arial", FontWeight.BLACK, FontPosture.REGULAR, 25),70,50);
        if ((level<4)&&(level>0)) {
            generateLabel(levelLabels, "Level:" + String.valueOf(level), Font.font("arial", FontWeight.BLACK, FontPosture.REGULAR, 25),70,660);
        }

        changeSceneIfTriggered();

        setMultiplier();
    }

    private void setMultiplier() {
        if (popsSinceMultiplier >= 5){
            multiplier +=1;
            popsSinceMultiplier = 0;
        }
    }

    private void generateScoreLabel() {
        for (Text scorelabel: scoreLabels){
            root.getChildren().remove(scorelabel);
        }
        scoreLabels.clear();
        Text scoreLabel = new Text("Score: " + String.valueOf(score));
        scoreLabel.setFont(Font.font("arial", FontWeight.BLACK, FontPosture.REGULAR, 25));
        scoreLabel.setX(850);
        scoreLabel.setY(50);
        root.getChildren().add(scoreLabel);
        scoreLabels.add(scoreLabel);
    }

    private void generateLabel(ArrayList<Text> labels, String s, Font font, double x, double y) {
        for (Text label: labels){
            root.getChildren().remove(label);
        }
        labels.clear();
        Text label = new Text(s);//"Score: " + String.valueOf(score));
        label.setFont(font);//Font.font("arial", FontWeight.BLACK, FontPosture.REGULAR, 25));
        label.setX(x);//850);
        label.setY(y);//50);
        root.getChildren().add(label);
        labels.add(label);
    }

    private void detectTriangleCollisions() {
        for (Polygon triangle : myTriangles) {
            var intersect = Shape.intersect(myBall.getShape(), triangle);
            if (intersect.getBoundsInLocal().getWidth() != -1) {
                myBall.bounceOffTriangle(lastTriangleHit);
                lastTriangleHit = System.currentTimeMillis();
            }
        }
    }

    private void removeInactivePowerups() {
        for (ActivePowerup p: powerupTimeTrackerToRemove){
            powerupTimeTracker.remove(p);
        }
        powerupTimeTrackerToRemove.clear();
    }

    private void trackActivePowerups() {
        for (ActivePowerup a : powerupTimeTracker) {
            if ((System.currentTimeMillis() - a.startTime) > 15000) {
                deactivatePowerups(a);
                powerupTimeTrackerToRemove.add(a);
            } else {
                activatePowerups(a);
            }
        }
    }

    private void removeDeadPowerups() {
        for (Powerup p : powerupsToRemove) {
            onscreenPowerups.remove(p);
        }
        powerupsToRemove.clear();
    }

    private void handleOnscreenPowerups(double elapsedTime) {
        for (Powerup p : onscreenPowerups) {
            p.move(elapsedTime);
            if (p.isOffScreen(SCREEN_HEIGHT)) {
                p.killPowerup();
                powerupsToRemove.add(p);
            }
            ifPaddleCatchesPowerup(p);
        }
    }

    private void ifPaddleCatchesPowerup(Powerup p) {
        if (myPaddle.getView().getBoundsInParent().intersects(p.getView().getBoundsInParent())) {
            bombPowerupActive = true;
            powerupTimeTracker.add(new ActivePowerup(p.getPowerupType()));
            p.killPowerup();
            powerupsToRemove.add(p);
        }
    }

    private void handleWallInteractions() {
        myBall.bounceOffWalls(SCREEN_WIDTH, SCREEN_HEIGHT);
        //if ball is dead, resets ball and sets game to paused
        if (myBall.resetBallIfDead(SCREEN_WIDTH, SCREEN_HEIGHT)) {
            multiplier = 1;
            popsSinceMultiplier = 0;
            lives -= 1;
            highPaddleSpeed = false;
        }
    }

    private void removeDeadBlocks() {
        for (Block b : blocksToRemove) {
            myBlocks.remove(b);
        }
        blocksToRemove.clear();
    }

    private void detectBlockCollision() {
        for (Block b : myBlocks) {
            if (myBall.getView().getBoundsInParent().intersects(b.getView().getBoundsInParent())) {
                myBall.bounceOffBlock(b);
                popBlockAndUpdateScore(b);
                if (bombPowerupActive) {
                    Node bomb = bombPowerup(b.getCenterxPos(), b.getCenteryPos());
                    root.getChildren().add(bomb);
                    for (Block c : myBlocks) {
                        if ((bomb.getBoundsInParent().intersects(c.getView().getBoundsInParent())) && (c != b)) {
                            popBlockAndUpdateScore(c);
                            killBlockIfDead(c);
                        }
                    }
                }
                killBlockIfDead(b);
            }
        }
    }

    private void popBlockAndUpdateScore(Block b) {
        int popsDone = b.blockWasHit(myBall.getPopsPerHit())*multiplier;
        score += popsDone;
        popsSinceMultiplier +=popsDone;
    }

    private void handleBouncingOffPaddle() {
        if (myBall.getView().getBoundsInParent().intersects(myPaddle.getView().getBoundsInParent())) {
            myBall.bounceOffPaddle(myPaddle);
        }
    }

    private void handleRotatorRotationsAndBounces() {
        for (Rectangle r : myRotators) {
            var intersect = Shape.intersect(myBall.getShape(), r);
            if (intersect.getBoundsInLocal().getWidth() != -1) {
                myBall.bounceOffRotator(lastRotatorHit);
            }
            r.setRotate(r.getRotate() + 1);
        }
    }

    private void changeSceneIfTriggered(){
//        System.out.println(level);
        if (lives <= 0){
            switchScene(setupFinalScene(SCREEN_WIDTH,SCREEN_HEIGHT,BACKGROUND));
            level = 4;
        }
        if (myBlocks.size() == 0){
            if (level == 0){
                switchScene(setupLevelOne(SCREEN_WIDTH,SCREEN_HEIGHT,BACKGROUND));
                level +=1;
            }
            else if (level == 1){
                switchScene(setupLevelTwo(SCREEN_WIDTH,SCREEN_HEIGHT,BACKGROUND));
                level +=1;
            }
            else if (level == 2){
                switchScene(setupLevelThree(SCREEN_WIDTH,SCREEN_HEIGHT,BACKGROUND));
                level +=1;
            }
            else if (level ==3){
                switchScene(setupFinalScene(SCREEN_WIDTH,SCREEN_HEIGHT,BACKGROUND));
                level += 1;
            }
            else if (level ==4 && gameOver){
                resetAll();
                level =0;
                gameOver = false;
            }
        }
    }

    private void resetAll(){
        multiplier = 1;
        score = 0;
        popsSinceMultiplier = 0;
        lives = NUM_LIVES;
        clearAllStorage();
    }

    private void killAllBlocks(){
        for (Block b:myBlocks){
            blocksToRemove.add(b);
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

        if (powerup.getPowerupType()==Block.INCREASE_BALL_SIZE){
            myBall.changeBallSize(2);
        }
        if (powerup.getPowerupType()==Block.BOMB){
            bombPowerupActive = true;
        }
        if (powerup.getPowerupType()==Block.DOUBLE_POPPER){
            myBall.changePopStrength(2);
        }
    }

    private void deactivatePowerups(ActivePowerup powerup){
        if (powerup.getPowerupType()==Block.INCREASE_BALL_SIZE){
            myBall.changeBallSize(1);
        }
        if (powerup.getPowerupType()==Block.BOMB){
            bombPowerupActive = false;
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
        if (code == KeyCode.DIGIT1){
            killAllBlocks();
        }
        if (code == KeyCode.DIGIT2){
            togglePaddleSpeed();
        }
        if (code == KeyCode.DIGIT3){
            powerupTimeTracker.add(new ActivePowerup(Block.INCREASE_BALL_SIZE));
        }
        if (code == KeyCode.DIGIT4){
            powerupTimeTracker.add(new ActivePowerup(Block.BOMB));
        }
        if (code == KeyCode.DIGIT5){
            powerupTimeTracker.add(new ActivePowerup(Block.DOUBLE_POPPER));
        }
    }

    private void startNewGame(KeyCode code){
        if (code == KeyCode.SPACE){
            System.out.println("HI");
            gameOver = true;
        }
    }

    private void togglePaddleSpeed() {
        if (highPaddleSpeed){
            myPaddle.setPaddleSpeed(Paddle.NORMAL_PADDLE_SPEED);
        }
        else {
            myPaddle.setPaddleSpeed(Paddle.HIGH_PADDLE_SPEED);
        }
    }

    private void switchScene(Scene scene){
        primaryStage.setScene(scene);
    }

    public static void main(String[] args) {
        launch(args);
    }
}

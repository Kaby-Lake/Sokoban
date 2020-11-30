package com.ae2dms.UI.Game;

import com.ae2dms.Business.*;
import com.ae2dms.GameObject.AbstractGameObject;
import com.ae2dms.GameObject.Objects.*;
import com.ae2dms.IO.ResourceFactory;
import com.ae2dms.IO.ResourceType;
import com.ae2dms.Main.Main;
import com.ae2dms.UI.AbstractBarController;
import com.ae2dms.UI.GameMediaPlayer;
import com.ae2dms.UI.MediaState;
import com.ae2dms.UI.SoundPreferenceController;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.image.ImageView;
import javafx.scene.control.Label;
import javafx.scene.effect.GaussianBlur;
import javafx.scene.image.Image;
import javafx.scene.input.*;
import javafx.scene.layout.*;
import javafx.scene.media.AudioClip;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.util.StringConverter;
import javafx.util.converter.NumberStringConverter;

import java.awt.*;
import java.util.HashMap;
import java.util.concurrent.atomic.AtomicReference;

public class GameViewController extends AbstractBarController {

    @FXML
    private GridPane previewGrid;

    // Draggable Object on the top right
    @FXML
    private ImageView DragStage1;

    @FXML
    private StackPane DragStack1;

    @FXML
    private StackPane DragStack2;

    @FXML
    private ImageView DragStage2;


    @FXML
    private Pane MusicControllerAlias;

    //Group: LevelCompletePopUp and GameCompletePopUp
    @FXML
    private Pane root;

    //TextBinding: Score of current game, top_right

    @FXML
    private Label Score;

    @FXML
    private Label Time_Spend;

    //Group: Content Group, used to be blurred when pop up shows

    @FXML
    private Group Can_Blur_Group;

    @FXML
    private ImageView Background_Image;


    //Assigned to numbers

    @FXML
    private Label This_Level_Index;

    @FXML
    private Label All_Level_Count;



    // Map to manipulate

    @FXML
    private volatile GridPane stageGrid;

    @FXML
    private volatile GridPane crateGrid;

    @FXML
    private volatile GridPane playerGrid;

    @FXML
    private volatile GridPane diamondsGrid;


    // Controllers to control LevelCompleteView and GameCompleteView

    private LevelCompletePopUpController levelCompletePopUpController;
    private GameCompletePopUpController gameCompletePopUpController;
    private ExitPopUpController exitPopUpController;
    private SoundPreferenceController soundPreferenceController;


    private GameMediaPlayer player = GameMediaPlayer.getInstance();

    private volatile GameDocument gameDocument = Main.gameDocument;

    public volatile static GraphicRender render;

    private GameStatus gameStatus = GameStatus.READY;

    private GameTimer timer = new GameTimer();

    public static HashMap<String, AudioClip> soundEffects = GameMediaPlayer.getInstance().soundEffects;

    private final BooleanProperty canUndo = new SimpleBooleanProperty(true);

    public static BooleanProperty isCheating = new SimpleBooleanProperty(false);

    // undo
    private final KeyCombination keyCombCtrZ = new KeyCodeCombination(KeyCode.Z, KeyCombination.SHORTCUT_DOWN);

    // mute
    private final KeyCombination keyCombCtrM = new KeyCodeCombination(KeyCode.M, KeyCombination.SHORTCUT_DOWN);

    // save
    private final KeyCombination keyCombCtrS = new KeyCodeCombination(KeyCode.S, KeyCombination.SHORTCUT_DOWN);


    public void initialize() throws IllegalStateException {

        isCheating.setValue(false);

        Background_Image.setImage(ResourceFactory.getRandomBackgroundImage());

        super.disableButton("High score");

        // this.isLevelComplete.bindBidirectional(gameDocument.isLevelComplete());
        soundEffects.put("UNMOVABLE_AUDIO_CLIP_MEDIA", (AudioClip)ResourceFactory.getResource("UNMOVABLE_AUDIO_CLIP", ResourceType.AudioClip));
        soundEffects.put("MOVE_AUDIO_CLIP_MEDIA", (AudioClip)ResourceFactory.getResource("MOVE_AUDIO_CLIP", ResourceType.AudioClip));
        soundEffects.put("MOVE_CRATE_AUDIO_CLIP_MEDIA", (AudioClip)ResourceFactory.getResource("MOVE_CRATE_AUDIO_CLIP", ResourceType.AudioClip));
        soundEffects.put("LEVEL_COMPLETE_AUDIO_CLIP", (AudioClip)ResourceFactory.getResource("LEVEL_COMPLETE_AUDIO_CLIP",ResourceType.AudioClip));
        soundEffects.put("GAME_COMPLETE_AUDIO_CLIP", (AudioClip)ResourceFactory.getResource("GAME_COMPLETE_AUDIO_CLIP",ResourceType.AudioClip));

        This_Level_Index.setText(Integer.toString(gameDocument.getCurrentLevel().getIndex()));

        All_Level_Count.setText(Integer.toString(gameDocument.getLevelsCount()));

        canUndo.addListener((observable, oldValue, newValue) -> {
            if (observable != null ) {
                if (observable.getValue()) {
                    enableButton("Undo");
                } else {
                    disableButton("Undo");
                }
            }
        });

        canUndo.bind(GameStageSaver.canUndo);

        Time_Spend.textProperty().bind(timer.timeToDisplay);

        render = new GraphicRender(stageGrid, crateGrid, playerGrid, diamondsGrid);
        render.renderMap(gameDocument.getCurrentLevel().objectsGrid, gameDocument.getCurrentLevel().diamondsGrid);
        gameDocument.getPlayer().syncIsAnimating(isAnimating);

        this.highestScore.textProperty().bind(this.gameDocument.highestScore.asString());
        StringConverter<Number> converter = new NumberStringConverter();
        Score.textProperty().bindBidirectional(this.gameDocument.movesCount, converter);

        loadBottomBar();

        soundPreferenceController = loadMusicController();

        musicControlIsShowing.bindBidirectional(soundPreferenceController.isShowing);
        soundPreferenceController.isMute.bindBidirectional(Main.prefMusicIsMute);

        addDraggableItem();
    }

    public void bindLevelGameCompleteController(LevelCompletePopUpController controller1, GameCompletePopUpController controller2, ExitPopUpController controller3) {
        this.levelCompletePopUpController = controller1;
        this.gameCompletePopUpController = controller2;
        this.exitPopUpController = controller3;
    }

    public void bindLevelGameCompleteView(BorderPane level, BorderPane game, BorderPane exit) {
        level.setVisible(false);
        game.setVisible(false);
        exit.setVisible(false);

        this.root.getChildren().add(level);
        this.root.getChildren().add(game);
        this.root.getChildren().add(exit);
    }

    private final BooleanProperty isAnimating = new SimpleBooleanProperty(false);

    private final BooleanProperty isBlur = new SimpleBooleanProperty(false);

    public void handleKey(KeyEvent event) {

        // simply ignore keys when animating
        if (isAnimating.getValue() || isBlur.getValue()) {
            return;
        }

        switch (gameStatus) {
            case READY -> {
                timer.start();
                gameStatus = GameStatus.PLAY;
            }

            case PAUSE -> {
                timer.resume();
                gameStatus = GameStatus.PLAY;
            }
            case END -> {
                return;
            }
        }

        // if is shortcuts

        if (keyCombCtrZ.match(event)) {
            this.clickUndo(null);
            return;
        }

        if (keyCombCtrM.match(event)) {
            this.clickToggleMusic(null);
            return;
        }

        if (keyCombCtrS.match(event)) {
            this.clickSaveGame(null);
            return;
        }

        Player player = gameDocument.getPlayer();
        Point direction = new Point(0, 0);

        KeyCode code = event.getCode();
        switch (code) {
            case UP, W, K -> {
                direction = new Point(0, -1);
            }

            case RIGHT, D, L -> {
                direction = new Point(1, 0);
            }

            case DOWN, S, J -> {
                direction = new Point(0, 1);
            }

            case LEFT, A, H -> {
                direction = new Point(-1, 0);
            }
        }
        isAnimating.set(true);

        if (isCheating.getValue()) {
            Crate cheatingCrate = GraphicRender.selectedCrate;
            if (code == KeyCode.SPACE) {
                cheatingCrate.settleDown();
                return;
            }
            if (cheatingCrate.canMoveBy(direction)) {
                try {
                    cheatingCrate.moveBy(direction);
                } catch (IllegalMovementException e) {
                    e.printStackTrace();
                }
            } else {
                cheatingCrate.shakeAnimation(direction);
            }
            return;
        }

        gameDocument.serializeCurrentState();
        GameDebugger.logMovement(this.gameDocument, direction);

        if (player.canMoveBy(direction)) {
            try {
                this.gameDocument.getPlayer().moveBy(direction);
                this.gameDocument.movesCount.set(this.gameDocument.movesCount.getValue() + 1);
            } catch (IllegalMovementException e) {
                e.printStackTrace();
            }
        } else {
            player.headTo(direction);
            player.shakeAnimation(direction);
        }

        checkIsLevelComplete();
    }

    @FXML
    private void clickToggleDebug() {
        menuBarClickToggleDebug();
    }

    private void checkIsLevelComplete() {
        if (gameDocument.isLevelComplete()) {
            GameViewController.soundEffects.get("LEVEL_COMPLETE_AUDIO_CLIP").play();
            this.timer.pause();
            this.gameStatus = GameStatus.PAUSE;

            GameDebugger.logLevelComplete(this.gameDocument.getCurrentLevel(), this.Time_Spend.getText(), this.Score.getText());

            gaussianBlur();
            levelCompletePopUpController.assignData(
                    Integer.toString(this.gameDocument.getCurrentLevel().getIndex()),
                    this.Time_Spend.getText(),
                    this.Score.getText()
            );

            levelCompletePopUpController.show();
            levelCompletePopUpController.Next_Level_Button.setOnMouseClicked((event) -> {
                levelCompletePopUpController.hide();
                exitGaussianBlur();
                switchToNextLevel();
            });
        }
    }

    private void checkIsGameComplete() {
        if (gameDocument.isGameComplete()) {
            GameViewController.soundEffects.get("GAME_COMPLETE_AUDIO_CLIP").play();
            timer.stop();
            gameStatus = GameStatus.END;

            GameDebugger.logGameComplete(gameDocument.getLevelsCount(), this.Time_Spend.getText(), this.Score.getText());

            gaussianBlur();
            gameCompletePopUpController.assignData(
                    timer.getTime(),
                    this.gameDocument.movesCount.getValue()
            );

            gameCompletePopUpController.show();
            gameCompletePopUpController.Save_Record.setOnMouseClicked((event) -> {
                String saveName = gameCompletePopUpController.inputPlayerName.toString();

                gameDocument.saveRecord(saveName, this.Time_Spend.getText(), this.Score.getText());
            });

            gameCompletePopUpController.Level_Complete_High_Score_List.setOnMouseClicked((event) -> {
                gameCompletePopUpController.hide();
                exitGaussianBlur();
                // TODO: high score list
            });

            gameCompletePopUpController.Level_Complete_Back_To_Menu.setOnMouseClicked((event) -> {
                levelCompletePopUpController.hide();
                exitGaussianBlur();
                gameDocument.restoreObject(GameStageSaver.getInitialState());
                GameStageSaver.clear();

                player.setMusic(MediaState.STOP);
                Main.primaryStage.setScene(Main.menuScene);
                player.setMusic(MediaState.PLAY);

            });
        }
    }

    private void switchToNextLevel() {
        if (gameDocument.isGameComplete()) {
            checkIsGameComplete();
            return;
        }

        gameDocument.changeToNextLevel();
        Background_Image.setImage(ResourceFactory.getRandomBackgroundImage());

        This_Level_Index.setText(Integer.toString(gameDocument.getCurrentLevel().getIndex()));

        render.renderMap(gameDocument.getCurrentLevel().objectsGrid, gameDocument.getCurrentLevel().diamondsGrid);
        gameDocument.getPlayer().syncIsAnimating(isAnimating);
    }

    @FXML
    private void clickToggleMusic(MouseEvent mouseEvent) {
        menuBarClickToggleMusic();
    }

    @FXML
    private void clickUndo(MouseEvent mouseEvent) {
        GameDebugger.logLevelComplete(this.gameDocument.getCurrentLevel(), this.Time_Spend.getText(), this.Score.getText());

        GameDocument restoreObject = GameStageSaver.pop();
        if (restoreObject != null) {
            Main.gameDocument.restoreObject(restoreObject);
            render.renderMap(gameDocument.getCurrentLevel().objectsGrid, gameDocument.getCurrentLevel().diamondsGrid);
            GameDebugger.logUndo(this.gameDocument);
        }
    }

    @FXML
    private void clickSaveGame(MouseEvent mouseEvent) {
        GameStageSaver.saveToFile(this.gameDocument);
    }

    @FXML
    private void clickToMenu(MouseEvent mouseEvent) {

        gaussianBlur();
        exitPopUpController.show();

        exitPopUpController.Confirm_Exit_Exit.setOnMouseClicked((event) -> {
            timer.stop();

            exitPopUpController.hide();
            exitGaussianBlur();
            gameDocument.restoreObject(GameStageSaver.getInitialState());
            GameStageSaver.clear();

            player.setMusic(MediaState.STOP);
            Main.primaryStage.setScene(Main.menuScene);
            player.setMusic(MediaState.PLAY);
        });

        exitPopUpController.Confirm_Exit_Back.setOnMouseClicked((event) -> {
            exitPopUpController.hide();
            exitGaussianBlur();
        });

    }

    private void exitGaussianBlur() {
        Can_Blur_Group.setEffect(null);
        this.isBlur.setValue(false);
    }

    private void gaussianBlur() {
        GaussianBlur gaussianBlur = new GaussianBlur();
        gaussianBlur.setRadius(20);
        Can_Blur_Group.setEffect(gaussianBlur);
        this.isBlur.setValue(true);
    }

    @FXML
    private void clickHighScoreList(MouseEvent mouseEvent) {
        menuBarClickToggleHighScoreList();
    }


    private void detectDragging(MouseEvent mouseEvent) {
        Double x = mouseEvent.getSceneX() + 20 - 195;
        Double y = mouseEvent.getSceneY() + 15 - 55;
        Integer xIndex = (int) (x / 48);
        Integer yIndex = (int) (y / 30);
        int XBound = gameDocument.getCurrentLevel().objectsGrid.getX();
        int YBound = gameDocument.getCurrentLevel().objectsGrid.getY();

        if (xIndex < 0 || xIndex >= XBound || yIndex < 0 || yIndex >= YBound) {
            restoreDraggingPosition();
            return;
        }
        AbstractGameObject clickedObject = gameDocument.getCurrentLevel().objectsGrid.getGameObjectAt(xIndex, yIndex);
        if (clickedObject instanceof Wall){
            System.out.println("at Wall" + clickedObject.xPosition + clickedObject.yPosition);
            gameDocument.getCurrentLevel().objectsGrid.putGameObjectAt(new Floor(gameDocument.getCurrentLevel().objectsGrid, xIndex, yIndex), new Point(xIndex, yIndex));
            render.renderMap(gameDocument.getCurrentLevel().objectsGrid, gameDocument.getCurrentLevel().diamondsGrid);
        } else {
            restoreDraggingPosition();
            return;
        }
    }

    private void restoreDraggingPosition() {
        this.DragStack1.setLayoutX(1234);
        this.DragStack1.setLayoutY(78);
        this.DragStack2.setLayoutX(1234);
        this.DragStack2.setLayoutY(144);
    }

    private void addDraggableItem() {
        DragStack1.setOnMouseReleased(this::detectDragging);
        DragStack1.setOnMouseDragged(this::previewDragging);
    }

    private final Rectangle rectangle = new Rectangle(48, 30, Color.gray(0.4));

    private void previewDragging(MouseEvent mouseEvent) {

        Double x = mouseEvent.getSceneX() + 20 - 195;
        Double y = mouseEvent.getSceneY() + 15 - 55;
        Integer xIndex = (int) (x / 48);
        Integer yIndex = (int) (y / 30);
        int XBound = gameDocument.getCurrentLevel().objectsGrid.getX();
        int YBound = gameDocument.getCurrentLevel().objectsGrid.getY();

        previewGrid.getChildren().clear();

        if (xIndex < 0 || xIndex >= XBound || yIndex < 0 || yIndex >= YBound) {
            DragStack1.setLayoutX(mouseEvent.getSceneX());
            DragStack1.setLayoutY(mouseEvent.getSceneY());
            return;
        }
        AbstractGameObject clickedObject = gameDocument.getCurrentLevel().objectsGrid.getGameObjectAt(xIndex, yIndex);
        if (clickedObject instanceof Wall){
            DragStack1.setOpacity(0.3);
            DragStack1.setLayoutX(mouseEvent.getSceneX());
            DragStack1.setLayoutY(mouseEvent.getSceneY());
            previewGrid.add(rectangle, xIndex, yIndex);
        } else {
            DragStack1.setLayoutX(mouseEvent.getSceneX());
            DragStack1.setLayoutY(mouseEvent.getSceneY());
            return;
        }
    }
}

enum GameStatus {
    READY, PAUSE, PLAY, END;
};



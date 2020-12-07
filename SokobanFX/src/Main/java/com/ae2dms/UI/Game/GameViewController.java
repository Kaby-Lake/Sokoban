package com.ae2dms.UI.Game;

import com.ae2dms.Business.*;
import com.ae2dms.GameObject.Objects.Crate;
import com.ae2dms.GameObject.Objects.IllegalMovementException;
import com.ae2dms.GameObject.Objects.Player;
import com.ae2dms.IO.ResourceFactory;
import com.ae2dms.Main.Main;
import com.ae2dms.UI.AbstractBarController;
import com.ae2dms.UI.Game.PopUps.ExitPopUpController;
import com.ae2dms.UI.Game.PopUps.GameCompletePopUpController;
import com.ae2dms.UI.Game.PopUps.LevelCompletePopUpController;
import com.ae2dms.UI.GameMediaPlayer;
import com.ae2dms.UI.MediaState;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.fxml.FXML;
import javafx.scene.Group;
import javafx.scene.control.Label;
import javafx.scene.effect.GaussianBlur;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.KeyCombination;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.media.AudioClip;
import javafx.util.StringConverter;
import javafx.util.converter.NumberStringConverter;

import java.awt.*;
import java.util.HashMap;

import static com.ae2dms.Business.GameDocument.logger;

/**
 * The Controller of GameView in MVC pattern.
 */
public class GameViewController extends AbstractBarController {

    /**
     * JavaFX BorderPane for LevelCompletePopUp
     */
    @FXML
    protected BorderPane LevelCompletePopUp;

    /**
     * Controller for LevelCompletePopUp
     *
     * @see LevelCompletePopUpController
     */
    @FXML
    protected LevelCompletePopUpController LevelCompletePopUpController;

    /**
     * JavaFX BorderPane for GameCompletePopUp
     */
    @FXML
    protected BorderPane GameCompletePopUp;

    /**
     * Controller for GameCompletePopUp
     *
     * @see GameCompletePopUpController
     */
    @FXML
    protected GameCompletePopUpController GameCompletePopUpController;

    /**
     * JavaFX BorderPane for ExitPopUp
     */
    @FXML
    protected BorderPane ExitPopUp;

    /**
     * Controller for ExitPopUp
     *
     * @see ExitPopUpController
     */
    @FXML
    protected ExitPopUpController ExitPopUpController;

    /**
     * JavaFX Pane for DraggableFloor
     */
    @FXML
    private Pane DraggableFloor;

    /**
     * Controller for DraggableFloor
     * @see DraggableFloorController
     */
    @FXML
    private DraggableFloorController DraggableFloorController;

    /**
     * Content Group, used for blurring when pop up shows
     */
    @FXML
    private Group Can_Blur_Group;

    /**
     * The Background Image of this game, will dynamically change between levels
     */
    @FXML
    private ImageView Background_Image;




    /**
     * Text for Binding: Score of current game, on the top_right
     */
    @FXML
    private Label Score;

    /**
     * Text for Binding: Time duration spend, on the top-right
     */
    @FXML
    private Label Time_Spend;

    /**
     * Text for Binding: index for this level, on the top
     */
    @FXML
    private Label This_Level_Index;

    /**
     * Text for Binding: count for all levels, on the top
     */
    @FXML
    private Label All_Level_Count;




    /**
     * The JavaFX GridPane as stageGrid to add Wall and Floor, assigned in constructor
     */
    @FXML
    private GridPane stageGrid;

    /**
     * The JavaFX GridPane as objectsGrid to add Crate and Player, assigned in constructor
     */
    @FXML
    private GridPane objectsGrid;

    /**
     * The JavaFX GridPane as diamondsGrid to add Diamond, assigned in constructor
     */
    @FXML
    private GridPane diamondsGrid;

    /**
     * The JavaFX GridPane as candyGrid to add Candy, assigned in constructor
     */
    @FXML
    private GridPane candyGrid;


    /**
     * GameMediaPlayer instance
     */
    private GameMediaPlayer player = GameMediaPlayer.getInstance();

    /**
     * GameDocument Instance
     */
    private GameDocument gameDocument = Main.gameDocument;

    /**
     * GraphicRender to render maps
     */
    public static GraphicRender render;

    /**
     * The current game status
     * @see GameStatus
     */
    private GameStatus gameStatus = GameStatus.READY;

    /**
     * The concurrent game timer to count time
     */
    private GameTimer timer = new GameTimer();

    /**
     * The hashmap to store all sound effects, can get the AudioClip by searching the name
     * with (SFX Name, AudioClip) pair
     */
    public static HashMap<String, AudioClip> soundEffects = GameMediaPlayer.getInstance().soundEffects;

    /**
     * whether can undo at this status
     * bind to view to determine if the undo button can click
     * bind to GameStageSaver.canUndo
     */
    private final BooleanProperty canUndo = new SimpleBooleanProperty(true);

    /**
     * whether is Cheating at this time (Crate is left up for moving)
     */
    public static BooleanProperty isCheating = new SimpleBooleanProperty(false);

    /**
     * KeyCombination of Ctrl(Command) + Z for undo
     */
    private final KeyCombination keyCombCtrZ = new KeyCodeCombination(KeyCode.Z, KeyCombination.SHORTCUT_DOWN);

    /**
     * KeyCombination of Ctrl(Command) + M for Music
     */
    private final KeyCombination keyCombCtrM = new KeyCodeCombination(KeyCode.M, KeyCombination.SHORTCUT_DOWN);

    /**
     * KeyCombination of Ctrl(Command) + S for Save
     */
    private final KeyCombination keyCombCtrS = new KeyCodeCombination(KeyCode.S, KeyCombination.SHORTCUT_DOWN);


    /**
     * Initialize the GameView and Controller
     * set the background image to a random one
     * bind the level index, level count, steps and time count with the one in GameDocument
     * render the game map with the map in GameDocument
     */
    public void initialize() {
        player.setMusic(MediaState.STOP);
        player.setMusic(MediaState.PLAY);

        isCheating.setValue(false);

        Background_Image.setImage(ResourceFactory.getRandomBackgroundImage());

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

        render = new GraphicRender(stageGrid, objectsGrid, diamondsGrid, candyGrid);
        render.renderMap(gameDocument.getCurrentLevel());
        StringConverter<Number> converter = new NumberStringConverter();
        bestRecord.textProperty().bindBidirectional(this.gameDocument.bestRecord, converter);
        Score.textProperty().bindBidirectional(this.gameDocument.movesCount, converter);
    }

    /**
     * whether at this time the animation is happening or not
     * will ignore all key events if still animating
     */
    public final static transient BooleanProperty isAnimating = new SimpleBooleanProperty(false);

    /**
     * whether at this time the gameView is blurred or not
     * will ignore all key events if still animating
     */
    private final BooleanProperty isBlur = new SimpleBooleanProperty(false);

    /**
     * handle the key event in the game view
     * like the WSAD, Up Down Left and Right
     *
     * @param event keyboard event
     */
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
            this.clickUndo();
            return;
        }

        if (keyCombCtrM.match(event)) {
            this.menuBarClickMusic();
            return;
        }

        if (keyCombCtrS.match(event)) {
            this.clickSaveGame();
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
            case SPACE -> {
            }
            default -> {
                return;
            }
        }
        isAnimating.set(true);

        if (isCheating.getValue()) {
            Crate cheatingCrate = GraphicRender.selectedCrate;
            if (code == KeyCode.SPACE) {
                cheatingCrate.settleDown();
                checkIsLevelComplete();
                return;
            }
            if (cheatingCrate.canMoveBy(direction)) {
                try {
                    cheatingCrate.moveBy(direction);
                } catch (IllegalMovementException e) {
                    logger.severe(e.getMessage());
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
                if (player.isOnCandy() ){
                    player.eatingCrate();
                    DraggableFloorController.addDraggableItem();
                }
            } catch (IllegalMovementException e) {
                logger.severe(e.getMessage());
            }
        } else {
            player.headTo(direction);
            player.shakeAnimation(direction);
        }
        checkIsLevelComplete();
    }

    /**
     * Check if this level is complete
     * if so, pop up the LevelCompletePopUp with steps and time duration
     */
    private void checkIsLevelComplete() {
        if (gameDocument.isLevelComplete()) {
            GameViewController.soundEffects.get("LEVEL_COMPLETE_AUDIO_CLIP").play();
            this.timer.pause();
            this.gameStatus = GameStatus.PAUSE;

            GameDebugger.logLevelComplete(this.gameDocument.getCurrentLevel(), this.Time_Spend.getText(), this.Score.getText());

            gaussianBlur();
            LevelCompletePopUpController.assignData(
                    Integer.toString(this.gameDocument.getCurrentLevel().getIndex()),
                    this.Time_Spend.getText(),
                    this.Score.getText()
            );

            LevelCompletePopUpController.show();
            LevelCompletePopUpController.Next_Level_Button.setOnMouseClicked((event) -> {
                LevelCompletePopUpController.hide();
                exitGaussianBlur();
                switchToNextLevel();
            });
        }
    }

    /**
     * Check if this Game is complete
     * if so, pop up the GameCompletePopUp with steps and time duration, with text field where user can type in names to save record
     */
    private void checkIsGameComplete() {
        if (gameDocument.isGameComplete()) {
            GameViewController.soundEffects.get("GAME_COMPLETE_AUDIO_CLIP").play();
            timer.stop();
            gameStatus = GameStatus.END;

            GameDebugger.logGameComplete(gameDocument.getLevelsCount(), this.Time_Spend.getText(), this.Score.getText());

            gaussianBlur();
            GameCompletePopUpController.assignData(
                    timer.getTime(),
                    this.gameDocument.movesCount.getValue()
            );

            GameCompletePopUpController.show();

            GameCompletePopUpController.Level_Complete_High_Score_List.setOnMouseClicked((event) -> {
                GameCompletePopUpController.hide();
                exitGaussianBlur();
                menuBarClickHighScoreList();
            });

            // the save code is in GameViewController

            GameCompletePopUpController.Level_Complete_Back_To_Menu.setOnMouseClicked((event) -> {
                LevelCompletePopUpController.hide();
                exitGaussianBlur();
                gameDocument.restoreObject(GameStageSaver.getInitialState());
                GameStageSaver.clear();

                player.setMusic(MediaState.STOP);
                Main.primaryStage.setScene(Main.menuScene);
                player.setMusic(MediaState.PLAY);

            });
        }
    }

    /**
     * Change to the next level, if no more level, then call checkIsGameComplete()
     */
    private void switchToNextLevel() {
        if (gameDocument.isGameComplete()) {
            checkIsGameComplete();
            return;
        }

        gameDocument.changeToNextLevel();
        Background_Image.setImage(ResourceFactory.getRandomBackgroundImage());

        This_Level_Index.setText(Integer.toString(gameDocument.getCurrentLevel().getIndex()));

        render.renderMap(gameDocument.getCurrentLevel());
    }

    /**
     * click undo button
     */
    @FXML
    private void clickUndo() {
        if (gameDocument.undo()) {
            render.renderMap(gameDocument.getCurrentLevel());
            DraggableFloorController.refreshDragList();
        }
    }

    /**
     * click save game button
     */
    @FXML
    private void clickSaveGame() {
        GameStageSaver.saveToFile(this.gameDocument);
    }

    /**
     * click back to menu button
     * will pop up a alert where you can save your game with advice
     */
    @FXML
    private void clickToMenu() {

        gaussianBlur();
        ExitPopUpController.show();

        ExitPopUpController.Confirm_Exit_Exit.setOnMouseClicked((event) -> {
            timer.stop();

            ExitPopUpController.hide();
            exitGaussianBlur();
            gameDocument.restoreObject(GameStageSaver.getInitialState());
            GameStageSaver.clear();

            player.setMusic(MediaState.STOP);
            Main.primaryStage.setScene(Main.menuScene);
            player.setMusic(MediaState.PLAY);
        });

        ExitPopUpController.Confirm_Exit_Back.setOnMouseClicked((event) -> {
            ExitPopUpController.hide();
            exitGaussianBlur();
        });

    }

    /**
     * exit the state where game view is in Gaussian Blur
     */
    private void exitGaussianBlur() {
        Can_Blur_Group.setEffect(null);
        this.isBlur.setValue(false);
    }

    /**
     * enter the state where game view is in Gaussian Blur
     */
    private void gaussianBlur() {
        GaussianBlur gaussianBlur = new GaussianBlur();
        gaussianBlur.setRadius(20);
        Can_Blur_Group.setEffect(gaussianBlur);
        this.isBlur.setValue(true);
    }
}

/**
 * The enum Game status.
 */
enum GameStatus {
    /**
     * Ready game status.
     */
    READY,
    /**
     * Pause game status.
     */
    PAUSE,
    /**
     * Play game status.
     */
    PLAY,
    /**
     * End game status.
     */
    END;
};



package com.ae2dms.UI.Game;

import com.ae2dms.Business.*;
import com.ae2dms.GameObject.Objects.IllegalMovementException;
import com.ae2dms.GameObject.Objects.Player;
import com.ae2dms.IO.ResourceFactory;
import com.ae2dms.IO.ResourceType;
import com.ae2dms.Main.Main;
import com.ae2dms.UI.AbstractBarController;
import com.ae2dms.UI.MediaState;
import com.ae2dms.UI.Menu.MenuView;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.fxml.FXML;
import javafx.scene.Group;
import javafx.scene.control.Label;
import javafx.scene.effect.GaussianBlur;
import javafx.scene.image.ImageView;
import javafx.scene.input.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.media.AudioClip;

import java.awt.*;
import java.util.HashMap;

public class GameViewController extends AbstractBarController {

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



    private volatile GameDocument gameDocument = Main.gameDocument;

    public volatile static GraphicRender render;

    private GameStatus gameStatus = GameStatus.READY;

    private GameTimer timer = new GameTimer();

    public static HashMap<String, AudioClip> soundEffects = new HashMap<>();

    private final BooleanProperty canUndo = new SimpleBooleanProperty(true);

    // undo
    private final KeyCombination keyCombCtrZ = new KeyCodeCombination(KeyCode.Z, KeyCombination.SHORTCUT_DOWN);

    // mute
    private final KeyCombination keyCombCtrM = new KeyCodeCombination(KeyCode.M, KeyCombination.SHORTCUT_DOWN);

    // save
    private final KeyCombination keyCombCtrS = new KeyCodeCombination(KeyCode.S, KeyCombination.SHORTCUT_DOWN);


    public void initialize() throws IllegalStateException {

        Background_Image.setImage(ResourceFactory.getRandomBackgroundImage());

        super.disableButton("High score");

        // this.isLevelComplete.bindBidirectional(gameDocument.isLevelComplete());
        soundEffects.put("UNMOVABLE_AUDIO_CLIP_MEDIA", (AudioClip)ResourceFactory.getResource("UNMOVABLE_AUDIO_CLIP", ResourceType.AudioClip));
        soundEffects.put("MOVE_AUDIO_CLIP_MEDIA", (AudioClip)ResourceFactory.getResource("MOVE_AUDIO_CLIP", ResourceType.AudioClip));
        soundEffects.put("MOVE_CRATE_AUDIO_CLIP_MEDIA", (AudioClip)ResourceFactory.getResource("MOVE_CRATE_AUDIO_CLIP", ResourceType.AudioClip));
        soundEffects.put("LEVEL_COMPLETE_AUDIO_CLIP", (AudioClip)ResourceFactory.getResource("LEVEL_COMPLETE_AUDIO_CLIP",ResourceType.AudioClip));

        This_Level_Index.setText(Integer.toString(gameDocument.getCurrentLevel().getIndex()));

        All_Level_Count.setText(Integer.toString(gameDocument.getLevelsCount()));

        musicIsMute.addListener((observable, oldValue, newValue) -> {
            if (observable != null ) {
                boolean isMute = observable.getValue();
                GameView.backgroundMusicPlayer.setMute(isMute);
                for (AudioClip mediaPlayer : soundEffects.values()) {
                    mediaPlayer.setVolume(isMute ? 0 : 100);
                }
            }
        });

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
        Score.textProperty().bind(this.gameDocument.movesCount.asString());

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

    public void handleKey(KeyEvent event) {

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

        // simply ignore keys when animating
        if (isAnimating.getValue()) {
            return;
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
            case UP -> {
                direction = new Point(0, -1);
            }

            case RIGHT -> {
                direction = new Point(1, 0);
            }

            case DOWN -> {
                direction = new Point(0, 1);
            }

            case LEFT -> {
                direction = new Point(-1, 0);
            }

            default -> {
                // TODO: implement something funny.
                return;
            }

        }
        isAnimating.set(true);
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
                Can_Blur_Group.setEffect(null);
                switchToNextLevel();
            });


            gaussianBlur();
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
                    this.Time_Spend.getText(),
                    this.Score.getText()
            );

            gameCompletePopUpController.show();
            gameCompletePopUpController.Save_Record.setOnMouseClicked((event) -> {
                String saveName = gameCompletePopUpController.name.toString();

                gameDocument.saveRecord(saveName, this.Time_Spend.getText(), this.Score.getText());
            });

            gameCompletePopUpController.Level_Complete_High_Score_List.setOnMouseClicked((event) -> {
                gameCompletePopUpController.hide();
                Can_Blur_Group.setEffect(null);
                // TODO: high score list
            });

            gameCompletePopUpController.Level_Complete_Back_To_Menu.setOnMouseClicked((event) -> {
                levelCompletePopUpController.hide();
                Can_Blur_Group.setEffect(null);
                gameDocument.restoreObject(GameStageSaver.getInitialState());
                GameStageSaver.clear();

                GameView.backgroundMusicPlayer.stop();
                Main.primaryStage.setScene(Main.menuScene);
                MenuView.getInstance().setMusic(MediaState.PLAY);

            });
        }
    }

    private void switchToNextLevel() {
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
            Can_Blur_Group.setEffect(null);
            gameDocument.restoreObject(GameStageSaver.getInitialState());
            GameStageSaver.clear();

            GameView.backgroundMusicPlayer.stop();
            Main.primaryStage.setScene(Main.menuScene);
            MenuView.getInstance().setMusic(MediaState.PLAY);
        });

        exitPopUpController.Confirm_Exit_Back.setOnMouseClicked((event) -> {
            exitPopUpController.hide();
            Can_Blur_Group.setEffect(null);
        });

    }

    private void gaussianBlur() {
        GaussianBlur gaussianBlur = new GaussianBlur();
        gaussianBlur.setRadius(20);
        Can_Blur_Group.setEffect(gaussianBlur);
    }
}

enum GameStatus {
    READY, PAUSE, PLAY, END;
};



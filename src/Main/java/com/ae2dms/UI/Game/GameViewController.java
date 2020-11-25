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
import javafx.scene.media.AudioClip;

import java.awt.*;
import java.util.HashMap;

public class GameViewController extends AbstractBarController {

    //Group: Confirm Exit Pop Up


    @FXML
    private BorderPane Confirm_Exit_Pop_Up;

    @FXML
    private ImageView Confirm_Exit_Exit;

    @FXML
    private ImageView Confirm_Exit_Back;


    //TextBinding: Score of current game, top_right

    @FXML
    private Label Score;

    @FXML
    private Label Time_Spend;


    //Group: Level Complete Pop Up

    @FXML
    private BorderPane Level_Complete_Pop_Up;

    @FXML
    private ImageView Next_Level_Button;

    @FXML
    private Label Level_Complete_Level_Index;

    @FXML
    private Label Level_Complete_Time;

    @FXML
    private Label Level_Complete_Score;


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

            Level_Complete_Level_Index.setText(Integer.toString(this.gameDocument.getCurrentLevel().getIndex()));
            Level_Complete_Time.setText(this.Time_Spend.getText());
            Level_Complete_Score.setText(this.Score.getText());
            Level_Complete_Pop_Up.getStyleClass().clear();

            GaussianBlur gaussianBlur = new GaussianBlur();
            gaussianBlur.setRadius(20);
            Can_Blur_Group.setEffect(gaussianBlur);

            Next_Level_Button.setOnMouseClicked((event) -> {
                Level_Complete_Pop_Up.getStyleClass().add("Hide");
                Can_Blur_Group.setEffect(null);
                switchToNextLevel();
            });
        }
    }

    private void checkIsGameComplete() {
        if (gameDocument.isGameComplete()) {

            timer.stop();
            gameStatus = GameStatus.END;

            // TODO:


            Level_Complete_Level_Index.setText(Integer.toString(gameDocument.getCurrentLevel().getIndex()));
            Level_Complete_Time.setText("13");
            Level_Complete_Score.setText(this.gameDocument.movesCount.toString());
            Level_Complete_Pop_Up.getStyleClass().clear();

            GaussianBlur gaussianBlur = new GaussianBlur();
            gaussianBlur.setRadius(20);
            Can_Blur_Group.setEffect(gaussianBlur);

            Next_Level_Button.setOnMouseClicked((event) -> {
                Level_Complete_Pop_Up.getStyleClass().add("Hide");
                Can_Blur_Group.setEffect(null);
                switchToNextLevel();
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
        GameDocument restoreObject = GameStageSaver.pop();
        if (restoreObject != null) {
            Main.gameDocument.restoreObject(restoreObject);
            render.renderMap(gameDocument.getCurrentLevel().objectsGrid, gameDocument.getCurrentLevel().diamondsGrid);
        }
    }

    @FXML
    private void clickSaveGame(MouseEvent mouseEvent) {
        GameStageSaver.saveToFile(this.gameDocument);
    }

    @FXML
    private void clickToMenu(MouseEvent mouseEvent) {

        Confirm_Exit_Pop_Up.getStyleClass().clear();

        GaussianBlur gaussianBlur = new GaussianBlur();
        gaussianBlur.setRadius(20);
        Can_Blur_Group.setEffect(gaussianBlur);

        Confirm_Exit_Exit.setOnMouseClicked((event) -> {
            timer.stop();

            Confirm_Exit_Pop_Up.getStyleClass().add("Hide");
            Can_Blur_Group.setEffect(null);
            gameDocument.restoreObject(GameStageSaver.getInitialState());
            GameStageSaver.clear();

            GameView.backgroundMusicPlayer.stop();
            Main.primaryStage.setScene(Main.menuScene);
            MenuView.getInstance().setMusic(MediaState.PLAY);
        });

        Confirm_Exit_Back.setOnMouseClicked((event) -> {
            Confirm_Exit_Pop_Up.getStyleClass().add("Hide");
            Can_Blur_Group.setEffect(null);
        });

    }
}

enum GameStatus {
    READY, PAUSE, PLAY, END;
};



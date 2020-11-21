package com.ae2dms.UI.Game;

import com.ae2dms.Business.GameDocument;
import com.ae2dms.Business.GameStageSaver;
import com.ae2dms.Business.GraphicRender;
import com.ae2dms.GameObject.Objects.*;
import com.ae2dms.IO.ResourceFactory;
import com.ae2dms.Main.Main;
import com.ae2dms.UI.AbstractBarController;
import com.ae2dms.UI.Menu.MenuView;
import javafx.animation.TranslateTransition;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.media.AudioClip;
import javafx.util.Duration;

import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;

public class GameViewController extends AbstractBarController {

    @FXML
    private ImageView backgroundImage;

    @FXML
    private Label this_level;

    @FXML
    private Label all_level;

    private volatile GameDocument gameDocument = Main.gameDocument;

    public volatile static GraphicRender render;

    @FXML
    private volatile GridPane stageGrid;

    @FXML
    private volatile GridPane crateGrid;

    @FXML
    private volatile GridPane playerGrid;

    @FXML
    private volatile GridPane diamondsGrid;

    private final IntegerProperty highestScoreValue = new SimpleIntegerProperty();

    public static HashMap<String, AudioClip> soundEffects = new HashMap<>();

    private BooleanProperty isLevelComplete;

    private BooleanProperty canUndo = new SimpleBooleanProperty(true);

    public void initialize() throws IllegalStateException {

        backgroundImage.setImage(ResourceFactory.randomBackgroundImage());

        super.disableButton("High score");

        this.highestScoreValue.setValue(gameDocument.highestScore);
        this.bindHighestScore(highestScoreValue);
        // this.isLevelComplete.bindBidirectional(gameDocument.isLevelComplete());
        soundEffects.put("UNMOVABLE_AUDIO_CLIP_MEDIA", ResourceFactory.UNMOVABLE_AUDIO_CLIP);
        soundEffects.put("MOVE_AUDIO_CLIP_MEDIA", ResourceFactory.MOVE_AUDIO_CLIP);

        this_level.setText(Integer.toString(gameDocument.getCurrentLevel().getIndex()));

        all_level.setText(Integer.toString(gameDocument.getLevelsCount()));

        musicSwitchToggle.addListener((observable, oldValue, newValue) -> {
            if (observable != null ) {
                boolean isMute = !observable.getValue();
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

        render = new GraphicRender(stageGrid, crateGrid, playerGrid, diamondsGrid);
        render.renderMap(gameDocument.getCurrentLevel().objectsGrid, gameDocument.getCurrentLevel().diamondsGrid);
        gameDocument.getPlayer().syncIsAnimating(isAnimating);
    }

    private BooleanProperty isAnimating = new SimpleBooleanProperty(false);

    public void handleKey(KeyCode code) {
        // simply ignore keys when animating
        if (isAnimating.getValue()) {
            return;
        }
        gameDocument.serializeObject();
        canUndo.set(!GameStageSaver.isEmpty());
        Player player = gameDocument.getPlayer();
        Point direction = new Point(0, 0);
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

        if (player.canMoveBy(direction)) {
            try {
                this.gameDocument.getPlayer().moveBy(direction);
            } catch (IllegalMovementException e) {
                e.printStackTrace();
            }
        } else {
            player.shakeAnimation(direction);
        }
//
//        if (GameDocument.isDebugActive()) {
//            System.out.println(code);
        checkIsLevelComplete();
    }

    @FXML
    private void clickToggleDebug() {
//        gameDocument.toggleDebug(menuBarClickToggleDebug());
    }

    private void checkIsLevelComplete() {
        if (gameDocument.isLevelComplete()) {
            gameDocument.changeToNextLevel();
        }
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
            canUndo.set(!GameStageSaver.isEmpty());
            render.renderMap(gameDocument.getCurrentLevel().objectsGrid, gameDocument.getCurrentLevel().diamondsGrid);
        } else {

        }

    }

    @FXML
    private void clickSaveGame(MouseEvent mouseEvent) {
    }

    @FXML
    private void clickToMenu(MouseEvent mouseEvent) {
        GameView.backgroundMusicPlayer.stop();
        Main.primaryStage.setScene(Main.menuScene);
        MenuView.backgroundMusicPlayer.play();
    }

}

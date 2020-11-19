package com.ae2dms.UI.Game;

import com.ae2dms.Business.Data.GameGrid;
import com.ae2dms.Business.GameDocument;
import com.ae2dms.Business.GameStageSaver;
import com.ae2dms.GameObject.AbstractGameObject;
import com.ae2dms.GameObject.Objects.*;
import com.ae2dms.IO.ResourceFactory;
import com.ae2dms.Main.Main;
import com.ae2dms.UI.AbstractBarController;
import com.ae2dms.UI.Menu.MenuView;
import javafx.animation.ParallelTransition;
import javafx.animation.ScaleTransition;
import javafx.animation.TranslateTransition;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.fxml.FXML;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.media.AudioClip;
import javafx.scene.media.MediaPlayer;
import javafx.util.Duration;

import java.awt.*;
import java.io.IOException;
import java.util.HashMap;

public class GameViewController extends AbstractBarController {

    private final GameDocument gameDocument = Main.gameDocument;

    @FXML
    private GridPane stageGrid;

    @FXML
    private GridPane itemGrid;

    @FXML
    private GridPane playerGrid;

    @FXML
    private GridPane diamondsGrid;

    private ImageView keeperImageView;

    private final HashMap<Crate, ImageView> crateImageViewMap = new HashMap<>();

    private final IntegerProperty highestScoreValue;

    private HashMap<String, AudioClip> soundEffects = new HashMap<>();

    private BooleanProperty isLevelComplete;

    private BooleanProperty canUndo = new SimpleBooleanProperty(true);

    public GameViewController() {
        this.highestScoreValue = new SimpleIntegerProperty();
    }

    public void initialize() throws IllegalStateException {

        super.disableButton("High score");

        this.highestScoreValue.setValue(gameDocument.highestScore);
        this.bindHighestScore(highestScoreValue);
        // this.isLevelComplete.bindBidirectional(gameDocument.isLevelComplete());
        this.soundEffects.put("UNMOVABLE_AUDIO_CLIP_MEDIA", ResourceFactory.UNMOVABLE_AUDIO_CLIP);
        this.soundEffects.put("MOVE_AUDIO_CLIP_MEDIA", ResourceFactory.MOVE_AUDIO_CLIP);

        musicSwitchToggle.addListener((observable, oldValue, newValue) -> {
            if (observable != null ) {
                boolean isMute = !observable.getValue();
                GameView.backgroundMusicPlayer.setMute(isMute);
                for (AudioClip mediaPlayer : this.soundEffects.values()) {
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

        this.renderMap();
    }

    private boolean isAnimating = false;

    public void handleKey(KeyCode code) {
        // simply ignore keys when animating
        if (isAnimating) {
            return;
        }
        gameDocument.serializeObject();
        canUndo.set(!GameStageSaver.isEmpty());
        Keeper keeper = gameDocument.getKeeper();
        Point direction = new Point(0, 0);
        switch (code) {
            case UP -> {
                direction = new Point(0, -1);
                keeperImageView.setImage(ResourceFactory.PLAYER_BACK_IMAGE);
            }

            case RIGHT -> {
                direction = new Point(1, 0);
                keeperImageView.setImage(ResourceFactory.PLAYER_RIGHT_IMAGE);
            }

            case DOWN -> {
                direction = new Point(0, 1);
                keeperImageView.setImage(ResourceFactory.PLAYER_FRONT_IMAGE);
            }

            case LEFT -> {
                direction = new Point(-1, 0);
                keeperImageView.setImage(ResourceFactory.PLAYER_LEFT_IMAGE);
            }

            default -> {
                // TODO: implement something funny.
                return;
            }

        }

        isAnimating = true;

        if (isDestinationOnTopOfCrate(GameGrid.translatePoint(keeper.at(), direction))) {
            renderPlayerCrateHierarchy(false);
        } else if (isDestinationOnBottomOfCrate(GameGrid.translatePoint(keeper.at(), direction))){
            renderPlayerCrateHierarchy(true);
        }

        if (keeper.canMoveBy(direction)) {
            try {
                Crate crate = keeper.willPushCrate(direction);
                if (crate != null) {
                    animateCrate(crateImageViewMap.get(crate), direction);
                    crate.moveBy(direction);
                    if(crate.isOnDiamond(this.gameDocument.getCurrentLevel().diamondsGrid)) {
                        crateImageViewMap.get(crate).setImage(ResourceFactory.CRATE_ON_DIAMOND_IMAGE);
                    } else {
                        crateImageViewMap.get(crate).setImage(ResourceFactory.CRATE_IMAGE);
                    }
                }
                animateKeeper(keeperImageView, direction);
                this.gameDocument.getKeeper().moveBy(direction);
            } catch (IllegalMovementException e) {
                e.printStackTrace();
            }
        } else {
            shakeAnimation(keeperImageView, direction);
        }
//
//        if (GameDocument.isDebugActive()) {
//            System.out.println(code);
    }

    private void shakeAnimation(Node item, Point direction) {
        this.soundEffects.get("UNMOVABLE_AUDIO_CLIP_MEDIA").play();

        ParallelTransition parallelTransition = new ParallelTransition();
        parallelTransition.setOnFinished((event) -> isAnimating = false);

        TranslateTransition translateTransition = new TranslateTransition(Duration.millis(150), item);
        translateTransition.setByX(10*direction.x);
        translateTransition.setByY(10*direction.y);
        translateTransition.setCycleCount(2);
        translateTransition.setAutoReverse(true);

        ScaleTransition scaleTransition = new ScaleTransition(Duration.millis(150), item);
        scaleTransition.setToY(0.5);
        scaleTransition.setCycleCount(2);
        scaleTransition.setAutoReverse(true);

        parallelTransition.getChildren().addAll(translateTransition, scaleTransition);
        parallelTransition.play();

    }

    private void animateKeeper(Node item, Point direction) {
        isAnimating = true;

        this.soundEffects.get("MOVE_AUDIO_CLIP_MEDIA").play();

        TranslateTransition translateTransition = new TranslateTransition(Duration.millis(200), item);

        translateTransition.setOnFinished((event) -> isAnimating = false);

        translateTransition.setByX(48*direction.x);
        translateTransition.setByY(30*direction.y);
        translateTransition.play();

    }

    private void animateCrate(Node item, Point direction) {
        isAnimating = true;

        TranslateTransition translateTransition = new TranslateTransition(Duration.millis(200), item);
        translateTransition.setByX(48*direction.x);
        translateTransition.setByY(30*direction.y);
        translateTransition.play();

    }

    private boolean isDestinationOnTopOfCrate(Point destination) {
        return (this.gameDocument.getCurrentLevel().objectsGrid.getGameObjectAt(GameGrid.translatePoint(destination, new Point(0, 1)))) instanceof Crate;
    }

    private boolean isDestinationOnBottomOfCrate(Point destination) {
        return (this.gameDocument.getCurrentLevel().objectsGrid.getGameObjectAt(GameGrid.translatePoint(destination, new Point(0, -1)))) instanceof Crate;
    }

    public void clickToggleDebug() {
//        gameDocument.toggleDebug(menuBarClickToggleDebug());
    }

    public void clickToggleMusic(MouseEvent mouseEvent) {
        menuBarClickToggleMusic();
    }

    public void clickUndo(MouseEvent mouseEvent) {
        GameDocument restoreObject = GameStageSaver.pop();
        if (restoreObject != null) {
            Main.gameDocument.restoreObject(restoreObject);
            canUndo.set(!GameStageSaver.isEmpty());
            renderMap();
        } else {

        }

    }

    public void clickSaveGame(MouseEvent mouseEvent) {
    }

    public void clickToMenu(MouseEvent mouseEvent) {
        GameView.backgroundMusicPlayer.stop();
        Main.primaryStage.setScene(Main.menuScene);
        MenuView.backgroundMusicPlayer.play();
    }

    private void renderItemAndPLayer() {
        crateImageViewMap.clear();
        itemGrid.getChildren().clear();
        playerGrid.getChildren().clear();

        Image crateImage = ResourceFactory.CRATE_IMAGE;
        for (AbstractGameObject object : gameDocument.getCurrentLevel().objectsGrid) {
            if (object instanceof Crate) {
                ImageView crateImageView = new ImageView(crateImage);
                crateImageView.setFitHeight(38);
                crateImageView.setFitWidth(35);
                crateImageView.setTranslateX(7);
                crateImageView.setTranslateY(-20);
                itemGrid.add(crateImageView, object.at().x, object.at().y);

                crateImageViewMap.put((Crate)object, crateImageView);
            }
        }

        Image playerFrontImage = ResourceFactory.PLAYER_FRONT_IMAGE;
        for (AbstractGameObject object : gameDocument.getCurrentLevel().objectsGrid) {
            if (object instanceof Keeper) {
                ImageView playerImageView = new ImageView(playerFrontImage);
                playerImageView.setFitHeight(38);
                playerImageView.setFitWidth(35);
                playerImageView.setTranslateX(7);
                playerImageView.setTranslateY(-20);
                this.keeperImageView = playerImageView;
                playerGrid.add(playerImageView, object.at().x, object.at().y);
            }
        }
    }

    private void renderMap() {

        Image stageImage = ResourceFactory.STAGE_IMAGE;
        for (AbstractGameObject object : gameDocument.getCurrentLevel().objectsGrid) {
            if (!(object instanceof Wall)) {
                ImageView stageImageView = new ImageView(stageImage);
                stageImageView.setFitHeight(48);
                stageImageView.setFitWidth(48);
                stageGrid.add(stageImageView, object.at().x, object.at().y);
            }
        }

        Image diamondImage = ResourceFactory.DIAMOND_IMAGE;
        for (AbstractGameObject object : gameDocument.getCurrentLevel().diamondsGrid) {
            if (object instanceof Diamond) {
                ImageView diamondImageView = new ImageView(diamondImage);
                diamondImageView.setFitHeight(16);
                diamondImageView.setFitWidth(16);
                diamondImageView.setTranslateX(16);
                diamondImageView.setTranslateY(-10);
                diamondsGrid.add(diamondImageView, object.at().x, object.at().y);
            }
        }
        renderItemAndPLayer();
    }

    private void renderPlayerCrateHierarchy(boolean isPlayerOnFirstStage) {
        if (isPlayerOnFirstStage) {
            playerGrid.toFront();
        } else {
            itemGrid.toFront();
        }
    }
}

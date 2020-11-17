package com.ae2dms.UI.Game;

import com.ae2dms.Business.GameDocument;
import com.ae2dms.GameObject.AbstractGameObject;
import com.ae2dms.GameObject.Objects.Crate;
import com.ae2dms.GameObject.Objects.Diamond;
import com.ae2dms.GameObject.Objects.Keeper;
import com.ae2dms.GameObject.Objects.Wall;
import com.ae2dms.IO.ResourceFactory;
import com.ae2dms.Main.Main;
import com.ae2dms.UI.AbstractBarController;
import com.ae2dms.UI.Menu.MenuView;
import javafx.animation.FillTransition;
import javafx.animation.ParallelTransition;
import javafx.animation.ScaleTransition;
import javafx.animation.TranslateTransition;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.fxml.FXML;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.effect.ColorAdjust;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.media.AudioClip;
import javafx.scene.paint.Color;
import javafx.util.Duration;

import java.awt.*;

public class GameViewController extends AbstractBarController {

    private GameDocument gameDocument = Main.gameDocument;

    @FXML
    private Group InfoGroup;

    @FXML
    private GridPane stageGrid;

    @FXML
    private GridPane itemGrid;

    @FXML
    private GridPane diamondsGrid;

    private ImageView keeperImageView;

    private final IntegerProperty highestScoreValue;

    public GameViewController() {
        this.highestScoreValue = new SimpleIntegerProperty();
    }

    public void initialize() throws IllegalStateException, ClassNotFoundException {

        super.disableButton("High score");

        musicSwitchToggle.addListener((observable, oldValue, newValue) -> {
            if (observable != null ) {
                MenuView.backgroundMusicPlayer.setMute(!observable.getValue());
            }
        });

        this.highestScoreValue.setValue(gameDocument.highestScore);
        this.bindHighestScore(highestScoreValue);

        this.renderMap();
    }

    private boolean isAnimating = false;

    public void handleKey(KeyCode code) {
        // simply ignore keys when animating
        if (isAnimating) {
            return;
        }
        switch (code) {
            case UP -> {
                if (gameDocument.getKeeper().canMoveBy(new Point(0, -1))) {
                    ScaleTransition scaleTransition = new ScaleTransition();
                    //Setting the duration for the transition
                    scaleTransition.setDuration(Duration.millis(500));
                    //Setting the node for the transition
                    scaleTransition.setNode(keeperImageView);
                    //Setting the dimensions for scaling
                    scaleTransition.setByY(100);
                    //Setting auto reverse value to true
                    scaleTransition.setAutoReverse(false);
                    //Playing the animation
                    scaleTransition.play();

                } else {
                    shakeAnimation(keeperImageView, new Point(0, -1));


                }
            }

//            case RIGHT:
//                gameDocument.move(new Point(0, 1));
//                break;
//
//            case DOWN:
//                gameDocument.move(new Point(1, 0));
//                break;
//
//            case LEFT:
//                gameDocument.move(new Point(0, -1));
//                break;
//
//            default:
//                // TODO: implement something funny.
//        }
//        updateView();
//
//        if (GameDocument.isDebugActive()) {
//            System.out.println(code);
        }
    }

    private void shakeAnimation(Node item, Point direction) {
        isAnimating = true;

        AudioClip clip = ResourceFactory.UNMOVABLE_AUDIO_CLIP;
        clip.play();

        ParallelTransition parallelTransition = new ParallelTransition();
        parallelTransition.setOnFinished((event) -> {
            isAnimating = false;
        });

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

    public void clickToggleDebug() {
//        gameDocument.toggleDebug(menuBarClickToggleDebug());
    }

    public void clickToggleMusic(MouseEvent mouseEvent) {
        menuBarClickToggleMusic();
    }

    public void clickUndo(MouseEvent mouseEvent) {
//        gameDocument.undo();
    }

    public void clickSaveGame(MouseEvent mouseEvent) {
    }

    public void clickToMenu(MouseEvent mouseEvent) throws Exception {
        GameView.backgroundMusicPlayer.stop();
        Main.primaryStage.setScene(Main.menuScene);
        MenuView.backgroundMusicPlayer.play();
    }

    private void renderMap() throws ClassNotFoundException {
        Image stageImage = ResourceFactory.STAGE_IMAGE;
        for (AbstractGameObject object : gameDocument.getCurrentLevel().objectsGrid) {
            if (!(object instanceof Wall)) {
                ImageView stageImageView = new ImageView(stageImage);
                stageImageView.setFitHeight(48);
                stageImageView.setFitWidth(48);
                stageGrid.add(stageImageView, object.at().y, object.at().x);
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
                diamondsGrid.add(diamondImageView, object.at().y, object.at().x);
            }
        }

        Image crateImage = ResourceFactory.CRATE_IMAGE;
        for (AbstractGameObject object : gameDocument.getCurrentLevel().objectsGrid) {
            if (object instanceof Crate) {
                ImageView crateImageView = new ImageView(crateImage);
                crateImageView.setFitHeight(38);
                crateImageView.setFitWidth(35);
                crateImageView.setTranslateX(7);
                crateImageView.setTranslateY(-20);
                itemGrid.add(crateImageView, object.at().y, object.at().x);
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
                itemGrid.add(playerImageView, object.at().y, object.at().x);
            }
        }
    }
}

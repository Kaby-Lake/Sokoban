package com.ae2dms.GameObject.Objects;


import com.ae2dms.Business.Data.GameGrid;
import com.ae2dms.Business.Data.Level;
import com.ae2dms.GameObject.AbstractGameObject;
import com.ae2dms.GameObject.Movable;
import com.ae2dms.IO.ResourceFactory;
import com.ae2dms.IO.ResourceType;
import com.ae2dms.UI.Game.GameViewController;
import javafx.animation.*;
import com.ae2dms.UI.Menu.ColourPreferenceController;
import javafx.animation.TranslateTransition;
import javafx.scene.image.*;
import javafx.scene.image.Image;
import javafx.scene.layout.StackPane;
import javafx.util.Duration;

import java.awt.*;
import java.util.UUID;

public class Crate extends AbstractGameObject implements Movable {

    GameGrid floorGrid;

    public boolean isCheating = false;

    private static Image CRATE_IMAGE = (Image)ResourceFactory.getResource("CRATE_IMAGE_Silver", ResourceType.Image);
    private static Image CRATE_ON_DIAMOND_IMAGE = (Image)ResourceFactory.getResource("CRATE_IMAGE_Red", ResourceType.Image);

    private final UUID uuid = UUID.randomUUID();

    public transient StackPane cheatingView;

    private GameGrid diamondsGrid;

    public Crate(Level linksTo, int atX, int atY) {
        super(linksTo, atX, atY);
        this.grid = linksTo.objectsGrid;
        this.diamondsGrid = linksTo.diamondsGrid;
        this.floorGrid = linksTo.floorGrid;
        ColourPreferenceController.selectedCrateColour.addListener((observable, oldValue, newValue) -> {
            if (observable != null) {
                CRATE_IMAGE = (Image)ResourceFactory.getResource("CRATE_IMAGE_" + newValue, ResourceType.Image);
            }
        });

        ColourPreferenceController.selectedDiamondColour.addListener((observable, oldValue, newValue) -> {
            if (observable != null) {
                CRATE_ON_DIAMOND_IMAGE = (Image)ResourceFactory.getResource("CRATE_IMAGE_" + newValue, ResourceType.Image);
            }
        });
    }

    @Override
    public char getCharSymbol() {
        return 'C';
    }

    @Override
    public String getStringSymbol() {
        return "CRATE";
    }

    @Override
    public ImageView render() {
        if (this.view != null) {
            this.view.setTranslateX(7);
            this.view.setTranslateY(-20);
            if (isOnDiamond()) {
                this.view.setImage(CRATE_ON_DIAMOND_IMAGE);
            } else {
                this.view.setImage(CRATE_IMAGE);
            }
        } else {
            this.view = new ImageView(CRATE_IMAGE);
            this.view.setFitHeight(38);
            this.view.setFitWidth(35);
            this.view.setTranslateX(7);
            this.view.setTranslateY(-20);
        }
        return this.view;
    }

    public StackPane renderCheating() {
        ImageView crateImage = new ImageView(CRATE_IMAGE);
        crateImage.setFitHeight(38);
        crateImage.setFitWidth(35);
        crateImage.setTranslateX(0);
        crateImage.setTranslateY(-20);

        StackPane stack = new StackPane();
        stack.setPrefHeight(30);
        stack.setPrefWidth(48);

        ImageView choiceGridHalo = new ImageView((Image) ResourceFactory.getResource("CHOICE_GRID_IMAGE", ResourceType.Image));
        choiceGridHalo.setFitWidth(40);
        choiceGridHalo.setFitHeight(25);
        choiceGridHalo.setTranslateY(-11);

        stack.getChildren().add(choiceGridHalo);
        stack.getChildren().add(crateImage);

        TranslateTransition translateTransition = new TranslateTransition(Duration.millis(200), crateImage);
        translateTransition.setByY(-5);

        ScaleTransition scaleTransition = new ScaleTransition(Duration.millis(200), choiceGridHalo);
        scaleTransition.setFromX(5);
        scaleTransition.setFromY(5);
        scaleTransition.setToX(1);
        scaleTransition.setToY(1);

        ParallelTransition parallelTransition = new ParallelTransition();
        parallelTransition.getChildren().addAll(
                scaleTransition,
                translateTransition
        );
        parallelTransition.play();

        this.cheatingView = stack;
        return stack;
    }


    // Movable Methods

    @Override
    public Boolean canMoveBy(Point delta) {
        if (moveMoreThanOneStep(delta)) {
            return false;
        }
        Point targetPosition = GameGrid.translatePoint(this.at(), delta);
        return canMoveTo(targetPosition);
    }

    @Override
    public void moveBy(Point delta) throws IllegalMovementException {
        Point targetPosition = GameGrid.translatePoint(this.at(), delta);
        AbstractGameObject objectOnDestination = floorGrid.getGameObjectAt(targetPosition);
        if (objectOnDestination instanceof Floor) {
            moveToFloor(targetPosition);
            animateCrate(delta);
        } else {
            throw new IllegalMovementException();
        }
    }

    public void settleDown() {
        ImageView choiceGridHalo = (ImageView)this.cheatingView.getChildren().get(0);
        ImageView crateImage = (ImageView)this.cheatingView.getChildren().get(0);

        ParallelTransition parallelTransition = new ParallelTransition();

        TranslateTransition translateTransition = new TranslateTransition(Duration.millis(200), crateImage);
        translateTransition.setByY(5);

        ScaleTransition scaleTransition = new ScaleTransition(Duration.millis(200), choiceGridHalo);
        scaleTransition.setToX(5);
        scaleTransition.setToY(5);

        FadeTransition fadeTransition = new FadeTransition(Duration.millis(200), choiceGridHalo);
        fadeTransition.setToValue(0);

        parallelTransition.getChildren().addAll(translateTransition, scaleTransition, fadeTransition);

        parallelTransition.play();
        parallelTransition.setOnFinished((event) -> {
            GameViewController.isCheating.setValue(false);
            this.isCheating = false;
            isAnimating.set(false);
        });
    }

    private void moveToFloor(Point targetPosition) {
        floorGrid.putGameObjectAt(new Floor(level, at()), at());
        grid.putGameObjectAt(null, at());
        grid.putGameObjectAt(this, targetPosition);
        this.updatePosition(targetPosition);
    }

    public Boolean isOnDiamond() {
        return diamondsGrid.getGameObjectAt(at()) instanceof Diamond;
    }

    private Boolean canMoveTo(Point destination) {
        AbstractGameObject objectOnDestination = floorGrid.getGameObjectAt(destination);
        return objectOnDestination instanceof Floor;
    }

    private void updatePosition(Point position) {
        this.xPosition = position.x;
        this.yPosition = position.y;
    }

    private void animateCrate(Point direction) {

        TranslateTransition translateTransition;

        if (isCheating) {
            translateTransition = new TranslateTransition(Duration.millis(200), this.cheatingView);
        } else {
            translateTransition = new TranslateTransition(Duration.millis(200), this.view);
        }

        translateTransition.setOnFinished((event) -> {
            isAnimating.set(false);
        });

        translateTransition.setByX(48*direction.x);
        translateTransition.setByY(30*direction.y);
        translateTransition.play();

    }

    public void shakeAnimation(Point direction) {
        GameViewController.soundEffects.get("UNMOVABLE_AUDIO_CLIP_MEDIA").play();

        ParallelTransition parallelTransition = new ParallelTransition();
        parallelTransition.setOnFinished((event) -> {
            isAnimating.set(false);
        });

        TranslateTransition translateTransition = new TranslateTransition(Duration.millis(150), this.view);
        translateTransition.setByX(10*direction.x);
        translateTransition.setByY(10*direction.y);
        translateTransition.setCycleCount(2);
        translateTransition.setAutoReverse(true);

        ScaleTransition scaleTransition = new ScaleTransition(Duration.millis(150), this.view);
        scaleTransition.setToY(0.5);
        scaleTransition.setCycleCount(2);
        scaleTransition.setAutoReverse(true);

        parallelTransition.getChildren().addAll(translateTransition, scaleTransition);
        parallelTransition.play();
    }


    @Override
    public int hashCode() {
        return uuid.hashCode();
    }

    @Override
    public boolean equals(Object object) {

        if (object == null || object.getClass() != this.getClass()) {
            return false;
        }

        Crate crateObject = (Crate)object;

        if (this.xPosition != crateObject.xPosition || this.yPosition != crateObject.yPosition) {
            return false;
        }
        return true;
    }


}

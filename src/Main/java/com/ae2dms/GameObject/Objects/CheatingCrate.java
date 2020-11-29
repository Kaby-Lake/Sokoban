package com.ae2dms.GameObject.Objects;

import com.ae2dms.Business.Data.GameGrid;
import com.ae2dms.GameObject.AbstractGameObject;
import com.ae2dms.GameObject.Movable;
import com.ae2dms.IO.ResourceFactory;
import com.ae2dms.IO.ResourceType;
import com.ae2dms.UI.Game.GameViewController;
import javafx.animation.ParallelTransition;
import javafx.animation.ScaleTransition;
import javafx.animation.TranslateTransition;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.util.Duration;

import java.awt.*;
import java.util.UUID;

/**
 * @Description: $
 * @Param: $
 * @return: $
 * @Author: Zichen XU
 */
public class CheatingCrate extends AbstractGameObject implements Movable {

    private final UUID uuid = UUID.randomUUID();

    private StackPane view;

    public CheatingCrate(Crate crate, GameGrid objectsGrid) {
        super(objectsGrid, crate.xPosition, crate.yPosition);
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
        return null;
    }

    public StackPane renderCheating() {
        if (this.view != null) {
            return view;

        } else {
            ImageView crateImage = new ImageView((Image)ResourceFactory.getResource("CRATE_IMAGE", ResourceType.Image));
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
            translateTransition.play();

            this.view = stack;
        }
        return this.view;
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
        AbstractGameObject objectOnDestination = grid.getGameObjectAt(targetPosition);
        if (objectOnDestination instanceof Floor) {
            moveToFloor(targetPosition);
            animateCrate(delta);
        } else {
            throw new IllegalMovementException();
        }
    }

    public void settleDown() {
        TranslateTransition translateTransition = new TranslateTransition(Duration.millis(200), this.view);
        translateTransition.setByY(5);
        translateTransition.play();
        translateTransition.setOnFinished((event) -> {
            isAnimating.set(false);
        });

    }

    private void moveToFloor(Point targetPosition) {
        grid.putGameObjectAt(new Floor(grid, at()), at());
        grid.putGameObjectAt(this, targetPosition);
        this.updatePosition(targetPosition);
    }

    private Boolean canMoveTo(Point destination) {
        AbstractGameObject objectOnDestination = grid.getGameObjectAt(destination);
        return objectOnDestination instanceof Floor;
    }

    private void updatePosition(Point position) {
        this.xPosition = position.x;
        this.yPosition = position.y;
    }

    private void animateCrate(Point direction) {
        TranslateTransition translateTransition = new TranslateTransition(Duration.millis(200), this.view);

        translateTransition.setOnFinished((event) -> {
            isAnimating.set(false);
        });

        translateTransition.setByX(48*direction.x);
        translateTransition.setByY(30*direction.y);
        translateTransition.play();
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


}

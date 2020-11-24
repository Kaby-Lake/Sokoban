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
import javafx.util.Duration;

import java.awt.*;

import static com.ae2dms.UI.Game.GameViewController.render;

public class Player extends AbstractGameObject implements Movable {


    public Player(GameGrid linksTo, int atX, int atY) {
        super(linksTo, atX, atY);
    }

    public Player(GameGrid linksTo, Point at) {
        super(linksTo, at);
    }

    @Override
    public char getCharSymbol() {
        return 'S';
    }

    @Override
    public String getStringSymbol() {
        return "KEEPER";
    }

    // Movable Methods

    @Override
    public ImageView render() {
        if (this.view != null) {
            return this.view;
        } else {
            return headTo(new Point(0, 0));
        }
    }

    public ImageView headTo(Point direction) {
        Image playerDirectionalImage;
        if (new Point(0, 1).equals(direction)) {
            playerDirectionalImage = (Image)ResourceFactory.getResource("PLAYER_FRONT_IMAGE", ResourceType.Image);
        } else if (new Point(0, -1).equals(direction)) {
            playerDirectionalImage = (Image)ResourceFactory.getResource("PLAYER_BACK_IMAGE", ResourceType.Image);
        } else if (new Point(-1, 0).equals(direction)) {
            playerDirectionalImage = (Image)ResourceFactory.getResource("PLAYER_LEFT_IMAGE", ResourceType.Image);
        } else if (new Point(1, 0).equals(direction)) {
            playerDirectionalImage = (Image)ResourceFactory.getResource("PLAYER_RIGHT_IMAGE", ResourceType.Image);
        } else {
            playerDirectionalImage = (Image)ResourceFactory.getResource("PLAYER_FRONT_IMAGE", ResourceType.Image);
        }

        if (this.view != null) {
            this.view.setImage(playerDirectionalImage);
        } else {
            this.view = new ImageView(playerDirectionalImage);
            this.view.setFitHeight(38);
            this.view.setFitWidth(35);
            this.view.setTranslateX(7);
            this.view.setTranslateY(-20);
            return this.view;
        }
        return this.view;
    }

    @Override
    public Boolean canMoveBy(Point delta) {
        if (moveMoreThanOneStep(delta)) {
            return false;
        }
        Point targetPosition = GameGrid.translatePoint(this.at(), delta);
        if (grid.isPointOutOfBounds(targetPosition)) {
            return false;
        }
        AbstractGameObject objectOnDestination = grid.getGameObjectAt(targetPosition);
        if (objectOnDestination instanceof Floor) {
            return true;
        } else if (objectOnDestination instanceof Crate) {
            return ((Crate) objectOnDestination).canMoveBy(delta);
        } else {
            return false;
        }
    }

    @Override
    public void moveBy(Point delta) throws IllegalMovementException {
        isAnimating.set(true);

        headTo(delta);

        Point targetPosition = GameGrid.translatePoint(this.at(), delta);
        AbstractGameObject objectOnDestination = grid.getGameObjectAt(targetPosition);
        if (objectOnDestination instanceof Floor) {
            moveToFloor(targetPosition);
            render.renderPlayerCrateHierarchyBeforeAnimation(this);
            animatePlayer(delta, 0);
        } else if (objectOnDestination instanceof Crate) {
            ((Crate) objectOnDestination).moveBy(delta);
            moveToFloor(targetPosition);
            animatePlayer(delta, 1);
        } else {
            throw new IllegalMovementException();
        }
    }

    private void moveToFloor(Point targetPosition) {
        grid.putGameObjectAt(new Floor(grid, at()), at());
        grid.putGameObjectAt(this, targetPosition);
        this.updatePosition(targetPosition);
    }

    private void updatePosition(Point position) {
        this.xPosition = position.x;
        this.yPosition = position.y;
    }

    private void animatePlayer(Point direction, int SFXType) {
        switch (SFXType) {
            case 0 -> {
                GameViewController.soundEffects.get("MOVE_AUDIO_CLIP_MEDIA").play();
            }
            case 1 -> {
                GameViewController.soundEffects.get("MOVE_CRATE_AUDIO_CLIP_MEDIA").play();
            }
        }
        isAnimating.set(true);

        TranslateTransition translateTransition = new TranslateTransition(Duration.millis(200), this.view);

        translateTransition.setOnFinished((event) -> {
            isAnimating.set(false);
        });

        translateTransition.setByX(48*direction.x);
        translateTransition.setByY(30*direction.y);
        translateTransition.play();
    }


    public boolean isOnNorthOfCrate() {
        return (grid.getGameObjectAt(GameGrid.translatePoint(at(), new Point(0, 1)))) instanceof Crate;
    }

    public boolean isOnSouthOfCrate() {
        return (grid.getGameObjectAt(GameGrid.translatePoint(at(), new Point(0, -1)))) instanceof Crate;
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

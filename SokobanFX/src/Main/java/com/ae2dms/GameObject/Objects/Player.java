package com.ae2dms.GameObject.Objects;

import com.ae2dms.Business.Data.GameGrid;
import com.ae2dms.Business.Data.Level;
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

/**
 * The Player that user can manipulate on
 */
public class Player extends AbstractGameObject implements Movable {

    /**
     * The Floor grid.
     */
    GameGrid floorGrid;

    /**
     * Instantiates a new Player.
     *
     * @param linksTo the links to
     * @param atX     the at x
     * @param atY     the at y
     */
    public Player(Level linksTo, int atX, int atY) {
        super(linksTo, atX, atY);
        this.grid = linksTo.objectsGrid;
        floorGrid = linksTo.floorGrid;
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

    /**
     * @return return the Player image with direction specified
     */
    @Override
    public ImageView render() {
        if (this.view != null) {
            this.view.setTranslateX(7);
            this.view.setTranslateY(-20);

            return this.view;
        } else {
            return headTo(new Point(0, 0));
        }
    }

    /**
     * let the Player head to which direction
     *
     * @param direction Point(x, y), can be (0, 1) (0, -1) (1, 0) (-1, 0)
     * @return the ImageView of the Player which heads to this direction
     */
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

    /**
     * if Player can move by this direction
     * if Crate is on the way, then it will 'ask' Crate if Crate can move by this direction
     * @param delta how far to be moved
     *              should only be one step
     * @return
     */
    @Override
    public Boolean canMoveBy(Point delta) {
        if (moveMoreThanOneStep(delta)) {
            return false;
        }
        Point targetPosition = GameGrid.translatePoint(this.at(), delta);
        if (grid.isPointOutOfBounds(targetPosition)) {
            return false;
        }
        AbstractGameObject objectOnDestinationInObjectsGrid = grid.getGameObjectAt(targetPosition);
        if (objectOnDestinationInObjectsGrid instanceof Crate) {
            return ((Crate) objectOnDestinationInObjectsGrid).canMoveBy(delta);
        }

        AbstractGameObject objectOnDestinationInStageGrid = floorGrid.getGameObjectAt(targetPosition);
        return objectOnDestinationInStageGrid instanceof Floor;

    }

    /**
     * should be called only after calling canMoveBy()
     * move the Player by this direction
     * if Crate is on the way, then it will 'ask' Crate to move this direction too
     * @param delta how far to be moved
     * @throws IllegalMovementException
     */
    @Override
    public void moveBy(Point delta) throws IllegalMovementException {
        isAnimating.set(true);

        headTo(delta);

        Point targetPosition = GameGrid.translatePoint(this.at(), delta);

        AbstractGameObject objectOnDestinationInObjectsGrid = grid.getGameObjectAt(targetPosition);
        if (objectOnDestinationInObjectsGrid instanceof Crate) {
            ((Crate) objectOnDestinationInObjectsGrid).moveBy(delta);
            moveToFloor(targetPosition);
            animatePlayer(delta, 1);
        }

        AbstractGameObject objectOnDestinationInStageGrid = floorGrid.getGameObjectAt(targetPosition);
        if (objectOnDestinationInStageGrid instanceof Floor) {
            moveToFloor(targetPosition);
            animatePlayer(delta, 0);
        } else {
            throw new IllegalMovementException();
        }
    }

    /**
     * if this Player is on a Candy
     *
     * @return boolean boolean
     */
    public boolean isOnCandy() {
        return level.candyGrid.getGameObjectAt(at()) instanceof Candy;
    }

    /**
     * move this player to this position
     * @param targetPosition
     */
    private void moveToFloor(Point targetPosition) {
        floorGrid.putGameObjectAt(new Floor(level, at()), at());
        grid.putGameObjectAt(null, at());
        grid.putGameObjectAt(this, targetPosition);
        this.updatePosition(targetPosition);
    }

    private void updatePosition(Point position) {
        this.xPosition = position.x;
        this.yPosition = position.y;
    }

    /**
     * move the Player by this direction with animation and SFX

     * @param direction which direction to move
     * @param SFXType FXType 0 means move to Floor, 1 means move the Crate
     */
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
            render.renderItemAndPlayer(level);
            isAnimating.set(false);
        });

        translateTransition.setByX(48*direction.x);
        translateTransition.setByY(30*direction.y);
        translateTransition.play();
    }

    /**
     * will 'eat' the candy if Player is on the Candy
     *
     * @return boolean
     */
    public boolean eatingCrate() {
        AbstractGameObject object = level.candyGrid.getGameObjectAt(this.xPosition, this.yPosition);
        if (object instanceof Candy) {
            ((Candy)object).eat();
            level.eatenCandyCount++;
            return true;
        }
        return false;
    }

    /**
     * produce a Shaking animation to indicate that cannot move by this direction
     *
     * @param direction direction to move
     */
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

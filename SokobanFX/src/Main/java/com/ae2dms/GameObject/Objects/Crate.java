package com.ae2dms.GameObject.Objects;


import com.ae2dms.Business.Data.GameGrid;
import com.ae2dms.Business.Data.Level;
import com.ae2dms.GameObject.AbstractGameObject;
import com.ae2dms.GameObject.Movable;
import com.ae2dms.IO.ResourceFactory;
import com.ae2dms.IO.ResourceType;
import com.ae2dms.UI.Game.GameViewController;
import com.ae2dms.UI.Menu.ColourPreferenceController;
import javafx.animation.FadeTransition;
import javafx.animation.ParallelTransition;
import javafx.animation.ScaleTransition;
import javafx.animation.TranslateTransition;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.util.Duration;
import static com.ae2dms.UI.Game.GameViewController.isAnimating;

import java.awt.*;
import java.util.UUID;

/**
 * Crate that Player can move, all Crate on Diamond means victory
 */
public class Crate extends AbstractGameObject implements Movable {

    /**
     * The Floor grid.
     */
    GameGrid floorGrid;

    /**
     * The Is cheating.
     */
    public boolean isCheating = false;

    private transient Image CRATE_IMAGE = (Image)ResourceFactory.getResource("CRATE_IMAGE_Silver", ResourceType.Image);
    private transient Image CRATE_ON_DIAMOND_IMAGE = (Image)ResourceFactory.getResource("CRATE_IMAGE_Red", ResourceType.Image);

    private final UUID uuid = UUID.randomUUID();

    /**
     * The Cheating view.
     */
    public transient StackPane cheatingView;

    private GameGrid diamondsGrid;

    /**
     * Instantiates a new Crate.
     *
     * @param linksTo the links to
     * @param atX     the at x
     * @param atY     the at y
     */
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

    private Image getCrateImage() {
        if (CRATE_IMAGE == null) {
            CRATE_IMAGE = (Image)ResourceFactory.getResource("CRATE_IMAGE_" + ColourPreferenceController.selectedCrateColour.getValue(), ResourceType.Image);
        }
        return CRATE_IMAGE;
    }

    private Image getCrateOnDiamondImage() {
        if (CRATE_ON_DIAMOND_IMAGE == null) {
            CRATE_ON_DIAMOND_IMAGE = (Image)ResourceFactory.getResource("CRATE_IMAGE_" + ColourPreferenceController.selectedDiamondColour.getValue(), ResourceType.Image);
        }
        return CRATE_ON_DIAMOND_IMAGE;
    }

    @Override
    public char getCharSymbol() {
        return 'C';
    }

    @Override
    public String getStringSymbol() {
        return "CRATE";
    }

    /**
     * @return the Crate image with selected colour
     */
    @Override
    public ImageView render() {
        if (this.view != null) {
            this.view.setTranslateX(7);
            this.view.setTranslateY(-20);
            if (isOnDiamond()) {
                this.view.setImage(getCrateOnDiamondImage());
            } else {
                this.view.setImage(getCrateImage());
            }
        } else {
            this.view = new ImageView(getCrateImage());
            this.view.setFitHeight(38);
            this.view.setFitWidth(35);
            this.view.setTranslateX(7);
            this.view.setTranslateY(-20);
        }
        return this.view;
    }

    /**
     * Render the cheating view of this Crate
     *
     * @return stack pane
     */
    public StackPane renderCheating() {
        ImageView crateImage = new ImageView(getCrateImage());
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
        parallelTransition.setOnFinished((event) -> {
            isAnimating.set(false);
        });
        parallelTransition.play();

        this.cheatingView = stack;
        return stack;
    }


    // Movable Methods

    /**
     * whether Crate can move by this direction
     * Crate can only move when the direction is a Floor
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
        return canMoveTo(targetPosition);
    }

    /**
     * should be called only after calling canMoveBy()
     * move this Crate by this direction
     * @param delta how far to be moved
     * @throws IllegalMovementException
     */
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

    /**
     * settle down this Crate and exit Cheating Mode
     */
    public void settleDown() {
        GameViewController.isCheating.setValue(false);
        this.isCheating = false;
        isAnimating.set(false);
    }

    /**
     * move this crate to targetPosition
     * @param targetPosition position to be moved to
     */
    private void moveToFloor(Point targetPosition) {
        floorGrid.putGameObjectAt(new Floor(level, at()), at());
        grid.putGameObjectAt(null, at());
        grid.putGameObjectAt(this, targetPosition);
        this.updatePosition(targetPosition);
    }

    /**
     * Is on diamond boolean.
     *
     * @return whether this Crate is on Diamond
     */
    public Boolean isOnDiamond() {
        return diamondsGrid.getGameObjectAt(at()) instanceof Diamond;
    }

    /**
     * whether this Crate can move by this direction
     * Crate can only move if the destination is Floor
     * @param destination point of destination
     * @return boolean can move or not
     */
    private Boolean canMoveTo(Point destination) {
        AbstractGameObject objectOnDestinationOfFloor = floorGrid.getGameObjectAt(destination);
        AbstractGameObject objectOnDestinationOfGameObject = grid.getGameObjectAt(destination);
        return (objectOnDestinationOfFloor instanceof Floor) && !((objectOnDestinationOfGameObject instanceof Player) || (objectOnDestinationOfGameObject instanceof Crate));
    }

    private void updatePosition(Point position) {
        this.xPosition = position.x;
        this.yPosition = position.y;
    }

    /**
     * create a animation that move the Crate to which direction
     * @param direction
     */
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

    /**
     * create a shaking animation which indicates that Crate cannot be moved in this direction
     *
     * @param direction the direction
     */
    public void shakeAnimation(Point direction) {
        GameViewController.soundEffects.get("UNMOVABLE_AUDIO_CLIP_MEDIA").play();

        ParallelTransition parallelTransition = new ParallelTransition();
        parallelTransition.setOnFinished((event) -> {
            isAnimating.set(false);
        });

        TranslateTransition translateTransition = new TranslateTransition(Duration.millis(150), this.cheatingView);
        translateTransition.setByX(10*direction.x);
        translateTransition.setByY(10*direction.y);
        translateTransition.setCycleCount(2);
        translateTransition.setAutoReverse(true);

        ScaleTransition scaleTransition = new ScaleTransition(Duration.millis(150), this.cheatingView);
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

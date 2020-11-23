package com.ae2dms.GameObject.Objects;


import com.ae2dms.Business.Data.GameGrid;
import com.ae2dms.GameObject.AbstractGameObject;
import com.ae2dms.GameObject.Movable;
import com.ae2dms.IO.ResourceFactory;
import javafx.animation.TranslateTransition;
import javafx.scene.image.ImageView;
import javafx.util.Duration;

import java.awt.*;
import java.util.UUID;

public class Crate extends AbstractGameObject implements Movable {

    private final UUID uuid = UUID.randomUUID();

    private GameGrid diamondsGrid;

    public Crate(GameGrid linksTo, int atX, int atY, GameGrid diamondsGrid) {
        super(linksTo, atX, atY);
        this.diamondsGrid = diamondsGrid;
    }

    public Crate(GameGrid linksTo, Point at, GameGrid diamondsGrid) {
        super(linksTo, at);
        this.diamondsGrid = diamondsGrid;
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
            if (isOnDiamond()) {
                this.view.setImage(ResourceFactory.CRATE_ON_DIAMOND_IMAGE);
            } else {
                this.view.setImage(ResourceFactory.CRATE_IMAGE);
            }
        } else {
            this.view = new ImageView(ResourceFactory.CRATE_IMAGE);
            this.view.setFitHeight(38);
            this.view.setFitWidth(35);
            this.view.setTranslateX(7);
            this.view.setTranslateY(-20);
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

    private void moveToFloor(Point targetPosition) {
        grid.putGameObjectAt(new Floor(grid, at()), at());
        grid.putGameObjectAt(this, targetPosition);
        this.updatePosition(targetPosition);
        this.render();
    }

    public Boolean isOnDiamond() {
        return diamondsGrid.getGameObjectAt(at()) instanceof Diamond;
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


}

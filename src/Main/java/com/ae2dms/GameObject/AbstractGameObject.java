package com.ae2dms.GameObject;

import com.ae2dms.Business.Data.GameGrid;
import javafx.scene.image.ImageView;

import java.awt.*;
import java.io.Serializable;

public abstract class AbstractGameObject implements Serializable {

    // this grid does not contain Diamond
    protected GameGrid grid;
    protected int xPosition;
    protected int yPosition;
    public transient ImageView view;

    public Point at() {
        return new Point(xPosition, yPosition);
    }

    public AbstractGameObject(GameGrid linksTo, int atX, int atY) {
        grid = linksTo;
        xPosition = atX;
        yPosition = atY;
    }

    public AbstractGameObject(GameGrid linksTo, Point at) {
        grid = linksTo;
        xPosition = at.x;
        yPosition = at.y;
    }

    public abstract char getCharSymbol();

    public abstract String getStringSymbol();

    public abstract ImageView render();
}

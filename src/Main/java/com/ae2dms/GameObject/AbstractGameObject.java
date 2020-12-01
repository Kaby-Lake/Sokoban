package com.ae2dms.GameObject;

import com.ae2dms.Business.Data.GameGrid;
import com.ae2dms.Business.Data.Level;
import javafx.scene.image.ImageView;

import java.awt.*;
import java.io.Serializable;

public abstract class AbstractGameObject implements Serializable {

    // this grid does not contain Diamond
    protected volatile Level level;
    protected volatile GameGrid grid;
    public int xPosition;
    public int yPosition;
    public transient ImageView view;

    public Point at() {
        return new Point(xPosition, yPosition);
    }

    public AbstractGameObject(Level linksTo, int atX, int atY) {
        level = linksTo;
        xPosition = atX;
        yPosition = atY;
    }

    public AbstractGameObject(Level linksTo, Point at) {
        level = linksTo;
        xPosition = at.x;
        yPosition = at.y;
    }

    public abstract char getCharSymbol();

    public abstract String getStringSymbol();

    public abstract ImageView render();

    @Override
    public String toString() {
        return getStringSymbol() + " at " + at().toString();
    }
}

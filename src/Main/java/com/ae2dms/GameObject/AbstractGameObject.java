package com.ae2dms.GameObject;

import com.ae2dms.Business.Data.GameGrid;
import com.ae2dms.Business.Data.Level;
import javafx.scene.image.ImageView;

import java.awt.*;
import java.io.Serializable;

public abstract class AbstractGameObject implements Serializable {

    /**
     * The Level that it connects to (within)
     */
    protected volatile Level level;

    /**
     * The grid within this Level, not yet defined which one, can be defined by Child
     */
    protected volatile GameGrid grid;

    /**
     * The x position of this GameObject
     */
    public int xPosition;

    /**
     * The y position of this GameObject
     */
    public int yPosition;

    /**
     * The corresponding view of this GameObject, will be delivered as render()
     */
    public transient ImageView view;

    /**
     * @return the Point(x, y)
     */
    public Point at() {
        return new Point(xPosition, yPosition);
    }

    /**
     * @param linksTo the Level it connects to (within)
     * @param atX x position
     * @param atY y position
     */
    public AbstractGameObject(Level linksTo, int atX, int atY) {
        level = linksTo;
        xPosition = atX;
        yPosition = atY;
    }

    /**
     * @param linksTo the Level it connects to (within)
     * @param at its position
     */
    public AbstractGameObject(Level linksTo, Point at) {
        level = linksTo;
        xPosition = at.x;
        yPosition = at.y;
    }

    /**
     * @return the character representation of this GameObject
     */
    public abstract char getCharSymbol();

    /**
     * @return the string representation of this GameObject
     */
    public abstract String getStringSymbol();

    /**
     * @return the ImageView of this GameObject, different Objects will have different views
     */
    public abstract ImageView render();

    @Override
    public String toString() {
        return getStringSymbol() + " at " + at().toString();
    }
}

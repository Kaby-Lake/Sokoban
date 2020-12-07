package com.ae2dms.Business.Data;

import com.ae2dms.GameObject.AbstractGameObject;

import java.awt.*;
import java.io.Serializable;
import java.util.Iterator;

/**
 * The type Game grid.
 */
public class GameGrid implements Iterable<AbstractGameObject>, Serializable {

    /**
     * Gets x.
     *
     * @return get the X bounds of the GameGrid map
     */
    public int getX() {
        return X;
    }

    /**
     * Gets y.
     *
     * @return get the Y bounds of the GameGrid map
     */
    public int getY() {
        return Y;
    }

    /**
     * the X bounds of the GameGrid map
     */
    final int X;

    /**
     * the Y bounds of the GameGrid map
     */
    final int Y;

    /**
     * the 2d list to store all AbstractGameObject
     */
    private final AbstractGameObject[][] gameObjects;

    /**
     * Instantiates a new Game grid.
     *
     * @param X the x
     * @param Y the y
     */
    public GameGrid(int X, int Y) {
        this.X = X;
        this.Y = Y;

        // Initialize the array
        gameObjects = new AbstractGameObject[Y][X];
    }


    /**
     * static method
     * Translate point.
     *
     * @param sourceLocation Java.awt.Point, target source location
     * @param delta          Java.awt.Point, distance respective
     * @return Translated point from source and delta
     */
    public static Point translatePoint(Point sourceLocation, Point delta) {
        Point translatedPoint = new Point(sourceLocation);
        translatedPoint.translate((int) delta.getX(), (int) delta.getY());
        return translatedPoint;
    }

    /**
     * Gets game object at.
     *
     * @param x x axis
     * @param y y axis            Coordinates: system             ------------> +col             |             |             |             |             ∨             +row
     * @return corresponding GameObject in which position
     * @throws ArrayIndexOutOfBoundsException the array index out of bounds exception
     */
    public AbstractGameObject getGameObjectAt(int x, int y) throws ArrayIndexOutOfBoundsException {
        if (isPointOutOfBounds(x, y)) {
            throw new ArrayIndexOutOfBoundsException("The point [" + x + ":" + y + "] is outside the map.");
        }

        return gameObjects[y][x];
    }

    /**
     * Gets game object at.
     *
     * @param p Point object
     * @return corresponding GameObject in which position
     * @throws IllegalArgumentException       the illegal argument exception
     * @throws ArrayIndexOutOfBoundsException the array index out of bounds exception
     */
    public AbstractGameObject getGameObjectAt(Point p) throws IllegalArgumentException, ArrayIndexOutOfBoundsException {
        if (p == null) {
            throw new IllegalArgumentException("Point cannot be null.");
        }

        return getGameObjectAt((int) p.getX(), (int) p.getY());
    }


    /**
     * Put game object at boolean.
     *
     * @param gameObject the object to be put in
     * @param x          the x
     * @param y          the y
     * @return false if PointOutOfBounds
     */
    public boolean putGameObjectAt(AbstractGameObject gameObject, int x, int y) {
        if (isPointOutOfBounds(x, y)) {
            return false;
        }

        gameObjects[y][x] = gameObject;
        return gameObjects[y][x] == gameObject;
    }

    /**
     * Put game object at boolean.
     *
     * @param gameObject the object to be put in
     * @param p          Point
     */
    public void putGameObjectAt(AbstractGameObject gameObject, Point p) {
        if (p != null) {
            putGameObjectAt(gameObject, (int) p.getX(), (int) p.getY());
        }
    }

    /**
     * Is point out of bounds boolean.
     *
     * @param x the x
     * @param y the y
     * @return the boolean
     */
    public boolean isPointOutOfBounds(int x, int y) {
        return (x < 0 || y < 0 || x >= X || y >= Y);
    }

    /**
     * Is point out of bounds boolean.
     *
     * @param p the p
     * @return the boolean
     */
    public boolean isPointOutOfBounds(Point p) {
        return isPointOutOfBounds(p.x, p.y);
    }


    /**
     * format this grid into a 2d String, with line breaks indicated next line of Grud
     * @return a string
     */
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder(gameObjects.length);

        for (AbstractGameObject[] gameObject : gameObjects) {
            for (AbstractGameObject aGameObject : gameObject) {
                if (aGameObject == null) {
                    sb.append(' ');
                    continue;
                }
                sb.append(aGameObject.getCharSymbol());
            }
            sb.append('\n');
        }

        return sb.toString();
    }

    @Override
    public Iterator<AbstractGameObject> iterator() {
        return new GridIterator();
    }

    /**
     * The iterator to iterate through all elements.
     * if no element on the specified point, return null
     */
    public class GridIterator implements Iterator<AbstractGameObject> {
        /**
         * The X.
         */
        int x = 0;
        /**
         * The Y.
         */
        int y = 0;

        @Override
        public boolean hasNext() {
            return (x < X && y < Y);
        }

        @Override
        public AbstractGameObject next() {

            AbstractGameObject toReturn = getGameObjectAt(x, y);
            x++;
            if (x >= X) {
                x = 0;
                y++;
            }
            return toReturn;
        }
    }
}
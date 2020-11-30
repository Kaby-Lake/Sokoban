package com.ae2dms.Business.Data;

import com.ae2dms.Business.GameDocument;
import com.ae2dms.GameObject.AbstractGameObject;

import java.awt.*;
import java.io.Serializable;
import java.util.Iterator;

public class GameGrid implements Iterable<AbstractGameObject>, Serializable {

    public int getX() {
        return X;
    }

    public int getY() {
        return Y;
    }

    final int X;
    final int Y;

    private final AbstractGameObject[][] gameObjects;

    public GameGrid(int X, int Y) {
        this.X = X;
        this.Y = Y;

        // Initialize the array
        gameObjects = new AbstractGameObject[Y][X];
    }


    /**
     * @param sourceLocation Java.awt.Point, target source location
     * @param delta Java.awt.Point, distance respective
     * @return Translated point from source and delta
     */
    public static Point translatePoint(Point sourceLocation, Point delta) {
        Point translatedPoint = new Point(sourceLocation);
        translatedPoint.translate((int) delta.getX(), (int) delta.getY());
        return translatedPoint;
    }

    /**
     * @param source
     * @param delta
     * @return corresponding GameObject in which position
     * @throws ArrayIndexOutOfBoundsException
     */
    public AbstractGameObject getTargetFromSource(Point source, Point delta) throws ArrayIndexOutOfBoundsException {
        if (delta == null) {
            delta = new Point(0, 0);
        }
        return getGameObjectAt(translatePoint(source, delta));
    }

    /**
     * @param x x axis
     * @param y y axis
     *            Coordinates: system
     *             ------------> +col
     *             |
     *             |
     *             |
     *             |
     *             ∨
     *             +row
     * @return corresponding GameObject in which position
     * @throws ArrayIndexOutOfBoundsException
     */
    public AbstractGameObject getGameObjectAt(int x, int y) throws ArrayIndexOutOfBoundsException {
        if (isPointOutOfBounds(x, y)) {
            throw new ArrayIndexOutOfBoundsException("The point [" + x + ":" + y + "] is outside the map.");
        }

        return gameObjects[y][x];
    }

    /**
     * @param p Point object
     * @return corresponding GameObject in which position
     * @throws ArrayIndexOutOfBoundsException
     */
    public AbstractGameObject getGameObjectAt(Point p) throws IllegalArgumentException, ArrayIndexOutOfBoundsException {
        if (p == null) {
            throw new IllegalArgumentException("Point cannot be null.");
        }

        return getGameObjectAt((int) p.getX(), (int) p.getY());
    }

    public Dimension getDimension() {
        return new Dimension(X, Y);
    }


    /**
     * @param gameObject the object to be put in
     * @param x
     * @param y
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
     * @param gameObject the object to be put in
     * @param p Point
     * @return false if PointOutOfBounds, true otherwise
     */
    public boolean putGameObjectAt(AbstractGameObject gameObject, Point p) {
        return p != null && putGameObjectAt(gameObject, (int) p.getX(), (int) p.getY());
    }

    /**
     * @param position the position of object to be deleted
     * @return false if PointOutOfBounds, true otherwise
     */
    public boolean removeGameObjectAt(Point position) {
        return putGameObjectAt(null, position);
    }



    public boolean isPointOutOfBounds(int x, int y) {
        return (x < 0 || y < 0 || x >= X || y >= Y);
    }

    public boolean isPointOutOfBounds(Point p) {
        return isPointOutOfBounds(p.x, p.y);
    }


    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder(gameObjects.length);

        for (AbstractGameObject[] gameObject : gameObjects) {
            for (AbstractGameObject aGameObject : gameObject) {
                if (aGameObject == null) {
                    // aGameObject = GameObject1.DEBUG_OBJECT;
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

    public class GridIterator implements Iterator<AbstractGameObject> {
        int x = 0;
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
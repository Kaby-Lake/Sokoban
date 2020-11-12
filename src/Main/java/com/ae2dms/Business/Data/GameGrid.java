package com.ae2dms.Business.Data;

import com.ae2dms.Business.GameDocument;
import com.ae2dms.GameObject.*;

import java.awt.*;
import java.util.Iterator;

public class GameGrid implements Iterable {

    final int COLUMNS;
    final int ROWS;

    private AbstractGameObject[][] gameObjects;

    public GameGrid(int columns, int rows) {
        COLUMNS = columns;
        ROWS = rows;

        // Initialize the array
        gameObjects = new AbstractGameObject[COLUMNS][ROWS];
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
     * @param col x axis
     * @param row y axis
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
    public AbstractGameObject getGameObjectAt(int col, int row) throws ArrayIndexOutOfBoundsException {
        if (isPointOutOfBounds(col, row)) {
            if (GameDocument.isDebugActive()) {
                System.out.printf("Trying to get null GameObject from COL: %d  ROW: %d", col, row);
            }
            throw new ArrayIndexOutOfBoundsException("The point [" + col + ":" + row + "] is outside the map.");
        }

        return gameObjects[col][row];
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
        return new Dimension(COLUMNS, ROWS);
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

        gameObjects[x][y] = gameObject;
        return gameObjects[x][y] == gameObject;
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



    private boolean isPointOutOfBounds(int x, int y) {
        return (x < 0 || y < 0 || x >= COLUMNS || y >= ROWS);
    }

    private boolean isPointOutOfBounds(Point p) {
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
        int row = 0;
        int column = 0;

        @Override
        public boolean hasNext() {
            return !(row == ROWS && column == COLUMNS);
        }

        @Override
        public AbstractGameObject next() {
            if (column >= COLUMNS) {
                column = 0;
                row++;
            }
            return getGameObjectAt(column++, row);
        }
    }
}
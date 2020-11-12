package com.ae2dms.Business.Data;

import com.ae2dms.Business.GameDocument;
import com.ae2dms.GameObject.*;
import com.ae2dms.GameObject.Objects.*;

import java.awt.*;
import java.util.Iterator;
import java.util.List;

public final class Level implements Iterable<AbstractGameObject> {
    public final GameGrid objectsGrid;
    public final GameGrid diamondsGrid;
    private final String name;
    private final int index;
    private Point keeperPosition = new Point(0, 0);

    public Level(String levelName, int levelIndex, List<String> rawLevel) {
        if (GameDocument.isDebugActive()) {
            System.out.printf("[ADDING LEVEL] LEVEL [%d]: %s\n", levelIndex, levelName);
        }

        name = levelName;
        index = levelIndex;

        int rows = rawLevel.size();
        int columns = rawLevel.get(0).trim().length();

        objectsGrid = new GameGrid(rows, columns);
        diamondsGrid = new GameGrid(rows, columns);

        GameObjectFactory factory = new GameObjectFactory();

        for (int row = 0; row < rows; row++) {

            for (int col = 0; col < columns; col++) {
                char curChar = rawLevel.get(row).charAt(col);
                AbstractGameObject curTile;

                if (Character.toUpperCase(curChar) == 'D') {
                    curTile = factory.getGameObject(curChar, diamondsGrid, row, col);
                    diamondsGrid.putGameObjectAt(curTile, row, col);
                    // then put Floor Object in objectsGrid
                    curTile = factory.getGameObject(' ', objectsGrid, row, col);
                } else if (Character.toUpperCase(curChar) == 'S') {
                    keeperPosition = new Point(row, col);
                    curTile = factory.getGameObject(curChar, objectsGrid, row, col);
                } else {
                    curTile = factory.getGameObject(curChar, objectsGrid, row, col);
                }

                objectsGrid.putGameObjectAt(curTile, row, col);}
        }
    }

    public boolean isComplete() {
        boolean isComplete = true;
        for (int row = 0; row < objectsGrid.ROWS; row++) {
            for (int col = 0; col < objectsGrid.COLUMNS; col++) {
                AbstractGameObject thisObject = objectsGrid.getGameObjectAt(col, row);
                if (thisObject instanceof Crate && !((Crate) thisObject).isOnDiamond(this.diamondsGrid)) {
                    isComplete = false;
                }
            }
        }
        return isComplete;
    }

    public String getName() {
        return name;
    }

    public int getIndex() {
        return index;
    }

    public Point getKeeperPosition() {
        return keeperPosition;
    }

    public AbstractGameObject getTargetObject(Point source, Point delta) {
        return objectsGrid.getTargetFromSource(source, delta);
    }

    @Override
    public String toString() {
        return objectsGrid.toString();
    }

    @Override
    public Iterator<AbstractGameObject> iterator() {
        return new LevelIterator();
    }

    public class LevelIterator implements Iterator<AbstractGameObject> {

        int column = 0;
        int row = 0;

        @Override
        public boolean hasNext() {
            return !(row == objectsGrid.ROWS - 1 && column == objectsGrid.COLUMNS);
        }

        @Override
        public AbstractGameObject next() {
            if (column >= objectsGrid.COLUMNS) {
                column = 0;
                row++;
            }

            return objectsGrid.getGameObjectAt(column, row);
        }

        public Point getcurrentposition() {
            return new Point(column, row);
        }
    }
}
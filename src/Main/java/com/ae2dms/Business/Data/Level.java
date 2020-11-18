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

        int Y = rawLevel.size();
        int X = rawLevel.get(0).trim().length();

        objectsGrid = new GameGrid(X, Y);
        diamondsGrid = new GameGrid(X, Y);

        GameObjectFactory factory = new GameObjectFactory();

        for (int y = 0; y < Y; y++) {

            for (int x = 0; x < X; x++) {

                char curChar = rawLevel.get(y).charAt(x);
                AbstractGameObject curTile;

                if (Character.toUpperCase(curChar) == 'D') {
                    curTile = factory.getGameObject(curChar, diamondsGrid, x, y);
                    diamondsGrid.putGameObjectAt(curTile, x, y);
                    // then put Floor Object in objectsGrid
                    curTile = factory.getGameObject(' ', objectsGrid, x, y);
                } else if (Character.toUpperCase(curChar) == 'S') {
                    keeperPosition = new Point(x, y);
                    curTile = factory.getGameObject(curChar, objectsGrid, x, y);
                } else {
                    curTile = factory.getGameObject(curChar, objectsGrid, x, y);
                }

                objectsGrid.putGameObjectAt(curTile, x, y);}
        }
    }

    public boolean isComplete() {
        boolean isComplete = true;
        for (int y = 0; y < objectsGrid.Y; y++) {
            for (int x = 0; x < objectsGrid.X; x++) {
                AbstractGameObject thisObject = objectsGrid.getGameObjectAt(x, y);
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
            return !(row == objectsGrid.Y - 1 && column == objectsGrid.X);
        }

        @Override
        public AbstractGameObject next() {
            if (column >= objectsGrid.X) {
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
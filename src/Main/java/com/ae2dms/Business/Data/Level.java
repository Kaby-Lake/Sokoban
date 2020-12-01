package com.ae2dms.Business.Data;

import com.ae2dms.Business.GameDebugger;
import com.ae2dms.Business.GameDocument;
import com.ae2dms.GameObject.AbstractGameObject;
import com.ae2dms.GameObject.GameObjectFactory;
import com.ae2dms.GameObject.Objects.Crate;

import java.awt.*;
import java.io.Serializable;
import java.util.Iterator;
import java.util.List;

public final class Level implements Iterable<AbstractGameObject>, Serializable {
    public volatile GameGrid floorGrid;
    public volatile GameGrid objectsGrid;
    public volatile GameGrid diamondsGrid;
    public volatile GameGrid candyGrid;
    private final String name;
    private final int index;
    private Point playerPosition = new Point(0, 0);

    public Level(String levelName, int levelIndex, List<String> rawLevel) {
        GameDebugger.logReadLevel(levelIndex, levelName);

        name = levelName;
        index = levelIndex;

        int Y = rawLevel.size();
        int X = rawLevel.get(0).trim().length();

        floorGrid = new GameGrid(X, Y);
        objectsGrid = new GameGrid(X, Y);
        diamondsGrid = new GameGrid(X, Y);
        candyGrid = new GameGrid(X, Y);

        GameObjectFactory factory = new GameObjectFactory();

        for (int y = 0; y < Y; y++) {

            for (int x = 0; x < X; x++) {

                char curChar = rawLevel.get(y).charAt(x);
                AbstractGameObject curTile;

                switch (Character.toUpperCase(curChar)) {
                    case 'D' -> {
                        curTile = factory.getGameObject('D', this, x, y);
                        diamondsGrid.putGameObjectAt(curTile, x, y);

                        curTile = factory.getGameObject(' ', this, x, y);
                        floorGrid.putGameObjectAt(curTile, x, y);
                    }

                    // player
                    case 'S' -> {
                        playerPosition = new Point(x, y);
                        curTile = factory.getGameObject('S', this, x, y);
                        objectsGrid.putGameObjectAt(curTile, x, y);

                        curTile = factory.getGameObject(' ', this, x, y);
                        floorGrid.putGameObjectAt(curTile, x, y);
                    }

                    // Candy
                    case 'Y' -> {
                        curTile = factory.getGameObject('Y', this, x, y);
                        candyGrid.putGameObjectAt(curTile, x, y);

                        curTile = factory.getGameObject(' ', this, x, y);
                        floorGrid.putGameObjectAt(curTile, x, y);
                    }

                    // Crate
                    case 'C' -> {
                        curTile = factory.getGameObject('C', this, x, y);
                        objectsGrid.putGameObjectAt(curTile, x, y);

                        curTile = factory.getGameObject(' ', this, x, y);
                        floorGrid.putGameObjectAt(curTile, x, y);
                    }

                    // Space
                    case ' ' -> {
                        curTile = factory.getGameObject(' ', this, x, y);
                        floorGrid.putGameObjectAt(curTile, x, y);

                    }

                    // Space
                    case 'W' -> {
                        curTile = factory.getGameObject('W', this, x, y);
                        floorGrid.putGameObjectAt(curTile, x, y);

                    }
                }
            }
        }
    }

    public boolean isComplete() {
        boolean isComplete = true;
        for (int y = 0; y < objectsGrid.Y; y++) {
            for (int x = 0; x < objectsGrid.X; x++) {
                AbstractGameObject thisObject = objectsGrid.getGameObjectAt(x, y);
                if (thisObject instanceof Crate && !((Crate) thisObject).isOnDiamond()) {
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

    public Point getPlayerPosition() {
        return playerPosition;
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
    }
}
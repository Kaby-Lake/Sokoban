package com.ae2dms.Business.Data;

import com.ae2dms.Business.GameDebugger;
import com.ae2dms.GameObject.*;
import com.ae2dms.GameObject.Objects.*;

import java.io.Serializable;
import java.util.List;

/**
 * The type Level.
 */
public final class Level implements Serializable {

    /**
     * the GameGrid which only contains Floor Object, null at other positions
     *
     * @see Floor
     */
    public GameGrid floorGrid;

    /**
     * the GameGrid which only contains Player and Crate Object, null at other positions
     *
     * @see Crate
     * @see Player
     */
    public GameGrid objectsGrid;

    /**
     * the GameGrid which only contains Diamond Object, null at other positions
     *
     * @see Diamond
     */
    public GameGrid diamondsGrid;

    /**
     * the GameGrid which only contains Candy Object, null at other positions
     *
     * @see Candy
     */
    public GameGrid candyGrid;

    /**
     * Name of this Level, initialized by reading skb from file
     */
    private final String name;

    /**
     * Name of this Level, starting from 1
     */
    private final int index;

    /**
     * The Candy that has already been eaten
     */
    public int eatenCandyCount = 0;


    /**
     * The Player object in this level.
     */
    Player player;

    /**
     * a Level Object with specified name and index, will parse the rawLevel into private GameGrid field
     *
     * @param levelName  name of the level
     * @param levelIndex index of the level, starting from 1
     * @param rawLevel   a list of string of raw data from skb, one line in skb denotes a String in the List
     */
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

                    // Diamond
                    case 'D' -> {
                        curTile = factory.getGameObject('D', this, x, y);
                        diamondsGrid.putGameObjectAt(curTile, x, y);

                        curTile = factory.getGameObject(' ', this, x, y);
                        floorGrid.putGameObjectAt(curTile, x, y);
                    }

                    // Player
                    case 'S' -> {
                        curTile = factory.getGameObject('S', this, x, y);
                        player = (Player)curTile;
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

                    // Wall
                    case 'W' -> {
                        curTile = factory.getGameObject('W', this, x, y);
                        floorGrid.putGameObjectAt(curTile, x, y);

                    }
                }
            }
        }
    }

    /**
     * check if this level is complete
     *
     * @return is complete
     */
    public boolean isComplete() {
        boolean isComplete = true;
        for (AbstractGameObject thisObject : objectsGrid) {
            if (thisObject instanceof Crate && !((Crate) thisObject).isOnDiamond()) {
                isComplete = false;
            }
        }
        return isComplete;
    }

    /**
     * Gets the map name.
     *
     * @return getter of name
     */
    public String getName() {
        return name;
    }

    /**
     * Gets the map index.
     *
     * @return getter of index
     */
    public int getIndex() {
        return index;
    }

    /**
     * Gets player object.
     *
     * @return getter of Player object
     * @see Player
     */
    public Player getPlayerObject() {
        return this.player;
    }

    /**
     * @return print the string which contains the four grids
     */
    @Override
    public String toString() {
        String string = """
            FloorGrid:
            %s
            
            ObjectGrid:
            %s
            
            DiamondsGrid:
            %s
            
            CandyGrid:
            %s
    """;
        return string.formatted(floorGrid.toString(), objectsGrid.toString(), diamondsGrid.toString(), candyGrid.toString());
    }
}
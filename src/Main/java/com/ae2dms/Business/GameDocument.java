package com.ae2dms.Business;

import com.ae2dms.Business.Data.GameGrid;
import com.ae2dms.Business.Data.Level;
import com.ae2dms.IO.*;
import com.ae2dms.GameObject.*;

import java.awt.*;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.NoSuchElementException;

public class GameDocument {
    public int highestScore;
    public static final String GAME_NAME = "SokobanFX";
    public static GameLogger logger;
    public int movesCount = 0;
    public String mapSetName;
    private static boolean debug = false;
    private Level currentLevel;
    private List<Level> levels;
    private boolean gameComplete = false;

    public GameDocument(InputStream input, boolean production) {

        // TODO:
        this.highestScore = 200;

        try {
            logger = new GameLogger();
            this.loadGameFile(input);
            currentLevel = getNextLevel();
        } catch (IOException x) {
            System.out.println("Cannot create logger.");
        } catch (NoSuchElementException e) {
            logger.warning("Cannot load the default save file: " + e.getStackTrace());
        }
    }

    public static boolean isDebugActive() {
        return debug;
    }

    public void move(Point delta) {
        if (isGameComplete()) {
            return;
        }

        Point keeperPosition = currentLevel.getKeeperPosition();
        AbstractGameObject keeper = currentLevel.objectsGrid.getGameObjectAt(keeperPosition);
        Point targetObjectPoint = GameGrid.translatePoint(keeperPosition, delta);
        AbstractGameObject keeperTarget = currentLevel.objectsGrid.getGameObjectAt(targetObjectPoint);

        if (GameDocument.isDebugActive()) {
            System.out.println("Current level state:");
            System.out.println(currentLevel.toString());
            System.out.println("Keeper pos: " + keeperPosition);
            System.out.println("Movement source obj: " + keeper);
            System.out.printf("Target object: %s at [%s]", keeperTarget, targetObjectPoint);
        }

        boolean keeperMoved = false;

//        switch (keeperTarget) {
//
//            case WALL:
//                break;
//
//            case CRATE:
//
//                GameObject crateTarget = currentLevel.getTargetObject(targetObjectPoint, delta);
//                if (crateTarget != GameObject.FLOOR) {
//                    break;
//                }
//
//                currentLevel.objectsGrid.putGameObjectAt(currentLevel.objectsGrid.getGameObjectAt(GameGrid.translatePoint(targetObjectPoint, delta)), targetObjectPoint);
//                currentLevel.objectsGrid.putGameObjectAt(keeperTarget, GameGrid.translatePoint(targetObjectPoint, delta));
//                currentLevel.objectsGrid.putGameObjectAt(currentLevel.objectsGrid.getGameObjectAt(GameGrid.translatePoint(keeperPosition, delta)), keeperPosition);
//                currentLevel.objectsGrid.putGameObjectAt(keeper, GameGrid.translatePoint(keeperPosition, delta));
//                keeperMoved = true;
//                break;
//
//            case FLOOR:
//                currentLevel.objectsGrid.putGameObjectAt(currentLevel.objectsGrid.getGameObjectAt(GameGrid.translatePoint(keeperPosition, delta)), keeperPosition);
//                currentLevel.objectsGrid.putGameObjectAt(keeper, GameGrid.translatePoint(keeperPosition, delta));
//                keeperMoved = true;
//                break;
//
//            default:
//                logger.severe("The object to be moved was not a recognised GameObject.");
//                throw new AssertionError("This should not have happened. Report this problem to the developer.");
//        }

        if (keeperMoved) {
            keeperPosition.translate((int) delta.getX(), (int) delta.getY());
            movesCount++;
            if (currentLevel.isComplete()) {
                if (isDebugActive()) {
                    System.out.println("Level complete!");
                }

                currentLevel = getNextLevel();
            }
        }
    }

    // @change mapSetName
    private void loadGameFile(InputStream input) {
        GameFileLoader loader = new GameFileLoader();
        try {
            loader.loadGameFile(input);
            this.levels = loader.getLevels();
            this.mapSetName = loader.getMapSetName();

        } catch (IOException e) {
            logger.severe("Error trying to load the game file: " + e);
        } catch (NullPointerException e) {
            logger.severe("Cannot open the requested file: " + e);
        }
    }

    public boolean isGameComplete() {
        return gameComplete;
    }

    public Level getNextLevel() {
        if (currentLevel == null) {
            return levels.get(0);
        }
        int currentLevelIndex = currentLevel.getIndex();
        if (currentLevelIndex < levels.size()) {
            return levels.get(currentLevelIndex + 1);
        }
        gameComplete = true;
        return null;
    }

    public Level getCurrentLevel() {
        return currentLevel;
    }

    public void toggleDebug(Boolean bool) {
        debug = bool;
    }

    public void toggleMusic(Boolean bool) {
    }

    public void reloadMapFromFile(InputStream input) {
        // TODO: flush the whole Object with new Data
    }

    public void reloadDataFromFile(InputStream input) {
        // TODO: flush the whole Object with new Data
    }
}
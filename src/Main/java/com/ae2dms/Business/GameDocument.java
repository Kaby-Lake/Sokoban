package com.ae2dms.Business;

import com.ae2dms.Business.Data.GameGrid;
import com.ae2dms.Business.Data.Level;
import com.ae2dms.GameObject.Objects.Keeper;
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
    private Keeper keeperObject;
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

    public Keeper getKeeper() {
        return this.keeperObject;

    }

    public static boolean isDebugActive() {
        return debug;
    }

    public void move(Point delta) {

//        if (keeperMoved) {
//            keeperPosition.translate((int) delta.getX(), (int) delta.getY());
//            movesCount++;
//            if (currentLevel.isComplete()) {
//                if (isDebugActive()) {
//                    System.out.println("Level complete!");
//                }
//
//                currentLevel = getNextLevel();
//            }
//        }
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
        int nextLevelIndex = currentLevel == null ? 0 : currentLevel.getIndex() + 1;
        if (nextLevelIndex == levels.size()) {
            gameComplete = true;
            return null;
        }

        Level nextLevel = levels.get(nextLevelIndex);
        this.keeperObject = (Keeper) nextLevel.getTargetObject(nextLevel.getKeeperPosition(), null);
        return nextLevel;
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
package com.ae2dms.Business;

import com.ae2dms.Business.Data.Level;
import com.ae2dms.GameObject.Objects.Player;
import com.ae2dms.IO.*;

import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.util.List;
import java.util.NoSuchElementException;

public class GameDocument implements Serializable {
    public int highestScore;
    public static final String GAME_NAME = "SokobanFX";
    public static transient GameLogger logger;
    public int movesCount = 0;
    public String mapSetName;
    private transient static boolean debug = false;
    private Level currentLevel;
    private List<Level> levels;
    private Player playerObject;
    private boolean gameComplete = false;

    public GameDocument(InputStream input, boolean production) {
        init(input);
    }

    private void init(InputStream input) {
        // TODO:
        this.highestScore = 200;

        try {
            logger = new GameLogger();
            this.loadMapFile(input);
            currentLevel = getNextLevel();
        } catch (IOException x) {
            System.out.println("Cannot create logger.");
        } catch (NoSuchElementException e) {
            logger.warning("Cannot load the default map file: " + e.getStackTrace());
        }
    }

    public Player getPlayer() {
        return this.playerObject;

    }

    public static boolean isDebugActive() {
        return debug;
    }

    // @change mapSetName
    private void loadMapFile(InputStream input) {
        MapFileLoader loader = new MapFileLoader();
        try {
            loader.loadMapFile(input);
            this.levels = loader.getLevels();
            this.mapSetName = loader.getMapSetName();

        } catch (IOException e) {
            logger.severe("Error trying to load the game file: " + e);
        } catch (NullPointerException e) {
            logger.severe("Cannot open the requested file: " + e);
        }
    }

    public boolean isLevelComplete() {
        return this.currentLevel.isComplete();
    }

    public void changeToNextLevel() {
        assert(this.currentLevel.isComplete());
        this.currentLevel = getNextLevel();
    }

    public Level getNextLevel() {
        int nextLevelIndex = currentLevel == null ? 0 : currentLevel.getIndex() + 1;
        if (nextLevelIndex == levels.size()) {
            gameComplete = true;
            return null;
        }

        Level nextLevel = levels.get(nextLevelIndex);
        this.playerObject = (Player) nextLevel.getTargetObject(nextLevel.getPlayerPosition(), null);
        return nextLevel;
    }

    public Level getCurrentLevel() {
        return currentLevel;
    }

    public int getLevelsCount() {
        return levels.size();
    }

    public void toggleDebug(Boolean bool) {
        debug = bool;
    }

    public void reloadMapFromFile(InputStream input) {
        init(input);
        this.highestScore = 0;
        this.movesCount = 0;
    }

    public void reloadDataFromFile(InputStream input) {
        // TODO: flush the whole Object with new Data
    }

    public void serializeObject() {
        try {
            GameStageSaver.push(this);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void restoreObject(GameDocument object) {
        this.highestScore = object.highestScore;
        this.movesCount = object.movesCount;
        this.mapSetName = object.mapSetName;
        this.currentLevel = object.currentLevel;
        this.levels = object.levels;
        this.playerObject = object.playerObject;
        this.gameComplete = object.gameComplete;
    }
}
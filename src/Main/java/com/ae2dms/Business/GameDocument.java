package com.ae2dms.Business;

import com.ae2dms.Business.Data.GameRecord;
import com.ae2dms.Business.Data.Level;
import com.ae2dms.GameObject.Objects.Player;
import com.ae2dms.IO.MapFileLoader;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;

import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.util.List;
import java.util.NoSuchElementException;

public class GameDocument implements Serializable {
    public transient IntegerProperty highestScore = new SimpleIntegerProperty(0);
    private int highestScoreSerializable;   // only used for backup of IntegerProperty highestScore

    public static final String GAME_NAME = "SokobanFX";
    public static transient GameLogger logger;

    public transient IntegerProperty movesCount = new SimpleIntegerProperty(0);
    private int movesCountSerializable; // only used for backup of IntegerProperty movesCount

    public String mapSetName;
    private Level currentLevel;
    private List<Level> levels;
    private Player playerObject;
    private boolean gameComplete = false;
    private int initialMapHashCode;

    public static transient GameRecord records = new GameRecord();

    public GameDocument(InputStream input, boolean production) {
        init(input);
    }

    private void init(InputStream input) {

        try {
            logger = new GameLogger();
            this.loadMapFile(input);
            currentLevel = getFirstLevel();
            records.restoreRecords(mapSetName, initialMapHashCode);
            this.highestScore.bindBidirectional(records.bestRecord);
        } catch (NoSuchElementException e) {
            logger.warning("Cannot load the map file: " + e.getStackTrace());
        }

        loadGameRecords();
        this.highestScore.bindBidirectional(records.bestRecord);
        serializeInitialState();
    }

    public Player getPlayer() {
        return this.playerObject;
    }

    // @change mapSetName
    private void loadMapFile(InputStream input) {
        MapFileLoader loader = new MapFileLoader();
        try {
            if (!loader.loadMapFile(input)) {
                GameDebugger.logLoadMapFailure();
                return;
            }

            this.levels = loader.getLevels();
            this.mapSetName = loader.getMapSetName();
            this.initialMapHashCode = loader.getMapHashCode();

        } catch (IOException e) {
            logger.severe("Error trying to load the game file: " + e);
        } catch (NullPointerException e) {
            logger.severe("Cannot open the requested file: " + e);
        }
    }

    public boolean isLevelComplete() {
        return this.currentLevel.isComplete();
    }

    public boolean isGameComplete() {
        for (Level level : levels) {
            if (!level.isComplete()) return false;
        }
        return true;
    }

    public void changeToNextLevel() {
        assert(this.currentLevel.isComplete());
        this.currentLevel = getNextLevel();
    }

    public Level getNextLevel() {
        int nextLevelIndex = currentLevel == null ? 0 : currentLevel.getIndex();
        if (nextLevelIndex == levels.size()) {
            gameComplete = true;
            return null;
        }

        Level nextLevel = levels.get(nextLevelIndex);
        this.playerObject = (Player) nextLevel.getPlayerObject();
        return nextLevel;
    }

    public Level getFirstLevel() {
        Level nextLevel = levels.get(0);
        this.playerObject = (Player) nextLevel.getPlayerObject();
        return nextLevel;
    }

    public Level getCurrentLevel() {
        return currentLevel;
    }

    public int getLevelsCount() {
        return levels.size();
    }

    public void reloadMapFromFile(InputStream input) {
        this.init(input);
        this.movesCount.set(0);
    }

    public void reloadStateFromFile(InputStream input) {
        // TODO: flush the whole Object with new Data
    }

    private void serializeInitialState() {
        try {
            this.movesCountSerializable = this.movesCount.getValue();
            this.highestScoreSerializable = this.highestScore.getValue();
            GameStageSaver.pushInitialState(this);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void serializeCurrentState() {
        try {
            this.movesCountSerializable = this.movesCount.getValue();
            this.highestScoreSerializable = this.highestScore.getValue();
            GameStageSaver.push(this);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void restoreObject(GameDocument object) {
        if (object == null) {
            return;
        }
        this.highestScore.set(object.highestScoreSerializable);
        this.movesCount.set(object.movesCountSerializable);

        this.mapSetName = object.mapSetName;
        this.currentLevel = object.currentLevel;
        this.levels = object.levels;
        this.playerObject = object.playerObject;
        this.gameComplete = object.gameComplete;
        this.initialMapHashCode = object.initialMapHashCode;
    }

    private void loadGameRecords() {
        records.restoreRecords(this.mapSetName, this.initialMapHashCode);
    }

    public void saveRecord(String name, int time, int score) {
        records.pushRecord(score, name, time);
    }

    public GameRecord getRecords() {
        return records;
    }
}
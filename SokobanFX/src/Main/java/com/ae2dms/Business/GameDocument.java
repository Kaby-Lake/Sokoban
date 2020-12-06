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
import java.util.Scanner;

/**
 * the Model in MVC, only contains the data and underlying logic
 * can be serialized to store every state
 */
public class GameDocument implements Serializable {

    /**
     * the Best Record of this map, will be bind by View after
     */
    public transient IntegerProperty bestRecord = new SimpleIntegerProperty(0);
    /**
     * the Serializable best record, only used as cache of bestRecord before serializing this document
     */
    private int bestRecordSerializable;

    /**
     * The name of this game
     */
    public static final String GAME_NAME = "SokobanFX";
    /**
     * The static logger
     */
    public static transient GameLogger logger;

    /**
     * the Moves Count of this map, will be bind by View after
     */
    public transient IntegerProperty movesCount = new SimpleIntegerProperty(0);

    /**
     * the Serializable move count, only used as cache of MovesCount before serializing this document
     */
    private int movesCountSerializable;

    /**
     * The customized mapsetName defined in map file
     */
    public String mapSetName;

    /**
     * always point to the current level
     */
    private Level currentLevel;

    /**
     * The arraylist of Levels to store all levels read from map file
     */
    private List<Level> levels;

    /**
     * always point to the Player object in the current level
     */
    private Player playerObject;

    /**
     * the hashcode of initial Map, initialized later by MapFileLoader, to authenticate the record file
     */
    private int initialMapHashCode;

    /**
     * The static GameRecord to store all records, can restore all records from defined directory by calling restoreRecords()
     *
     * @see GameRecord
     */
    public static transient GameRecord records = new GameRecord();

    /**
     * Instantiates a new Game document.
     *
     * @param input the input
     * @throws ErrorMapFileLoadException the error map file load exception
     */
    public GameDocument(InputStream input) throws MapFileLoader.ErrorMapFileLoadException {
        init(input);
    }

    /**
     * the init method to call when load a new GameDocument
     * @param input input stream of raw map
     */
    private void init (InputStream input) throws MapFileLoader.ErrorMapFileLoadException {
        logger = new GameLogger();
        try {
            loadMapFile(input);
        } catch (NoSuchElementException e) {
            logger.warning("Cannot load the map file: " + e.getStackTrace());
        }
        changeToNextLevel();
        records.bestRecord.addListener((observable, oldValue, newValue) -> {
            if (observable != null) {
                if (observable.getValue().intValue() == Integer.MAX_VALUE) {
                    this.bestRecord.set(0);
                } else {
                    this.bestRecord.set(observable.getValue().intValue());
                }
            }
        });
        records.restoreRecords(mapSetName, initialMapHashCode);
        serializeInitialState();
    }

    /**
     * Gets player.
     *
     * @return return the Player object of this level, will be changed when level switched
     */
    public Player getPlayer() {
        return this.playerObject;
    }

    /**
     * load the Map from input given
     * will change the levels, mapSetName and initialMapHashCode accordingly
     * @param input input stream of raw map
     */
    private void loadMapFile(InputStream input) throws MapFileLoader.ErrorMapFileLoadException {
        MapFileLoader loader = new MapFileLoader();
        try {
            if (!loader.loadMapFile(input)) {
                GameDebugger.logLoadMapFailure();
                throw new MapFileLoader.ErrorMapFileLoadException();
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

    /**
     * check if the current level is complete
     *
     * @return boolean boolean
     */
    public boolean isLevelComplete() {
        return this.currentLevel.isComplete();
    }

    /**
     * Is game complete boolean.
     *
     * @return the boolean
     */
    public boolean isGameComplete() {
        for (Level level : levels) {
            if (!level.isComplete()) {
                return false;
            }
        }
        return true;
    }

    /**
     * change to next level, and change the PlayerObject as well
     * if is the last level, then the current level will be set to null
     */
    public void changeToNextLevel() {
        this.currentLevel = getNextLevel();
        if (currentLevel != null) {
            this.playerObject = currentLevel.getPlayerObject();
        }
    }

    /**
     * get the next level from current level, if first called, will set to the first level
     * if there is no more levels left, will return null
     *
     * @return the next level object or null if no more lefts
     */
    public Level getNextLevel() {
        int nextLevelIndex = currentLevel == null ? 1 : (currentLevel.getIndex() + 1);
        if (nextLevelIndex == (levels.size() + 1)) {
            return null;
        }
        return levels.get(nextLevelIndex - 1);
    }

    /**
     * Gets current level.
     *
     * @return the current level
     */
    public Level getCurrentLevel() {
        return currentLevel;
    }

    /**
     * Gets levels count.
     *
     * @return the total counts of all levels
     */
    public int getLevelsCount() {
        return levels.size();
    }

    /**
     * to reload a customized map from inputStream
     * will flush all data and state stored before
     *
     * @param input the InputStream of the map file
     * @throws ErrorMapFileLoadException the error map file load exception
     */
    public void reloadMapFromFile(InputStream input) throws MapFileLoader.ErrorMapFileLoadException {
        this.currentLevel = null;
        this.init(input);
        this.movesCount.set(0);
    }

    /**
     * to reload a previously saved .skbsave from inputStream
     * will flush all data and state stored before
     *
     * @param input the InputStream of the save file
     * @throws ErrorSaveFileLoadException the error save file load exception
     */
    public void reloadStateFromFile(InputStream input) throws MapFileLoader.ErrorSaveFileLoadException {
        Scanner s = new Scanner(input);
        String result = s.hasNext() ? s.next() : "";
        GameDocument restoreObject = null;
        try {
            restoreObject = (GameDocument)GameStageSaver.decode(result);
        } catch (IOException | ClassNotFoundException | IllegalArgumentException e) {
            throw new MapFileLoader.ErrorSaveFileLoadException();
        }
        if (restoreObject == null) {
            GameDebugger.logErrorMessage("not a valid skbsave file");
            return;
        }
        this.restoreObject(restoreObject);
    }

    /**
     * Push the initial state of this document to GameStageSaver
     * can be used when re-start the game from menu
     * @see GameStageSaver
     */
    private void serializeInitialState() {
        try {
            this.movesCountSerializable = this.movesCount.getValue();
            this.bestRecordSerializable = this.bestRecord.getValue();
            GameStageSaver.pushInitialState(this);
        } catch (Exception e) {
            logger.severe(e.getMessage());
        }
    }

    /**
     * Push the current state of this document to GameStageSaver
     * to realize the redo function, as GameStageSaver will store a Stack of serialized document
     *
     * @see GameStageSaver
     */
    public void serializeCurrentState() {
        try {
            this.movesCountSerializable = this.movesCount.getValue();
            this.bestRecordSerializable = this.bestRecord.getValue();
            GameStageSaver.push(this);
        } catch (Exception e) {
            logger.severe(e.getMessage());
        }
    }

    /**
     * restore some of the data field to this from the given another GameDocument Object
     *
     * @param object another GameDocument with data to be stored to this document
     */
    public void restoreObject(GameDocument object) {
        if (object == null) {
            return;
        }
        this.bestRecord.set(object.bestRecordSerializable);
        this.movesCount.set(object.movesCountSerializable);

        this.mapSetName = object.mapSetName;
        this.currentLevel = object.currentLevel;
        this.levels = object.levels;
        this.playerObject = object.playerObject;
        this.initialMapHashCode = object.initialMapHashCode;
    }

    /**
     * push this record to GmeRecord
     *
     * @param steps the steps of this turn
     * @param name  the name inputted by user
     * @param time  the seconds duration of this turn
     * @see GameRecord
     */
    public void saveRecord(int steps, String name, int time) {
        records.pushRecord(steps, name, time);
    }

    /**
     * Undo to the last state
     *
     * @return true if restored, false if no stage available
     */
    public boolean undo() {
        GameDocument restoreObject = GameStageSaver.pop();
        if (restoreObject != null) {
            this.restoreObject(restoreObject);
            GameDebugger.logUndo(this);
            return true;
        }
        return false;
    }
}
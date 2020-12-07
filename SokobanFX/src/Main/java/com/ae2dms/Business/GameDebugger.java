package com.ae2dms.Business;

import com.ae2dms.Business.Data.GameGrid;
import com.ae2dms.Business.Data.Level;
import com.ae2dms.GameObject.AbstractGameObject;
import com.ae2dms.GameObject.Objects.Crate;
import com.ae2dms.GameObject.Objects.Player;
import com.ae2dms.GameObject.Objects.Wall;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;

import java.awt.*;
import java.io.File;

/**
 * the Debug helper
 * add debug information when debug is active
 */
public class GameDebugger {
    /**
     * The constant active.
     */
    public static BooleanProperty active = new SimpleBooleanProperty(false);

    private static final GameLogger debugger = new GameLogger();

    /**
     * Log movement.
     *
     * @param document  the document
     * @param direction the direction
     */
    public static void logMovement(GameDocument document, Point direction) {
        if (!active.getValue()) {
            return;
        }
        LogMessage log = new LogMessage();

        String emoji;
        if (direction.getX() == 1 && direction.getY() == 0) {
            emoji = "➡️";
        } else if (direction.getX() == 0 && direction.getY() == 1) {
            emoji = "⬇️";
        } else if (direction.getX() == -1 && direction.getY() == 0) {
            emoji = "⬅️";
        } else {
            emoji = "⬆️";
        }
        Player player = document.getPlayer();
        Point targetPosition = GameGrid.translatePoint(player.at(), direction);
        log.addInfo("Current Level State:");
        log.addInfo(document.getCurrentLevel().toString());
        log.addInfo("Keeper Info: " + document.getPlayer().toString());

        if (document.getCurrentLevel().objectsGrid.isPointOutOfBounds(targetPosition)) {
            log.addInfo("Movement Status: " + "❕ Failed to Move: Destination on " + emoji + targetPosition + " is Outside of Map");
        } else {
            AbstractGameObject objectOnDestination = document.getCurrentLevel().objectsGrid.getGameObjectAt(targetPosition);

            if (player.canMoveBy(direction)) {
                if (objectOnDestination instanceof Crate) {
                    log.addInfo("Movement Status: " + "☑️ Successfully Pushed Crate at " + objectOnDestination.toString() + " " + emoji + " By 1 Step");
                } else {
                    log.addInfo("Movement Status: " + "☑️ Successfully Moved " + emoji + " By 1 Step");
                }
            } else {
                if (objectOnDestination instanceof Wall) {
                    log.addInfo("Movement Status: " + "❕ Failed to Move: Destination on " + emoji + " is Wall");
                } else {
                    log.addInfo("Movement Status: " + "❕ Failed to Move: " + objectOnDestination.toString() + " on " + emoji + " Cannot be Moved " + emoji + " Further");
                }
            }
        }
        log.newLine();
        debugger.info(log.get());
    }

    /**
     * Log level complete.
     *
     * @param currentLevel the current level
     * @param time         the time
     * @param score        the score
     */
    public static void logLevelComplete(Level currentLevel, String time, String score) {
        if (!active.getValue()) {
            return;
        }
        LogMessage log = new LogMessage();
        log.addInfo("ℹ️ Level " + currentLevel.getIndex() + " Complete in Total Game Time of " + time + " with Total Game Score of " + score);
        debugger.info(log.get());

    }

    /**
     * Log game complete.
     *
     * @param totalLevels the total levels
     * @param time        the time
     * @param score       the score
     */
    public static void logGameComplete(int totalLevels, String time, String score) {
        if (!active.getValue()) {
            return;
        }
        LogMessage log = new LogMessage();
        log.addInfo("ℹ️ " + totalLevels + " Levels All Complete in Total Game Time of " + time + " with Total Game Score of " + score);
        debugger.info(log.get());
    }

    /**
     * Log undo.
     *
     * @param document the document
     */
    public static void logUndo(GameDocument document) {
        if (!active.getValue()) {
            return;
        }
        LogMessage log = new LogMessage();
        log.addInfo("ℹ️ Undo to Previous State");
        log.addInfo(document.getCurrentLevel().toString());
        debugger.info(log.get());
    }

    /**
     * Log load map file.
     *
     * @param file the file
     */
    public static void logLoadMapFile(File file) {
        if (!active.getValue()) {
            return;
        }
        LogMessage log = new LogMessage();
        log.addInfo("Loading Map File: " + file.getAbsolutePath());
        debugger.info(log.get());
    }

    /**
     * Log load map failure.
     */
    public static void logLoadMapFailure() {
        if (!active.getValue()) {
            return;
        }
        LogMessage log = new LogMessage();
        log.addInfo("Failed to Load Map, Not a Valid Map File");
        debugger.warning(log.get());
    }

    /**
     * Log load map failure with reason.
     *
     * @param pointer the pointer
     * @param message the message
     */
    public static void logLoadMapFailureWithReason(int pointer, String message) {
        if (!active.getValue()) {
            return;
        }
        LogMessage log = new LogMessage();
        log.addWarring("Error in Line 👉 " + pointer + " " + message);
        debugger.warning(log.get());
    }

    /**
     * Log read level.
     *
     * @param levelIndex the level index
     * @param levelName  the level name
     */
    public static void logReadLevel(int levelIndex, String levelName) {
        if (!active.getValue()) {
            return;
        }
        LogMessage log = new LogMessage();

        debugger.info(log.get());
    }

    /**
     * Log error message.
     *
     * @param msg the msg
     */
    public static void logErrorMessage(String msg) {
        if (!active.getValue()) {
            return;
        }
        LogMessage log = new LogMessage();
        log.addSevere("Exception: " + msg);
        debugger.severe(log.get());
    }
}

/**
 * The type Log message.
 */
class LogMessage {
    private String logMessage;

    /**
     * Instantiates a new Log message.
     */
    public LogMessage() {
        this.logMessage = "";
    }

    /**
     * Add info.
     *
     * @param log the log
     */
    public void addInfo(String log) {
        this.logMessage += "ℹ️ " + log + "\n";
    }

    /**
     * Add warring.
     *
     * @param log the log
     */
    public void addWarring(String log) {
        this.logMessage += "⚠️ " + log + "\n";
    }

    /**
     * Add severe.
     *
     * @param log the log
     */
    public void addSevere(String log) {
        this.logMessage += "❌ " + log + "\n";
    }

    /**
     * Get string.
     *
     * @return the string
     */
    public String get() {
        return logMessage;
    }

    /**
     * New line.
     */
    public void newLine() {
        this.logMessage += "\n";
    }
}



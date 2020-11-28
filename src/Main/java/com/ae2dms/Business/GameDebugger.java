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

public class GameDebugger {
    public static BooleanProperty active = new SimpleBooleanProperty(false);

    private static GameLogger logger = new GameLogger();

    public static void logMovement(GameDocument document, Point direction) {
        LogMessage log = new LogMessage();

        if (active.getValue()) {
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
                log.addInfo("Target Object: " + objectOnDestination.toString());

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
        }
        log.newLine();
        logger.info(log.get());
    }

    public static void logLevelComplete(Level currentLevel, String time, String score) {
        LogMessage log = new LogMessage();
        log.addInfo("ℹ️ Level " + currentLevel.getIndex() + " Complete in Total Game Time of " + time + " with Total Game Score of " + score);
        logger.info(log.get());

    }

    public static void logGameComplete(int totalLevels, String time, String score) {
        LogMessage log = new LogMessage();
        log.addInfo("ℹ️ " + totalLevels + " Levels All Complete in Total Game Time of " + time + " with Total Game Score of " + score);
        logger.info(log.get());
    }

    public static void logUndo(GameDocument document) {
        LogMessage log = new LogMessage();
        log.addInfo("ℹ️ Undo to Previous State");
        log.addInfo(document.getCurrentLevel().toString());
        logger.info(log.get());
    }

    public static void logLoadMapFile(File file) {
        LogMessage log = new LogMessage();
        log.addInfo("Loading Map File: " + file.getAbsolutePath());
        logger.info(log.get());
    }

    public static void logLoadMapFailure() {
        LogMessage log = new LogMessage();
        log.addInfo("Failed to Load Map, Not a Valid Map File");
        logger.warning(log.get());
    }

    public static void logLoadMapFailureWithReason(int pointer, String message) {
        LogMessage log = new LogMessage();
        log.addWarring("Error in Line 👉 " + pointer + " " + message);
        logger.warning(log.get());
    }

    public static void logLoadMapSuccess(File file) {
        // TODO:

    }

    public static void logReadLevel(int levelIndex, String levelName) {
        LogMessage log = new LogMessage();

        logger.info(log.get());
    }

    public static void logErrorMessage(String msg) {
        LogMessage log = new LogMessage();
        log.addSevere("Exception: " + msg);
        logger.severe(log.get());
    }
}

class LogMessage {
    private String logMessage;

    public LogMessage() {
        this.logMessage = "";
    }

    public void addInfo(String log) {
        this.logMessage += "ℹ️ " + log + "\n";
    }

    public void addWarring(String log) {
        this.logMessage += "⚠️ " + log + "\n";
    }

    public void addSevere(String log) {
        this.logMessage += "❌ " + log + "\n";
    }

    public String get() {
        return logMessage;
    }

    public void newLine() {
        this.logMessage += "\n";
    }
}



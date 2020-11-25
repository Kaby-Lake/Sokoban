package com.ae2dms.Business;

import com.ae2dms.Business.Data.GameGrid;
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

    public static void logMovement(GameDocument document, Point direction) {
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
            System.out.println("ℹ️ Current Level State:");
            System.out.println(document.getCurrentLevel().toString());
            System.out.println("ℹ️ Keeper Info: " + document.getPlayer().toString());
            if (document.getCurrentLevel().objectsGrid.isPointOutOfBounds(targetPosition)) {
                System.out.println("ℹ️ Movement Status: " + "⚠️ Failed to Move: Destination on " + emoji + targetPosition + " is Outside of Map");
            } else {
                AbstractGameObject objectOnDestination = document.getCurrentLevel().objectsGrid.getGameObjectAt(targetPosition);
                System.out.println("ℹ️ Target Object: " + objectOnDestination.toString());

                if (player.canMoveBy(direction)) {
                    if (objectOnDestination instanceof Crate) {
                        System.out.println("ℹ️ Movement Status: " + "☑️ Successfully Pushed Crate at " + objectOnDestination.toString() + " " + emoji + " By 1 Step");
                    } else {
                        System.out.println("ℹ️ Movement Status: " + "☑️ Successfully Moved " + emoji + " By 1 Step");
                    }
                } else {
                    if (objectOnDestination instanceof Wall) {
                        System.out.println("ℹ️ Movement Status: " + "⚠️ Failed to Move: Destination on " + emoji + " is Wall");
                    } else {
                        System.out.println("ℹ️ Movement Status: " + "⚠️ Failed to Move: " + objectOnDestination.toString() + " on " + emoji + " Cannot be Moved " + emoji + " Further");
                    }
                }
            }
        }
        System.out.println();
    }

    public static void logLevelComplete() {
        // TODO:

        System.out.println("ℹ️ Level Complete");

    }

    public static void logGameComplete() {
        // TODO:
    }

    public static void logUndo() {

    }

    public static void logLoadMapFile(File file) {

    }

    public static void logReadLevel(int levelIndex, String levelName) {

    }

}

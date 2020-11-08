package GameObject;

import Business.Data.GameGrid;
import GameObject.Objects.*;

public class GameObjectFactory {
    public AbstractGameObject getGameObject(String type, GameGrid linkTo, int xPosition, int yPosition) {

        if (type == null) {
            System.err.println("GameObject Type Missing");
            return null;
        }

        switch (type.toUpperCase()) {
            case "CRATE" -> {
                return new Crate(linkTo, xPosition, yPosition);
            }
            case "DIAMOND" -> {
                return new Diamond(linkTo, xPosition, yPosition);
            }
            case "FLOOR" -> {
                return new Floor(linkTo, xPosition, yPosition);
            }
            case "KEEPER" -> {
                return new Keeper(linkTo, xPosition, yPosition);
            }
            case "WALL" -> {
                return new Wall(linkTo, xPosition, yPosition);
            }
        }

        System.err.println("Unrecognizable GameObject Type");
        return null;
    }
    public AbstractGameObject getGameObject(Character type, GameGrid linkTo, int xPosition, int yPosition) {

        if (type == null) {
            System.err.println("GameObject Type Missing");
            return null;
        }

        switch (Character.toUpperCase(type)) {
            case 'C' -> {
                return new Crate(linkTo, xPosition, yPosition);
            }
            case 'D' -> {
                return new Diamond(linkTo, xPosition, yPosition);
            }
            case ' ' -> {
                return new Floor(linkTo, xPosition, yPosition);
            }
            case 'S' -> {
                return new Keeper(linkTo, xPosition, yPosition);
            }
            case 'W' -> {
                return new Wall(linkTo, xPosition, yPosition);
            }
        }

        System.err.println("Unrecognizable GameObject Type");
        return null;
    }
}

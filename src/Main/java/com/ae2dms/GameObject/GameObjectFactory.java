package com.ae2dms.GameObject;

import com.ae2dms.Business.Data.GameGrid;
import com.ae2dms.GameObject.Objects.*;

public class GameObjectFactory {


    // diamonds Object is only for Crate object to detect whether on diamond
    public AbstractGameObject getGameObject(Character type, GameGrid linkTo, int xPosition, int yPosition, GameGrid diamondsGrid) {

        if (type == null) {
            System.err.println("GameObject Type Missing");
            return null;
        }

        switch (Character.toUpperCase(type)) {
            case 'C' -> {
                return new Crate(linkTo, xPosition, yPosition, diamondsGrid);
            }
            case 'D' -> {
                return new Diamond(linkTo, xPosition, yPosition);
            }
            case ' ' -> {
                return new Floor(linkTo, xPosition, yPosition);
            }
            case 'S' -> {
                return new Player(linkTo, xPosition, yPosition);
            }
            case 'W' -> {
                return new Wall(linkTo, xPosition, yPosition);
            }
            default -> {
                System.err.println("Unrecognizable GameObject Type");
                return null;
            }
        }
    }
}

package com.ae2dms.GameObject;

import com.ae2dms.Business.Data.Level;
import com.ae2dms.GameObject.Objects.*;

/**
 * The type Game object factory.
 */
public class GameObjectFactory {


    /**
     * Gets game object.
     *
     * @param type      the type
     * @param linkTo    the link to
     * @param xPosition the x position
     * @param yPosition the y position
     * @return the game object
     */
// diamonds Object is only for Crate object to detect whether on diamond
    public AbstractGameObject getGameObject(Character type, Level linkTo, int xPosition, int yPosition) {

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
                return new Player(linkTo, xPosition, yPosition);
            }
            case 'W' -> {
                return new Wall(linkTo, xPosition, yPosition);
            }
            case 'Y' -> {
                return new Candy(linkTo, xPosition, yPosition);
            }
            default -> {
                System.err.println("Unrecognizable GameObject Type");
                return null;
            }
        }
    }
}

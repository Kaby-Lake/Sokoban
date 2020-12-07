package com.ae2dms.GameObject;

import com.ae2dms.GameObject.Objects.IllegalMovementException;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;

import java.awt.*;

/**
 * The interface Movable.
 */
public interface Movable {

    /**
     * Can move by boolean.
     *
     * @param delta how far to be moved              should only be one step
     * @return whether can moved to which position, according to the map constraints
     */
    Boolean canMoveBy(Point delta);

    /**
     * Move by.
     *
     * @param delta how far to be moved              should only be one step
     * @throws IllegalMovementException the illegal movement exception
     */
    void moveBy(Point delta) throws IllegalMovementException;

    /**
     * Move more than one step boolean.
     *
     * @param delta the delta
     * @return the boolean
     */
    default Boolean moveMoreThanOneStep(Point delta) {
        return delta.distance(0, 0) > 1;
    }

}

package GameObject;

import Business.Data.GameGrid;

import java.awt.*;

public interface Movable {

    /**
     * @param delta how far to be moved
     *              should only be one step
     * @return whether can moved to which position, according to the map constraints
     */
    Boolean canMoveBy(Point delta);

    /**
     * @param delta how far to be moved
     *              should only be one step
     */
    void moveBy(Point delta) throws GameObject.Objects.IllegalMovementException;

    default Boolean moveMoreThanOneStep(Point delta) {
        return delta.distance(0, 0) >= 1;
    }
}

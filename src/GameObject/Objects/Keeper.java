package GameObject.Objects;

import Business.Data.GameGrid;
import GameObject.AbstractGameObject;
import GameObject.Movable;

import java.awt.*;

public class Keeper extends AbstractGameObject implements Movable {


    public Keeper(GameGrid linksTo, int atX, int atY) {
        super(linksTo, atX, atY);
    }

    public Keeper(GameGrid linksTo, Point at) {
        super(linksTo, at);
    }

    @Override
    public char getCharSymbol() {
        return 'S';
    }

    @Override
    public String getStringSymbol() {
        return "KEEPER";
    }

    // Movable Methods

    @Override
    public Boolean canMoveBy(Point delta) {
        if (moveMoreThanOneStep(delta)) return false;
        Point targetPosition = GameGrid.translatePoint(this.at(), delta);
        AbstractGameObject objectOnDestination = grid.getGameObjectAt(targetPosition);
        if (objectOnDestination instanceof Floor) {
            return true;
        } else if (objectOnDestination instanceof Crate) {
            return ((Crate) objectOnDestination).canMoveBy(delta);
        } else {
            return false;
        }
    }

    @Override
    public void moveBy(Point delta) throws IllegalMovementException {
        Point targetPosition = GameGrid.translatePoint(this.at(), delta);
        AbstractGameObject objectOnDestination = grid.getGameObjectAt(targetPosition);
        if (objectOnDestination instanceof Floor) {
            moveToFloor(targetPosition);
        } else if (objectOnDestination instanceof Crate) {
            ((Crate) objectOnDestination).moveBy(delta);
            moveToFloor(targetPosition);
        } else {
            throw new IllegalMovementException();
        }
    }

    private void moveToFloor(Point targetPosition) {
        grid.putGameObjectAt(new Floor(grid, at()), at());
        grid.putGameObjectAt(this, targetPosition);
        this.updatePosition(targetPosition);
    }

    public Boolean isOnDiamond(GameGrid diamondsGrid) {
        return diamondsGrid.getGameObjectAt(at()) instanceof Diamond;
    }

    private void updatePosition(Point position) {
        this.xPosition = position.x;
        this.yPosition = position.y;
    }
}

package GameObject.Objects;

import Business.Data.GameGrid;
import GameObject.AbstractGameObject;
import GameObject.Movable;

import java.awt.*;

public class Crate extends AbstractGameObject implements Movable {

    public Crate(GameGrid linksTo, int atX, int atY) {
        super(linksTo, atX, atY);
    }

    public Crate(GameGrid linksTo, Point at) {
        super(linksTo, at);
    }

    @Override
    public char getCharSymbol() {
        return 'C';
    }

    @Override
    public String getStringSymbol() {
        return "CRATE";
    }

    // Movable Methods

    @Override
    public Boolean canMoveBy(Point delta) {
        if (moveMoreThanOneStep(delta)) return false;
        Point targetPosition = GameGrid.translatePoint(this.at(), delta);
        return canMoveTo(targetPosition);
    }

    @Override
    public void moveBy(Point delta) throws IllegalMovementException {
        Point targetPosition = GameGrid.translatePoint(this.at(), delta);
        AbstractGameObject objectOnDestination = grid.getGameObjectAt(targetPosition);
        if (objectOnDestination instanceof Floor) {
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

    private Boolean canMoveTo(Point destination) {
        AbstractGameObject objectOnDestination = grid.getGameObjectAt(destination);
        return objectOnDestination instanceof Floor;
    }

    private void updatePosition(Point position) {
        this.xPosition = position.x;
        this.yPosition = position.y;
    }


}

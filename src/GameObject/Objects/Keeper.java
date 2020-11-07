package GameObject.Objects;

import Business.Data.GameGrid;
import GameObject.AbstractGameObject;

public class Keeper extends AbstractGameObject {


    Keeper(GameGrid linksTo) {
        super(linksTo);
    }

    @Override
    public char getCharSymbol() {
        return 'S';
    }

    @Override
    public String getStringSymbol() {
        return "KEEPER";
    }
}

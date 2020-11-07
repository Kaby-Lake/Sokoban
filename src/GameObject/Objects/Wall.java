package GameObject.Objects;

import Business.Data.GameGrid;
import GameObject.AbstractGameObject;

public class Wall extends AbstractGameObject {


    Wall(GameGrid linksTo) {
        super(linksTo);
    }

    @Override
    public char getCharSymbol() {
        return 'W';
    }

    @Override
    public String getStringSymbol() {
        return "WALL";
    }
}

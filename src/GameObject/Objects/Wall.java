package GameObject.Objects;

import Business.Data.GameGrid;
import GameObject.AbstractGameObject;

import java.awt.*;

public class Wall extends AbstractGameObject {


    Wall(GameGrid linksTo, int atX, int atY) {
        super(linksTo, atX, atY);
    }

    Wall(GameGrid linksTo, Point at) {
        super(linksTo, at);
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

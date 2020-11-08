package GameObject.Objects;

import Business.Data.GameGrid;
import GameObject.AbstractGameObject;

import java.awt.*;

public class Floor extends AbstractGameObject {


    Floor(GameGrid linksTo, int atX, int atY) {
        super(linksTo, atX, atY);
    }

    Floor(GameGrid linksTo, Point at) {
        super(linksTo, at);
    }

    @Override
    public char getCharSymbol() {
        return ' ';
    }

    @Override
    public String getStringSymbol() {
        return "FLOOR";
    }
}

package GameObject.Objects;

import Business.Data.GameGrid;
import GameObject.AbstractGameObject;

import java.awt.*;

public class Diamond extends AbstractGameObject {


    public Diamond(GameGrid linksTo, int atX, int atY) {
        super(linksTo, atX, atY);
    }

    public Diamond(GameGrid linksTo, Point at) {
        super(linksTo, at);
    }

    @Override
    public char getCharSymbol() {
        return 'D';
    }

    @Override
    public String getStringSymbol() {
        return "DIAMOND";
    }
}

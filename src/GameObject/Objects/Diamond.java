package GameObject.Objects;

import Business.Data.GameGrid;
import GameObject.AbstractGameObject;

public class Diamond extends AbstractGameObject {


    Diamond(GameGrid linksTo) {
        super(linksTo);
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

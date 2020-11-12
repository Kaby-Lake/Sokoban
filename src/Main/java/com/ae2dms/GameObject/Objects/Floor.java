package com.ae2dms.GameObject.Objects;

import com.ae2dms.Business.Data.GameGrid;
import com.ae2dms.GameObject.*;

import java.awt.*;

public class Floor extends AbstractGameObject {


    public Floor(GameGrid linksTo, int atX, int atY) {
        super(linksTo, atX, atY);
    }

    public Floor(GameGrid linksTo, Point at) {
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

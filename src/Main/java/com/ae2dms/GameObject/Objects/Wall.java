package com.ae2dms.GameObject.Objects;

import com.ae2dms.Business.Data.GameGrid;
import com.ae2dms.GameObject.*;
import javafx.scene.image.ImageView;

import java.awt.*;

public class Wall extends AbstractGameObject {


    public Wall(GameGrid linksTo, int atX, int atY) {
        super(linksTo, atX, atY);
    }

    public Wall(GameGrid linksTo, Point at) {
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

    @Override
    public ImageView render() {
        if (this.view == null) {
            this.view = new ImageView();
        }
        return this.view;
    }
}

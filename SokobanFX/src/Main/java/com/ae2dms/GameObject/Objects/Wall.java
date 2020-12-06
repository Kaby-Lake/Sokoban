package com.ae2dms.GameObject.Objects;

import com.ae2dms.Business.Data.Level;
import com.ae2dms.GameObject.AbstractGameObject;
import javafx.scene.image.ImageView;

public class Wall extends AbstractGameObject {


    public Wall(Level linksTo, int atX, int atY) {
        super(linksTo, atX, atY);
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

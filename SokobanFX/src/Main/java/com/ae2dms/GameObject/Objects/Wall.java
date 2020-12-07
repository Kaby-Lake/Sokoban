package com.ae2dms.GameObject.Objects;

import com.ae2dms.Business.Data.Level;
import com.ae2dms.GameObject.AbstractGameObject;
import javafx.scene.image.ImageView;

/**
 * Wall on the map, represented as empty
 */
public class Wall extends AbstractGameObject {


    /**
     * Instantiates a new Wall.
     *
     * @param linksTo the links to
     * @param atX     the at x
     * @param atY     the at y
     */
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

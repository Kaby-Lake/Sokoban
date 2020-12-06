package com.ae2dms.GameObject.Objects;

import com.ae2dms.Business.Data.Level;
import com.ae2dms.GameObject.AbstractGameObject;
import com.ae2dms.IO.ResourceFactory;
import com.ae2dms.IO.ResourceType;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.awt.*;

/**
 * The type Floor.
 */
public class Floor extends AbstractGameObject {

    /**
     * Instantiates a new Floor.
     *
     * @param linksTo the links to
     * @param atX     the at x
     * @param atY     the at y
     */
    public Floor(Level linksTo, int atX, int atY) {
        super(linksTo, atX, atY);
        grid = linksTo.floorGrid;
    }

    /**
     * Instantiates a new Floor.
     *
     * @param linksTo the links to
     * @param at      the at
     */
    public Floor(Level linksTo, Point at) {
        super(linksTo, at);
        grid = linksTo.floorGrid;
    }

    @Override
    public char getCharSymbol() {
        return ' ';
    }

    @Override
    public String getStringSymbol() {
        return "FLOOR";
    }

    /**
     * @return return the Floor image
     */
    @Override
    public ImageView render() {
        if (this.view == null) {
            this.view = new ImageView((Image)ResourceFactory.getResource("STAGE_IMAGE", ResourceType.Image));
            this.view.setFitHeight(48);
            this.view.setFitWidth(48);
        }
        return this.view;
    }
}

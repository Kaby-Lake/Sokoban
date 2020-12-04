package com.ae2dms.GameObject.Objects;

import com.ae2dms.Business.Data.GameGrid;
import com.ae2dms.Business.Data.Level;
import com.ae2dms.GameObject.AbstractGameObject;
import com.ae2dms.IO.ResourceFactory;
import com.ae2dms.IO.ResourceType;
import javafx.scene.image.*;
import javafx.scene.image.Image;

import java.awt.*;

public class Floor extends AbstractGameObject {

    public Floor(Level linksTo, int atX, int atY) {
        super(linksTo, atX, atY);
        grid = linksTo.floorGrid;
    }

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

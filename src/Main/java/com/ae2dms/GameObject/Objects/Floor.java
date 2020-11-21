package com.ae2dms.GameObject.Objects;

import com.ae2dms.Business.Data.GameGrid;
import com.ae2dms.GameObject.*;
import com.ae2dms.IO.ResourceFactory;
import javafx.scene.image.ImageView;

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

    @Override
    public ImageView render() {
        if (this.view == null) {
            this.view = new ImageView(ResourceFactory.STAGE_IMAGE);
            this.view.setFitHeight(48);
            this.view.setFitWidth(48);
        }
        return this.view;
    }

    public static ImageView staticRender() {
        ImageView floor = new ImageView(ResourceFactory.STAGE_IMAGE);
        floor.setFitHeight(48);
        floor.setFitWidth(48);
        return floor;
    }
}

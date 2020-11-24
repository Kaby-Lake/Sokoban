package com.ae2dms.GameObject.Objects;

import com.ae2dms.Business.Data.GameGrid;
import com.ae2dms.GameObject.AbstractGameObject;
import com.ae2dms.IO.ResourceFactory;
import com.ae2dms.IO.ResourceType;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

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

    @Override
    public ImageView render() {
        if (this.view == null) {
            this.view = new ImageView((Image) ResourceFactory.getResource("DIAMOND_IMAGE", ResourceType.Image));
            this.view.setFitHeight(16);
            this.view.setFitWidth(16);
            this.view.setTranslateX(16);
            this.view.setTranslateY(-10);
        }
        return this.view;
    }
}

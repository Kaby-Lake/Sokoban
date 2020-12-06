package com.ae2dms.GameObject.Objects;

import com.ae2dms.Business.Data.Level;
import com.ae2dms.GameObject.AbstractGameObject;
import com.ae2dms.IO.ResourceFactory;
import com.ae2dms.IO.ResourceType;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;


/**
 * Candy that Player can eat to gain additional draggable Floor
 */
public class Candy extends AbstractGameObject {

    /**
     * Instantiates a new Candy.
     *
     * @param linksTo the links to
     * @param atX     the at x
     * @param atY     the at y
     */
    public Candy(Level linksTo, int atX, int atY) {
        super(linksTo, atX, atY);
        this.grid = linksTo.candyGrid;
    }

    @Override
    public char getCharSymbol() {
        return 'Y';
    }

    @Override
    public String getStringSymbol() {
        return "CANDY";
    }

    @Override
    public ImageView render() {
        if (this.view == null) {
            this.view = new ImageView((Image)ResourceFactory.getResource("CANDY_IMAGE", ResourceType.Image));
            this.view.setFitHeight(16);
            this.view.setFitWidth(16);
            this.view.setTranslateX(16);
            this.view.setTranslateY(-10);
        }
        return this.view;
    }

    /**
     * remove this Candy on grid
     */
    public void eat() {
        this.grid.putGameObjectAt(null, this.xPosition, this.yPosition);
    }
}

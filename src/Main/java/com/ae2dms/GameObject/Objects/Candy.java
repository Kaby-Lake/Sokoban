package com.ae2dms.GameObject.Objects;

import com.ae2dms.Business.Data.GameGrid;
import com.ae2dms.GameObject.AbstractGameObject;
import com.ae2dms.IO.ResourceFactory;
import com.ae2dms.IO.ResourceType;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

/**
 * @Description: $
 * @Param: $
 * @return: $
 * @Author: Zichen XU
 */
public class Candy extends AbstractGameObject {

    GameGrid candyGrid;

    public Candy(GameGrid linksTo, int atX, int atY, GameGrid candyGrid) {
        super(linksTo, atX, atY);
        this.candyGrid = candyGrid;
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

    public void eat() {
        this.view.setVisible(false);
        this.candyGrid.putGameObjectAt(null, this.xPosition, this.yPosition);
    }
}

package com.ae2dms.GameObject.Objects;

import com.ae2dms.Business.Data.GameGrid;
import com.ae2dms.Business.Data.Level;
import com.ae2dms.GameObject.AbstractGameObject;
import com.ae2dms.IO.ResourceFactory;
import com.ae2dms.IO.ResourceType;
import com.ae2dms.UI.Menu.ColourPreferenceController;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class Diamond extends AbstractGameObject {

    private Image DIAMOND_IMAGE = (Image)ResourceFactory.getResource("DIAMOND_IMAGE_Red", ResourceType.Image);


    public Diamond(Level linksTo, int atX, int atY) {
        super(linksTo, atX, atY);
        ColourPreferenceController.selectedDiamondColour.addListener((observable, oldValue, newValue) -> {
            if (observable != null) {
                DIAMOND_IMAGE = (Image)ResourceFactory.getResource("DIAMOND_IMAGE_" + newValue, ResourceType.Image);
            }
        });
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
            this.view = new ImageView(DIAMOND_IMAGE);
            this.view.setFitHeight(16);
            this.view.setFitWidth(16);
            this.view.setTranslateX(16);
            this.view.setTranslateY(-10);
        }
        return this.view;
    }
}

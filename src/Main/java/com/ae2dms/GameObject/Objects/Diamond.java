package com.ae2dms.GameObject.Objects;

import com.ae2dms.Business.Data.Level;
import com.ae2dms.GameObject.AbstractGameObject;
import com.ae2dms.IO.ResourceFactory;
import com.ae2dms.IO.ResourceType;
import com.ae2dms.UI.Menu.ColourPreferenceController;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class Diamond extends AbstractGameObject {

    private transient Image DIAMOND_IMAGE;

    public Diamond(Level linksTo, int atX, int atY) {
        super(linksTo, atX, atY);
    }

    @Override
    public char getCharSymbol() {
        return 'D';
    }

    @Override
    public String getStringSymbol() {
        return "DIAMOND";
    }

    private Image getDiamondImage() {
        if (DIAMOND_IMAGE == null) {
            DIAMOND_IMAGE = (Image)ResourceFactory.getResource("DIAMOND_IMAGE_" + ColourPreferenceController.selectedDiamondColour.getValue(), ResourceType.Image);
        }
        return DIAMOND_IMAGE;
    }



    @Override
    public ImageView render() {
        if (this.view == null) {
            this.view = new ImageView(getDiamondImage());
            this.view.setFitHeight(16);
            this.view.setFitWidth(16);
            this.view.setTranslateX(16);
            this.view.setTranslateY(-10);
        }
        return this.view;
    }
}

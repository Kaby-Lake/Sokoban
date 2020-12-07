package com.ae2dms.GameObject.Objects;

import com.ae2dms.Business.Data.Level;
import com.ae2dms.GameObject.AbstractGameObject;
import com.ae2dms.IO.ResourceFactory;
import com.ae2dms.IO.ResourceType;
import com.ae2dms.UI.Menu.ColourPreferenceController;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

/**
 * Diamond, which represents the position that player have to move Crate to
 */
public class Diamond extends AbstractGameObject {

    private transient Image DIAMOND_IMAGE;

    /**
     * Instantiates a new Diamond.
     *
     * @param linksTo the links to
     * @param atX     the at x
     * @param atY     the at y
     */
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


    /**
     * @return the Diamond image with selected colour
     */
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

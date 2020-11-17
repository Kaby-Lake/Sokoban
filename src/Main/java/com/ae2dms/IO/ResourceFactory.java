package com.ae2dms.IO;

import com.ae2dms.UI.Menu.MenuView;
import javafx.scene.image.Image;
import javafx.scene.media.Media;

import java.net.URISyntaxException;

public class ResourceFactory {

    public static Image STAGE_IMAGE = null;

    static {
        try {
            STAGE_IMAGE = new Image(ResourceFactory.class.getResource("/ui/Assets/Game/Stage.png").toURI().toString());
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
    }


    public ResourceFactory() throws Exception {
    }
}

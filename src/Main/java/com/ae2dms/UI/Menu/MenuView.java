package com.ae2dms.UI.Menu;

import javafx.fxml.FXMLLoader;
import javafx.scene.layout.*;

import java.io.IOException;

public class MenuView {

    private static Pane view;

    public static Pane getInstance() throws Exception {
        if(view == null) {
            FXMLLoader menuLoader = new FXMLLoader(MenuView.class.getResource("/ui/FXML/MenuViewScene.fxml"));
            view = menuLoader.load();
            return view;
        }
        return view;
    }
}

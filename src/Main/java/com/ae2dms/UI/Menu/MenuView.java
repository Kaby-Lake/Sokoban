package com.ae2dms.UI.Menu;

import com.ae2dms.UI.GameMediaPlayer;
import com.ae2dms.UI.ViewUtilities;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.Pane;

import java.io.IOException;


public class MenuView {

    private static volatile MenuView instance;
    private Pane view;

    private MenuView() {

        GameMediaPlayer.getInstance().play();

        try {
            FXMLLoader menuLoader = new FXMLLoader(MenuView.class.getResource("/ui/FXML/MenuViewScene.fxml"));
            view = menuLoader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static synchronized MenuView getInstance()
    {
        if(instance == null) {
            instance = new MenuView();
        }
        return instance;
    }

    public Pane getView() {
        ViewUtilities.loadViewWithEffect(view);
        return view;
    }

}

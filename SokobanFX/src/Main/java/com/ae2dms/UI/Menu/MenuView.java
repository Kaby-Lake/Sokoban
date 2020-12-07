package com.ae2dms.UI.Menu;

import com.ae2dms.UI.GameMediaPlayer;
import com.ae2dms.UI.ViewUtilities;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.Pane;

import java.io.IOException;

import static com.ae2dms.Business.GameDocument.logger;


/**
 * The wrapper for MenuView which acts like a View in MVC
 * contains utilities for view such as key binding as well as view effects
 */
public class MenuView {

    /**
     * The singleton private instance of MenuView
     */
    private static MenuView instance;

    /**
     * The MenuView view
     */
    private Pane view;

    /**
     * private constructor for MenuView will only be used with getInstance()
     */
    private MenuView() {

        GameMediaPlayer.getInstance().play();

        try {
            FXMLLoader menuLoader = new FXMLLoader(MenuView.class.getResource("/ui/FXML/MenuViewScene.fxml"));
            view = menuLoader.load();
        } catch (IOException e) {
            logger.severe(e.getMessage());
        }
    }

    /**
     * get the singleton instance of MenuView, if not initialized yet, wil call private constructor
     *
     * @return the only MenuView instance
     */
    public static synchronized MenuView getInstance()
    {
        if(instance == null) {
            instance = new MenuView();
        }
        return instance;
    }

    /**
     * get the JavaFX view Pane for MenuView
     *
     * @return the Pane
     */
    public Pane getView() {
        ViewUtilities.loadViewWithEffect(view);
        return view;
    }

}

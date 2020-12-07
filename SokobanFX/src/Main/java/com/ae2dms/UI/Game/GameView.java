package com.ae2dms.UI.Game;

import com.ae2dms.Business.GameDebugger;
import com.ae2dms.UI.ViewUtilities;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;

/**
 * The wrapper for GameView which acts like a View in MVC
 * contains utilities for view such as key binding as well as view effects
 */
public class GameView {

    /**
     * The JavaFX Pane of GameView loaded from FXML
     */
    private Pane gameView;

    /**
     * the controller of this GameView
     */
    private GameViewController controller;

    /**
     * get a FXML from file and load the view to gameView, as well as the controllers
     */
    public GameView() {
        try {
            FXMLLoader gameLoader = new FXMLLoader(GameView.class.getResource("/ui/FXML/GameViewScene.fxml"));
            gameView = gameLoader.load();
            controller = gameLoader.getController();
        } catch (Exception e) {
            GameDebugger.logErrorMessage(e.getMessage());
        }

    }

    /**
     * used for get the view Pane from GameView
     *
     * @return view with loading effect
     */
    public Pane getGameView() {
        ViewUtilities.loadViewWithEffect(gameView);
        return gameView;
    }

    /**
     * bind the key listener with controller
     *
     * @param scene the scene to be added to PrimaryState
     */
    public void bindKey(Scene scene) {
        scene.setOnKeyPressed(keyEvent ->  {
            controller.handleKey(keyEvent);
        });
    }
}

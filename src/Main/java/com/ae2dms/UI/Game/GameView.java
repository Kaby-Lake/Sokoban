package com.ae2dms.UI.Game;

import com.ae2dms.Business.GameDebugger;
import com.ae2dms.UI.ViewUtilities;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;

public class GameView {

    private Pane gameView;
    private BorderPane levelCompleteView;
    private BorderPane gameCompleteView;
    private BorderPane exitView;
    private GameViewController controller;

    public GameView() {

        try {
            FXMLLoader levelCompleteViewLoader = new FXMLLoader(GameView.class.getResource("/ui/FXML/LevelCompletePopUp.fxml"));
            levelCompleteView = levelCompleteViewLoader.load();

            FXMLLoader gameCompleteViewLoader = new FXMLLoader(GameView.class.getResource("/ui/FXML/GameCompletePopUp.fxml"));
            gameCompleteView = gameCompleteViewLoader.load();

            FXMLLoader exitViewLoader = new FXMLLoader(GameView.class.getResource("/ui/FXML/ExitPopUp.fxml"));
            exitView = exitViewLoader.load();

            FXMLLoader gameLoader = new FXMLLoader(GameView.class.getResource("/ui/FXML/GameViewScene.fxml"));
            gameView = gameLoader.load();
            controller = gameLoader.getController();
            controller.bindLevelGameCompleteController(levelCompleteViewLoader.getController(), gameCompleteViewLoader.getController(), exitViewLoader.getController());
            controller.addLevelGameCompleteView(levelCompleteView, gameCompleteView, exitView);
        } catch (Exception e) {
            GameDebugger.logErrorMessage(e.getMessage());
        }

    }

    public Pane getGameView() {
        ViewUtilities.loadViewWithEffect(gameView);
        return gameView;
    }

    public void bindKey(Scene scene) {
        scene.setOnKeyPressed(keyEvent ->  {
            controller.handleKey(keyEvent);
        });
    }
}

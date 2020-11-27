package com.ae2dms.UI.Game;

import com.ae2dms.IO.ResourceFactory;
import com.ae2dms.UI.ViewUtilities;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.media.MediaPlayer;

public class GameView {

    private Pane gameView;
    private BorderPane levelCompleteView;
    private BorderPane gameCompleteView;
    private BorderPane exitView;
    private GameViewController controller;

    public GameView() throws Exception {

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
        controller.bindLevelGameCompleteView(this.getLevelCompleteView(), this.getGameCompleteView(), this.getExitView());

    }

    public Pane getGameView() {
        ViewUtilities.loadViewWithEffect(gameView);
        return gameView;
    }

    public void bind(Scene scene) {
        scene.setOnKeyPressed(keyEvent ->  {
            controller.handleKey(keyEvent);
        });
    }

    public BorderPane getLevelCompleteView() {
        return levelCompleteView;
    }

    public BorderPane getGameCompleteView() {
        return gameCompleteView;
    }

    public BorderPane getExitView() {
        return exitView;
    }
}

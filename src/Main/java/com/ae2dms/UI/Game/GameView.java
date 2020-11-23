package com.ae2dms.UI.Game;

import com.ae2dms.IO.ResourceFactory;
import com.ae2dms.UI.ViewUtilities;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.scene.media.MediaPlayer;

public class GameView {

    private Pane view;
    public static MediaPlayer backgroundMusicPlayer;
    private GameViewController controller;

    public GameView() throws Exception {

        backgroundMusicPlayer = new MediaPlayer(ResourceFactory.randomBackgroundMusic());
        backgroundMusicPlayer.setOnEndOfMedia(() -> {
            backgroundMusicPlayer = new MediaPlayer(ResourceFactory.randomBackgroundMusic());
            backgroundMusicPlayer.play();
        });
        backgroundMusicPlayer.play();

        FXMLLoader gameLoader = new FXMLLoader(GameView.class.getResource("/ui/FXML/GameViewScene.fxml"));
        view = gameLoader.load();
        controller = gameLoader.getController();
    }

    public Pane getView() {
        ViewUtilities.loadViewWithEffect(view);
        return view;
    }

    public void bind(Scene scene) {
        scene.setOnKeyPressed(keyEvent ->  {
            controller.handleKey(keyEvent);
        });
    }
}

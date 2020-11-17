package com.ae2dms.UI.Game;

import com.ae2dms.Business.GameDocument;
import com.ae2dms.IO.ResourceFactory;
import com.ae2dms.UI.ViewUtilities;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.util.Duration;

import java.io.IOException;
import java.net.URISyntaxException;

public class GameView {

    private Pane view;
    public static MediaPlayer backgroundMusicPlayer;
    private GameViewController controller;

    public GameView() throws Exception {
        Media backgroundMusic = ResourceFactory.GAME_BACKGROUND_MUSIC;
        backgroundMusicPlayer = new MediaPlayer(backgroundMusic);
        backgroundMusicPlayer.setOnEndOfMedia(() -> backgroundMusicPlayer.seek(Duration.ZERO));
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
            controller.handleKey(keyEvent.getCode());
        });
    }
}

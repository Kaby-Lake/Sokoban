package com.ae2dms.UI.Game;

import com.ae2dms.Business.GameDocument;
import com.ae2dms.UI.ViewUtilities;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.Pane;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.util.Duration;

import java.io.IOException;
import java.net.URISyntaxException;

public class GameView {

    private Pane view;
    public static MediaPlayer backgroundMusicPlayer;

    public GameView() throws Exception {
        Media backgroundMusic = new Media(GameView.class.getResource("/music/PETO-Okay.mp3").toURI().toString());

        backgroundMusicPlayer = new MediaPlayer(backgroundMusic);
        backgroundMusicPlayer.setOnEndOfMedia(() -> backgroundMusicPlayer.seek(Duration.ZERO));
        backgroundMusicPlayer.play();

        FXMLLoader gameLoader = new FXMLLoader(GameView.class.getResource("/ui/FXML/GameViewScene.fxml"));
        view = gameLoader.load();
    }

    public Pane getView() {
        ViewUtilities.loadViewWithEffect(view);
        return view;
    }
}

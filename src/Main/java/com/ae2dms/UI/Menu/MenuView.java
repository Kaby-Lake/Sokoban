package com.ae2dms.UI.Menu;

import javafx.fxml.FXMLLoader;
import javafx.scene.layout.Pane;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.util.Duration;


public class MenuView {

    private static Pane view;
    public static MediaPlayer backgroundMusicPlayer;

    public static Pane getInstance() throws Exception {
        if(view == null) {
            Media backgroundMusic = new Media(MenuView.class.getResource("/music/PaperClip-Jumping.mp3").toURI().toString());
            backgroundMusicPlayer = new MediaPlayer(backgroundMusic);
            backgroundMusicPlayer.setOnEndOfMedia(() -> backgroundMusicPlayer.seek(Duration.ZERO));
            backgroundMusicPlayer.play();

            FXMLLoader menuLoader = new FXMLLoader(MenuView.class.getResource("/ui/FXML/MenuViewScene.fxml"));
            view = menuLoader.load();
            return view;
        }
        return view;
    }
}

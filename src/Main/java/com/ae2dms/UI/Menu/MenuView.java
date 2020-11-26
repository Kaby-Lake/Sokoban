package com.ae2dms.UI.Menu;

import com.ae2dms.IO.ResourceFactory;
import com.ae2dms.UI.MediaState;
import com.ae2dms.UI.ViewUtilities;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.Pane;
import javafx.scene.media.MediaPlayer;

import java.io.IOException;


public class MenuView {

    private static volatile MenuView instance;
    private MediaPlayer backgroundMusicPlayer;
    private Pane view;

    private MenuView() {
        backgroundMusicPlayer = new MediaPlayer(ResourceFactory.getRandomBackgroundMusic());
        backgroundMusicPlayer.setOnEndOfMedia(() -> {
            backgroundMusicPlayer = new MediaPlayer(ResourceFactory.getRandomBackgroundMusic());
            backgroundMusicPlayer.play();
        });
        backgroundMusicPlayer.play();

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

    public void setMusic(MediaState state) {
        switch (state) {
            case MUTE -> backgroundMusicPlayer.setMute(true);
            case NON_MUTE -> backgroundMusicPlayer.setMute(false);
            case PLAY -> backgroundMusicPlayer.play();
            case PAUSE -> backgroundMusicPlayer.pause();
            case STOP -> {
                backgroundMusicPlayer.stop();
                backgroundMusicPlayer = new MediaPlayer(ResourceFactory.getRandomBackgroundMusic());
            }
        }
    }
}

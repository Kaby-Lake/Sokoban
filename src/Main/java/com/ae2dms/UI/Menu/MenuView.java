package com.ae2dms.UI.Menu;

import com.ae2dms.IO.ResourceFactory;
import com.ae2dms.UI.ViewUtilities;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.Pane;
import javafx.scene.media.MediaPlayer;


public class MenuView {

    private static Pane view;
    public static MediaPlayer backgroundMusicPlayer;

    public static Pane getInstance() throws Exception {
        if(view == null) {
            backgroundMusicPlayer = new MediaPlayer(ResourceFactory.randomBackgroundMusic());
            backgroundMusicPlayer.setOnEndOfMedia(() -> {
                backgroundMusicPlayer = new MediaPlayer(ResourceFactory.randomBackgroundMusic());
                backgroundMusicPlayer.play();
            });
            backgroundMusicPlayer.play();

            FXMLLoader menuLoader = new FXMLLoader(MenuView.class.getResource("/ui/FXML/MenuViewScene.fxml"));
            view = menuLoader.load();

            ViewUtilities.loadViewWithEffect(view);

            return view;
        }
        return view;
    }
}

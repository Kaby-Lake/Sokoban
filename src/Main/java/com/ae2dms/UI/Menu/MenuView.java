package com.ae2dms.UI.Menu;

import com.ae2dms.Business.GameDocument;
import com.ae2dms.UI.ViewUtilities;
import javafx.animation.ScaleTransition;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.Pane;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.Node;
import javafx.stage.Stage;
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

            ViewUtilities.loadViewWithEffect(view);

            return view;
        }
        return view;
    }
}

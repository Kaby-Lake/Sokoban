package com.ae2dms.UI.Menu;

import com.ae2dms.Business.GameDocument;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.fxml.FXML;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseEvent;

import java.awt.*;
import java.io.InputStream;

public class MenuViewController {
    private GameDocument gameDocument;

    private final BooleanProperty debugSwitchToggle;
    @FXML
    private ImageView debugSwitch;

    private final BooleanProperty musicSwitchToggle;
    @FXML
    private ImageView musicSwitch;

    public MenuViewController() {
        this.musicSwitchToggle = new SimpleBooleanProperty(true);
        this.debugSwitchToggle = new SimpleBooleanProperty(false);
    }

    public void initialize() throws IllegalStateException {
        // ensure model is only set once:
        if (this.gameDocument != null) {
            throw new IllegalStateException("Model can only be initialized once");
        }
        InputStream in = getClass().getClassLoader().getResourceAsStream("level/SampleGame.skb");
        this.gameDocument = new GameDocument(in, false);

        debugSwitchToggle.addListener((observable, oldValue, newValue) -> {
            if (observable != null && observable.getValue()==true) {
                debugSwitch.setImage(new Image(String.valueOf(getClass().getResource("/ui/Assets/BottomBar/Debug_on.png"))));
            } else if (observable != null && observable.getValue()==false){
                debugSwitch.setImage(new Image(String.valueOf(getClass().getResource("/ui/Assets/BottomBar/Debug_off.png"))));
            }
        });

        musicSwitchToggle.addListener((observable, oldValue, newValue) -> {
            if (observable != null && observable.getValue()==true) {
                musicSwitch.setImage(new Image(String.valueOf(getClass().getResource("/ui/Assets/BottomBar/Music_on.png"))));
            } else if (observable != null && observable.getValue()==false){
                musicSwitch.setImage(new Image(String.valueOf(getClass().getResource("/ui/Assets/BottomBar/Music_off.png"))));
            }
        });
    }

    public void handleKey(KeyCode code) {
//        switch (code) {
//            case UP:
//                gameDocument.move(new Point(-1, 0));
//                break;
//
//            case RIGHT:
//                gameDocument.move(new Point(0, 1));
//                break;
//
//            case DOWN:
//                gameDocument.move(new Point(1, 0));
//                break;
//
//            case LEFT:
//                gameDocument.move(new Point(0, -1));
//                break;
//
//            default:
//                // TODO: implement something funny.
//        }
//        updateView();
//
//        if (GameDocument.isDebugActive()) {
//            System.out.println(code);
//        }
    }

    public void clickToggleDebug() {
        debugSwitchToggle.setValue(!debugSwitchToggle.getValue());
        gameDocument.toggleDebug(debugSwitchToggle.getValue());
    }

    //TODO:
    public void clickStartGame(MouseEvent mouseEvent) {
    }

    public void clickToggleMusic(MouseEvent mouseEvent) {
        musicSwitchToggle.setValue(!musicSwitchToggle.getValue());
        gameDocument.toggleMusic(musicSwitchToggle.getValue());
    }

    //TODO:
    public void clickUndo(MouseEvent mouseEvent) {
    }

    //TODO:
    public void clickInformation(MouseEvent mouseEvent) {
    }
}

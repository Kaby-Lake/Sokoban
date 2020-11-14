package com.ae2dms.UI.Menu;

import com.ae2dms.Business.GameDocument;
import com.ae2dms.UI.AbstractBarController;
import javafx.fxml.FXML;
import javafx.scene.Group;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseEvent;
import javafx.stage.FileChooser;

import java.io.File;
import java.io.InputStream;

public class MenuViewController extends AbstractBarController {

    private GameDocument gameDocument;

    @FXML
    private Group InfoGroup;

    @FXML
    private ImageView loadGameFileButton;

    public MenuViewController() {

    }

    public void initialize() throws IllegalStateException {
        // ensure model is only set once:
        if (this.gameDocument != null) {
            throw new IllegalStateException("Model can only be initialized once");
        }
        InputStream in = getClass().getClassLoader().getResourceAsStream("level/SampleGame.skb");
        this.gameDocument = new GameDocument(in, false);

        super.disableButton("Debug");
        super.disableButton("Save Game");
        super.disableButton("Undo");

        musicSwitchToggle.addListener((observable, oldValue, newValue) -> {
            if (observable != null ) {
                MenuView.backgroundMusicPlayer.setMute(!observable.getValue());
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
//        gameDocument.toggleDebug(menuBarClickToggleDebug());
    }

    public void clickStartGame(MouseEvent mouseEvent) {
        //TODO:
    }

    public void clickToggleMusic(MouseEvent mouseEvent) {
        menuBarClickToggleMusic();
    }

    public void clickUndo(MouseEvent mouseEvent) {
//        gameDocument.undo();
    }

    public void clickInformation(MouseEvent mouseEvent) {
        InfoGroup.getStyleClass().clear();
    }

    public void clickCloseInformation(MouseEvent mouseEvent) {
        InfoGroup.getStyleClass().clear();
        InfoGroup.getStyleClass().add("Hide");
    }

    public void clickSaveGame(MouseEvent mouseEvent) {
    }

    public void clickLoadGame(MouseEvent mouseEvent) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Open Resource File");
        File file = fileChooser.showOpenDialog(loadGameFileButton.getScene().getWindow());
        if (file != null) {
            System.out.println(file.getAbsolutePath());
            // TODO:
        }
    }

    public void clickExit(MouseEvent mouseEvent) {
        System.exit(0);
    }
}

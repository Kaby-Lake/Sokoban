package com.ae2dms.UI.Menu;

import com.ae2dms.Business.GameDocument;
import com.ae2dms.Main.Main;
import com.ae2dms.UI.AbstractBarController;
import com.ae2dms.UI.Game.GameView;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.fxml.FXML;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.stage.FileChooser;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

public class MenuViewController extends AbstractBarController {

    private GameDocument gameDocument = Main.gameDocument;

    @FXML
    private Group InfoGroup;

    private IntegerProperty highestScoreValue;

    @FXML
    private ImageView loadGameFileButton;

    public MenuViewController() {
        this.highestScoreValue = new SimpleIntegerProperty();
    }

    public void initialize() throws IllegalStateException {

        super.disableButton("Debug");
        super.disableButton("Save Game");
        super.disableButton("Undo");

        musicSwitchToggle.addListener((observable, oldValue, newValue) -> {
            if (observable != null ) {
                MenuView.backgroundMusicPlayer.setMute(!observable.getValue());
            }
        });

        this.highestScoreValue.setValue(gameDocument.highestScore);
        this.bindHighestScore(highestScoreValue);

        setKeyboardHandler();

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

    public void clickStartGame(MouseEvent mouseEvent) throws Exception {
        MenuView.backgroundMusicPlayer.stop();

        GameView gameView = new GameView();
        Scene gameViewScene = new Scene(gameView.getView());
        gameView.bind(gameViewScene);

        Main.primaryStage.setScene(gameViewScene);

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
        fileChooser.setTitle("Open Game Save File");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Sokoban save file", "*.skbsave"));
        File file = fileChooser.showOpenDialog(loadGameFileButton.getScene().getWindow());
        if (file != null) {
            System.out.println(file.getAbsolutePath());
            // TODO:
        }
    }

    public void clickLoadMapFile(MouseEvent mouseEvent) throws FileNotFoundException {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Open Game Map File");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Sokoban Map file", "*.skb"));
        File file = fileChooser.showOpenDialog(loadGameFileButton.getScene().getWindow());
        if (file != null) {
            // System.out.println(file.getAbsolutePath());
            if (GameDocument.isDebugActive()) {
                GameDocument.logger.info("Loading save file: " + file.getName());
            }
            gameDocument.reloadMapFromFile(new FileInputStream(file));
        }
    }

    public void clickExit(MouseEvent mouseEvent) {
        System.exit(0);
    }

    private void setKeyboardHandler() {
        Main.primaryStage.addEventFilter(KeyEvent.KEY_PRESSED, event -> {
            this.handleKey(event.getCode());
        });
    }
}

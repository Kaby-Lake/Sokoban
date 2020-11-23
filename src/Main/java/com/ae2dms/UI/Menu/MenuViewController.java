package com.ae2dms.UI.Menu;

import com.ae2dms.Business.GameDocument;
import com.ae2dms.Business.GameStageSaver;
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
import javafx.stage.FileChooser;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

public class MenuViewController extends AbstractBarController {

    private GameDocument gameDocument = Main.gameDocument;

    @FXML
    private Group infoGroup;

    @FXML
    private ImageView loadGameFileButton;

    public void initialize() throws IllegalStateException {

        super.disableButton("Debug");
        super.disableButton("Save Game");
        super.disableButton("Undo");

        musicIsMute.addListener((observable, oldValue, newValue) -> {
            if (observable != null ) {
                MenuView.backgroundMusicPlayer.setMute(observable.getValue());
            }
        });

        this.highestScore.textProperty().bind(Main.gameDocument.highestScore.asString());
    }


    public void clickStartGame(MouseEvent mouseEvent) throws Exception {
        MenuView.backgroundMusicPlayer.stop();

        this.gameDocument.restoreObject(GameStageSaver.getInitialState());
        GameView gameView = new GameView();
        Scene gameViewScene = new Scene(gameView.getView());
        gameView.bind(gameViewScene);

        Main.primaryStage.setScene(gameViewScene);

    }

    public void clickToggleMusic(MouseEvent mouseEvent) {
        menuBarClickToggleMusic();
    }

    public void clickInformation(MouseEvent mouseEvent) {
        infoGroup.getStyleClass().clear();
    }

    public void clickCloseInformation(MouseEvent mouseEvent) {
        infoGroup.getStyleClass().clear();
        infoGroup.getStyleClass().add("Hide");
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




    // Not used in MenuViewController

    public void clickToggleDebug() {
    }
    public void clickUndo(MouseEvent mouseEvent) {
    }
    public void clickSaveGame(MouseEvent mouseEvent) {
    }



}

package com.ae2dms.UI.Menu;

import com.ae2dms.Business.GameDebugger;
import com.ae2dms.Business.GameDocument;
import com.ae2dms.Business.GameStageSaver;
import com.ae2dms.Main.Main;
import com.ae2dms.UI.AbstractBarController;
import com.ae2dms.UI.Game.GameView;
import com.ae2dms.UI.Game.GameViewController;
import com.ae2dms.UI.HighScoreBar.HighScoreBarController;
import com.ae2dms.UI.MediaState;
import javafx.fxml.FXML;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.stage.FileChooser;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

public class MenuViewController extends AbstractBarController {

    private GameDocument gameDocument = Main.gameDocument;

    @FXML
    private Group infoGroup;

    private HighScoreBarController highScoreBarController;

    public void initialize() throws IllegalStateException {

        super.disableButton("Debug");
        super.disableButton("Save Game");
        super.disableButton("Undo");

        musicIsMute.addListener((observable, oldValue, newValue) -> {
            if (observable != null ) {
                if (observable.getValue() == true) {
                    MenuView.getInstance().setMusic(MediaState.MUTE);
                } else {
                    MenuView.getInstance().setMusic(MediaState.NON_MUTE);
                }
            }
        });

        this.highestScore.textProperty().bind(Main.gameDocument.highestScore.asString());
        highScoreBarController = this.loadBottomBar();
    }


    public void clickStartGame(MouseEvent mouseEvent) {
        MenuView.getInstance().setMusic(MediaState.STOP);

        this.gameDocument.restoreObject(GameStageSaver.getInitialState());
        GameView gameView = new GameView();
        Scene gameViewScene = new Scene(gameView.getGameView());
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
        File file = fileChooser.showOpenDialog(Main.primaryStage.getScene().getWindow());
        if (file != null) {
            System.out.println(file.getAbsolutePath());
            // TODO:
        }
    }

    public void clickLoadMapFile(MouseEvent mouseEvent) throws FileNotFoundException {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Open Game Map File");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Sokoban Map file", "*.skb"));
        File file = fileChooser.showOpenDialog(Main.primaryStage.getScene().getWindow());
        if (file != null) {
            gameDocument.reloadMapFromFile(new FileInputStream(file));
            GameDebugger.logLoadMapFile(file);
            clickStartGame(null);
        }
    }

    public void clickExit(MouseEvent mouseEvent) {
        System.exit(0);
    }

    public void clickHighScoreList() {
        menuBarClickToggleHighScoreList();
    }




    // Not used in MenuViewController

    public void clickToggleDebug() {
    }
    public void clickUndo(MouseEvent mouseEvent) {
    }
    public void clickSaveGame(MouseEvent mouseEvent) {
    }



}

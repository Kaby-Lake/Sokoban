package com.ae2dms.UI.Menu;

import com.ae2dms.Business.GameDocument;
import com.ae2dms.Business.GameStageSaver;
import com.ae2dms.IO.MapFileLoader;
import com.ae2dms.Main.Main;
import com.ae2dms.UI.AbstractBarController;
import com.ae2dms.UI.Game.GameView;
import javafx.fxml.FXML;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.stage.FileChooser;
import javafx.util.StringConverter;
import javafx.util.converter.NumberStringConverter;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

/**
 * JavaFX controller for MenuView
 */
public class MenuViewController extends AbstractBarController {

    /**
     * static GameDocument
     */
    private GameDocument gameDocument = Main.gameDocument;

    /**
     * The view to show information
     */
    @FXML
    private Group infoGroup;

    /**
     * will be called first by JavaFX
     * disables the button not relevant to MenuView and bind the bestRecord with gameDocument
     */
    public void initialize() {
        super.disableButton("Debug");
        super.disableButton("Save Game");
        super.disableButton("Undo");

        StringConverter<Number> converter = new NumberStringConverter();
        this.bestRecord.textProperty().bindBidirectional(Main.gameDocument.bestRecord, converter);

    }


    /**
     * button to click Start Game
     * will restore the GameStatus to initial and set the scene
     *
     */
    public void clickStartGame() {

        this.gameDocument.restoreObject(GameStageSaver.getInitialState());
        GameView gameView = new GameView();
        Scene gameViewScene = new Scene(gameView.getGameView());
        gameView.bindKey(gameViewScene);

        Main.primaryStage.setScene(gameViewScene);

    }

    /**
     * Click the game information panel
     */
    public void clickInformation() {
        infoGroup.getStyleClass().clear();
    }

    public void clickCloseInformation() {
        infoGroup.getStyleClass().clear();
        infoGroup.getStyleClass().add("Hide");
    }

    // TODO: add information

    /**
     * click restore game from file button
     * will read a file from user select and restored to GameDocument
     * will log status of success or failed
     */
    public void clickLoadGame() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Open Game Save File");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Sokoban save file", "*.skbsave"));
        File file = fileChooser.showOpenDialog(Main.primaryStage.getScene().getWindow());
        if (file != null) {
            GameDocument.logger.info("reading file from" + file.getAbsolutePath());
            try {
                gameDocument.reloadStateFromFile(new FileInputStream(file));
                GameDocument.logger.info("restored state from" + file.getAbsolutePath());
            } catch (FileNotFoundException e) {
                GameDocument.logger.warning(e.getMessage());
            } catch (MapFileLoader.ErrorSaveFileLoadException | IllegalArgumentException e) {
                GameDocument.logger.warning("Not a valid sokoban save file");
                return;
            }
            clickStartGame();
        } else {
            GameDocument.logger.warning("file not chosen");
        }
    }

    /**
     * click Load Map file button
     * will read a file from user select and restored the map to GameDocument
     * will log status of success or failed
     */
    public void clickLoadMapFile() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Open Game Map File");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Sokoban Map file", "*.skb"));
        File file = fileChooser.showOpenDialog(Main.primaryStage.getScene().getWindow());
        if (file != null) {
            GameDocument.logger.info("reading file from" + file.getAbsolutePath());
            try {
                gameDocument.reloadMapFromFile(new FileInputStream(file));
                GameDocument.logger.info("loaded map from" + file.getAbsolutePath());
            } catch (FileNotFoundException e) {
                GameDocument.logger.warning(e.getMessage());
            } catch (MapFileLoader.ErrorMapFileLoadException e) {
                return;
            }
            clickStartGame();
        } else {
            GameDocument.logger.warning("file not chosen");
        }
    }

    /**
     * click Exit on the screen
     */
    public void clickExit() {
        System.exit(0);
    }

    /**
     * click Colour Preference button on screen
     */
    @FXML
    private void toggleColourPreferences() {
        ColourPreferenceController.show();
    }



}

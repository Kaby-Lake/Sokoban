package com.ae2dms.UI.Game;

import com.ae2dms.Business.GameDocument;
import com.ae2dms.GameObject.AbstractGameObject;
import com.ae2dms.GameObject.Objects.Wall;
import com.ae2dms.IO.ResourceFactory;
import com.ae2dms.Main.Main;
import com.ae2dms.UI.AbstractBarController;
import com.ae2dms.UI.Menu.MenuView;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.fxml.FXML;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.stage.FileChooser;

import java.io.File;
import java.io.InputStream;

public class GameViewController extends AbstractBarController {

    private GameDocument gameDocument = Main.gameDocument;

    @FXML
    private Group InfoGroup;

    @FXML
    private GridPane stageGrid;

    @FXML
    private GridPane itemGrid;

    @FXML
    private GridPane diamondsGrid;

    private final IntegerProperty highestScoreValue;

    @FXML
    private ImageView loadGameFileButton;

    public GameViewController() {
        this.highestScoreValue = new SimpleIntegerProperty();
    }

    public void initialize() throws IllegalStateException, ClassNotFoundException {

        super.disableButton("High score");

        musicSwitchToggle.addListener((observable, oldValue, newValue) -> {
            if (observable != null ) {
                MenuView.backgroundMusicPlayer.setMute(!observable.getValue());
            }
        });

        this.highestScoreValue.setValue(gameDocument.highestScore);
        this.bindHighestScore(highestScoreValue);

        this.renderMap();

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

    public void clickToMenu(MouseEvent mouseEvent) throws Exception {
        GameView.backgroundMusicPlayer.stop();
        Main.primaryStage.setScene(Main.menuScene);
        MenuView.backgroundMusicPlayer.play();
    }

    private void renderMap() throws ClassNotFoundException {
        Image stageImage = ResourceFactory.STAGE_IMAGE;
        for (AbstractGameObject object : gameDocument.getCurrentLevel().objectsGrid) {
            if (!(object instanceof Wall)) {
                ImageView stageImageView = new ImageView(stageImage);
                stageImageView.setFitHeight(48);
                stageImageView.setFitWidth(48);
                stageGrid.add(stageImageView, object.at().y, object.at().x);
            }
        }
    }
}

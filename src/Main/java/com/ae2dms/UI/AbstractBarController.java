package com.ae2dms.UI;

import com.ae2dms.Business.Data.GameRecord;
import com.ae2dms.Business.GameDebugger;
import com.ae2dms.IO.ResourceFactory;
import com.ae2dms.IO.ResourceType;
import com.ae2dms.Main.Main;
import com.ae2dms.UI.HighScoreBar.HighScoreBarController;
import com.ae2dms.UI.Menu.ColourPreferenceController;
import javafx.animation.TranslateTransition;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.util.Duration;

import java.io.IOException;
import java.util.List;

public class AbstractBarController {


    @FXML
    private Pane MusicControllerAlias;

    Pane MusicController;

    private SoundPreferenceController soundPreferenceController;

    @FXML
    private Pane BottomBarAlias;

    Pane BottomBar;

    @FXML
    private Pane ColourPreferenceAlias;

    BorderPane colourPreference;

    public ColourPreferenceController colourPreferenceController;

    private HighScoreBarController highScoreBarController;

    @FXML
    private ImageView undoSwitch;

    public BooleanProperty musicControlIsShowing = new SimpleBooleanProperty(false);

    @FXML
    private ImageView musicSwitch;

    @FXML
    public Label highestScore;

    public BooleanProperty debugIsActive = new SimpleBooleanProperty(false);

    @FXML
    private ImageView debugSwitch;

    @FXML
    private ImageView saveGameSwitch;

    public BooleanProperty highScoreIsShown = new SimpleBooleanProperty(false);

    @FXML
    private ImageView highScoreSwitch;


    public AbstractBarController() {

        debugIsActive.bindBidirectional(GameDebugger.active);

        debugIsActive.addListener((observable, oldValue, newValue) -> {
            if (observable != null && observable.getValue()==true) {
                debugSwitch.setImage((Image)ResourceFactory.getResource("DEBUG_ON_ICON", ResourceType.Image));
            } else if (observable != null && observable.getValue()==false){
                debugSwitch.setImage((Image)ResourceFactory.getResource("DEBUG_OFF_ICON", ResourceType.Image));
            }
        });

        // set the button image
        highScoreIsShown.addListener((observable, oldValue, newValue) -> {
            if (observable != null && observable.getValue()==true) {
                highScoreSwitch.setImage((Image)ResourceFactory.getResource("HIGH_SCORE_LIST_ON_ICON", ResourceType.Image));
            } else if (observable != null && observable.getValue()==false){
                highScoreSwitch.setImage((Image)ResourceFactory.getResource("HIGH_SCORE_LIST_OFF_ICON", ResourceType.Image));
            }
        });

        // set the animation and render
        highScoreIsShown.addListener((observable, oldValue, newValue) -> {
            if (observable != null && observable.getValue()==true) {
                TranslateTransition translateTransition = new TranslateTransition(Duration.millis(500), BottomBar);
                // render the high score list
                renderHighScoreList();

                translateTransition.setByY(-668);
                translateTransition.play();
            } else if (observable != null && observable.getValue()==false){
                TranslateTransition translateTransition = new TranslateTransition(Duration.millis(500), BottomBar);
                translateTransition.setByY(668);
                translateTransition.play();
            }
        });
    }

    private void renderHighScoreList() {
        highScoreBarController.renderRecords();
    }

    public HighScoreBarController loadBottomBar() {
        // load real pane
        Pane barView = null;
        FXMLLoader menuBarLoader = null;
        try {
            menuBarLoader = new FXMLLoader(getClass().getResource("/ui/FXML/HighScoreBar.fxml"));
            barView = menuBarLoader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }

        // get children of parent of secPane (the VBox)
        List<Node> parentChildren = ((Pane) BottomBarAlias.getParent()).getChildren();

        // replace the child that contained the old secPane
        parentChildren.set(parentChildren.indexOf(BottomBarAlias), barView);

        // store the new pane in the secPane field to allow replacing it the same way later
        BottomBar = barView;

        barView.setLayoutY(668);

        highScoreBarController = menuBarLoader.getController();

        return highScoreBarController;
    }

    public SoundPreferenceController loadMusicController() {
        // load real pane
        Pane musicView = null;
        FXMLLoader musicLoader = null;
        try {
            musicLoader = new FXMLLoader(getClass().getResource("/ui/FXML/SoundPreference.fxml"));
            musicView = musicLoader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }

        musicView.setVisible(false);

        // get children of parent of secPane (the VBox)
        List<Node> parentChildren = ((Pane) MusicControllerAlias.getParent()).getChildren();

        // replace the child that contained the old secPane
        parentChildren.set(parentChildren.indexOf(MusicControllerAlias), musicView);

        // store the new pane in the secPane field to allow replacing it the same way later
        MusicController = musicView;

        soundPreferenceController = musicLoader.getController();

        return soundPreferenceController;
    }

    public ColourPreferenceController loadColourController() {
        // load real pane
        BorderPane colourView = null;
        FXMLLoader colourLoader = null;
        try {
            colourLoader = new FXMLLoader(getClass().getResource("/ui/FXML/ColourPreference.fxml"));
            colourView = colourLoader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }

        colourView.setVisible(false);

        // get children of parent of secPane (the VBox)
        List<Node> parentChildren = ((Pane) ColourPreferenceAlias.getParent()).getChildren();

        // replace the child that contained the old secPane
        parentChildren.set(parentChildren.indexOf(ColourPreferenceAlias), colourView);

        // store the new pane in the secPane field to allow replacing it the same way later
        colourPreference = colourView;

        colourPreferenceController = colourLoader.getController();

        return colourPreferenceController;
    }



    public void disableButton(String name) {
        switch (name) {
            case "Debug" -> {
                debugSwitch.setImage((Image)ResourceFactory.getResource("DEBUG_NULL_ICON", ResourceType.Image));
                debugSwitch.setDisable(true);
            }
            case "Undo" -> {
                undoSwitch.setImage((Image)ResourceFactory.getResource("UNDO_NULL_ICON", ResourceType.Image));
                undoSwitch.getStyleClass().clear();
                undoSwitch.setDisable(true);
            }
            case "Save Game" -> {
                saveGameSwitch.setImage((Image)ResourceFactory.getResource("SAVE_GAME_NULL_ICON", ResourceType.Image));
                saveGameSwitch.getStyleClass().clear();
                saveGameSwitch.setDisable(true);

            }
        };
    }

    public void enableButton(String name) {
        switch (name) {
            case "Undo" -> {
                undoSwitch.setImage((Image)ResourceFactory.getResource("UNDO_ICON", ResourceType.Image));
                undoSwitch.getStyleClass().add("Button");
                undoSwitch.setDisable(false);
            }
            case "Save Game" -> {
                saveGameSwitch.setImage((Image)ResourceFactory.getResource("SAVE_GAME_ICON", ResourceType.Image));
                saveGameSwitch.getStyleClass().add("Button");
                saveGameSwitch.setDisable(false);
            }
        };
    }


    public void menuBarClickToggleDebug() {
        debugIsActive.setValue(!debugIsActive.getValue());
    }

    public void menuBarClickToggleMusic() {
        musicControlIsShowing.setValue(!musicControlIsShowing.getValue());
    }

    public void menuBarSetMusicIsMute(boolean value) {
        musicControlIsShowing.setValue(value);
    }

    public void menuBarClickToggleHighScoreList() {
        highScoreIsShown.setValue(!highScoreIsShown.getValue());
    }

}

package com.ae2dms.UI;

import com.ae2dms.Business.GameDebugger;
import com.ae2dms.IO.ResourceFactory;
import com.ae2dms.IO.ResourceType;
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

/**
 * MenuView and GameView all have tbe same Bar, and the same FXMLs to that,
 * so the control login inside the bar items will be manages here,
 * GameView and MenuView shall all extend this Controller and get the identical controller for Bar
 *
 * the fx:id ending with Alias means that this is just the Pane reserved for substituting with the real item on the fly
 * and the real Pane will not have this ending.
 */
public class AbstractBarController {

    /**
     * Pane reserved for substituting with the real Music Controller on the fly
     * should only be used with loadMusicController()
     */
    @FXML
    private Pane MusicControllerAlias;

    /**
     * The real Pane of MusicController, as long as loadMusicController() is called before
     */
    Pane MusicController;

    /**
     * The controller for SoundPreference view, will be provided by calling loadMusicController()
     * @see SoundPreferenceController
     */
    private SoundPreferenceController soundPreferenceController;

    /**
     * Pane reserved for substituting with the real BottomBar on the fly
     * should only be used with loadMusicController()
     */
    @FXML
    private Pane BottomBarHighScoreAlias;

    /**
     * The real Pane of HighScoreList, as long as loadHighScoreBottomBar() is called before
     */
    Pane BottomBarHighScore;

    /**
     * The controller for HighScoreBar view, will be provided by calling loadHighScoreBottomBar()
     * @see HighScoreBarController
     */
    private HighScoreBarController highScoreBarController;

    /**
     * Pane reserved for substituting with the real Colour selector on the fly
     * should only be used with loadMusicController()
     */
    @FXML
    private Pane ColourPreferenceAlias;

    /**
     * The real BorderPane of HighScoreList, as long as loadColourController() is called before
     */
    BorderPane colourPreference;

    /**
     * The controller for Colour selector view, will be provided by calling loadColourController()
     * @see ColourPreferenceController
     */
    public ColourPreferenceController colourPreferenceController;

    /**
     * The ImageView of Undo button
     */
    @FXML
    private ImageView undoSwitch;

    /**
     * Whether the music controller is showing, can be set and the music controller view will appear
     */
    public BooleanProperty musicControlIsShowing = new SimpleBooleanProperty(false);

    /**
     * The label to show the best record at the bottom-left, will be bind to document
     */
    @FXML
    public Label bestRecord;

    /**
     * Whether the debug mode is activated, can be set and the debugger will start to log message
     */
    public BooleanProperty debugIsActive = new SimpleBooleanProperty(false);

    /**
     * The Image of Debug button, will be set with change to debugIsActive
     */
    @FXML
    private ImageView debugSwitch;

    /**
     * The Image of save game button
     */
    @FXML
    private ImageView saveGameSwitch;

    /**
     * Whether the high score list is showing, can be set and the high score view will appear
     */
    public BooleanProperty highScoreIsShown = new SimpleBooleanProperty(false);

    /**
     * The Image of high score button
     */
    @FXML
    private ImageView highScoreSwitch;


    /**
     * constructor of AbstractBarController
     * bind the state of debugButton with the static state in GameDebugger
     */
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
                TranslateTransition translateTransition = new TranslateTransition(Duration.millis(500), BottomBarHighScore);
                // render the high score list
                renderHighScoreList();

                translateTransition.setByY(-668);
                translateTransition.play();
            } else if (observable != null && observable.getValue()==false){
                TranslateTransition translateTransition = new TranslateTransition(Duration.millis(500), BottomBarHighScore);
                translateTransition.setByY(668);
                translateTransition.play();
            }
        });
    }

    /**
     * render the HighScoreList, will call renderRecords() in HighScoreBarController
     */
    private void renderHighScoreList() {
        highScoreBarController.renderRecords();
    }

    /**
     * load the HighScoreBar from FXML and substitute to the corresponding position
     * will initialize the colourPreferenceController
     * @return the controller of HighScoreBar
     * @see HighScoreBarController
     */
    public HighScoreBarController loadHighScoreBottomBar() {
        Pane barView = null;
        FXMLLoader menuBarLoader = null;
        try {
            menuBarLoader = new FXMLLoader(getClass().getResource("/ui/FXML/HighScoreBar.fxml"));
            barView = menuBarLoader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }
        List<Node> parentChildren = ((Pane) BottomBarHighScoreAlias.getParent()).getChildren();
        parentChildren.set(parentChildren.indexOf(BottomBarHighScoreAlias), barView);
        BottomBarHighScore = barView;
        barView.setLayoutY(668);
        highScoreBarController = menuBarLoader.getController();
        return highScoreBarController;
    }

    /**
     * load the SoundPreference from FXML and substitute to the corresponding position
     * will initialize the colourPreferenceController
     * @return the controller of SoundPreference
     * @see SoundPreferenceController
     */
    public SoundPreferenceController loadMusicController() {
        Pane musicView = null;
        FXMLLoader musicLoader = null;
        try {
            musicLoader = new FXMLLoader(getClass().getResource("/ui/FXML/SoundPreference.fxml"));
            musicView = musicLoader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }

        musicView.setVisible(false);
        List<Node> parentChildren = ((Pane) MusicControllerAlias.getParent()).getChildren();
        parentChildren.set(parentChildren.indexOf(MusicControllerAlias), musicView);
        MusicController = musicView;
        soundPreferenceController = musicLoader.getController();
        musicControlIsShowing.bindBidirectional(soundPreferenceController.isShowing);
        return soundPreferenceController;
    }

    /**
     * load the ColourPreference from FXML and substitute to the corresponding position
     * will initialize the colourPreferenceController
     * @return the controller of ColourPreference
     * @see ColourPreferenceController
     */
    public ColourPreferenceController loadColourController() {
        BorderPane colourView = null;
        FXMLLoader colourLoader = null;
        try {
            colourLoader = new FXMLLoader(getClass().getResource("/ui/FXML/ColourPreference.fxml"));
            colourView = colourLoader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }

        colourView.setVisible(false);
        List<Node> parentChildren = ((Pane) ColourPreferenceAlias.getParent()).getChildren();
        parentChildren.set(parentChildren.indexOf(ColourPreferenceAlias), colourView);
        colourPreference = colourView;
        colourPreferenceController = colourLoader.getController();
        return colourPreferenceController;
    }


    /**
     * disable the button in BottomBar
     * @param name chosen from "Debug", "Undo" and "Save Game"
     */
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

    /**
     * enable the button in BottomBar
     * @param name chosen from "Undo" and "Save Game"
     */
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


    /**
     * click the Debug button
     */
    public void menuBarClickDebug() {
        debugIsActive.setValue(!debugIsActive.getValue());
    }

    /**
     * click the Music button
     */
    public void menuBarClickMusic() {
        musicControlIsShowing.setValue(!musicControlIsShowing.getValue());
    }

    /**
     * click the HighScoreList button
     */
    public void menuBarClickHighScoreList() {
        highScoreIsShown.setValue(!highScoreIsShown.getValue());
    }

}

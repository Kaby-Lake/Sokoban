package com.ae2dms.UI;

import com.ae2dms.Business.GameDebugger;
import com.ae2dms.Business.GameDocument;
import com.ae2dms.IO.ResourceFactory;
import com.ae2dms.IO.ResourceType;
import com.ae2dms.Main.Main;
import com.ae2dms.UI.HighScoreBar.HighScoreBarController;
import com.ae2dms.UI.Menu.ColourPreferenceController;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;

/**
 * MenuView and GameView all have tbe same Bar, and the same FXMLs to that,
 * so the control logic inside the bar items will be manages here,
 * GameView and MenuView shall all extend this Controller and get the identical controller for Bar
 * <p>
 * Nested FXMLs as well as nested controllers are used here to improve maintainability and structure controller code
 */
public class AbstractBarController {

    /**
     * static GameDocument
     */
    private GameDocument gameDocument = Main.gameDocument;

    /**
     * The Pane for SoundPreference
     */
    @FXML
    protected Pane SoundPreference;

    /**
     * The Controller for SoundPreference
     *
     * @see SoundPreferenceController
     */
    @FXML
    protected SoundPreferenceController SoundPreferenceController;

    /**
     * The Pane for HighScoreBar
     */
    @FXML
    protected Pane HighScoreBar;

    /**
     * The controller for HighScoreBar
     *
     * @see HighScoreBarController
     */
    @FXML
    protected HighScoreBarController HighScoreBarController;

    /**
     * The Pane for ColourPreference
     */
    @FXML
    protected Pane ColourPreference;

    /**
     * The controller for ColourPreference
     *
     * @see ColourPreferenceController
     */
    @FXML
    protected ColourPreferenceController ColourPreferenceController;

    /**
     * The ImageView of Undo button
     */
    @FXML
    private ImageView undoSwitch;

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

        highScoreIsShown.addListener((observable, oldValue, newValue) -> {
            if (observable != null && observable.getValue()==true) {
                highScoreSwitch.setImage((Image)ResourceFactory.getResource("HIGH_SCORE_LIST_ON_ICON", ResourceType.Image));
                HighScoreBarController.show();
            } else if (observable != null && observable.getValue()==false){
                highScoreSwitch.setImage((Image)ResourceFactory.getResource("HIGH_SCORE_LIST_OFF_ICON", ResourceType.Image));
                HighScoreBarController.hide();
            }
        });
    }

    /**
     * disable the button in BottomBar
     *
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
     *
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
    @FXML
    protected void menuBarClickDebug() {
        debugIsActive.setValue(!debugIsActive.getValue());
    }

    /**
     * click the Music button
     */
    @FXML
    protected void menuBarClickMusic() {
        SoundPreferenceController.isShowing.setValue(!SoundPreferenceController.isShowing.getValue());
    }

    /**
     * click the HighScoreList button
     */
    @FXML
    protected void menuBarClickHighScoreList() {
        highScoreIsShown.setValue(!highScoreIsShown.getValue());
    }

}

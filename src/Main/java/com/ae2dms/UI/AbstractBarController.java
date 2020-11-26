package com.ae2dms.UI;

import com.ae2dms.Business.GameDebugger;
import com.ae2dms.IO.ResourceFactory;
import com.ae2dms.IO.ResourceType;
import com.ae2dms.Main.Main;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class AbstractBarController {

    @FXML
    private ImageView undoSwitch;

    public BooleanProperty musicIsMute = new SimpleBooleanProperty();

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

        musicIsMute.bindBidirectional(Main.prefMusicIsMute);

        debugIsActive.bindBidirectional(GameDebugger.active);

        debugIsActive.addListener((observable, oldValue, newValue) -> {
            if (observable != null && observable.getValue()==true) {
                debugSwitch.setImage((Image)ResourceFactory.getResource("DEBUG_ON_ICON", ResourceType.Image));
            } else if (observable != null && observable.getValue()==false){
                debugSwitch.setImage((Image)ResourceFactory.getResource("DEBUG_OFF_ICON", ResourceType.Image));
            }
        });

        musicIsMute.addListener((observable, oldValue, newValue) -> {
            if (observable != null && observable.getValue()==true) {
                musicSwitch.setImage((Image)ResourceFactory.getResource("MUSIC_OFF_ICON", ResourceType.Image));
            } else if (observable != null && observable.getValue()==false){
                musicSwitch.setImage((Image)ResourceFactory.getResource("MUSIC_ON_ICON", ResourceType.Image));
            }
        });

        highScoreIsShown.addListener((observable, oldValue, newValue) -> {
            if (observable != null && observable.getValue()==true) {
                highScoreSwitch.setImage((Image)ResourceFactory.getResource("HIGH_SCORE_LIST_ON_ICON", ResourceType.Image));
            } else if (observable != null && observable.getValue()==false){
                highScoreSwitch.setImage((Image)ResourceFactory.getResource("HIGH_SCORE_LIST_OFF_ICON", ResourceType.Image));
            }
        });
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
        musicIsMute.setValue(!musicIsMute.getValue());
    }

    public void menuBarSetMusicIsMute(boolean value) {
        musicIsMute.setValue(value);
    }

    public void menuBarClickToggleHighScoreList() {
        highScoreIsShown.setValue(!highScoreIsShown.getValue());

    }

}

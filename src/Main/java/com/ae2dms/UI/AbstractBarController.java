package com.ae2dms.UI;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class AbstractBarController {

    @FXML
    private ImageView undoSwitch;

    public final BooleanProperty musicSwitchToggle;
    @FXML
    private ImageView musicSwitch;

    @FXML
    public Label highestScore;

    public final BooleanProperty debugSwitchToggle;
    @FXML
    private ImageView debugSwitch;

    @FXML
    private ImageView saveGameSwitch;

    @FXML
    private ImageView highScoreSwitch;


    public AbstractBarController() {
        this.musicSwitchToggle = new SimpleBooleanProperty(true);
        this.debugSwitchToggle = new SimpleBooleanProperty(false);


        debugSwitchToggle.addListener((observable, oldValue, newValue) -> {
            if (observable != null && observable.getValue()==true) {
                debugSwitch.setImage(new Image(String.valueOf(getClass().getResource("/ui/Assets/BottomBar/Debug_on.png"))));
            } else if (observable != null && observable.getValue()==false){
                debugSwitch.setImage(new Image(String.valueOf(getClass().getResource("/ui/Assets/BottomBar/Debug_off.png"))));
            }
        });

        musicSwitchToggle.addListener((observable, oldValue, newValue) -> {
            if (observable != null && observable.getValue()==true) {
                musicSwitch.setImage(new Image(String.valueOf(getClass().getResource("/ui/Assets/BottomBar/Music_on.png"))));
            } else if (observable != null && observable.getValue()==false){
                musicSwitch.setImage(new Image(String.valueOf(getClass().getResource("/ui/Assets/BottomBar/Music_off.png"))));
            }
        });
    }

    public void disableButton(String name) {
        switch (name) {
            case "Debug" -> {
                debugSwitch.setImage(new Image(String.valueOf(getClass().getResource("/ui/Assets/BottomBar/Debug_null.png"))));
                debugSwitch.getStyleClass().clear();
                debugSwitch.setDisable(true);
            }
            case "Undo" -> {
                undoSwitch.setImage(new Image(String.valueOf(getClass().getResource("/ui/Assets/BottomBar/Undo_null.png"))));
                undoSwitch.getStyleClass().clear();
                undoSwitch.setDisable(true);
            }
            case "High score" -> {
                highScoreSwitch.setImage(new Image(String.valueOf(getClass().getResource("/ui/Assets/BottomBar/High_Score_List_null.png"))));
                highScoreSwitch.getStyleClass().clear();
                highScoreSwitch.setDisable(true);
            }
            case "Save Game" -> {
                saveGameSwitch.setImage(new Image(String.valueOf(getClass().getResource("/ui/Assets/BottomBar/Save_Game_null.png"))));
                saveGameSwitch.getStyleClass().clear();
                saveGameSwitch.setDisable(true);

            }
        };
    }

    public void enableButton(String name) {
        switch (name) {
            case "Undo" -> {
                undoSwitch.setImage(new Image(String.valueOf(getClass().getResource("/ui/Assets/BottomBar/Undo.png"))));
                undoSwitch.getStyleClass().add("Button");
                undoSwitch.setDisable(false);
            }
            case "Save Game" -> {
                saveGameSwitch.setImage(new Image(String.valueOf(getClass().getResource("/ui/Assets/BottomBar/Save_Game.png"))));
                saveGameSwitch.getStyleClass().add("Button");
                saveGameSwitch.setDisable(false);
            }
        };
    }



    public void menuBarClickToggleDebug() {
        debugSwitchToggle.setValue(!debugSwitchToggle.getValue());
    }

    public void menuBarClickToggleMusic() {
        musicSwitchToggle.setValue(!musicSwitchToggle.getValue());
    }

    public void bindHighestScore(IntegerProperty _highestScore) {
        this.highestScore.textProperty().bind(_highestScore.asString());
    }

}

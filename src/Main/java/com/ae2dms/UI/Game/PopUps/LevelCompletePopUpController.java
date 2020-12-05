package com.ae2dms.UI.Game.PopUps;

import com.ae2dms.UI.ViewUtilities;
import javafx.animation.Interpolator;
import javafx.animation.ScaleTransition;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.util.Duration;

public class LevelCompletePopUpController {

    /**
     * The JavaFX BorderPane of this pop up
     */
    @FXML
    private BorderPane Level_Complete_Pop_Up;

    /**
     * The Button to next level
     */
    @FXML
    public ImageView Next_Level_Button;

    /**
     * the index of the completed level
     */
    @FXML
    private Label Complete_Level_Index;

    /**
     * The total time of completion
     */
    @FXML
    private Label Complete_Time;

    /**
     * The total score of completion
     */
    @FXML
    private Label Complete_Step;

    /**
     * assign data to this pop up
     * @param levelIndex the level index of this completion
     * @param time the total time for now
     * @param steps the total steps for now
     */
    public void assignData(String levelIndex, String time, String steps) {

        Complete_Level_Index.setText(levelIndex);
        Complete_Time.setText(time);
        Complete_Step.setText(steps);

    }

    /**
     * show the pop up
     */
    public void show() {
        ViewUtilities.popUp(Level_Complete_Pop_Up);
    }

    /**
     * hide the pop up
     */
    public void hide() {
        ViewUtilities.fadeOut(Level_Complete_Pop_Up);
    }
}

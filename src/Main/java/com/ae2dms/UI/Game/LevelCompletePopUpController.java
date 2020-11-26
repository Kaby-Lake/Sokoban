package com.ae2dms.UI.Game;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;

public class LevelCompletePopUpController {


    //Group: Level Complete Pop Up

    @FXML
    private BorderPane Level_Complete_Pop_Up;

    @FXML
    public ImageView Next_Level_Button;

    @FXML
    private Label Level_Complete_Level_Index;

    @FXML
    private Label Level_Complete_Time;

    @FXML
    private Label Level_Complete_Score;

    void assignData(String levelIndex, String time, String score) {

        Level_Complete_Level_Index.setText(levelIndex);
        Level_Complete_Time.setText(time);
        Level_Complete_Score.setText(score);

    }

    public void show() {
        Level_Complete_Pop_Up.setVisible(true);
    }

    public void hide() {
        Level_Complete_Pop_Up.setVisible(false);
    }
}

package com.ae2dms.UI.Game;

import com.ae2dms.IO.ResourceFactory;
import com.ae2dms.IO.ResourceType;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;

public class GameCompletePopUpController {

    @FXML
    private BorderPane Game_Complete_Pop_Up;

    @FXML
    ImageView Level_Complete_Back_To_Menu;

    @FXML
    ImageView Level_Complete_High_Score_List;

    @FXML
    private TextField Game_Complete_Name;

    @FXML
    ImageView Save_Record;

    @FXML
    private Label Game_Complete_Time;

    @FXML
    private Label Game_Complete_Score;

    StringProperty name = new SimpleStringProperty();

    public void initialize() {
        name.bind(Game_Complete_Name.textProperty());

        Save_Record.setOnMouseClicked((event) -> {
            Save_Record.setImage((Image)ResourceFactory.getResource("SAVE_RECORD_DONE", ResourceType.Image));
        });

    }

    void assignData(String time, String score) {

        Game_Complete_Time.setText(time);
        Game_Complete_Score.setText(score);

    }

    public void show() {
        Game_Complete_Pop_Up.setVisible(true);
    }

    public void hide() {
        Game_Complete_Pop_Up.setVisible(false);
    }



}

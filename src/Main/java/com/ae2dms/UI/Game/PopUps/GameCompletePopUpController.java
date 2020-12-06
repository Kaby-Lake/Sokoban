package com.ae2dms.UI.Game.PopUps;

import com.ae2dms.Business.GameTimer;
import com.ae2dms.IO.ResourceFactory;
import com.ae2dms.IO.ResourceType;
import com.ae2dms.Main.Main;
import com.ae2dms.UI.ViewUtilities;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;

/**
 * The controller for GameCompletePopUp
 */
public class GameCompletePopUpController {

    int timeDuration;

    int score;

    @FXML
    private BorderPane Game_Complete_Pop_Up;

    @FXML
    public ImageView Level_Complete_Back_To_Menu;

    @FXML
    public ImageView Level_Complete_High_Score_List;

    @FXML
    private TextField Game_Complete_Name;

    @FXML
    ImageView Save_Record;

    @FXML
    private Label Game_Complete_Time;

    @FXML
    private Label Game_Complete_Score;

    public void initialize() {

    }

    public void assignData(int time, int score) {
        this.timeDuration = time;
        this.score = score;

        Game_Complete_Time.setText(GameTimer.parseToTimeFormat(time));
        Game_Complete_Score.setText(String.valueOf(score));

    }

    public void show() {
        ViewUtilities.popUp(Game_Complete_Pop_Up);
    }

    public void hide() {
        ViewUtilities.fadeOut(Game_Complete_Pop_Up);
    }


    @FXML
    private void clickSaveRecord(MouseEvent mouseEvent) {
        if ("".equals(this.Game_Complete_Name.getText())) {
            this.Game_Complete_Name.setText("Default Name");
            return;
        }
        Main.gameDocument.saveRecord(this.score, this.Game_Complete_Name.getText(), this.timeDuration);
        Save_Record.setImage((Image)ResourceFactory.getResource("SAVE_RECORD_DONE", ResourceType.Image));
        Save_Record.setDisable(true);
        Save_Record.getStyleClass().clear();
    }
}

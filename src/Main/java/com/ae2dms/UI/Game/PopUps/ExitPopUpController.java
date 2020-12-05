package com.ae2dms.UI.Game.PopUps;

import javafx.fxml.FXML;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;

public class ExitPopUpController {

    //Group: Confirm Exit Pop Up

    @FXML
    private BorderPane Confirm_Exit_Pop_Up;

    @FXML
    public ImageView Confirm_Exit_Exit;

    @FXML
    public ImageView Confirm_Exit_Back;


    public void show() {
        Confirm_Exit_Pop_Up.setVisible(true);
    }

    public void hide() {
        Confirm_Exit_Pop_Up.setVisible(false);
    }



    public void clickSaveGame(MouseEvent mouseEvent) {
    }
}

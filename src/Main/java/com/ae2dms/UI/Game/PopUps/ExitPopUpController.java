package com.ae2dms.UI.Game.PopUps;

import com.ae2dms.UI.ViewUtilities;
import javafx.fxml.FXML;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;


/**
 * Controller for ExitPopUp
 */
public class ExitPopUpController {

    @FXML
    private BorderPane Confirm_Exit_Pop_Up;

    @FXML
    public ImageView Confirm_Exit_Exit;

    @FXML
    public ImageView Confirm_Exit_Back;


    public void show() {
        ViewUtilities.popUp(Confirm_Exit_Pop_Up);
    }

    public void hide() {
        ViewUtilities.fadeOut(Confirm_Exit_Pop_Up);
    }



    public void clickSaveGame(MouseEvent mouseEvent) {
    }
}

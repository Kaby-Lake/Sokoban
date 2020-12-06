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

    /**
     * The Confirm exit exit.
     */
    @FXML
    public ImageView Confirm_Exit_Exit;

    /**
     * The Confirm exit back.
     */
    @FXML
    public ImageView Confirm_Exit_Back;


    /**
     * Show.
     */
    public void show() {
        ViewUtilities.popUp(Confirm_Exit_Pop_Up);
    }

    /**
     * Hide.
     */
    public void hide() {
        ViewUtilities.fadeOut(Confirm_Exit_Pop_Up);
    }


    /**
     * Click save game.
     *
     * @param mouseEvent the mouse event
     */
    public void clickSaveGame(MouseEvent mouseEvent) {
    }
}

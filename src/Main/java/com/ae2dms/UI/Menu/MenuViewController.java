package com.ae2dms.UI.Menu;

import com.ae2dms.Business.GameDocument;
import com.ae2dms.UI.AbstractBarController;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseEvent;

import java.io.InputStream;

public class MenuViewController extends AbstractBarController {
    private GameDocument gameDocument;

    public MenuViewController() {

    }

    public void initialize() throws IllegalStateException {
        // ensure model is only set once:
        if (this.gameDocument != null) {
            throw new IllegalStateException("Model can only be initialized once");
        }
        InputStream in = getClass().getClassLoader().getResourceAsStream("level/SampleGame.skb");
        this.gameDocument = new GameDocument(in, false);

        super.disableButton("Debug");
        super.disableButton("Save Game");

    }

    public void handleKey(KeyCode code) {
//        switch (code) {
//            case UP:
//                gameDocument.move(new Point(-1, 0));
//                break;
//
//            case RIGHT:
//                gameDocument.move(new Point(0, 1));
//                break;
//
//            case DOWN:
//                gameDocument.move(new Point(1, 0));
//                break;
//
//            case LEFT:
//                gameDocument.move(new Point(0, -1));
//                break;
//
//            default:
//                // TODO: implement something funny.
//        }
//        updateView();
//
//        if (GameDocument.isDebugActive()) {
//            System.out.println(code);
//        }
    }

    public void clickToggleDebug() {
        gameDocument.toggleDebug(menuBarClickToggleDebug());
    }

    //TODO:
    public void clickStartGame(MouseEvent mouseEvent) {
    }

    public void clickToggleMusic(MouseEvent mouseEvent) {
        gameDocument.toggleMusic(menuBarClickToggleMusic());
    }

    public void clickUndo(MouseEvent mouseEvent) {
        gameDocument.undo();
    }

    //TODO:
    public void clickInformation(MouseEvent mouseEvent) {
    }

    //TODO:
    public void clickSaveGame(MouseEvent mouseEvent) {
    }
}

package com.ae2dms.UI.Menu;

import com.ae2dms.Business.GameDocument;
import com.ae2dms.UI.GameView;
import javafx.fxml.FXML;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseEvent;

import java.awt.*;

public class MenuViewController {
    private GameView gameView;
    private GameDocument gameDocument;
    // FIXME: GameDocument and GameView can only have one instance, so use customized init

    public MenuViewController(GameView view) {
        this.gameView = view;
    }

    public MenuViewController() {
    }

    public void initialize(GameDocument gameDocument) throws IllegalStateException {
        // ensure model is only set once:
        if (this.gameDocument != null) {
            throw new IllegalStateException("Model can only be initialized once");
        }

        this.gameDocument = gameDocument ;
    }

    public void handleKey(KeyCode code) {
        switch (code) {
            case UP:
                gameDocument.move(new Point(-1, 0));
                break;

            case RIGHT:
                gameDocument.move(new Point(0, 1));
                break;

            case DOWN:
                gameDocument.move(new Point(1, 0));
                break;

            case LEFT:
                gameDocument.move(new Point(0, -1));
                break;

            default:
                // TODO: implement something funny.
        }
        updateView();

        if (GameDocument.isDebugActive()) {
            System.out.println(code);
        }
    }

    public void toggleDebug() {
        gameDocument.toggleDebug();
    }

    public void updateView() {
        // gameView.updateView();

    }

    public void clickStartGame(MouseEvent mouseEvent) {
    }
}

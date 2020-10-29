package Business;

import UI.GameView;
import javafx.scene.input.KeyCode;

import java.awt.*;

public class Controller {
    private GameView view;
    private GameDocument game;

    public Controller(GameView view, GameDocument game) {
        this.view = view;
        this.game = game;
    }

    public void handleKey(KeyCode code) {
        switch (code) {
            case UP:
                game.move(new Point(-1, 0));
                break;

            case RIGHT:
                game.move(new Point(0, 1));
                break;

            case DOWN:
                game.move(new Point(1, 0));
                break;

            case LEFT:
                game.move(new Point(0, -1));
                break;

            default:
                // TODO: implement something funny.
        }

        if (GameDocument.isDebugActive()) {
            System.out.println(code);
        }
    }

}

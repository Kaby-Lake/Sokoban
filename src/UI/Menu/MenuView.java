package UI.Menu;

import javafx.application.Application;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import javafx.util.Callback;

import java.awt.*;
import java.io.IOException;

public class MenuView extends BorderPane {

    private BorderPane view;
    private ActionSceneController controller;


    public MenuView() {
        view = new BorderPane();
    }
    public BorderPane init() throws Exception {
        FXMLLoader actionLoader = new FXMLLoader(getClass().getResource("/UI/MenuView/ActionScene.fxml"));
        view.setLeft(actionLoader.load());
        Background background = new Background(new BackgroundImage(new Image("/UI/IMG_2422.JPG"), null, null, null, null));
        view.setBackground(background);

        ActionSceneController actionSceneController = actionLoader.getController();
        return view;
    }

    private class ActionSceneController {
//        @FXML
//        private ListView<Person> listView ;
//
//        private DataModel model ;
//
//        public void initModel(DataModel model) {
//            // ensure model is only set once:
//            if (this.model != null) {
//                throw new IllegalStateException("Model can only be initialized once");
//            }
//
//            this.model = model ;
//            listView.setItems(model.getPersonList());
//
//            listView.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) ->
//                    model.setCurrentPerson(newSelection));
//
//            model.currentPersonProperty().addListener((obs, oldPerson, newPerson) -> {
//                if (newPerson == null) {
//                    listView.getSelectionModel().clearSelection();
//                } else {
//                    listView.getSelectionModel().select(newPerson);
//                }
//            });
//        }
    }
}

package UI.Menu;

import javafx.fxml.FXMLLoader;
import javafx.scene.layout.*;

import java.io.IOException;

public class MenuView {

    private static Pane view;
    private ActionSceneController controller;

    private MenuView() throws Exception {
        FXMLLoader menuLoader = new FXMLLoader(getClass().getResource("/UI/FXML/MenuViewScene.fxml"));
        view = menuLoader.load();
        ActionSceneController actionSceneController = menuLoader.getController();
    }


    public static Pane getInstance() throws Exception {
        if(view == null) {
            new MenuView();
            return view;
        }
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

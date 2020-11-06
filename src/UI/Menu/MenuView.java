package UI.Menu;

import javafx.fxml.FXMLLoader;
import javafx.scene.layout.*;

public class MenuView extends Pane {

    private Pane view;
    private ActionSceneController controller;

    public Pane getInstance() throws Exception {
        if (view != null) {
            return view;
        } else {
            view = new Pane();
            FXMLLoader actionLoader = new FXMLLoader(getClass().getResource("/UI/MenuViewScene.fxml"));
            ActionSceneController actionSceneController = actionLoader.getController();
            return view;
        }

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

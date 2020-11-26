package com.ae2dms.UI.HighScoreBar;

import com.ae2dms.Business.Data.GameRecord;
import com.ae2dms.Business.Data.GameRecord.Record;
import com.ae2dms.UI.Menu.MenuView;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Group;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;

import java.io.IOException;
import java.util.ArrayList;

public class HighScoreBarController {

    @FXML
    private VBox HighScoreItemVBox;

    @FXML
    private VBox HighTimeItemVBox;

    private ArrayList<Group> scoresView;

    private ArrayList<ScoreItemTemplateController> scoresController;

    private ArrayList<Group> timesView;

    private ArrayList<ScoreItemTemplateController> timesController;

    public void renderRecords(GameRecord records) {

        int scoreCount = 0;
        for (Record record : records.getRecords()) {
            if (scoreCount > 7) {
                break;
            }
            Group itemView = null;
            FXMLLoader itemLoader = null;
            try {
                itemLoader = new FXMLLoader(MenuView.class.getResource("/ui/FXML/ScoreItemTemplate.fxml"));
                itemView = itemLoader.load();
            } catch (IOException e) {
                e.printStackTrace();
            }

            ScoreItemTemplateController timeController = itemLoader.getController();
            timeController.setType("Score");
            timeController.setName(record.getName());
            timeController.setData(record.getScore());

            HighScoreItemVBox.getChildren().add( 0, itemView); // add on top

            scoreCount++;

//            scoresView.add(itemView);
//            scoresController.add(itemLoader.getController());
        }

        int timeCount = 0;
        for (Record record : records.sortRecordsByTime()) {
            if (timeCount > 7) {
                break;
            }
            Group itemView = null;
            FXMLLoader itemLoader = null;
            try {
                itemLoader = new FXMLLoader(MenuView.class.getResource("/ui/FXML/ScoreItemTemplate.fxml"));
                itemView = itemLoader.load();
            } catch (IOException e) {
                e.printStackTrace();
            }

            ScoreItemTemplateController timeController = itemLoader.getController();
            timeController.setType("Time");
            timeController.setName(record.getName());
            timeController.setData(record.getDurationSeconds());

            HighTimeItemVBox.getChildren().add( 0, itemView); // add on top

            timeCount++;

//            timesView.add(itemView);
//            timesController.add(itemLoader.getController());
        }

    }
}

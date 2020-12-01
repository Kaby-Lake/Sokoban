package com.ae2dms.UI.HighScoreBar;

import com.ae2dms.Business.Data.GameRecord;
import com.ae2dms.Business.Data.GameRecord.Record;
import com.ae2dms.IO.ResourceFactory;
import com.ae2dms.IO.ResourceType;
import com.ae2dms.UI.Menu.MenuView;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Group;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;

import java.io.IOException;
import java.net.URL;
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

        HighScoreItemVBox.getChildren().clear();
        HighTimeItemVBox.getChildren().clear();

        int scoreCount = 0;
        for (Record record : records.getRecords()) {
            if (scoreCount > 7) {
                break;
            }
            Group itemView = null;
            FXMLLoader itemLoader = null;
            try {
                itemLoader = new FXMLLoader((URL)ResourceFactory.getResource("SCORE_ITEM_TEMPLATE_FXML", ResourceType.FXML));
                itemView = itemLoader.load();
            } catch (IOException e) {
                e.printStackTrace();
            }

            ScoreItemTemplateController timeController = itemLoader.getController();
            timeController.setRandomImage();
            timeController.setType("Score");
            timeController.setName(record.getName());
            timeController.setData(record.getScore());

            HighScoreItemVBox.getChildren().add( 0, itemView); // add on top

            scoreCount++;

//            scoresView.add(itemView);
//            scoresController.add(itemLoader.getController());
        }

        if (scoreCount == 0) {
            //display
            HighScoreItemVBox.getChildren().add( 0, new ImageView((Image)ResourceFactory.getResource("NO_RECORDS_INFO", ResourceType.Image)));
        }

        int timeCount = 0;
        for (Record record : records.sortRecordsByTime()) {
            if (timeCount > 7) {
                break;
            }
            Group itemView = null;
            FXMLLoader itemLoader = null;
            try {
                itemLoader = new FXMLLoader((URL)ResourceFactory.getResource("SCORE_ITEM_TEMPLATE_FXML", ResourceType.FXML));
                itemView = itemLoader.load();
            } catch (IOException e) {
                e.printStackTrace();
            }

            ScoreItemTemplateController timeController = itemLoader.getController();
            timeController.setRandomImage();
            timeController.setType("Time");
            timeController.setName(record.getName());
            timeController.setData(record.getDurationSeconds());

            HighTimeItemVBox.getChildren().add( 0, itemView); // add on top

            timeCount++;

//            timesView.add(itemView);
//            timesController.add(itemLoader.getController());
        }

        if (scoreCount == 0) {
            //display
            HighTimeItemVBox.getChildren().add( 0, new ImageView((Image)ResourceFactory.getResource("NO_RECORDS_INFO", ResourceType.Image)));
        }

    }
}

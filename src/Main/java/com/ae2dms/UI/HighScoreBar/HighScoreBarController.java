package com.ae2dms.UI.HighScoreBar;

import com.ae2dms.Business.Data.GameRecord.Record;
import com.ae2dms.IO.ResourceFactory;
import com.ae2dms.IO.ResourceType;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Group;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;

import java.io.IOException;
import java.net.URL;

import static com.ae2dms.Business.GameDocument.records;

public class HighScoreBarController {

    @FXML
    private VBox HighScoreItemVBox;

    @FXML
    private VBox HighTimeItemVBox;

    public void renderRecords() {
        renderScoreRecords();
        renderTimeRecords();
    }

    private void renderScoreRecords() {
        HighScoreItemVBox.getChildren().clear();

        int scoreCount = 0;
        for (Record record : records.getRecords()) {
            scoreCount++;

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

            ScoreItemTemplateController scoreController = itemLoader.getController();
            scoreController.setRandomImage();
            scoreController.setType("Score");
            scoreController.setName(record.getName());
            scoreController.setData(record.getSteps());
            if (scoreCount == 1) {
                scoreController.setFirst("Score");
            }
            HighScoreItemVBox.getChildren().add(itemView);
        }

        if (scoreCount == 0) {
            HighScoreItemVBox.getChildren().add( 0, new ImageView((Image)ResourceFactory.getResource("NO_RECORDS_INFO", ResourceType.Image)));
        }
    }

    private void renderTimeRecords() {
        HighTimeItemVBox.getChildren().clear();

        int scoreCount = 0;
        for (Record record : records.sortRecordsByTime()) {
            scoreCount++;

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
            timeController.setType("Time");
            timeController.setName(record.getName());
            timeController.setData(record.getDurationSeconds());
            if (scoreCount == 1) {
                timeController.setFirst("Time");
            }
            HighTimeItemVBox.getChildren().add(itemView);
        }

        if (scoreCount == 0) {
            HighTimeItemVBox.getChildren().add( 0, new ImageView((Image)ResourceFactory.getResource("NO_RECORDS_INFO", ResourceType.Image)));
        }
    }
}

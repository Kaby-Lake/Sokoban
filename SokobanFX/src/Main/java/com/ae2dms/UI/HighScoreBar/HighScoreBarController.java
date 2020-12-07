package com.ae2dms.UI.HighScoreBar;

import com.ae2dms.Business.Data.GameRecord.Record;
import com.ae2dms.IO.ResourceFactory;
import com.ae2dms.IO.ResourceType;
import javafx.animation.TranslateTransition;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Group;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.util.Duration;

import java.io.IOException;
import java.net.URL;

import static com.ae2dms.Business.GameDocument.logger;
import static com.ae2dms.Business.GameDocument.records;

/**
 * The controller for High score list.
 */
public class HighScoreBarController {

    @FXML
    private Pane HighScoreBar;
    /**
     * The Vertical Box to display all sorted Records of Best Step Records, every individual is ScoreItemTemplate
     * see ScoreItemTemplate.fxml
     */
    @FXML
    private VBox HighScoreItemVBox;

    /**
     * The Vertical Box to display all sorted Records of Best Time Records, every individual is ScoreItemTemplate
     * see ScoreItemTemplate.fxml
     */

    @FXML
    private VBox HighTimeItemVBox;

    /**
     * called every time when the HighScoreList is displayed
     * the listView won't update in time tp save system resource until called to
     */
    public void renderRecords() {
        renderScoreRecords();
        renderTimeRecords();
    }

    /**
     * Render the Best Step Records in Vertical Box, with data from @records
     * will only display seven of it due to the limited length of ScoreBoard
     * with each time a different avatar
     * @see com.ae2dms.Business.Data.GameRecord
     */
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
                logger.severe(e.getMessage());
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

    /**
     * Render the Best Time Records in Vertical Box, with data from @records
     * will only display seven of it due to the limited length of ScoreBoard
     * with each time a different avatar
     * @see com.ae2dms.Business.Data.GameRecord
     */
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
                logger.severe(e.getMessage());
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

    public void show() {
        TranslateTransition translateTransition = new TranslateTransition(Duration.millis(500), HighScoreBar);
        // render the high score list
        renderRecords();
        translateTransition.setByY(-668);
        translateTransition.play();
    }

    public void hide() {
        TranslateTransition translateTransition = new TranslateTransition(Duration.millis(500), HighScoreBar);
        translateTransition.setByY(668);
        translateTransition.play();
    }
}

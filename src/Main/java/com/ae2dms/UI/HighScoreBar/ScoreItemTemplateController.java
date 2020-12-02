package com.ae2dms.UI.HighScoreBar;

import com.ae2dms.IO.ResourceFactory;
import com.ae2dms.IO.ResourceType;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.util.Random;

public class ScoreItemTemplateController {
    
    @FXML
    private ImageView HighScoreItemImage;
    
    @FXML
    private ImageView AvatarImage;
    
    @FXML
    private Label NameField;
    
    @FXML
    private Label DataField;

    public void setRandomImage() {
        Random random = new Random();
        Image randomImage = (Image)ResourceFactory.getResource("AVATARS_" + (random.nextInt(10) + 1), ResourceType.Image);
        AvatarImage.setImage(randomImage);
    }

    public void setType(String type) {
        switch (type) {
            case "Score" -> {
                this.HighScoreItemImage.setImage((Image)ResourceFactory.getResource("SCORE_ITEM_BACKGROUND", ResourceType.Image));
            }
            case "Time" -> {
                this.HighScoreItemImage.setImage((Image)ResourceFactory.getResource("TIME_ITEM_BACKGROUND", ResourceType.Image));
            }
        }
    }

    public void setName(String name) {
        this.NameField.setText(name);
    }

    public void setData(int data) {
        this.DataField.setText(String.valueOf(data));
    }

    public void setFirst(String type) {
        switch (type) {
            case "Score" -> {
                this.HighScoreItemImage.setImage((Image)ResourceFactory.getResource("SCORE_ITEM_FIRST_BACKGROUND", ResourceType.Image));
            }
            case "Time" -> {
                this.HighScoreItemImage.setImage((Image)ResourceFactory.getResource("TIME_ITEM_FIRST_BACKGROUND", ResourceType.Image));
            }
        }
    }
}

package com.ae2dms.UI.Menu;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.fxml.FXML;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;

import java.net.URISyntaxException;

/**
 * @Description: $
 * @Param: $
 * @return: $
 * @Author: Zichen XU
 */
public class ColourPreferenceController {

    @FXML
    private BorderPane Colour_Preference_Pop_Up;

    @FXML
    private BorderPane CrateBlue;

    @FXML
    private BorderPane CrateGreen;

    @FXML
    private BorderPane CrateSilver;

    @FXML
    private BorderPane CrateBrown;

    @FXML
    private BorderPane CrateRed;

    @FXML
    private BorderPane DiamondBlue;

    @FXML
    private BorderPane DiamondGreen;

    @FXML
    private BorderPane DiamondSilver;

    @FXML
    private BorderPane DiamondBrown;

    @FXML
    private BorderPane DiamondRed;

    public static final StringProperty selectedCrateColour = new SimpleStringProperty("Silver");

    public static final StringProperty selectedDiamondColour = new SimpleStringProperty("Red");

    private ImageView selectImageCrateView;
    private ImageView selectImageDiamondView;

    {
        try {
            selectImageCrateView = new ImageView(new Image(getClass().getResource("/ui/Assets/Colour_Select/Colour_Selected.png").toURI().toString()));
            selectImageDiamondView = new ImageView(new Image(getClass().getResource("/ui/Assets/Colour_Select/Colour_Selected.png").toURI().toString()));
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
    }

    public void initialize() {
        selectedCrateColour.addListener((observable, oldValue, newValue) -> {
            if (observable != null) {
                clearSelectionCrate();
                switch (newValue) {
                    case "Blue" -> {
                        CrateBlue.setCenter(selectImageCrateView);
                    }
                    case "Green" -> {
                        CrateGreen.setCenter(selectImageCrateView);
                    }
                    case "Silver" -> {
                        CrateSilver.setCenter(selectImageCrateView);
                    }
                    case "Brown" -> {
                        CrateBrown.setCenter(selectImageCrateView);
                    }
                    case "Red" -> {
                        CrateRed.setCenter(selectImageCrateView);
                    }
                }
            }
        });

        selectedDiamondColour.addListener((observable, oldValue, newValue) -> {
            if (observable != null) {
                clearSelectionDiamond();
                switch (newValue) {
                    case "Blue" -> {
                        DiamondBlue.setCenter(selectImageDiamondView);
                    }
                    case "Green" -> {
                        DiamondGreen.setCenter(selectImageDiamondView);
                    }
                    case "Silver" -> {
                        DiamondSilver.setCenter(selectImageDiamondView);
                    }
                    case "Brown" -> {
                        DiamondBrown.setCenter(selectImageDiamondView);
                    }
                    case "Red" -> {
                        DiamondRed.setCenter(selectImageDiamondView);
                    }

                }
            }
        });

    }

    private void clearSelectionCrate() {
        CrateBlue.setCenter(null);
        CrateGreen.setCenter(null);
        CrateSilver.setCenter(null);
        CrateBrown.setCenter(null);
        CrateRed.setCenter(null);
    }

    private void clearSelectionDiamond() {
        DiamondBlue.setCenter(null);
        DiamondGreen.setCenter(null);
        DiamondSilver.setCenter(null);
        DiamondBrown.setCenter(null);
        DiamondRed.setCenter(null);
    }


    public void show() {
        Colour_Preference_Pop_Up.setVisible(true);
    }

    public void hide() {
        Colour_Preference_Pop_Up.setVisible(false);
    }




    @FXML
    private void clickCrateBlue(MouseEvent mouseEvent) {
        selectedCrateColour.setValue("Blue");
    }

    @FXML
    private void clickCrateGreen(MouseEvent mouseEvent) {
        selectedCrateColour.setValue("Green");
    }

    @FXML
    private void clickCrateSilver(MouseEvent mouseEvent) {
        selectedCrateColour.setValue("Silver");
    }

    @FXML
    private void clickCrateBrown(MouseEvent mouseEvent) {
        selectedCrateColour.setValue("Brown");
    }

    @FXML
    private void clickCrateRed(MouseEvent mouseEvent) {
        selectedCrateColour.setValue("Red");
    }

    @FXML
    private void clickDiamondBlue(MouseEvent mouseEvent) {
        selectedDiamondColour.setValue("Blue");
    }

    @FXML
    private void clickDiamondGreen(MouseEvent mouseEvent) {
        selectedDiamondColour.setValue("Green");
    }

    @FXML
    private void clickDiamondSilver(MouseEvent mouseEvent) {
        selectedDiamondColour.setValue("Silver");
    }

    @FXML
    private void clickDiamondBrown(MouseEvent mouseEvent) {
        selectedDiamondColour.setValue("Brown");
    }

    @FXML
    private void clickDiamondRed(MouseEvent mouseEvent) {
        selectedDiamondColour.setValue("Red");
    }
}

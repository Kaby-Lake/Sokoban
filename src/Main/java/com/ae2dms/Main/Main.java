package com.ae2dms.Main;

import com.ae2dms.Business.GameDocument;
import com.ae2dms.UI.Menu.MenuView;
import javafx.application.Application;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.effect.Effect;
import javafx.scene.effect.MotionBlur;
import javafx.scene.image.Image;
import javafx.scene.layout.Background;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.InputStream;

public class Main extends Application {
    public static Stage primaryStage;
    public static GameDocument gameDocument;
    public static Scene menuScene;
    public static Font smartisanMaquetteBold;

    public static DoubleProperty prefMusicVolume = new SimpleDoubleProperty(80);
    public static DoubleProperty prefSFXVolume = new SimpleDoubleProperty(80);

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {

        smartisanMaquetteBold = Font.loadFont(Main.class.getResourceAsStream("/font/SmartisanMaquetteBold.ttf"), 20);

        Main.primaryStage = primaryStage;
        primaryStage.getIcons().add(new Image(Main.class.getResourceAsStream("/icon/icon.jpg")));

        loadDefaultGameMapAndInitDocument();
        Pane menuView = MenuView.getInstance().getView();


        primaryStage.setTitle(GameDocument.GAME_NAME);
        menuScene = new Scene(menuView, 1280, 720);
        primaryStage.setScene(menuScene);
        primaryStage.show();
        primaryStage.setResizable(false);

    }

    void loadDefaultGameMapAndInitDocument() {
        InputStream in = getClass().getClassLoader().getResourceAsStream("level/debugLevel.skb");
        Main.gameDocument = new GameDocument(in);
    }

    private void showVictoryMessage() {
        String dialogTitle = "Game Over!";
        String dialogMessage = "You completed " + gameDocument.mapSetName + " in " + gameDocument.movesCount + " moves!";
        MotionBlur mb = new MotionBlur(2, 3);

        newDialog(dialogTitle, dialogMessage, mb);
    }

    private void newDialog(String dialogTitle, String dialogMessage, Effect dialogMessageEffect) {
        final Stage dialog = new Stage();
        dialog.initModality(Modality.APPLICATION_MODAL);
        dialog.initOwner(primaryStage);
        dialog.setResizable(false);
        dialog.setTitle(dialogTitle);

        Text text1 = new Text(dialogMessage);
        text1.setTextAlignment(TextAlignment.CENTER);
        text1.setFont(javafx.scene.text.Font.font(14));

        if (dialogMessageEffect != null) {
            text1.setEffect(dialogMessageEffect);
        }

        VBox dialogVbox = new VBox(20);
        dialogVbox.setAlignment(Pos.CENTER);
        dialogVbox.setBackground(Background.EMPTY);
        dialogVbox.getChildren().add(text1);

        Scene dialogScene = new Scene(dialogVbox, 350, 150);
        dialog.setScene(dialogScene);
        dialog.show();
    }
}

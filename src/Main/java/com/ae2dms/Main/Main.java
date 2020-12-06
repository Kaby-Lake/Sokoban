package com.ae2dms.Main;

import com.ae2dms.Business.GameDocument;
import com.ae2dms.IO.MapFileLoader;
import com.ae2dms.UI.Menu.MenuView;
import javafx.application.Application;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.Pane;
import javafx.scene.text.Font;
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
        try {
            Main.gameDocument = new GameDocument(in);
        } catch (MapFileLoader.ErrorMapFileLoadException e) {
            e.printStackTrace();
        }
    }
}

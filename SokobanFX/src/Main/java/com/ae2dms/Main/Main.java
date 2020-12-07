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

/**
 * The Entry of this game, init document and switch to MenuView.
 */
public class Main extends Application {
    /**
     * The static primaryStage shared across.
     */
    public static Stage primaryStage;
    /**
     * The static gameDocument.
     */
    public static GameDocument gameDocument;
    /**
     * The singleton menuScene.
     */
    public static Scene menuScene;
    /**
     * The font SmartisanMaquetteBold.
     */
    public static Font smartisanMaquetteBold;

    /**
     * The preferred volume of music, from 0 - 100
     * later bind with musicPlayer in MenuView and GameView
     */
    public static DoubleProperty prefMusicVolume = new SimpleDoubleProperty(80);

    /**
     * The preferred volume of sound effects, from 0 - 100
     * later bind with AudioClip in MenuView and GameView
     */
    public static DoubleProperty prefSFXVolume = new SimpleDoubleProperty(80);

    /**
     * The entry point of application.
     *
     * @param args the input arguments
     */
    public static void main(String[] args) {
        launch(args);
    }

    /**
     * the first methods JavaFX calls
     * @param primaryStage primaryStage
     */
    @Override
    public void start(Stage primaryStage) {

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

    /**
     * Load default game map (SampleGame.skb).
     */
    void loadDefaultGameMapAndInitDocument() {
        InputStream in = getClass().getClassLoader().getResourceAsStream("level/SampleGame.skb");
        try {
            Main.gameDocument = new GameDocument(in);
        } catch (MapFileLoader.ErrorMapFileLoadException e) {
            e.printStackTrace();
        }
    }
}

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
 * The type Main.
 */
public class Main extends Application {
    /**
     * The constant primaryStage.
     */
    public static Stage primaryStage;
    /**
     * The constant gameDocument.
     */
    public static GameDocument gameDocument;
    /**
     * The constant menuScene.
     */
    public static Scene menuScene;
    /**
     * The constant smartisanMaquetteBold.
     */
    public static Font smartisanMaquetteBold;

    /**
     * The constant prefMusicVolume.
     */
    public static DoubleProperty prefMusicVolume = new SimpleDoubleProperty(80);
    /**
     * The constant prefSFXVolume.
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

    /**
     * Load default game map and init document.
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

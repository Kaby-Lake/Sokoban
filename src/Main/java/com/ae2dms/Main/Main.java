package com.ae2dms.Main;

import com.ae2dms.UI.Game.GameView;
import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.*;
import javafx.scene.effect.Effect;
import javafx.scene.effect.MotionBlur;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Background;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.scene.text.Font;

import java.awt.*;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import com.ae2dms.GameObject.*;
import com.ae2dms.Business.*;
import com.ae2dms.Business.Data.*;
import com.ae2dms.UI.Menu.*;
import javafx.scene.image.Image;

public class Main extends Application {
    public static Stage primaryStage;
    public static GameDocument gameDocument;
    public static Scene menuScene;
    private File saveFile;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {

        Font.loadFont(Main.class.getResource("/font/SmartisanMaquetteBold.woff.ttf").toExternalForm(), 10);

        Main.primaryStage = primaryStage;
        primaryStage.getIcons().add(new Image(Main.class.getResourceAsStream("/icon/icon.jpg")));

//        GridPane root = new GridPane();
//        root.add(menuBarInit(), 0, 0);
//        root.add(gameGrid, 0, 1);
//        root.add(MenuView.getInstance(), 0, 1);
        loadDefaultGameMapAndInitDocument(Main.primaryStage);
        Pane menuView = MenuView.getInstance();


        primaryStage.setTitle(GameDocument.GAME_NAME);
        menuScene = new Scene(menuView, 1280, 720);
        primaryStage.setScene(menuScene);
        primaryStage.show();
        primaryStage.setResizable(false);

    }

    private MenuBar menuBarInit() {
        MenuBar menu = new MenuBar();

        MenuItem menuItemSaveGame = new MenuItem("Save Game");
        menuItemSaveGame.setDisable(true);
        menuItemSaveGame.setOnAction(actionEvent -> saveGame());
        MenuItem menuItemLoadGame = new MenuItem("Load Game");
        menuItemLoadGame.setOnAction(actionEvent -> loadGame());
        MenuItem menuItemExit = new MenuItem("Exit");
        menuItemExit.setOnAction(actionEvent -> closeGame());
        Menu menuFile = new Menu("File");
        menuFile.getItems().addAll(menuItemSaveGame, menuItemLoadGame, new SeparatorMenuItem(), menuItemExit);

        MenuItem menuItemUndo = new MenuItem("Undo");
        menuItemUndo.setDisable(true);
        menuItemUndo.setOnAction(actionEvent -> undo());
        RadioMenuItem radioMenuItemMusic = new RadioMenuItem("Toggle Music");
        radioMenuItemMusic.setOnAction(actionEvent -> toggleMusic());
        RadioMenuItem radioMenuItemDebug = new RadioMenuItem("Toggle Debug");
        radioMenuItemDebug.setOnAction(actionEvent -> toggleDebug());
        MenuItem menuItemResetLevel = new MenuItem("Reset Level");
        menuItemResetLevel.setOnAction(actionEvent -> resetLevel());
        Menu menuLevel = new Menu("Level");
        menuLevel.getItems().addAll(menuItemUndo, radioMenuItemMusic, radioMenuItemDebug,
                new SeparatorMenuItem(), menuItemResetLevel);

        MenuItem menuItemGame = new MenuItem("About This Game");
        Menu menuAbout = new Menu("About");
        menuAbout.setOnAction(actionEvent -> showAbout());
        menuAbout.getItems().addAll(menuItemGame);
        menu.getMenus().addAll(menuFile, menuLevel, menuAbout);

        return menu;
    }

    void loadDefaultGameMapAndInitDocument(Stage primaryStage) {
        Main.primaryStage = primaryStage;
        InputStream in = getClass().getClassLoader().getResourceAsStream("level/SampleGame.skb");
        Main.gameDocument = new GameDocument(in, true);
    }

    private void loadGameFile() throws FileNotFoundException {

    }

    private void reloadGrid() {
//        if (gameDocument.isGameComplete()) {
//            showVictoryMessage();
//            return;
//        }
//
//        Level currentLevel = gameDocument.getCurrentLevel();
//        Level.LevelIterator levelGridIterator = (Level.LevelIterator) currentLevel.iterator();
//        gameGrid.getChildren().clear();
//        while (levelGridIterator.hasNext()) {
//            addObjectToGrid(levelGridIterator.next(), levelGridIterator.getcurrentposition());
//        }gameGrid.autosize();
//        primaryStage.sizeToScene();
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

    private void addObjectToGrid(AbstractGameObject gameObject, Point location) {
//        GraphicObject graphicObject = new GraphicObject(gameObject);
//        gameGrid.add(graphicObject, location.y, location.x);
    }

    public void closeGame() {
        System.exit(0);
    }

    public void saveGame() {
    }

    public void loadGame() {
        try {
            loadGameFile();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    public void undo() {
        closeGame();
    }

    public void resetLevel() {
        //TODO: implement Reset Level functionality
    }

    public void showAbout() {
        String title = "About this game";
        String message = "Game created by XXX\n";

        newDialog(title, message, null);
    }

    public void toggleMusic() {
        // TODO: implement Toggle music functionality
    }

    public void toggleDebug() {
        // gameDocument.toggleDebug();
        reloadGrid();
    }
}

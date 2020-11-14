package com.ae2dms.Main;

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
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.awt.*;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import com.ae2dms.GameObject.*;
import com.ae2dms.Business.*;
import com.ae2dms.Business.Data.*;
import com.ae2dms.UI.*;
import com.ae2dms.UI.Menu.*;
import javafx.scene.image.Image;

public class Main extends Application {
    private Stage primaryStage;
    private GameDocument gameDocument;
    private MenuViewController controller;
    private GameView view;
    private GridPane gameGrid;
    private File saveFile;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        this.primaryStage = primaryStage;
        primaryStage.getIcons().add(new Image(Main.class.getResourceAsStream("/icon/icon.jpg")));

        gameGrid = new GridPane();
//        GridPane root = new GridPane();
//        root.add(menuBarInit(), 0, 0);
//        root.add(gameGrid, 0, 1);
//        root.add(MenuView.getInstance(), 0, 1);

        primaryStage.setTitle(GameDocument.GAME_NAME);
        primaryStage.setScene(new Scene(MenuView.getInstance(), 1280, 720));
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

    void loadDefaultSaveFile(Stage primaryStage) {
        this.primaryStage = primaryStage;
        InputStream in = getClass().getClassLoader().getResourceAsStream("level/SampleGame.skb");
        initializeGame(in);
        setEventFilter();
    }

    private void initializeGame(InputStream input) {
//        gameDocument = new GameDocument(input, true);
//        view = new GameView();
//        controller = view.getController();
//        controller.initialize(gameDocument);
//        reloadGrid();
    }

    private void setEventFilter() {
        primaryStage.addEventFilter(KeyEvent.KEY_PRESSED, event -> {
            controller.handleKey(event.getCode());
            reloadGrid();
        });
    }

    private void loadGameFile() throws FileNotFoundException {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Open Save File");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Sokoban save file", "*.skb"));
        saveFile = fileChooser.showOpenDialog(primaryStage);

        if (saveFile != null) {
            if (GameDocument.isDebugActive()) {
                GameDocument.logger.info("Loading save file: " + saveFile.getName());
            }
            initializeGame(new FileInputStream(saveFile));
        }
    }

    private void reloadGrid() {
        if (gameDocument.isGameComplete()) {
            showVictoryMessage();
            return;
        }

        Level currentLevel = gameDocument.getCurrentLevel();
        Level.LevelIterator levelGridIterator = (Level.LevelIterator) currentLevel.iterator();
        gameGrid.getChildren().clear();
        while (levelGridIterator.hasNext()) {
            addObjectToGrid(levelGridIterator.next(), levelGridIterator.getcurrentposition());
        }gameGrid.autosize();
        primaryStage.sizeToScene();
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

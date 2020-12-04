package com.ae2dms.UI.Game;

import com.ae2dms.Business.*;
import com.ae2dms.GameObject.AbstractGameObject;
import com.ae2dms.GameObject.Objects.*;
import com.ae2dms.IO.ResourceFactory;
import com.ae2dms.IO.ResourceType;
import com.ae2dms.Main.Main;
import com.ae2dms.UI.AbstractBarController;
import com.ae2dms.UI.GameMediaPlayer;
import com.ae2dms.UI.MediaState;
import com.ae2dms.UI.SoundPreferenceController;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.fxml.FXML;
import javafx.scene.Group;
import javafx.scene.control.Label;
import javafx.scene.effect.GaussianBlur;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.media.AudioClip;
import javafx.util.StringConverter;
import javafx.util.converter.NumberStringConverter;

import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;

public class GameViewController extends AbstractBarController {

    @FXML
    private Pane Content;

    @FXML
    private GridPane previewGrid;

    @FXML
    private Pane MusicControllerAlias;

    //Group: LevelCompletePopUp and GameCompletePopUp
    @FXML
    private Pane root;

    //TextBinding: Score of current game, top_right

    @FXML
    private Label Score;

    @FXML
    private Label Time_Spend;

    //Group: Content Group, used to be blurred when pop up shows

    @FXML
    private Group Can_Blur_Group;

    @FXML
    private ImageView Background_Image;


    //Assigned to numbers

    @FXML
    private Label This_Level_Index;

    @FXML
    private Label All_Level_Count;



    // Map to manipulate

    @FXML
    private volatile GridPane stageGrid;

    @FXML
    private volatile GridPane objectsGrid;

    @FXML
    private volatile GridPane diamondsGrid;

    @FXML
    private volatile GridPane candyGrid;


    // Controllers to control LevelCompleteView and GameCompleteView

    private LevelCompletePopUpController levelCompletePopUpController;
    private GameCompletePopUpController gameCompletePopUpController;
    private ExitPopUpController exitPopUpController;
    private SoundPreferenceController soundPreferenceController;


    private GameMediaPlayer player = GameMediaPlayer.getInstance();

    private volatile GameDocument gameDocument = Main.gameDocument;

    public volatile static GraphicRender render;

    private GameStatus gameStatus = GameStatus.READY;

    private GameTimer timer = new GameTimer();

    public static HashMap<String, AudioClip> soundEffects = GameMediaPlayer.getInstance().soundEffects;

    private final BooleanProperty canUndo = new SimpleBooleanProperty(true);

    public static BooleanProperty isCheating = new SimpleBooleanProperty(false);

    // undo
    private final KeyCombination keyCombCtrZ = new KeyCodeCombination(KeyCode.Z, KeyCombination.SHORTCUT_DOWN);

    // mute
    private final KeyCombination keyCombCtrM = new KeyCodeCombination(KeyCode.M, KeyCombination.SHORTCUT_DOWN);

    // save
    private final KeyCombination keyCombCtrS = new KeyCodeCombination(KeyCode.S, KeyCombination.SHORTCUT_DOWN);


    public void initialize() throws IllegalStateException {

        isCheating.setValue(false);

        Background_Image.setImage(ResourceFactory.getRandomBackgroundImage());

        super.disableButton("High score");

        // this.isLevelComplete.bindBidirectional(gameDocument.isLevelComplete());
        soundEffects.put("UNMOVABLE_AUDIO_CLIP_MEDIA", (AudioClip)ResourceFactory.getResource("UNMOVABLE_AUDIO_CLIP", ResourceType.AudioClip));
        soundEffects.put("MOVE_AUDIO_CLIP_MEDIA", (AudioClip)ResourceFactory.getResource("MOVE_AUDIO_CLIP", ResourceType.AudioClip));
        soundEffects.put("MOVE_CRATE_AUDIO_CLIP_MEDIA", (AudioClip)ResourceFactory.getResource("MOVE_CRATE_AUDIO_CLIP", ResourceType.AudioClip));
        soundEffects.put("LEVEL_COMPLETE_AUDIO_CLIP", (AudioClip)ResourceFactory.getResource("LEVEL_COMPLETE_AUDIO_CLIP",ResourceType.AudioClip));
        soundEffects.put("GAME_COMPLETE_AUDIO_CLIP", (AudioClip)ResourceFactory.getResource("GAME_COMPLETE_AUDIO_CLIP",ResourceType.AudioClip));

        This_Level_Index.setText(Integer.toString(gameDocument.getCurrentLevel().getIndex()));

        All_Level_Count.setText(Integer.toString(gameDocument.getLevelsCount()));

        canUndo.addListener((observable, oldValue, newValue) -> {
            if (observable != null ) {
                if (observable.getValue()) {
                    enableButton("Undo");
                } else {
                    disableButton("Undo");
                }
            }
        });

        canUndo.bind(GameStageSaver.canUndo);

        Time_Spend.textProperty().bind(timer.timeToDisplay);

        render = new GraphicRender(stageGrid, objectsGrid, diamondsGrid, candyGrid);
        render.renderMap(gameDocument.getCurrentLevel());
        gameDocument.getPlayer().syncIsAnimating(isAnimating);

        this.highestScore.textProperty().bind(this.gameDocument.bestRecord.asString());
        StringConverter<Number> converter = new NumberStringConverter();
        Score.textProperty().bindBidirectional(this.gameDocument.movesCount, converter);

        loadBottomBar();

        soundPreferenceController = loadMusicController();

        musicControlIsShowing.bindBidirectional(soundPreferenceController.isShowing);
        soundPreferenceController.isMute.bindBidirectional(Main.prefMusicIsMute);
    }

    public void bindLevelGameCompleteController(LevelCompletePopUpController controller1, GameCompletePopUpController controller2, ExitPopUpController controller3) {
        this.levelCompletePopUpController = controller1;
        this.gameCompletePopUpController = controller2;
        this.exitPopUpController = controller3;
    }

    public void bindLevelGameCompleteView(BorderPane level, BorderPane game, BorderPane exit) {
        level.setVisible(false);
        game.setVisible(false);
        exit.setVisible(false);

        this.root.getChildren().add(level);
        this.root.getChildren().add(game);
        this.root.getChildren().add(exit);
    }

    private final BooleanProperty isAnimating = new SimpleBooleanProperty(false);

    private final BooleanProperty isBlur = new SimpleBooleanProperty(false);

    public void handleKey(KeyEvent event) {

        // simply ignore keys when animating
        if (isAnimating.getValue() || isBlur.getValue()) {
            return;
        }

        switch (gameStatus) {
            case READY -> {
                timer.start();
                gameStatus = GameStatus.PLAY;
            }

            case PAUSE -> {
                timer.resume();
                gameStatus = GameStatus.PLAY;
            }
            case END -> {
                return;
            }
        }

        // if is shortcuts

        if (keyCombCtrZ.match(event)) {
            this.clickUndo(null);
            return;
        }

        if (keyCombCtrM.match(event)) {
            this.clickToggleMusic(null);
            return;
        }

        if (keyCombCtrS.match(event)) {
            this.clickSaveGame(null);
            return;
        }

        Player player = gameDocument.getPlayer();
        Point direction = new Point(0, 0);

        KeyCode code = event.getCode();
        switch (code) {
            case UP, W, K -> {
                direction = new Point(0, -1);
            }

            case RIGHT, D, L -> {
                direction = new Point(1, 0);
            }

            case DOWN, S, J -> {
                direction = new Point(0, 1);
            }

            case LEFT, A, H -> {
                direction = new Point(-1, 0);
            }
        }
        isAnimating.set(true);

        if (isCheating.getValue()) {
            Crate cheatingCrate = GraphicRender.selectedCrate;
            if (code == KeyCode.SPACE) {
                cheatingCrate.settleDown();
                return;
            }
            if (cheatingCrate.canMoveBy(direction)) {
                try {
                    cheatingCrate.moveBy(direction);
                } catch (IllegalMovementException e) {
                    e.printStackTrace();
                }
            } else {
                cheatingCrate.shakeAnimation(direction);
            }
            return;
        }

        gameDocument.serializeCurrentState();
        GameDebugger.logMovement(this.gameDocument, direction);

        if (player.canMoveBy(direction)) {
            try {
                this.gameDocument.getPlayer().moveBy(direction);
                this.gameDocument.movesCount.set(this.gameDocument.movesCount.getValue() + 1);
                if (player.eatingCrate(gameDocument.getCurrentLevel().candyGrid)) {
                    addDraggableItem();
                }

            } catch (IllegalMovementException e) {
                e.printStackTrace();
            }
        } else {
            player.headTo(direction);
            player.shakeAnimation(direction);
        }



        checkIsLevelComplete();
    }

    @FXML
    private void clickToggleDebug() {
        menuBarClickToggleDebug();
    }

    private void checkIsLevelComplete() {
        if (gameDocument.isLevelComplete()) {
            GameViewController.soundEffects.get("LEVEL_COMPLETE_AUDIO_CLIP").play();
            this.timer.pause();
            this.gameStatus = GameStatus.PAUSE;

            GameDebugger.logLevelComplete(this.gameDocument.getCurrentLevel(), this.Time_Spend.getText(), this.Score.getText());

            gaussianBlur();
            levelCompletePopUpController.assignData(
                    Integer.toString(this.gameDocument.getCurrentLevel().getIndex()),
                    this.Time_Spend.getText(),
                    this.Score.getText()
            );

            levelCompletePopUpController.show();
            levelCompletePopUpController.Next_Level_Button.setOnMouseClicked((event) -> {
                levelCompletePopUpController.hide();
                exitGaussianBlur();
                switchToNextLevel();
            });
        }
    }

    private void checkIsGameComplete() {
        if (gameDocument.isGameComplete()) {
            GameViewController.soundEffects.get("GAME_COMPLETE_AUDIO_CLIP").play();
            timer.stop();
            gameStatus = GameStatus.END;

            GameDebugger.logGameComplete(gameDocument.getLevelsCount(), this.Time_Spend.getText(), this.Score.getText());

            gaussianBlur();
            gameCompletePopUpController.assignData(
                    timer.getTime(),
                    this.gameDocument.movesCount.getValue()
            );

            gameCompletePopUpController.show();

            gameCompletePopUpController.Level_Complete_High_Score_List.setOnMouseClicked((event) -> {
                gameCompletePopUpController.hide();
                exitGaussianBlur();
                menuBarClickToggleHighScoreList();
            });

            // the save code is in GameViewController

            gameCompletePopUpController.Level_Complete_Back_To_Menu.setOnMouseClicked((event) -> {
                levelCompletePopUpController.hide();
                exitGaussianBlur();
                gameDocument.restoreObject(GameStageSaver.getInitialState());
                GameStageSaver.clear();

                player.setMusic(MediaState.STOP);
                Main.primaryStage.setScene(Main.menuScene);
                player.setMusic(MediaState.PLAY);

            });
        }
    }

    private void switchToNextLevel() {
        if (gameDocument.isGameComplete()) {
            checkIsGameComplete();
            return;
        }

        gameDocument.changeToNextLevel();
        Background_Image.setImage(ResourceFactory.getRandomBackgroundImage());

        This_Level_Index.setText(Integer.toString(gameDocument.getCurrentLevel().getIndex()));

        render.renderMap(gameDocument.getCurrentLevel());
        gameDocument.getPlayer().syncIsAnimating(isAnimating);
    }

    @FXML
    private void clickToggleMusic(MouseEvent mouseEvent) {
        menuBarClickToggleMusic();
    }

    @FXML
    private void clickUndo(MouseEvent mouseEvent) {
        if (gameDocument.undo()) {
            render.renderMap(gameDocument.getCurrentLevel());
        }
    }

    @FXML
    private void clickSaveGame(MouseEvent mouseEvent) {
        GameStageSaver.saveToFile(this.gameDocument);
    }

    @FXML
    private void clickToMenu(MouseEvent mouseEvent) {

        gaussianBlur();
        exitPopUpController.show();

        exitPopUpController.Confirm_Exit_Exit.setOnMouseClicked((event) -> {
            timer.stop();

            exitPopUpController.hide();
            exitGaussianBlur();
            gameDocument.restoreObject(GameStageSaver.getInitialState());
            GameStageSaver.clear();

            player.setMusic(MediaState.STOP);
            Main.primaryStage.setScene(Main.menuScene);
            player.setMusic(MediaState.PLAY);
        });

        exitPopUpController.Confirm_Exit_Back.setOnMouseClicked((event) -> {
            exitPopUpController.hide();
            exitGaussianBlur();
        });

    }

    private void exitGaussianBlur() {
        Can_Blur_Group.setEffect(null);
        this.isBlur.setValue(false);
    }

    private void gaussianBlur() {
        GaussianBlur gaussianBlur = new GaussianBlur();
        gaussianBlur.setRadius(20);
        Can_Blur_Group.setEffect(gaussianBlur);
        this.isBlur.setValue(true);
    }

    @FXML
    private void clickHighScoreList(MouseEvent mouseEvent) {
        menuBarClickToggleHighScoreList();
    }




    // Dragging Stage

    // Draggable Object on the top right


    private final ArrayList<ImageView> DragList = new ArrayList<>();

    @FXML
    private void addDraggableItem() {

        ImageView stageDrag = new ImageView((Image)ResourceFactory.getResource("STAGE_DRAG_IMAGE", ResourceType.Image));

        stageDrag.setLayoutX(1234);
        stageDrag.setLayoutY(70 + DragList.size() * 70);

        stageDrag.setOnMouseReleased(this::draggingOnMouseReleased);
        stageDrag.setOnMouseDragged(this::previewDragging);
        stageDrag.setOnMousePressed(this::selectDragging);

        DragList.add(stageDrag);
        Content.getChildren().add(stageDrag);
    }

    private void draggingOnMouseReleased(MouseEvent mouseEvent) {
        double x = mouseEvent.getSceneX() + 20 - 195;
        double y = mouseEvent.getSceneY() + 15 - 55;
        int xIndex = (int) (x / 48);
        int yIndex = (int) (y / 30);
        int XBound = gameDocument.getCurrentLevel().floorGrid.getX();
        int YBound = gameDocument.getCurrentLevel().floorGrid.getY();

        if (xIndex < 0 || xIndex >= XBound || yIndex < 0 || yIndex >= YBound) {
            addDraggableItem();
            rePositionDraggings();
            chosenView.setVisible(false);
            return;
        }
        AbstractGameObject clickedObject = gameDocument.getCurrentLevel().floorGrid.getGameObjectAt(xIndex, yIndex);
        if (clickedObject instanceof Wall) {    // put the Floor at this position
            gameDocument.getCurrentLevel().floorGrid.putGameObjectAt(new Floor(gameDocument.getCurrentLevel(), xIndex, yIndex), new Point(xIndex, yIndex));
            render.renderMap(gameDocument.getCurrentLevel());
            chosenView.setVisible(false);
            previewGrid.getChildren().clear();
            rePositionDraggings();
        } else {
            addDraggableItem();
            rePositionDraggings();
            chosenView.setVisible(false);
            return;
        }
    }

    private void rePositionDraggings() {
        for (ImageView view : DragList) {
            view.setLayoutX(1234);
            view.setLayoutY(70 + DragList.indexOf(view) * 70);
        }
    }

    private static final ImageView preview = new ImageView((Image)ResourceFactory.getResource("STAGE_IMAGE", ResourceType.Image));

    static {
        preview.setFitWidth(48);
        preview.setFitHeight(48);
        preview.setOpacity(0.6);
    }

    ImageView chosenView;


    private void selectDragging(MouseEvent mouseEvent) {
        chosenView = DragList.remove(DragList.size() - 1);
        rePositionDraggings();
    }

    private void previewDragging(MouseEvent mouseEvent) {

        double x = mouseEvent.getSceneX() + 20 - 195;
        double y = mouseEvent.getSceneY() + 15 - 55;
        int xIndex = (int) (x / 48);
        int yIndex = (int) (y / 30);
        int XBound = gameDocument.getCurrentLevel().floorGrid.getX();
        int YBound = gameDocument.getCurrentLevel().floorGrid.getY();

        previewGrid.getChildren().clear();

        if (xIndex < 0 || xIndex >= XBound || yIndex < 0 || yIndex >= YBound) {
            chosenView.setLayoutX(mouseEvent.getSceneX());
            chosenView.setLayoutY(mouseEvent.getSceneY() - 15);
            return;
        }
        AbstractGameObject clickedObject = gameDocument.getCurrentLevel().floorGrid.getGameObjectAt(xIndex, yIndex);
        if (clickedObject instanceof Wall){
            chosenView.setOpacity(0.3);
            chosenView.setLayoutX(mouseEvent.getSceneX());
            chosenView.setLayoutY(mouseEvent.getSceneY());
            previewGrid.add(preview, xIndex, yIndex);
        } else {
            chosenView.setLayoutX(mouseEvent.getSceneX());
            chosenView.setLayoutY(mouseEvent.getSceneY());
            return;
        }
    }
}

enum GameStatus {
    READY, PAUSE, PLAY, END;
};



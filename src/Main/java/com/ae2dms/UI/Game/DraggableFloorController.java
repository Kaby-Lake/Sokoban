package com.ae2dms.UI.Game;

import com.ae2dms.Business.GameDocument;
import com.ae2dms.GameObject.AbstractGameObject;
import com.ae2dms.GameObject.Objects.Floor;
import com.ae2dms.GameObject.Objects.Wall;
import com.ae2dms.IO.ResourceFactory;
import com.ae2dms.IO.ResourceType;
import com.ae2dms.Main.Main;
import javafx.animation.ScaleTransition;
import javafx.fxml.FXML;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.util.Duration;

import java.awt.*;
import java.util.ArrayList;

/**
 * Controller for DraggableFloorPane
 * contains mouse event handlers and utils for the dragging and dropping
 */
public class DraggableFloorController {

    /**
     * The Pane reserved for adding draggable floor into
     */
    @FXML
    private Pane DraggableFloor;

    /**
     * The First Level grid to add preview Floor when dragging and hovering Floor behind
     */
    @FXML
    private GridPane previewGrid;

    private GameDocument gameDocument = Main.gameDocument;

    /**
     * ArrayList of JavaFX ImageView of Draggable Nodes
     */
    private final ArrayList<ImageView> DragList = new ArrayList<>();

    /**
     * add one draggable Floor on the screen
     * has pop up animation
     */
    void addDraggableItem() {

        ImageView stageDrag = new ImageView((Image) ResourceFactory.getResource("STAGE_DRAG_IMAGE", ResourceType.Image));

        stageDrag.setLayoutX(1234);
        stageDrag.setLayoutY(70 + DragList.size() * 70);

        stageDrag.setOnMouseReleased(this::draggingOnMouseReleased);
        stageDrag.setOnMouseDragged(this::previewDragging);
        stageDrag.setOnMousePressed(this::selectDragging);

        DragList.add(stageDrag);
        DraggableFloor.getChildren().add(stageDrag);
        ScaleTransition scaleTransition = new ScaleTransition(Duration.millis(150), stageDrag);
        scaleTransition.setFromX(0.01);
        scaleTransition.setFromY(0.01);
        scaleTransition.setToX(1);
        scaleTransition.setToY(1);
        scaleTransition.play();
    }

    /**
     * called when mouse release dragging
     * if the position of mouse is within Grid, then put the Floor into designated position
     * else put the Floor back to side
     * @param mouseEvent mouse event
     */
    private void draggingOnMouseReleased(MouseEvent mouseEvent) {
        double x = mouseEvent.getSceneX() + 20 - 195;
        double y = mouseEvent.getSceneY() + 15 - 55;
        int xIndex = (int) (x / 48);
        int yIndex = (int) (y / 30);
        int XBound = gameDocument.getCurrentLevel().floorGrid.getX();
        int YBound = gameDocument.getCurrentLevel().floorGrid.getY();

        if (xIndex < 0 || xIndex >= XBound || yIndex < 0 || yIndex >= YBound) {
            addDraggableItem();
            rePositionDragging();
            chosenView.setVisible(false);
            return;
        }
        AbstractGameObject clickedObject = gameDocument.getCurrentLevel().floorGrid.getGameObjectAt(xIndex, yIndex);
        if (clickedObject instanceof Wall) {
            gameDocument.getCurrentLevel().floorGrid.putGameObjectAt(new Floor(gameDocument.getCurrentLevel(), xIndex, yIndex), new Point(xIndex, yIndex));
            GameViewController.render.renderMap(gameDocument.getCurrentLevel());
            chosenView.setVisible(false);
            previewGrid.getChildren().clear();
            rePositionDragging();
        } else {
            addDraggableItem();
            rePositionDragging();
            chosenView.setVisible(false);
            return;
        }
    }

    /**
     * re structure of all the dragging in a list on the side
     */
    private void rePositionDragging() {
        for (ImageView view : DragList) {
            view.setLayoutX(1234);
            view.setLayoutY(70 + DragList.indexOf(view) * 70);
        }
    }

    /**
     * Preview image of Floor, will be semi-transparent
     */
    private static final ImageView preview = new ImageView((Image)ResourceFactory.getResource("STAGE_IMAGE", ResourceType.Image));

    static {
        preview.setFitWidth(48);
        preview.setFitHeight(48);
        preview.setOpacity(0.6);
    }

    /**
     * The ImageView for Floor selected for dragging
     */
    ImageView chosenView;

    /**
     * called when user click on a draggable Floor
     * @param mouseEvent mouse event
     */
    private void selectDragging(MouseEvent mouseEvent) {
        chosenView = DragList.remove(DragList.size() - 1);
        rePositionDragging();
    }

    /**
     * called each time mouse hold and moves
     * position the draggable node under the mouse cursor
     * or place the preview image to underneath grid position
     *
     * @param mouseEvent mouse event
     */
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

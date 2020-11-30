package com.ae2dms.Business;

import com.ae2dms.Business.Data.GameGrid;
import com.ae2dms.GameObject.AbstractGameObject;
import com.ae2dms.GameObject.Objects.*;
import com.ae2dms.UI.Game.GameViewController;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;

import java.awt.*;

public class GraphicRender {

    @FXML
    private volatile GridPane stageGrid;

    @FXML
    private volatile GridPane crateGrid;

    @FXML
    private volatile GridPane playerGrid;

    @FXML
    private volatile GridPane diamondsGrid;

    public GraphicRender(GridPane stageGrid, GridPane crateGrid, GridPane playerGrid, GridPane diamondsGrid) {
        this.stageGrid = stageGrid;
        this.crateGrid = crateGrid;
        this.playerGrid = playerGrid;
        this.diamondsGrid = diamondsGrid;
    }

    public void renderMap(GameGrid objectsGridDocument, GameGrid diamondsGridDocument) {

        this.stageGrid.getChildren().clear();
        this.diamondsGrid.getChildren().clear();

        for (AbstractGameObject object : objectsGridDocument) {
            if (!(object instanceof Wall)) {
                stageGrid.add((Floor.staticRender()), object.xPosition, object.yPosition);
            }
        }
        for (AbstractGameObject object : diamondsGridDocument) {
            if (object instanceof Diamond) {
                diamondsGrid.add(object.render(), object.xPosition, object.yPosition);
            }
        }
        renderItemAndPLayer(objectsGridDocument);
    }

    public void renderItemAndPLayer(GameGrid objectsGridDocument) {

        this.crateGrid.getChildren().clear();
        this.playerGrid.getChildren().clear();

        for (AbstractGameObject object : objectsGridDocument) {
            if (object instanceof Crate) {
                Crate crate = (Crate) object;
                this.crateGrid.add(crate.render(), crate.xPosition, crate.yPosition);
            }
        }

        for (AbstractGameObject object : objectsGridDocument) {
            if (object instanceof Player) {
                Player player = (Player) object;
                this.playerGrid.add(player.render(), player.xPosition, player.yPosition);
            }
        }

        this.crateGrid.setOnMouseClicked((event) -> {
            if (GameViewController.isCheating.getValue()) {
                return;
            }
            Node clickedNode = event.getPickResult().getIntersectedNode();
            Integer x = GridPane.getColumnIndex(clickedNode);
            Integer y = GridPane.getRowIndex(clickedNode);
            if (x == null || y == null) {
                return;
            }
            AbstractGameObject clickedObject = objectsGridDocument.getGameObjectAt(x, y);
            if (clickedObject instanceof Crate) {
                Crate thisObject = (Crate)clickedObject;
                GameViewController.isCheating.setValue(true);
                thisObject.isCheating = true;
                renderCheatingObject(thisObject);
            }
        });

        GameViewController.isCheating.addListener((observable, oldValue, newValue) -> {
            if (observable != null && observable.getValue()==false){
                if (selectedCrate != null) {
                    this.crateGrid.getChildren().remove(selectedCrate.cheatingView);
                    this.removeNodeByRowColumnIndex(selectedCrate.yPosition, selectedCrate.xPosition, this.crateGrid);
                    this.crateGrid.add(selectedCrate.render(), selectedCrate.xPosition, selectedCrate.yPosition);
                }
            }
        });
    }

    private void removeNodeByRowColumnIndex(int row, int column, GridPane gridPane) {
        ObservableList<Node> children = gridPane.getChildren();
        for (Node node : children) {
            if (GridPane.getRowIndex(node) == row && GridPane.getColumnIndex(node) == column) {
                ImageView imageView = (ImageView) node;
                gridPane.getChildren().remove(imageView);
                break;
            }
        }
    }

    public static Crate selectedCrate;

    private void renderCheatingObject(Crate crate) {
        selectedCrate = crate;
        this.crateGrid.getChildren().remove(crate.render());
        this.crateGrid.add(crate.renderCheating(), crate.xPosition, crate.yPosition);
    }

    private void renderPlayerCrateHierarchy(boolean isPlayerOnFirstStage) {
        if (isPlayerOnFirstStage) {
            playerGrid.toFront();
        } else {
            crateGrid.toFront();
        }
    }

    public void renderPlayerCrateHierarchyBeforeAnimation(Player afterMovement) {
        if (afterMovement.isOnNorthOfCrate()) {
            renderPlayerCrateHierarchy(false);
        } else if (afterMovement.isOnSouthOfCrate()) {
            renderPlayerCrateHierarchy(true);
        }
    }
}

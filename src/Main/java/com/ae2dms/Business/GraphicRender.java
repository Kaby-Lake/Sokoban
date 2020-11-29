package com.ae2dms.Business;

import com.ae2dms.Business.Data.GameGrid;
import com.ae2dms.GameObject.AbstractGameObject;
import com.ae2dms.GameObject.Objects.*;
import com.ae2dms.IO.ResourceFactory;
import com.ae2dms.IO.ResourceType;
import com.ae2dms.UI.Game.GameViewController;
import javafx.animation.TranslateTransition;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.util.Duration;

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
            int x = GridPane.getColumnIndex(clickedNode);
            int y = GridPane.getRowIndex(clickedNode);
            AbstractGameObject clickedObject = objectsGridDocument.getGameObjectAt(x, y);
            if (clickedObject instanceof Crate) {
                GameViewController.isCheating.setValue(true);
                renderCheatingObject((Crate)clickedObject, objectsGridDocument);
            }
        });

        GameViewController.isCheating.addListener((observable, oldValue, newValue) -> {
            if (observable != null && observable.getValue()==false){
                if (originalCrate != null) {
                    originalCrate.updatePosition(new Point(cheatingCrate.xPosition, cheatingCrate.yPosition));
                    this.crateGrid.getChildren().remove(cheatingCrate.render());
                    this.crateGrid.add(originalCrate.render(), originalCrate.xPosition, originalCrate.yPosition);
                }
            }
        });
    }

    public static CheatingCrate cheatingCrate;

    public static Crate originalCrate;

    private void renderCheatingObject(Crate crate, GameGrid objectsGridDocument) {
        originalCrate = crate;
        cheatingCrate = new CheatingCrate(crate, objectsGridDocument);
        this.crateGrid.getChildren().remove(crate.render());
        this.crateGrid.add(cheatingCrate.renderCheating(), crate.xPosition, crate.yPosition);
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

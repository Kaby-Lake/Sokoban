package com.ae2dms.Business;

import com.ae2dms.Business.Data.GameGrid;
import com.ae2dms.GameObject.AbstractGameObject;
import com.ae2dms.GameObject.Objects.*;
import javafx.fxml.FXML;
import javafx.scene.layout.GridPane;

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

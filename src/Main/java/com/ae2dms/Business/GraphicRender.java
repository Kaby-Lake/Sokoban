package com.ae2dms.Business;

import com.ae2dms.Business.Data.GameGrid;
import com.ae2dms.Business.Data.Level;
import com.ae2dms.GameObject.AbstractGameObject;
import com.ae2dms.GameObject.Objects.*;
import com.ae2dms.UI.Game.GameViewController;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;

public class GraphicRender {

    @FXML
    private volatile GridPane stageGrid;

    @FXML
    private volatile GridPane objectsGrid;

    @FXML
    private volatile GridPane diamondsGrid;

    @FXML
    private volatile GridPane candyGrid;

    public GraphicRender(GridPane stageGrid, GridPane objectsGrid, GridPane diamondsGrid, GridPane candyGrid) {
        this.stageGrid = stageGrid;
        this.objectsGrid = objectsGrid;
        this.diamondsGrid = diamondsGrid;
        this.candyGrid = candyGrid;
    }

    public void renderMap(Level level) {

        this.stageGrid.getChildren().clear();
        this.diamondsGrid.getChildren().clear();

        for (AbstractGameObject object : level.floorGrid) {
            if (!(object instanceof Wall)) {
                stageGrid.add((Floor.staticRender()), object.xPosition, object.yPosition);
            }
        }
        for (AbstractGameObject object : level.diamondsGrid) {
            if (object instanceof Diamond) {
                diamondsGrid.add(object.render(), object.xPosition, object.yPosition);
            }
        }
        renderItemAndPlayer(level);
    }

    public void renderItemAndPlayer(Level level) {

        this.objectsGrid.getChildren().clear();

        for (AbstractGameObject object : level.objectsGrid) {
            if (object instanceof Crate) {
                Crate crate = (Crate) object;
                this.objectsGrid.add(crate.render(), crate.xPosition, crate.yPosition);
            } else if (object instanceof Player) {
                Player player = (Player) object;
                this.objectsGrid.add(player.render(), player.xPosition, player.yPosition);
            }
        }

        this.candyGrid.getChildren().clear();

        for (AbstractGameObject object : level.candyGrid) {
            if (object instanceof Candy) {
                Candy candy = (Candy) object;
                candyGrid.add(candy.render(), candy.xPosition, candy.yPosition);
            }
        }

        this.objectsGrid.setOnMouseClicked((event) -> {
            if (GameViewController.isCheating.getValue()) {
                return;
            }
            Node clickedNode = event.getPickResult().getIntersectedNode();
            Integer x = GridPane.getColumnIndex(clickedNode);
            Integer y = GridPane.getRowIndex(clickedNode);
            if (x == null || y == null) {
                return;
            }
            AbstractGameObject clickedObject = level.objectsGrid.getGameObjectAt(x, y);
            if (clickedObject instanceof Crate) {
                Crate thisObject = (Crate)clickedObject;
                GameViewController.isCheating.setValue(true);
                thisObject.isCheating = true;
                renderCheatingObject(thisObject);
            }
        });

        GameViewController.isCheating.addListener((observable, oldValue, newValue) -> {
            if (observable != null && observable.getValue()==false){
                renderItemAndPlayer(level);
            }
        });
    }

    public static Crate selectedCrate;

    private void renderCheatingObject(Crate crate) {
        selectedCrate = crate;
        this.objectsGrid.getChildren().remove(crate.render());
        this.objectsGrid.add(crate.renderCheating(), crate.xPosition, crate.yPosition);
    }
}

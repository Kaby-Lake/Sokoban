package com.ae2dms.Business;

import com.ae2dms.Business.Data.Level;
import com.ae2dms.GameObject.AbstractGameObject;
import com.ae2dms.GameObject.Objects.*;
import com.ae2dms.UI.Game.GameViewController;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.layout.GridPane;

public class GraphicRender {

    /**
     * The JavaFX GridPane as stageGrid to add Wall and Floor, assigned in constructor
     */
    @FXML
    private volatile GridPane stageGrid;

    /**
     * The JavaFX GridPane as objectsGrid to add Crate and Player, assigned in constructor
     */
    @FXML
    private volatile GridPane objectsGrid;

    /**
     * The JavaFX GridPane as diamondsGrid to add Diamond, assigned in constructor
     */
    @FXML
    private volatile GridPane diamondsGrid;

    /**
     * The JavaFX GridPane as candyGrid to add Candy, assigned in constructor
     */
    @FXML
    private volatile GridPane candyGrid;

    /**
     * the GraphicRender Object to render all GameObjects into the assigned GridPane
     * @param stageGrid the stageGrid in FXML
     * @param objectsGrid the objectsGrid in FXML
     * @param diamondsGrid the diamondsGrid in FXML
     * @param candyGrid the candyGrid in FXML
     */
    public GraphicRender(GridPane stageGrid, GridPane objectsGrid, GridPane diamondsGrid, GridPane candyGrid) {
        this.stageGrid = stageGrid;
        this.objectsGrid = objectsGrid;
        this.diamondsGrid = diamondsGrid;
        this.candyGrid = candyGrid;
    }

    /**
     * render GameObjects according to the GameGrids in level
     * @param level the level which contains floorGrid, diamondsGrid, objectsGrid and candyGrid
     * @see Level
     */
    public void renderMap(Level level) {

        this.stageGrid.getChildren().clear();
        this.diamondsGrid.getChildren().clear();

        for (AbstractGameObject object : level.floorGrid) {
            if (object instanceof Floor) {
                stageGrid.add((object.render()), object.xPosition, object.yPosition);
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

    /**
     * The selected Crate by user to perform cheating
     */
    public static Crate selectedCrate;

    /**
     * render the given Crate into objectsGrid, with cheating status and view
     * @param crate the selected crate to cheat
     * @see Crate
     */
    private void renderCheatingObject(Crate crate) {
        selectedCrate = crate;
        this.objectsGrid.getChildren().remove(crate.render());
        this.objectsGrid.add(crate.renderCheating(), crate.xPosition, crate.yPosition);
    }
}

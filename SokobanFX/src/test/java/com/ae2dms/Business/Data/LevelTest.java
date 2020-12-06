package com.ae2dms.Business.Data;

import com.ae2dms.GameObject.AbstractGameObject;
import com.ae2dms.GameObject.Objects.*;
import javafx.embed.swing.JFXPanel;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.powermock.reflect.Whitebox;
import org.testfx.framework.junit.ApplicationTest;

import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class LevelTest extends ApplicationTest {

    public static List<String> lineParser(String raw) {
        return new ArrayList<>(Arrays.asList(raw.split(System.lineSeparator())));
    }


    @BeforeEach
    public void setUp() {
        JFXPanel jfxPanel = new JFXPanel();
    }

    @Test
    void testRawLevelParsingMap1() {

        List<String> level1Raw = LevelTest.lineParser(
                """
                        WWWWWWWWWWWWW WWWWWW
                        W    W       Y     W
                        W    W D  C  S     W
                        w    w   Y  WWWWWWWW
                        wwwwwwwwWwwwwwwwwwww""");
        Level testLevel1 = new Level("testLevelName", 1, level1Raw);
        Player player = testLevel1.getPlayerObject();
        helperTestWallVerificationOnObjectsGrid(testLevel1, new Point(0, 0));
        helperTestWallVerificationOnObjectsGrid(testLevel1, new Point(19, 0));
        helperTestFloorVerificationOnObjectsGrid(testLevel1, new Point(1, 1));
        helperTestCandyVerificationOnObjectsGrid(testLevel1, new Point(13, 1));
        helperTestCandyVerificationOnObjectsGrid(testLevel1, new Point(9, 3));
        helperTestFloorVerificationOnObjectsGrid(testLevel1, new Point(7, 2));
        helperTestCrateVerificationOnObjectsGrid(testLevel1, new Point(10, 2));
        helperTestPlayerVerificationOnDiamondsGrid(testLevel1, new Point(13, 2));
        for (int i = 0; i < 19; i++) {
            helperTestWallVerificationOnObjectsGrid(testLevel1, new Point(i, 4));
        }
        helperTestDiamondVerificationOnDiamondsGrid(testLevel1, new Point(7, 2));
        System.out.println(testLevel1.toString());
    }

    @Test
    void testRawLevelParsingMap2() {
        List<String> level1Raw = LevelTest.lineParser(
                """
                        WWWWWWWWWWWWWwWWWWWW
                        Wsycd              W
                        wwwwwwwwWwwwwwwwwwww""");
        Level testLevel1 = new Level("", 100000, level1Raw);
        for (int i = 0; i < 19; i++) {
            helperTestWallVerificationOnObjectsGrid(testLevel1, new Point(i, 0));
        }
        helperTestWallVerificationOnObjectsGrid(testLevel1, new Point(0, 1));
        helperTestPlayerVerificationOnDiamondsGrid(testLevel1, new Point(1, 1));
        helperTestCandyVerificationOnObjectsGrid(testLevel1, new Point(2, 1));
        helperTestCrateVerificationOnObjectsGrid(testLevel1, new Point(3, 1));
        helperTestDiamondVerificationOnDiamondsGrid(testLevel1, new Point(4, 1));
        for (int i = 0; i < 19; i++) {
            helperTestWallVerificationOnObjectsGrid(testLevel1, new Point(i, 2));
        }
        System.out.println(testLevel1.toString());
    }

    @Test
    void testRawLevelParsingMap3() {
        List<String> level1Raw = LevelTest.lineParser(
                """
                        WWWWWWWWWWWWWYWWWWWW
                        W    W D  C        W
                        W    W D  C  S     W
                        w    w      WWWWWWWW
                        wwwwwwwwWwwwwwwwwwww""");
        Level testLevel1 = new Level("testLevelName", 1, level1Raw);
        helperTestCandyVerificationOnObjectsGrid(testLevel1, new Point(13, 0));
        helperTestDiamondVerificationOnDiamondsGrid(testLevel1, new Point(7, 1));
        helperTestCrateVerificationOnObjectsGrid(testLevel1, new Point(10, 1));
        helperTestPlayerVerificationOnDiamondsGrid(testLevel1, new Point(13, 2));
        System.out.println(testLevel1.toString());
    }

    @Test
    void testMovement1() throws IllegalMovementException {
        List<String> level1Raw = LevelTest.lineParser(
                """
                        WWWWWWWWWWWWWWWWWWWW
                        W    W       S     W
                        W    W D     C     W
                        w    w      WWWWWWWW
                        wwwwwwwwWwwwwwwwwwww""");
        Level testLevel1 = new Level("testLevelName", 1, level1Raw);
        Player player = testLevel1.getPlayerObject();
        assertFalse(player.canMoveBy(new Point(0, 1)));
        assertFalse(player.canMoveBy(new Point(0, -1)));
        player.moveBy(new Point(1, 0));
        helperTestPlayerVerificationOnDiamondsGrid(testLevel1, new Point(14,1));
        player.moveBy(new Point(0, 1));
        for (int i = 0; i < 6; i++) {
            player.moveBy(new Point(-1, 0));
        }
        assertTrue(testLevel1.isComplete());
        System.out.println(testLevel1.toString());
    }

    @Test
    void testMovement2() throws IllegalMovementException {
        List<String> level1Raw = LevelTest.lineParser(
                """
                        WWWWWWWWWWWWW WWWWWW
                        W    W   D  CY     W
                        W    W D C   S     W
                        w    w   Y  WWWWWWWW
                        wwwwwwwwWwwwwwwwwwww""");
        Level testLevel1 = new Level("testLevelName", 1, level1Raw);
        Player player = testLevel1.getPlayerObject();
        player.moveBy(new Point(0, -1));
        if (player.isOnCandy() ){
            player.eatingCrate();
        }
        assertNull(testLevel1.candyGrid.getGameObjectAt(13, 1));
        for (int i = 0; i < 3; i++) {
            player.moveBy(new Point(-1, 0));
        }
        assertFalse(testLevel1.isComplete());
        player.moveBy(new Point(0, 1));
        player.moveBy(new Point(-1, 0));
        player.moveBy(new Point(-1, 0));
        assertTrue(testLevel1.isComplete());
        System.out.println(testLevel1.toString());

    }


    private void helperTestWallVerificationOnObjectsGrid(Level testLevel1, Point objectAt) {
        GameGrid floorGrid = testLevel1.floorGrid;
        AbstractGameObject wall = floorGrid.getGameObjectAt(objectAt);
        assertTrue(wall instanceof Wall);
        assertEquals(objectAt, wall.at());
        assertSame(Whitebox.getInternalState(wall, "level"), testLevel1);
    }

    private void helperTestFloorVerificationOnObjectsGrid(Level testLevel1, Point objectAt) {
        GameGrid floorGrid = testLevel1.floorGrid;
        AbstractGameObject floor = floorGrid.getGameObjectAt(objectAt);
        assertTrue(floor instanceof Floor);
        assertEquals(objectAt, floor.at());
        assertSame(Whitebox.getInternalState(floor, "level"), testLevel1);
    }

    private void helperTestCrateVerificationOnObjectsGrid(Level testLevel1, Point objectAt) {
        GameGrid objectsGrid = testLevel1.objectsGrid;
        AbstractGameObject crate = objectsGrid.getGameObjectAt(objectAt);
        assertTrue(crate instanceof Crate);
        assertEquals(objectAt, crate.at());
        assertSame(Whitebox.getInternalState(crate, "grid"), objectsGrid);
        assertSame(Whitebox.getInternalState(crate, "level"), testLevel1);
    }

    private void helperTestCandyVerificationOnObjectsGrid(Level testLevel1, Point objectAt) {
        GameGrid candyGrid = testLevel1.candyGrid;
        AbstractGameObject candy = candyGrid.getGameObjectAt(objectAt);
        assertTrue(candy instanceof Candy);
        assertEquals(objectAt, candy.at());
        assertSame(Whitebox.getInternalState(candy, "grid"), candyGrid);
        assertSame(Whitebox.getInternalState(candy, "level"), testLevel1);
    }

    private void helperTestDiamondVerificationOnDiamondsGrid(Level testLevel1, Point objectAt){
        GameGrid diamondsGrid = testLevel1.diamondsGrid;
        AbstractGameObject diamond = diamondsGrid.getGameObjectAt(objectAt);
        assertTrue(diamond instanceof Diamond);
        assertEquals(objectAt, diamond.at());
        assertSame(Whitebox.getInternalState(diamond, "level"), testLevel1);
    }

    private void helperTestPlayerVerificationOnDiamondsGrid(Level testLevel1, Point objectAt){
        GameGrid playerGrid = testLevel1.objectsGrid;
        AbstractGameObject player = playerGrid.getGameObjectAt(objectAt);
        assertTrue(player instanceof Player);
        assertEquals(objectAt, player.at());
        assertSame(Whitebox.getInternalState(player, "grid"), playerGrid);
        assertSame(Whitebox.getInternalState(player, "level"), testLevel1);
    }
}
package com.ae2dms.Business.Data;

import java.awt.*;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


import static org.junit.jupiter.api.Assertions.*;

import com.ae2dms.GameObject.AbstractGameObject;
import com.ae2dms.GameObject.Objects.*;

import org.junit.jupiter.api.Test;


class LevelTest {
    public static List<String> lineParser(String raw) {
        return new ArrayList<String>(Arrays.asList(raw.split(System.lineSeparator())));
    }

    @Test
    void testRawLevelParsingMap1() throws Exception {
        List<String> level1Raw = LevelTest.lineParser(
                """
                        WWWWWWWWWWWWW WWWWWW
                        W    W             W
                        W    W D  C  S     W
                        w    w      WWWWWWWW
                        wwwwwwwwWwwwwwwwwwww""");
        Level testLevel1 = new Level("testLevelName", 1, level1Raw);
        helperTestGameObjectVerificationOnObjectsGrid(testLevel1, new Point(0, 0), "Wall");
        helperTestGameObjectVerificationOnObjectsGrid(testLevel1, new Point(0, 19), "Wall");
        helperTestGameObjectVerificationOnObjectsGrid(testLevel1, new Point(1, 1), "Floor");
        helperTestGameObjectVerificationOnObjectsGrid(testLevel1, new Point(2, 7), "Floor");
        helperTestGameObjectVerificationOnObjectsGrid(testLevel1, new Point(2, 10), "Crate");
        helperTestGameObjectVerificationOnObjectsGrid(testLevel1, new Point(2, 13), "Keeper");
        for (int i = 0; i < 19; i++) {
            helperTestGameObjectVerificationOnObjectsGrid(testLevel1, new Point(4, i), "Wall");
        }
        helperTestGameObjectVerificationOnDiamondsGrid(testLevel1, new Point(2, 7));
    }

    @Test
    void testRawLevelParsingMap2() throws Exception {
        List<String> level1Raw = LevelTest.lineParser(
                """
                        WWWWWWWWWWWWWwWWWWWW
                        Ws cd              W
                        wwwwwwwwWwwwwwwwwwww""");
        Level testLevel1 = new Level("testLevelName", 1, level1Raw);
        for (int i = 0; i < 19; i++) {
            helperTestGameObjectVerificationOnObjectsGrid(testLevel1, new Point(0, i), "Wall");
        }
        helperTestGameObjectVerificationOnObjectsGrid(testLevel1, new Point(1, 0), "Wall");
        helperTestGameObjectVerificationOnObjectsGrid(testLevel1, new Point(1, 1), "Keeper");
        helperTestGameObjectVerificationOnObjectsGrid(testLevel1, new Point(1, 3), "Crate");
        for (int i = 0; i < 19; i++) {
            helperTestGameObjectVerificationOnObjectsGrid(testLevel1, new Point(2, i), "Wall");
        }
        helperTestGameObjectVerificationOnDiamondsGrid(testLevel1, new Point(1, 4));
    }

    @Test
    void testRawLevelParsingMap3() throws Exception {
        List<String> level1Raw = LevelTest.lineParser(
                """
                        WWWWWWWWWWWWW WWWWWW
                        W    W D  C        W
                        W    W D  C  S     W
                        w    w      WWWWWWWW
                        wwwwwwwwWwwwwwwwwwww""");
        Level testLevel1 = new Level("testLevelName", 1, level1Raw);
        helperTestGameObjectVerificationOnObjectsGrid(testLevel1, new Point(0, 13), "Floor");
        helperTestGameObjectVerificationOnObjectsGrid(testLevel1, new Point(1, 7), "Floor");
        helperTestGameObjectVerificationOnObjectsGrid(testLevel1, new Point(1, 10), "Crate");
        helperTestGameObjectVerificationOnObjectsGrid(testLevel1, new Point(2, 13), "Keeper");
        helperTestGameObjectVerificationOnDiamondsGrid(testLevel1, new Point(1, 7));
    }

    @Test
    void testIsCompleteMap1() throws IllegalMovementException {
        List<String> level1Raw = LevelTest.lineParser(
                """
                        WWWWWWWWWWWWW WWWWWW
                        W    W             W
                        W    W D  C  S     W
                        w    w      WWWWWWWW
                        wwwwwwwwWwwwwwwwwwww""");
        Level testLevel1 = new Level("testLevelName", 1, level1Raw);
        ((Crate)testLevel1.objectsGrid.getGameObjectAt(2, 10)).moveBy(new Point(0, -3));
        assertTrue(testLevel1.isComplete());

    }


    private void helperTestGameObjectVerificationOnObjectsGrid(Level testLevel1, Point gameObjectAt, String instanceOf) throws NoSuchFieldException, IllegalAccessException, ClassNotFoundException {
        Field objectsGridField = Level.class.getDeclaredField("objectsGrid");
        objectsGridField.setAccessible(true);
        GameGrid objectsGrid = (GameGrid)objectsGridField.get(testLevel1);
        AbstractGameObject testGameObject1 = objectsGrid.getGameObjectAt(gameObjectAt);
        assertTrue((testGameObject1.getClass() == (Class.forName("GameObject.Objects." + instanceOf))) && testGameObject1.at().equals(gameObjectAt));
        Field objectsGridFieldInGameObject = AbstractGameObject.class.getDeclaredField("grid");
        objectsGridFieldInGameObject.setAccessible(true);
        assertSame((GameGrid)objectsGridFieldInGameObject.get(testGameObject1), objectsGrid);
    }

    private void helperTestGameObjectVerificationOnDiamondsGrid(Level testLevel1, Point gameObjectAt) throws NoSuchFieldException, IllegalAccessException, ClassNotFoundException {
        Field diamondsGridField = Level.class.getDeclaredField("diamondsGrid");
        diamondsGridField.setAccessible(true);
        GameGrid objectsGrid = (GameGrid)diamondsGridField.get(testLevel1);
        AbstractGameObject testGameObject1 = objectsGrid.getGameObjectAt(gameObjectAt);
        assertTrue((testGameObject1.getClass() == (Class.forName("GameObject.Objects.Diamond"))) && testGameObject1.at().equals(gameObjectAt));
        Field objectsGridFieldInGameObject = AbstractGameObject.class.getDeclaredField("grid");
        objectsGridFieldInGameObject.setAccessible(true);
        assertSame((GameGrid)objectsGridFieldInGameObject.get(testGameObject1), objectsGrid);
    }


}
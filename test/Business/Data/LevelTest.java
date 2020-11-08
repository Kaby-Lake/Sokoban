package Business.Data;

import java.awt.*;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


import static org.junit.jupiter.api.Assertions.*;

import GameObject.AbstractGameObject;
import GameObject.Objects.Wall;
import org.junit.jupiter.api.Test;


class LevelTest {
    public static List<String> lineParser(String raw) {
        return new ArrayList<String>(Arrays.asList(raw.split(System.lineSeparator())));
    }

    @Test
    void testRawLevelParsing() throws Exception {
        List<String> level1Raw = LevelTest.lineParser(
                """
                        WWWWWWWWWWWWW WWWWWW
                        W    W             W
                        W    W D  C  S     W
                        w    w      WWWWWWWW
                        wwwwwwwwWwwwwwwwwwww""");
        Field objectsGridField = Level.class.getDeclaredField("objectsGrid");
        Field diamondsGridField = Level.class.getDeclaredField("diamondsGrid");
        Level testLevel1 = new Level("testLevelName", 1, level1Raw);
        objectsGridField.setAccessible(true);
        diamondsGridField.setAccessible(true);
        GameGrid objectsGrid = (GameGrid)objectsGridField.get(testLevel1);
        AbstractGameObject testGameObject1 = objectsGrid.getGameObjectAt(0, 0);
        assertTrue(testGameObject1 instanceof Wall && testGameObject1.at().equals(new Point(0, 0)));
        Field objectsGridFieldInGameObject = AbstractGameObject.class.getDeclaredField("grid");
        objectsGridFieldInGameObject.setAccessible(true);
        assertSame((GameGrid)objectsGridFieldInGameObject.get(testGameObject1), objectsGrid);
    }
}
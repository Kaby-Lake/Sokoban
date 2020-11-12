package com.ae2dms.Business.Data;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.awt.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

class GameGridTest {
    GameGrid grid;


    @Test
    void testTranslatePoint() {
        assertEquals(new Point(0, 0), GameGrid.translatePoint(new Point(-1, -3), new Point(1, 3)));
        assertEquals(new Point(-1, -3), GameGrid.translatePoint(new Point(-1, -3), new Point(0, 0)));
    }

    @BeforeEach
    void init() {
        grid = new GameGrid(10, 10);
    }

    @Test
    void testPutGameObjectAt() throws Exception {
//        grid.putGameObjectAt(GameObject.fromChar('w'), 0, 0);
//        grid.putGameObjectAt(GameObject.fromChar('c'), 3, 7);
//        Class gridClass = grid.getClass();
//        // get private property
//        Field privatGeameObjects = gridClass.getDeclaredField("gameObjects");
//        // Make the field accessible
//        privatGeameObjects.setAccessible(true);
//        // get the value and have a test
//        assertEquals('W', ((GameObject[][]) privatGeameObjects.get(grid))[0][0].getCharSymbol());
//        assertEquals('C', ((GameObject[][]) privatGeameObjects.get(grid))[3][7].getCharSymbol());
//
//        assertFalse(grid.putGameObjectAt(GameObject.fromChar('w'), -1, -1));
//        assertFalse(grid.putGameObjectAt(GameObject.fromChar('w'), 11, 11));

    }


    @Test
    void testGetTargetFromSource() {
//        grid.putGameObjectAt(GameObject.fromChar('w'), 0, 0);
//        grid.putGameObjectAt(GameObject.fromChar('c'), 3, 7);
//        assertEquals(grid.getTargetFromSource(new Point(0, 0), null).getCharSymbol(), 'W');
//        assertNull(grid.getTargetFromSource(new Point(3, 5), null));
//        assertEquals(grid.getTargetFromSource(new Point(1, 3), new Point(2, 4)).getCharSymbol(), 'C');
//        assertThrows(ArrayIndexOutOfBoundsException.class, () -> {
//            grid.getTargetFromSource(new Point(1, 3), new Point(10, 20));
//        });
//
//        assertThrows(ArrayIndexOutOfBoundsException.class, () -> {
//            grid.getTargetFromSource(new Point(1, 3), new Point(-10, -20));
//        });
    }

    @Test
    void iterator() {
    }
}
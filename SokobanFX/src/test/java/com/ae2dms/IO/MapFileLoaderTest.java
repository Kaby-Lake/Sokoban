package com.ae2dms.IO;

import org.apache.commons.io.IOUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.powermock.reflect.Whitebox;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.PrintStream;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class MapFileLoaderTest {

    private MapFileLoader loader;

    private final PrintStream standardOut = System.out;
    private final ByteArrayOutputStream outputStreamCaptor = new ByteArrayOutputStream();

    @BeforeEach
    public void setUp() {
        loader = new MapFileLoader();
        System.setOut(new PrintStream(outputStreamCaptor));
    }

    @Test
    void testValidMap1() throws Exception {

        InputStream input = getClass().getClassLoader().getResourceAsStream("map/debugGame.skb");
        assert input != null;
        String rawMapFile = IOUtils.toString(input, "utf-8");

        boolean result = Whitebox.invokeMethod(loader, "validMap", rawMapFile);
        assertTrue(result);

    }

    @Test
    void testValidMap2() throws Exception {

        InputStream input = getClass().getClassLoader().getResourceAsStream("map/debugLevel.skb");
        assert input != null;
        String rawMapFile = IOUtils.toString(input, "utf-8");

        boolean result = Whitebox.invokeMethod(loader, "validMap", rawMapFile);
        assertTrue(result);

    }

    @Test
    void testValidMap3() throws Exception {

        InputStream input = getClass().getClassLoader().getResourceAsStream("map/SampleGame.skb");
        assert input != null;
        String rawMapFile = IOUtils.toString(input, "utf-8");

        boolean result = Whitebox.invokeMethod(loader, "validMap", rawMapFile);
        assertTrue(result);

    }

    @Test
    void testInvalidMap1() throws Exception {

        InputStream input = getClass().getClassLoader().getResourceAsStream("map/debugGame1.skb");
        assert input != null;
        String rawMapFile = IOUtils.toString(input, "utf-8");

        boolean result = Whitebox.invokeMethod(loader, "validMap", rawMapFile);
        assertFalse(result);

        assertTrue(outputStreamCaptor.toString().trim().contains("4 This line does not match the format of Map"));
    }

    @Test
    void testInvalidMap2() throws Exception {

        InputStream input = getClass().getClassLoader().getResourceAsStream("map/debugGame2.skb");
        assert input != null;
        String rawMapFile = IOUtils.toString(input, "utf-8");

        boolean result = Whitebox.invokeMethod(loader, "validMap", rawMapFile);
        assertFalse(result);

        assertTrue(outputStreamCaptor.toString().trim().contains("4 This line does not match the format of Map"));
    }

    @Test
    void testInvalidMap3() throws Exception {

        InputStream input = getClass().getClassLoader().getResourceAsStream("map/debugGame3.skb");
        assert input != null;
        String rawMapFile = IOUtils.toString(input, "utf-8");

        boolean result = Whitebox.invokeMethod(loader, "validMap", rawMapFile);
        assertFalse(result);

        assertTrue(outputStreamCaptor.toString().trim().contains("1 FirstLine does not match \"MapSetName: $Name\""));
    }

    @Test
    void testInvalidMap4() throws Exception {

        InputStream input = getClass().getClassLoader().getResourceAsStream("map/debugGame4.skb");
        assert input != null;
        String rawMapFile = IOUtils.toString(input, "utf-8");

        boolean result = Whitebox.invokeMethod(loader, "validMap", rawMapFile);
        assertFalse(result);

        assertTrue(outputStreamCaptor.toString().trim().contains("2 The Start of Every Level does not match \"LevelName: $Name\""));
    }



}
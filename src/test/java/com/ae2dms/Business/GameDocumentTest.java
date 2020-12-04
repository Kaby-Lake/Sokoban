package com.ae2dms.Business;

import com.ae2dms.Business.Data.Level;
import com.ae2dms.GameObject.Objects.IllegalMovementException;
import com.ae2dms.GameObject.Objects.Player;
import javafx.collections.ObservableList;
import javafx.embed.swing.JFXPanel;
import org.apache.maven.shared.utils.io.FileUtils;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.powermock.reflect.Whitebox;

import java.awt.*;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class GameDocumentTest {

    GameDocument document;

    @BeforeEach
    public void setUp() {
        JFXPanel jfxPanel = new JFXPanel();
    }

    @AfterEach
    @BeforeEach
    public void clean() throws IOException {
        File file = new File(System.getProperty("user.dir") + "/" + "records");
        if (file.isDirectory())
            FileUtils.deleteDirectory(file);
    }

    @Test
    public void testInitialization1() {
        InputStream in = getClass().getClassLoader().getResourceAsStream("level/debugLevel.skb");
        document = new GameDocument(in);
        assertEquals(document.bestRecord.getValue(), 0);
        assertEquals(document.mapSetName, "Game debug!");
        assertEquals(document.getLevelsCount(), 1);
        assertEquals(Whitebox.getInternalState(document, "currentLevel"), ((List<Level>)Whitebox.getInternalState(document, "levels")).get(0));
        assertEquals(document.getPlayer(), document.getCurrentLevel().getPlayerObject());
        document.saveRecord(10, "Test1", 20);
        document.saveRecord(20, "Test2", 15);

        document.reloadMapFromFile(getClass().getClassLoader().getResourceAsStream("level/debugLevel.skb"));;
        assertEquals(document.bestRecord.getValue(), 10);
    }

    @Test
    public void testInitialization2() {
        InputStream in = getClass().getClassLoader().getResourceAsStream("level/SampleGame.skb");
        document = new GameDocument(in);
        assertEquals(document.mapSetName, "Example Game!");
        assertEquals(document.getLevelsCount(), 5);
        assertEquals(Whitebox.getInternalState(document, "currentLevel"), ((List<Level>)Whitebox.getInternalState(document, "levels")).get(0));
        assertEquals(document.getPlayer(), document.getCurrentLevel().getPlayerObject());

        document.saveRecord(10, "Test1", 20);
        document.saveRecord(20, "Test2", 15);

        document.reloadMapFromFile(getClass().getClassLoader().getResourceAsStream("level/SampleGame.skb"));;
        assertEquals(document.bestRecord.getValue(), 10);

    }

    @Test
    public void testSerializeInitialState() throws IllegalMovementException {
        InputStream in = getClass().getClassLoader().getResourceAsStream("level/debugLevel.skb");
        document = new GameDocument(in);
        Level initialLevel = document.getCurrentLevel();
        Player player = document.getPlayer();
        for (int i = 0; i < 7; i++) {
            player.moveBy(new Point(1, 0));
            document.serializeCurrentState();
        }
        document.restoreObject(GameStageSaver.getInitialState());
        assertEquals(document.getCurrentLevel().toString(), initialLevel.toString());
    }

    @Test
    public void testUndo() throws IllegalMovementException {
        InputStream in = getClass().getClassLoader().getResourceAsStream("level/debugLevel.skb");
        document = new GameDocument(in);
        Level initialLevel = document.getCurrentLevel();
        Player player = document.getPlayer();
        for (int i = 0; i < 7; i++) {
            player.moveBy(new Point(1, 0));
            document.serializeCurrentState();
        }
        for (int i = 0; i < 7; i++) {
            document.undo();
        }
        assertEquals(document.getCurrentLevel().toString(), initialLevel.toString());
    }

}
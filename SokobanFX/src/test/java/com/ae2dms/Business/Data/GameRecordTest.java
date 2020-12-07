package com.ae2dms.Business.Data;

import org.apache.maven.shared.utils.io.FileUtils;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.powermock.reflect.Whitebox;

import java.io.File;
import java.io.IOException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class GameRecordTest {
    private GameRecord record;
    String testMapName = "TestName";
    Integer testMapHashCode = testMapName.hashCode();

    @BeforeEach
    public void setUp() {
        record = new GameRecord();
        Whitebox.setInternalState(record, "mapName", testMapName);
        Whitebox.setInternalState(record, "mapHashCode", testMapHashCode);
    }

    @AfterEach
    public void clean() throws IOException {
        File file = new File(System.getProperty("user.dir") + "/" + "records");
        if (file.isDirectory())
            FileUtils.deleteDirectory(file);
    }

    @Test
    void testRestoreRecordsFromEmpty() throws Exception {
        record.restoreRecords(testMapName, testMapHashCode);
        File file = new File(System.getProperty("user.dir") + "/" + "records" + "/" + testMapName + testMapHashCode.toString() + ".rec");
        assertTrue(file.exists());
        List restoredRecords = Whitebox.getInternalState(record, "records");
        assertEquals(restoredRecords.size(), 0);
    }



    @Test
    void testWriteChangesToFileAndRestoreRecords() throws Exception {
        record.restoreRecords(testMapName, testMapHashCode);
        record.pushRecord(20, "Test1", 20);
        record.pushRecord(15, "Test2", 60);
        record.pushRecord(5, "Test3", 10);
        Whitebox.invokeMethod(record, "writeChangesToFile");
        File file = new File(System.getProperty("user.dir") + "/" + "records" + "/" + testMapName + testMapHashCode.toString() + ".rec");
        assertTrue(file.exists());
        record = new GameRecord();
        record.restoreRecords(testMapName, testMapHashCode);
        assertEquals(record.bestRecord.getValue(), 5);
        List restoredRecords = Whitebox.getInternalState(record, "records");
        assertEquals(restoredRecords.size(), 3);
    }

    @Test
    void testSortRecords() {
        record.pushRecord(20, "Test1", 20);
        record.pushRecord(15, "Test2", 60);
        record.pushRecord(60, "Test3", 70);
        record.pushRecord(5, "Test4", 8);
        record.pushRecord(80, "Test5", 100);
        record.pushRecord(5, "Test6", 1);
        record.pushRecord(1, "Test7", 3);
        assertEquals(record.getRecords().get(0).steps, 1);
        assertEquals(record.bestRecord.getValue(), 1);
        assertEquals(record.sortRecordsByTime().get(0).durationSeconds, 1);

    }


}
package com.ae2dms.UI.HighScoreBar;

import com.ae2dms.Business.Data.GameRecord;
import com.ae2dms.Business.GameDocument;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.loadui.testfx.GuiTest;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.loadui.testfx.GuiTest.*;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.reflect.Whitebox;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static com.ae2dms.Business.GameDocument.records;
import static org.assertj.core.util.DateUtil.now;
import static org.junit.jupiter.api.Assertions.*;

class HighScoreBarControllerTest extends GuiTest {

    String testMapName = "TestName";
    Integer testMapHashCode = testMapName.hashCode();

    HighScoreBarController controller;

    VBox mockHighScoreItemVBox;
    VBox mockHighTimeItemVBox;

    @BeforeEach
    public void setUp() {
        controller = new HighScoreBarController();
        mockHighScoreItemVBox = new VBox();
        mockHighTimeItemVBox = new VBox();
    }

    @Test
    public void testRender() {
        GameDocument.records = new GameRecord();
        records.restoreRecords(testMapName, testMapHashCode);
        records.pushRecord(20, "Test1", 20);
        records.pushRecord(15, "Test2", 60);
        records.pushRecord(60, "Test3", 70);
        records.pushRecord(5, "Test4", 8);
        records.pushRecord(80, "Test5", 100);
        records.pushRecord(5, "Test6", 1);
        records.pushRecord(1, "Test7", 3);

        Whitebox.setInternalState(controller, "HighScoreItemVBox", mockHighScoreItemVBox);
        Whitebox.setInternalState(controller, "HighTimeItemVBox", mockHighTimeItemVBox);

        controller.renderRecords();
        int i = 1 +1;

    }

    @Override
    protected Parent getRootNode() {
        Parent parent = null;
        try {
            parent = FXMLLoader.load(getClass().getResource("/ui/FXML/HighScoreBar.fxml"));
            return parent;
        } catch (IOException ex) {
            // TODO ...
        }
        return parent;
    }
}

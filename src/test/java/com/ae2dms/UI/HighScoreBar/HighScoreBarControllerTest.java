package com.ae2dms.UI.HighScoreBar;

import com.ae2dms.Business.Data.GameRecord;
import com.ae2dms.Business.GameDocument;
import javafx.embed.swing.JFXPanel;
import javafx.fxml.FXMLLoader;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import org.apache.maven.shared.utils.io.FileUtils;
import org.junit.jupiter.api.AfterEach;
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
import org.testfx.api.FxAssert;
import org.testfx.framework.junit.ApplicationTest;
import org.testfx.matcher.control.LabeledMatchers;
import org.testfx.service.query.NodeQuery;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static com.ae2dms.Business.GameDocument.records;
import static org.assertj.core.util.DateUtil.now;
import static org.junit.jupiter.api.Assertions.*;

class HighScoreBarControllerTest extends ApplicationTest {

    String testMapName = "TestName";
    Integer testMapHashCode = testMapName.hashCode();

    Pane pane;
    HighScoreBarController controller;

    VBox mockHighScoreItemVBox;
    VBox mockHighTimeItemVBox;

    @BeforeEach
    public void setUp() throws IOException {

        JFXPanel jfxPanel = new JFXPanel();

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/ui/FXML/HighScoreBar.fxml"));
            pane = loader.load();
            controller = loader.getController();
        } catch (IOException e) {
            e.printStackTrace();
        }

        mockHighScoreItemVBox = new VBox();
        mockHighTimeItemVBox = new VBox();

        File file = new File(System.getProperty("user.dir") + "/" + "records");
        if (file.isDirectory())
            FileUtils.deleteDirectory(file);
    }

    @AfterEach
    public void clean() throws IOException {
        File file = new File(System.getProperty("user.dir") + "/" + "records");
        if (file.isDirectory())
            FileUtils.deleteDirectory(file);
    }

    @Test
    // failed, and change the sorting mechanism to Comparator.comparingInt(Record::getDurationSeconds)
    public void testRender1() {
        GameDocument.records = new GameRecord();
        records.restoreRecords(testMapName, testMapHashCode);
        records.pushRecord(20, "Test1", 20);
        records.pushRecord(15, "Test2", 60);
        records.pushRecord(60, "Test3", 70);
        records.pushRecord(5, "Test4", 8);
        records.pushRecord(80, "Test5", 100);
        records.pushRecord(5, "Test6", 1);
        records.pushRecord(2, "Test7", 3);
        records.pushRecord(19, "Test8", 9);
        records.pushRecord(78, "Test9", 7);
        records.pushRecord(62, "Test10", 2);

        Whitebox.setInternalState(controller, "HighScoreItemVBox", mockHighScoreItemVBox);
        Whitebox.setInternalState(controller, "HighTimeItemVBox", mockHighTimeItemVBox);

        controller.renderRecords();
        Group time1 = (Group)mockHighTimeItemVBox.getChildren().get(0);
        Group steps1 = (Group)mockHighScoreItemVBox.getChildren().get(0);
        assertEquals(getNameField(time1), "Test6");
        assertEquals(getDataField(time1), "1");
        assertEquals(getNameField(steps1), "Test7");
        assertEquals(getDataField(steps1), "2");
        assertEquals(mockHighScoreItemVBox.getChildren().size(), 7);
        assertEquals(mockHighTimeItemVBox.getChildren().size(), 7);

    }

    private String getDataField(Group object) {
        return ((Label)((BorderPane)object.getChildren().get(3)).getCenter()).getText();
    }

    private String getNameField(Group object) {
        return ((Label)((BorderPane)object.getChildren().get(2)).getCenter()).getText();
    }
}

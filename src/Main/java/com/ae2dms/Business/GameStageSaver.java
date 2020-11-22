package com.ae2dms.Business;

import com.ae2dms.Main.Main;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.stage.FileChooser;

import javax.sound.sampled.BooleanControl;
import java.io.*;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Date;
import java.util.LinkedList;

public class GameStageSaver {

    private static ObservableList<String> GameDocumentJsonList = FXCollections.observableList(new ArrayList<String>());

    private static String initialGameDocumentState;
    private static final int LIMIT = 15;

    public static BooleanProperty canUndo = new SimpleBooleanProperty(!GameDocumentJsonList.isEmpty());

    static {
        GameDocumentJsonList.addListener((ListChangeListener) change -> canUndo.setValue(!GameDocumentJsonList.isEmpty()));
    }

    public static void clear() {
        GameDocumentJsonList.clear();
        initialGameDocumentState = null;
    }

    public static GameDocument pop() {
        try {
            if (!canUndo.getValue()) {
                return null;
            }
            return (GameDocument) decode(GameDocumentJsonList.remove(GameDocumentJsonList.size() - 1));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static Object decode(String coding) throws IOException, ClassNotFoundException {
        byte[] data = Base64.getDecoder().decode(coding);
        ObjectInputStream ois = new ObjectInputStream(new ByteArrayInputStream(data));
        Object object = ois.readObject();
        ois.close();
        return object;
    }

    public static String encode(Object object) throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ObjectOutputStream oos = new ObjectOutputStream( baos );
        oos.writeObject(object);
        oos.close();
        return Base64.getEncoder().encodeToString(baos.toByteArray());
    }

    public static void pushInitialState(GameDocument object) {
        try {
            initialGameDocumentState = encode(object);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static GameDocument getInitialState() {
        try {
            if (initialGameDocumentState == null) {
                return null;
            }
            return (GameDocument) decode(initialGameDocumentState);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /** Write the object to a Base64 string. */
    public static void push(GameDocument object) {
        try {
            GameDocumentJsonList.add(encode(object));
            if (GameDocumentJsonList.size() > LIMIT) {
                GameDocumentJsonList.remove(0);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void saveToFile(GameDocument object) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Save Current State to file\n You can always reload it to restore the state");
        Date day = new Date();
        String fileName = "[" + object.mapSetName + "]" + day.toString().replace(' ', '_') + ".skbsave";
        fileName = fileName.replace(' ', '_');
        fileChooser.setInitialFileName(fileName);

        //Set extension filter for text files
        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("Save files", "*.skbsave");
        fileChooser.getExtensionFilters().add(extFilter);

        //Show save file dialog
        File file = fileChooser.showSaveDialog(Main.primaryStage);

        if (file != null) {
            try {
                FileWriter writer = new FileWriter(file);
                writer.write(encode(object));
                writer.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}

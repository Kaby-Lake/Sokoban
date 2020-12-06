package com.ae2dms.Business;

import com.ae2dms.Main.Main;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.stage.FileChooser;

import java.io.*;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Date;

import static com.ae2dms.Business.GameDocument.logger;

public class GameStageSaver {

    /**
     * LIMIT of maximum steps that can undo
     */
    private static final int LIMIT = 20;

    private static ObservableList<String> GameDocumentJsonList = FXCollections.observableList(new ArrayList<>(LIMIT));

    private static String initialGameDocumentState;

    /**
     * Used for binding with the View on bottomBar
     */
    public static BooleanProperty canUndo = new SimpleBooleanProperty(!GameDocumentJsonList.isEmpty());

    static {
        GameDocumentJsonList.addListener((ListChangeListener) change -> canUndo.setValue(!GameDocumentJsonList.isEmpty()));
    }

    /**
     * clean the Stack and ready for new game start
     */
    public static void clear() {
        GameDocumentJsonList.clear();
        initialGameDocumentState = null;
    }

    /**
     * Push the object onto a stack with limited capacity, can call pop() to restore it
     * @param object the one to be pushed
     * */
    public static void push(GameDocument object) {
        try {
            if (GameDocumentJsonList.size() >= LIMIT - 1) {
                GameDocumentJsonList.remove(0);
            }
            GameDocumentJsonList.add(encode(object));
        } catch (Exception e) {
            logger.severe(e.getMessage());
        }
    }

    /**
     * pop out the last stage on the stack
     * @return the GameDocument object to restore
     *          will return null if no stages in the Stack
     */
    public static GameDocument pop() {
        try {
            if (!canUndo.getValue()) {
                return null;
            }
            return (GameDocument) decode(GameDocumentJsonList.remove(GameDocumentJsonList.size() - 1));
        } catch (Exception e) {
            logger.severe(e.getMessage());
        }
        return null;
    }

    /**
     * decode the object stored in Serialized Base64 string back to object
     * @param coding coded string
     * @return restored Object
     * @throws IOException
     * @throws ClassNotFoundException
     */
    public static Object decode(String coding) throws IOException, ClassNotFoundException, IllegalArgumentException {
        byte[] data = Base64.getDecoder().decode(coding);
        ObjectInputStream ois = new ObjectInputStream(new ByteArrayInputStream(data));
        Object object = ois.readObject();
        ois.close();
        return object;
    }


    /**
     * @param object encode the Object into a Serialized Base64 string
     * @return the encoded string
     * @throws IOException
     */
    public static String encode(Object object) throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ObjectOutputStream oos = new ObjectOutputStream( baos );
        oos.writeObject(object);
        oos.close();
        return Base64.getEncoder().encodeToString(baos.toByteArray());
    }

    /**
     * should call this everytime load a new map
     * when back to menu and click start again, the initial state will be restored
     * @param object initial state of GameDocument
     */
    public static void pushInitialState(GameDocument object) {
        try {
            initialGameDocumentState = encode(object);
        } catch (Exception e) {
            logger.severe(e.getMessage());
        }
    }

    /**
     * get the initial state of the GameDocument
     * @return the initial GameDocument Object
     */
    public static GameDocument getInitialState() {
        try {
            if (initialGameDocumentState == null) {
                return null;
            }
            return (GameDocument) decode(initialGameDocumentState);
        } catch (Exception e) {
            logger.severe(e.getMessage());
        }
        return null;
    }

    /**
     * Used for saving the current state to File
     * can later load this game state to recover GameDocument
     * @param object the current GameDocument Object
     */
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
                logger.severe(e.getMessage());
            }
        }
    }
}

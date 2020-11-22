package com.ae2dms.Business;

import com.ae2dms.Main.Main;
import javafx.stage.FileChooser;

import java.io.*;
import java.util.Base64;
import java.util.Date;
import java.util.LinkedList;

public class GameStageSaver {
    private static LinkedList<String> GameDocumentJsonList = new LinkedList<>();
    private static int limit = 15;

    /** Read the object from Base64 string. */
    public static boolean isEmpty() {
        return GameDocumentJsonList.isEmpty();
    }

    public static GameDocument pop() {
        try {
            if (isEmpty()) {
                return null;
            }
            String string = GameDocumentJsonList.pop();
            byte[] data = Base64.getDecoder().decode(string);
            ObjectInputStream ois = new ObjectInputStream(new ByteArrayInputStream(data));
            GameDocument object = (GameDocument) ois.readObject();
            ois.close();
            return object;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /** Write the object to a Base64 string. */
    public static void push(GameDocument object) {
        try {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ObjectOutputStream oos = new ObjectOutputStream( baos );
            oos.writeObject(object);
            oos.close();
            GameDocumentJsonList.push(Base64.getEncoder().encodeToString(baos.toByteArray()));
            if (GameDocumentJsonList.size() > 15) {
                GameDocumentJsonList.removeLast();
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
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                ObjectOutputStream oos = new ObjectOutputStream( baos );
                oos.writeObject(object);
                oos.close();

                FileWriter writer = new FileWriter(file);
                writer.write(Base64.getEncoder().encodeToString(baos.toByteArray()));
                writer.close();

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}

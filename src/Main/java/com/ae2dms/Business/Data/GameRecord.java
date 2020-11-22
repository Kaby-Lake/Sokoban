package com.ae2dms.Business.Data;

import com.ae2dms.Business.GameDocument;
import com.ae2dms.Business.GameStageSaver;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import org.apache.commons.io.FileUtils;

import java.io.*;
import java.util.*;
import java.util.logging.FileHandler;

public class GameRecord implements Serializable {

    public transient IntegerProperty highestScore = new SimpleIntegerProperty(0);

    private int highestScoreSerializable;

    private final ArrayList<Record> records = new ArrayList<>();

    // this will set the date to be now
    public void pushRecord(int score, String playerName, int durationSeconds) {
        records.add(new Record(score, playerName, durationSeconds));
        sortRecords();
        if (score > highestScore.getValue()) {
            highestScore.set(score);
        }
    }

    public void pushRecord(int score, Date date, String playerName, int durationSeconds) {
        records.add(new Record(score, date, playerName, durationSeconds));
        sortRecords();
        if (score > highestScore.getValue()) {
            highestScore.set(score);
        }
    }

    private void sortRecords() {
        Collections.sort(records);
    }

    private ArrayList<Record> getRecords() {
        return records;
    }

    public void readInRecords(String mapName, Integer mapHashCode) {

        File directory = new File(System.getProperty("user.dir") + "/" + "records");
        if (!directory.exists()) {
            directory.mkdirs();
        }
        String fileName = directory + "/" + mapName + mapHashCode.toString() + ".rec";
        try {
            File file = new File(fileName);
            if (!file.exists()) {
                file.createNewFile();
                return;
            }
            String recordsObjectEncode = FileUtils.readFileToString(file);
            GameRecord object = (GameRecord)GameStageSaver.decode(recordsObjectEncode);
            for (Record record : object.getRecords()) {
                this.pushRecord(record.score, record.date, record.playerName, record.durationSeconds);
            }
        } catch (EOFException e) {
            return;
        } catch (IOException | ClassNotFoundException e){
            e.printStackTrace();
        }

    }



    // inner class
    private class Record implements Serializable, Comparable<Record> {

        private final UUID uuid = UUID.randomUUID();

        Integer score;
        Date date;
        String playerName;
        Integer durationSeconds;

        public Record(int score, String playerName, int durationSeconds) {
            Date date = new Date();
            this.score = score;
            this.date = date;
            this.playerName = playerName;
            this.durationSeconds = durationSeconds;
        }

        public Record(int score, Date date, String playerName, int durationSeconds) {
            this.score = score;
            this.date = date;
            this.playerName = playerName;
            this.durationSeconds = durationSeconds;
        }


        @Override
        public int compareTo(Record o) {
            return this.score.compareTo(o.score);
        }
    }
}

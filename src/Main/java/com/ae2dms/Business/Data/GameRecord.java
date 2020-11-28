package com.ae2dms.Business.Data;

import com.ae2dms.Business.GameStageSaver;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import org.apache.commons.io.FileUtils;

import java.io.EOFException;
import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.util.*;

public class GameRecord implements Serializable {

    public transient IntegerProperty highestScore = new SimpleIntegerProperty(0);

    // only used when to serialize
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

    public List<Record> sortRecordsByTime() {

        ArrayList<Record> recordsSortedByTime = new ArrayList<>(this.records);

        recordsSortedByTime.sort((record1, record2) -> {
            // TODO Auto-generated method stub
            if (record1.getDurationSeconds() >= record2.getDurationSeconds()) {
                return 1;
            } else {
                return -1;
            }
        });

        return recordsSortedByTime;
    }


    public ArrayList<Record> getRecords() {
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
            this.records.clear();
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
    public class Record implements Serializable, Comparable<Record> {

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

        public int getDurationSeconds() {
            return durationSeconds;
        }

        public String getName() {
            return playerName;
        }

        public int getScore() {
            return score;
        }


        @Override
        public int compareTo(Record o) {
            return this.score.compareTo(o.score);
        }
    }
}

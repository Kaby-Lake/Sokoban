package com.ae2dms.Business.Data;

import com.ae2dms.Business.GameStageSaver;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;

import java.io.*;
import java.util.*;

public class GameRecord implements Serializable {

    private String mapName;
    private Integer mapHashCode;

    public transient IntegerProperty highestScore = new SimpleIntegerProperty(0);

    private ArrayList<Record> records = new ArrayList<>();

    // this will set the date to be now
    public void pushRecord(int score, String playerName, int durationSeconds) {
        records.add(new Record(score, playerName, durationSeconds));
        sortRecords();
        writeChangesToFile();
        if (score > highestScore.getValue()) {
            highestScore.set(score);
        }
    }

    public void pushRecord(int score, Date date, String playerName, int durationSeconds) {
        records.add(new Record(score, date, playerName, durationSeconds));
        sortRecords();
        writeChangesToFile();
        if (score > highestScore.getValue()) {
            highestScore.set(score);
        }
    }

    private void writeChangesToFile() {
        File directory = new File(System.getProperty("user.dir") + "/" + "records");
        String fileName = directory + "/" + mapName + mapHashCode.toString() + ".rec";
        try {
            File file = new File(fileName);
            PrintWriter out = new PrintWriter(file);
            out.print(GameStageSaver.encode(this));
            out.close();
        } catch (IOException e){
            e.printStackTrace();
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

    public void restoreRecords(String mapName, Integer mapHashCode) {
        this.mapName = mapName;
        this.mapHashCode = mapHashCode;

        this.highestScore.set(0);
        records = new ArrayList<>();

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

            Scanner in = new Scanner(file);
            if (!in.hasNextLine()) {
                return;
            }
            String recordsObjectEncode = in.nextLine();
            in.close();
            GameRecord object = (GameRecord)GameStageSaver.decode(recordsObjectEncode);
            for (Record record : object.getRecords()) {
                records.add(new Record(record.score, record.date, record.playerName, record.durationSeconds));
                sortRecords();
                if (record.score > highestScore.getValue()) {
                    highestScore.set(record.score);
                }
            }
        } catch (EOFException e) {
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

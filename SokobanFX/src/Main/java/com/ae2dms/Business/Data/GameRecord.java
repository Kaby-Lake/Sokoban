package com.ae2dms.Business.Data;

import com.ae2dms.Business.GameStageSaver;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;

import java.io.*;
import java.util.*;

import static com.ae2dms.Business.GameDocument.logger;

/**
 * The Game record class to store all records
 * has inner class named Record to store single record
 * @see Record
 */
public class GameRecord implements Serializable {

    /**
     * MapSet Name of this record, used for data retrieving from records savefile
     */
    private String mapName;

    /**
     * the hashcode generated by every unique mapSet, used for verifying that the reading saveFile is valid
     */
    private Integer mapHashCode;

    /**
     * the best record (least steps) of this map, initialized to MAX_VALUE in the beginning
     */
    public transient IntegerProperty bestRecord = new SimpleIntegerProperty(Integer.MAX_VALUE);

    /**
     * The ArrayList to store all records, sorted by steps in ascending order
     */
    private ArrayList<Record> records = new ArrayList<>();


    /**
     * create a record and push to records list, will sort and update @bestRecord accordingly
     * will set the record adding time to now()
     *
     * @param steps           the total moved steps of this turn
     * @param playerName      the inputted name by user when game complete
     * @param durationSeconds the duration seconds that timer recorded
     */
    public void pushRecord(int steps, String playerName, int durationSeconds) {
        this.pushRecord(steps, new Date(), playerName, durationSeconds);
    }

    /**
     * create a record and push to records list, will sort and update @bestRecord accordingly
     * will set the record adding time to the given one
     *
     * @param steps the total moved steps of this turn
     * @param playerName the inputted name by user when game complete
     * @param durationSeconds the duration seconds that timer recorded
     */
    private void pushRecord(int steps, Date date, String playerName, int durationSeconds) {
        records.add(new Record(steps, date, playerName, durationSeconds));
        sortRecords();
        writeChangesToFile();
        if (steps < bestRecord.getValue()) {
            bestRecord.set(steps);
        }
    }

    /**
     * write the changes make by pushing records to the defined output file
     * the filepath is namely $(user.dir)/records/$(mapName)$(mapHashcode).rec
     */
    private void writeChangesToFile() {
        File directory = new File(System.getProperty("user.dir") + "/" + "records");
        String fileName = directory + "/" + mapName + mapHashCode.toString() + ".rec";
        try {
            File file = new File(fileName);
            PrintWriter out = new PrintWriter(file);
            out.print(GameStageSaver.encode(this));
            out.close();
        } catch (IOException e){
            logger.severe(e.getMessage());
        }
    }


    /**
     * sort the records by steps in ascending order
     */
    private void sortRecords() {
        Collections.sort(records);
    }

    /**
     * Produce a List of Records, sorted by time duration of records in ascending order
     *
     * @return a unmodifiable list of sorted list
     */
    public List<Record> sortRecordsByTime() {

        ArrayList<Record> recordsSortedByTime = new ArrayList<>(this.records);

        recordsSortedByTime.sort(Comparator.comparingInt(Record::getDurationSeconds));

        return Collections.unmodifiableList(recordsSortedByTime);
    }


    /**
     * Get a unmodifiableList of all records, sorted by steps in ascending order
     *
     * @return getter of records
     */
    public List<Record> getRecords() {
        return Collections.unmodifiableList(records);
    }

    /**
     * to read permanent record from the $(user.dir)/records/$(mapName)$(mapHashcode).rec
     * if not found, then create the directory or file
     *
     * @param mapName     name of the mapSet
     * @param mapHashCode hashcode of the mapSet, for verify the map in case maps with same name but different content
     */
    public void restoreRecords(String mapName, Integer mapHashCode) {
        this.mapName = mapName;
        this.mapHashCode = mapHashCode;

        this.bestRecord.set(Integer.MAX_VALUE);
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
                this.pushRecord(record.steps, record.date, record.playerName, record.durationSeconds);
            }
        } catch (EOFException e) {
        } catch (IOException | ClassNotFoundException e){
            logger.severe(e.getMessage());
        }
    }


    /**
     * Inner Record class, representing the data of every record
     */
    public class Record implements Serializable, Comparable<Record> {

        private final UUID uuid = UUID.randomUUID();

        /**
         * The Steps.
         */
        Integer steps;
        /**
         * The Date.
         */
        Date date;
        /**
         * The Player name.
         */
        String playerName;
        /**
         * The Duration seconds.
         */
        Integer durationSeconds;

        /**
         * Instantiates a new Record.
         *
         * @param score           the score
         * @param date            the date
         * @param playerName      the player name
         * @param durationSeconds the duration seconds
         */
        public Record(int score, Date date, String playerName, int durationSeconds) {
            this.steps = score;
            this.date = date;
            this.playerName = playerName;
            this.durationSeconds = durationSeconds;
        }

        /**
         * Gets duration seconds.
         *
         * @return getter of durationSeconds
         */
        public int getDurationSeconds() {
            return durationSeconds;
        }

        /**
         * Gets name.
         *
         * @return getter of playerName
         */
        public String getName() {
            return playerName;
        }

        /**
         * Gets steps.
         *
         * @return getter of score
         */
        public int getSteps() {
            return steps;
        }

        @Override
        public int compareTo(Record o) {
            return (this.steps.compareTo(o.steps));
        }
    }
}

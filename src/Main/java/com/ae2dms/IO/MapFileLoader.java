package com.ae2dms.IO;

import com.ae2dms.Business.Data.Level;

import java.io.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import static com.ae2dms.Business.GameDocument.logger;

/**
 * helper class to load map file into game Document
 * can detect which line is incorrect in the sysout
 */
public class MapFileLoader {
    private final ArrayList<Level> levels;
    private String mapSetName;
    private String rawMapFile;

    public MapFileLoader() {
        levels = new ArrayList<>(5);
    }

    /**
     * @return an unmodifiable list of levels
     */
    public List<Level> getLevels() {
        return Collections.unmodifiableList(this.levels);
    }

    public String getMapSetName() {
        return this.mapSetName;
    }

    /**
     * load the map file from input stream
     * @param input map file input stream
     * @return if this is a valid map file
     * @throws IOException
     * @throws NullPointerException
     */
    public boolean loadMapFile(InputStream input) throws IOException, NullPointerException {


        rawMapFile = new BufferedReader(new InputStreamReader(input)).lines().collect(Collectors.joining(System.lineSeparator()));

        if (!validMap(rawMapFile)) {
            return false;
        }

        BufferedReader reader = new BufferedReader(new StringReader(rawMapFile));
        boolean firstLevelIsParsed = false;
        List<String> rawLevel = new ArrayList<>();
        String levelName = "";

        int levelIndex = 0;
        while(true) {
                String line = reader.readLine();

                // this line means meets the end of the file
                if (line == null) {
                    if (rawLevel.size() != 0) {
                        Level parsedLevel = new Level(levelName, ++levelIndex, rawLevel);
                        levels.add(parsedLevel);
                    }
                    break;
                }

                if (line.contains("MapSetName")) {
                    mapSetName = line.replace("MapSetName: ", "");
                    continue;
                }

                if (line.contains("LevelName")) {
                    if (firstLevelIsParsed) {
                        Level parsedLevel = new Level(levelName, ++levelIndex, rawLevel);
                        levels.add(parsedLevel);
                        rawLevel.clear();
                    } else {
                        firstLevelIsParsed = true;
                    }

                    levelName = line.replace("LevelName: ", "");
                    continue;
                }

                line = line.trim();
                line = line.toUpperCase();
                if (line.matches(".*W.*W.*")) {
                    rawLevel.add(line);
                }
            }
        return true;
    }

    /**
     * detect whether this is a valid map or not
     * if not, will show in detail which line is incorrect in sysout
     * @param rawMap the map in war string
     * @return boolean whether is valid
     */
    private boolean validMap(String rawMap) {
        ModifiedBufferReader reader = new ModifiedBufferReader(new StringReader(rawMap));
        try {
            String firstLine = reader.readLineAndAddPointer();
            if (!firstLine.matches("MapSetName: .+")) {
                logLoadMapFailureWithReason(reader.getPointer(), "FirstLine does not match \"MapSetName: $Name\"");
                return false;
            }
            boolean atLeastOneLevel = false;
            outer:
            while (true) {
                String line = reader.readLineAndAddPointer();
                if ("".equals(line)) break;
                if (line.contains("LevelName: ")) {
                    int mapLineCount = 0;
                    if (!line.matches("LevelName: .+")) {
                        logLoadMapFailureWithReason(reader.getPointer(), "The Start of Every Level does not match \"LevelName: $Name\"");
                        return false;
                    }
                    while (true) {
                        String mapLine = reader.readLineAndAddPointer();
                        if (mapLine == null) {
                            atLeastOneLevel = true;
                            if (mapLineCount == 0) {
                                logLoadMapFailureWithReason(reader.getPointer(), "Every Level should have at least one line");
                                return false;
                            }
                            if (mapLineCount > 20) {
                                logLoadMapFailureWithReason(reader.getPointer(), "Every Level should at mose have 20 line");
                                return false;
                            }
                            break outer;
                        };
                        if ("".equals(mapLine)) break;
                        if (!mapLine.matches("^[Ww][WwCcDdSsYy ]{18}([Ww] ?)$")) {
                            logLoadMapFailureWithReason(reader.getPointer(), "This line does not match the format of Map");
                            return false;
                        }
                        mapLineCount++;
                    }
                    atLeastOneLevel = true;
                    if (mapLineCount == 0) {
                        logLoadMapFailureWithReason(reader.getPointer(), "Every Level should have at least one line");
                        return false;
                    }
                    if (mapLineCount > 20) {
                        logLoadMapFailureWithReason(reader.getPointer(), "Every Level should at mose have 20 line");
                        return false;
                    }
                } else {
                    logLoadMapFailureWithReason(reader.getPointer(), "The Start of Every Level does not match \"LevelName: $Name\"");
                    return false;
                }
            }
            if (!atLeastOneLevel) {
                logLoadMapFailureWithReason(reader.getPointer(), "Every MapSet should have at least 1 Level");
                return false;
            }

        } catch (Exception e) {
            return false;
        }
        return true;
    }

    public int getMapHashCode() {
        return rawMapFile.hashCode();
    }

    private void logLoadMapFailureWithReason(int pointer, String message) {
        logger.info("Error in Line 👉 " + pointer + " " + message);
    }

    public static class ErrorMapFileLoadException extends Throwable {
    }

    public static class ErrorSaveFileLoadException extends Throwable {
    }

}

class ModifiedBufferReader extends BufferedReader {
    int pointer = 0;

    public ModifiedBufferReader(Reader in) {
        super(in);
    }

    public String readLineAndAddPointer() throws IOException {
        this.pointer++;
        return readLine();
    }

    public int getPointer() {
        return pointer;
    }
}




package com.ae2dms.IO;

import com.ae2dms.Business.Data.Level;
import org.apache.commons.io.IOUtils;

import java.io.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MapFileLoader {
    private final ArrayList<Level> levels;
    private String mapSetName;
    private String rawMapFile;

    public MapFileLoader() {
        levels = new ArrayList<>(5);
    }

    // return an unmodifiable list
    public List<Level> getLevels() {
        return Collections.unmodifiableList(this.levels);
    }

    public String getMapSetName() {
        return this.mapSetName;
    }

    public void loadMapFile(InputStream input) throws IOException, NullPointerException {

        rawMapFile = IOUtils.toString(input, "utf-8");

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

    }

    public int getMapHashCode() {
        return rawMapFile.hashCode();
    }
}

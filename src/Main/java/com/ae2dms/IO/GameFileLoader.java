package com.ae2dms.IO;

import com.ae2dms.Business.Data.Level;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class GameFileLoader {
    private final ArrayList<Level> levels;
    private String mapSetName;

    public GameFileLoader() {
        levels = new ArrayList<>(5);
    }

    // return an unmodifiable list
    public List<Level> getLevels() {
        return Collections.unmodifiableList(this.levels);
    }

    public String getMapSetName() {
        return this.mapSetName;
    }

    public void loadGameFile(InputStream input) throws IOException, NullPointerException {

        BufferedReader reader = new BufferedReader(new InputStreamReader(input));
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
}

package com.ae2dms.Business;

import java.io.File;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.logging.FileHandler;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

public class GameLogger extends Logger {

    private static Logger logger = Logger.getLogger("GameLogger");
    private DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
    private Calendar calendar = Calendar.getInstance();

    public GameLogger() {
        super("com.aes2dms.sokoban", null);

        File directory = new File(System.getProperty("user.dir") + "/" + "logs");
        directory.mkdirs();

        FileHandler fileHandler = null;
        try {
            fileHandler = new FileHandler(directory + "/" + GameDocument.GAME_NAME + ".log");
        } catch (IOException e) {
            e.printStackTrace();
        }
        logger.addHandler(fileHandler);
        SimpleFormatter formatter = new SimpleFormatter();
        fileHandler.setFormatter(formatter);
    }

    private String createFormattedMessage(String message) {
        return dateFormat.format(calendar.getTime()) + " -- " + message;
    }

    @Override
    public void info(String message) {
        String log = createFormattedMessage(message);
        logger.info(log);
        System.out.println(log);
    }

    @Override
    public void warning(String message) {
        String log = createFormattedMessage(message);
        logger.warning(log);
        System.out.println(log);
    }

    @Override
    public void severe(String message) {
        String log = createFormattedMessage(message);
        logger.severe(log);
        System.out.println(log);
    }
}
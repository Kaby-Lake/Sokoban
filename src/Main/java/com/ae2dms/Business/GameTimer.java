package com.ae2dms.Business;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.util.Duration;

/**
 * Timer Util to count the time of the game
 */
public class GameTimer {

    /**
     * the timer to count the time, use JavaFX Timeline as to reduce multi-threading problems
     */
    private Timeline timer;

    /**
     * second duration of this count
     */
    private int seconds = 0;

    /**
     * The StringProperty to be displayed on the top-right of the screen
     */
    public StringProperty timeToDisplay = new SimpleStringProperty("00:00");

    /**
     * This will create a new Timer, which add 1 to $seconds every minute
     * and update $timeToDisplay for binding with view
     */
    public void start() {
        seconds = 0;
        timer = new Timeline(new KeyFrame(Duration.millis(1000), event -> timeToDisplay.set(parseToTimeFormat(++seconds))));
        timer.setCycleCount(Animation.INDEFINITE);
        timer.play();
    }

    /**
     * called when want to stop the timer, the $seconds and $timeToDisplay will still
     */
    public void stop(){
        if (timer != null) {
            timer.stop();
        }
    }

    /**
     * getter of time duration
     * @return time used in seconds
     */
    public int getTime() {
        return seconds;
    }

    /**
     * called to pause a timer
     */
    public void pause() {
        timer.pause();
    }

    /**
     * called to resume a timer
     */
    public void resume() {
        timer.play();
    }

    /**
     * static function
     * convert the time in seconds to ??:??
     * @param seconds the seconds to convert
     * @return a formatted string like ??:??
     */
    public static String parseToTimeFormat(int seconds) {
        Integer minute = seconds / 60;
        String minuteString = "0" + minute.toString();
        Integer second = seconds % 60;
        String secondString = "0" + second.toString();

        if (minuteString.length() != 2) {
            minuteString = minuteString.substring(minuteString.length() - 2);
        }

        if (secondString.length() != 2) {
            secondString = secondString.substring(secondString.length() - 2);
        }
        return minuteString + ":" + secondString;
    }
}


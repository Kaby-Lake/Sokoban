package com.ae2dms.UI;

import com.ae2dms.IO.ResourceFactory;
import com.ae2dms.IO.ResourceType;
import javafx.application.Platform;
import javafx.beans.InvalidationListener;
import javafx.beans.Observable;
import javafx.beans.property.*;
import javafx.beans.value.ChangeListener;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Slider;
import javafx.scene.media.AudioClip;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.util.Duration;

import java.util.HashMap;
import java.util.Random;

public class GameMediaPlayer {
    private static volatile GameMediaPlayer instance;

    public MediaPlayer backgroundMusicPlayer;

    public int duration;

    public StringProperty nowPlaying = new SimpleStringProperty();

    public ObservableList<String> bgmList;

    public HashMap<String, Integer> bgmDuration = new HashMap<>();

    public HashMap<String, AudioClip> soundEffects = new HashMap<>();

    public DoubleProperty MusicVolume = new SimpleDoubleProperty(80);

    public DoubleProperty SFXVolume = new SimpleDoubleProperty(80);

    private SoundPreferenceController controller;

    private GameMediaPlayer() {

        soundEffects.put("UNMOVABLE_AUDIO_CLIP_MEDIA", (AudioClip)ResourceFactory.getResource("UNMOVABLE_AUDIO_CLIP", ResourceType.AudioClip));
        soundEffects.put("MOVE_AUDIO_CLIP_MEDIA", (AudioClip)ResourceFactory.getResource("MOVE_AUDIO_CLIP", ResourceType.AudioClip));
        soundEffects.put("MOVE_CRATE_AUDIO_CLIP_MEDIA", (AudioClip)ResourceFactory.getResource("MOVE_CRATE_AUDIO_CLIP", ResourceType.AudioClip));
        soundEffects.put("LEVEL_COMPLETE_AUDIO_CLIP", (AudioClip)ResourceFactory.getResource("LEVEL_COMPLETE_AUDIO_CLIP",ResourceType.AudioClip));

        bgmList = FXCollections.observableArrayList(
                "PaperClip-Blip",
                "PaperClip-BOUNCE",
                "PaperClip-DEMONS",
                "PaperClip-Distant",
                "PaperClip-Heeyha",
                "PaperClip-Jumping",
                "PaperClip-KINGSTON",
                "PaperClip-Lust",
                "PaperClip-Heat Up",
                "PETO-Okay");

        bgmDuration.put("PaperClip-Blip", 235);
        bgmDuration.put("PaperClip-BOUNCE", 215);
        bgmDuration.put("PaperClip-DEMONS", 196);
        bgmDuration.put("PaperClip-Distant", 328);
        bgmDuration.put("PaperClip-Heeyha", 129);
        bgmDuration.put("PaperClip-Jumping", 124);
        bgmDuration.put("PaperClip-KINGSTON", 240);
        bgmDuration.put("PaperClip-Lust", 199);
        bgmDuration.put("PaperClip-Heat Up", 171);
        bgmDuration.put("PETO-Okay", 345);


        MusicVolume.addListener((observable, oldValue, newValue) -> {
            if (observable != null) {
                this.backgroundMusicPlayer.setVolume((double) observable.getValue() / 100);
            }
        });

        SFXVolume.addListener((observable, oldValue, newValue) -> {
            if (observable != null ) {
                for (AudioClip mediaPlayer : soundEffects.values()) {
                    mediaPlayer.setVolume((double) observable.getValue() / 100);
                }
            }
        });
    }

    public static synchronized GameMediaPlayer getInstance()
    {
        if(instance == null) {
            instance = new GameMediaPlayer();
        }
        return instance;
    }

    public void play(String musicName) {

        Media mediaPlaying = (Media)ResourceFactory.getResource(musicName, ResourceType.Media);
        backgroundMusicPlayer = new MediaPlayer(mediaPlaying);
        this.nowPlaying.setValue(musicName);
        this.setMusic(MediaState.PLAY);

        backgroundMusicPlayer.setOnEndOfMedia(() -> {
            this.setMusic(MediaState.STOP);
            this.setMusic(MediaState.PLAY);
        });
    }

    public void play() {
        String songName = getRandomBackgroundMusicName();
        duration = bgmDuration.get(songName);
        Media mediaPlaying = (Media)ResourceFactory.getResource(songName, ResourceType.Media);
        backgroundMusicPlayer = new MediaPlayer(mediaPlaying);
        this.nowPlaying.setValue(songName);
        this.setMusic(MediaState.PLAY);

        backgroundMusicPlayer.setOnEndOfMedia(() -> {
            this.setMusic(MediaState.STOP);
            this.setMusic(MediaState.PLAY);
        });
    }

    private String getRandomBackgroundMusicName() {
        Random random = new Random();
        return bgmList.get(random.nextInt(bgmList.size()));
    }

    public void setMusic(MediaState state) {
        switch (state) {
            case PLAY -> {
                backgroundMusicPlayer.currentTimeProperty().addListener(listener);
                backgroundMusicPlayer.play();
            }
            case PAUSE -> backgroundMusicPlayer.pause();
            case STOP -> {
                backgroundMusicPlayer.stop();
                String songName = getRandomBackgroundMusicName();
                Media mediaPlaying = (Media)ResourceFactory.getResource(songName, ResourceType.Media);
                backgroundMusicPlayer = new MediaPlayer(mediaPlaying);
                backgroundMusicPlayer.currentTimeProperty().removeListener(listener);
                this.nowPlaying.setValue(songName);duration = bgmDuration.get(songName);
                duration = bgmDuration.get(songName);
            }
        }
    }

    private ChangeListener listener = (observable, oldValue, newValue) -> {
        controller.updatesDurationSlider();
    };

    public void bindSoundPreferencesController(SoundPreferenceController controller) {
        this.controller = controller;
    }

}

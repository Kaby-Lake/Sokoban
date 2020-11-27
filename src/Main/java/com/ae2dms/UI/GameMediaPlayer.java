package com.ae2dms.UI;

import com.ae2dms.IO.ResourceFactory;
import com.ae2dms.IO.ResourceType;
import javafx.beans.property.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.media.AudioClip;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.util.Duration;

import java.util.HashMap;
import java.util.Random;

public class GameMediaPlayer {
    private static volatile GameMediaPlayer instance;

    private MediaPlayer backgroundMusicPlayer;

    public DoubleProperty MusicProgress = new SimpleDoubleProperty(0);

    public StringProperty nowPlaying = new SimpleStringProperty();

    public ObservableList<String> bgmList;

    public HashMap<String, AudioClip> soundEffects = new HashMap<>();

    public DoubleProperty MusicVolume = new SimpleDoubleProperty(80);

    public DoubleProperty SFXVolume = new SimpleDoubleProperty(80);

    private GameMediaPlayer() {

        soundEffects.put("UNMOVABLE_AUDIO_CLIP_MEDIA", (AudioClip)ResourceFactory.getResource("UNMOVABLE_AUDIO_CLIP", ResourceType.AudioClip));
        soundEffects.put("MOVE_AUDIO_CLIP_MEDIA", (AudioClip)ResourceFactory.getResource("MOVE_AUDIO_CLIP", ResourceType.AudioClip));
        soundEffects.put("MOVE_CRATE_AUDIO_CLIP_MEDIA", (AudioClip)ResourceFactory.getResource("MOVE_CRATE_AUDIO_CLIP", ResourceType.AudioClip));
        soundEffects.put("LEVEL_COMPLETE_AUDIO_CLIP", (AudioClip)ResourceFactory.getResource("LEVEL_COMPLETE_AUDIO_CLIP",ResourceType.AudioClip));

        bgmList = FXCollections.observableArrayList("PaperClip-Blip",
                "PaperClip-BOUNCE",
                "PaperClip-DEMONS",
                "PaperClip-Distant",
                "PaperClip-Heeyha",
                "PaperClip-Jumping",
                "PaperClip-KINGSTON",
                "PaperClip-Lust",
                "PaperClip-Heat Up",
                "PETO-Okay");

        MusicVolume.addListener((observable, oldValue, newValue) -> {
            if (observable != null) {
                this.backgroundMusicPlayer.setVolume((double) observable.getValue());
            }
        });

        SFXVolume.addListener((observable, oldValue, newValue) -> {
            if (observable != null ) {
                for (AudioClip mediaPlayer : soundEffects.values()) {
                    mediaPlayer.setVolume((double) observable.getValue());
                }
            }
        });

        MusicProgress.addListener((observable, oldValue, newValue) -> {
            if (observable != null) {
                Duration totalTime = backgroundMusicPlayer.getTotalDuration();
                Duration startTime = totalTime.multiply((double) observable.getValue());
                backgroundMusicPlayer.stop();
                backgroundMusicPlayer.setStartTime(startTime);
                this.setMusic(MediaState.PLAY);
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
        backgroundMusicPlayer = new MediaPlayer((Media)ResourceFactory.getResource(musicName, ResourceType.Media));
        this.nowPlaying.setValue(musicName);
        this.setMusic(MediaState.PLAY);

        backgroundMusicPlayer.setOnEndOfMedia(() -> {
            String newSongName = getRandomBackgroundMusicName();
            this.nowPlaying.setValue(newSongName);
            backgroundMusicPlayer = new MediaPlayer((Media)ResourceFactory.getResource(newSongName, ResourceType.Media));
            this.setMusic(MediaState.PLAY);
        });
    }

    public void play() {
        String songName = getRandomBackgroundMusicName();
        backgroundMusicPlayer = new MediaPlayer((Media)ResourceFactory.getResource(songName, ResourceType.Media));
        this.nowPlaying.setValue(songName);
        this.setMusic(MediaState.PLAY);

        backgroundMusicPlayer.setOnEndOfMedia(() -> {
            String newSongName = getRandomBackgroundMusicName();
            this.nowPlaying.setValue(newSongName);
            backgroundMusicPlayer = new MediaPlayer((Media)ResourceFactory.getResource(newSongName, ResourceType.Media));
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
                backgroundMusicPlayer.play();
                bindDurationSlider();
            }
            case PAUSE -> backgroundMusicPlayer.pause();
            case STOP -> {
                backgroundMusicPlayer.stop();
                String newSongName = getRandomBackgroundMusicName();
                this.nowPlaying.setValue(newSongName);
                backgroundMusicPlayer = new MediaPlayer((Media)ResourceFactory.getResource(newSongName, ResourceType.Media));
            }
        }
    }

    private void bindDurationSlider() {
        backgroundMusicPlayer.startTimeProperty().addListener((observable, oldValue, newValue) -> {
            if (observable != null) {
                Duration totalTime = backgroundMusicPlayer.getTotalDuration();
                Duration thisTime = observable.getValue();
                this.MusicProgress.setValue(thisTime.toSeconds() % totalTime.toSeconds());
            }
        });
    }

}

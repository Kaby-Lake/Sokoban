package com.ae2dms.UI;

import com.ae2dms.IO.ResourceFactory;
import com.ae2dms.IO.ResourceType;
import com.ae2dms.Main.Main;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.beans.value.ChangeListener;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.media.AudioClip;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

import java.util.HashMap;
import java.util.Random;

/**
 * singleton MediaPlayer to player music
 */
public class GameMediaPlayer {

    /**
     * The GameMediaPlayer instance
     */
    private static volatile GameMediaPlayer instance;

    /**
     * The JavaFX MediaPlayer
     * @see MediaPlayer
     */
    public MediaPlayer backgroundMusicPlayer;

    /**
     * total duration of the music playing
     */
    public int duration;

    /**
     * The name of the music playing
     */
    public StringProperty nowPlaying = new SimpleStringProperty();

    /**
     * a list of music that can be chosen, will be bind to view
     */
    public ObservableList<String> bgmList;

    /**
     * The hashMap to store the duration of the songs
     * with (Song Name, Duration) pair
     */
    public HashMap<String, Integer> bgmDuration = new HashMap<>();

    /**
     * The hashmap to store all sound effects, can get the AudioClip by searching the name
     * with (SFX Name, AudioClip) pair
     */
    public HashMap<String, AudioClip> soundEffects = new HashMap<>();

    /**
     * The music volume of the MediaPlayer, bind to sound volume in Main and in Controller and in View
     */
    public DoubleProperty MusicVolume = new SimpleDoubleProperty(80);

    /**
     * The SFX volume of the AudioCLip, bind to SFX volume in Main and in Controller and in View
     */
    public DoubleProperty SFXVolume = new SimpleDoubleProperty(80);

    /**
     * The controller of SoundPreference view
     */
    private SoundPreferenceController controller;

    /**
     * private constructor, can only be initialized by getInstance in the inside
     */
    private GameMediaPlayer() {

        soundEffects.put("UNMOVABLE_AUDIO_CLIP_MEDIA", (AudioClip)ResourceFactory.getResource("UNMOVABLE_AUDIO_CLIP", ResourceType.AudioClip));
        soundEffects.put("MOVE_AUDIO_CLIP_MEDIA", (AudioClip)ResourceFactory.getResource("MOVE_AUDIO_CLIP", ResourceType.AudioClip));
        soundEffects.put("MOVE_CRATE_AUDIO_CLIP_MEDIA", (AudioClip)ResourceFactory.getResource("MOVE_CRATE_AUDIO_CLIP", ResourceType.AudioClip));
        soundEffects.put("LEVEL_COMPLETE_AUDIO_CLIP", (AudioClip)ResourceFactory.getResource("LEVEL_COMPLETE_AUDIO_CLIP",ResourceType.AudioClip));
        soundEffects.put("GAME_COMPLETE_AUDIO_CLIP", (AudioClip)ResourceFactory.getResource("GAME_COMPLETE_AUDIO_CLIP",ResourceType.AudioClip));

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
                "PaperClip-Bricks",
                "PaperClip-GOOD SHOW",
                "PaperClip-Last Time",
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
        bgmDuration.put("PaperClip-Bricks", 184);
        bgmDuration.put("PaperClip-GOOD SHOW", 339);
        bgmDuration.put("PaperClip-Last Time", 250);
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

    /**
     * get the only GameMediaPlayer instance
     * @return the only GameMediaPlayer instance
     */
    public static synchronized GameMediaPlayer getInstance()
    {
        if(instance == null) {
            instance = new GameMediaPlayer();
        }
        return instance;
    }

    /**
     * play the specified musicName in the bgmList;
     * @param musicName the musicName to play, must be in the bgmList
     */
    public void play(String musicName) {

        duration = bgmDuration.get(musicName);
        Media mediaPlaying = (Media)ResourceFactory.getResource(musicName, ResourceType.Media);
        backgroundMusicPlayer = new MediaPlayer(mediaPlaying);
        backgroundMusicPlayer.setVolume(Main.prefMusicVolume.getValue() / 100);
        this.nowPlaying.setValue(musicName);
        this.setMusic(MediaState.PLAY);

        backgroundMusicPlayer.setOnEndOfMedia(() -> {
            this.setMusic(MediaState.STOP);
            this.setMusic(MediaState.PLAY);
        });
    }

    /**
     * Shuffle play music in the bgmList
     */
    public void play() {

        String songName = getRandomBackgroundMusicName();
        duration = bgmDuration.get(songName);
        Media mediaPlaying = (Media)ResourceFactory.getResource(songName, ResourceType.Media);
        backgroundMusicPlayer = new MediaPlayer(mediaPlaying);
        backgroundMusicPlayer.setVolume(Main.prefMusicVolume.getValue() / 100);
        this.nowPlaying.setValue(songName);
        this.setMusic(MediaState.PLAY);

        backgroundMusicPlayer.setOnEndOfMedia(() -> {
            this.setMusic(MediaState.STOP);
            this.setMusic(MediaState.PLAY);
        });
    }

    /**
     * get a random music name from the bgmList
     * @return a random music name from the bgmList
     */
    private String getRandomBackgroundMusicName() {
        Random random = new Random();
        return bgmList.get(random.nextInt(bgmList.size()));
    }

    /**
     * set the status of MediaPlayer
     * PLAY: resume the MediaPlayer
     * PAUSE: pause the MediaPlayer
     * STOP: stop the MediaPlayer and select a random music for next play
     * @param state MediaState: PLAY, PAUSE, STOP
     * @see MediaState
     */
    public void setMusic(MediaState state) {
        switch (state) {
            case PLAY -> {
                backgroundMusicPlayer.currentTimeProperty().addListener(listener);
                backgroundMusicPlayer.play();
            }
            case PAUSE -> backgroundMusicPlayer.pause();
            case STOP -> {
                backgroundMusicPlayer.stop();
                backgroundMusicPlayer.currentTimeProperty().removeListener(listener);
                String songName = getRandomBackgroundMusicName();
                Media mediaPlaying = (Media)ResourceFactory.getResource(songName, ResourceType.Media);
                duration = bgmDuration.get(songName);
                backgroundMusicPlayer = new MediaPlayer(mediaPlaying);
                backgroundMusicPlayer.setVolume(Main.prefMusicVolume.getValue() / 100);
                backgroundMusicPlayer.setOnEndOfMedia(() -> {
                    this.setMusic(MediaState.STOP);
                    this.setMusic(MediaState.PLAY);
                });

                this.nowPlaying.setValue(songName);
            }
        }
    }

    /**
     * the Listener to call each time music duration change
     * will update the progress in view
     */
    private ChangeListener listener = (observable, oldValue, newValue) -> {
        controller.updatesDurationSlider();
    };

    /**
     * bind the controller in SoundPreferences view
     * because the binding and updating all goes through this controller
     * @param controller
     */
    public void bindSoundPreferencesController(SoundPreferenceController controller) {
        this.controller = controller;
    }

}

package com.ae2dms.UI;

import com.ae2dms.Business.GameTimer;
import com.ae2dms.IO.ResourceFactory;
import com.ae2dms.IO.ResourceType;
import com.ae2dms.Main.Main;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.Slider;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.util.Duration;
import javafx.util.StringConverter;
import javafx.util.converter.NumberStringConverter;

import java.text.NumberFormat;

/**
 * The controller class for SoundPreference
 */
public class SoundPreferenceController {

    /**
     * The GameMediaPlayer that this controller can control, including volume, songs and progress
     */
    private final GameMediaPlayer player = GameMediaPlayer.getInstance();

    /**
     * The JavaFX Label to display the music time, in 02:13/04:23 format
     */
    @FXML
    private Label MusicTime;

    /**
     * The JavaFX Label to display the name of the song
     */
    @FXML
    private Label MusicName;

    /**
     * The JavaFX Slider which progress is sync to the progress of the song
     * and user can drag to change the progress
     */
    @FXML
    private Slider MusicProgressSlider;

    /**
     * The Underlying JavaFX Control Pane
     * used to control the show and hide of this controller
     */
    @FXML
    private Pane MusicControl;

    /**
     * The Image which indicated if it is in mute mode or not
     */
    @FXML
    private ImageView MuteImage;

    /**
     * The Text to indicate if it is in mute mode or not
     */
    @FXML
    private Label MuteLabel;

    /**
     * The JavaFX slider to control the volume of playing Music
     */
    @FXML
    private Slider MusicSlider;

    /**
     * The JavaFX slider to control the volume of playing Sound Effects(SFX)
     */
    @FXML
    private Slider SFXSlider;

    /**
     * The value to indicate how much volume the music is set, with max of 100
     */
    @FXML
    private Label MusicSliderValue;

    /**
     * The value to indicate how much volume the SFX is set, with max of 100
     */
    @FXML
    private Label SFXSliderValue;

    /**
     * The JavaFX list to show all songs
     */
    @FXML
    private ListView MusicList;

    /**
     * whether this control pane is showing or not
     */
    public BooleanProperty isShowing = new SimpleBooleanProperty(false);

    /**
     * whether it is globally muted or not
     */
    public BooleanProperty isMute = new SimpleBooleanProperty(false);


    /**
     * Called by JavaFX
     * to bind the Sliders with corresponding SliderValue
     * and bind the SliderValue to the global preference in Main
     * if all volume is set to 0, then the mute button will be automatically set
     * and bind the musicList in GameMediaPlayer to the list view
     */
    public void initialize() {
        isShowing.addListener((observable, oldValue, newValue) -> {
            if (observable != null && observable.getValue()==true) {
                MusicControl.setVisible(true);
            } else if (observable != null && observable.getValue()==false){
                MusicControl.setVisible(false);
            }
        });

        StringConverter<Number> converter = new NumberStringConverter(NumberFormat.getIntegerInstance());

        MusicSliderValue.textProperty().bindBidirectional(MusicSlider.valueProperty(), converter);
        SFXSliderValue.textProperty().bindBidirectional(SFXSlider.valueProperty(), converter);

        MusicSlider.valueProperty().bindBidirectional(player.MusicVolume);
        MusicSlider.valueProperty().bindBidirectional(Main.prefMusicVolume);
        SFXSlider.valueProperty().bindBidirectional(player.SFXVolume);
        SFXSlider.valueProperty().bindBidirectional(Main.prefSFXVolume);

        MusicSlider.valueProperty().addListener((observable, oldValue, newValue) -> {
            if (observable != null) {
                if (observable.getValue().intValue() == 0 && SFXSlider.getValue() == 0) {
                    isMute.setValue(true);
                } else {
                    isMute.setValue(false);
                }
            }
        });

        MusicSlider.valueProperty().addListener((observable, oldValue, newValue) -> {
            if (observable != null) {
                if (observable.getValue().intValue() == 0 && SFXSlider.getValue() == 0) {
                    isMute.setValue(true);
                } else {
                    isMute.setValue(false);
                }
            }
        });

        MusicList.setItems(player.bgmList);

        MusicList.getSelectionModel().selectedItemProperty().addListener(
        (observable, oldValue, newValue) ->{
            player.setMusic(MediaState.STOP);
            String musicName = (String)observable.getValue();
            player.play(musicName);
        });

        isMute.addListener((observable, oldValue, newValue) -> {
            if (observable != null && observable.getValue()==true) {
                MuteImage.setImage((Image) ResourceFactory.getResource("ALL_SOUND_OFF_ICON", ResourceType.Image));
                MuteLabel.setText("is Mute");
                MusicSlider.adjustValue(0);
                SFXSlider.adjustValue(0);
            } else if (observable != null && observable.getValue()==false){
                MuteImage.setImage((Image) ResourceFactory.getResource("ALL_SOUND_ON_ICON", ResourceType.Image));
                MuteLabel.setText("Not Mute");
                MusicSlider.adjustValue(80);
                SFXSlider.adjustValue(80);
            }
        });

        MusicName.textProperty().bind(player.nowPlaying);

        if (Main.prefSFXVolume.getValue() == 0 || Main.prefSFXVolume.getValue() == 0) {
            this.isMute.setValue(true);
        }

        MusicProgressSlider.setOnMouseClicked((event) ->{
            player.backgroundMusicPlayer.seek(Duration.seconds(player.duration * MusicProgressSlider.getValue() / 100));
        });

        player.bindSoundPreferencesController(this);

    }

    /**
     * should be called when a song's progress has changed
     * will update the position of progress slider with sync
     */
    public void updatesDurationSlider() {
        if (!MusicProgressSlider.isPressed()) {
            double value = player.backgroundMusicPlayer.getCurrentTime().toSeconds() / player.duration * 100;
            MusicProgressSlider.valueProperty().setValue(value);
            MusicTime.setText(GameTimer.parseToTimeFormat((int)player.backgroundMusicPlayer.getCurrentTime().toSeconds()) + " / " + GameTimer.parseToTimeFormat(player.duration));
        }
    }

    /**
     * click between mute all and open to 80% volume
     */
    @FXML
    private void clickMuteAll() {
        isMute.setValue(!isMute.getValue());
    }
}

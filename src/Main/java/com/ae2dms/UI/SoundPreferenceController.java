package com.ae2dms.UI;

import com.ae2dms.Business.GameTimer;
import com.ae2dms.IO.ResourceFactory;
import com.ae2dms.IO.ResourceType;
import com.ae2dms.Main.Main;
import javafx.application.Platform;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.Slider;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.util.Duration;
import javafx.util.StringConverter;
import javafx.util.converter.NumberStringConverter;

import java.text.NumberFormat;
import java.util.Locale;

public class SoundPreferenceController {

    private final GameMediaPlayer player = GameMediaPlayer.getInstance();

    @FXML
    private Label MusicTime;

    @FXML
    private Label MusicName;

    @FXML
    private Slider MusicProgressSlider;

    @FXML
    private Pane MusicControl;
    
    @FXML
    private ImageView MuteImage;

    @FXML
    private Label MuteLabel;

    @FXML
    private Slider MusicSlider;

    @FXML
    private Slider SFXSlider;

    @FXML
    private Label MusicSliderValue;

    @FXML
    private Label SFXSliderValue;

    @FXML
    private ListView MusicList;
    
    public BooleanProperty isShowing = new SimpleBooleanProperty(false);

    public BooleanProperty isMute = new SimpleBooleanProperty(false);
    
    
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
        init();

    }

    private void init() {
        MusicProgressSlider.setOnMouseClicked((event) ->{
            player.backgroundMusicPlayer.seek(Duration.seconds(player.duration * MusicProgressSlider.getValue() / 100));
        });

        player.bindSoundPreferencesController(this);
    }

    public void updatesDurationSlider() {
        if (!MusicProgressSlider.isPressed()) {
            double value = player.backgroundMusicPlayer.getCurrentTime().toSeconds() / player.duration * 100;
            MusicProgressSlider.valueProperty().setValue(value);
            MusicTime.setText(GameTimer.parseToTimeFormat((int)player.backgroundMusicPlayer.getCurrentTime().toSeconds()) + " / " + GameTimer.parseToTimeFormat(player.duration));
        }
    }

    @FXML
    private void clickMuteAll(MouseEvent mouseEvent) {
        isMute.setValue(!isMute.getValue());
    }
}

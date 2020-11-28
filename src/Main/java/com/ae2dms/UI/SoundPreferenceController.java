package com.ae2dms.UI;

import com.ae2dms.Business.GameTimer;
import com.ae2dms.IO.ResourceFactory;
import com.ae2dms.IO.ResourceType;
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

        isMute.addListener((observable, oldValue, newValue) -> {
            if (observable != null && observable.getValue()==true) {
                MuteImage.setImage((Image) ResourceFactory.getResource("MUSIC_OFF_ICON", ResourceType.Image));
                MuteLabel.setText("is Mute");
                MusicSlider.adjustValue(0);
                SFXSlider.adjustValue(0);
            } else if (observable != null && observable.getValue()==false){
                MuteImage.setImage((Image) ResourceFactory.getResource("MUSIC_ON_ICON", ResourceType.Image));
                MuteLabel.setText("Not Mute");
                MusicSlider.adjustValue(80);
                SFXSlider.adjustValue(80);
            }
        });

        StringConverter<Number> converter = new NumberStringConverter();

        MusicName.textProperty().bindBidirectional(player.nowPlaying);

        
        MusicSliderValue.textProperty().bindBidirectional(MusicSlider.valueProperty(), converter);
        SFXSliderValue.textProperty().bindBidirectional(SFXSlider.valueProperty(), converter);

        MusicSlider.valueProperty().bindBidirectional(player.MusicVolume);
        SFXSlider.valueProperty().bindBidirectional(player.SFXVolume);

        MusicList.setItems(player.bgmList);

        MusicList.getSelectionModel().selectedItemProperty().addListener(
        (observable, oldValue, newValue) ->{
            String musicName = (String)observable.getValue();
            player.setMusic(MediaState.STOP);
            player.play(musicName);
        });

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

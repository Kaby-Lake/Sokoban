package com.ae2dms.IO;

import javafx.scene.image.Image;
import javafx.scene.media.AudioClip;
import javafx.scene.media.Media;

import java.net.URISyntaxException;

public class ResourceFactory {

    public static Media MENU_BACKGROUND_MUSIC = null;
    public static Media GAME_BACKGROUND_MUSIC = null;

    public static Image STAGE_IMAGE = null;
    public static Image DIAMOND_IMAGE = null;
    public static Image CRATE_IMAGE = null;
    public static Image CRATE_ON_DIAMOND_IMAGE = null;
    public static Image PLAYER_FRONT_IMAGE = null;
    public static Image PLAYER_BACK_IMAGE = null;
    public static Image PLAYER_LEFT_IMAGE = null;
    public static Image PLAYER_RIGHT_IMAGE = null;
    public static AudioClip UNMOVABLE_AUDIO_CLIP = null;
    public static AudioClip MOVE_AUDIO_CLIP = null;

    static {
        try {
            MENU_BACKGROUND_MUSIC = new Media(ResourceFactory.class.getResource("/music/PaperClip-Jumping.mp3").toURI().toString());
            GAME_BACKGROUND_MUSIC = new Media(ResourceFactory.class.getResource("/music/PETO-Okay.mp3").toURI().toString());

            STAGE_IMAGE = new Image(ResourceFactory.class.getResource("/ui/Assets/Game/Stage.png").toURI().toString());
            DIAMOND_IMAGE = new Image(ResourceFactory.class.getResource("/ui/Assets/Game/Diamond.png").toURI().toString());
            CRATE_IMAGE = new Image(ResourceFactory.class.getResource("/ui/Assets/Game/Crate.png").toURI().toString());
            CRATE_ON_DIAMOND_IMAGE = new Image(ResourceFactory.class.getResource("/ui/Assets/Game/Crate_On_Diamond.png").toURI().toString());
            PLAYER_FRONT_IMAGE = new Image(ResourceFactory.class.getResource("/ui/Assets/Game/Front.png").toURI().toString());
            PLAYER_BACK_IMAGE = new Image(ResourceFactory.class.getResource("/ui/Assets/Game/Back.png").toURI().toString());
            PLAYER_LEFT_IMAGE = new Image(ResourceFactory.class.getResource("/ui/Assets/Game/Left.png").toURI().toString());
            PLAYER_RIGHT_IMAGE = new Image(ResourceFactory.class.getResource("/ui/Assets/Game/Right.png").toURI().toString());

            UNMOVABLE_AUDIO_CLIP = new AudioClip(ResourceFactory.class.getResource("/music/Basso.mp3").toURI().toString());
            MOVE_AUDIO_CLIP = new AudioClip(ResourceFactory.class.getResource("/music/Pop.mp3").toURI().toString());
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
    }


    public ResourceFactory() throws Exception {
    }
}

package com.ae2dms.IO;

import javafx.scene.image.Image;
import javafx.scene.media.AudioClip;
import javafx.scene.media.Media;

import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

public class ResourceFactory {

    public static Media DEFAULT_BACKGROUND_MUSIC = null;

    public static Image BACKGROUND_IMAGE1 = null;
    public static Image BACKGROUND_IMAGE2 = null;
    public static Image BACKGROUND_IMAGE3 = null;


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
    public static AudioClip MOVE_CRATE_AUDIO_CLIP = null;
    public static AudioClip LEVEL_COMPLETE_AUDIO_CLIP = null;


    private static final ArrayList<String> BACKGROUND_MUSIC_RESOURCES_ADDRESSES = new ArrayList<>();

    static {
        try {

            DEFAULT_BACKGROUND_MUSIC = new Media(getResource("/music/Songs/PETO-Okay.mp3"));


            BACKGROUND_MUSIC_RESOURCES_ADDRESSES.addAll(Arrays.asList(
                    "/music/Songs/PaperClip-Blip.mp3",
                    "/music/Songs/PaperClip-BOUNCE.mp3",
                    "/music/Songs/PaperClip-DEMONS.mp3",
                    "/music/Songs/PaperClip-Distant.mp3",
                    "/music/Songs/PaperClip-Heeyha.mp3",
                    "/music/Songs/PaperClip-Jumping.mp3",
                    "/music/Songs/PaperClip-KINGSTON.mp3",
                    "/music/Songs/PaperClip-Lust.mp3",
                    "/music/Songs/PETO-Okay.mp3"));

            BACKGROUND_IMAGE1 = new Image(getResource("/ui/Assets/Game/GameBackground1.jpg"));
            BACKGROUND_IMAGE2 = new Image(getResource("/ui/Assets/Game/GameBackground2.jpg"));
            BACKGROUND_IMAGE3 = new Image(getResource("/ui/Assets/Game/GameBackground3.jpg"));

            STAGE_IMAGE = new Image(getResource("/ui/Assets/Game/Stage.png"));
            DIAMOND_IMAGE = new Image(getResource("/ui/Assets/Game/Diamond.png"));
            CRATE_IMAGE = new Image(getResource("/ui/Assets/Game/Crate.png"));
            CRATE_ON_DIAMOND_IMAGE = new Image(getResource("/ui/Assets/Game/Crate_On_Diamond.png"));
            PLAYER_FRONT_IMAGE = new Image(getResource("/ui/Assets/Game/Front.png"));
            PLAYER_BACK_IMAGE = new Image(getResource("/ui/Assets/Game/Back.png"));
            PLAYER_LEFT_IMAGE = new Image(getResource("/ui/Assets/Game/Left.png"));
            PLAYER_RIGHT_IMAGE = new Image(getResource("/ui/Assets/Game/Right.png"));

            UNMOVABLE_AUDIO_CLIP = new AudioClip(getResource("/music/SFX/Basso.mp3"));
            MOVE_AUDIO_CLIP = new AudioClip(getResource("/music/SFX/Pop.mp3"));
            MOVE_CRATE_AUDIO_CLIP = new AudioClip(getResource("/music/SFX/Jeans_Drop.mp3"));

            LEVEL_COMPLETE_AUDIO_CLIP = new AudioClip(getResource("/music/SFX/Funk.mp3"));
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
    }

    public ResourceFactory() {
    }

    public static String getResource(String url) throws URISyntaxException {
        return ResourceFactory.class.getResource(url).toURI().toString();
    }

    public static Image randomBackgroundImage() {
        ArrayList<Image> list = new ArrayList<>();
        list.add(ResourceFactory.BACKGROUND_IMAGE1);
        list.add(ResourceFactory.BACKGROUND_IMAGE2);
        list.add(ResourceFactory.BACKGROUND_IMAGE3);
        Random random = new Random();
        return list.get(random.nextInt(3));
    }

    public static Media randomBackgroundMusic() {
        Random random = new Random();
        String url = BACKGROUND_MUSIC_RESOURCES_ADDRESSES.get(random.nextInt(BACKGROUND_MUSIC_RESOURCES_ADDRESSES.size()));
        try {
            return new Media(getResource(url));
        } catch (URISyntaxException e) {
            return DEFAULT_BACKGROUND_MUSIC;
        }
    }
}

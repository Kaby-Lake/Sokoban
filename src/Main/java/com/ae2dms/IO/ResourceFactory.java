package com.ae2dms.IO;

import javafx.scene.image.Image;
import javafx.scene.media.AudioClip;
import javafx.scene.media.Media;

import java.net.URISyntaxException;
import java.util.*;

public class ResourceFactory {

    //                              name     url
    private static final HashMap<String, String> urlMap = new HashMap<>();
    //                              url     object
    private static final HashMap<String, Media> mediaShelf = new HashMap<>();
    private static final HashMap<String, Image> imageShelf = new HashMap<>();
    private static final HashMap<String, AudioClip> audioClipShelf = new HashMap<>();

    static {
        urlMap.put("PaperClip-Blip","/music/Songs/PaperClip-Blip.mp3");
        urlMap.put("PaperClip-BOUNCE","/music/Songs/PaperClip-BOUNCE.mp3");
        urlMap.put("PaperClip-DEMONS","/music/Songs/PaperClip-DEMONS.mp3");
        urlMap.put("PaperClip-Distant","/music/Songs/PaperClip-Distant.mp3");
        urlMap.put("PaperClip-Heeyha","/music/Songs/PaperClip-Heeyha.mp3");
        urlMap.put("PaperClip-Jumping","/music/Songs/PaperClip-Jumping.mp3");
        urlMap.put("PaperClip-KINGSTON","/music/Songs/PaperClip-KINGSTON.mp3");
        urlMap.put("PaperClip-Lust","/music/Songs/PaperClip-Lust.mp3");
        urlMap.put("PETO-Okay","/music/Songs/PETO-Okay.mp3");

        urlMap.put("GameBackground_1", "/ui/Assets/Game/GameBackground1.jpg");
        urlMap.put("GameBackground_2", "/ui/Assets/Game/GameBackground2.jpg");
        urlMap.put("GameBackground_3", "/ui/Assets/Game/GameBackground3.jpg");

        urlMap.put("STAGE_IMAGE", "/ui/Assets/Game/Stage.png");
        urlMap.put("DIAMOND_IMAGE", "/ui/Assets/Game/Diamond.png");
        urlMap.put("CRATE_IMAGE", "/ui/Assets/Game/Crate.png");
        urlMap.put("CRATE_ON_DIAMOND_IMAGE", "/ui/Assets/Game/Crate_On_Diamond.png");
        urlMap.put("PLAYER_FRONT_IMAGE", "/ui/Assets/Game/Front.png");
        urlMap.put("PLAYER_BACK_IMAGE", "/ui/Assets/Game/Back.png");
        urlMap.put("PLAYER_LEFT_IMAGE", "/ui/Assets/Game/Left.png");
        urlMap.put("PLAYER_RIGHT_IMAGE", "/ui/Assets/Game/Right.png");

        urlMap.put("UNMOVABLE_AUDIO_CLIP", "/music/SFX/Basso.mp3");
        urlMap.put("MOVE_AUDIO_CLIP", "/music/SFX/Pop.mp3");
        urlMap.put("MOVE_CRATE_AUDIO_CLIP", "/music/SFX/Jeans_Drop.mp3");

        urlMap.put("LEVEL_COMPLETE_AUDIO_CLIP", "/music/SFX/Funk.mp3");
    }

    private static String getResourceSystemPath(String url) {
        try {
            return ResourceFactory.class.getResource(url).toURI().toString();
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static Object getResource(String name, ResourceType type) {
        String url = urlMap.get(name);

        switch (type) {
            case Media -> {
                if (mediaShelf.containsKey(url)) {
                    return mediaShelf.get(url);
                } else {
                    Media object = new Media(getResourceSystemPath(url));
                    mediaShelf.put(url, object);
                    return object;
                }
            }
            case Image -> {
                if (imageShelf.containsKey(url)) {
                    return imageShelf.get(url);
                } else {
                    Image object = new Image(getResourceSystemPath(url));
                    imageShelf.put(url, object);
                    return object;
                }
            }
            case AudioClip -> {
                if (audioClipShelf.containsKey(url)) {
                    return audioClipShelf.get(url);
                } else {
                    AudioClip object = new AudioClip(getResourceSystemPath(url));
                    audioClipShelf.put(url, object);
                    return object;
                }
            }
            default -> {
                return null;
            }
        }
    }

    public static Image getRandomBackgroundImage() {
        List<Image> list = (List<Image>) imageShelf.values();
        Random random = new Random();
        return list.get(random.nextInt(list.size()));
    }

    public static Media getRandomBackgroundMusic() {
        List<Media> list = (List<Media>) mediaShelf.values();
        Random random = new Random();
        return list.get(random.nextInt(list.size()));
    }
}

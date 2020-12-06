package com.ae2dms.IO;

import javafx.scene.image.Image;
import javafx.scene.media.AudioClip;
import javafx.scene.media.Media;

import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

import static com.ae2dms.Business.GameDocument.logger;

/**
 * The type Resource factory.
 */
public class ResourceFactory {

    //                              name     url
    private static final HashMap<String, String> urlMap = new HashMap<>();
    //                              url     object
    private static final HashMap<String, Media> mediaShelf = new HashMap<>();
    private static final HashMap<String, Image> imageShelf = new HashMap<>();
    private static final HashMap<String, AudioClip> audioClipShelf = new HashMap<>();
    private static final HashMap<String, URL> FXMLShelf = new HashMap<>();

    static {
        urlMap.put("PaperClip-Blip","/music/Songs/PaperClip-Blip.mp3");
        urlMap.put("PaperClip-BOUNCE","/music/Songs/PaperClip-BOUNCE.mp3");
        urlMap.put("PaperClip-DEMONS","/music/Songs/PaperClip-DEMONS.mp3");
        urlMap.put("PaperClip-Distant","/music/Songs/PaperClip-Distant.mp3");
        urlMap.put("PaperClip-Heeyha","/music/Songs/PaperClip-Heeyha.mp3");
        urlMap.put("PaperClip-Jumping","/music/Songs/PaperClip-Jumping.mp3");
        urlMap.put("PaperClip-KINGSTON","/music/Songs/PaperClip-KINGSTON.mp3");
        urlMap.put("PaperClip-Lust","/music/Songs/PaperClip-Lust.mp3");
        urlMap.put("PaperClip-Heat Up","/music/Songs/PaperClip-Heat Up.mp3");
        urlMap.put("PaperClip-Bricks","/music/Songs/PaperClip-Bricks.mp3");
        urlMap.put("PaperClip-GOOD SHOW","/music/Songs/PaperClip-GOOD SHOW.mp3");
        urlMap.put("PaperClip-Last Time","/music/Songs/PaperClip-Last Time.mp3");
        urlMap.put("PETO-Okay","/music/Songs/PETO-Okay.mp3");

        urlMap.put("GameBackground_1", "/ui/Assets/Game/GameBackground1.jpg");
        urlMap.put("GameBackground_2", "/ui/Assets/Game/GameBackground2.jpg");
        urlMap.put("GameBackground_3", "/ui/Assets/Game/GameBackground3.jpg");

        urlMap.put("SCORE_ITEM_BACKGROUND", "/ui/Assets/HighScore/Score_Item.png");
        urlMap.put("TIME_ITEM_BACKGROUND", "/ui/Assets/HighScore/Time_Item.png");
        urlMap.put("SCORE_ITEM_FIRST_BACKGROUND", "/ui/Assets/HighScore/Score_Item_First.png");
        urlMap.put("TIME_ITEM_FIRST_BACKGROUND", "/ui/Assets/HighScore/Time_Item_First.png");
        urlMap.put("NO_RECORDS_INFO", "/ui/Assets/HighScore/No_Records_Info.png");

        urlMap.put("STAGE_IMAGE", "/ui/Assets/Game/Stage.png");
        urlMap.put("STAGE_DRAG_IMAGE", "/ui/Assets/Game/Stage_Drag.png");

        urlMap.put("CANDY_IMAGE", "/ui/Assets/Game/Candy.png");

        urlMap.put("DIAMOND_IMAGE_Blue", "/ui/Assets/Game/Diamond/Diamond_Blue.png");
        urlMap.put("DIAMOND_IMAGE_Brown", "/ui/Assets/Game/Diamond/Diamond_Brown.png");
        urlMap.put("DIAMOND_IMAGE_Green", "/ui/Assets/Game/Diamond/Diamond_Green.png");
        urlMap.put("DIAMOND_IMAGE_Red", "/ui/Assets/Game/Diamond/Diamond_Red.png");
        urlMap.put("DIAMOND_IMAGE_Silver", "/ui/Assets/Game/Diamond/Diamond_Silver.png");

        urlMap.put("CRATE_IMAGE_Blue", "/ui/Assets/Game/Crate/Crate_Blue.png");
        urlMap.put("CRATE_IMAGE_Brown", "/ui/Assets/Game/Crate/Crate_Brown.png");
        urlMap.put("CRATE_IMAGE_Green", "/ui/Assets/Game/Crate/Crate_Green.png");
        urlMap.put("CRATE_IMAGE_Red", "/ui/Assets/Game/Crate/Crate_Red.png");
        urlMap.put("CRATE_IMAGE_Silver", "/ui/Assets/Game/Crate/Crate_Silver.png");

        urlMap.put("CRATE_ON_DIAMOND_IMAGE", "/ui/Assets/Game/Crate/Crate_Brown.png");
        urlMap.put("PLAYER_FRONT_IMAGE", "/ui/Assets/Game/Front.png");
        urlMap.put("PLAYER_BACK_IMAGE", "/ui/Assets/Game/Back.png");
        urlMap.put("PLAYER_LEFT_IMAGE", "/ui/Assets/Game/Left.png");
        urlMap.put("PLAYER_RIGHT_IMAGE", "/ui/Assets/Game/Right.png");

        urlMap.put("CHOICE_GRID_IMAGE", "/ui/Assets/Game/Choice_Grid.png");

        urlMap.put("DEBUG_ON_ICON", "/ui/Assets/BottomBar/Debug_on.png");
        urlMap.put("DEBUG_OFF_ICON", "/ui/Assets/BottomBar/Debug_off.png");
        urlMap.put("DEBUG_NULL_ICON", "/ui/Assets/BottomBar/Debug_null.png");

        urlMap.put("MUSIC_ON_ICON", "/ui/Assets/BottomBar/Music_on.png");
        urlMap.put("MUSIC_OFF_ICON", "/ui/Assets/BottomBar/Music_off.png");

        urlMap.put("ALL_SOUND_ON_ICON", "/ui/Assets/MusicControl/Sound_on.png");
        urlMap.put("ALL_SOUND_OFF_ICON", "/ui/Assets/MusicControl/Sound_off.png");

        urlMap.put("UNDO_ICON", "/ui/Assets/BottomBar/Undo.png");
        urlMap.put("UNDO_NULL_ICON", "/ui/Assets/BottomBar/Undo_null.png");
        urlMap.put("HIGH_SCORE_LIST_ON_ICON", "/ui/Assets/BottomBar/High_Score_List_on.png");
        urlMap.put("HIGH_SCORE_LIST_OFF_ICON", "/ui/Assets/BottomBar/High_Score_List_off.png");
        urlMap.put("HIGH_SCORE_LIST_NULL_ICON", "/ui/Assets/BottomBar/High_Score_List_null.png");
        urlMap.put("SAVE_RECORD", "/ui/Assets/Game/GameCompletePopUp/Save_Record.png");
        urlMap.put("SAVE_RECORD_DONE", "/ui/Assets/Game/GameCompletePopUp/Save_Record_done.png");
        urlMap.put("SAVE_GAME_ICON", "/ui/Assets/BottomBar/Save_Game.png");
        urlMap.put("SAVE_GAME_NULL_ICON", "/ui/Assets/BottomBar/Save_Game_null.png");


        urlMap.put("AVATARS_1", "/ui/Assets/HighScore/avatars-01.png");
        urlMap.put("AVATARS_2", "/ui/Assets/HighScore/avatars-02.png");
        urlMap.put("AVATARS_3", "/ui/Assets/HighScore/avatars-03.png");
        urlMap.put("AVATARS_4", "/ui/Assets/HighScore/avatars-04.png");
        urlMap.put("AVATARS_5", "/ui/Assets/HighScore/avatars-05.png");
        urlMap.put("AVATARS_6", "/ui/Assets/HighScore/avatars-06.png");
        urlMap.put("AVATARS_7", "/ui/Assets/HighScore/avatars-07.png");
        urlMap.put("AVATARS_8", "/ui/Assets/HighScore/avatars-08.png");
        urlMap.put("AVATARS_9", "/ui/Assets/HighScore/avatars-09.png");
        urlMap.put("AVATARS_10", "/ui/Assets/HighScore/avatars-10.png");


        urlMap.put("UNMOVABLE_AUDIO_CLIP", "/music/SFX/Basso.mp3");
        urlMap.put("MOVE_AUDIO_CLIP", "/music/SFX/Pop.mp3");
        urlMap.put("MOVE_CRATE_AUDIO_CLIP", "/music/SFX/Jeans_Drop.mp3");

        urlMap.put("LEVEL_COMPLETE_AUDIO_CLIP", "/music/SFX/level_complete.mp3");
        urlMap.put("GAME_COMPLETE_AUDIO_CLIP", "/music/SFX/game_complete.mp3");

        urlMap.put("SCORE_ITEM_TEMPLATE_FXML", "/ui/FXML/ScoreItemTemplate.fxml");
    }

    private static String getResourceSystemPath(String url) {
        try {
            return ResourceFactory.class.getResource(url).toURI().toString();
        } catch (URISyntaxException e) {
            logger.severe(e.getMessage());
        }
        return null;
    }

    /**
     * Gets resource.
     *
     * @param name the name
     * @param type the type
     * @return the resource
     */
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
            case FXML -> {
                if (FXMLShelf.containsKey(url)) {
                    return FXMLShelf.get(url);
                } else {
                    URL object = ResourceFactory.class.getResource(url);
                    FXMLShelf.put(url, object);
                    return object;
                }

            }
            default -> {
                return null;
            }
        }
    }

    /**
     * Gets random background image.
     *
     * @return the random background image
     */
    public static Image getRandomBackgroundImage() {
        ArrayList<String> list = new ArrayList<>();
        list.add("GameBackground_1");
        list.add("GameBackground_2");
        list.add("GameBackground_3");
        Random random = new Random();
        String name = list.get(random.nextInt(list.size()));
        return (Image)getResource(name, ResourceType.Image);
    }

}

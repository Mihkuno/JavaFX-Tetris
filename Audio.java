package src;

import java.io.File;
import java.net.URISyntaxException;

import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.scene.media.AudioClip;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.util.Duration;

public class Audio implements MainInterface {


    public static double V_SOUND = 1.0;
    public static double V_MUSIC = 0.30;
    private AudioClip sound;
    private MediaPlayer music;

    public void playSound(String location) {
        if (sound != null) {
            sound.stop();
        }
        sound = new AudioClip(getClass().getResource(location).toExternalForm());
        sound.setVolume(V_SOUND);
        sound.play();
    }
    
    public void playMusic(String location) {
        if (music != null) {
            music.stop();
        }
        Media media = new Media(getClass().getResource(location).toExternalForm());

        this.music = new MediaPlayer(media);
        music.setVolume(V_MUSIC);
        music.play();

        MUSIC.add(this.music);
    }

    public static void setMusicVolume(double value) {
        V_MUSIC = value;
        // since mediaplayer is a running object, must maniplate each one's attributes through an array
        // reason why cannot just change value through static
        for (MediaPlayer bgm : MUSIC) {
            bgm.setVolume(Audio.V_MUSIC);
        }
    }

    public static void setSoundVolume(double value) {
        V_SOUND = value;
    }

    public static void setMusicFadeOut(int seconds) {
        for (MediaPlayer bgm : MUSIC) {
            Timeline timeline = new Timeline( new KeyFrame(Duration.seconds(seconds), new KeyValue(bgm.volumeProperty(), 0)));
            timeline.play();
            timeline.setOnFinished((e) -> {
                bgm.pause();
            });
        }
    }

    public static void setMusicFadeIn(int seconds) {
        for (MediaPlayer bgm : MUSIC) {
            bgm.play();
            Timeline timeline = new Timeline( new KeyFrame(Duration.seconds(seconds), new KeyValue(bgm.volumeProperty(), V_MUSIC)));
            timeline.play();
        }
    }
}

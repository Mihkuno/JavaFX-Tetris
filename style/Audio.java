package proj.style;

import java.io.File;

import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.scene.media.AudioClip;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.util.Duration;
import proj.MainInterface;


public class Audio implements MainInterface {


    public static double V_SOUND = 1.0;
    public static double V_MUSIC = 0.40;
    private AudioClip sound;
    private MediaPlayer music;

    public void playSound(String location) {
        if (sound != null) {
            sound.stop();
        }
        sound = new AudioClip(this.getClass().getResource(location).toExternalForm());
        sound.setVolume(V_SOUND);
        sound.play();
    }
    
    public void playMusic(String location) {
        if (music != null) {
            music.stop();
        }
        Media media = new Media(new File(location).toURI().toString());
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
            Timeline timeline = new Timeline(
            new KeyFrame(Duration.seconds(seconds),
                new KeyValue(bgm.volumeProperty(), 0)));
            timeline.play();
            timeline.setOnFinished((e) -> {
                bgm.stop();
            });
        }
    }
}

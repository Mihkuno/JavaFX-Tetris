package proj.style;

import java.io.File;

import javafx.scene.media.AudioClip;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import proj.MainInterface;


public class Audio implements MainInterface {


    public static double V_SOUND = 1.0;
    public static double V_MUSIC = 0.2;
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
}

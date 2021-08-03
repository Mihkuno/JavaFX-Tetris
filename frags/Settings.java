package proj.frags;

import javafx.beans.InvalidationListener;
import javafx.beans.Observable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.control.Button;
import javafx.scene.control.Slider;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.media.MediaPlayer;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import proj.MainInterface;
import proj.style.Animate;
import proj.style.Audio;

public class Settings implements MainInterface {
    static Slider vol_music;
    static Slider vol_sound;

    static Font txt_font;

    static Text txt_music;
    static Text txt_sound;

    static Text lvl_music;
    static Text lvl_sound;

    static Button btn_prev;

    public static void show() {
        initObj();
        initCase();
        initInput();

        SETTINGS_CONTAINER.getChildren().addAll(SETTINGS);

        LAYOUT.getChildren().add(SETTINGS_CONTAINER); 
    }

    public static void remove() {
        SETTINGS.getChildren().clear();
        SETTINGS_CONTAINER.getChildren().clear();
        LAYOUT.getChildren().remove(SETTINGS_CONTAINER);
    }

    public static void initObj() {

        txt_font = Font.loadFont( Settings.class.getClassLoader().getResourceAsStream( "proj/font/TrulyMadlyDpad-a72o.ttf"), 15);

        lvl_sound = new Text(Integer.toString((int)(Audio.V_SOUND*100))+"%");
        lvl_sound.setFont(txt_font);
        lvl_sound.translateYProperty().set((double)-20);
        lvl_sound.translateXProperty().set((double)235);
        
        lvl_music = new Text(Integer.toString((int)(Audio.V_MUSIC*100))+"%");
        lvl_music.setFont(txt_font);
        lvl_music.translateYProperty().set((double)-20);
        lvl_music.translateXProperty().set((double)235);

        txt_sound = new Text("Sound Volume");
        txt_sound.setFont(txt_font);
        txt_sound.translateYProperty().set((double)-20);

        txt_music = new Text("Music Volume");
        txt_music.setFont(txt_font);
        txt_music.translateYProperty().set((double)-20);

        vol_music = new Slider();
        vol_music.setMinWidth(260);
        vol_music.setValue(Audio.V_MUSIC*100);
        vol_music.getStylesheets().add("proj/style/slider.css");

        vol_sound = new Slider();
        vol_sound.setMinWidth(260);
        vol_sound.setValue(Audio.V_SOUND*100);
        vol_sound.getStylesheets().add("proj/style/slider.css");

        btn_prev = new Button("Back");
        btn_prev.setFont(txt_font);
        btn_prev.setCursor(Cursor.CLOSED_HAND);
        btn_prev.setGraphic(new ImageView(new Image("proj/image/ic_prev.png",28,28,true,true)) ); 
        btn_prev.setStyle("-fx-background-color: none");

        Animate.hover_deflate(btn_prev);
    }

    public static void initCase() {
        SETTINGS.setMaxWidth(300);
        SETTINGS.setAlignment(Pos.CENTER); 
        SETTINGS.setPadding(new Insets(80, 30, 40, 30)); 
        SETTINGS.setVgap(50); 
        SETTINGS.setHgap(30);       
        SETTINGS.setAlignment(Pos.CENTER);
        SETTINGS.add(lvl_sound, 0, 0);
        SETTINGS.add(txt_sound, 0, 0);
        SETTINGS.add(vol_sound, 0, 0);
        SETTINGS.add(lvl_music, 0, 1);
        SETTINGS.add(txt_music, 0, 1); 
        SETTINGS.add(vol_music, 0, 1); 
        SETTINGS.add(btn_prev, 0, 2);
        SETTINGS.setStyle("-fx-border-width:1px;-fx-border-color:#3d3d3d; -fx-border-radius: 20px;");
        // SETTINGS.setGridLinesVisible(true);
        // SETTINGS.getStylesheets().add("proj/grid.css");


        SETTINGS_CONTAINER.setAlignment(Pos.CENTER);
        SETTINGS_CONTAINER.setMinWidth(DOCUMENT_WIDTH);
        SETTINGS_CONTAINER.setMinHeight(DOCUMENT_HEIGHT);  
    }

    public static void initInput() {
        vol_music.valueProperty().addListener(new InvalidationListener() {
            @Override
            public void invalidated(Observable observable) {
                lvl_music.setText(Integer.toString((int)vol_music.getValue())+"%");
                Audio.V_MUSIC = (vol_music.getValue()/100);
                for (MediaPlayer bgm : MUSIC) {
                    bgm.setVolume(Audio.V_MUSIC);
                }
            }
        });

        vol_sound.valueProperty().addListener(new InvalidationListener() {
            @Override
            public void invalidated(Observable observable) {
                lvl_sound.setText(Integer.toString((int)vol_sound.getValue())+"%");
                Audio.V_SOUND = (vol_sound.getValue()/100);       
            }
        });

        vol_sound.setOnMouseReleased((e) -> {
            new Audio().playSound("../sound/m_click.mp3");
        });

        btn_prev.setOnMousePressed((e) -> { 
            new Audio().playSound("../sound/m_click.mp3"); 
            pressed("prev");
        });
        btn_prev.setOnMouseExited((e) -> {
            Animate.hover_deflate(btn_prev);
        });
        btn_prev.setOnMouseEntered((e) -> {
            new Audio().playSound("../sound/m_hover.mp3");
            Animate.hover_inflate(btn_prev);
        });
    }


    private static void pressed(String button) {
        if (button == "prev") {
            Animate.end_swipe(SETTINGS_CONTAINER, true).setOnFinished((e) -> {
                remove();
                Menu.btn_info.setDisable(false);
                Menu.btn_start.setDisable(false);
                Menu.btn_setting.setDisable(false);
                
            });
            Animate.mid_unfade(Menu.bg_menu).setOnFinished((e) -> {
                Animate.start_swipe(Menu.INTRO, false);
                Animate.start_swipe(Menu.INTRO_MISC, true);
            });
            btn_prev.setDisable(true);
        }
    }

    
    
    
}

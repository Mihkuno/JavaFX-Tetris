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
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import proj.MainInterface;
import proj.style.Animate;
import proj.style.Audio;
import proj.style.ToggleSwitch;
import javafx.scene.paint.Color;

public class Settings implements MainInterface {
    static Slider slider_music;
    static Slider slider_sound;

    static Font txt_font;

    static Text txt_music;
    static Text txt_sound;

    static Text lvl_music;
    static Text lvl_sound;

    static Button btn_prev;

    static GridPane TOGGLE_CONTAINER = new GridPane();

    static ToggleSwitch tgl_grid;
    static Text txt_grid;

    public static boolean SHOWGRID = false;

    public static void show() {
        initObj();
        initCase();
        initInput();

        SETTINGS_CONTAINER.getChildren().addAll(SETTINGS);

        LAYOUT.getChildren().add(SETTINGS_CONTAINER); 
    }

    public static void remove() {
        SETTINGS.getChildren().clear();
        TOGGLE_CONTAINER.getChildren().clear();
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

        slider_music = new Slider();
        slider_music.setMinWidth(260);
        slider_music.setValue(Audio.V_MUSIC*100);
        slider_music.getStylesheets().add("proj/style/slider.css");

        slider_sound = new Slider();
        slider_sound.setMinWidth(260);
        slider_sound.setValue(Audio.V_SOUND*100);
        slider_sound.getStylesheets().add("proj/style/slider.css");

        btn_prev = new Button("Back");
        btn_prev.setFont(txt_font);
        btn_prev.setCursor(Cursor.HAND);
        btn_prev.setGraphic(new ImageView(new Image("proj/image/ic_prev.png",28,28,true,true)) ); 
        btn_prev.setStyle("-fx-background-color: none");
        btn_prev.setTranslateX(-10);

        txt_grid = new Text("Show grid");
        txt_grid.setFont(txt_font);
        tgl_grid = new ToggleSwitch();
        tgl_grid.setTranslateX(-40);


        if (SHOWGRID == false) {
            tgl_grid.button.setStyle(tgl_grid.buttonStyleOff);
            tgl_grid.back.setFill(Color.valueOf("#ced5da"));
            StackPane.setAlignment(tgl_grid.button, Pos.CENTER_LEFT);
            tgl_grid.state = SHOWGRID;
            
            TOGGLE_CONTAINER.setGridLinesVisible(false);
            SETTINGS.setGridLinesVisible(false);
        
        } else {

            tgl_grid.button.setStyle(tgl_grid.buttonStyleOn);
            tgl_grid.back.setFill(Color.valueOf("#80C49E"));
            StackPane.setAlignment(tgl_grid.button, Pos.CENTER_RIGHT);
            tgl_grid.state = SHOWGRID;
            
            TOGGLE_CONTAINER.setGridLinesVisible(true);
            SETTINGS.setGridLinesVisible(true);
        }

        new Animate().hover_deflate(btn_prev);
    }

    public static void toggleGrid() {
        if (SHOWGRID == true) {

            SHOWGRID = false;

            tgl_grid.button.setStyle(tgl_grid.buttonStyleOff);
            tgl_grid.back.setFill(Color.valueOf("#ced5da"));
            StackPane.setAlignment(tgl_grid.button, Pos.CENTER_LEFT);
            tgl_grid.state = SHOWGRID;
            
            TOGGLE_CONTAINER.setGridLinesVisible(false);
            SETTINGS.setGridLinesVisible(false);
        
        } else {

            SHOWGRID = true;

            tgl_grid.button.setStyle(tgl_grid.buttonStyleOn);
            tgl_grid.back.setFill(Color.valueOf("#80C49E"));
            StackPane.setAlignment(tgl_grid.button, Pos.CENTER_RIGHT);
            tgl_grid.state = SHOWGRID;
            
            TOGGLE_CONTAINER.setGridLinesVisible(true);
            SETTINGS.setGridLinesVisible(true);
        }
    }

    public static void initCase() {

        TOGGLE_CONTAINER.setGridLinesVisible(false);
        SETTINGS.setGridLinesVisible(false);

        TOGGLE_CONTAINER.setHgap(50);
        TOGGLE_CONTAINER.add(txt_grid, 0, 0);
        TOGGLE_CONTAINER.add(tgl_grid, 1, 0);

        SETTINGS.setMaxWidth(300);
        SETTINGS.setPadding(new Insets(80, 30, 40, 30)); 
        SETTINGS.setVgap(40); 
        SETTINGS.setHgap(30);       
        SETTINGS.add(lvl_sound,        0, 0);
        SETTINGS.add(txt_sound,        0, 0);
        SETTINGS.add(slider_sound,     0, 0);
        SETTINGS.add(lvl_music,        0, 1);
        SETTINGS.add(txt_music,        0, 1); 
        SETTINGS.add(slider_music,     0, 1); 
        SETTINGS.add(TOGGLE_CONTAINER, 0, 2);
        SETTINGS.add(btn_prev,         0, 3);
        
        SETTINGS.setStyle("-fx-border-width:3px;-fx-border-color:#3d3d3d; -fx-border-radius: 10px;");

        SETTINGS_CONTAINER.setTranslateY(500);
        SETTINGS_CONTAINER.setAlignment(Pos.CENTER);
        SETTINGS_CONTAINER.setMinWidth(DOCUMENT_WIDTH);
        SETTINGS_CONTAINER.setMinHeight(DOCUMENT_HEIGHT);

        if (SHOWGRID) {
            TOGGLE_CONTAINER.setGridLinesVisible(true);
            SETTINGS.setGridLinesVisible(true);
        }
    }

    public static void initInput() {

        tgl_grid.setOnMouseClicked((e) -> { 
            toggleGrid(); 
        });

        tgl_grid.getButton().setOnMouseClicked((e) -> { 
            toggleGrid(); 
        });

        slider_music.valueProperty().addListener(new InvalidationListener() {
            @Override
            public void invalidated(Observable observable) {
                lvl_music.setText(Integer.toString((int)slider_music.getValue())+"%");
                Audio.setMusicVolume(slider_music.getValue()/100);
            }
        });

        slider_sound.valueProperty().addListener(new InvalidationListener() {
            @Override
            public void invalidated(Observable observable) {
                lvl_sound.setText(Integer.toString((int)slider_sound.getValue())+"%");
                Audio.setSoundVolume(slider_sound.getValue()/100);     
            }
        });

        slider_sound.setOnMouseReleased((e) -> {
            new Audio().playSound("../sound/m_click.mp3");
        });

        btn_prev.setOnMousePressed((e) -> { 
            new Audio().playSound("../sound/m_click.mp3"); 
            pressed("prev");
        });
        btn_prev.setOnMouseExited((e) -> {
            new Animate().hover_deflate(btn_prev);
        });
        btn_prev.setOnMouseEntered((e) -> {
            new Audio().playSound("../sound/m_hover.mp3");
            new Animate().hover_inflate(btn_prev);
        });
    }


    private static void pressed(String button) {
        if (button == "prev") {
            new Animate().end_swipe(SETTINGS_CONTAINER, true).setOnFinished((e) -> {
                remove();
                Menu.btn_info.setDisable(false);
                Menu.btn_start.setDisable(false);
                Menu.btn_setting.setDisable(false);
                
            });
            new Animate().mid_unfade(Menu.bg_menu).setOnFinished((e) -> {
                new Animate().start_swipe(Menu.INTRO, false);
                new Animate().start_swipe(Menu.INTRO_MISC, true);
            });
            btn_prev.setDisable(true);
        }
    }

    
    
    
}

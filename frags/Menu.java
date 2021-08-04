package proj.frags;

import javafx.animation.ScaleTransition;
import javafx.animation.TranslateTransition;
import javafx.geometry.Pos;
import javafx.scene.CacheHint;
import javafx.scene.Cursor;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.util.Duration;
import proj.MainInterface;
import proj.style.Animate;
import proj.style.Audio;
import proj.tetris.Tetris;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.media.MediaPlayer;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

public class Menu implements MainInterface {

    // LOADING ANIMATION
    // Image bg_anim = new Image("tetris/proj/image/bg_anim.gif");
    // ImageView view_bg_anim = new ImageView(bg_anim);

    MediaPlayer bgm_intro;
    
    Image img_menu;
    static ImageView bg_menu;
    TranslateTransition bg_anim;

    static Text title_text;
    Font title_font;
    
    static Button btn_start;
    Font btn_start_font;
    ScaleTransition btn_start_anim;

    Image img_setting;
    static Button btn_setting;

    Image img_info;
    static Button btn_info;    


    public Menu() {
        initObj();
        initAnim();
        initCase();
        initInput();
    }

    private void initInput() {
        btn_start.setOnMousePressed((e) -> {
            new Audio().playSound("../sound/m_click.mp3");
            btn_start.setStyle(
                "-fx-background-radius: 50px; " +
                "-fx-min-width: 225px; " +
                "-fx-min-height: 60px; "+
                "-fx-background-color: #92E3B4"
            );
            pressed("start");
        });
        btn_start.setOnMouseReleased((e) -> {
            btn_start.setStyle(
                "-fx-background-radius: 50px; " +
                "-fx-min-width: 225px; " +
                "-fx-min-height: 60px; "+
                "-fx-background-color: #2ecc71"
            );
        });
        btn_start.setOnMouseExited((e) -> { 
            Animate.hover_deflate(btn_start); 
            btn_start_anim.play(); 
        });
        btn_start.setOnMouseEntered((e) -> { 
            btn_start_anim.stop(); 
            new Audio().playSound("../sound/m_hover.mp3");
            Animate.hover_inflate(btn_start); 
        });



        btn_info.setOnMousePressed((e) -> {
            new Audio().playSound("../sound/m_click.mp3");
        });
        btn_info.setOnMouseEntered((e) -> {
            new Audio().playSound("../sound/m_hover.mp3");
            Animate.hover_inflate(btn_info);
        }); 
        btn_info.setOnMouseExited((e) -> {
            Animate.hover_deflate(btn_info);
        });


        btn_setting.setOnMousePressed((e) -> {
            new Audio().playSound("../sound/m_click.mp3");
            pressed("settings");
        });
        btn_setting.setOnMouseEntered((e) -> {
            new Audio().playSound("../sound/m_hover.mp3");
            Animate.hover_inflate(btn_setting);
        });        
        btn_setting.setOnMouseExited((e) -> {
            Animate.hover_deflate(btn_setting);
        });
        
        
    }


    private void pressed(String button) {
        if (button == "start") {

            Audio.setMusicFadeOut(2);

            Animate.end_fade(INTRO); 
            Animate.end_fade(bg_menu);
            Animate.end_swipe(INTRO_MISC, true);
            Animate.end_swipe(INTRO, false)
                .setOnFinished((e) -> { new Tetris(); });
        }
        else if (button == "settings") {
            Animate.mid_fade(bg_menu).setOnFinished((e) -> {
                Animate.start_swipe(SETTINGS_CONTAINER, true);
                Settings.show();
            });
            Animate.end_swipe(INTRO_MISC, true);
            Animate.end_swipe(INTRO, false);
        }

        btn_info.setDisable(true);
        btn_start.setDisable(true);
        btn_setting.setDisable(true);
        
    }
  

    private void initAnim() {
        // background image animation
        bg_anim = new TranslateTransition();
        bg_anim.setFromY(-55);
        bg_anim.setToY(5);
        bg_anim.setCycleCount(1000);  
        bg_anim.setDuration(Duration.millis(25000));
        bg_anim.setAutoReverse(true);
        bg_anim.setNode(bg_menu);
        bg_anim.play();

        // start button increase decrease animation
        btn_start_anim = new ScaleTransition(); 
        btn_start_anim.setDuration(Duration.millis(1000)); 
        btn_start_anim.setNode(btn_start); 
        btn_start_anim.setFromX(1);
        btn_start_anim.setFromY(1);
        btn_start_anim.setToY(0.9); 
        btn_start_anim.setToX(0.9); 
        btn_start_anim.setCycleCount(1000); 
        btn_start_anim.setAutoReverse(true); 
        btn_start_anim.play(); 

        // init is inflated.. must deflate
        Animate.hover_deflate(btn_info); 
        Animate.hover_deflate(btn_setting);

        Animate.start_fade(title_text); 
        Animate.start_fade(btn_start); 
        Animate.start_fade(bg_menu);
    }

    private void initObj() {
        // background image
        img_menu = new Image("proj/image/bg_menu.png");
        bg_menu = new ImageView(img_menu);
        bg_menu.smoothProperty().set(true);
        bg_menu.setFitWidth(DOCUMENT_WIDTH + 250);
        bg_menu.setFitHeight(DOCUMENT_HEIGHT + 50);
        bg_menu.setX(-120);
        bg_menu.setCache(true);
        bg_menu.setCacheHint(CacheHint.SPEED);        

        // title text object
        title_font = Font.loadFont( Tetris.class.getClassLoader().getResourceAsStream("proj/font/JoystickBold-62LA.ttf"), 140);
        title_text = new Text();
        title_text.setFont(title_font);
        title_text.setText("Tetris");
        title_text.setCache(true);
        title_text.setCacheHint(CacheHint.SPEED);

        // start button object
        btn_start_font = Font.loadFont( Tetris.class.getClassLoader().getResourceAsStream( "proj/font/TrulyMadlyDpad-a72o.ttf"), 30);
        btn_start = new Button("start");
        btn_start.setTextFill(Color.WHITESMOKE);
        btn_start.setCursor(Cursor.CLOSED_HAND);
        btn_start.setFont(btn_start_font);
        btn_start.setMinHeight(50);
        btn_start.setMinWidth(50);
        btn_start.setText("Start");
        btn_start.setStyle(
            "-fx-background-radius: 50px; " +
            "-fx-min-width: 225px; " +
            "-fx-min-height: 60px; "+
            "-fx-background-color: #2ecc71"
        );

        // settings button
        img_setting = new Image("proj/image/ic_setting.png");
        btn_setting = new Button();
        btn_setting.setCursor(Cursor.CLOSED_HAND);
        btn_setting.setGraphic(new ImageView(img_setting) ); 
        btn_setting.setStyle("-fx-background-color: none");

        // info button
        img_info = new Image("proj/image/ic_info.png");
        btn_info = new Button();
        btn_info.setCursor(Cursor.CLOSED_HAND);
        btn_info.setGraphic(new ImageView(img_info) ); 
        btn_info.setStyle("-fx-background-color: none");

        // background music
        new Audio().playMusic("src/proj/sound/bg_wires.mp3");
    }

    private void initCase() {
        INTRO.setAlignment(Pos.CENTER);
        INTRO.getChildren().addAll(title_text,btn_start);

        INTRO_MISC.setTranslateY(120);
        INTRO_MISC.setAlignment(Pos.CENTER);
        INTRO_MISC.getChildren().addAll(btn_info,btn_setting);
        
        INTRO_CONTAINER.setAlignment(Pos.CENTER);
        INTRO_CONTAINER.setMinWidth(DOCUMENT_WIDTH);
        INTRO_CONTAINER.setMinHeight(DOCUMENT_HEIGHT);
        INTRO_CONTAINER.getChildren().addAll(INTRO,INTRO_MISC);
        INTRO_CONTAINER.backgroundProperty().set(
            new Background(new BackgroundFill(BACKGROUND, null, null)));
        
        LAYOUT.getChildren().addAll(INTRO_CONTAINER, bg_menu);
    }
    
}

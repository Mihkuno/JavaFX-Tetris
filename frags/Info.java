package proj.frags;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import proj.MainInterface;
import proj.style.Animate;
import proj.style.Audio;

import java.io.IOException;
import java.awt.Desktop;
import java.net.URI;
import java.net.URISyntaxException;

public class Info implements MainInterface {

    static Font txt_font, link_font;

    static Text control, desc;

    static Button btn_prev, btn_hub, btn_twt;

    public static void show() {
        initObj();
        initCase();
        initInput();

        INFO_CONTAINER.getChildren().addAll(INFO);

        LAYOUT.getChildren().add(INFO_CONTAINER); 
    }

    public static void remove() {
        INFO.getChildren().clear();
        INFO_CONTAINER.getChildren().clear();
        LAYOUT.getChildren().remove(INFO_CONTAINER);
    }

    public static void initObj() {

        link_font = Font.loadFont( Settings.class.getClassLoader().getResourceAsStream( "proj/font/mikachan.ttf"), 14);
        txt_font = Font.loadFont( Settings.class.getClassLoader().getResourceAsStream( "proj/font/TrulyMadlyDpad-a72o.ttf"), 15);


        btn_prev = new Button("Back");
        btn_prev.setFont(txt_font);
        btn_prev.setCursor(Cursor.HAND);
        btn_prev.setGraphic(new ImageView(new Image("proj/image/ic_prev.png",28,28,true,true)) ); 
        btn_prev.setStyle("-fx-background-color: none");
        btn_prev.setTranslateX(-10);

        btn_hub = new Button();
        btn_hub.setFont(txt_font);
        btn_hub.setCursor(Cursor.HAND);
        btn_hub.setGraphic(new ImageView(new Image("proj/image/GitHub-Mark-64px.png",28,28,true,true)) ); 
        btn_hub.setStyle("-fx-background-color: none");
        btn_hub.setTranslateX(80);

        btn_twt = new Button();
        btn_twt.setFont(txt_font);
        btn_twt.setCursor(Cursor.HAND);
        btn_twt.setGraphic(new ImageView(new Image("proj/image/twitter-circled-48.png",35,35,true,true)) ); 
        btn_twt.setStyle("-fx-background-color: none");
        btn_twt.setTranslateX(40);

        control = new Text("W\nA\nS\nD\nSPACE\nSHIFT");
        control.setFont(txt_font);

        desc = new Text("\t:\trotate block\n\t:\tmove left\n\t:\tmove down\n\t:\tmove right\n\t:\thard drop\n\t:\thold block");
        desc.setFont(txt_font);

        control.setTranslateX(10);

        new Animate().hover_deflate(btn_prev);
        new Animate().hover_deflate(btn_twt);
        new Animate().hover_deflate(btn_hub);
    }

    public static void initCase() {

        INFO.setGridLinesVisible(false);

        INFO.setMaxWidth(300);
        INFO.setAlignment(Pos.CENTER);
        INFO.setPadding(new Insets(60, 30, 60, 30)); 
        INFO.setHgap(10);
        INFO.setVgap(50);   
        INFO.add(control,  0, 0);
        INFO.add(desc,     1, 0);
        INFO.add(btn_prev, 0, 1);
        INFO.add(btn_hub,  1, 1);
        INFO.add(btn_twt,  1, 1);
        INFO.setStyle("-fx-border-width:3px;-fx-border-color:#3d3d3d; -fx-border-radius: 10px;");

        INFO_CONTAINER.setTranslateY(500);
        INFO_CONTAINER.setAlignment(Pos.CENTER);
        INFO_CONTAINER.setMinWidth(DOCUMENT_WIDTH);
        INFO_CONTAINER.setMinHeight(DOCUMENT_HEIGHT);

        if (proj.frags.Settings.SHOWGRID) { INFO.setGridLinesVisible(true); }
    }

    public static void initInput() {

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

        btn_hub.setOnMousePressed((e) -> { 
            new Audio().playSound("../sound/m_click.mp3"); 
            pressed("hub");
        });
        btn_hub.setOnMouseExited((e) -> {
            new Animate().hover_deflate(btn_hub);
        });
        btn_hub.setOnMouseEntered((e) -> {
            new Audio().playSound("../sound/m_hover.mp3");
            new Animate().hover_inflate(btn_hub);
        });

        btn_twt.setOnMousePressed((e) -> { 
            new Audio().playSound("../sound/m_click.mp3"); 
            pressed("twt");
        });
        btn_twt.setOnMouseExited((e) -> {
            new Animate().hover_deflate(btn_twt);
        });
        btn_twt.setOnMouseEntered((e) -> {
            new Audio().playSound("../sound/m_hover.mp3");
            new Animate().hover_inflate(btn_twt);
        });
    }


    private static void pressed(String button) {
        if (button == "prev") {
            new Animate().end_swipe(INFO_CONTAINER, true).setOnFinished((e) -> {
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

        if  (button == "hub") {
            openWebpage("https://github.com/Mihkuno");
        }

        if  (button == "twt") {
            openWebpage("https://twitter.com/Mihkuno");
        }
    }

    public static void openWebpage(String url) {
        // try {
        //     new ProcessBuilder("x-www-browser", url).start();
        // } catch (IOException e) {
        //     e.printStackTrace();
        // }

        if (Desktop.isDesktopSupported() &&     
            Desktop.getDesktop().isSupported(Desktop.Action.BROWSE)) {
            try {
                Desktop.getDesktop().browse(new URI(url));
            }

            catch (IOException | URISyntaxException e) {
                e.printStackTrace();
            }
        }
        else {
            System.out.println("not supported");
        }
    }

    
    
    
}

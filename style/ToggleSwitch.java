package proj.style;

import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import proj.MainInterface;

public class ToggleSwitch extends StackPane implements MainInterface {
    public final Rectangle back = new Rectangle(30, 10, Color.RED);
    public final Button button = new Button();
    public String buttonStyleOff = "-fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.2), 0.2, 0.0, 0.0, 2); -fx-background-color: WHITE;";
    public String buttonStyleOn = "-fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.2), 0.2, 0.0, 0.0, 2); -fx-background-color: #00893d;";
    public boolean state;

    private void init() {
        getChildren().addAll(back, button);
        setMinSize(30, 15);
        back.maxWidth(30);
        back.minWidth(30);
        back.maxHeight(10);
        back.minHeight(10);
        back.setArcHeight(back.getHeight());
        back.setArcWidth(back.getHeight());
        back.setFill(Color.valueOf("#ced5da"));

        Double r = 3.0;
        button.setShape(new Circle(r));
        setAlignment(button, Pos.CENTER_LEFT);
        button.setTranslateY(-1);
        button.setMaxSize(15, 15);
        button.setMinSize(15, 15);
        button.setStyle(buttonStyleOff);
    }

    public ToggleSwitch() {
        init();

        button.setFocusTraversable(false);
        // setOnMouseClicked(this);
        // button.setOnMouseClicked(this);
    }

    public Button getButton() {
        return button;
    }

    public boolean getState() {
        return state;
    }

    public void setState(boolean state) {
        this.state = state;
    }

    // @Override
    // public void handle(Event e) {
    //     if (state) {
    //         button.setStyle(buttonStyleOff);
    //         back.setFill(Color.valueOf("#ced5da"));
    //         setAlignment(button, Pos.CENTER_LEFT);
    //         state = false;
            
    //     } else {
    //         button.setStyle(buttonStyleOn);
    //         back.setFill(Color.valueOf("#80C49E"));
    //         setAlignment(button, Pos.CENTER_RIGHT);
    //         state = true;
    //     }
    // }



}
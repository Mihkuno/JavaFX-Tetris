package proj.style;

import javafx.animation.FadeTransition;
import javafx.animation.ScaleTransition;
import javafx.animation.TranslateTransition;
import javafx.scene.Node;
import javafx.util.Duration;

public class Animate {
    
    ScaleTransition increase;
    ScaleTransition decrease;
    FadeTransition fade;
    TranslateTransition swipe;
    TranslateTransition bounce;


    public void hover_inflate(Node node) {
        increase = new ScaleTransition(); 
        increase.setDuration(Duration.millis(100)); 
        increase.setNode(node); 
        increase.setToY(1); 
        increase.setToX(1); 
        increase.play();
        
    }

    public void hover_deflate(Node node) {
        decrease = new ScaleTransition(); 
        decrease.setDuration(Duration.millis(100)); 
        decrease.setNode(node); 
        decrease.setToY(0.9); 
        decrease.setToX(0.9); 
        decrease.play(); 
    }

    public FadeTransition start_fade(Node node) {
        fade = new FadeTransition();  
        fade.setDuration(Duration.millis(1300)); 
        fade.setCycleCount(1);  
        fade.setFromValue(0);  
        fade.setToValue(1);   
        fade.setNode(node);
        fade.play();

        return fade;
    }

    public FadeTransition mid_fade(Node node) {
        fade = new FadeTransition();  
        fade.setDuration(Duration.millis(1000)); 
        fade.setCycleCount(1);  
        fade.setFromValue(1);  
        fade.setToValue(0.5);   
        fade.setNode(node);
        fade.play();
        return fade;
    }

    public FadeTransition mid_unfade(Node node) {
        fade = new FadeTransition();  
        fade.setDuration(Duration.millis(1000)); 
        fade.setCycleCount(1);  
        fade.setFromValue(0.5);  
        fade.setToValue(1);   
        fade.setNode(node);
        fade.play();
        return fade;
    }


    public void end_fade(Node node) {
        fade = new FadeTransition();  
        fade.setDuration(Duration.millis(1800)); 
        fade.setCycleCount(1);  
        fade.setFromValue(1);  
        fade.setToValue(0);   
        fade.setNode(node);
        fade.play();
    }

    public TranslateTransition start_swipe(Node node, boolean direction) {

        int init = direction ? -20 : 20;
        double z = direction ? 500 : -500;
        double current = 0;

        swipe = new TranslateTransition();  
        swipe.setDelay(Duration.millis(10));
        swipe.setDuration(Duration.millis(400)); 
        swipe.setCycleCount(1);  

        if (node.getTranslateY() == 0) {
            swipe.setFromY(z);
            current = init;
            swipe.setToY(current);
        }
        else {
            swipe.setFromY(node.getTranslateY());
            current = (node.getTranslateY() - z) + init;
            swipe.setToY(current);
        }
        
        swipe.setNode(node);
        swipe.play();

        bounce = new TranslateTransition();  
        bounce.setDelay(Duration.millis(410));
        bounce.setDuration(Duration.millis(300)); 
        bounce.setCycleCount(1);  
        bounce.setFromY(current);
        bounce.setToY(current-init);
        bounce.setNode(node);
        bounce.play();

        
        return bounce;
    }

    public TranslateTransition start_swipe(Node node, boolean direction, int speed) {

        int init = direction ? -20 : 20;
        double z = direction ? 500 : -500;
        double current = 0;

        swipe = new TranslateTransition();  
        swipe.setDelay(Duration.millis(10));
        swipe.setDuration(Duration.millis(400+speed)); 
        swipe.setCycleCount(1);  

        if (node.getTranslateY() == 0) {
            swipe.setFromY(z);
            current = init;
            swipe.setToY(current);
        }
        else {
            swipe.setFromY(node.getTranslateY());
            current = (node.getTranslateY() - z) + init;
            swipe.setToY(current);
        }
        
        swipe.setNode(node);
        swipe.play();

        bounce = new TranslateTransition();  
        bounce.setDelay(Duration.millis(410+speed));
        bounce.setDuration(Duration.millis(300+speed)); 
        bounce.setCycleCount(1);  
        bounce.setFromY(current);
        bounce.setToY(current-init);
        bounce.setNode(node);
        bounce.play();

        
        return bounce;
    }

    public TranslateTransition end_swipe(Node node, boolean direction) {

        int init = direction ? -20 : 20;
        double z = direction ? 500 : -500;

        swipe = new TranslateTransition();  
        swipe.setDuration(Duration.millis(450)); 
        swipe.setCycleCount(1);  
        swipe.setFromY(node.getTranslateY());
        swipe.setToY(node.getTranslateY() + init);
        swipe.setNode(node);
        swipe.play();

        bounce = new TranslateTransition();  
        bounce.setDelay(Duration.millis(450));
        bounce.setDuration(Duration.millis(450)); 
        bounce.setCycleCount(1);  
        bounce.setFromY(node.getTranslateY() + init);
        bounce.setToY(node.getTranslateY() + z); 
        bounce.setNode(node);
        bounce.play();

        return bounce;
    }


    
}

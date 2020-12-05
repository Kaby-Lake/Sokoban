package com.ae2dms.UI;

import javafx.animation.ScaleTransition;
import javafx.scene.Node;
import javafx.util.Duration;

public class ViewUtilities {

    /**
     * load the given node with a scale effect
     * @param view the view to add effect
     */
    public static void loadViewWithEffect(Node view) {
        
        ScaleTransition scaleTransition = new ScaleTransition();
        //Setting the duration for the transition
        scaleTransition.setDuration(Duration.millis(500));
        //Setting the node for the transition
        scaleTransition.setNode(view);
        //Setting the dimensions for scaling
        scaleTransition.setFromX(2);
        scaleTransition.setFromY(2);
        scaleTransition.setToX(1);
        scaleTransition.setToY(1);
        //Playing the animation
        scaleTransition.play();
    }
}

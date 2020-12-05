package com.ae2dms.UI;

import javafx.animation.FadeTransition;
import javafx.animation.Interpolator;
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
        scaleTransition.setInterpolator(Interpolator.EASE_BOTH);
        //Playing the animation
        scaleTransition.play();
    }

    public static void popUp(Node view) {
        view.setVisible(true);
        ScaleTransition scaleTransition = new ScaleTransition(Duration.millis(300), view);
        scaleTransition.setFromX(0.01);
        scaleTransition.setFromY(0.01);
        scaleTransition.setToX(1);
        scaleTransition.setToY(1);
        scaleTransition.setInterpolator(Interpolator.EASE_BOTH);
        scaleTransition.play();
    }

    public static void fadeOut(Node view) {
        FadeTransition fadeTransition = new FadeTransition(Duration.millis(300), view);
        fadeTransition.setFromValue(1);
        fadeTransition.setToValue(0);
        fadeTransition.setOnFinished((event) -> {
            view.setVisible(false);
            view.setOpacity(1);
        });

        fadeTransition.play();

    }
}

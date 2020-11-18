package com.ae2dms.UI;

import javafx.animation.ScaleTransition;
import javafx.scene.Node;
import javafx.util.Duration;

import javafx.animation.Interpolator;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Group;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.util.Duration;

public class ViewUtilities {

    public static void loadViewWithEffect(Node view) {
        
        ScaleTransition scaleTransition = new ScaleTransition();
        //Setting the duration for the transition
        scaleTransition.setDuration(Duration.millis(2000));
        //Setting the node for the transition
        scaleTransition.setNode(view);
        //Setting the dimensions for scaling
        scaleTransition.setFromX(0);
        scaleTransition.setFromY(0);
        scaleTransition.setToX(1);
        scaleTransition.setToY(1);
        //Setting auto reverse value to true
        scaleTransition.setAutoReverse(false);
        //Playing the animation
        scaleTransition.play();
    }
}

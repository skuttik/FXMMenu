/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.sku.fxmmenu;

import javafx.animation.Interpolator;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.control.Label;
import javafx.scene.effect.BlendMode;
import javafx.scene.paint.Color;
import javafx.scene.shape.Arc;
import javafx.scene.shape.ArcType;
import javafx.scene.shape.Circle;
import javafx.scene.shape.StrokeLineCap;
import javafx.scene.text.Font;
import javafx.util.Duration;

/**
 *
 * @author skuttik
 */
public class FXMMenuItem extends Group {

    private final Circle circle;
    private final Label label;
    private final Arc arc;
    private double offsetX;
    private double offsetY;
    private double refSize;
    private boolean held;
    private double holdingLength;
    private Timeline longPressTL;
    private EventHandler<ActionEvent> pressHandler = null;
    private EventHandler<ActionEvent> holdHandler = null;
    private EventHandler<ActionEvent> releaseHandler;

    public FXMMenuItem(Color bgColor, Color labelColor, String text) {
        holdingLength = 2.0;
        circle = new Circle(10, bgColor);
        label = new Label(text);
        arc = new Arc();
        arc.setMouseTransparent(true);
        arc.setType(ArcType.OPEN);
        arc.setStrokeLineCap(StrokeLineCap.ROUND);
        arc.setStroke(Color.BEIGE);
        arc.setFill(new Color(0, 0, 0, 0));
        arc.setStartAngle(90);
        label.setTextFill(labelColor);
        label.setMouseTransparent(true);
        offsetX = 0.0;
        offsetY = 0.0;
        held = false;
        getChildren().addAll(circle, label, arc);
    }

    void init(double size, double offsetX, double offsetY) {
        refSize = size;
        circle.setRadius(refSize);
        this.offsetX = offsetX;
        this.offsetY = offsetY;
        label.setFont(new Font(refSize));
        arc.setRadiusX(refSize + size / 20);
        arc.setRadiusY(refSize + size / 20);
        arc.setStrokeWidth(size / 10);
        arc.setBlendMode(BlendMode.SOFT_LIGHT);
        circle.setOnMouseReleased(e -> {
            try {
                if (held) {
                    if (releaseHandler != null) {
                        releaseHandler.handle(new ActionEvent(this, null));
                    }
                } else {
                    if (pressHandler != null) {
                        pressHandler.handle(new ActionEvent(this, null));
                    }
                }
            } catch (Exception ex) {
                Thread.currentThread().getUncaughtExceptionHandler().uncaughtException(Thread.currentThread(), ex);
            }
            longPressTL.stop();
            new Timeline(new KeyFrame(Duration.seconds(holdingLength/10), new KeyValue(arc.lengthProperty(), 0, Interpolator.EASE_OUT))).play();
            held = false;
        });

        longPressTL = new Timeline(
                new KeyFrame(Duration.seconds(holdingLength), (ActionEvent event) -> {
                    held = true;
                    if (holdHandler != null) {
                        try {
                            holdHandler.handle(new ActionEvent(this, null));
                        } catch (Exception ex) {
                            Thread.currentThread().getUncaughtExceptionHandler().uncaughtException(Thread.currentThread(), ex);
                        }
                    }
                }),
                new KeyFrame(Duration.seconds(holdingLength/4), new KeyValue(arc.lengthProperty(), arc.lengthProperty().getValue(), Interpolator.DISCRETE)),
                new KeyFrame(Duration.seconds(holdingLength), new KeyValue(arc.lengthProperty(), -360, Interpolator.LINEAR))
        );
        longPressTL.setOnFinished(e -> {

        });

        circle.setOnMousePressed(e -> {
            longPressTL.play();
        });
    }

    void setMenuCenter(double x, double y) {
        circle.setCenterX(offsetX + x);
        circle.setCenterY(offsetY + y);
        label.setLayoutX(offsetX + x - refSize * .3);
        label.setLayoutY(offsetY + y - refSize * .74);
        arc.setCenterX(offsetX + x);
        arc.setCenterY(offsetY + y);
    }

    public double getHoldingLength() {
        return holdingLength;
    }

    public void setHoldingLength(double holdingLength) {
        this.holdingLength = holdingLength;
    }

    public FXMMenuItem setOnPressed(EventHandler<ActionEvent> handler) {
        pressHandler = handler;
        return this;
    }

    public FXMMenuItem setOnHold(EventHandler<ActionEvent> handler) {
        holdHandler = handler;
        return this;
    }

    public FXMMenuItem setOnHoldReleased(EventHandler<ActionEvent> handler) {
        releaseHandler = handler;
        return this;
    }

}

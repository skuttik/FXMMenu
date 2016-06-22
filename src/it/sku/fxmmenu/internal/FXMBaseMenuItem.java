/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.sku.fxmmenu.internal;

import javafx.animation.Interpolator;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.effect.BlendMode;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.shape.Arc;
import javafx.scene.shape.ArcType;
import javafx.scene.shape.StrokeLineCap;
import javafx.scene.text.Font;
import javafx.scene.text.TextAlignment;
import javafx.util.Duration;

/**
 *
 * @author skuttik
 */
abstract public class FXMBaseMenuItem implements FXMAbstractItem {

    private boolean activeValue = true;

    private final Group itemGroup;
    private Label label = null;
    private Label tooltip = null;
    private final Arc arc;
    private double offsetX;
    private double offsetY;
    private double refSize;
    private boolean held;
    private double holdingLength;
    private final Timeline longPressTL;
    private EventHandler<ActionEvent> pressHandler = null;
    private EventHandler<ActionEvent> holdHandler = null;
    private EventHandler<ActionEvent> releaseHandler = null;
    private final ImageView icon;

    public FXMBaseMenuItem(Color labelColor, String labelText, Image itemImage, String tooltipText) {
        holdingLength = 1.5;
        if (itemImage != null) {
            this.icon = new ImageView(itemImage);
        } else {
            this.icon = null;
        }

        if (labelText != null && !labelText.equals("")) {
            label = new Label(labelText);
        }

        if (tooltipText != null) {
            tooltip = new Label(tooltipText);
        }

        arc = new Arc();

        itemGroup = new Group();

        if (icon != null) {
            icon.setOnMouseReleased(e -> {
                if (e.getButton() == MouseButton.PRIMARY) {
                    applyMouseReleased(e);
                } else {
                    e.consume();
                }
            });
            icon.setOnMousePressed(e -> {
                if (e.getButton() == MouseButton.PRIMARY) {
                    applyMousePressed(e);
                } else {
                    e.consume();
                }
            });
            icon.setOnMouseEntered(e -> {
                applyMouseOver(e);
            });
            icon.setOnMouseExited(e -> {
                applyMouseOff(e);
            });
        }

        if (label != null) {
            label.setTextFill(labelColor);
            label.setMouseTransparent(true);
            label.setTextAlignment(TextAlignment.CENTER);
            label.setOnMouseEntered(e -> {
                applyMouseOver(e);
            });
            label.setOnMouseExited(e -> {
                applyMouseOff(e);
            });
        }

        arc.setMouseTransparent(true);
        arc.setType(ArcType.OPEN);
        arc.setStrokeLineCap(StrokeLineCap.ROUND);
        arc.setStroke(Color.BEIGE);
        arc.setFill(new Color(0, 0, 0, 0));
        arc.setStartAngle(90);

        if (tooltip != null) {
            tooltip.setMouseTransparent(true);
            tooltip.setVisible(false);
            tooltip.setTextAlignment(TextAlignment.CENTER);
            tooltip.setStyle("-fx-font-size: 14px; -fx-text-fill: wheat;");
            tooltip.setBlendMode(BlendMode.ADD);
        }

        offsetX = 0.0;
        offsetY = 0.0;
        held = false;

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
                new KeyFrame(Duration.seconds(holdingLength / 4), new KeyValue(arc.lengthProperty(), arc.lengthProperty().getValue(), Interpolator.DISCRETE)),
                new KeyFrame(Duration.seconds(holdingLength), new KeyValue(arc.lengthProperty(), -360, Interpolator.LINEAR))
        );
        longPressTL.setOnFinished(e -> {
        });
    }

    @Override
    public Node getNode() {
        return itemGroup;
    }

    protected Group getItemGroup() {
        return itemGroup;
    }

    protected void initialize() {
        itemGroup.getChildren().add(arc);
        if (icon != null) {
            itemGroup.getChildren().add(icon);
        }
        if (label != null) {
            itemGroup.getChildren().add(label);
        }
        if (tooltip != null) {
            itemGroup.getChildren().add(tooltip);
        }
    }

    protected void baseArrange(double size, int totalNumber, int index) {
        refSize = size * .25;
        double d = 2 * Math.PI / totalNumber;
        if (index == -1) {
            offsetX = 0;
            offsetY = 0;
        } else {
            offsetX = size * 0.62 * Math.cos(d * index - Math.PI / 2);
            offsetY = size * 0.62 * Math.sin(d * index - Math.PI / 2);
        }

        if (label != null) {
            label.setFont(new Font(refSize));
        }

        if (icon != null) {
            double scaleFactorX = refSize * 2 / icon.getImage().getWidth();
            double scaleFactorY = refSize * 2 / icon.getImage().getHeight();

            icon.setScaleX(scaleFactorX);
            icon.setScaleY(scaleFactorY);
        }

        arc.setRadiusX(refSize + size / 20);
        arc.setRadiusY(refSize + size / 20);
        arc.setStrokeWidth(size / 10);
        arc.setBlendMode(BlendMode.SCREEN);
    }

    protected double getOffsetX() {
        return offsetX;
    }

    protected double getOffsetY() {
        return offsetY;
    }

    protected double getRefSize() {
        return refSize;
    }

    public String getTooltip() {
        return tooltip.getText();
    }

    protected final void applyMousePressed(MouseEvent e) {
        if (activeValue && holdHandler != null) {
            longPressTL.play();
        }
        e.consume();
    }

    protected final void applyMouseOver(MouseEvent e) {
        if (activeValue) {
            if (tooltip != null) {
                tooltip.setVisible(true);
            }
        }
        e.consume();
    }

    protected final void applyMouseOff(MouseEvent e) {
        if (activeValue) {
            if (tooltip != null) {
                tooltip.setVisible(false);
            }
        }
        e.consume();
    }

    protected final void applyMouseReleased(MouseEvent e) {
        if (activeValue) {
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
            new Timeline(new KeyFrame(Duration.seconds(holdingLength / 10), new KeyValue(arc.lengthProperty(), 0, Interpolator.EASE_OUT))).play();
            held = false;
        }
        e.consume();
    }

    protected void setMenuCenter(double x, double y) {
        if (label != null) {
            label.setLayoutX(offsetX + x - refSize * .3);
            label.setLayoutY(offsetY + y - refSize * .74);
        }
        if (tooltip != null) {
            tooltip.setLayoutX(offsetX * 2 + x);
            tooltip.setLayoutY(offsetY * 2 + y);
        }
        if (icon != null) {
            icon.setX(offsetX + x - icon.getImage().getWidth() / 2);
            icon.setY(offsetY + y - icon.getImage().getHeight() / 2);
        }
        arc.setCenterX(offsetX + x);
        arc.setCenterY(offsetY + y);
    }

    @Override
    public void setActive(boolean value) {
        activeValue = value;
    }

    @Override
    public void close(boolean animated) {
    }

    public double getHoldingLength() {
        return holdingLength;
    }

    public void setHoldingLength(double holdingLength) {
        this.holdingLength = holdingLength;
    }

    public FXMBaseMenuItem setOnPressed(EventHandler<ActionEvent> handler) {
        pressHandler = handler;
        return this;
    }

    public FXMBaseMenuItem setOnHold(EventHandler<ActionEvent> handler) {
        holdHandler = handler;
        return this;
    }

    public FXMBaseMenuItem setOnHoldReleased(EventHandler<ActionEvent> handler) {
        releaseHandler = handler;
        return this;
    }

}

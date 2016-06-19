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
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.effect.BlendMode;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.shape.Arc;
import javafx.scene.shape.ArcType;
import javafx.scene.shape.Circle;
import javafx.scene.shape.StrokeLineCap;
import javafx.scene.text.Font;
import javafx.util.Duration;
import javax.swing.ImageIcon;

/**
 *
 * @author skuttik
 */
public class FXMMenuItem implements FXMAbstractItem {
    private boolean activeValue = true;

    public enum ItemStyle {

        CIRCULAR,
        PIE
    }

    private final Group itemGroup;
    private Circle circle = null;
    private Label label = null;
    private Label tooltip = null;
    private Arc pieSlice = null;
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
    private Color bgColor;
    private Color labelColor;
    private String labelText;
    private final ItemStyle itemStyle;
    private ImageIcon icon;

    public FXMMenuItem(ItemStyle itemStyle, Color bgColor, Color labelColor, String labelText, ImageIcon icon, String tooltipText) {
        holdingLength = 1.5;
        this.itemStyle = itemStyle;
        this.bgColor = bgColor;
        this.labelColor = labelColor;
        this.labelText = labelText;

        itemGroup = new Group();

        switch (itemStyle) {
            case CIRCULAR:
                circle = new Circle(10, bgColor);
                circle.setMouseTransparent(false);
                circle.setOnMouseReleased(e -> {
                    if (e.getButton() == MouseButton.PRIMARY) {
                        applyMouseReleased(e);
                    } else {
                        e.consume();
                    }
                });
                circle.setOnMousePressed(e -> {
                    if (e.getButton() == MouseButton.PRIMARY) {
                        applyMousePressed(e);
                    } else {
                        e.consume();
                    }
                });
                circle.setOnMouseEntered(e -> {
                    applyMouseOver(e);
                });
                circle.setOnMouseExited(e -> {
                    applyMouseOff(e);
                });
                itemGroup.getChildren().add(circle);
                break;
            case PIE:
                pieSlice = new Arc();
                pieSlice.setMouseTransparent(false);
                pieSlice.setType(ArcType.OPEN);
                pieSlice.setStrokeLineCap(StrokeLineCap.BUTT);
                pieSlice.setStroke(bgColor);
                pieSlice.setFill(new Color(0.0, 0.0, 0.0, 1.0));
                pieSlice.setOnMouseReleased(e -> {
                    if (e.getButton() == MouseButton.PRIMARY) {
                        applyMouseReleased(e);
                    } else {
                        e.consume();
                    }
                });
                pieSlice.setOnMousePressed(e -> {
                    if (e.getButton() == MouseButton.PRIMARY) {
                        applyMousePressed(e);
                    } else {
                        e.consume();
                    }
                });
                pieSlice.setOnMouseEntered(e -> {
                    applyMouseOver(e);
                });
                pieSlice.setOnMouseExited(e -> {
                    applyMouseOff(e);
                });
                itemGroup.getChildren().add(pieSlice);
                break;
        }

        if (labelText != null && !labelText.equals("")) {
            label = new Label(labelText);
            label.setTextFill(labelColor);
            label.setMouseTransparent(true);
            label.setOnMouseEntered(e -> {
                applyMouseOver(e);
            });
            label.setOnMouseExited(e -> {
                applyMouseOff(e);
            });
            itemGroup.getChildren().add(label);
        }

        arc = new Arc();
        arc.setMouseTransparent(true);
        arc.setType(ArcType.OPEN);
        arc.setStrokeLineCap(StrokeLineCap.ROUND);
        arc.setStroke(Color.BEIGE);
        arc.setFill(new Color(0, 0, 0, 0));
        arc.setStartAngle(90);
        itemGroup.getChildren().add(arc);

        if (tooltipText != null) {
            tooltip = new Label(tooltipText);
            tooltip.setMouseTransparent(true);
            tooltip.setVisible(false);
            itemGroup.getChildren().add(tooltip);
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

    @Override
    public void init(double size, int totalNumber, int index) {
        refSize = size * .25;
        double d = 2 * Math.PI / totalNumber;
        if (index == -1) {
            offsetX = 0;
            offsetY = 0;
        } else {
            offsetX = size * 0.62 * Math.cos(d * index - Math.PI / 2);
            offsetY = size * 0.62 * Math.sin(d * index - Math.PI / 2);
        }

        if (circle != null) {
            circle.setRadius(refSize);
        }
        if (pieSlice != null) {
            if (index == -1) {
                System.err.println(this.getClass() + ": ERROR - central item cannot be PIE");
            } else {
                double sliceLen = 360 / totalNumber - 6;
                pieSlice.setLength(sliceLen);
                pieSlice.setStartAngle(90 - sliceLen / 2 - 360 / totalNumber * index);
                pieSlice.setStrokeWidth(size * 0.7);
                pieSlice.setRadiusX(size * .7);
                pieSlice.setRadiusY(size * .7);
            }
        }

        if (label != null) {
            label.setFont(new Font(refSize));
        }

        arc.setRadiusX(refSize + size / 20);
        arc.setRadiusY(refSize + size / 20);
        arc.setStrokeWidth(size / 10);
        arc.setBlendMode(BlendMode.SCREEN);
    }

    public ItemStyle getItemStyle() {
        return itemStyle;
    }

    public Color getBgColor() {
        return bgColor;
    }

    public Color getLabelColor() {
        return labelColor;
    }

    public String getLabelText() {
        return labelText;
    }

    public ImageIcon getIcon() {
        return icon;
    }

    public String getTooltip() {
        return tooltip.getText();
    }

    private void applyMousePressed(MouseEvent e) {
        if (activeValue) {
            longPressTL.play();
        }
        e.consume();
    }

    private void applyMouseOver(MouseEvent e) {
        if (activeValue) {
            if (tooltip != null) {
                tooltip.setVisible(true);
            }
        }
        e.consume();
    }

    private void applyMouseOff(MouseEvent e) {
        if (activeValue) {
            if (tooltip != null) {
                tooltip.setVisible(false);
            }
        }
        e.consume();
    }

    private void applyMouseReleased(MouseEvent e) {
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

    @Override
    public void setMenuCenter(Group container, double x, double y) {
        if (circle != null) {
            circle.setCenterX(offsetX + x);
            circle.setCenterY(offsetY + y);
        }
        if (pieSlice != null) {
            pieSlice.setCenterX(offsetX + x);
            pieSlice.setCenterY(offsetX + y);
        }
        if (label != null) {
            label.setLayoutX(offsetX + x - refSize * .3);
            label.setLayoutY(offsetY + y - refSize * .74);
        }
        arc.setCenterX(offsetX + x);
        arc.setCenterY(offsetY + y);
    }
   
    @Override
        public void setActive(boolean value) {
        activeValue = value;
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

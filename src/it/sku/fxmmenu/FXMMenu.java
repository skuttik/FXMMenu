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
import javafx.scene.Group;
import javafx.scene.effect.BlendMode;
import javafx.scene.paint.Color;
import javafx.scene.shape.Arc;
import javafx.scene.shape.ArcType;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Polygon;
import javafx.scene.shape.Shape;
import javafx.scene.shape.StrokeLineCap;
import javafx.util.Duration;

/**
 *
 * @author skuttik
 */
public class FXMMenu extends Group {

    public enum Style {

        EMPTY,
        CIRCULAR,
        POLYGONAL_CORNER,
        POLYGONAL_BASE
    }

    private Group container = null;
    private Circle baseCircle = null;
    private Polygon basePolygon = null;
    double size;
    private Color color;
    private FXMAbstractItem items[];
    private FXMMenuItem centralItem = null;
    private final Style baseStyle;
    private double centerX = 0;
    private double centerY = 0;

    private Timeline longPressTL;
    private Arc arc;
    private Timeline disappearTL;

    public FXMMenu(double size, Style style, Color color) {
        this.size = size;
        this.color = color;
        this.baseStyle = style;
        create();
        arrange();
    }

    private void create() {
        if (baseStyle == Style.CIRCULAR) {
            baseCircle = new Circle(size, color);
            baseCircle.opacityProperty().set(1.0);
            baseCircle.setVisible(false);
            baseCircle.setMouseTransparent(false);
            getChildren().add(baseCircle);
        }

        arc = new Arc();
        arc.setMouseTransparent(false);
        arc.setType(ArcType.OPEN);
        arc.setStrokeLineCap(StrokeLineCap.ROUND);
        arc.setStroke(Color.BEIGE);
        arc.setFill(new Color(0, 0, 0, 0));
        arc.setStartAngle(90);

        longPressTL = null;
        disappearTL = null;
    }

    public final void addCentralItem(FXMMenuItem item) {
        centralItem = item;
        centralItem.getNode().setVisible(false);
        centralItem.init(size, -1, -1);
        getChildren().add(centralItem.getNode());
    }

    public final void add(FXMSubMenu submenu) {
        addItem(submenu);
        submenu.setParentMenu(this);
        getChildren().add(submenu);
    }

    public final void add(FXMMenuItem item) {
        addItem(item);
    }

    private void addItem(FXMAbstractItem item) {
        item.getNode().setVisible(false);
        if (items != null) {
            int s = items.length;
            FXMAbstractItem newItems[] = new FXMAbstractItem[s + 1];
            System.arraycopy(this.items, 0, newItems, 0, s);
            newItems[s] = item;
            items = newItems;
        } else {
            items = new FXMAbstractItem[1];
            items[0] = item;
        }
    }

    private double[] getPolygonVertices(int number, double delta) {
        double pts[] = new double[number * 2];
        for (int i = 0; i < number; i++) {
            double factor = 1;
            double angle = 0;
            if (baseStyle == Style.POLYGONAL_BASE) {
                angle = delta * i - Math.PI / 2;
                factor = 1.1;
            } else {
                angle = delta * i - Math.PI / 2 + delta / 2;
                factor = 1.4;
            }
            pts[i * 2] = centerX + size * factor * Math.cos(angle);
            pts[i * 2 + 1] = centerY + size * factor * Math.sin(angle);
        }
        return pts;
    }

    private void arrange() {
        if (items == null) {
            return;
        }
        for (FXMAbstractItem item : items) {
            getChildren().remove(item.getNode());
        }
        int n = items.length;
        double d = 2 * Math.PI / n;
        if (baseStyle == Style.POLYGONAL_CORNER || baseStyle == Style.POLYGONAL_BASE) {
            getChildren().remove(basePolygon);
            basePolygon = new Polygon(getPolygonVertices(n, d));
            basePolygon.opacityProperty().set(1.0);
            basePolygon.setMouseTransparent(false);
            getChildren().add(basePolygon);
        }
        int idx = 0;
        for (FXMAbstractItem item : items) {
            item.init(size, n, idx);
            item.getNode().setVisible(true);
            getChildren().add(item.getNode());
            idx++;
        }
        if (centralItem != null) {
            getChildren().remove(centralItem.getNode());
            getChildren().add(centralItem.getNode());
        }
    }

    public double getSize() {
        return size;
    }

    public void setSize(double size) {
        this.size = size;
    }

    public Color getColor() {
        return color;
    }

    public void setColor(Color color) {
        this.color = color;
    }

    public void delayedShow(Group container, Duration delay, double x, double y, Duration duration) {
        arc.setLayoutX(x);
        arc.setLayoutY(y);
        arc.setRadiusX(size / 2);
        arc.setRadiusY(size / 2);
        arc.setStrokeWidth(size / 10);
        arc.setBlendMode(BlendMode.SOFT_LIGHT);
        getChildren().add(arc);

        disappearTL = new Timeline(new KeyFrame(duration.multiply(1.2), new KeyValue(arc.opacityProperty(), 0.0, Interpolator.EASE_BOTH)));
        disappearTL.setOnFinished(e -> getChildren().remove(arc));

        arc.opacityProperty().setValue(0.0);
        longPressTL = new Timeline(
                new KeyFrame(delay.divide(2), new KeyValue(arc.lengthProperty(), arc.lengthProperty().getValue(), Interpolator.DISCRETE)),
                new KeyFrame(delay, new KeyValue(arc.lengthProperty(), -360, Interpolator.LINEAR)),
                new KeyFrame(delay, new KeyValue(arc.opacityProperty(), 1.0, Interpolator.EASE_IN))
        );
        longPressTL.setOnFinished(e -> {
            disappearTL.play();
            show(container, x, y, duration);
        });
        longPressTL.play();
    }

    public void interruptDelayedShow() {
        if (longPressTL != null) {
            longPressTL.stop();
        }
        if (disappearTL != null) {
            disappearTL.play();
        }
    }

    public void hide(Duration duration) {
        Shape base = null;
        switch (baseStyle) {
            case CIRCULAR:
                if (baseCircle != null) {
                    base = baseCircle;
                }
                break;
            case POLYGONAL_BASE:
            case POLYGONAL_CORNER:
                if (basePolygon != null) {
                    base = basePolygon;
                }
                break;
        }
        if (base != null) {
            base.setVisible(false);
        }
        centralItem.getNode().setVisible(false);
        for (FXMAbstractItem item : items) {
            item.getNode().setVisible(false);
        }
    }

    public void show(Group container, double x, double y, Duration duration) {
        if (this.container == null) {
            this.container = container;
            container.getChildren().add(this);
        }
        centerX = x;
        centerY = y;

        Shape base = null;
        switch (baseStyle) {
            case CIRCULAR:
                if (baseCircle != null) {
                    baseCircle.setCenterX(x);
                    baseCircle.setCenterY(y);
                    baseCircle.setVisible(true);
                    base = baseCircle;
                }
                break;
            case POLYGONAL_BASE:
            case POLYGONAL_CORNER:
                if (basePolygon != null) {
                    basePolygon.setOnMouseClicked(null);
                    basePolygon.setVisible(true);
                    base = basePolygon;
                }
                break;
        }
        arrange();

        centralItem.getNode().setVisible(true);
        for (FXMAbstractItem item : items) {
            item.getNode().setVisible(true);
        }

        int index = 0;
        double wt = duration.toSeconds() / (items.length + 1);
        double at = duration.toSeconds() / (items.length + 1) * 2.0;

        if (base != null) {
            base.setOnMouseClicked(null);
            base.setOpacity(0.0);
            new Timeline(new KeyFrame(Duration.seconds(wt), new KeyValue(base.opacityProperty(), 1.0, Interpolator.EASE_BOTH))).play();
        }

        index++;
        for (FXMAbstractItem item : items) {
            item.setMenuCenter(this, x, y);
            item.getNode().setOpacity(0);
            new Timeline(new KeyFrame(Duration.seconds(wt * index), new KeyValue(item.getNode().opacityProperty(), 0.0, Interpolator.DISCRETE)),
                    new KeyFrame(Duration.seconds(wt * index + at), new KeyValue(item.getNode().opacityProperty(), 1.0, Interpolator.EASE_IN)))
                    .play();
            index++;
        }
        if (centralItem != null) {
            centralItem.setMenuCenter(this, x, y);
            centralItem.getNode().setOpacity(0);
            new Timeline(new KeyFrame(Duration.seconds(wt * index), new KeyValue(centralItem.getNode().opacityProperty(), 0.0, Interpolator.DISCRETE)),
                    new KeyFrame(Duration.seconds(wt * index + at), new KeyValue(centralItem.getNode().opacityProperty(), 1.0, Interpolator.EASE_IN)))
                    .play();
        }
    }

}

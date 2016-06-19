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
public class FXMMenu {

    public enum MenuStyle {

        EMPTY,
        CIRCULAR,
        POLYGONAL_CORNER,
        POLYGONAL_BASE
    }

    protected final Group container;
    private final Group parentContainer;
    private Circle baseCircle = null;
    private Polygon basePolygon = null;
    double size;
    private Color color;
    protected FXMAbstractItem items[];
    private FXMMenuItem centralItem = null;
    private final MenuStyle baseStyle;
    private double centerX = 0;
    private double centerY = 0;

    private Timeline longPressTL;
    private Arc arc;
    private Timeline disappearTL;

    public FXMMenu(Group parentContainer, double size, MenuStyle style, Color color) {
        this.size = size;
        this.color = color;
        this.baseStyle = style;
        this.container = new Group();
//        this.container.setVisible(false);
        this.parentContainer = parentContainer;
        parentContainer.getChildren().add(this.container);
        create();
        arrange();
    }

    private void create() {
        if (baseStyle == MenuStyle.CIRCULAR) {
            baseCircle = new Circle(size, color);
            baseCircle.opacityProperty().set(1.0);
            baseCircle.setMouseTransparent(false);
            container.getChildren().add(baseCircle);
        }

        arc = new Arc();
        arc.setMouseTransparent(false);
        arc.setType(ArcType.OPEN);
        arc.setStrokeLineCap(StrokeLineCap.ROUND);
        arc.setStroke(Color.BEIGE);
        arc.setFill(new Color(0, 0, 0, 0));
        arc.setStartAngle(90);
        parentContainer.getChildren().add(arc);

        longPressTL = null;
        disappearTL = null;
    }

    public void destroy(){
        parentContainer.getChildren().remove(container);    
    }
    
    public final void addCentralItem(FXMMenuItem item) {
        centralItem = item;
        centralItem.init(size, -1, -1);
        container.getChildren().add(centralItem.getNode());
    }

    public Group getParentContainer() {
        return parentContainer;
    }

    public final void add(FXMSubMenu submenu) {
        addItem(submenu);
        submenu.getSubmenuNode().setVisible(false);
    }

    public final void add(FXMMenuItem item) {
        addItem(item);
    }

    private void addItem(FXMAbstractItem item) {
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
        container.getChildren().add(item.getNode());
    }

    private double[] getPolygonVertices(int number, double delta) {
        double pts[] = new double[number * 2];
        for (int i = 0; i < number; i++) {
            double factor = 1;
            double angle = 0;
            if (baseStyle == MenuStyle.POLYGONAL_BASE) {
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
        int n = items.length;
        double d = 2 * Math.PI / n;
        if (baseStyle == MenuStyle.POLYGONAL_CORNER || baseStyle == MenuStyle.POLYGONAL_BASE) {
            basePolygon = new Polygon(getPolygonVertices(n, d));
            basePolygon.opacityProperty().set(1.0);
            basePolygon.setMouseTransparent(false);
            container.getChildren().add(basePolygon);
        }
        int idx = 0;
        for (FXMAbstractItem item : items) {
            item.init(size, n, idx);
            idx++;
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

    public void delayedShow(Duration delay, double x, double y, Duration duration) {
        arc.setLayoutX(x);
        arc.setLayoutY(y);
        arc.setRadiusX(size / 2);
        arc.setRadiusY(size / 2);
        arc.setStrokeWidth(size / 10);
        arc.setBlendMode(BlendMode.SCREEN);

        disappearTL = new Timeline(new KeyFrame(duration.multiply(1.2), new KeyValue(arc.opacityProperty(), 0.0, Interpolator.EASE_BOTH)));
        disappearTL.setOnFinished(e -> parentContainer.getChildren().remove(arc));

        arc.opacityProperty().setValue(0.0);
        longPressTL = new Timeline(
                new KeyFrame(delay.divide(2), new KeyValue(arc.lengthProperty(), arc.lengthProperty().getValue(), Interpolator.DISCRETE)),
                new KeyFrame(delay, new KeyValue(arc.lengthProperty(), -360, Interpolator.LINEAR)),
                new KeyFrame(delay, new KeyValue(arc.opacityProperty(), 1.0, Interpolator.EASE_IN))
        );
        longPressTL.setOnFinished(e -> {
            disappearTL.play();
            open(x, y, duration);
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
        container.setVisible(false);
    }

    void show() {
        container.setVisible(true);
    }

    public void open(double x, double y, Duration duration) {
        centerX = x;
        centerY = y;

        Shape base = null;
        switch (baseStyle) {
            case CIRCULAR:
                if (baseCircle != null) {
                    baseCircle.setCenterX(x);
                    baseCircle.setCenterY(y);
                    base = baseCircle;
                }
                break;
            case POLYGONAL_BASE:
            case POLYGONAL_CORNER:
                if (basePolygon != null) {
                    basePolygon.setOnMouseClicked(null);
                    base = basePolygon;
                }
                break;
        }
        arrange();

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
            item.setMenuCenter(container, x, y);
            item.getNode().setOpacity(0);
            new Timeline(new KeyFrame(Duration.seconds(wt * index), new KeyValue(item.getNode().opacityProperty(), 0.0, Interpolator.DISCRETE)),
                    new KeyFrame(Duration.seconds(wt * index + at), new KeyValue(item.getNode().opacityProperty(), 1.0, Interpolator.EASE_IN)))
                    .play();
            index++;
        }
        if (centralItem != null) {
            centralItem.setMenuCenter(container, x, y);
            centralItem.getNode().setOpacity(0);
            new Timeline(new KeyFrame(Duration.seconds(wt * index), new KeyValue(centralItem.getNode().opacityProperty(), 0.0, Interpolator.DISCRETE)),
                    new KeyFrame(Duration.seconds(wt * index + at), new KeyValue(centralItem.getNode().opacityProperty(), 1.0, Interpolator.EASE_IN)))
                    .play();
        }
    }

}

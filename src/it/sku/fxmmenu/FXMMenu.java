/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.sku.fxmmenu;

import javafx.scene.Node;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Polygon;
import javafx.util.Duration;

/**
 *
 * @author skuttik
 */
public class FXMMenu extends Pane {

    public enum Style {

        CIRCULAR,
        POLYGONAL,
        POLYGONAL_ARROW,
        TRANSPARENT
    }

    private Circle baseCircle = null;
    private Polygon basePolygon = null;
    double size;
    private Color color;
    private FXMMenuItem items[];
    private FXMMenuItem centralItem = null;
    private final Style baseStyle;
    private double centerX = 0;
    private double centerY = 0;

    public FXMMenu(double size, Style style, Color color) {
        this.size = size;
        this.color = color;
        this.baseStyle = style;
        create();
        arrange();
    }

    public FXMMenu(double size, Style style, Color color, FXMMenuItem... items) {
        this.size = size;
        this.color = color;
        this.baseStyle = style;
        create();
        addAll(items);
    }

    private void create() {
        if (baseStyle == Style.CIRCULAR) {
            baseCircle = new Circle(size, color);
            baseCircle.opacityProperty().set(1.0);
            getChildren().add(baseCircle);
        }
    }

    public final void addAll(FXMMenuItem... items) {
        this.items = items;
        arrange();
    }

    public final void addCentralItem(FXMMenuItem item) {
        this.centralItem = item;
        this.centralItem.init(size * .25, 0, 0);
        getChildren().add(this.centralItem);
    }

    public final void add(FXMMenuItem item) {
        if (this.items != null) {
            int s = this.items.length;
            FXMMenuItem newItems[] = new FXMMenuItem[s + 1];
            System.arraycopy(this.items, 0, newItems, 0, s);
            newItems[s] = item;
            this.items = newItems;
        } else {
            this.items = new FXMMenuItem[1];
            this.items[0] = item;
        }
        arrange();
    }

    private double[] getPolygonVertices(int number, double delta) {
        double pts[] = new double[number * 2];
        for (int i = 0; i < number; i++) {
            double factor = 1;
            double angle = 0;
            if (baseStyle == Style.POLYGONAL_ARROW) {
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
        getChildren().removeAll(items);
        int n = this.items.length;
        double d = 2 * Math.PI / n;
        if (baseStyle == Style.POLYGONAL || baseStyle == Style.POLYGONAL_ARROW) {
            getChildren().remove(basePolygon);
            basePolygon = new Polygon(getPolygonVertices(n, d));
            basePolygon.opacityProperty().set(1.0);
            getChildren().add(basePolygon);
        }
        int idx = 0;
        for (FXMMenuItem item : items) {
            item.init(size * .25, size * 0.62 * Math.cos(d * idx - Math.PI / 2), size * 0.62 * Math.sin(d * idx - Math.PI / 2));
            getChildren().add(item);
            idx++;
        }
        if (centralItem != null) {
            getChildren().remove(centralItem);
            getChildren().addAll(centralItem);
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

    public void show(double x, double y, Duration duration) {
        centerX = x;
        centerY = y;

        if (baseCircle != null) {
            baseCircle.setCenterX(x);
            baseCircle.setCenterY(y);
            baseCircle.setOpacity(1.0);
            baseCircle.setOnMouseClicked(null);
        }
        if (basePolygon != null) {
            arrange();
            basePolygon.setOpacity(1.0);
            basePolygon.setOnMouseClicked(null);
        }
        getChildren().filtered((Node t) -> (t.getClass() == FXMMenuItem.class))
                .forEach((Node n) -> {
                    FXMMenuItem item = (FXMMenuItem) n;
                    item.setOpacity(1);
                    item.setMenuCenter(x, y);
                });

    }

}

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
import javafx.util.Duration;

/**
 *
 * @author skuttik
 */
public class FXMMenu extends Pane {

    private Circle circle;
    double size;
    private Color color;
    private FXMMenuItem items[];
    private FXMMenuItem centralItem = null;

    public FXMMenu(double size, Color color) {
        this.size = size;
        this.color = color;

        create();
        arrange();
    }

    public FXMMenu(double size, Color color, FXMMenuItem... items) {
        this.size = size;
        this.color = color;
        create();
        addAll(items);
    }

    private final void create() {
        circle = new Circle(size, color);
        circle.opacityProperty().set(1.0);
        getChildren().add(circle);
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
        int s = this.items.length;
        FXMMenuItem newItems[] = new FXMMenuItem[s + 1];
        System.arraycopy(this.items, 0, newItems, 0, s);
        newItems[s] = item;
        this.items = newItems;
        arrange();
    }

    private void arrange() {
        getChildren().removeAll(items);
        int n = this.items.length;
        double d = 2 * Math.PI / n;
        int idx = 0;
        for (FXMMenuItem item : items) {
            item.init(size * .25, size * 0.62 * Math.cos(d * idx - Math.PI / 2), size * 0.62 * Math.sin(d * idx - Math.PI / 2));
            getChildren().add(item);
            idx++;
        }
    }

    public void show(double x, double y, Duration duration) {
        circle.setCenterX(x);
        circle.setCenterY(y);
        circle.setOpacity(1.0);
        getChildren().filtered((Node t) -> (t.getClass() == FXMMenuItem.class))
                .forEach((Node n) -> {
                    FXMMenuItem item = (FXMMenuItem) n;
                    item.setOpacity(1);
                    item.setMenuCenter(x, y);
                });
        
        circle.setOnMouseClicked(null);
    }

}

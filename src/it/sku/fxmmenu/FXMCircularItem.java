/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.sku.fxmmenu;

import it.sku.fxmmenu.internal.FXMBaseMenuItem;
import javafx.scene.Group;
import javafx.scene.image.Image;
import javafx.scene.input.MouseButton;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

/**
 *
 * @author skuttik
 */
public class FXMCircularItem extends FXMBaseMenuItem {

    private final Circle circle;

    public FXMCircularItem(Color bgColor, Color labelColor, String labelText, Image itemImage, String tooltipText) {
        super(labelColor, labelText, itemImage, tooltipText);
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
        getItemGroup().getChildren().add(circle);
        initialize();
    }

    @Override
    public void setMenuCenter(Group container, double x, double y) {
        super.setMenuCenter(x, y);
        circle.setCenterX(getOffsetX() + x);
        circle.setCenterY(getOffsetY() + y);
    }

    @Override
    public void arrange(double size, int totalNumber, int index) {
        baseArrange(size, totalNumber, index);
        circle.setRadius(getRefSize());
    }
}

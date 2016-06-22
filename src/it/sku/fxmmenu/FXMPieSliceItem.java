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
import javafx.scene.shape.Arc;
import javafx.scene.shape.ArcType;
import javafx.scene.shape.StrokeLineCap;

/**
 *
 * @author skuttik
 */
public class FXMPieSliceItem extends FXMBaseMenuItem {

    private Arc pieSlice = null;

    public FXMPieSliceItem(Color bgColor, Color labelColor, String labelText, Image itemImage, String tooltipText) {
        super(labelColor, labelText, itemImage, tooltipText);
        pieSlice = new Arc();
        pieSlice.setMouseTransparent(false);
        pieSlice.setType(ArcType.OPEN);
        pieSlice.setStrokeLineCap(StrokeLineCap.BUTT);
        pieSlice.setStroke(bgColor);
        //pieSlice.setFill(new Color(0.0, 0.0, 0.0, 1.0));
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
        getItemGroup().getChildren().add(pieSlice);
        initialize();
    }

    @Override
    public void setMenuCenter(Group container, double x, double y) {
        super.setMenuCenter(x, y);
        pieSlice.setCenterX(x);
        pieSlice.setCenterY(y);
    }

    @Override
    public void arrange(double size, int totalNumber, int index) {
        if (index == -1) {
            System.err.println(this.getClass() + ": ERROR - central item cannot be PIE");
        } else {
            double sliceLen = 360 / totalNumber - totalNumber * 1;
            pieSlice.setLength(sliceLen);
            pieSlice.setStartAngle(90 - sliceLen / 2 - 360 / totalNumber * index);
            pieSlice.setStrokeWidth(size * 0.66);
            pieSlice.setRadiusX(size * 0.66);
            pieSlice.setRadiusY(size * 0.66);
        }
        baseArrange(size, totalNumber, index);
    }

}

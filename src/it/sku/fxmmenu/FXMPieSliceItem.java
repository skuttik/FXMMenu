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

    public FXMPieSliceItem(Color bgColor, Color labelColor, String labelText, Image itemImage) {
        super(labelColor, labelText, itemImage);
        pieSlice = new Arc();
        pieSlice.setMouseTransparent(false);
        pieSlice.setType(ArcType.OPEN);
        pieSlice.setFill(Color.TRANSPARENT);
        pieSlice.setStrokeLineCap(StrokeLineCap.BUTT);
        pieSlice.setStroke(bgColor);
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
        pieSlice.setCenterX(-offsetX);
        pieSlice.setCenterY(-offsetY);
    }

    @Override
    public void arrange(double size, int totalNumber, int index) {
        if (index == -1) {
            System.err.println(this.getClass() + ": ERROR - central item cannot be PIE");
        } else {
            double sliceLen = (360 - 2 * totalNumber) / totalNumber;
            pieSlice.setLength(sliceLen);
            pieSlice.setStartAngle(90 - sliceLen / 2 - 360 / totalNumber * index);
            pieSlice.setStrokeWidth(size * 0.66);
            pieSlice.setRadiusX(size * (menuLevel + 1) * 0.66);
            pieSlice.setRadiusY(size * (menuLevel + 1) * 0.66);
        }
        baseArrange(size, totalNumber, index, SubLevelMode.ENLARGE_FROM_CENTER);
    }

    @Override
    public void setMenuLevel(int menuLevel) {
        this.menuLevel = menuLevel;
        tooltipFactor = 1 + menuLevel * 0.66;
    }
}

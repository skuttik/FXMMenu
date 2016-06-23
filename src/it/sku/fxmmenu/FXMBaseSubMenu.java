/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.sku.fxmmenu;

import it.sku.fxmmenu.internal.FXMAbstractItem;
import it.sku.fxmmenu.internal.FXMBaseMenuItem;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.paint.Color;

/**
 *
 * @author skuttik
 */
public class FXMBaseSubMenu extends FXMMenu implements FXMAbstractItem {

    private FXMBaseMenuItem backItem;
    private FXMBaseMenuItem submenuItem;
    private double x = 0;
    private double y = 0;
    private double offsetX;
    private double offsetY;
    private boolean activeValue = true;
    private final FXMMenu parentMenu;

    public FXMBaseSubMenu(FXMMenu parentMenu, FillingStyle submenuStyle, Color submenuColor) {
        super(parentMenu.getParentContainer(), parentMenu.getSize(), submenuStyle, submenuColor);
        this.parentMenu = parentMenu;
    }

    protected void setItems(FXMBaseMenuItem submenuItem, FXMBaseMenuItem backItem) {
        this.backItem = backItem;
        this.backItem.setOnPressed(e -> {
            parentMenu.show();
            close(true);
        });
        addCentralItem(this.backItem);

        this.submenuItem = submenuItem;
        this.submenuItem.setOnPressed(e -> {
            parentMenu.hide();
            open(x, y);
        });
    }

    @Override
    public void arrange(double size, int totalNumber, int index) {
        double d = 2 * Math.PI / totalNumber;
        offsetX = size * 0.62 * Math.cos(d * index - Math.PI / 2);
        offsetY = size * 0.62 * Math.sin(d * index - Math.PI / 2);
        submenuItem.arrange(size, totalNumber, index);
    }

    @Override
    public void setMenuCenter(Group container, double x, double y) {
        submenuItem.setMenuCenter(container, x, y);
        this.x = offsetX + x;
        this.y = offsetY + y;
    }

    @Override
    public Node getNode() {
        return submenuItem.getNode();
    }

    public Node getSubmenuNode() {
        return container;
    }

    @Override
    public void setActive(boolean value) {
        activeValue = value;
    }

    public FXMBaseSubMenu setTooltipText(String text) {
        submenuItem.setTooltipText(text);
        return this;
    }

    public FXMBaseSubMenu setBackTooltipText(String text) {
        backItem.setTooltipText(text);
        return this;
    }
}

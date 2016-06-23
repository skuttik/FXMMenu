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
public abstract class FXMBaseSubMenu extends FXMMenu implements FXMAbstractItem {

    protected FXMBaseMenuItem backItem;
    protected FXMBaseMenuItem submenuItem;
    protected double x = 0;
    protected double y = 0;
    protected double offsetX;
    protected double offsetY;
    protected boolean activeValue = true;
    private final FXMMenu parentMenu;

    public FXMBaseSubMenu(FXMMenu parentMenu, FillingStyle submenuStyle, Color submenuColor) {
        super(parentMenu.getParentContainer(), parentMenu.getSize(), submenuStyle, submenuColor);
        this.parentMenu = parentMenu;
        super.menuLevel = parentMenu.menuLevel + 1;
    }

    protected void setBaseItems(FXMBaseMenuItem submenuItem, FXMBaseMenuItem backItem) {
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

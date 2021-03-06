/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.sku.fxmmenu;

import it.sku.fxmmenu.internal.FXMBaseMenuItem;
import javafx.scene.Group;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;

/**
 *
 * @author skuttik
 */
public final class FXMPieSliceSubMenu extends FXMBaseSubMenu {

    public FXMPieSliceSubMenu(Color bgColor, Color labelColor, String labelText, Image itemImage, String tooltipText, FXMMenu parentMenu, FillingStyle submenuStyle, Color submenuColor) {
        super(parentMenu, submenuStyle, submenuColor);
        FXMBaseMenuItem submenuItem = new FXMPieSliceItem(bgColor, labelColor, labelText, itemImage);
        submenuItem.setOnHold(null);
        submenuItem.setOnHoldReleased(null);

        FXMBaseMenuItem backItem = new FXMCircularItem(bgColor, labelColor, labelText, itemImage);
        backItem.setOnHold(null);
        backItem.setOnHoldReleased(null);

        setBaseItems(submenuItem, backItem);
    }

    @Override
    public void setMenuCenter(Group container, double x, double y) {
        submenuItem.setMenuCenter(container, x, y);
        backItem.setMenuCenter(container, x, y);
        this.x = x;
        this.y = y;
    }
}

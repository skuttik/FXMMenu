/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.sku.fxmmenu;

import it.sku.fxmmenu.internal.FXMBaseMenuItem;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;

/**
 *
 * @author skuttik
 */
public class FXMCircularSubMenu extends FXMBaseSubMenu {

    public FXMCircularSubMenu(Color bgColor, Color labelColor, String labelText, Image itemImage, String tooltipText, FXMMenu parentMenu, FillingStyle submenuStyle, Color submenuColor) {
        super(parentMenu, submenuStyle, submenuColor);
        FXMBaseMenuItem submenuItem = new FXMCircularItem(bgColor, labelColor, labelText, itemImage, tooltipText);
        submenuItem.setOnHold(null);
        submenuItem.setOnHoldReleased(null);

        FXMBaseMenuItem backItem = new FXMCircularItem(bgColor, labelColor, labelText, itemImage, tooltipText);
        backItem.setOnHold(null);
        backItem.setOnHoldReleased(null);
        
        setItems(submenuItem, backItem);
    }

}

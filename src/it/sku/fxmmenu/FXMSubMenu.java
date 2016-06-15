/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.sku.fxmmenu;

import it.sku.fxmmenu.FXMMenuItem.ItemStyle;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.paint.Color;
import javax.swing.ImageIcon;

/**
 *
 * @author skuttik
 */
public class FXMSubMenu extends FXMMenu implements FXMAbstractItem {

    private final FXMMenuItem backItem;
    private final FXMMenuItem submenuItem;
    private FXMMenu parent = null;

    public FXMSubMenu(ItemStyle itemStyle, Color bgColor, Color labelColor, String labelText, ImageIcon icon, String tooltipText,
            double submenuSize, Style submenuStyle, Color submenuColor) {
        super(submenuSize, submenuStyle, submenuColor);
        submenuItem = new FXMMenuItem(itemStyle, bgColor, labelColor, labelText, icon, tooltipText);
        submenuItem.setOnHold(null);
        submenuItem.setOnHoldReleased(null);

        backItem = new FXMMenuItem(itemStyle, bgColor, labelColor, labelText, icon, tooltipText);
        backItem.setOnHold(null);
        backItem.setOnHoldReleased(null);
        
        backItem.setOnPressed(e -> {
            super.setVisible(false);
            parent.setVisible(true);
        });
        
        submenuItem.setOnPressed(e -> {
            parent.setVisible(false);
            backItem.getNode().setVisible(true);
            super.setVisible(true);
        });
        
        super.addCentralItem(backItem);
    }

    @Override
    public void init(double size, int totalNumber, int index) {
        submenuItem.init(size, totalNumber, index);
    }

    @Override
    public void setMenuCenter(Group container, double x, double y) {
            
        submenuItem.setMenuCenter(container, x, y);
        backItem.getNode().setLayoutX(x);
        backItem.getNode().setLayoutY(y);
    }

    void setParentMenu(FXMMenu parent) {
        this.parent = parent;
    }

    @Override
    public Node getNode() {
        return submenuItem.getNode();
    }

    Node getSubMenuNode() {
        return backItem.getNode();
    }
}

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
import javafx.util.Duration;
import javax.swing.ImageIcon;

/**
 *
 * @author skuttik
 */
public class FXMSubMenu extends FXMMenu implements FXMAbstractItem {

    private final FXMMenuItem backItem;
    private final FXMMenuItem submenuItem;
    private double x = 0;
    private double y = 0;
    private double offsetX;
    private double offsetY;
    private boolean activeValue = true;

    public FXMSubMenu(ItemStyle itemStyle, Color bgColor, Color labelColor, String labelText, ImageIcon icon, String tooltipText,
            FXMMenu parentMenu, double submenuSize, MenuStyle submenuStyle, Color submenuColor) {
        super(parentMenu.getParentContainer(), submenuSize, submenuStyle, submenuColor);
        submenuItem = new FXMMenuItem(itemStyle, bgColor, labelColor, labelText, icon, tooltipText);
        submenuItem.setOnHold(null);
        submenuItem.setOnHoldReleased(null);

        backItem = new FXMMenuItem(itemStyle, bgColor, labelColor, labelText, icon, tooltipText);
        backItem.setOnHold(null);
        backItem.setOnHoldReleased(null);

        backItem.setOnPressed(e -> {
            System.out.println("BACK!");
            parentMenu.show(Duration.seconds(0.3));
            close(Duration.seconds(0.3));
        });

        submenuItem.setOnPressed(e -> {
            System.out.println("SUBMENU in " + x + ", " + y);
            parentMenu.hide(Duration.seconds(0.3));
            open(x, y, Duration.seconds(0.3));
        });
        addCentralItem(backItem);
    }

    @Override
    public void init(double size, int totalNumber, int index) {
        double d = 2 * Math.PI / totalNumber;
        offsetX = size * 0.62 * Math.cos(d * index - Math.PI / 2);
        offsetY = size * 0.62 * Math.sin(d * index - Math.PI / 2);
        submenuItem.init(size, totalNumber, index);
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
}

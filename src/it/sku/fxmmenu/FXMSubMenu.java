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
            parentMenu.show();
            hide(Duration.seconds(0.1));
            submenuItem.getNode().setVisible(true);
            backItem.getNode().setVisible(false);
        });

        submenuItem.setOnPressed(e -> {
            System.out.println("SUBMENU in " + x + ", " + y);
            parentMenu.hide(Duration.seconds(0.1));
            open(x, y, Duration.seconds(0.3));
            show();
            backItem.getNode().setVisible(true);
            submenuItem.getNode().setVisible(true);
        });
        addCentralItem(backItem);
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
        this.x = x;
        this.y = y;
    }

    @Override
    public Node getNode() {
        return submenuItem.getNode();
    }

    public Node getSubmenuNode() {
        return container;
    }
}

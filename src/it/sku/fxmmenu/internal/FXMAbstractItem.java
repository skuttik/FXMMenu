/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.sku.fxmmenu.internal;

import it.sku.fxmmenu.FXMMenu;
import javafx.scene.Group;
import javafx.scene.Node;

/**
 *
 * @author skuttik
 */
public interface FXMAbstractItem {

    void arrange(double size, int totalNumber, int index);

    void setMenuCenter(Group container, double x, double y);

    Node getNode();

    void setActive(boolean value);

    void close(boolean animated);
}

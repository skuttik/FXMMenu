/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.sku.fxmmenu;

import javafx.scene.Group;
import javafx.scene.Node;

/**
 *
 * @author skuttik
 */
public abstract interface FXMAbstractItem {
//    boolean activeValue = true;

    abstract void init(double size, int totalNumber, int index);

    abstract void setMenuCenter(Group container, double x, double y);

    abstract Node getNode();

    public default void setActive(boolean value) {
//        activeValue = value;
    }

}

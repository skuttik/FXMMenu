/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.sku.fxmmenu;

import it.sku.fxmmenu.FXMMenuItem.ItemStyle;
import javafx.application.Application;
import static javafx.application.Application.launch;
import javafx.application.Platform;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.input.MouseButton;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Duration;

//////////////////////////////////////////////////////////////////////////////////////////////////
//
// In this example a new menu is added to the scene when:
//     right mouse button is pressed 
//     or for long press of the left button
//
// The menu has circular shapes with a random number of menu items (doing nothing) 
// and a central item for exiting test application
//
//////////////////////////////////////////////////////////////////////////////////////////////////
/**
 *
 * @author skuttik
 */
public class Test extends Application {

    FXMMenu showingMenu = null;

    @Override
    public void start(Stage primaryStage) {
        Group root = new Group();
        Scene scene = new Scene(root, 800, 800, new Color(0.1, 0.1, 0.1, 0.3));

        primaryStage.setTitle("FXMenu");
        primaryStage.setScene(scene);
        primaryStage.initStyle(StageStyle.TRANSPARENT);

        Duration longPressDuration = Duration.seconds(1.0);
        Duration menuCreationDuration = Duration.seconds(0.4);

        scene.setOnMousePressed(e -> {
            if (e.getButton() == MouseButton.SECONDARY) {
                FXMMenu menu = createMenu();
                menu.show(root, e.getSceneX(), e.getSceneY(), menuCreationDuration);
            } else if (e.getButton() == MouseButton.PRIMARY) {
                showingMenu = createMenu();
                showingMenu.delayedShow(root, longPressDuration, e.getSceneX(), e.getSceneY(), menuCreationDuration);
            }
        });

        scene.setOnMouseReleased(e -> {
            if (showingMenu != null) {
                showingMenu.interruptDelayedShow();
            }
        });
        primaryStage.show();
    }

    private FXMMenu createMenu() {
        FXMSubMenu submenu = new FXMSubMenu(ItemStyle.CIRCULAR, Color.BLUE, Color.WHITE, "SB", null, "Submenu", 100, FXMMenu.Style.CIRCULAR, Color.BLUE);
        for (int i = 0; i < 5; i++) {
            String lbl = "S" + i;
            submenu.add(new FXMMenuItem(ItemStyle.CIRCULAR, Color.BLUE, Color.WHITE, lbl, null, lbl).
                    setOnHold(e -> System.out.println("HOLD " + lbl)).
                    setOnPressed(e -> System.out.println("PRESSED " + lbl)).
                    setOnHoldReleased(e -> System.out.println("RELEASED " + lbl)));
        }

        FXMMenu menu = new FXMMenu(100, FXMMenu.Style.EMPTY, new Color(0.2, 0.2, 0.2, 0.76));

        menu.addCentralItem(new FXMMenuItem(ItemStyle.CIRCULAR, Color.DIMGRAY, Color.BLACK, "X", null, "Close").
                setOnPressed(e -> menu.hide(Duration.ZERO)));
        menu.add(new FXMMenuItem(ItemStyle.CIRCULAR, Color.RED, Color.WHITE, "Q", null, "Quit").
                setOnPressed(e -> Platform.exit()));
        menu.add(submenu);

        for (int i = 0; i < (Math.random() * 4 + 2); i++) {
            String lbl = "" + i;
            menu.add(new FXMMenuItem(ItemStyle.CIRCULAR, Color.CORAL, Color.BLACK, lbl, null, lbl).
                    setOnHold(e -> System.out.println("HOLD " + lbl)).
                    setOnPressed(e -> System.out.println("PRESSED " + lbl)).
                    setOnHoldReleased(e -> System.out.println("RELEASED " + lbl)));
        }
        return menu;
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }

}

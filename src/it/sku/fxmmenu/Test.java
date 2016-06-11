/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.sku.fxmmenu;

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
                root.getChildren().add(menu);
                menu.show(e.getSceneX(), e.getSceneY(), menuCreationDuration);
            } else if (e.getButton() == MouseButton.PRIMARY) {
                showingMenu = createMenu();
                root.getChildren().add(showingMenu);
                showingMenu.delayedShow(longPressDuration, e.getSceneX(), e.getSceneY(), menuCreationDuration);
            }
        });

        scene.setOnMouseReleased(e -> {
            if (showingMenu != null) {
                showingMenu.interruptDelayedShow();
            }
        });
        primaryStage.show();

//        menu.show(250, 250, Duration.ZERO);
    }

    private FXMMenu createMenu() {
        /*
         FXMMenu menu = new FXMMenu(150, FXMMenu.Style.CIRCULAR, Color.DIMGRAY,
         new FXMMenuItem(Color.CORAL, Color.BLACK, "1").setOnHold(e -> System.out.println("A HOLD")).setOnPressed(e -> System.out.println("A PRESSED")).setOnHoldReleased(e -> System.out.println("A RELEASED")),
         new FXMMenuItem(Color.CORAL, Color.BLANCHEDALMOND, "2"),
         new FXMMenuItem(Color.CORAL, Color.BLACK, "3"));

         FXMMenuItem kmi = new FXMMenuItem(Color.CORAL, Color.BLACK, "4");
         menu.add(kmi);
         FXMMenuItem kmi1 = new FXMMenuItem(Color.CORAL, Color.BLACK, "5");
         menu.add(kmi1);
         FXMMenuItem kmi2 = new FXMMenuItem(Color.CORAL, Color.BLACK, "6");
         menu.add(kmi2);
         */

        FXMMenu menu = new FXMMenu(100, FXMMenu.Style.CIRCULAR, new Color(0.2, 0.2, 0.2, 0.76));
        
        menu.addCentralItem(
                new FXMMenuItem(Color.DIMGRAY, Color.BLACK, "X").
                setOnPressed(e -> Platform.exit()));
        
        for (int i = 0; i < (Math.random() * 4 + 2); i++) {
            String lbl = "" + i;
            menu.add(
                    new FXMMenuItem(Color.CORAL, Color.BLACK, lbl).
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

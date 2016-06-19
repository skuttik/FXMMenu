/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.sku.test;

import it.sku.fxmmenu.FXMMenu;
import it.sku.fxmmenu.FXMMenuItem;
import it.sku.fxmmenu.FXMMenuItem.ItemStyle;
import it.sku.fxmmenu.FXMSubMenu;
import javafx.application.Application;
import static javafx.application.Application.launch;
import javafx.application.Platform;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.image.Image;
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

        showingMenu = createMenuIcons(root);

        scene.setOnMousePressed(e -> {
            if (e.getButton() == MouseButton.SECONDARY) {
                showingMenu.open(e.getSceneX(), e.getSceneY());
            } else if (e.getButton() == MouseButton.PRIMARY) {
                showingMenu.delayedShow(e.getSceneX(), e.getSceneY());
            }
        });

        scene.setOnMouseReleased(e -> {
            if (showingMenu != null) {
                showingMenu.interruptDelayedShow();
            }
        });
        primaryStage.show();
    }

    private FXMMenu createMenu(Group parent) {

        FXMMenu menu = new FXMMenu(parent, 70, FXMMenu.MenuStyle.CIRCULAR, new Color(0.5, 0.5, 0.5, 0.65));

        ////// SUBMENU ///////////////////////////////
        FXMSubMenu submenu = new FXMSubMenu(ItemStyle.CIRCULAR, Color.CADETBLUE, Color.WHITE, "S", null, "Submenu", menu,
                FXMMenu.MenuStyle.CIRCULAR, Color.CADETBLUE.deriveColor(1.0, 1.0, 1.0, 0.4));

        submenu.add(new FXMMenuItem(ItemStyle.CIRCULAR, Color.CADETBLUE.darker(), Color.WHITE, "A", null, "function S - A"));
        submenu.add(new FXMMenuItem(ItemStyle.CIRCULAR, Color.CADETBLUE.darker(), Color.WHITE, "B", null, "function S - B"));
        submenu.add(new FXMMenuItem(ItemStyle.CIRCULAR, Color.CADETBLUE.darker(), Color.WHITE, "C", null, "function S - C"));
        submenu.add(new FXMMenuItem(ItemStyle.CIRCULAR, Color.CADETBLUE.darker(), Color.WHITE, "D", null, "function S - D"));
        submenu.add(new FXMMenuItem(ItemStyle.CIRCULAR, Color.CADETBLUE.darker(), Color.WHITE, "E", null, "function S - E"));
        ///////////////////////////////////////////////

        menu.addCentralItem(new FXMMenuItem(ItemStyle.CIRCULAR, Color.DIMGRAY, Color.BLACK, "X", null, "Close").
                setOnPressed(e -> menu.close(true)));

        menu.add(new FXMMenuItem(ItemStyle.CIRCULAR, Color.RED, Color.WHITE, "Q", null, "Quit").
                setOnPressed(e -> Platform.exit()));

        menu.add(submenu);

        for (int i = 0; i < 2; i++) {
            String lbl = "" + i;
            menu.add(new FXMMenuItem(ItemStyle.CIRCULAR, Color.CORAL, Color.BLACK, lbl, null, lbl).
                    setOnHold(e -> System.out.println("HOLD " + lbl)).
                    setOnPressed(e -> System.out.println("PRESSED " + lbl)).
                    setOnHoldReleased(e -> System.out.println("RELEASED " + lbl)));
        }
        return menu;
    }

    private FXMMenu createMenuIcons(Group parent) {

        ///////// Icon images  ///////////////////////
        Image btnCloseImage = new Image(Test.class.getResource("resources/closeT.png").toExternalForm());
        Image btnConfigImage = new Image(Test.class.getResource("resources/configurationT.png").toExternalForm());
        Image btnPencilImage = new Image(Test.class.getResource("resources/pencilT.png").toExternalForm());
        Image btnRubberImage = new Image(Test.class.getResource("resources/rubberT.png").toExternalForm());
        Image btnSelectionImage = new Image(Test.class.getResource("resources/selectionT.png").toExternalForm());
        Image btnSpotImage = new Image(Test.class.getResource("resources/spotlightT.png").toExternalForm());
        Image btnTrackingImage = new Image(Test.class.getResource("resources/spottrackingT.png").toExternalForm());
        Image btnStreetsImage = new Image(Test.class.getResource("resources/streetsT.png").toExternalForm());
        /////////////////////////////////////////////

        FXMMenu menu = new FXMMenu(parent, 65, FXMMenu.MenuStyle.CIRCULAR, new Color(0.5, 0.5, 0.5, 0.65));

        ////// SUBMENU ///////////////////////////////
        FXMSubMenu submenu = new FXMSubMenu(ItemStyle.CIRCULAR, Color.CADETBLUE, Color.WHITE, "", btnConfigImage, "Config", menu,
                FXMMenu.MenuStyle.CIRCULAR, Color.CADETBLUE.deriveColor(1.0, 1.0, 1.0, 0.4));

        submenu.add(new FXMMenuItem(ItemStyle.CIRCULAR, Color.CADETBLUE.darker(), Color.WHITE, "", btnStreetsImage, "Streets on"));
        submenu.add(new FXMMenuItem(ItemStyle.CIRCULAR, Color.CADETBLUE.darker(), Color.WHITE, "", btnPencilImage, "Set parameters"));
        submenu.add(new FXMMenuItem(ItemStyle.CIRCULAR, Color.CADETBLUE.darker(), Color.WHITE, "", btnRubberImage, "Reset parameters"));
        ///////////////////////////////////////////////

        menu.addCentralItem(new FXMMenuItem(ItemStyle.CIRCULAR, Color.DIMGRAY, Color.BLACK, "", btnCloseImage, "Close").
                setOnPressed(e -> menu.close(true)));

        menu.add(new FXMMenuItem(ItemStyle.CIRCULAR, Color.RED, Color.WHITE, "", btnCloseImage, "Quit").
                setOnPressed(e -> Platform.exit()));

        menu.add(submenu);

        menu.add(new FXMMenuItem(ItemStyle.CIRCULAR, Color.CORAL, Color.BLACK, null, btnSelectionImage, "Item selection").
                setOnHold(e -> System.out.println("HOLD Item selection")).
                setOnPressed(e -> System.out.println("PRESSED Item selection")).
                setOnHoldReleased(e -> System.out.println("RELEASED Item selection")));
        
        menu.add(new FXMMenuItem(ItemStyle.CIRCULAR, Color.CORAL, Color.BLACK, null, btnSpotImage, "Spotlight").
                setOnHold(e -> System.out.println("HOLD Spotlight")).
                setOnPressed(e -> System.out.println("PRESSED Spotlight")).
                setOnHoldReleased(e -> System.out.println("RELEASED Spotlight")));
        
        menu.add(new FXMMenuItem(ItemStyle.CIRCULAR, Color.CORAL, Color.BLACK, null, btnTrackingImage, "Tracking").
                setOnHold(e -> System.out.println("HOLD Tracking")).
                setOnPressed(e -> System.out.println("PRESSED Tracking")).
                setOnHoldReleased(e -> System.out.println("RELEASED Tracking")));
        
        return menu;
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }

}

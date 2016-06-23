/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.sku.test;

import it.sku.fxmmenu.FXMMenu;
import it.sku.fxmmenu.FXMBaseSubMenu;
import it.sku.fxmmenu.FXMCircularItem;
import it.sku.fxmmenu.FXMCircularSubMenu;
import it.sku.fxmmenu.FXMMenu.FillingStyle;
import it.sku.fxmmenu.FXMPieSliceItem;
import it.sku.fxmmenu.FXMPieSliceSubMenu;
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

//////////////////////////////////////////////////////////////////////////////////////////////////
//
// In this example a new menu is added to the scene when:
//     right mouse button is pressed 
//     or for long press of the left button
//
// createMenuCircular: The menu has circular shapes with a random number of menu items (doing nothing) 
//                     and a central item for exiting test application
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

        showingMenu = createMenuPie(root);
//        showingMenu = createMenuCircular(root);

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

    private FXMMenu createMenuCircular(Group parent) {

        FXMMenu menu = new FXMMenu(parent, 70, FXMMenu.FillingStyle.EMPTY, new Color(0.5, 0.5, 0.5, 0.65));

        ////// SUBMENU ///////////////////////////////
        FXMBaseSubMenu submenu = new FXMCircularSubMenu(Color.CADETBLUE, Color.WHITE, "S", null, menu,
                FXMMenu.FillingStyle.CIRCULAR, Color.CADETBLUE.deriveColor(1.0, 1.0, 1.0, 0.4))
                .setTooltipText("Submenu")
                .setBackTooltipText("Back");

        submenu.add(new FXMCircularItem(Color.CADETBLUE.darker(), Color.WHITE, "A", null)
                .setTooltipText("function S - A")
                .setOnPressed(e -> System.out.println("S-A")));
        submenu.add(new FXMCircularItem(Color.CADETBLUE.darker(), Color.WHITE, "B", null)
                .setTooltipText("function S - B")
                .setOnPressed(e -> System.out.println("S-B")));
        submenu.add(new FXMCircularItem(Color.CADETBLUE.darker(), Color.WHITE, "C", null)
                .setTooltipText("function S - C")
                .setOnPressed(e -> System.out.println("S-C")));
        submenu.add(new FXMCircularItem(Color.CADETBLUE.darker(), Color.WHITE, "D", null)
                .setTooltipText("function S - D")
                .setOnPressed(e -> System.out.println("S-D")));
        submenu.add(new FXMCircularItem(Color.CADETBLUE.darker(), Color.WHITE, "E", null)
                .setTooltipText("function S - E")
                .setOnPressed(e -> System.out.println("S-E")));
        ///////////////////////////////////////////////

        menu.addCentralItem(new FXMCircularItem(Color.DIMGRAY, Color.BLACK, "X", null)
                .setTooltipText("Close")
                .setOnPressed(e -> menu.close(true)));

        menu.add(new FXMCircularItem(Color.RED, Color.WHITE, "Q", null)
                .setTooltipText("Quit")
                .setOnPressed(e -> Platform.exit()));

        menu.add(submenu);
        for (int i = 0; i < 4; i++) {
            String lbl = "" + i;
            menu.add(new FXMCircularItem(Color.CORAL, Color.BLACK, lbl, null)
                    .setTooltipText(lbl)
                    .setOnHold(e -> System.out.println("HOLD " + lbl))
                    .setOnPressed(e -> System.out.println("PRESSED " + lbl))
                    .setOnHoldReleased(e -> System.out.println("RELEASED " + lbl)));
        }
        return menu;
    }

    private FXMMenu createMenuPie(Group parent) {

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

        Color gcol = new Color(0.5, 0.5, 0.5, 0.65);

        FXMMenu menu = new FXMMenu(parent, 50, FXMMenu.FillingStyle.CIRCULAR, gcol);

        ////// SUBMENU ///////////////////////////////
        FXMBaseSubMenu submenu = new FXMPieSliceSubMenu(Color.DIMGRAY, Color.WHITE, "", btnConfigImage, "Config", menu,
                FXMMenu.FillingStyle.CIRCULAR, Color.CADETBLUE.deriveColor(1.0, 1.0, 1.0, 0.4));

        submenu.add(new FXMPieSliceItem(gcol.darker(), Color.WHITE, "", btnStreetsImage)
                .setTooltipText("Streets on"));
        submenu.add(new FXMPieSliceItem(gcol.darker(), Color.WHITE, "", btnPencilImage)
                .setTooltipText("Set parameters"));
        submenu.add(new FXMPieSliceItem(gcol.darker(), Color.WHITE, "", btnRubberImage)
                .setTooltipText("Reset parameters"));
        ///////////////////////////////////////////////

        menu.addCentralItem(new FXMCircularItem(Color.DIMGRAY, Color.BLACK, "", btnCloseImage)
                .setTooltipText("Close")
                .setOnPressed(e -> menu.close(true)));

        menu.add(new FXMPieSliceItem(Color.RED, Color.WHITE, "", btnCloseImage)
                .setTooltipText("Quit")
                .setOnPressed(e -> Platform.exit()));

        menu.add(submenu);

        menu.add(new FXMPieSliceItem(Color.GRAY, Color.BLACK, null, btnSelectionImage)
                .setTooltipText("Item selection")
                .setOnHold(e -> System.out.println("HOLD Item selection"))
                .setOnPressed(e -> System.out.println("PRESSED Item selection"))
                .setOnHoldReleased(e -> System.out.println("RELEASED Item selection")));

        menu.add(new FXMPieSliceItem(Color.GRAY, Color.BLACK, null, btnSpotImage)
                .setTooltipText("Spotlight")
                .setOnHold(e -> System.out.println("HOLD Spotlight"))
                .setOnPressed(e -> System.out.println("PRESSED Spotlight"))
                .setOnHoldReleased(e -> System.out.println("RELEASED Spotlight")));

        menu.add(new FXMPieSliceItem(Color.GRAY, Color.BLACK, null, btnTrackingImage)
                .setTooltipText("Tracking")
                .setOnHold(e -> System.out.println("HOLD Tracking"))
                .setOnPressed(e -> System.out.println("PRESSED Tracking"))
                .setOnHoldReleased(e -> System.out.println("RELEASED Tracking")));

        return menu;
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }

}

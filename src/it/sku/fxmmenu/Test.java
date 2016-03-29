/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.sku.fxmmenu;

import javafx.application.Application;
import static javafx.application.Application.launch;
import javafx.scene.Scene;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.util.Duration;

/**
 *
 * @author skuttik
 */
public class Test extends Application {

    @Override
    public void start(Stage primaryStage) {
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
        FXMMenu menu = new FXMMenu(100, FXMMenu.Style.CIRCULAR, Color.AQUA);
        menu.addCentralItem(new FXMMenuItem(Color.CHARTREUSE, Color.BLACK, "X"));
        for (int i = 0; i < Math.random() * 10; i++) {
            FXMMenuItem kmi1 = new FXMMenuItem(Color.CORAL, Color.BLACK, "" + i).
                    setOnHold(e -> System.out.println("HOLD")).
                    setOnPressed(e -> System.out.println("PRESSED")).
                    setOnHoldReleased(e -> System.out.println("RELEASED"));
            menu.add(kmi1);
        }
        StackPane root = new StackPane();
        root.getChildren().add(menu);

        Scene scene = new Scene(root, 600, 600);

        primaryStage.setTitle("Hello World!");
        primaryStage.setScene(scene);
        primaryStage.show();

        menu.show(250, 250, Duration.ZERO);
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }

}

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
        FXMMenu menu = new FXMMenu(150, Color.DIMGRAY, 
                new FXMMenuItem(Color.CORAL, Color.BLACK, "A").setOnHold(e -> System.out.println("A HOLD")).setOnPressed(e -> System.out.println("A PRESSED")).setOnHoldReleased(e -> System.out.println("A RELEASED")),
                new FXMMenuItem(Color.CORAL, Color.BLANCHEDALMOND, "F"),
                new FXMMenuItem( Color.CORAL, Color.BLACK, "S"));

        menu.addCentralItem(new FXMMenuItem(Color.CHARTREUSE, Color.BLACK, "X"));
        FXMMenuItem kmi = new FXMMenuItem(Color.CORAL, Color.BLACK, "N");
        menu.add(kmi);
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

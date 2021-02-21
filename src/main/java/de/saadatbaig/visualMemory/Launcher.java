/*
 * Project: visualMemory
 * Author:  Saadat M. Baig
 * Date:    21.02.2021
 * License: BSD-3-Clause-Attribution
 */
package de.saadatbaig.visualMemory;

import de.saadatbaig.visualMemory.Controllers.HomeViewController;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;


public class Launcher extends Application {

    @Override
    public void start(Stage stage) throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("FXMLs/Home.fxml"));
        Pane root = loader.load();
        root.getStylesheets().add(String.valueOf(getClass().getClassLoader().getResource("visualMemory.css")));
        ((HomeViewController)loader.getController()).setHostServices(getHostServices());
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.setTitle("visualMemory");
        stage.setResizable(false);
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }


    /* End */
}
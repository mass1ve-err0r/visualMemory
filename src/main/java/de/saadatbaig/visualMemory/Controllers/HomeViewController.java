/*
 * Project: visualMemory
 * Author:  Saadat M. Baig
 * Date:    21.02.21
 * License: BSD-3-Clause-Attribution
 */
package de.saadatbaig.visualMemory.Controllers;

import javafx.application.HostServices;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Tooltip;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;


public class HomeViewController {

    @FXML public Button buttonCreateMemory;
    @FXML public Button buttonSettings;
    @FXML public Label labelBottomText;
    HostServices _services;

    public HomeViewController() {
        Platform.runLater(() ->labelBottomText.setTooltip(new Tooltip("Click me to view the source code!")));
    }

    public void setHostServices(HostServices hostServices) {
        this._services = hostServices;
    }

    public void openTheSauce(MouseEvent mouseEvent) {
        Platform.runLater(() -> _services.showDocument("https://github.com/mass1ve-err0r/visualMemory"));
        mouseEvent.consume();
    }

    public void openSettings(MouseEvent mouseEvent) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("FXMLs/Settings.fxml"));
            Pane settingsPane = loader.load();
            settingsPane.getStylesheets().add(String.valueOf(getClass().getClassLoader().getResource("visualMemory.css")));
            Stage settingsStage = new Stage();
            settingsStage.initModality(Modality.APPLICATION_MODAL);
            settingsStage.initStyle(StageStyle.DECORATED);
            settingsStage.setTitle("visualMemory - Settings");
            settingsStage.setScene(new Scene(settingsPane));
            settingsStage.setResizable(false);
            settingsStage.show();
        } catch (IOException e) {
            System.out.println("[visualMemory][E]: Failed to create SettingsPane!");
            e.printStackTrace();
        }
        mouseEvent.consume();
    }


    /* End */
}
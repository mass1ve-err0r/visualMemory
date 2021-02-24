/*
 * Project: visualMemory
 * Author:  Saadat M. Baig
 * Date:    21.02.21
 * License: BSD-3-Clause-Attribution
 */
package de.saadatbaig.visualMemory.Controllers;

import de.saadatbaig.visualMemory.Views.BlockView;
import javafx.application.HostServices;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextInputDialog;
import javafx.scene.control.Tooltip;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;


public class HomeViewController {

    @FXML public Button buttonCreateMemory;
    @FXML public Button buttonSettings;
    @FXML public Label labelBottomText;
    HostServices _services;
    int sessions = 1;

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

    public void openSettings(ActionEvent evt) {
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
        evt.consume();
    }

    public void createBlockView(ActionEvent evt) {
        AtomicInteger sz = new AtomicInteger(10);
        TextInputDialog dialog = new TextInputDialog("10");
        dialog.setTitle("visualMemory");
        dialog.setHeaderText("Set virtual memory size.\nDefaults to 10, maximum allowed/ recommended is 35.");
        dialog.setContentText("Virtual size in bytes:");
        Optional<String> result = dialog.showAndWait();
        result.ifPresent(c_sz -> {
            if (Integer.parseInt(c_sz) > 10 && Integer.parseInt(c_sz) < 36) { sz.set(Integer.parseInt(c_sz)); }
            BlockView view = new BlockView(String.format("Playground #%d", sessions), sz.get());
            Stage settingsStage = new Stage();
            settingsStage.initModality(Modality.WINDOW_MODAL);
            settingsStage.initStyle(StageStyle.DECORATED);
            settingsStage.setTitle(String.format("Playground #%d", sessions));
            sessions++;
            settingsStage.setScene(new Scene(view.asParent()));
            settingsStage.setResizable(false);
            settingsStage.show();
        });

        evt.consume();
    }


    /* End */
}
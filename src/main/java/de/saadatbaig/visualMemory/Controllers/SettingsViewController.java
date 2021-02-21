/*
 * Project: visualMemory
 * Author:  Saadat M. Baig
 * Date:    21.02.21
 * License: BSD-3-Clause-Attribution
 */
package de.saadatbaig.visualMemory.Controllers;

import de.saadatbaig.visualMemory.Models.Preferences;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;


public class SettingsViewController {

    @FXML public AnchorPane apRoot;
    @FXML public Label labelCharSZ;
    @FXML public TextField textfieldChar;
    @FXML public Label labelShortSZ;
    @FXML public TextField textfieldShort;
    @FXML public Label labelIntSZ;
    @FXML public TextField textfieldInt;
    @FXML public Label labelLongSZ;
    @FXML public TextField textfieldLong;
    @FXML public Button buttonRestore;
    @FXML public Button buttonSave;

    public SettingsViewController() {
        initFields();
    }

    public void saveSettings(ActionEvent evt) {
        int newChar = Integer.parseInt(textfieldChar.getText());
        int newShort = Integer.parseInt(textfieldShort.getText());
        int newInt = Integer.parseInt(textfieldInt.getText());
        int newLong = Integer.parseInt(textfieldLong.getText());

        setValuesAndSync(newChar, newShort, newInt, newLong, "Saved Successfully!");
        evt.consume();
    }

    public void restoreSettings(ActionEvent evt) {
        setValuesAndSync(8, 16, 32, 64, "Restored Successfully!");
        initFields();
        evt.consume();
    }

    private void initFields() {
        Platform.runLater(() ->textfieldChar.setText(Preferences.sharedInstance().getValueForKey("sz_char")));
        Platform.runLater(() ->textfieldShort.setText(Preferences.sharedInstance().getValueForKey("sz_short")));
        Platform.runLater(() ->textfieldInt.setText(Preferences.sharedInstance().getValueForKey("sz_int")));
        Platform.runLater(() ->textfieldLong.setText(Preferences.sharedInstance().getValueForKey("sz_long")));
    }

    private void setValuesAndSync(int newChar, int newShort, int newInt, int newLong, String message) {
        Preferences.sharedInstance().setValueForKey("sz_char", newChar);
        Preferences.sharedInstance().setValueForKey("sz_short", newShort);
        Preferences.sharedInstance().setValueForKey("sz_int", newInt);
        Preferences.sharedInstance().setValueForKey("sz_long", newLong);
        if (Preferences.sharedInstance().synchronize()) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("visualMemory - Settings");
            alert.setHeaderText(null);
            alert.setContentText(message);
            alert.showAndWait();
        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("visualMemory - Settings");
            alert.setHeaderText(null);
            alert.setContentText("Something went wrong, check the logs!");
            alert.showAndWait();
        }
    }


    /* End */
}
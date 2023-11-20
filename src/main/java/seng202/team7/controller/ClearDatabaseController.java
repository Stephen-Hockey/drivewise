package seng202.team7.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.stage.Stage;

/**
 * FXML controller class for the CSV importer.
 * It imports data into the database from a file.
 *
 * @author Luke Edwards
 */
public class ClearDatabaseController {
    @FXML
    private Button closeButton;
    private MainController mainController;

    /**
     * The init function for the CSV importer.
     * Sets the main controller and shows the popup.
     *
     * @param mainController The main controller.
     */
    public void init(MainController mainController) {
        this.mainController = mainController;
    }

    /**
     * Function the close the popup.
     */
    @FXML
    private void closeScene() {
        Stage stage = (Stage) closeButton.getScene().getWindow();
        stage.close();
    }

    /**
     * The function to check and handel a file being uploaded.
     */
    @FXML
    private void confirmButtonClicked(){
        mainController.clearDatabase();
        closeScene();
    }
}
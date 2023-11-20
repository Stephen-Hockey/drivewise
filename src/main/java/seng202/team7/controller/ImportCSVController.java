package seng202.team7.controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.DragEvent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.BorderPane;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;

/**
 * FXML controller class for the CSV importer.
 * It imports data into the database from a file.
 *
 * @author Luke Edwards
 */
public class ImportCSVController {

    private static final Logger log = LogManager.getLogger(ImportCSVController.class);

    private Stage stage;
    @FXML
    private Button closeButton;
    @FXML
    private VBox fileInstructionsContainer;
    @FXML
    private HBox fileInfoContainer;
    @FXML
    private Text fileNameLabel;
    private File selectedFile;
    private MainController mainController;

    /**
     * The init function for the CSV importer.
     * Sets the main controller and shows the popup.
     *
     * @param mainController The main controller.
     */
    public void init(MainController mainController) {
        this.mainController = mainController;
        showFileInstructions();
        enableDragAndDrop();
    }

    /**
     * Function to allow users to drag and drop files into the uploader.
     */
    private void enableDragAndDrop(){

        // Enable drag-and-drop functionality
        fileInstructionsContainer.setOnDragOver(event -> {
            Dragboard dragboard = event.getDragboard();
            if (dragboard.hasFiles()) {
                event.acceptTransferModes(TransferMode.COPY);
            }
            event.consume();
        });

        fileInstructionsContainer.setOnDragDropped((DragEvent event) -> {
            Dragboard dragboard = event.getDragboard();
            boolean success = false;
            if (dragboard.hasFiles() && dragboard.getFiles().size() == 1) {
                success = true;
                // Add your file processing logic here
                selectedFile = dragboard.getFiles().get(0);
                displayFileInfo(selectedFile.getName());
            }
            event.setDropCompleted(success);
            event.consume();
        });
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
     * Allows users to browse through their files to upload.
     */
    @FXML
    private void browseFiles() {

        // Initialise the file chooser
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Open Resource File");
        fileChooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("CSV Files", "*.csv"));

        // Try and get the url of the chosen file
        try {
            fileChooser.setInitialDirectory(new File(ImportCSVController.class.getProtectionDomain().getCodeSource()
                    .getLocation().toURI()).getParentFile());
        } catch (URISyntaxException e) {
            log.error(e.getMessage());
        }

        // Sets the chosen fileusing a logg
        selectedFile = fileChooser.showOpenDialog(stage);

        if (selectedFile != null) {
            // Display the selected file information
            displayFileInfo(selectedFile.getName());
        }
    }

    /**
     * Shows the file instructions.
     */
    private void showFileInstructions() {
        fileInstructionsContainer.setVisible(true);
        fileInfoContainer.setVisible(false);
    }

    /**
     * Shows the file information given the filename in the file uploader.
     *
     * @param fileName The filename to display.
     */
    private void displayFileInfo(String fileName) {
        // Update the Text element with the selected file name
        fileNameLabel.setText("File Name: " + fileName);

        // Shows the text
        fileInstructionsContainer.setVisible(false);
        fileInfoContainer.setVisible(true);
    }

    /**
     * Button to remove the selected file.
     */
    @FXML
    private void removeFileClicked() {
        // Handle close button action
        selectedFile = null;
        showFileInstructions();
    }

    /**
     * The function to check and handle a file being uploaded.
     */
    @FXML
    private void confirmButtonClicked(){
        if(selectedFile != null){
            mainController.importData(selectedFile);
        }
        closeScene();
    }

    /**
     * The function to clear the database when clear database button is clicked.
     */
    @FXML
    private void clearDatabaseClicked(){
        try {
            FXMLLoader clearDatabasePopup = new FXMLLoader(getClass().getResource("/fxml/clear_database_popup.fxml"));
            BorderPane root = clearDatabasePopup.load();

            ClearDatabaseController clearDatabaseController= clearDatabasePopup.getController();
            clearDatabaseController.init(mainController);

            Stage modalStage = new Stage();
            modalStage.setScene(new Scene(root));
            modalStage.setTitle("Clear DataBase");
            modalStage.initModality(Modality.APPLICATION_MODAL);
            modalStage.initOwner(stage); // Set the owner to disable the main window
            modalStage.setResizable(false);
            modalStage.initStyle(StageStyle.UNDECORATED);

            // Show the modal dialog
            modalStage.showAndWait();

        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }
}
package seng202.team7.controller;

import javafx.animation.RotateTransition;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.*;
import javafx.util.Duration;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.controlsfx.control.Notifications;
import seng202.team7.business.AdviceLogic;
import seng202.team7.business.CrashManager;
import seng202.team7.exceptions.GeolocatorFailedException;
import seng202.team7.exceptions.InvalidUserInputException;
import seng202.team7.io.CrashCSVImporter;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * FXML controller class for the main window.
 * This window is central to our app, as it controls main.fxml, and also loads in the map, table, and graphs views.
 * It communicates with Table, Map, Graph controllers accordingly.
 *
 * @author Joseph Hendry
 * @author Stephen Hockey
 * @author Bella Hill
 * @author Kendra van Loon
 */
public class MainController {
    private static final Logger log = LogManager.getLogger(MainController.class);
    @FXML
    private Slider endYearSlider;
    @FXML
    private Slider startYearSlider;
    @FXML
    private RadioButton bikeRadioButton;
    @FXML
    private RadioButton carRadioButton;
    @FXML
    private RadioButton pedestrianRadioButton;
    @FXML
    private RadioButton fatalRadioButton;
    @FXML
    private RadioButton seriousRadioButton;
    @FXML
    private RadioButton minorRadioButton;
    @FXML
    private Label startYearLabel;
    @FXML
    private Label endYearLabel;


    // Crash manager and stage.
    private CrashManager crashManager;
    private Stage stage;

    // FXML variables
    @FXML
    private TextField startLocationTextField;
    @FXML
    private TextField endLocationTextField;
    @FXML
    private ToggleGroup transports;
    @FXML
    private ChoiceBox getMapViewMode;
    @FXML
    private TextField areaTextField;
    @FXML
    private Slider radiusSlider;
    @FXML
    private Label radiusLabel;
    @FXML
    private StackPane mainStackPane;
    @FXML
    private ProgressIndicator progressIndicator;
    @FXML
    private Button goButton;
    @FXML
    private Button swapButton;
    @FXML
    private Button assessButton;
    @FXML
    private Button mapViewButton;
    @FXML
    private Button graphViewButton;
    @FXML
    private Button tableViewButton;
    @FXML
    private Button importButton;
    @FXML
    private ToggleButton carButton;
    @FXML
    private ToggleButton cycleButton;
    @FXML
    private ToggleButton footButton;
    @FXML
    private Button filterButton;
    /**
     * A JavaFX VBox for displaying advice cards or related content.
     */
    @FXML
    public VBox adviceCardsVBox;

    /**
     * A JavaFX VBox used for filtering and selecting options.
     */
    @FXML
    public VBox filterVBox;

    /**
     * The parent VBox containing filter options and settings.
     */
    @FXML
    public VBox filterVBoxParent;

    // Defines the 3 different views
    private Parent mapView;
    private Parent tableView;
    private Parent graphView;

    // Defines the 3 different controllers and handling of advice cards
    private MapController mapController;
    private TableViewController tableViewController;
    private GraphController graphController;
    private final List<AdviceCardController> adviceCardControllerList = new ArrayList<>();
    @FXML
    private Button closeAllAdviceCardsBtn;
    private boolean isLoadingData = false;

    /**
     * Initialises the main window
     * @param stage stage to load
     */
    public void init(Stage stage) {
        // Set the stage and crash manager
        this.stage = stage;
        this.crashManager = new CrashManager(this);

        // Set title and default values

        closeAllAdviceCardsBtn.setVisible(false);

        // Initiate the area slider
        radiusSlider.valueProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                radiusLabel.setText(String.format("Radius: %.1f km", newValue));
            }
        });
        startYearSlider.valueProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                startYearLabel.setText(String.format("%.0f", newValue));
            }
        });
        endYearSlider.valueProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                endYearLabel.setText(String.format("%.0f", newValue));
            }
        });


        // Load the map and size page
        loadMap();
        loadTable();
        loadGraph();
        Tooltip goTooltip = new Tooltip("Assess route safety");
        Tooltip swapTooltip = new Tooltip("Swap address order");
        Tooltip assessTooltip = new Tooltip("Assess area safety");
        Tooltip mapTooltip = new Tooltip("Switch to map view");
        Tooltip graphTooltip = new Tooltip("Switch to graph view");
        Tooltip tableTooltip = new Tooltip("Switch to table view");
        Tooltip importTooltip = new Tooltip("Import CSV data");
        goButton.setTooltip(goTooltip);
        swapButton.setTooltip(swapTooltip);
        assessButton.setTooltip(assessTooltip);
        mapViewButton.setTooltip(mapTooltip);
        graphViewButton.setTooltip(graphTooltip);
        tableViewButton.setTooltip(tableTooltip);
        importButton.setTooltip(importTooltip);


        // Set the crash manager controllers
        crashManager.setControllers(mapController, tableViewController, graphController);

        // Set size and check for prompts
        //stage.sizeToScene();
        Platform.runLater(this::checkForEmptyDatabase);
    }

    /**
     * The update function for MainController
     * closes all advice cards then re-generates them and adds them to the view
     */
    public void update() {
        closeAllAdviceCards();
        updateAdviceCards();
    }

    /**
     * Re-generates advice based on the current crashes loaded in, then adds them to the view in the toolbar
     */
    public void updateAdviceCards() {
        // Generate advice for the data
        AdviceLogic adviceLogic = new AdviceLogic(crashManager.getCurrentCrashes());
        for (List<String> advicePair : adviceLogic.finalAdviceList) {
            String title = advicePair.get(0);
            String advice = advicePair.get(1);
            String imageURL = advicePair.get(2);
            addAdviceCardToToolbar(title, advice, imageURL);
        }
    }

    ////////// Map, Table, Graph Loaders //////////

    /**
     * Loads the map view.
     */
    private void loadMap() {
        try {
            if (mapView != null) {
                mainStackPane.getChildren().remove(mapView);
                mapView = null;
            }
            FXMLLoader mapViewLoader = new FXMLLoader(getClass().getResource("/fxml/map.fxml"));
            mapView = mapViewLoader.load();

            mapController = mapViewLoader.getController();
            mapController.init(stage, crashManager);


            mainStackPane.getChildren().add(mapView);
            mapView.toBack();
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }

    /**
     * Loads the data table view.
     */
    private void loadTable() {
        try {
            if (tableView != null) {
                mainStackPane.getChildren().remove(tableView);
                tableView = null;
            }
            FXMLLoader tableViewLoader = new FXMLLoader(getClass().getResource("/fxml/crashes_table.fxml"));
            tableView = tableViewLoader.load();

            tableViewController = tableViewLoader.getController();
            tableViewController.init(stage, crashManager, this);

            mainStackPane.getChildren().add(tableView);
            tableView.toBack();
        } catch (IOException e) {
            log.error(e.getMessage());

        }
    }

    /**
     * Loads the graph view.
     */
    private void loadGraph() {
        try {
            if (graphView != null) {
                mainStackPane.getChildren().remove(graphView);
                mapView = null;
            }
            FXMLLoader graphViewLoader = new FXMLLoader(getClass().getResource("/fxml/graph_view.fxml"));
            graphView = graphViewLoader.load();

            graphController = graphViewLoader.getController();
            graphController.init(stage, crashManager);

            mainStackPane.getChildren().add(graphView);
            graphView.toBack();
        } catch (IOException e) {
            log.error(e.getMessage());

        }
    }

    /**
     * Loads in an advice_card.fxml instance and adds it to adviceCardsVBox
     *
     * @param title the title of the advice card
     * @param advice the advice to be displayed on the card
     * @param imageURL the url of the image to be displayed on the new advice card
     */
    public void addAdviceCardToToolbar(String title, String advice, String imageURL) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/advice_card.fxml"));
            Parent adviceCard = loader.load();

            adviceCardsVBox.getChildren().add(adviceCard);

            AdviceCardController adviceCardController = loader.getController();
            adviceCardController.init(stage,title, advice, imageURL, this);
            adviceCardControllerList.add(adviceCardController);
            adviceCard.getStylesheets().add(Objects.requireNonNull(getClass().getResource("/stylesheets/adviceCard.css")).toExternalForm());

            closeAllAdviceCardsBtn.setVisible(true);

        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }

    /**
     * Checks for an empty database.
     * Prompts user to add data.
     */
    private void checkForEmptyDatabase() {
        if(crashManager.getDatabaseLength() < 1){
            showMessage("It appears your Crash Database is empty.\nPlease import Crash Data");
        }
    }

    ////////// Route & Area Searches //////////

    /**
     * Handles erroneous entries for a start location and an end location, and then calls a MapController method to
     * generate a route.
     * Triggered when the route search button is pressed
     */
    public void calculateRoute() {

        try {

            if (Objects.equals(endLocationTextField.getText(), "")) {
                throw new InvalidUserInputException("Please enter a location to go to");
            }
            if (Objects.equals(startLocationTextField.getText(), "")) {
                throw new InvalidUserInputException("Please enter a location to start from");
            }

            String transportVehicle = ((ToggleButton) transports.getSelectedToggle()).getId();

            mapController.setRoute(startLocationTextField.getText(), endLocationTextField.getText(), transportVehicle);
        } catch (InvalidUserInputException e) {
            showMessage(e.getMessage());
        } catch (GeolocatorFailedException e) {
            showMessage("Sorry, but \"" + e.getAddress() + "\" is not a valid address within Aotearoa");
        }
    }

    /**
     * Swaps dest location and start location. This will not automatically re-run the route search
     * @param startLocationTextField Address of start location
     * @param endLocationTextField Address of destination
     */
    public void swapLocations(TextField startLocationTextField, TextField endLocationTextField) {
        String location1 = startLocationTextField.getText();
        String location2 = endLocationTextField.getText();

        startLocationTextField.setText(location2);
        endLocationTextField.setText(location1);
    }

    /**
     * Handles erroneous entries for a location, and then calls a MapController method to generate an area search
     * Triggered when the 'Radius Search' button is pressed
     */
    public void doAreaAnalysis() {
        String mapViewMode = (String) (getMapViewMode.getValue());

        try {
            if (Objects.equals(areaTextField.getText(), "")) {
                throw new InvalidUserInputException("Please enter a location to search around");
            }

            mapController.setArea(areaTextField.getText(), radiusSlider.getValue(), mapViewMode);

        } catch (InvalidUserInputException e) {
            showMessage(e.getMessage());
        } catch (GeolocatorFailedException e) {
            showMessage("Sorry, but " + e.getAddress() + " is not a valid address within Aotearoa");
        }
    }

    ////////// Button Classes //////////

    /**
     * Switches to map view
     */
    @FXML
    private void showMap() {
        tableView.toBack();
        graphView.toBack();
    }

    /**
     * Switches to table view
     */
    @FXML
    private void showTable() {
        mapView.toBack();
        graphView.toBack();
    }

    /**
     * Switches to graph view
     */
    @FXML
    private void showGraph() {
        mapView.toBack();
        tableView.toBack();
    }

    /**
     * Triggered when the 'go' button is pressed, calls a routing method
     */
    @FXML
    private void goButtonPressed() {
        showMap();
        calculateRoute();
    }

    /**
     * Triggered when the "go" button is pressed, calls the swapping method
     */
    @FXML
    private void swapButtonPressed() {
        swapLocations(startLocationTextField, endLocationTextField);
    }

    /**
     * Toggles the filter section of the left toolbar between shown and hidden
     */
    @FXML
    private void toggleFilters() {
        if (filterVBox.getParent() == filterVBoxParent) {
            filterVBoxParent.getChildren().remove(filterVBox);
            filterButton.setText("Show Filters");
        } else {
            filterVBoxParent.getChildren().add(filterVBox);
            filterButton.setText("Hide Filters");
        }
    }

    /**
     * Triggered when the 'Radius Search' button is pressed, calls an AnalysisViewController method
     */
    @FXML
    private void assessAreaButtonClicked() {
        showMap();
        doAreaAnalysis();
    }

    /**
     * Closes all advice cards instead of them having to be closed individually.
     * Triggered when the "Close all advice cards" button is clicked.
     */
    @FXML
    private void closeAllAdviceCards() {
        ArrayList<AdviceCardController> adviceCardControllerListCopy = new ArrayList<>(adviceCardControllerList);
        for (AdviceCardController adviceCardController : adviceCardControllerListCopy) {
            adviceCardController.deleteCard();
        }
        adviceCardControllerList.clear();
    }

    /**
     * Called from an advice card right after deletion so that the adviceCardControllerList can be updated accurately
     * @param deletedAdviceCard the AdviceCardController of the deleted advice card.
     */
    public void adviceCardDeleted(AdviceCardController deletedAdviceCard) {
        adviceCardControllerList.remove(deletedAdviceCard);
        if (adviceCardControllerList.isEmpty()) {
            closeAllAdviceCardsBtn.setVisible(false);
        }
    }

    /**
     * Opens a file chooser window and allows a user to select a single .csv file to import
     */
    @FXML
    private void showImportCSVPopup(){
        if(isLoadingData){
            return;
        }
        try {
            FXMLLoader importCSVPopupLoader = new FXMLLoader(getClass().getResource("/fxml/import_popup.fxml"));
            BorderPane root = importCSVPopupLoader.load();

            ImportCSVController importCSVController = importCSVPopupLoader.getController();
            importCSVController.init(this);

            Stage modalStage = new Stage();
            modalStage.setScene(new Scene(root));
            modalStage.setTitle("Import CSV File");
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

    /**
     * Imports the data into the database using the crash manager.
     *
     * @param selectedFile The file to get the data from.
     */
    @FXML
    public void importData(File selectedFile) {
        RotateTransition rotateTransition = new RotateTransition(Duration.seconds(2), progressIndicator);
        rotateTransition.setByAngle(360);
        rotateTransition.setCycleCount(RotateTransition.INDEFINITE);
        Task<Void> task = new Task<Void>() {
            @Override
            protected Void call() throws Exception {
                Platform.runLater(() -> {

                    rotateTransition.play();
                    progressIndicator.setVisible(true);
                    isLoadingData = true;
                    Notifications.create()
                            .title("Importing CSV")
                            .owner(stage)
                            .text("DriveWise is currently importing \nthe crash data from " + selectedFile.getName() + "\nWe will let you know when its done")
                            .hideAfter(Duration.millis(20000))
                            .showInformation();
                });

                // Perform the actual import
                crashManager.addAllCrashesFromFile(new CrashCSVImporter(), selectedFile, () -> {
                    // This code runs on the JavaFX Application Thread
                    Platform.runLater(() -> {
                        Notifications.create()
                                .title("Finished Importing")
                                .owner(stage)
                                .text("DriveWise has loaded all the crashes from: " + selectedFile.getName())
                                .hideAfter(Duration.millis(5000))
                                .show();
                        crashManager.updateControllers();
                        rotateTransition.stop();
                        progressIndicator.setVisible(false);
                        isLoadingData = false;
                    });
                });
                return null;
            }
        };

        // Set up a callback for when the task is completed
        task.setOnSucceeded(e -> {
            // Additional actions after the import is completed, if needed
        });
        // Start the task in a new thread
        new Thread(task).start();
    }

    /**
     * Clears the database entirely
     */
    public void clearDatabase(){
        crashManager.clearDatabase();
    }

    /**
     * Shows the user a message in a notification
     *
     * @param message The message to show the user.
     */
    public void showMessage(String message) {
        Notifications.create()
                .title("Warning")
                .owner(stage)
                .text(message)
                .hideAfter(Duration.millis(10000))
                .showInformation();
    }

    /**
     * Applies the chosen filters, called when the "Apply Filters" button is clicked
     */
    @FXML
    public void applyFilters() {
        // Set transportation mode

        mapController.setMapViewMode();

        // Check which RadioButtons are selected
        boolean showCars = carRadioButton.isSelected();
        boolean showBikes = bikeRadioButton.isSelected();
        boolean showPedestrian = pedestrianRadioButton.isSelected();
        boolean showFatal = fatalRadioButton.isSelected();
        boolean showSerious = seriousRadioButton.isSelected();
        boolean showMinor = minorRadioButton.isSelected();

        // Get the value of the sliders
        int startYear = (int) startYearSlider.getValue();
        int endYear = (int) endYearSlider.getValue();

        // Run through crash manager
        crashManager.applyFilters(showCars, showBikes, showPedestrian, showFatal, showSerious, showMinor, startYear, endYear);
    }

    /**
     * Simply makes it so there has to be a selected toggle button
     */
    @FXML
    public void carTransportClicked() {
        if (!carButton.isSelected()) {
            carButton.setSelected(true);
        }
    }

    /**
     * Simply makes it so there has to be a selected toggle button
     */
    @FXML
    public void cycleTransportClicked() {
        if (!cycleButton.isSelected()) {
            cycleButton.setSelected(true);
        }
    }

    /**
     * Simply makes it so there has to be a selected toggle button
     */
    @FXML
    public void footTransportClicked() {
        if (!footButton.isSelected()) {
            footButton.setSelected(true);
        }
    }
}

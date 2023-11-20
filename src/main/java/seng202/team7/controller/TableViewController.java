package seng202.team7.controller;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import seng202.team7.business.CrashManager;
import seng202.team7.exceptions.InvalidUserInputException;
import seng202.team7.model.Crash;
import javafx.util.Callback;

import java.util.List;
import java.util.Objects;


/**
 * FXML controller class for route_analysis.fxml.
 * Displays content of database in a table format.
 *
 * @author Luke Edwards
 * @author Kendra van Loon
 */
public class TableViewController {
    private CrashManager crashManager;
    private MainController mainController;
    @FXML
    private TableView<Crash> crashesTableView;
    @FXML
    private Pagination pagination;
    @FXML
    private TextArea infoPanelTextArea;
    @FXML
    private TextField pageNumTextField;
    private final int rowsPerPage = 50;
    private int currentPage;
    private ObservableList<Crash> data;
    private Task<List<Crash>> currentLoadDataTask;
    private int highlightedRowIndex = -1;

    /**
     * Initialises the table view.
     *
     * @param stage current stage.
     * @param crashManager the crash manager to use.
     * @param mainController the MainController that oversees this controller.
     */
    public void init(Stage stage, CrashManager crashManager, MainController mainController) {
        // Set variables
        this.crashManager = crashManager;
        this.mainController = mainController;

        // Initiate the page variables
        int numCrashes = crashManager.getCurrentCrashes().size();
        int pageCount = (int) Math.ceil(numCrashes / (double) rowsPerPage);
        this.data = FXCollections.observableArrayList();
        this.currentPage = 0;

        // Initiate table and start loading pages
        initCrashesTable();
        update();
        pagination.setPageCount(pageCount);
        pagination.setPageFactory(createPageFactory());

        crashesTableView.getSelectionModel().selectedItemProperty().addListener(
                (observable, oldValue, newValue) -> {
                    // Handle the selection change
                    if (newValue != null) {
                        // Update the highlighted row index
                        highlightedRowIndex = crashesTableView.getSelectionModel().getSelectedIndex();

                        // Display the data of the highlighted row in the info panel
                        displayHighlightedRowData();
                    }
                });
    }

    /**
     * Highlights the specified row in the table view and displays its data in the info panel.
     *
     * @param rowIndex The index of the row to highlight.
     */
    private void highlightRow(int rowIndex) {
        if (rowIndex >= 0 && rowIndex < data.size()) {
            crashesTableView.getSelectionModel().clearAndSelect(rowIndex);
            crashesTableView.scrollTo(rowIndex);

            // Update the highlighted row index
            highlightedRowIndex = rowIndex;

            // Display the data of the highlighted row in the info panel
            displayHighlightedRowData();
        }
    }

    /**
     * Displays data of the highlighted row in the info panel.
     */
    private void displayHighlightedRowData() {
        // Retrieve the highlighted crash data
        Crash highlightedCrash = crashesTableView.getSelectionModel().getSelectedItem();

        // Assuming you have a TextArea named "infoPanelTextArea" in your FXML file
        if (highlightedCrash != null) {

            String crashData = "Crash ID: "+highlightedCrash.getId() + "\n"
                    + "Severity: " +highlightedCrash.getSeverity() + "\n"
                    + "Year: " + highlightedCrash.getYear() + "\n"
                    + "Spped limit: " + highlightedCrash.getSpeedLimit() +"\n";

            if (!Objects.equals(highlightedCrash.getHoliday(), "")) {
                crashData += "Holiday: " + highlightedCrash.getHoliday() +"\n";
            }
            crashData += "\n";

            crashData += "Location One: \n"+ highlightedCrash.getLocation1() +"\n\n"
                    + "Location Two: \n" +highlightedCrash.getLocation2() + "\n\n"
                    + "District: " +highlightedCrash.getTlaName() + "\n\n";

            Boolean vehicles = false;

            String vehiclesInvolved = "Vehicles involved in crash: \n";
            if (highlightedCrash.getCarStationWagon() != 0) {
                vehiclesInvolved += "Car: " + highlightedCrash.getCarStationWagon() + "\n";
                vehicles = true;
            }
            if (highlightedCrash.getBicycle() != 0) {
                vehiclesInvolved+=("Bike: " + highlightedCrash.getBicycle() + "\n");
                vehicles = true;
            }
            if (highlightedCrash.getMoped() != 0) {
                vehiclesInvolved+=("Moped: " + highlightedCrash.getMoped() + "\n");
                vehicles = true;
            }
            if (highlightedCrash.getMotorcycle() != 0) {
                vehiclesInvolved+=("Motorcycle: " + highlightedCrash.getMotorcycle() + "\n");
                vehicles = true;
            }
            if (highlightedCrash.getBus() != 0) {
                vehiclesInvolved+=("Bus: " + highlightedCrash.getBus() + "\n");
                vehicles = true;
            }
            if (highlightedCrash.getPedestrian() != 0) {
                vehiclesInvolved+=("Pedestrian: " + highlightedCrash.getPedestrian() + "\n");
                vehicles = true;
            }
            if (highlightedCrash.getSuv() != 0) {
                vehiclesInvolved+=("SUV: " + highlightedCrash.getSuv() + "\n");
                vehicles = true;
            }
            if (highlightedCrash.getTaxi() != 0) {
                vehiclesInvolved+=("Taxi: " + highlightedCrash.getTaxi() + "\n");
                vehicles = true;
            }
            if (highlightedCrash.getTruck() != 0) {
                vehiclesInvolved+=("Truck: " + highlightedCrash.getTruck() + "\n");
                vehicles = true;
            }
            if (highlightedCrash.getVanOrUtility() != 0) {
                vehiclesInvolved+=("Van or Utility: " + highlightedCrash.getVanOrUtility() + "\n");
                vehicles = true;
            }
            if (highlightedCrash.getTrain() != 0) {
                vehiclesInvolved+=("Train: " + highlightedCrash.getTrain() + "\n");
                vehicles = true;
            }
            if (highlightedCrash.getOtherVehicleType() != 0) {
                vehiclesInvolved+=("Other: " + highlightedCrash.getOtherVehicleType() + "\n");
                vehicles = true;
            }
            if (highlightedCrash.getParkedVehicle() != 0) {
                vehiclesInvolved+=("Parked vehicle: " + highlightedCrash.getParkedVehicle() + "\n");
                vehicles = true;
            }

            if (vehicles) {
                crashData += (vehiclesInvolved + "\n");
            }

            if (!Objects.equals(highlightedCrash.getTrafficControl(), "Nil") && !Objects.equals(highlightedCrash.getTrafficControl(), "Unknown")) {
                crashData += "Traffic control: " +highlightedCrash.getTrafficControl() +"\n\n";
            }

            if (!Objects.equals(highlightedCrash.getLight(), "Null")) {
                crashData += "Light: " +highlightedCrash.getLight() +"\n";
            }
            if (!Objects.equals(highlightedCrash.getWeatherA(), "Null")) {
                crashData += "WeatherA: " +highlightedCrash.getWeatherA() +"\n";
            }
            if (!Objects.equals(highlightedCrash.getWeatherB(), "Null")) {
                crashData += "WeatherB: " +highlightedCrash.getWeatherB() +"\n";
            }
            crashData += "\n";

            crashData += "LAT: " +highlightedCrash.getLat() +"\n";
            crashData += "LNG: " +highlightedCrash.getLng() +"\n";

            // Update the info panel text
            infoPanelTextArea.setText(crashData);
        }
    }

    /**
     * Called when the "Go To Page" button is clicked. Handles erroneous inputs and then goes to the appropriate page.
     */
    @FXML
    public void goToEnteredPageNum() {
        try {
            int pageNum = Integer.parseInt(pageNumTextField.getText());
            if (pageNum < 1 || pageNum > pagination.getPageCount()) {
                throw new InvalidUserInputException("Please enter a valid page number, between 1 and " + pagination.getPageCount());
            }

            currentPage = pageNum - 1;
            update();
        } catch (NumberFormatException e) {
            mainController.showMessage("Please enter a valid integer");
        } catch (InvalidUserInputException e) {
            mainController.showMessage(e.getMessage());
        }
    }

    /**
     * Updates the current table values to those in crash manager.
     */
    public void update() {
        pagination.setCurrentPageIndex(currentPage);
        loadNextPageAsync();
    }

    /**
     * Returns a Callback to be used by a pagination control for dynamically loading pages.
     *
     * @return The Callback that provides the content for each page.
     */
    private Callback<Integer, Node> createPageFactory() {

        return pageIndex -> {

            // Load the data only when navigating to a new page
            if (pageIndex != currentPage) {

                // Load the data for the current page
                loadNextPageAsync();

                // Update current page
                currentPage = pageIndex;

                highlightRow(0);
            }
            return crashesTableView;
        };
    }

    /**
     * Asynchronously loads data for the current page.
     * Cancels current tasks to prevent multiple simultaneous data requests.
     */
    private void loadNextPageAsync() {

        // Cancel the ongoing task if it exists
        if (currentLoadDataTask != null && currentLoadDataTask.isRunning()) {
            currentLoadDataTask.cancel();
        }

        // Create a new task for loading data
        currentLoadDataTask = new Task<List<Crash>>() {
            @Override
            protected List<Crash> call() throws Exception {
                try {
                    return crashManager.getPage(currentPage, rowsPerPage);
                } catch (Exception e) {
                    // Handle exceptions if needed
                    return null;
                }
            }
        };

        // Replaces current data with loaded data on success
        currentLoadDataTask.setOnSucceeded(event -> {
            List<Crash> nextPageData = currentLoadDataTask.getValue();

            // Synchronize the data update
            Platform.runLater(() -> {
                // Clear the data before adding new data
                data.clear();
                data.addAll(nextPageData);
                crashesTableView.setItems(data);
                // Update the page count based on the new data
                int numCrashes = crashManager.getCurrentCrashes().size();
                int pageCount = (int) Math.ceil(numCrashes / (double) rowsPerPage);
                pagination.setPageCount(pageCount);
                highlightRow(0);
            });
        });

        // Handle the failure, for example, by logging an error
        currentLoadDataTask.setOnFailed(event -> {
            Throwable exception = currentLoadDataTask.getException();
            exception.printStackTrace();
        });

        // Sets a thread to run the task
        Thread thread = new Thread(currentLoadDataTask);
        thread.setDaemon(true);
        thread.start();
    }

    /**
     * Initialises the actual table
     */
    private void initCrashesTable() {
        TableColumn<Crash, Integer> advisorySpeedCol = new TableColumn<>("Advisory Speed");
        advisorySpeedCol.setCellValueFactory(new PropertyValueFactory<>("advisorySpeed"));

        TableColumn<Crash, Integer> bicycleCol = new TableColumn<>("Bicycle");
        bicycleCol.setCellValueFactory(new PropertyValueFactory<>("bicycle"));

        TableColumn<Crash, Integer> bridgeCol = new TableColumn<>("Bridge");
        bridgeCol.setCellValueFactory(new PropertyValueFactory<>("bridge"));

        TableColumn<Crash, Integer> busCol = new TableColumn<>("Bus");
        busCol.setCellValueFactory(new PropertyValueFactory<>("bus"));

        TableColumn<Crash, Integer> carStationWagonCol = new TableColumn<>("Car Station Wagon");
        carStationWagonCol.setCellValueFactory(new PropertyValueFactory<>("carStationWagon"));

        TableColumn<Crash, Integer> cliffBankCol = new TableColumn<>("Cliff Bank");
        cliffBankCol.setCellValueFactory(new PropertyValueFactory<>("cliffBank"));

        TableColumn<Crash, String> location1Col = new TableColumn<>("Location 1");
        location1Col.setCellValueFactory(new PropertyValueFactory<>("location1"));

        TableColumn<Crash, String> location2Col = new TableColumn<>("Location 2");
        location2Col.setCellValueFactory(new PropertyValueFactory<>("location2"));

        TableColumn<Crash, String> severityCol = new TableColumn<>("Severity");
        severityCol.setCellValueFactory(new PropertyValueFactory<>("severity"));

        TableColumn<Crash, Integer> yearCol = new TableColumn<>("Year");
        yearCol.setCellValueFactory(new PropertyValueFactory<>("year"));

        TableColumn<Crash, Integer> ditchCol = new TableColumn<>("Ditch");
        ditchCol.setCellValueFactory(new PropertyValueFactory<>("ditch"));

        TableColumn<Crash, Integer> fatalCountCol = new TableColumn<>("Fatal Count");
        fatalCountCol.setCellValueFactory(new PropertyValueFactory<>("fatalCount"));

        TableColumn<Crash, Integer> fenceCol = new TableColumn<>("Fence");
        fenceCol.setCellValueFactory(new PropertyValueFactory<>("fence"));

        TableColumn<Crash, String> flatHillCol = new TableColumn<>("Flat Hill");
        flatHillCol.setCellValueFactory(new PropertyValueFactory<>("flatHill"));

        TableColumn<Crash, Integer> guardRailCol = new TableColumn<>("Guard Rail");
        guardRailCol.setCellValueFactory(new PropertyValueFactory<>("guardRail"));

        TableColumn<Crash, String> holidayCol = new TableColumn<>("Holiday");
        holidayCol.setCellValueFactory(new PropertyValueFactory<>("holiday"));

        TableColumn<Crash, Integer> houseOrBuildingCol = new TableColumn<>("House or Building");
        houseOrBuildingCol.setCellValueFactory(new PropertyValueFactory<>("houseOrBuilding"));

        TableColumn<Crash, String> intersectionCol = new TableColumn<>("Intersection");
        intersectionCol.setCellValueFactory(new PropertyValueFactory<>("intersection"));

        TableColumn<Crash, Integer> kerbCol = new TableColumn<>("Kerb");
        kerbCol.setCellValueFactory(new PropertyValueFactory<>("kerb"));

        TableColumn<Crash, String> lightCol = new TableColumn<>("Light");
        lightCol.setCellValueFactory(new PropertyValueFactory<>("light"));

        TableColumn<Crash, Integer> minorInjuryCountCol = new TableColumn<>("Minor Injury Count");
        minorInjuryCountCol.setCellValueFactory(new PropertyValueFactory<>("minorInjuryCount"));

        TableColumn<Crash, Integer> mopedCol = new TableColumn<>("Moped");
        mopedCol.setCellValueFactory(new PropertyValueFactory<>("moped"));

        TableColumn<Crash, Integer> motorcycleCol = new TableColumn<>("Motorcycle");
        motorcycleCol.setCellValueFactory(new PropertyValueFactory<>("motorcycle"));

        TableColumn<Crash, Integer> numberOfLanesCol = new TableColumn<>("Number of Lanes");
        numberOfLanesCol.setCellValueFactory(new PropertyValueFactory<>("numberOfLanes"));

        TableColumn<Crash, Integer> objectThrownOrDroppedCol = new TableColumn<>("Object Thrown or Dropped");
        objectThrownOrDroppedCol.setCellValueFactory(new PropertyValueFactory<>("objectThrownOrDropped"));

        TableColumn<Crash, Integer> otherObjectCol = new TableColumn<>("Other Object");
        otherObjectCol.setCellValueFactory(new PropertyValueFactory<>("otherObject"));

        TableColumn<Crash, Integer> otherVehicleTypeCol = new TableColumn<>("Other Vehicle Type");
        otherVehicleTypeCol.setCellValueFactory(new PropertyValueFactory<>("otherVehicleType"));

        TableColumn<Crash, Integer> overBankCol = new TableColumn<>("Over Bank");
        overBankCol.setCellValueFactory(new PropertyValueFactory<>("overBank"));

        TableColumn<Crash, Integer> parkedVehicleCol = new TableColumn<>("Parked Vehicle");
        parkedVehicleCol.setCellValueFactory(new PropertyValueFactory<>("parkedVehicle"));

        TableColumn<Crash, Integer> phoneBoxEtcCol = new TableColumn<>("Phone Box, Etc.");
        phoneBoxEtcCol.setCellValueFactory(new PropertyValueFactory<>("phoneBoxEtc"));

        TableColumn<Crash, Integer> pedestrian = new TableColumn<>("Pedestrian");
        pedestrian.setCellValueFactory(new PropertyValueFactory<>("pedestrian"));

        TableColumn<Crash, Integer> postOrPoleCol = new TableColumn<>("Post or Pole");
        postOrPoleCol.setCellValueFactory(new PropertyValueFactory<>("postOrPole"));

        TableColumn<Crash, String> roadCharacterCol = new TableColumn<>("Road Character");
        roadCharacterCol.setCellValueFactory(new PropertyValueFactory<>("roadCharacter"));

        TableColumn<Crash, String> roadLaneCol = new TableColumn<>("Road Lane");
        roadLaneCol.setCellValueFactory(new PropertyValueFactory<>("roadLane"));

        TableColumn<Crash, String> roadSurfaceCol = new TableColumn<>("Road Surface");
        roadSurfaceCol.setCellValueFactory(new PropertyValueFactory<>("roadSurface"));

        TableColumn<Crash, Integer> roadworksCol = new TableColumn<>("Roadworks");
        roadworksCol.setCellValueFactory(new PropertyValueFactory<>("roadworks"));

        TableColumn<Crash, Integer> schoolBusCol = new TableColumn<>("School Bus");
        schoolBusCol.setCellValueFactory(new PropertyValueFactory<>("schoolBus"));

        TableColumn<Crash, Integer> seriousInjuryCountCol = new TableColumn<>("Serious Injury Count");
        seriousInjuryCountCol.setCellValueFactory(new PropertyValueFactory<>("seriousInjuryCount"));

        TableColumn<Crash, Integer> slipOrFloodCol = new TableColumn<>("Slip or Flood");
        slipOrFloodCol.setCellValueFactory(new PropertyValueFactory<>("slipOrFlood"));

        TableColumn<Crash, Integer> speedLimitCol = new TableColumn<>("Speed Limit");
        speedLimitCol.setCellValueFactory(new PropertyValueFactory<>("speedLimit"));

        TableColumn<Crash, Integer> strayAnimalCol = new TableColumn<>("Stray Animal");
        strayAnimalCol.setCellValueFactory(new PropertyValueFactory<>("strayAnimal"));

        TableColumn<Crash, Integer> streetLightCol = new TableColumn<>("Street Light");
        streetLightCol.setCellValueFactory(new PropertyValueFactory<>("streetLight"));

        TableColumn<Crash, Integer> suvCol = new TableColumn<>("SUV");
        suvCol.setCellValueFactory(new PropertyValueFactory<>("suv"));

        TableColumn<Crash, Integer> taxiCol = new TableColumn<>("Taxi");
        taxiCol.setCellValueFactory(new PropertyValueFactory<>("taxi"));

        TableColumn<Crash, String> tlaNameCol = new TableColumn<>("TLA Name");
        tlaNameCol.setCellValueFactory(new PropertyValueFactory<>("tlaName"));

        TableColumn<Crash, Integer> temporarySpeedLimitCol = new TableColumn<>("Temporary Speed Limit");
        temporarySpeedLimitCol.setCellValueFactory(new PropertyValueFactory<>("temporarySpeedLimit"));

        TableColumn<Crash, String> trafficControlCol = new TableColumn<>("Traffic Control");
        trafficControlCol.setCellValueFactory(new PropertyValueFactory<>("trafficControl"));

        TableColumn<Crash, Integer> trafficIslandCol = new TableColumn<>("Traffic Island");
        trafficIslandCol.setCellValueFactory(new PropertyValueFactory<>("trafficIsland"));

        TableColumn<Crash, Integer> trafficSignCol = new TableColumn<>("Traffic Sign");
        trafficSignCol.setCellValueFactory(new PropertyValueFactory<>("trafficSign"));

        TableColumn<Crash, Integer> trainCol = new TableColumn<>("Train");
        trainCol.setCellValueFactory(new PropertyValueFactory<>("train"));

        TableColumn<Crash, Integer> treeCol = new TableColumn<>("Tree");
        treeCol.setCellValueFactory(new PropertyValueFactory<>("tree"));

        TableColumn<Crash, Integer> truckCol = new TableColumn<>("Truck");
        truckCol.setCellValueFactory(new PropertyValueFactory<>("truck"));

        TableColumn<Crash, Integer> unknownVehicleTypeCol = new TableColumn<>("Unknown Vehicle Type");
        unknownVehicleTypeCol.setCellValueFactory(new PropertyValueFactory<>("unknownVehicleType"));

        TableColumn<Crash, String> urbanCol = new TableColumn<>("Urban");
        urbanCol.setCellValueFactory(new PropertyValueFactory<>("urban"));

        TableColumn<Crash, Integer> vanOrUtilityCol = new TableColumn<>("Van or Utility");
        vanOrUtilityCol.setCellValueFactory(new PropertyValueFactory<>("vanOrUtility"));

        TableColumn<Crash, Integer> vehicleCol = new TableColumn<>("Vehicle");
        vehicleCol.setCellValueFactory(new PropertyValueFactory<>("vehicle"));

        TableColumn<Crash, Integer> waterRiverCol = new TableColumn<>("Water River");
        waterRiverCol.setCellValueFactory(new PropertyValueFactory<>("waterRiver"));

        TableColumn<Crash, String> weatherACol = new TableColumn<>("Weather A");
        weatherACol.setCellValueFactory(new PropertyValueFactory<>("weatherA"));

        TableColumn<Crash, String> weatherBCol = new TableColumn<>("Weather B");
        weatherBCol.setCellValueFactory(new PropertyValueFactory<>("weatherB"));

        TableColumn<Crash, Float> latCol = new TableColumn<>("Latitude");
        latCol.setCellValueFactory(new PropertyValueFactory<>("lat"));

        TableColumn<Crash, Float> lngCol = new TableColumn<>("Longitude");
        lngCol.setCellValueFactory(new PropertyValueFactory<>("lng"));

        crashesTableView.getColumns().add(advisorySpeedCol);
        crashesTableView.getColumns().add(bicycleCol);
        crashesTableView.getColumns().add(bridgeCol);
        crashesTableView.getColumns().add(busCol);
        crashesTableView.getColumns().add(carStationWagonCol);
        crashesTableView.getColumns().add(cliffBankCol);
        crashesTableView.getColumns().add(location1Col);
        crashesTableView.getColumns().add(location2Col);
        crashesTableView.getColumns().add(severityCol);
        crashesTableView.getColumns().add(yearCol);
        crashesTableView.getColumns().add(ditchCol);
        crashesTableView.getColumns().add(fatalCountCol);
        crashesTableView.getColumns().add(fenceCol);
        crashesTableView.getColumns().add(flatHillCol);
        crashesTableView.getColumns().add(guardRailCol);
        crashesTableView.getColumns().add(holidayCol);
        crashesTableView.getColumns().add(houseOrBuildingCol);
        crashesTableView.getColumns().add(intersectionCol);
        crashesTableView.getColumns().add(kerbCol);
        crashesTableView.getColumns().add(lightCol);
        crashesTableView.getColumns().add(minorInjuryCountCol);
        crashesTableView.getColumns().add(mopedCol);
        crashesTableView.getColumns().add(motorcycleCol);
        crashesTableView.getColumns().add(numberOfLanesCol);
        crashesTableView.getColumns().add(objectThrownOrDroppedCol);
        crashesTableView.getColumns().add(otherObjectCol);
        crashesTableView.getColumns().add(otherVehicleTypeCol);
        crashesTableView.getColumns().add(overBankCol);
        crashesTableView.getColumns().add(parkedVehicleCol);
        crashesTableView.getColumns().add(phoneBoxEtcCol);
        crashesTableView.getColumns().add(pedestrian);
        crashesTableView.getColumns().add(postOrPoleCol);
        crashesTableView.getColumns().add(roadCharacterCol);
        crashesTableView.getColumns().add(roadLaneCol);
        crashesTableView.getColumns().add(roadSurfaceCol);
        crashesTableView.getColumns().add(roadworksCol);
        crashesTableView.getColumns().add(schoolBusCol);
        crashesTableView.getColumns().add(seriousInjuryCountCol);
        crashesTableView.getColumns().add(slipOrFloodCol);
        crashesTableView.getColumns().add(speedLimitCol);
        crashesTableView.getColumns().add(strayAnimalCol);
        crashesTableView.getColumns().add(streetLightCol);
        crashesTableView.getColumns().add(suvCol);
        crashesTableView.getColumns().add(taxiCol);
        crashesTableView.getColumns().add(tlaNameCol);
        crashesTableView.getColumns().add(temporarySpeedLimitCol);
        crashesTableView.getColumns().add(trafficControlCol);
        crashesTableView.getColumns().add(trafficIslandCol);
        crashesTableView.getColumns().add(trafficSignCol);
        crashesTableView.getColumns().add(trainCol);
        crashesTableView.getColumns().add(treeCol);
        crashesTableView.getColumns().add(truckCol);
        crashesTableView.getColumns().add(unknownVehicleTypeCol);
        crashesTableView.getColumns().add(urbanCol);
        crashesTableView.getColumns().add(vanOrUtilityCol);
        crashesTableView.getColumns().add(vehicleCol);
        crashesTableView.getColumns().add(waterRiverCol);
        crashesTableView.getColumns().add(weatherACol);
        crashesTableView.getColumns().add(weatherBCol);
        crashesTableView.getColumns().add(latCol);
        crashesTableView.getColumns().add(lngCol);
    }
}

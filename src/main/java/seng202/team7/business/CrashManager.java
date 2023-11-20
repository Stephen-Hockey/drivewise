package seng202.team7.business;

import javafx.concurrent.Task;
import seng202.team7.controller.GraphController;
import seng202.team7.controller.MainController;
import seng202.team7.controller.MapController;
import seng202.team7.controller.TableViewController;
import seng202.team7.io.Importable;
import seng202.team7.map.Position;
import seng202.team7.model.Crash;
import seng202.team7.repository.CrashDAO;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Singleton class to handle and store all actions for current crashes and queries.
 * It is also the primary handler of data communication from map.js
 *
 * @author Joseph Hendry
 * @author Luke Edwards
 * @author Stephen Hockey
 * @author Bella Hill
 */
public class CrashManager {

    private final CrashDAO crashDAO; // The database access object
    private List<Crash> queryCrashes = new ArrayList<>(); // The crashes from the current query
    private List<Crash> currentCrashes = new ArrayList<>(); // The current stored crashes from filters

    // The controllers of each view to update
    private MapController mapController;
    private TableViewController tableViewController;
    private GraphController graphController;
    private final MainController mainController;

    /**
     * CrashManager constructor, which creates a private CrashDAO object it will later use for all database interactions.
     *
     * @param mainController The controller of main.fxml, useful to CrashManager for informing the user of error.
     */
    public CrashManager(MainController mainController) {
        crashDAO = new CrashDAO();
        this.mainController = mainController;
    }

    /**
     * CrashManager constructor for when given a crashDAO.
     *
     * @param crashDAO The CrashDAO
     * @param mainController The controller of main.fxml, useful to CrashManager for informing the user of error.
     */
    public CrashManager(CrashDAO crashDAO, MainController mainController) {
        this.crashDAO = crashDAO;
        this.mainController = mainController;
    }

    /**
     * Getter for the current crash values.
     *
     * @return List of crashes to display.
     */
    public List<Crash> getCurrentCrashes() {
        return currentCrashes;
    }

    /**
     * Getter for number of database crashes.
     *
     * @return Size of current list.
     */
    public int getDatabaseLength() {
        return crashDAO.getCrashesTableLength();
    }

    /**
     * Getter for the CrashDAO
     *
     * @return The CrashDAO
     */
    public CrashDAO getCrashDAO() {
        return crashDAO;
    }

    /**
     * Calls a function in crashDAO that clears the database
     */
    public void clearDatabase() {
        crashDAO.clearDatabase();
    }

    /**
     * Setter for the current crashes
     *
     * @param crashes the new current crashes
     */
    public void setCrashes(List<Crash> crashes) {
        this.queryCrashes.addAll(crashes);
        this.currentCrashes.addAll(crashes);
    }

    /**
     * Sets the controllers for thr crash manager to update.
     *
     * @param mapController The map controller
     * @param tableViewController The table controller
     * @param graphController The graph controller
     */
    public void setControllers(MapController mapController, TableViewController tableViewController, GraphController graphController) {
        this.mapController = mapController;
        this.tableViewController = tableViewController;
        this.graphController = graphController;
    }

    /**
     * Updates the map, table, graph, and main controllers
     */
    public void updateControllers() {
        mapController.update();
        tableViewController.update();
        graphController.update();
        mainController.update();
    }

    /**
     * Imports a list of crashes from a file using the provided importer and adds them to the database asynchronously.
     *
     * @param importer The importable object.
     * @param file     The file to import.
     * @param callback The threading call back function.
     */
    public void addAllCrashesFromFile(Importable<Crash> importer, File file, Runnable callback) {
        Task<Void> task = new Task<Void>() {
            @Override
            protected Void call() {
                // Get crashes from file
                List<Crash> crashes = importer.readFromFile(file);

                // Process each crash and update progress
                crashDAO.addBatch(crashes);
                return null;
            }
        };

        // Set up a callback for when the task is completed
        task.setOnSucceeded(e -> {
            if (callback != null) {
                callback.run();
            }
        });

        // Start the task in a new thread
        new Thread(task).start();
    }

    /**
     * Sets crashes to all crashes stored in the database and updates all the views.
     */
    public void setAllCrashes() {
        queryCrashes = crashDAO.getAll();
        currentCrashes.clear();
        currentCrashes.addAll(queryCrashes);
        updateControllers();
    }

    /**
     * Gets a specific page of crashes from the current list.
     *
     * @param pageNumber   The page number (starting from 0).
     * @param itemsPerPage The number of crashes to fetch per page.
     * @return A list of Crash objects for the specified page.
     */
    public List<Crash> getPage(int pageNumber, int itemsPerPage) {
        // Make the page list
        List<Crash> pageCrashes = new ArrayList<>();

        // Find the start and end index
        int startIndex = pageNumber * itemsPerPage;
        int endIndex = Math.min(startIndex + itemsPerPage, currentCrashes.size());

        // Ensure that startIndex and endIndex are within bounds
        if (startIndex >= 0 && startIndex < currentCrashes.size() && endIndex > startIndex) {
            pageCrashes.addAll(currentCrashes.subList(startIndex, endIndex));
        }

        // Return the page
        return pageCrashes;
    }

    /**
     * Performs an R-Tree search and updates the current list, then updates controllers.
     *
     * @param centre centre of the search circle.
     * @param radius radius of the search circle.
     */
    public void doRTreeCircleSearch(Position centre, double radius) {
        // Performs K-Tree search
        queryCrashes = crashDAO.rTreeCircleSearch(centre.lat, centre.lng, radius);
        currentCrashes.clear();
        currentCrashes.addAll(queryCrashes);

        // Updates controllers
        updateControllers();
    }

    /**
     * This function is called from map.js.
     * Queries the database for all the crashes within the given bounds, then sends the coordinates of them to map.js,
     * for the purpose of map.js handling the more complex job of finding which of those crashes are along a route.
     *
     * @param minLat minimum latitude (as a string because it is from map.js)
     * @param maxLat maximum latitude (as a string because it is from map.js)
     * @param minLng minimum longitude (as a string because it is from map.js)
     * @param maxLng maximum longitude (as a string because it is from map.js)
     */
    public void getBoundsOfRouteFromJS(String minLat, String maxLat, String minLng, String maxLng) {

        Position bottomLeft = new Position(Double.parseDouble(minLat), Double.parseDouble(minLng));
        Position topRight = new Position(Double.parseDouble(maxLat), Double.parseDouble(maxLng));

        currentCrashes = crashDAO.rTreeRectangleSearch(bottomLeft, topRight);

        List<Double> crashCoordsForJS = new ArrayList<>();
        for (Crash crash : currentCrashes) {
            crashCoordsForJS.add((double) crash.getLat());
            crashCoordsForJS.add((double) crash.getLng());
        }
        if(crashCoordsForJS.isEmpty()) {
            mapController.setLoadingIndicatorVisible(false);
            updateControllers();
        } else {
            mapController.sendCrashesInBounds(crashCoordsForJS);
        }

    }

    /**
     * This function is called from map.js.
     * After map.js finds all the crashes on the route, they are sent to this function so they can be
     * stored for the purposes of the several views, the advice cards, etc.
     *
     * @param jsCrashIndexesString a string representing a list of indexes of crashes from the bounding box search that
     *                             are actually on the route
     */
    public void getCrashesOnRouteFromJS(String jsCrashIndexesString) {
        String[] indexStrings = jsCrashIndexesString.split(",");
        queryCrashes.clear();
        for (String indexString : indexStrings) {
            int index = Integer.parseInt(indexString);
            queryCrashes.add(currentCrashes.get(index));
        }
        currentCrashes.clear();
        currentCrashes.addAll(queryCrashes);
        if(currentCrashes.isEmpty()) {
            mapController.setLoadingIndicatorVisible(false);
            updateControllers();
        }

        try {
            mapController.setLoadingIndicatorVisible(false);
        } catch (Exception ignored) {
        }

        updateControllers();
    }

    /**
     * Applies user-oriented simple filters that act on the current pool of crashes
     * @param showCars true if crashes involving a car are desired
     * @param showBikes true if crashes involving a bike are desired
     * @param showPedestrian true if crashes involving a pedestrian are desired
     * @param showFatal true if fatal crashes are desired
     * @param showSerious true if serious crashes are desired
     * @param showMinor true if minor crashes are desired
     * @param startYear the lower bound of the year filter
     * @param endYear the upper bound of the year filter
     */
    public void applyFilters(boolean showCars, boolean showBikes, boolean showPedestrian, boolean showFatal, boolean showSerious, boolean showMinor, int startYear, int endYear) {
        // Clear the current filters
        currentCrashes.clear();

        // Run the car type filters
        if (showCars || showBikes || showPedestrian) {
            for (Crash crash : queryCrashes) {
                // Check if the crash matches the selected criteria
                boolean match = (showCars && (crash.getSuv() > 0 || crash.getCarStationWagon() > 0)) ||
                        (showBikes && (crash.getBicycle() > 0)) ||
                        (showPedestrian && (crash.getPedestrian() > 0));

                if (match) {
                    currentCrashes.add(crash);
                }
            }
        } else {
            currentCrashes.addAll(queryCrashes);
        }

        // Run the severity filters
        if (showFatal || showSerious || showMinor) {
            currentCrashes.removeIf(crash -> {
                // Define the matching criteria

                return !(showFatal && crash.getSeverity().equals("Fatal Crash")) &&
                        !(showSerious && crash.getSeverity().equals("Serious Crash")) &&
                        !(showMinor && (crash.getSeverity().equals("Minor Crash") || crash.getSeverity().equals("Non-Injury Crash")));
            });

        }

        // Run the year filters
        currentCrashes.removeIf(crash -> crash.getYear() < startYear || crash.getYear() > endYear);

        // Check for errors and show user
        if (queryCrashes.isEmpty()) {
            mainController.showMessage("Please first select a area or route search.");
        } else if (startYear > endYear) {
            mainController.showMessage("Please select a start year less than or equal to the end year.");
        } else if (currentCrashes.isEmpty()) {
            mainController.showMessage("The filters applied returned no results.");
        } else {
            // If no errors update controllers
            updateControllers();
        }
    }
}

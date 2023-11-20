package seng202.team7.controller;

import javafx.application.Platform;
import javafx.concurrent.Worker;
import javafx.fxml.FXML;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import javafx.stage.Stage;
import seng202.team7.business.CrashManager;
import seng202.team7.exceptions.GeolocatorFailedException;
import seng202.team7.map.Geolocator;
import seng202.team7.map.Position;
import seng202.team7.map.Route;
import netscape.javascript.JSObject;
import seng202.team7.model.Crash;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * FXML controller class for map.fxml.
 * It also solely handles the calling of functions in map.js.
 *
 * @author Stephen Hockey
 * @author Joseph Hendry
 * @author Bella Hill
 * @author Kendra van Loon
 */
public class MapController {
    @FXML
    private WebView webView;
    @FXML
    private ProgressIndicator loadingIndicator;
    private WebEngine webEngine;
    private JSObject javaScriptConnector;
    private Geolocator geolocator;
    private CrashManager crashManager;
    private Position centre;
    private String mapViewMode;
    private double radius;
    private String centreStr;

    /**
     * Initialises the map with a specific AnalysisViewController to allow for communication between these two
     * controllers - allowing a bridge from map.js.
     *
     * @param stage current stage
     * @param crashManager the crash manager to use
     */
    public void init(Stage stage, CrashManager crashManager) {

        // Set variables
        this.crashManager = crashManager;
        this.mapViewMode = "Empty";

        geolocator = new Geolocator();

        // Initialise the map.
        initMap();
    }

    /**
     * Initialises the WebView loading in the appropriate html and initialising important communicator
     * objects between Java and Javascript.
     */
    private void initMap() {
        webEngine = webView.getEngine();
        webEngine.setJavaScriptEnabled(true);
        webEngine.load(Objects.requireNonNull(MapController.class.getResource("/html/map.html")).toExternalForm());

        webEngine.getLoadWorker().stateProperty().addListener(
                (ov, oldState, newState) -> {
                    // if javascript loads successfully
                    if (newState == Worker.State.SUCCEEDED) {
                        // set our bridge object
                        JSObject window = (JSObject) webEngine.executeScript("window");
                        //window.setMember("javaScriptBridge", javaScriptBridge);
                        window.setMember("crashManager", crashManager);
                        // get a reference to the js object that has a reference to the js methods we need to use in java
                        javaScriptConnector = (JSObject) webEngine.executeScript("jsConnector");
                        // call the javascript function to initialise the map
                        javaScriptConnector.call("initMap");
                    }
                });
    }

    /**
     * Sets the loading indicator to not visible.
     *
     * @param visible The visibility of the loading indicator
     */
    public void setLoadingIndicatorVisible(boolean visible) {
        this.loadingIndicator.setVisible(visible);
    }

    /**
     * Selects the correct map mode for a filter depending on the current state.
     */
    public void setMapViewMode() {
        if (mapViewMode.equals("Cluster") || mapViewMode.equals("Empty")) {
            this.mapViewMode = "Filter Points";
        }
    }

    /**
     * Sets the relevant values for a route analysis.
     *
     * @param startStr a string of the start location
     * @param endStr a string of the end location
     * @param transportMode the transport mode ("bike", "car", "walk")
     *
     * @throws GeolocatorFailedException If a location can not be found
     */
    public void setRoute(String startStr, String endStr, String transportMode) throws GeolocatorFailedException {
        // Set map modes
        mapViewMode = "Empty";

        // Clear map
        clearMap();

        // Shows the loading wheel
        Position A = geolocator.queryAddress(startStr);
        Position B = geolocator.queryAddress(endStr);
        Route route = new Route(A, B);

        loadingIndicator.setVisible(true);
        // Generate and display map points and get route
        displayRoute(route, transportMode, startStr, endStr);
    }

    /**
     * Sets the relevant values for an area analysis.
     *
     * @param centreAddress address of the centre of the search circle
     * @param radius radius of the search circle
     * @param mapViewMode the view mode of the map
     *
     * @throws GeolocatorFailedException If a location can not be found
     */
    public void setArea(String centreAddress, double radius, String mapViewMode) throws GeolocatorFailedException {
        // Set centre position of the query
        this.centre = geolocator.queryAddress(centreAddress);

        loadingIndicator.setVisible(true);

        this.centreStr = centreAddress;

        // Set the current radius
        this.radius = radius;

        // Set the mapViewMode
        this.mapViewMode = mapViewMode;

        // Do area search
        crashManager.doRTreeCircleSearch(centre, radius);
    }

    /**
     * Updates the maps points. Based on the current set mode.
     */
    public void update() {

        // Check for appropriate map view mode.
        switch (mapViewMode) {
            case "Cluster" -> {
                // Clear map
                clearMap();
                List<Double> crashList = new ArrayList<>();
                // Get each point from crash manager
                for (Crash crash : crashManager.getCurrentCrashes()) {


                    // Add each point to cluster map if not null
                    if (crash != null && crash.getLat() != 0f && crash.getLng() != 0f) {
                        Position position = new Position(crash.getLat(), crash.getLng());
                        crashList.add(position.lat);
                        crashList.add(position.lng);
                    }
                }
                addCrashMarkers(crashList);

                // Display the points and area circle on map
                displayAreaCircle(centre, radius, centreStr);
                displayCrashMarkers();
            }
            case "Heatmap" -> {
                // Clear map
                clearMap();

                // Get each point from crash manager
                for (Crash crash : crashManager.getCurrentCrashes()) {

                    // Add each point to heat map if not null
                    if (crash != null && crash.getLat() != 0f && crash.getLng() != 0f) {
                        addHeatMarker(crash);
                    }
                }

                // Display the points
                displayHeatMap(centre, radius);
            }
            case "Filter Points" -> {
                // Remove just the markers
                removeCrashMarkers();

                List<Double> crashList = new ArrayList<>();
                // Get each point from crash manager
                for (Crash crash : crashManager.getCurrentCrashes()) {

                    // Add each point to cluster map if not null
                    if (crash != null && crash.getLat() != 0f && crash.getLng() != 0f) {
                        Position position = new Position(crash.getLat(), crash.getLng());
                        crashList.add(position.lat);
                        crashList.add(position.lng);
                    }
                }

                // Add and display the crash markers
                addCrashMarkers(crashList);
                displayCrashMarkers();
            }
            case "Empty" -> {
                // Don't do anything when no mode is set.
            }
        }
        loadingIndicator.setVisible(false);
    }

    /**
     * Calls the JS function that adds a crash marker to the map.
     *
     * @param crashes crashes to be added to map
     */
    private void addCrashMarkers(List<Double> crashes) {
        javaScriptConnector.call("addCrashMarkers", crashes); }
    /**
     * Calls the JS function that adds a heat marker to the map.
     *
     * @param crash crash to be added to heatmap
     */
    private void addHeatMarker(Crash crash) {
        javaScriptConnector.call("addHeatMarker", crash.toMarkerString(), crash.getLat(), crash.getLng(), crash.getSeverity());
    }

    /**
     * Calls the JS function which displays the given route on the map.
     * @param newRoute route to be displayed, made up of 2 or more Positions
     */
    private void displayRoute(Route newRoute, String transportMode, String startStr, String endStr) {
        javaScriptConnector.call("displayRoute", newRoute.toJSONArray(), transportMode, startStr, endStr);
    }

    /**
     * Calls the JS function that displays the visual indicator of the area being analysed on the map.
     *
     * @param centre centre of the search circle
     * @param radius radius of the search circle
     * @param centreStr string name of the centre of the search circle
     */
    private void displayAreaCircle(Position centre, double radius, String centreStr) {
        javaScriptConnector.call("displayAreaCircle", centre, radius, centreStr);
        Platform.runLater(() -> {
            loadingIndicator.setVisible(false);
        });
    }
    /**
     * Calls the JS function that displays the crash marker clustering of the area being analysed on the map.
     */
    private void displayCrashMarkers() { javaScriptConnector.call("displayCrashMarkers");}
    /**
     * Calls the JS function that displays the heatmap of the area being analysed on the map.
     */
    private void displayHeatMap(Position centre, double radius) { javaScriptConnector.call("displayHeatMap", centre, radius); }

    /**
     * Calls the JS function that removes the current route generated from the map.
     */
    private void removeRoute() {javaScriptConnector.call("removeRoute");}

    /**
     * Calls the JS function that removes the visual indicator of the area being analysed from the map.
     */
    private void removeAreaCircle() { javaScriptConnector.call("removeAreaCircle"); }

    /**
     * Calls the JS function that removes the crash markers of the area being analysed from the map.
     */
    private void removeCrashMarkers() { javaScriptConnector.call("removeCrashMarkers"); }
    /**
     * Calls the JS function that removes the heatmap of the area being analysed from the map.
     */
    private void removeHeatMap() { javaScriptConnector.call("removeHeatMap"); }

    /**
     * Called by CrashManager, and sends a list of crashes that are inside just the bounding rectangle of the route.
     * @param crashes list of coordinates of the form {lat1, lng1, lat2, lng2, ...}
     */
    public void sendCrashesInBounds(List<Double> crashes) {
        javaScriptConnector.call("receiveCrashesInBounds", crashes);
        loadingIndicator.setVisible(false);
    }


    /**
     * A single function to clear all points on the map
     */
    private void clearMap() {
        removeRoute();
        removeAreaCircle();
        removeCrashMarkers();
        removeHeatMap();
    }
}
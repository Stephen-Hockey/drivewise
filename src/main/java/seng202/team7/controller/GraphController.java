package seng202.team7.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.chart.*;
import javafx.stage.Stage;
import seng202.team7.business.CrashManager;
import seng202.team7.model.Crash;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * This class is the controller for the graphs view.
 * It generates the graphs based off of the current data stored in the crash manager.
 *
 * @author Joseph Hendry
 */
public class GraphController {

    private CrashManager crashManager;

    // The graph type variables
    @FXML
    private BarChart<String,Number> yearBarChart;

    @FXML
    private PieChart severityPieChart;
    @FXML
    private PieChart weatherPieChart;


    /**
     * The initiation class for the Graphs.
     * Sets the stage and the crash manager.
     *
     * @param stage The parent classes stage.
     * @param crashManager The crash manager instance to use.
     */
    public void init(Stage stage, CrashManager crashManager) {
        // Set stage and crash manager
        // The parent stage and crash manager
        this.crashManager = crashManager;
    }

    /**
     * Updates the charts to show new data.
     * Used by the crash manager when new data is added.
     */
    public void update() {
        // Run each graph class
        yearBarChart();
        severityPieChart();
        weatherPieChart();
    }

    ////////// Independent Graph Classes //////////

    /**
     * Adds the current data to the year bar chart.
     */
    private void yearBarChart() {

        // Initialises a data structure to store crash counts by year
        Map<Integer, Integer> crashCountsByYear = new HashMap<>();

        // Iterate through the crashes and count them by year
        for (Crash crash : crashManager.getCurrentCrashes()) {
            int year = crash.getYear();
            crashCountsByYear.put(year, crashCountsByYear.getOrDefault(year, 0) + 1);
        }

        // Adds the remaining years with a count of zero crashes so that they are displayed properly on the bar chart
        for (int year = 2000; year <= 2023; year++) {
            if(!crashCountsByYear.containsKey(year)) {
                crashCountsByYear.put(year, 0);
            }
        }


        // Sort the years in ascending order
        List<Integer> sortedYears = crashCountsByYear.keySet().stream().sorted().toList();


        // Create a BarChart with the defined X and Y axes
        yearBarChart.setTitle("Crashes by Year");

        // Remove the legend
        yearBarChart.setLegendVisible(false);

        // Create a series to hold the data
        XYChart.Series<String, Number> series = new XYChart.Series<>();

        // Iterate through the sorted years and add data points to the series
        for (Integer year : sortedYears) {
            int count = crashCountsByYear.get(year);
            series.getData().add(new XYChart.Data<>(year.toString(), count));
        }

        // Clear existing then add new data in the BarChart
        yearBarChart.getData().clear();
        yearBarChart.getData().add(series);
    }

    /**
     * Adds the current data to the severity pie chart.
     */
    private void severityPieChart() {

        // Initialize a data structure to store crash counts by year
        Map<String, Integer> crashCountsBySeverity = new HashMap<>();

        // Iterate through the crashes and count them by year
        for (Crash crash : crashManager.getCurrentCrashes()) {

            // Get crash severity
            String severity = crash.getSeverity();

            // Add severity if not null
            if (!Objects.equals(severity, "Null")) {
                crashCountsBySeverity.put(severity, crashCountsBySeverity.getOrDefault(severity, 0) + 1);
            }
        }

        // Set the title
        severityPieChart.setTitle("Severity");

        // Create the pie chart data list
        ObservableList<PieChart.Data> pieChartData = FXCollections.observableArrayList();

        // Add the data to the list
        for (Map.Entry<String, Integer> entry : crashCountsBySeverity.entrySet()) {
            String severity = entry.getKey();
            int count = entry.getValue();
            pieChartData.add(new PieChart.Data(severity, count));
        }

        // Clear and set the Pie Chart
        severityPieChart.getData().clear();
        severityPieChart.setData(pieChartData);
    }

    /**
     * Adds the current data to the weather pir chart.
     */
    private void weatherPieChart() {

        // Initialize a data structure to store crash counts by year
        Map<String, Integer> crashCountsByWeather = new HashMap<>();

        // Iterate through the crashes and count them by year
        for (Crash crash : crashManager.getCurrentCrashes()) {

            // Get weather with preference to weather b
            String weather;
            if (!Objects.equals(crash.getWeatherB(), "Null")) {
                weather = crash.getWeatherB();
            } else {
                weather = crash.getWeatherA();
            }

            // Add weather if not null
            if (!Objects.equals(weather, "Null")) {
                crashCountsByWeather.put(weather, crashCountsByWeather.getOrDefault(weather, 0) + 1);
            }
        }

        // Set the title
        weatherPieChart.setTitle("Weather");

        // Create the pie chart data
        ObservableList<PieChart.Data> pieChartData = FXCollections.observableArrayList();

        // Add the data to the list
        for (Map.Entry<String, Integer> entry : crashCountsByWeather.entrySet()) {
            String weather = entry.getKey();
            int count = entry.getValue();
            pieChartData.add(new PieChart.Data(weather, count));
        }

        // Clear and set the Pie Chart
        weatherPieChart.getData().clear();
        weatherPieChart.setData(pieChartData);
    }
}

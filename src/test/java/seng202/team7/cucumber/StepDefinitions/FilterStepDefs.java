package seng202.team7.cucumber.StepDefinitions;

import io.cucumber.java.Before;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.junit.jupiter.api.Assertions;
import seng202.team7.business.CrashManager;
import seng202.team7.controller.GraphController;
import seng202.team7.controller.MainController;
import seng202.team7.controller.MapController;
import seng202.team7.controller.TableViewController;
import seng202.team7.io.CrashCSVImporter;
import seng202.team7.model.Crash;
import seng202.team7.repository.CrashDAO;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Cucumbers tests for the filtering.
 * @author Joseph Hendry
 */
public class FilterStepDefs {

    private static CrashManager crashManager;
    static CrashCSVImporter importer;

    private boolean showPedestrian = false;
    private int startYear = 2000;
    private int endYear = 2023;

    @Before
    public void setup() {

        // Create mock objects for controllers and dependencies
        MapController mockMapController = mock(MapController.class);
        TableViewController mockTableViewController = mock(TableViewController.class);
        GraphController mockGraphController = mock(GraphController.class);
        MainController mockMainController = mock(MainController.class);
        CrashDAO mockCrashDAO = mock(CrashDAO.class);

        // Create an instance of the CrashManager with the mock dependencies
        crashManager = new CrashManager(mockCrashDAO, mockMainController);
        importer = new CrashCSVImporter();

        // Set the controllers and importer for the CrashManager
        crashManager.setControllers(mockMapController, mockTableViewController, mockGraphController);
    }

    private List<Crash> createSampleCrashes() {

        List<Crash> mockCrashes = new ArrayList<>();

        for (int i = 0; i < 100; i++) {
            // Create a mock Crash object
            Crash mockCrash = mock(Crash.class);
            if (i % 10 == 0) {
                when(mockCrash.getPedestrian()).thenReturn(1);
                when(mockCrash.getYear()).thenReturn(2018);
            } else {
                when(mockCrash.getPedestrian()).thenReturn(0);
                when(mockCrash.getYear()).thenReturn(2015);
            }

            // Add the mock Crash object to the list
            mockCrashes.add(mockCrash);
        }

        return mockCrashes;
    }
    
    @Given("I have done an area or route search")
    public void iHaveDoneAnAreaOrRouteSearch() {
        crashManager.setCrashes(createSampleCrashes());
    }

    @And("I have selected the Pedestrian filter")
    public void iHaveSelectedThePedestrianFilter() {
        showPedestrian = true;
    }

    @And("I have selected the start year as {int}")
    public void iHaveSelectedTheStartYear(int startYear) {
        this.startYear = startYear;
    }

    @And("I have selected the end year as {int}")
    public void iHaveSelectedTheEndYear(int endYear) {
        this.endYear = endYear;
    }

    @When("I click filter")
    public void iClickFilter() {
        boolean showCars = false;
        boolean showBikes = false;
        boolean showFatal = false;
        boolean showSerious = false;
        boolean showMinor = false;
        crashManager.applyFilters(showCars, showBikes, showPedestrian, showFatal, showSerious, showMinor, startYear, endYear);
    }

    @Then("only crashes with a pedestrian involved are loaded into the controllers")
    public void onlyCrashesInvolvingPedestriansAppear() {
        Assertions.assertEquals(10, crashManager.getCurrentCrashes().size());
    }

    @Then("only crashes between 2016 and 2022 are loaded into the controllers")
    public void onlyCrashesBetweenYearsAppear() {
        Assertions.assertEquals(10, crashManager.getCurrentCrashes().size());
    }
}

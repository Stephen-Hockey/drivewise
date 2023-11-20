package seng202.team7.unittests.businessTest;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.mockito.Mockito.*;

import seng202.team7.business.CrashManager;
import seng202.team7.controller.GraphController;
import seng202.team7.controller.MainController;
import seng202.team7.controller.MapController;
import seng202.team7.controller.TableViewController;
import seng202.team7.io.CrashCSVImporter;
import seng202.team7.map.Position;
import seng202.team7.model.Crash;
import seng202.team7.repository.CrashDAO;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
/**
 * @author Joseph Hendry
 * @author Kendra van Loon
 * Test class for AdviceLogic
 */
public class CrashManagerTest {

    private static CrashManager crashManager;
    private static MainController mockMainController;
    private static MapController mockMapController;
    private static TableViewController mockTableViewController;
    private static GraphController mockGraphController;
    private static CrashDAO mockCrashDAO;
    static CrashCSVImporter importer;

    //CrashDAO crashDAO;

    @BeforeEach
    public void setUp() {
        // Create mock objects for controllers and dependencies
        mockMapController = mock(MapController.class);
        mockTableViewController = mock(TableViewController.class);
        mockGraphController = mock(GraphController.class);
        mockMainController = mock(MainController.class);
        mockCrashDAO = mock(CrashDAO.class);

        // Create an instance of the CrashManager with the mock dependencies
        crashManager = new CrashManager(mockCrashDAO, mockMainController);
        importer = new CrashCSVImporter();

        // Set the controllers and importer for the CrashManager
        crashManager.setControllers(mockMapController, mockTableViewController, mockGraphController);
    }

    @Test
    public void testGetCrashes() {
        // Arrange
        List<Crash> sampleCrashes = createSampleCrashes(50);
        crashManager.setCrashes(sampleCrashes);

        // Act
        List<Crash> crashes = crashManager.getCurrentCrashes();

        // Assert
        Assertions.assertEquals(sampleCrashes, crashes);
    }

    @Test
    public void testGetDatabaseLength() {
        // Act
        crashManager.getDatabaseLength();

        // Assert
        verify(mockCrashDAO).getCrashesTableLength();
    }

    @Test
    public void testCrashDAO() {
        // Arrange
        CrashManager testCrashManager = new CrashManager(mockMainController);

        // Act
        CrashDAO testCrashDAO = testCrashManager.getCrashDAO();

        // Assert
        Assertions.assertNotNull(testCrashDAO);
    }

    @Test
    public void testUpdateControllers() {
        // Act
        crashManager.updateControllers();

        // Assert
        verify(mockMapController).update();
        verify(mockTableViewController).update();
        verify(mockGraphController).update();
        verify(mockMainController).update();
    }

    @Test
    public void testSetAllCrashes() {

        // Act
        crashManager.setAllCrashes();

        // Assert
        verify(mockCrashDAO).getAll();

        //Assertions.assertEquals(crashManager.getCurrentCrashesLength(), 10); //
    }

    private List<Crash> createSampleCrashes(int numberOfMocks) {

        List<Crash> mockCrashes = new ArrayList<>();

        for (int i = 0; i < numberOfMocks; i++) {
            // Create a mock Crash object
            Crash mockCrash = mock(Crash.class);

            // Add the mock Crash object to the list
            mockCrashes.add(mockCrash);
        }

        return mockCrashes;
    }


    @Test
    public void testGetPage_withValidPageNumber() {
        // Arrange
        crashManager.setCrashes(createSampleCrashes(10)); // Provide a list of sample crashes
        int pageNumber = 0; // The first page
        int itemsPerPage = 10; // Number of items per page

        // Act
        List<Crash> page = crashManager.getPage(pageNumber, itemsPerPage);

        // Assert
        Assertions.assertEquals(10, page.size()); // Check that the expected number of items is returned
    }

    @Test
    public void testGetPage_withPageNumberOutOfBounds() {
        // Arrange
        crashManager.setCrashes(createSampleCrashes(5)); // Provide a list of sample crashes
        int pageNumber = 5; // An out-of-bounds page number
        int itemsPerPage = 10; // Number of items per page

        // Act
        List<Crash> page = crashManager.getPage(pageNumber, itemsPerPage);

        // Assert
        Assertions.assertTrue(page.isEmpty()); // Ensure that an empty list is returned for an out-of-bounds page
    }

    @Test
    public void testGetPage_withValidPageNumberAndPartialPage() {
        // Arrange
        crashManager.setCrashes(createSampleCrashes(15)); // Provide a list of 15 sample crashes
        int pageNumber = 1; // The second page
        int itemsPerPage = 10; // Number of items per page

        // Act
        List<Crash> page = crashManager.getPage(pageNumber, itemsPerPage);

        // Assert
        Assertions.assertEquals(5, page.size()); // Check that only the remaining 5 items are returned
    }

    @Test
    public void testGetPage_withEmptyCrashesList() {
        // Arrange
        crashManager.setCrashes(createSampleCrashes(0)); // An empty list of crashes
        int pageNumber = 0;
        int itemsPerPage = 10;

        // Act
        List<Crash> page = crashManager.getPage(pageNumber, itemsPerPage);

        // Assert
        Assertions.assertTrue(page.isEmpty()); // Ensure that an empty list is returned
    }

    @Test
    public void testGetPage_withNegativeItemsPerPage() {
        // Arrange
        crashManager.setCrashes(createSampleCrashes(0));
        int pageNumber = 0;
        int itemsPerPage = -5; // Negative itemsPerPage

        // Act
        List<Crash> page = crashManager.getPage(pageNumber, itemsPerPage);

        // Assert
        Assertions.assertTrue(page.isEmpty()); // Ensure that an empty list is returned for negative itemsPerPage
    }

    @Test
    public void testGetPage_withStartIndexOutOfBounds() {
        // Arrange
        crashManager.setCrashes(createSampleCrashes(10)); // Provide a list of sample crashes
        int pageNumber = 1; // A page with startIndex out of bounds
        int itemsPerPage = 10; // Number of items per page

        // Act
        List<Crash> page = crashManager.getPage(pageNumber, itemsPerPage);

        // Assert
        Assertions.assertTrue(page.isEmpty()); // Ensure that an empty list is returned when startIndex is out of bounds
    }

    @Test
    public void testGetPage_withEndIndexOutOfBounds() {
        // Arrange
        crashManager.setCrashes(createSampleCrashes(5)); // Provide a list of sample crashes
        int pageNumber = 0; // A page with endIndex out of bounds
        int itemsPerPage = 10; // Number of items per page

        // Act
        List<Crash> page = crashManager.getPage(pageNumber, itemsPerPage);

        // Assert
        Assertions.assertEquals(5, page.size()); // Check that only the available items are returned when endIndex is out of bounds
    }

    @Test
    public void testGetPage_withBothIndicesLessThanZero() {
        // Arrange
        crashManager.setCrashes(createSampleCrashes(5)); // Provide a list of sample crashes
        int pageNumber = -2; // A negative page number
        int itemsPerPage = 10; // Number of items per page

        // Act
        List<Crash> page = crashManager.getPage(pageNumber, itemsPerPage);

        // Assert
        Assertions.assertTrue(page.isEmpty()); // Ensure that an empty list is returned when both indices are less than zero
    }

    @Test
    public void testGetPage_withStartIndexLessThanZeroAndEndIndexZeroOrPositive() {
        // Arrange
        crashManager.setCrashes(createSampleCrashes(10)); // Provide a list of sample crashes
        int pageNumber = -1; // A page with startIndex less than zero but endIndex greater than or equal to zero
        int itemsPerPage = 10; // Number of items per page

        // Act
        List<Crash> page = crashManager.getPage(pageNumber, itemsPerPage);

        // Assert
        Assertions.assertTrue(page.isEmpty()); // Ensure that an empty list is returned when startIndex is less than zero but endIndex is greater than or equal to zero
    }

    @Test
    public void testGetPage_withStartIndexZeroOrPositiveAndEndIndexLessThanOrEqualToZero() {
        // Arrange
        crashManager.setCrashes(createSampleCrashes(5)); // Provide a list of sample crashes
        int pageNumber = 0; // A page with startIndex zero or positive but endIndex less than or equal to zero
        int itemsPerPage = -3; // Negative itemsPerPage

        // Act
        List<Crash> page = crashManager.getPage(pageNumber, itemsPerPage);

        // Assert
        Assertions.assertTrue(page.isEmpty()); // Ensure that an empty list is returned when startIndex is zero or positive but endIndex is less than or equal to zero
    }

    @Test void testDoRTreeCircleSearch() {
        // Arrange
        Position position = new Position(50.5, 80.5);
        double radius = 67.89;

        // Act
        crashManager.doRTreeCircleSearch(position, radius);

        // Assert
        verify(mockCrashDAO).rTreeCircleSearch(position.lat, position.lng, radius);
        verify(mockMapController, times(1)).update();
        verify(mockTableViewController, times(1)).update();
        verify(mockGraphController, times(1)).update();
    }

    @Test
    public void testGetBoundsOfRouteFromJS() {
        // Arrange
        String minLat = "42.0";
        String maxLat = "43.0";
        String minLng = "-71.0";
        String maxLng = "-70.0";

        // Create sample crashes to be returned by mockCrashDAO
        List<Crash> sampleCrashes = createSampleCrashes(5);
        when(mockCrashDAO.rTreeRectangleSearch(any(), any())).thenReturn(sampleCrashes);

        // Act
        crashManager.getBoundsOfRouteFromJS(minLat, maxLat, minLng, maxLng);

        // Assert
        // Verify that the correct positions are used to call rTreeRectangleSearch
        verify(mockCrashDAO).rTreeRectangleSearch(
                any(Position.class),
                any(Position.class)
        );

        // Verify that the expected data is sent to the mapController
        List<Double> expectedCrashCoords = new ArrayList<>();
        for (Crash crash : sampleCrashes) {
            expectedCrashCoords.add((double) crash.getLat());
            expectedCrashCoords.add((double) crash.getLng());
        }

        verify(mockMapController).sendCrashesInBounds(expectedCrashCoords);
    }

    @Test
    public void testGetCrashesOnRouteFromJS() {
        // Arrange
        String jsCrashIndexesString = "1,3,5"; // Sample string with crash indexes
        List<Crash> sampleCrashes = createSampleCrashes(10); // Create sample data
        List<Crash> expectedCrashes = Arrays.asList(sampleCrashes.get(1), sampleCrashes.get(3), sampleCrashes.get(5)); // Set the expected value
        crashManager.setCrashes(sampleCrashes); // Add it to the crash manager

        // Act
        crashManager.getCrashesOnRouteFromJS(jsCrashIndexesString);

        // Assert
        Assertions.assertEquals(expectedCrashes, crashManager.getCurrentCrashes());
        verify(mockMapController, times(1)).update();
        verify(mockTableViewController, times(1)).update();
        verify(mockGraphController, times(1)).update();
    }

    @Test
    void testApplyFilters_AllTrue() {
        // Arrange
        // Create a mock Crash object
        Crash mockCrash = mock(Crash.class);

        // Define the behavior for the mock object
        when(mockCrash.getYear()).thenReturn(2016);
        when(mockCrash.getSuv()).thenReturn(1);
        when(mockCrash.getCarStationWagon()).thenReturn(1);
        when(mockCrash.getBicycle()).thenReturn(1);
        when(mockCrash.getPedestrian()).thenReturn(1);
        when(mockCrash.getSeverity()).thenReturn("Fatal Crash");

        // Create a list of the mock and add it to the crash manager
        List<Crash> crashList = new ArrayList<>();
        crashList.add(mockCrash);
        crashManager.setCrashes(crashList);

        // Apply filters with all flags set to true
        crashManager.applyFilters(true, true, true, true, true, true, 2000, 2022);

        // Assert that currentCrashes should remain the same
        Assertions.assertEquals(crashList, crashManager.getCurrentCrashes());
    }

    @Test
    void testApplyFilters_AllFalse() {
        // Arrange
        // Create a mock Crash object
        Crash mockCrash = mock(Crash.class);

        // Define the behavior for the mock object
        when(mockCrash.getYear()).thenReturn(2016);

        // Create a list of the mock and add it to the crash manager
        List<Crash> crashList = new ArrayList<>();
        crashList.add(mockCrash);
        crashManager.setCrashes(crashList);

        // Act
        crashManager.applyFilters(false, false, false, false, false, false, 2000, 2022);

        // Assert that currentCrashes contains all queryCrashes
        Assertions.assertEquals(crashList, crashManager.getCurrentCrashes());
    }

    @Test
    void testApplyFilters_EmptyQueryList() {
        // Set query list to be empty
        crashManager.setCrashes(new ArrayList<>());

        // Act
        crashManager.applyFilters(false, false, false, false, false, false, 2000, 2022);

        // Assert that currentCrashes should be empty
        Assertions.assertTrue(crashManager.getCurrentCrashes().isEmpty());
        verify(mockMainController).showMessage("Please first select a area or route search.");
    }

    @Test
    void testApplyFilters_YearsSwapped() {
        // Set query list to be empty
        Crash mockCrash = mock(Crash.class);

        // Define the behavior for the mock object
        when(mockCrash.getYear()).thenReturn(2016);

        // Create a list of the mock and add it to the crash manager
        List<Crash> crashList = new ArrayList<>();
        crashList.add(mockCrash);
        crashManager.setCrashes(crashList);

        // Act
        crashManager.applyFilters(false, false, false, false, false, false, 2022, 2000);

        // Assert that currentCrashes should be empty
        Assertions.assertTrue(crashManager.getCurrentCrashes().isEmpty());
        verify(mockMainController).showMessage("Please select a start year less than or equal to the end year.");
    }

    @Test
    void testApplyFilters_YearsEqual() {
        // Set query list to be empty
        Crash mockCrash = mock(Crash.class);

        // Define the behavior for the mock object
        when(mockCrash.getYear()).thenReturn(2016);

        // Create a list of the mock and add it to the crash manager
        List<Crash> crashList = new ArrayList<>();
        crashList.add(mockCrash);
        crashManager.setCrashes(crashList);

        // Act
        crashManager.applyFilters(false, false, false, false, false, false, 2022, 2022);

        // Assert that currentCrashes should be empty
        Assertions.assertTrue(crashManager.getCurrentCrashes().isEmpty());
        verify(mockMainController).showMessage("The filters applied returned no results.");
    }

    @Test
    void testApplyFilters_EmptyQueryResult() {
        // Arrange
        // Create a mock Crash object
        Crash mockCrash = mock(Crash.class);

        // Define the behavior for the mock object
        when(mockCrash.getYear()).thenReturn(2016);
        when(mockCrash.getSuv()).thenReturn(0);
        when(mockCrash.getCarStationWagon()).thenReturn(0);

        // Create a list of the mock and add it to the crash manager
        List<Crash> crashList = new ArrayList<>();
        crashList.add(mockCrash);
        crashManager.setCrashes(crashList);

        // Act
        crashManager.applyFilters(true, false, false, false, false, false, 2000, 2022);

        // Assert that currentCrashes should be empty
        Assertions.assertTrue(crashManager.getCurrentCrashes().isEmpty());
        verify(mockMainController).showMessage("The filters applied returned no results.");
    }

    @Test
    void testApplyFilters_MatchAllButOneCriteria() {
        // Arrange
        // Create a mock Crash object
        Crash mockCrash = mock(Crash.class);

        // Define the behavior for the mock object
        when(mockCrash.getYear()).thenReturn(2016);
        when(mockCrash.getSuv()).thenReturn(1); // Matches showCars but not showPedestrian
        when(mockCrash.getCarStationWagon()).thenReturn(1); // Matches showCars but not showPedestrian
        when(mockCrash.getBicycle()).thenReturn(0);
        when(mockCrash.getPedestrian()).thenReturn(0);
        when(mockCrash.getSeverity()).thenReturn("Fatal Crash");

        // Create a list of the mock and add it to the crash manager
        List<Crash> crashList = new ArrayList<>();
        crashList.add(mockCrash);
        crashManager.setCrashes(crashList);

        // Apply filters with showCars, showBikes, and showFatal set to true, but showPedestrian set to false
        crashManager.applyFilters(true, false, true, true, false, false, 2000, 2022);

        // Assert that the mockCrash should not be in currentCrashes as it doesn't match showPedestrian criteria
        Assertions.assertTrue(crashManager.getCurrentCrashes().contains(mockCrash));
    }

    @Test
    void testApplyFilters_MatchSeverityCriteria() {
        // Arrange
        // Create a mock Crash object
        Crash mockCrash = mock(Crash.class);

        // Define the behavior for the mock object
        when(mockCrash.getYear()).thenReturn(2016);
        when(mockCrash.getSeverity()).thenReturn("Fatal Crash"); // Matches showFatal

        // Create a list of the mock and add it to the crash manager
        List<Crash> crashList = new ArrayList<>();
        crashList.add(mockCrash);
        crashManager.setCrashes(crashList);

        // Apply filters with showFatal set to true and others set to false
        crashManager.applyFilters(false, false, false, true, false, false, 2000, 2022);

        // Assert that the mockCrash should still be in currentCrashes as it matches showFatal criteria
        Assertions.assertTrue(crashManager.getCurrentCrashes().contains(mockCrash));
    }

    @Test
    void testApplyFilters_OutOfRangeYear() {
        // Arrange
        // Create a mock Crash object
        Crash mockCrash = mock(Crash.class);

        // Define the behavior for the mock object
        when(mockCrash.getYear()).thenReturn(2016);

        // Create a list of the mock and add it to the crash manager
        List<Crash> crashList = new ArrayList<>();
        crashList.add(mockCrash);
        crashManager.setCrashes(crashList);

        // Apply filters with out-of-range year criteria
        crashManager.applyFilters(false, false, false, false, false, false, 2000, 2010);

        // Assert that the mockCrash should not be in currentCrashes as its year is out of the specified range
        Assertions.assertFalse(crashManager.getCurrentCrashes().contains(mockCrash));
    }

    @Test
    void testApplyFilters_OnlySeverity() {
        // Arrange
        // Create a mock Crash object
        Crash mockCrash = mock(Crash.class);

        // Define the behavior for the mock object
        when(mockCrash.getYear()).thenReturn(2016);
        when(mockCrash.getSeverity()).thenReturn("Serious Crash"); // Matches showSerious

        // Create a list of the mock and add it to the crash manager
        List<Crash> crashList = new ArrayList<>();
        crashList.add(mockCrash);
        crashManager.setCrashes(crashList);

        // Apply filters with only showSerious set to true
        crashManager.applyFilters(false, false, false, false, true, false, 2000, 2022);

        // Assert that the mockCrash should still be in currentCrashes as it matches showSerious criteria
        Assertions.assertTrue(crashManager.getCurrentCrashes().contains(mockCrash));
    }

    @Test
    void testApplyFilters_NoSeverityMatch() {
        // Arrange
        // Create a mock Crash object
        Crash mockCrash = mock(Crash.class);

        // Define the behavior for the mock object
        when(mockCrash.getYear()).thenReturn(2016);
        when(mockCrash.getSeverity()).thenReturn("Fatal Crash"); // Matches showFatal

        // Create a list of the mock and add it to the crash manager
        List<Crash> crashList = new ArrayList<>();
        crashList.add(mockCrash);
        crashManager.setCrashes(crashList);

        // Apply filters with showSerious and showMinor set to true, but showFatal set to false
        crashManager.applyFilters(false, false, false, false, true, true, 2000, 2022);

        // Assert that the mockCrash should not be in currentCrashes as it doesn't match showFatal criteria
        Assertions.assertFalse(crashManager.getCurrentCrashes().contains(mockCrash));
    }

    @Test
    void testApplyFilters_AllDisabledButYearRange() {
        // Arrange
        // Create a mock Crash object
        Crash mockCrash = mock(Crash.class);

        // Define the behavior for the mock object
        when(mockCrash.getYear()).thenReturn(2016);

        // Create a list of the mock and add it to the crash manager
        List<Crash> crashList = new ArrayList<>();
        crashList.add(mockCrash);
        crashManager.setCrashes(crashList);

        // Apply filters with all criteria disabled except year range
        crashManager.applyFilters(false, false, false, false, false, false, 2000, 2022);

        // Assert that the mockCrash should still be in currentCrashes as year range is the only active criteria
        Assertions.assertTrue(crashManager.getCurrentCrashes().contains(mockCrash));
    }
}
package seng202.team7.unittests.repositoryTest;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import seng202.team7.io.CrashCSVImporter;
import seng202.team7.map.Position;
import seng202.team7.model.Crash;
import seng202.team7.repository.CrashDAO;
import seng202.team7.repository.DatabaseManager;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Joseph Hendry
 * @author Kendra van Loon
 * Test class for AdviceLogic
 */

public class CrashDAOTest {
    static DatabaseManager dataManager;
    static CrashDAO crashDAO;
    static File file1;
    static Crash crash1;

    @BeforeAll
    static public void setup() {

        dataManager = null;
        dataManager = DatabaseManager.getInstance();
        crashDAO = new CrashDAO();

        crash1 = new Crash(
                30,
                0,
                0,
                0,
                1,
                0,
                "SH 1N",
                "MANUKAU OFF SBD",
                "Non-Injury Crash",
                2001,
                0,
                0,
                0,
                "Flat",
                0,
                null,
                0,
                null,
                0,
                "Overcast",
                0,
                0,
                0,
                3,
                0,
                0,
                0,
                0,
                0,
                0,
                0,
                0,
                "Nil",
                "1-way",
                "Sealed",
                0,
                0,
                0,
                0,
                100,
                0,
                "On",
                0,
                0,
                "Manawatu District",
                30,
                "Stop",
                0,
                1,
                0,
                0,
                0,
                0,
                "Open",
                1,
                0,
                0,
                "Heavy rain",
                null,
                (float)-40.2569761312752,
                (float)175.388698343564
        );

        file1 = new File("crash_data_10k.csv");
    }

    @BeforeEach
    public void resetDB() {
        dataManager.resetDB();
    }

    @Test
    public void startsEmpty() {Assertions.assertEquals(0, crashDAO.getAll().size());}

    @Test
    public void addOneCrashWithAddBatch() {
        ArrayList<Crash> crashes = new ArrayList<>();
        crashes.add(crash1);
        crashDAO.addBatch(crashes);
        Assertions.assertEquals(1, crashDAO.getAll().size());
    }

    @Test
    public void addBatch() {
        CrashCSVImporter importer = new CrashCSVImporter();
        List<Crash> crashes = importer.readFromFile(file1);
        crashDAO.addBatch(crashes);

        Assertions.assertEquals(9878, crashDAO.getCrashesTableLength());
    }

    @Test
    public void tableLenTest() {
        CrashCSVImporter importer = new CrashCSVImporter();
        List<Crash> crashes = importer.readFromFile(file1);
        crashDAO.addBatch(crashes);

        Assertions.assertEquals(9878, crashDAO.getCrashesTableLength());
    }

    @Test
    public void getPageTest() {

        CrashCSVImporter importer = new CrashCSVImporter();
        List<Crash> crashes = importer.readFromFile(file1);
        crashDAO.addBatch(crashes);
        List<Crash> listTest = crashDAO.getPage(2, 10);

        Assertions.assertEquals(10, listTest.size());
        Assertions.assertEquals(21, listTest.get(0).getId());
        Assertions.assertEquals(26, listTest.get(5).getId());
        Assertions.assertEquals(30, listTest.get(9).getId());
    }

    @Test
    public void getAllTest() {
        CrashCSVImporter importer = new CrashCSVImporter();
        List<Crash> crashes = importer.readFromFile(file1);
        crashDAO.addBatch(crashes);

        Assertions.assertEquals(9878, crashDAO.getAll().size());
        Assertions.assertEquals(1, crashDAO.getAll().get(0).getId());
        Assertions.assertEquals(10255, crashDAO.getAll().get(9877).getId());
        Assertions.assertEquals(5102, crashDAO.getAll().get(5000).getId());

    }

    @Test
    public  void deleteCheck() {
        CrashCSVImporter importer = new CrashCSVImporter();
        List<Crash> crashes = importer.readFromFile(file1);
        crashDAO.addBatch(crashes);
        crashDAO.delete(1);

        Assertions.assertEquals(2, crashDAO.getAll().get(0).getId());
    }


    @Test
    public void rTreeSearchCheck() {
        CrashCSVImporter importer = new CrashCSVImporter();
        List<Crash> crashes = importer.readFromFile(file1);
        crashDAO.addBatch(crashes);

        Position position = new Position(-43.5225, 172.5794);
        List<Crash> list = crashDAO.rTreeCircleSearch(position.lat, position.lng, 1);

        Assertions.assertEquals(15, list.size());
        Assertions.assertEquals(848, list.get(0).getId());
        Assertions.assertEquals(9937, list.get(14).getId());

    }

    @Test
    public void testRTreeRectangleSearch() {
        // Arrange: Prepare sample data
        CrashCSVImporter importer = new CrashCSVImporter();
        List<Crash> crashes = importer.readFromFile(file1);
        crashDAO.addBatch(crashes);

        // Define the coordinates of the bottom left and top right corners of the rectangle
        Position bottomLeft = new Position(-43.55, 172.55);
        Position topRight = new Position(-43.50, 172.60);

        // Act: Perform the R-tree rectangle search
        List<Crash> results = crashDAO.rTreeRectangleSearch(bottomLeft, topRight);

        // Assert: Check the results
        Assertions.assertEquals(104, results.size());
        Assertions.assertEquals(4508, results.get(0).getId());
        Assertions.assertEquals(1212, results.get(14).getId());
    }

    @Test
    public void testClearDatabase() {
        // Arrange: Insert sample data into the database
        CrashCSVImporter importer = new CrashCSVImporter();
        List<Crash> crashes = importer.readFromFile(file1);
        crashDAO.addBatch(crashes);

        // Act: Call the clearDatabase method
        crashDAO.clearDatabase();

        // Assert: Check if the database is empty
        Assertions.assertEquals(0, crashDAO.getCrashesTableLength());
    }
}
package seng202.team7.unittests.ioTest;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import seng202.team7.io.CrashCSVImporter;

import java.io.File;

/**
 * Test class for AdviceLogic
 * @author Kendra van Loon
 */
public class CrashCSVImporterTest {

    static CrashCSVImporter importer;
    static File file;

    @BeforeAll
    public static void setUp() {
        importer = new CrashCSVImporter();
        file = new File("crash_data_10k.csv");
    }

    @Test
    public void readFromFileTest() {
        importer.readFromFile(file);
        importer.readFromFile(file);

        Assertions.assertEquals(10255,importer.readFromFile(file).size());
    }
}

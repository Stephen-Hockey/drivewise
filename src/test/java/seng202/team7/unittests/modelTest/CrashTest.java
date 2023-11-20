package seng202.team7.unittests.modelTest;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Assertions;
import seng202.team7.model.Crash;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Unit test for Crash Object
 * @author Kendra van Loon
 * @author Imogen Keeling
 */

public class CrashTest {

    private Crash crash;
    private Crash crash1;
    private Crash crashInvalidSeverity;

    /**
     * Creates an instance of crash to use for each test, instance created without ID.
     */
    @BeforeEach
    public void makeCrash() {
        Crash crashInstance = new Crash(
                30,
                0,
                0,
                1,
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
                (float) -40.2569761312752,
                (float) 175.388698343564
        );
        this.crash = crashInstance;


        Crash crashInstance1 = new Crash(
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
                0,
                0,
                0,
                "Heavy rain",
                null,
                (float) -40.2569761312752,
                (float) 175.388698343564
        );
        this.crash1 = crashInstance1;

        Crash invalidCrash = new Crash(
                30,
                0,
                0,
                1,
                1,
                0,
                "SH 1N",
                "MANUKAU OFF SBD",
                "Non Crash",
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
                (float) -40.2569761312752,
                (float) 175.388698343564
        );
        this.crashInvalidSeverity = invalidCrash;
    }


    /**
     * Checks that an instance of crash without a specified ID sets Id to -1.
     */
    @Test
    public void checkID() {
        assertEquals(-1, crash.getId());
    }

    @Test
    public void equalsChecks() {
        assertTrue(crash.equals(crash1));
    }

    /**
     * Tests that getNumericSeverity() converts to the correct number
     */
    @Test
    public void testNumericSeverity() {
        assertEquals(0.8, crash1.getNumericSeverity());
    }

    /**
     * Tests that when an invalid crash severity is given, the numeric severity is -1
     */
    @Test
    public void testNumericSeverityInvalid() {
        assertEquals(-1, crashInvalidSeverity.getNumericSeverity());
    }

    /**
     * Tests that isMultipleVehicles() returns 1 (True) when more than one moving vehicle is involved
     */
    @Test
    public void testIsMultipleVehicles() {
        assertEquals(1, crash.isMultipleVehicles());
    }

    /**
     * Tests that isMultipleVehicles() returns 0 (False) when less than 2 vehicles are involved in the crash
     */
    @Test
    public void testIsMultipleVehiclesNOT() {
        assertEquals(0, crash1.isMultipleVehicles());
    }

    @Test
    public void hashCodeCheck() {
        Assertions.assertEquals(1733575688, crash.hashCode());
    }

    @Test
    public void toMarkerStringCheck() {
        Assertions.assertEquals("Crash Here", crash.toMarkerString());
    }
}

package seng202.team7.unittests.mapTest;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import seng202.team7.exceptions.GeolocatorFailedException;
import seng202.team7.map.Geolocator;
import seng202.team7.map.Position;

import static java.lang.String.format;

/**
 * @author Joseph Hendry
 * @author Kendra van Loon
 * Test class for AdviceLogic
 */

public class GeoLocatorTest {

    private Geolocator geoLocator;
    private Position position;

    @BeforeEach
    public void setUp() {
        geoLocator = new Geolocator();
    }

    @Test
    public void UniversityAddressTest() {
        try {position = geoLocator.queryAddress("University of Canterbury");} catch (Exception ignored) {}

        String string = format("%.4f, %.4f", position.lat, position.lng);

        Assertions.assertEquals("-43.5241, 172.5803", string);
    }

    @Test
    public void normalAddressFormat() {
        try {position = geoLocator.queryAddress("205A Hills Road Edgeware Christchurch");} catch (Exception ignored) {}

        String string = format("%.4f, %.4f", position.lat, position.lng);

        Assertions.assertEquals("-43.5087, 172.6512", string);
    }

    @Test
    public void testQueryValidAddress() {
        try {
            Position position = geoLocator.queryAddress("1 Memorial Avenue");
            Assertions.assertNotNull(position);
            Assertions.assertEquals(-43.5187113, position.lat, 0.0001); // Ensure the latitude is approximately correct
            Assertions.assertEquals(172.5898077, position.lng, 0.0001); // Ensure the longitude is approximately correct
        } catch (GeolocatorFailedException e) {
            Assertions.fail("Geolocation failed for a valid address.");
        }
    }

    @Test
    public void testQueryInvalidAddress() {
        Assertions.assertThrows(GeolocatorFailedException.class, () -> {
            geoLocator.queryAddress("ThisIsAnInvalidAddress123");
        });
    }

    @Test
    public void testQueryInterruptedThread() {
        // Simulate an interrupted thread by setting the interrupted flag
        Thread.currentThread().interrupt();
        Assertions.assertThrows(GeolocatorFailedException.class, () -> {
            geoLocator.queryAddress("1 Memorial Avenue");
        });
        // Reset the interrupted flag to avoid affecting other tests
        Thread.interrupted();
    }
}

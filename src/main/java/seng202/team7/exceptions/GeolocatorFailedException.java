package seng202.team7.exceptions;

/**
 * Custom exception to be thrown when the Nominatim Geolocation API cannot successfully geolocate coordinates based on
 * a location entered by the user.
 * @author Stephen Hockey
 */
public class GeolocatorFailedException extends Exception {

    /**
     * The address represented by a string.
     */
    private final String address;

    /**
     * Simple constructor that passes to parent Exception class
     * @param address the address for which the exception occurred
     */
    public GeolocatorFailedException(String address) {
        super();
        this.address = address;
    }

    /**
     * Getter for address
     * @return address
     */
    public String getAddress() {
        return address;
    }
}
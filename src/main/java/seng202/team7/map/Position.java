package seng202.team7.map;

/**
 * Simple class representing a location as a Latitude and Longitude.
 *
 * @author Morgan English
 */
public class Position {
    /**
     * The latitude coordinate value.
     */
    public double lat;

    /**
     * The longitude coordinate value.
     */
    public double lng;

    /**
     * Position constructor with the provided lat and long.
     *
     * @param lat latitude for position
     * @param lng longitude for position
     */
    public Position(double lat, double lng) {
        this.lat = lat;
        this.lng = lng;
    }
}
package seng202.team7.map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import seng202.team7.exceptions.GeolocatorFailedException;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

/**
 * Class to handle requesting location from Nominatim Geolocation API.
 *
 * @author Morgan English
 */
public class Geolocator {

    private static final Logger log = LogManager.getLogger(Geolocator.class);

    /**
     * Runs a query with the address given and finds the most applicable lat, lng co-ordinates.
     *
     * @param address address to find lat, lng for
     * @return The position of the address
     * @throws GeolocatorFailedException error for when the address is not a valid location in Aotearoa or for when the geolocator fails for any reason at all
     */
    public Position queryAddress(String address) throws GeolocatorFailedException {
        String addressForUrl = address.replace(' ', '+');
        try {
            // Creating the http request
            HttpClient client = HttpClient.newHttpClient();
            HttpRequest request = HttpRequest.newBuilder(
                    URI.create("https://nominatim.openstreetmap.org/search?q=" + addressForUrl + ",+New+Zealand&format=json")
            ).build();
            // Getting the response
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            // Parsing the json response to get the latitude and longitude co-ordinates
            JSONParser parser = new JSONParser();
            JSONArray results = (JSONArray)  parser.parse(response.body());
            if (!results.isEmpty()) {
                JSONObject bestResult = (JSONObject) results.get(0);
                double lat = Double.parseDouble((String) bestResult.get("lat"));
                double lng = Double.parseDouble((String) bestResult.get("lon"));
                return new Position(lat, lng);
            }
        } catch (IOException | ParseException e) {
            log.error(e);
        } catch (InterruptedException ie ) {
            log.error(ie);
            Thread.currentThread().interrupt();
        }
        throw new GeolocatorFailedException(address);
    }
}

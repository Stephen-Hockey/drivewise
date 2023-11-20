package seng202.team7.io;

import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvValidationException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import seng202.team7.model.Crash;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * Implementation of Importable for crashes from a csv file.
 *
 * @author Morgan English
 * @author Imogen Keeling
 * @author Luke Edwards
 */
public class CrashCSVImporter implements Importable<Crash> {
    private static final Logger log = LogManager.getLogger(CrashCSVImporter.class);
    private int malformedLineCount = 0;
    private final String[] SEVERITY_LIST = {"Fatal Crash", "Serious Crash", "Minor Crash", "Non-Injury Crash"};
    private final String[] FLAT_HILL_LIST = {"Flat", "Hill Road", "Null"};
    private final String[] HOLIDAY_LIST = {"Christmas New Year", "Easter", "Queens Birthday", "Labour Weekend", ""};
    private final String[] LIGHT_LIST = {"Bright sun", "Overcast", "Twilight", "Dark", "Unknown"};
    private final String[] ROAD_CHARACTER_LIST = {"Bridge", "Motorway ramp", "Overpass", "Rail xing", "Speed hump", "Tunnel", "Tram lines", "Nil", "Underpass", "Null"};
    private final String[] ROAD_LANE_LIST = {"1-way", "2-way", "Off road", "Null"};
    private final String[] ROAD_SURFACE_LIST = {"Sealed", "Unsealed", "End of seal", "Null"};
    private final String[] STR_LIGHT_LIST = {"On", "Off", "None", "Null"};
    private final String[] TRAFFIC_CTRL_LIST = {"Traffic Signals", "Stop", "Give way", "Pointsman", "School Patrol/warden", "Nil", "Isolated Pedestrian signal (non-intersection)", "Unknown"};
    private final String[] URBAN_LIST = {"Urban", "Open"};
    private final String[] WEATHER_CON_A_LIST = {"Fine", "Mist or Fog", "Light rain", "Heavy rain", "Snow", "Hail or Sleet", "Null"};
    private final String[] WEATHER_CON_B_LIST = {"Frost", "Strong wind", "None", "Null"};

    /**
     * Read Crashes from csv file.
     *
     * @param file File to read from
     * @return List of crashes in csv file
     */
    @Override
    public List<Crash> readFromFile(File file) {
        ArrayList<Crash> crashes = new ArrayList<>();
        try (CSVReader reader = new CSVReader(new FileReader(file))) {
            reader.skip(1);
            String[] line = null;
            while((line = reader.readNext()) != null){
                if(line.length > 1){
                    Crash crash = readCrashFromLine(line);
                    if (crash != null) {
                        crashes.add(crash);
                    }else{
                        malformedLineCount++;
                    }
                }
            }
            log.info("Number of malformed lines: " + malformedLineCount);
            log.info("Number of correct lines: " + crashes.size());
            return crashes;
        } catch (IOException | CsvValidationException e) {
            log.error(e);
        }
        return Collections.emptyList();
    }

    /**
     * Checks if an integer column value is within a specified range.
     *
     * @param value     The integer value to check.
     * @param min       The minimum allowed value (inclusive).
     * @param max       The maximum allowed value (inclusive).
     * @throws IllegalArgumentException if the value is not within the specified range.
     */
    private void checkIntInRange(int value, int min, int max) {
        if (value < min || value > max) {
            throw new IllegalArgumentException("Value is not within the specified range: " + value);
        }
    }

    /**
     * Checks if a string column value is in a list of valid values.
     *
     * @param value             The string value to check.
     * @param validValues       A list of valid string values.
     * @throws IllegalArgumentException if the value is not in the list of valid values.
     */
    private void checkStringInList(String value, List<String> validValues) {
        if (!validValues.contains(value)) {
            throw new IllegalArgumentException("Value is not in the list of valid values: " + value + validValues);
        }
    }

    /**
     * Simple helper function to read crash from line of csv.
     *
     * @param line current line of csv to parse for a Crash object
     * @return Crash object parsed from line
     */
    private Crash readCrashFromLine(String[] line) {
        try {
            int advisorySpeed = (line[1].isEmpty()) ? 0 : Integer.parseInt(line[1]);
            int MAX_SPEED_VALUE = 110;
            checkIntInRange(advisorySpeed,0, MAX_SPEED_VALUE);

            int bicycle = (line[2].isEmpty()) ? 0 : Integer.parseInt(line[2]);
            int MAX_OBJ_VALUE = 100;
            checkIntInRange(bicycle,0, MAX_OBJ_VALUE);

            int bridge = (line[3].isEmpty()) ? 0 : Integer.parseInt(line[3]);
            checkIntInRange(bridge, 0, MAX_OBJ_VALUE); // Adjust the range as needed

            int bus = (line[4].isEmpty()) ? 0 : Integer.parseInt(line[4]);
            checkIntInRange(bus, 0, MAX_OBJ_VALUE); // Adjust the range as needed

            int carStationWagon = (line[5].isEmpty()) ? 0 : Integer.parseInt(line[5]);
            checkIntInRange(carStationWagon, 0, MAX_OBJ_VALUE); // Adjust the range as needed

            int cliffBank = (line[6].isEmpty()) ? 0 : Integer.parseInt(line[6]);
            checkIntInRange(cliffBank, 0, MAX_OBJ_VALUE); // Adjust the range as needed

            String crashLocation1 = line[9];
            String crashLocation2 = line[10];

            String crashSeverity = line[12];
            checkStringInList(crashSeverity, Arrays.asList(SEVERITY_LIST));

            int crashYear = (line[14].isEmpty()) ? 0 : Integer.parseInt(line[14]);
            checkIntInRange(crashYear, 2000, 2023); // Adjust the range as needed

            int ditch = (line[17].isEmpty()) ? 0 : Integer.parseInt(line[17]);
            checkIntInRange(ditch, 0, MAX_OBJ_VALUE); // Adjust the range as needed

            int fatalCount = (line[18].isEmpty()) ? 0 : Integer.parseInt(line[18]);
            checkIntInRange(fatalCount, 0, MAX_OBJ_VALUE); // Adjust the range as needed

            int fence = (line[19].isEmpty()) ? 0 : Integer.parseInt(line[19]);
            checkIntInRange(fence, 0, MAX_OBJ_VALUE); // Adjust the range as needed

            String flatHill = line[20];
            checkStringInList(flatHill, Arrays.asList(FLAT_HILL_LIST));

            int guardRail = (line[21].isEmpty()) ? 0 : Integer.parseInt(line[21]);
            checkIntInRange(guardRail, 0, MAX_OBJ_VALUE); // Adjust the range as needed

            String holiday = line[22];
            checkStringInList(holiday, Arrays.asList(HOLIDAY_LIST));

            int houseOrBuilding = (line[23].isEmpty()) ? 0 : Integer.parseInt(line[23]);
            checkIntInRange(houseOrBuilding, 0, MAX_OBJ_VALUE); // Adjust the range as needed

            String intersection = line[24];

            int kerb = (line[25].isEmpty()) ? 0 : Integer.parseInt(line[25]);
            checkIntInRange(kerb, 0, MAX_OBJ_VALUE); // Adjust the range as needed

            String light = line[26];
            checkStringInList(light, Arrays.asList(LIGHT_LIST));

            int minorInjuryCount = (line[27].isEmpty()) ? 0 : Integer.parseInt(line[27]);
            checkIntInRange(minorInjuryCount, 0, MAX_OBJ_VALUE); // Adjust the range as needed

            int moped = (line[28].isEmpty()) ? 0 : Integer.parseInt(line[28]);
            checkIntInRange(moped, 0, MAX_OBJ_VALUE); // Adjust the range as needed

            int motorcycle = (line[29].isEmpty()) ? 0 : Integer.parseInt(line[29]);
            checkIntInRange(motorcycle, 0, MAX_OBJ_VALUE); // Adjust the range as needed

            int numberOfLanes = (line[30].isEmpty()) ? 0 : Integer.parseInt(line[30]);
            checkIntInRange(numberOfLanes, 0, MAX_OBJ_VALUE); // Adjust the range as needed

            int objectThrownOrDropped = (line[31].isEmpty()) ? 0 : Integer.parseInt(line[31]);
            checkIntInRange(objectThrownOrDropped, 0, MAX_OBJ_VALUE); // Adjust the range as needed

            int otherObject = (line[32].isEmpty()) ? 0 : Integer.parseInt(line[32]);
            checkIntInRange(otherObject, 0, MAX_OBJ_VALUE); // Adjust the range as needed

            int otherVehicleType = (line[33].isEmpty()) ? 0 : Integer.parseInt(line[33]);
            checkIntInRange(otherVehicleType, 0, MAX_OBJ_VALUE); // Adjust the range as needed

            int overBank = (line[34].isEmpty()) ? 0 : Integer.parseInt(line[34]);
            checkIntInRange(overBank, 0, MAX_OBJ_VALUE); // Adjust the range as needed

            int parkedVehicle = (line[35].isEmpty()) ? 0 : Integer.parseInt(line[35]);
            checkIntInRange(parkedVehicle, 0, MAX_OBJ_VALUE); // Adjust the range as needed

            int pedestrian = (line[36].isEmpty()) ? 0 : Integer.parseInt(line[36]);
            checkIntInRange(pedestrian, 0, MAX_OBJ_VALUE); // Adjust the range as needed

            int phoneBoxEtc = (line[37].isEmpty()) ? 0 : Integer.parseInt(line[37]);
            checkIntInRange(phoneBoxEtc, 0, MAX_OBJ_VALUE); // Adjust the range as needed

            int postOrPole = (line[38].isEmpty()) ? 0 : Integer.parseInt(line[38]);
            checkIntInRange(postOrPole, 0, MAX_OBJ_VALUE); // Adjust the range as needed

            String roadCharacter = line[40];
            checkStringInList(roadCharacter, Arrays.asList(ROAD_CHARACTER_LIST));

            String roadLane = line[41];
            checkStringInList(roadLane, Arrays.asList(ROAD_LANE_LIST));

            String roadSurface = line[42];
            checkStringInList(roadSurface, Arrays.asList(ROAD_SURFACE_LIST));

            int roadworks = (line[43].isEmpty()) ? 0 : Integer.parseInt(line[43]);
            checkIntInRange(roadworks, 0, MAX_OBJ_VALUE); // Adjust the range as needed

            int schoolBus = (line[44].isEmpty()) ? 0 : Integer.parseInt(line[44]);
            checkIntInRange(schoolBus, 0, MAX_OBJ_VALUE); // Adjust the range as needed

            int seriousInjuryCount = (line[45].isEmpty()) ? 0 : Integer.parseInt(line[45]);
            checkIntInRange(seriousInjuryCount, 0, MAX_OBJ_VALUE); // Adjust the range as needed

            int slipOrFlood = (line[46].isEmpty()) ? 0 : Integer.parseInt(line[46]);
            checkIntInRange(slipOrFlood, 0, MAX_OBJ_VALUE); // Adjust the range as needed

            int speedLimit = (line[47].isEmpty()) ? 0 : Integer.parseInt(line[47]);
            checkIntInRange(speedLimit, 0, MAX_SPEED_VALUE);

            int strayAnimal = (line[48].isEmpty()) ? 0 : Integer.parseInt(line[48]);
            checkIntInRange(strayAnimal, 0, MAX_OBJ_VALUE); // Adjust the range as needed

            String streetLight = line[49];
            checkStringInList(streetLight, Arrays.asList(STR_LIGHT_LIST));

            int suv = (line[50].isEmpty()) ? 0 : Integer.parseInt(line[50]);
            checkIntInRange(suv, 0, MAX_OBJ_VALUE); // Adjust the range as needed

            int taxi = (line[51].isEmpty()) ? 0 : Integer.parseInt(line[51]);
            checkIntInRange(taxi, 0, MAX_OBJ_VALUE); // Adjust the range as needed

            int temporarySpeedLimit = (line[52].isEmpty()) ? 0 : Integer.parseInt(line[52]);
            checkIntInRange(temporarySpeedLimit, 0, MAX_SPEED_VALUE);

            String tlaName = line[53];

            String trafficControl = line[54];
            checkStringInList(trafficControl, Arrays.asList(TRAFFIC_CTRL_LIST));

            int trafficIsland = (line[55].isEmpty()) ? 0 : Integer.parseInt(line[55]);
            checkIntInRange(trafficIsland, 0, MAX_OBJ_VALUE); // Adjust the range as needed

            int trafficSign = (line[56].isEmpty()) ? 0 : Integer.parseInt(line[56]);
            checkIntInRange(trafficSign, 0, MAX_OBJ_VALUE); // Adjust the range as needed

            int train = (line[57].isEmpty()) ? 0 : Integer.parseInt(line[57]);
            checkIntInRange(train, 0, MAX_OBJ_VALUE); // Adjust the range as needed

            int tree = (line[58].isEmpty()) ? 0 : Integer.parseInt(line[58]);
            checkIntInRange(tree, 0, MAX_OBJ_VALUE); // Adjust the range as needed

            int truck = (line[59].isEmpty()) ? 0 : Integer.parseInt(line[59]);
            checkIntInRange(truck, 0, MAX_OBJ_VALUE); // Adjust the range as needed

            int unknownVehicleType = (line[60].isEmpty()) ? 0 : Integer.parseInt(line[60]);
            checkIntInRange(unknownVehicleType, 0, MAX_OBJ_VALUE); // Adjust the range as needed

            String urban = line[61];
            checkStringInList(urban, Arrays.asList(URBAN_LIST));

            int vanOrUtility = (line[62].isEmpty()) ? 0 : Integer.parseInt(line[62]);
            checkIntInRange(vanOrUtility, 0, MAX_OBJ_VALUE); // Adjust the range as needed

            int vehicle = (line[63].isEmpty()) ? 0 : Integer.parseInt(line[63]);
            checkIntInRange(vehicle, 0, MAX_OBJ_VALUE); // Adjust the range as needed

            int waterRiver = (line[64].isEmpty()) ? 0 : Integer.parseInt(line[64]);
            checkIntInRange(waterRiver, 0, MAX_OBJ_VALUE);

            String weatherA = line[65];
            checkStringInList(weatherA, Arrays.asList(WEATHER_CON_A_LIST));

            String weatherB = line[66];
            checkStringInList(weatherB, Arrays.asList(WEATHER_CON_B_LIST));

            float lng = Float.parseFloat(line[67]);
            float lat = Float.parseFloat(line[68]);

            return new Crash(advisorySpeed, bicycle, bridge, bus, carStationWagon, cliffBank,
                    crashLocation1, crashLocation2,
                    crashSeverity, crashYear, ditch, fatalCount, fence, flatHill, guardRail,
                    holiday, houseOrBuilding, intersection, kerb, light, minorInjuryCount, moped,
                    motorcycle, numberOfLanes, objectThrownOrDropped, otherObject, otherVehicleType,
                    overBank, parkedVehicle, pedestrian, phoneBoxEtc, postOrPole, roadCharacter,
                    roadLane, roadSurface, roadworks, schoolBus, seriousInjuryCount, slipOrFlood,
                    speedLimit, strayAnimal, streetLight, suv, taxi, tlaName, temporarySpeedLimit,
                    trafficControl, trafficIsland, trafficSign, train, tree, truck, unknownVehicleType,
                    urban, vanOrUtility, vehicle, waterRiver, weatherA, weatherB, lng, lat);
        } catch (NumberFormatException e) {
            log.warn("NumberFormatException occurred. Check the line indexes.");
        } catch (IllegalArgumentException e){
            log.warn(e);
        }
        return null;
    }
}

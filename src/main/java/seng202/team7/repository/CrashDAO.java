package seng202.team7.repository;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import seng202.team7.map.Position;
import seng202.team7.model.Crash;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Concrete implementation of Database Access Object that handles all crashes related actions to the database
 *
 * @author Morgan English
 * @author Joseph Hendry
 */
public class CrashDAO {
    private static final Logger log = LogManager.getLogger(CrashDAO.class);
    private final DatabaseManager databaseManager;

    /**
     * CrashDAO constructor, gets a reference to the database singleton
     */
    public CrashDAO() {
        databaseManager = DatabaseManager.getInstance();
    }

    /**
     * Returns the length/size of the database.
     *
     * @return the length/size of the database
     */
    public int getCrashesTableLength() {
        // Makes the sql query
        String sql = "SELECT COUNT(*) FROM crashes";

        // Tries to connect to the database and run the query.
        try (Connection conn = databaseManager.connect();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            if (rs.next()) {
                return rs.getInt(1); // Get the count from the first (and only) column
            } else {
                return 0; // Return 0 if there are no rows (table is empty)
            }
        } catch (SQLException sqlException) {
            log.error(sqlException);
            return -1; // Return -1 to indicate an error occurred
        }
    }

    /**
     * Creates R-tree index (should be done once during schema setup)
     */
    private void createRtreeIndex() {
        try (Connection conn = databaseManager.connect();
             Statement statement = conn.createStatement()) {
            String createIndexSql = "CREATE INDEX IF NOT EXISTS crashes_spatial_index ON crashes(lat, lng);";
            statement.execute(createIndexSql);
        } catch (SQLException sqlException) {
            log.error("Error creating R-tree index: " + sqlException.getMessage());
        }
    }

    /**
     * Does a circular R-Tree search on the database with the given clat, clng, and radius.
     *
     * @param clat centre latitude
     * @param clng centre longitude
     * @param radius radius
     * @return a list of all Crash instances that fall within the search
     */
    public List<Crash> rTreeCircleSearch(double clat, double clng, double radius) {
        // Creates the list and query
        List<Crash> results = new ArrayList<>();
        String sql = "SELECT * " +
                "FROM crashes " +
                "WHERE (6371 * acos(cos(radians(?)) * cos(radians(lat)) * cos(radians(lng) - radians(?)) + " +
                "sin(radians(?)) * sin(radians(lat)))) <= ?";

        // Tries to connect to the database and run the query
        try (Connection conn = databaseManager.connect();
            PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setDouble(1, clat);
            ps.setDouble(2, clng);
            ps.setDouble(3, clat);
            ps.setDouble(4, radius);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    results.add(buildCrashFromResultSet(rs));
                }
            }
        } catch (SQLException sqlException) {
            log.error(sqlException);
        }

        // Returns the result
        return results;
    }

    /**
     * Performs a rectangular R-Tree search.
     *
     * @param bottomLeft A position object representing the bottom left corner of the search rectangle
     * @param topRight A position object representing the top right corner of the search rectangle
     * @return A list of crashes that fall inside the search rectangle
     */
    public List<Crash> rTreeRectangleSearch(Position bottomLeft, Position topRight) {

        // Creates the list and makes the query
        List<Crash> results = new ArrayList<>();
        String sql = "SELECT *" +
                "FROM crashes " +
                "WHERE lat BETWEEN ? AND ? " +
                "AND lng BETWEEN ? AND ?";

        // Tries to connect to the database and run the query
        try (Connection conn = databaseManager.connect();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setDouble(1, bottomLeft.lat);
            ps.setDouble(2, topRight.lat);
            ps.setDouble(3, bottomLeft.lng);
            ps.setDouble(4, topRight.lng);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    results.add(buildCrashFromResultSet(rs));
                }
            }
        } catch (SQLException sqlException) {
            log.error(sqlException);
        }

        // Returns the result
        return results;
    }

    /**
     * Gets a specific page of crashes from the database, used for pagination in the table.
     *
     * @param pageNumber   The page number (starting from 0).
     * @param itemsPerPage The number of crashes to fetch per page.
     * @return A list of Crash objects for the specified page.
     */
    public List<Crash> getPage(int pageNumber, int itemsPerPage) {
        List<Crash> crashes = new ArrayList<>();
        String sql =  "SELECT * FROM crashes ORDER BY id LIMIT ? OFFSET ?";
        int offset = pageNumber * itemsPerPage;

        try (Connection conn = databaseManager.connect();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, itemsPerPage);
            ps.setInt(2, offset);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    crashes.add(buildCrashFromResultSet(rs));
                }
                return crashes;
            }
        } catch (SQLException sqlException) {
            log.error(sqlException);
            return new ArrayList<>();
        }
    }

    /**
     * Gets all crashes in database
     *
     * @return a list of all crashes
     */
    public List<Crash> getAll() {
        // Create the list to add to and the sql query
        List<Crash> crashes = new ArrayList<>();
        String sql = "SELECT * FROM crashes";

        // Tries to connect to the database and run the query
        try (Connection conn = databaseManager.connect();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                crashes.add(buildCrashFromResultSet(rs));
            }
            return crashes;
        } catch (SQLException sqlException) {
            log.error(sqlException);
            return new ArrayList<>();
        }
    }

    /**
     * Adds a batch of crashes to the database
     * This is done much quicker than individually
     *
     * @param toAdd list of crashes to add to the database
     */
    public void addBatch(List<Crash> toAdd) {
        // Creates the sql query.
        String sql = "INSERT OR IGNORE INTO crashes (advisorySpeed, bicycle, bridge, bus, carStationWagon, cliffBank, location1, location2, severity, year, ditch, fatalCount, fence, flatHill, guardRail, holiday, houseOrBuilding, intersection, kerb, light, minorInjuryCount, moped, motorcycle, numberOfLanes, objectThrownOrDropped, otherObject, otherVehicleType, overBank, parkedVehicle, phoneBoxEtc, pedestrian, postOrPole, roadCharacter, roadLane, roadSurface, roadworks, schoolBus, seriousInjuryCount, slipOrFlood, speedLimit, strayAnimal, streetLight, suv, taxi, tlaName, temporarySpeedLimit, trafficControl, trafficIsland, trafficSign, train, tree, truck, unknownVehicleType, urban, vanOrUtility, vehicle, waterRiver, weatherA, weatherB, lat, lng) values (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?);";

        // Tries to connect to the database and run the query
        try (Connection conn = databaseManager.connect();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            conn.setAutoCommit(false);
            for (Crash crash : toAdd) {
                setCrash(ps, crash);
            }
            ps.executeBatch();
            ResultSet rs = ps.getGeneratedKeys();
            while (rs.next()){
                log.info(rs.getLong(1));
            }
            conn.commit();
        } catch (SQLException sqlException) {
            log.error(sqlException);
        }

        // Complete a new r-tree index
        createRtreeIndex();
    }

    /**
     * Deletes crash from database by id
     *
     * @param id id of object to delete
     */
    public void delete ( int id){
        // Creates the sql query
        String sql = "DELETE FROM crashes WHERE id=?";

        // Connects to the database and runs the query
        try (Connection conn = databaseManager.connect();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            ps.executeUpdate();
        } catch (SQLException sqlException) {
            log.error(sqlException);
        }
    }

    /**
     * Clears sql database
     */
    public void clearDatabase() {
        // Creates the SQL query to delete all records from the crashes table
        String sql = "DELETE FROM crashes";

        // Connects to the database and runs the query
        try (Connection conn = databaseManager.connect();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            // Executes the query to delete all records
            ps.executeUpdate();
        } catch (SQLException sqlException) {
            // Handle any SQL exceptions (e.g., log or throw)
            log.error(sqlException);
        }
    }

    /**
     * Creates and returns the crash based on the result of a query
     *
     * @param rs result of the search
     * @return the created crash
     */
    private Crash buildCrashFromResultSet(ResultSet rs) throws SQLException {
        return new Crash(
                rs.getInt("id"),
                rs.getInt("advisorySpeed"),
                rs.getInt("bicycle"),
                rs.getInt("bridge"),
                rs.getInt("bus"),
                rs.getInt("carStationWagon"),
                rs.getInt("cliffBank"),
                rs.getString("location1"),
                rs.getString("location2"),
                rs.getString("severity"),
                rs.getInt("year"),
                rs.getInt("ditch"),
                rs.getInt("fatalCount"),
                rs.getInt("fence"),
                rs.getString("flatHill"),
                rs.getInt("guardRail"),
                rs.getString("holiday"),
                rs.getInt("houseOrBuilding"),
                rs.getString("intersection"),
                rs.getInt("kerb"),
                rs.getString("light"),
                rs.getInt("minorInjuryCount"),
                rs.getInt("moped"),
                rs.getInt("motorcycle"),
                rs.getInt("numberOfLanes"),
                rs.getInt("objectThrownOrDropped"),
                rs.getInt("otherObject"),
                rs.getInt("otherVehicleType"),
                rs.getInt("overBank"),
                rs.getInt("parkedVehicle"),
                rs.getInt("phoneBoxEtc"),
                rs.getInt("pedestrian"),
                rs.getInt("postOrPole"),
                rs.getString("roadCharacter"),
                rs.getString("roadLane"),
                rs.getString("roadSurface"),
                rs.getInt("roadworks"),
                rs.getInt("schoolBus"),
                rs.getInt("seriousInjuryCount"),
                rs.getInt("slipOrFlood"),
                rs.getInt("speedLimit"),
                rs.getInt("strayAnimal"),
                rs.getString("streetLight"),
                rs.getInt("suv"),
                rs.getInt("taxi"),
                rs.getString("tlaName"),
                rs.getInt("temporarySpeedLimit"),
                rs.getString("trafficControl"),
                rs.getInt("trafficIsland"),
                rs.getInt("trafficSign"),
                rs.getInt("train"),
                rs.getInt("tree"),
                rs.getInt("truck"),
                rs.getInt("unknownVehicleType"),
                rs.getString("urban"),
                rs.getInt("vanOrUtility"),
                rs.getInt("vehicle"),
                rs.getInt("waterRiver"),
                rs.getString("weatherA"),
                rs.getString("weatherB"),
                rs.getFloat("lat"),
                rs.getFloat("lng")
        );
    }

    /**
     * Sets the values of a crash to the values in the prepared statement.
     *
     * @param ps statement to add data to
     * @param crash the crash to add
     * @throws SQLException Exception to throw when error in sql
     */
    private void setCrash(PreparedStatement ps, Crash crash) throws SQLException {
        ps.setInt(1, crash.getAdvisorySpeed());
        ps.setInt(2, crash.getBicycle());
        ps.setInt(3, crash.getBridge());
        ps.setInt(4, crash.getBus());
        ps.setInt(5, crash.getCarStationWagon());
        ps.setInt(6, crash.getCliffBank());
        ps.setString(7, crash.getLocation1());
        ps.setString(8, crash.getLocation2());
        ps.setString(9, crash.getSeverity());
        ps.setInt(10, crash.getYear());
        ps.setInt(11, crash.getDitch());
        ps.setInt(12, crash.getFatalCount());
        ps.setInt(13, crash.getFence());
        ps.setString(14, crash.getFlatHill());
        ps.setInt(15, crash.getGuardRail());
        ps.setString(16, crash.getHoliday());
        ps.setInt(17, crash.getHouseOrBuilding());
        ps.setString(18, crash.getIntersection());
        ps.setInt(19, crash.getKerb());
        ps.setString(20, crash.getLight());
        ps.setInt(21, crash.getMinorInjuryCount());
        ps.setInt(22, crash.getMoped());
        ps.setInt(23, crash.getMotorcycle());
        ps.setInt(24, crash.getNumberOfLanes());
        ps.setInt(25, crash.getObjectThrownOrDropped());
        ps.setInt(26, crash.getOtherObject());
        ps.setInt(27, crash.getOtherVehicleType());
        ps.setInt(28, crash.getOverBank());
        ps.setInt(29, crash.getParkedVehicle());
        ps.setInt(30, crash.getPhoneBoxEtc());
        ps.setInt(31, crash.getPedestrian());
        ps.setInt(32, crash.getPostOrPole());
        ps.setString(33, crash.getRoadCharacter());
        ps.setString(34, crash.getRoadLane());
        ps.setString(35, crash.getRoadSurface());
        ps.setInt(36, crash.getRoadworks());
        ps.setInt(37, crash.getSchoolBus());
        ps.setInt(38, crash.getSeriousInjuryCount());
        ps.setInt(39, crash.getSlipOrFlood());
        ps.setInt(40, crash.getSpeedLimit());
        ps.setInt(41, crash.getStrayAnimal());
        ps.setString(42, crash.getStreetLight());
        ps.setInt(43, crash.getSuv());
        ps.setInt(44, crash.getTaxi());
        ps.setString(45, crash.getTlaName());
        ps.setInt(46, crash.getTemporarySpeedLimit());
        ps.setString(47, crash.getTrafficControl());
        ps.setInt(48, crash.getTrafficIsland());
        ps.setInt(49, crash.getTrafficSign());
        ps.setInt(50, crash.getTrain());
        ps.setInt(51, crash.getTree());
        ps.setInt(52, crash.getTruck());
        ps.setInt(53, crash.getUnknownVehicleType());
        ps.setString(54, crash.getUrban());
        ps.setInt(55, crash.getVanOrUtility());
        ps.setInt(56, crash.getVehicle());
        ps.setInt(57, crash.getWaterRiver());
        ps.setString(58, crash.getWeatherA());
        ps.setString(59, crash.getWeatherB());
        ps.setFloat(60, crash.getLat());
        ps.setFloat(61, crash.getLng());
        ps.addBatch();
    }
}
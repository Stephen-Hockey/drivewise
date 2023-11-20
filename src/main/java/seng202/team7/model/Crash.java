package seng202.team7.model;

import java.util.Objects;

/**
 * Model class for Crash object
 *
 * @author Luke Edwards
 * @author Kendra van Loon
 */
public class Crash {
    private int id;
    private final int advisorySpeed;
    private final int bicycle;
    private final int bridge;
    private final int bus;
    private final int carStationWagon;
    private final int cliffBank;
    private final String location1;
    private final String location2;
    private final String severity;
    private final int year;
    private final int ditch;
    private final int fatalCount;
    private final int fence;
    private final String flatHill;
    private final int guardRail;
    private final String holiday;
    private final int houseOrBuilding;
    private final String intersection;
    private final int kerb;
    private final String light;
    private final int minorInjuryCount;
    private final int moped;
    private final int motorcycle;
    private final int numberOfLanes;
    private final int objectThrownOrDropped;
    private final int otherObject;
    private final int otherVehicleType;
    private final int overBank;
    private final int parkedVehicle;
    private final int phoneBoxEtc;
    private final int pedestrian;
    private final int postOrPole;
    private final String roadCharacter;
    private final String roadLane;
    private final String roadSurface;
    private final int roadworks;
    private final int schoolBus;
    private final int seriousInjuryCount;
    private final int slipOrFlood;
    private final int speedLimit;
    private final int strayAnimal;
    private final String streetLight;
    private final int suv;
    private final int taxi;
    private final String tlaName;
    private final int temporarySpeedLimit;
    private final String trafficControl;
    private final int trafficIsland;
    private final int trafficSign;
    private final int train;
    private final int tree;
    private final int truck;
    private final int unknownVehicleType;
    private final String urban;
    private final int vanOrUtility;
    private final int vehicle;
    private final int waterRiver;
    private final String weatherA;
    private final String weatherB;
    private final float lat;
    private final float lng;

    /**
     * Constructor that takes all parameters and creates a new Crash object.
     *
     * @param id item database identifier
     * @param advisorySpeed The advisory (adv) speed (spd) at the crash site at the time of the crash.
     * @param bicycle Derived variable to indicate how many bicycles were involved in the crash.
     * @param bridge Derived variable to indicate how many times a bridge, tunnel, the abutments, handrails were struck in the crash.
     * @param bus Derived variable to indicate how many buses were involved in the crash (excluding school buses which are counted in the SCHOOL_BUS field).
     * @param carStationWagon Derived variable to indicate how many cars or station wagons were involved in the crash.
     * @param cliffBank Derived variable to indicate how many times a 'cliff' or 'bank' was struck in the crash. This includes retaining walls.
     * @param location1 Part 1 of the 'crash location' (crash_locn). May be a road name, route position (RP), landmark, or other, e.g., 'Ninety Mile Beach'. Used for location descriptions in reports, etc.
     * @param location2 Part 2 of the 'crash location' (crash_locn). May be a side road name, landmark, etc. Used for location descriptions in reports, etc.
     * @param severity The severity of a crash. Possible values are 'F' (fatal), 'S' (serious), 'M' (minor), 'N' (non-injury). This is determined by the worst injury sustained in the crash at the time of entry.
     * @param year The year in which a crash occurred if known.
     * @param ditch Derived variable to indicate how many times a 'ditch' or 'waterable drainage channel' was struck in a crash.
     * @param fatalCount A count of the number of fatal casualties associated with this crash.
     * @param fence Derived variable to indicate how many times a 'fence' was struck in the crash. This includes letterbox(es), hoardings, private roadside furniture, hedges, sight rails, etc.
     * @param flatHill Whether the road is flat or sloped. Possible values include 'Flat' or 'Hill'.
     * @param guardRail Derived variable to indicate how many times a guard or guard rail was struck in the crash. This includes 'New Jersey' barriers, 'ARMCO,' sand-filled barriers, wire catch fences, etc.
     * @param holiday Indicates where a crash occurred during a 'Christmas/New Year,' 'Easter,' 'Queens Birthday,' or 'Labour Weekend' holiday period; otherwise, 'None.'
     * @param houseOrBuilding Derived variable to indicate how many times houses, garages, sheds, or other buildings (Bldg) were struck in the crash.
     * @param intersection A derived variable to indicate if a crash occurred at an intersection (intsn) or not.
     * @param kerb Derived variable to indicate how many times a kerb was struck in the crash, contributing directly to the crash.
     * @param light The light at the time and place of the crash. Possible values: 'Bright Sun,' 'Overcast,' 'Twilight,' 'Dark,' or 'Unknown.'
     * @param minorInjuryCount A count of the number of minor injuries (inj) associated with this crash.
     * @param moped Derived variable to indicate how many mopeds were involved in the crash.
     * @param motorcycle Derived variable to indicate how many motorcycles were involved in the crash.
     * @param numberOfLanes The number (num) of lanes on the crash road.
     * @param objectThrownOrDropped Derived variable to indicate how many times objects were thrown at or dropped on vehicles in the crash.
     * @param otherObject Derived variable to indicate how many times an object was struck in a crash and the object struck was not pre-defined. This variable includes stockpiled materials, rubbish bins, fallen poles, fallen trees, etc.
     * @param otherVehicleType Derived variable to indicate how many other vehicles (not included in any other category) were involved in the crash.
     * @param overBank Derived variable to indicate how many times an embankment was struck or driven over during a crash. This variable includes other vertical drops driven over during a crash.
     * @param parkedVehicle Derived variable to indicate how many times a parked or unattended vehicle was struck in the crash. This variable can include trailers.
     * @param phoneBoxEtc Derived variable to indicate how many times a telephone kiosk, traffic signal controllers, bus shelters, or other public furniture were struck in the crash.
     * @param pedestrian Derived variable to indicate how many pedestrians were involved in the crash. This includes pedestrians on skateboards, scooters, and wheelchairs.
     * @param postOrPole Derived variable to indicate how many times a post or pole was struck in the crash. This includes light, power, phone, utility poles, and objects practically forming part of a pole (i.e., 'Transformer Guy' wires).
     * @param roadCharacter The general nature of the road. Possible values include 'Bridge,' 'Motorway Ramp,' 'Rail crossing,' or 'Nil.'
     * @param roadLane The lane configuration of the road. Possible values: '1' (one way), '2' (two ways), 'M' (for where a median exists), 'O' (for off-road lane configurations), ' ' (for unknown or invalid configurations).
     * @param roadSurface The road surface description applying at the crash site. Possible values: 'Sealed' or 'Unsealed.'
     * @param roadworks Derived variable to indicate how many times an object associated with 'roadworks' (including signs, cones, drums, barriers, but not roadwork vehicles) was struck during the crash.
     * @param schoolBus Derived variable to indicate how many school buses were involved in the crash.
     * @param seriousInjuryCount A count of the number of serious injuries (inj) associated with this crash.
     * @param slipOrFlood Derived variable to indicate how many times landslips, washouts, or floods (excluding rivers) were objects struck in the crash.
     * @param speedLimit The speed (spd) limit (lim) in force at the crash site at the time of the crash. May be a number or 'LSZ' for a limited speed zone.
     * @param strayAnimal Derived variable to indicate how many times a stray animal(s) was struck in the crash. This variable includes wild animals such as pigs, goats, deer, straying farm animals, house pets, and birds.
     * @param streetLight The street lighting at the time of the crash. Possible values 'On,' 'Off,' 'None,' or 'Unknown.'
     * @param suv Derived variable to indicate how many SUVs were involved in the crash.
     * @param taxi Derived variable to indicate how many taxis were involved in the crash.
     * @param tlaName The name of the territorial local authority (TLA) the crash has been attributed.
     * @param temporarySpeedLimit The temporary (temp) speed (spd) limit (lim) at the crash site if one exists (e.g., for road works).
     * @param trafficControl The traffic control (ctrl) signals at the crash site. Possible values are 'Traffic Signals,' 'Stop Sign,' 'Give Way Sign,' 'Pointsman,' 'School Patrol,' 'Nil,' or 'N/A.'
     * @param trafficIsland Derived variable to indicate how many times a traffic island, medians (excluding barriers) was struck in the crash.
     * @param trafficSign Derived variable to indicate how many times 'traffic signage' (including traffic signals, their poles, bollards, or roadside delineators) was struck in the crash.
     * @param train Derived variable to indicate how many times a train, rolling stock, or jiggers was struck in the crash, whether stationary or moving.
     * @param tree Derived variable to indicate how many times trees or other growing items were struck during the crash.
     * @param truck Derived variable to indicate how many trucks were involved in the crash.
     * @param unknownVehicleType Derived variable to indicate how many vehicles were involved in the crash (where the vehicle type is unknown).
     * @param urban A derived variable using the 'spd_lim' variable. Possible values are 'Urban' (urban, spd_lim less than 80) or 'Open Road' (open road, spd_lim >= 80 or 'LSZ').
     * @param vanOrUtility Derived variable to indicate how many vans or utes were involved in the crash.
     * @param vehicle Derived variable to indicate how many times a stationary attended vehicle was struck in the crash. This includes broken down vehicles, workmen's vehicles, taxis, buses.
     * @param waterRiver Derived variable to indicate how many times a body of water (including rivers, streams, lakes, the sea, tidal flats, canals, watercourses, or swamps) was struck in the crash.
     * @param weatherA Indicates weather at the crash time/place. See wthr_b. Values that are possible are 'Fine,' 'Mist,' 'Light Rain,' 'Heavy Rain,' 'Snow,' 'Unknown.'
     * @param weatherB The weather at the crash time/place. See weather_a. Values 'Frost,' 'Strong Wind,' or 'Unknown.'
     * @param lat The latitude coordinate of the crash location.
     * @param lng The longitude coordinate of the crash location.
     */
    public Crash(
            int id,
            int advisorySpeed,
            int bicycle,
            int bridge,
            int bus,
            int carStationWagon,
            int cliffBank,
            String location1,
            String location2,
            String severity,
            int year,
            int ditch,
            int fatalCount,
            int fence,
            String flatHill,
            int guardRail,
            String holiday,
            int houseOrBuilding,
            String intersection,
            int kerb,
            String light,
            int minorInjuryCount,
            int moped,
            int motorcycle,
            int numberOfLanes,
            int objectThrownOrDropped,
            int otherObject,
            int otherVehicleType,
            int overBank,
            int parkedVehicle,
            int phoneBoxEtc,
            int pedestrian,
            int postOrPole,
            String roadCharacter,
            String roadLane,
            String roadSurface,
            int roadworks,
            int schoolBus,
            int seriousInjuryCount,
            int slipOrFlood,
            int speedLimit,
            int strayAnimal,
            String streetLight,
            int suv,
            int taxi,
            String tlaName,
            int temporarySpeedLimit,
            String trafficControl,
            int trafficIsland,
            int trafficSign,
            int train,
            int tree,
            int truck,
            int unknownVehicleType,
            String urban,
            int vanOrUtility,
            int vehicle,
            int waterRiver,
            String weatherA,
            String weatherB,
            float lat,
            float lng
    ) {
        this.id = id;
        this.advisorySpeed = advisorySpeed;
        this.bicycle = bicycle;
        this.bridge = bridge;
        this.bus = bus;
        this.carStationWagon = carStationWagon;
        this.cliffBank = cliffBank;
        this.location1 = location1;
        this.location2 = location2;
        this.severity = severity;
        this.year = year;
        this.ditch = ditch;
        this.fatalCount = fatalCount;
        this.fence = fence;
        this.flatHill = flatHill;
        this.guardRail = guardRail;
        this.holiday = holiday;
        this.houseOrBuilding = houseOrBuilding;
        this.intersection = intersection;
        this.kerb = kerb;
        this.light = light;
        this.minorInjuryCount = minorInjuryCount;
        this.moped = moped;
        this.motorcycle = motorcycle;
        this.numberOfLanes = numberOfLanes;
        this.objectThrownOrDropped = objectThrownOrDropped;
        this.otherObject = otherObject;
        this.otherVehicleType = otherVehicleType;
        this.overBank = overBank;
        this.parkedVehicle = parkedVehicle;
        this.phoneBoxEtc = phoneBoxEtc;
        this.pedestrian = pedestrian;
        this.postOrPole = postOrPole;
        this.roadCharacter = roadCharacter;
        this.roadLane = roadLane;
        this.roadSurface = roadSurface;
        this.roadworks = roadworks;
        this.schoolBus = schoolBus;
        this.seriousInjuryCount = seriousInjuryCount;
        this.slipOrFlood = slipOrFlood;
        this.speedLimit = speedLimit;
        this.strayAnimal = strayAnimal;
        this.streetLight = streetLight;
        this.suv = suv;
        this.taxi = taxi;
        this.tlaName = tlaName;
        this.temporarySpeedLimit = temporarySpeedLimit;
        this.trafficControl = trafficControl;
        this.trafficIsland = trafficIsland;
        this.trafficSign = trafficSign;
        this.train = train;
        this.tree = tree;
        this.truck = truck;
        this.unknownVehicleType = unknownVehicleType;
        this.urban = urban;
        this.vanOrUtility = vanOrUtility;
        this.vehicle = vehicle;
        this.waterRiver = waterRiver;
        this.weatherA = weatherA;
        this.weatherB = weatherB;
        this.lat = lat;
        this.lng = lng;
    }

    /**
     * Constructor that takes all parameters and creates a new Crash object.
     *
     * @param advisorySpeed The advisory (adv) speed (spd) at the crash site at the time of the crash.
     * @param bicycle Derived variable to indicate how many bicycles were involved in the crash.
     * @param bridge Derived variable to indicate how many times a bridge, tunnel, the abutments, handrails were struck in the crash.
     * @param bus Derived variable to indicate how many buses were involved in the crash (excluding school buses which are counted in the SCHOOL_BUS field).
     * @param carStationWagon Derived variable to indicate how many cars or station wagons were involved in the crash.
     * @param cliffBank Derived variable to indicate how many times a 'cliff' or 'bank' was struck in the crash. This includes retaining walls.
     * @param location1 Part 1 of the 'crash location' (crash_locn). May be a road name, route position (RP), landmark, or other, e.g., 'Ninety Mile Beach'. Used for location descriptions in reports, etc.
     * @param location2 Part 2 of the 'crash location' (crash_locn). May be a side road name, landmark, etc. Used for location descriptions in reports, etc.
     * @param severity The severity of a crash. Possible values are 'F' (fatal), 'S' (serious), 'M' (minor), 'N' (non-injury). This is determined by the worst injury sustained in the crash at the time of entry.
     * @param year The year in which a crash occurred if known.
     * @param ditch Derived variable to indicate how many times a 'ditch' or 'waterable drainage channel' was struck in a crash.
     * @param fatalCount A count of the number of fatal casualties associated with this crash.
     * @param fence Derived variable to indicate how many times a 'fence' was struck in the crash. This includes letterbox(es), hoardings, private roadside furniture, hedges, sight rails, etc.
     * @param flatHill Whether the road is flat or sloped. Possible values include 'Flat' or 'Hill'.
     * @param guardRail Derived variable to indicate how many times a guard or guard rail was struck in the crash. This includes 'New Jersey' barriers, 'ARMCO,' sand-filled barriers, wire catch fences, etc.
     * @param holiday Indicates where a crash occurred during a 'Christmas/New Year,' 'Easter,' 'Queens Birthday,' or 'Labour Weekend' holiday period; otherwise, 'None.'
     * @param houseOrBuilding Derived variable to indicate how many times houses, garages, sheds, or other buildings (Bldg) were struck in the crash.
     * @param intersection A derived variable to indicate if a crash occurred at an intersection (intsn) or not.
     * @param kerb Derived variable to indicate how many times a kerb was struck in the crash, contributing directly to the crash.
     * @param light The light at the time and place of the crash. Possible values: 'Bright Sun,' 'Overcast,' 'Twilight,' 'Dark,' or 'Unknown.'
     * @param minorInjuryCount A count of the number of minor injuries (inj) associated with this crash.
     * @param moped Derived variable to indicate how many mopeds were involved in the crash.
     * @param motorcycle Derived variable to indicate how many motorcycles were involved in the crash.
     * @param numberOfLanes The number (num) of lanes on the crash road.
     * @param objectThrownOrDropped Derived variable to indicate how many times objects were thrown at or dropped on vehicles in the crash.
     * @param otherObject Derived variable to indicate how many times an object was struck in a crash and the object struck was not pre-defined. This variable includes stockpiled materials, rubbish bins, fallen poles, fallen trees, etc.
     * @param otherVehicleType Derived variable to indicate how many other vehicles (not included in any other category) were involved in the crash.
     * @param overBank Derived variable to indicate how many times an embankment was struck or driven over during a crash. This variable includes other vertical drops driven over during a crash.
     * @param parkedVehicle Derived variable to indicate how many times a parked or unattended vehicle was struck in the crash. This variable can include trailers.
     * @param phoneBoxEtc Derived variable to indicate how many times a telephone kiosk, traffic signal controllers, bus shelters, or other public furniture were struck in the crash.
     * @param pedestrian Derived variable to indicate how many pedestrians were involved in the crash. This includes pedestrians on skateboards, scooters, and wheelchairs.
     * @param postOrPole Derived variable to indicate how many times a post or pole was struck in the crash. This includes light, power, phone, utility poles, and objects practically forming part of a pole (i.e., 'Transformer Guy' wires).
     * @param roadCharacter The general nature of the road. Possible values include 'Bridge,' 'Motorway Ramp,' 'Rail crossing,' or 'Nil.'
     * @param roadLane The lane configuration of the road. Possible values: '1' (one way), '2' (two ways), 'M' (for where a median exists), 'O' (for off-road lane configurations), ' ' (for unknown or invalid configurations).
     * @param roadSurface The road surface description applying at the crash site. Possible values: 'Sealed' or 'Unsealed.'
     * @param roadworks Derived variable to indicate how many times an object associated with 'roadworks' (including signs, cones, drums, barriers, but not roadwork vehicles) was struck during the crash.
     * @param schoolBus Derived variable to indicate how many school buses were involved in the crash.
     * @param seriousInjuryCount A count of the number of serious injuries (inj) associated with this crash.
     * @param slipOrFlood Derived variable to indicate how many times landslips, washouts, or floods (excluding rivers) were objects struck in the crash.
     * @param speedLimit The speed (spd) limit (lim) in force at the crash site at the time of the crash. May be a number or 'LSZ' for a limited speed zone.
     * @param strayAnimal Derived variable to indicate how many times a stray animal(s) was struck in the crash. This variable includes wild animals such as pigs, goats, deer, straying farm animals, house pets, and birds.
     * @param streetLight The street lighting at the time of the crash. Possible values 'On,' 'Off,' 'None,' or 'Unknown.'
     * @param suv Derived variable to indicate how many SUVs were involved in the crash.
     * @param taxi Derived variable to indicate how many taxis were involved in the crash.
     * @param tlaName The name of the territorial local authority (TLA) the crash has been attributed.
     * @param temporarySpeedLimit The temporary (temp) speed (spd) limit (lim) at the crash site if one exists (e.g., for road works).
     * @param trafficControl The traffic control (ctrl) signals at the crash site. Possible values are 'Traffic Signals,' 'Stop Sign,' 'Give Way Sign,' 'Pointsman,' 'School Patrol,' 'Nil,' or 'N/A.'
     * @param trafficIsland Derived variable to indicate how many times a traffic island, medians (excluding barriers) was struck in the crash.
     * @param trafficSign Derived variable to indicate how many times 'traffic signage' (including traffic signals, their poles, bollards, or roadside delineators) was struck in the crash.
     * @param train Derived variable to indicate how many times a train, rolling stock, or jiggers was struck in the crash, whether stationary or moving.
     * @param tree Derived variable to indicate how many times trees or other growing items were struck during the crash.
     * @param truck Derived variable to indicate how many trucks were involved in the crash.
     * @param unknownVehicleType Derived variable to indicate how many vehicles were involved in the crash (where the vehicle type is unknown).
     * @param urban A derived variable using the 'spd_lim' variable. Possible values are 'Urban' (urban, spd_lim less than 80) or 'Open Road' (open road, spd_lim >= 80 or 'LSZ').
     * @param vanOrUtility Derived variable to indicate how many vans or utes were involved in the crash.
     * @param vehicle Derived variable to indicate how many times a stationary attended vehicle was struck in the crash. This includes broken down vehicles, workmen's vehicles, taxis, buses.
     * @param waterRiver Derived variable to indicate how many times a body of water (including rivers, streams, lakes, the sea, tidal flats, canals, watercourses, or swamps) was struck in the crash.
     * @param weatherA Indicates weather at the crash time/place. See wthr_b. Values that are possible are 'Fine,' 'Mist,' 'Light Rain,' 'Heavy Rain,' 'Snow,' 'Unknown.'
     * @param weatherB The weather at the crash time/place. See weather_a. Values 'Frost,' 'Strong Wind,' or 'Unknown.'
     * @param lat The latitude coordinate of the crash location.
     * @param lng The longitude coordinate of the crash location.
     */
    public Crash(
            int advisorySpeed,
            int bicycle,
            int bridge,
            int bus,
            int carStationWagon,
            int cliffBank,
            String location1,
            String location2,
            String severity,
            int year,
            int ditch,
            int fatalCount,
            int fence,
            String flatHill,
            int guardRail,
            String holiday,
            int houseOrBuilding,
            String intersection,
            int kerb,
            String light,
            int minorInjuryCount,
            int moped,
            int motorcycle,
            int numberOfLanes,
            int objectThrownOrDropped,
            int otherObject,
            int otherVehicleType,
            int overBank,
            int parkedVehicle,
            int phoneBoxEtc,
            int pedestrian,
            int postOrPole,
            String roadCharacter,
            String roadLane,
            String roadSurface,
            int roadworks,
            int schoolBus,
            int seriousInjuryCount,
            int slipOrFlood,
            int speedLimit,
            int strayAnimal,
            String streetLight,
            int suv,
            int taxi,
            String tlaName,
            int temporarySpeedLimit,
            String trafficControl,
            int trafficIsland,
            int trafficSign,
            int train,
            int tree,
            int truck,
            int unknownVehicleType,
            String urban,
            int vanOrUtility,
            int vehicle,
            int waterRiver,
            String weatherA,
            String weatherB,
            float lat,
            float lng
    ) {
        this.id = -1;
        this.advisorySpeed = advisorySpeed;
        this.bicycle = bicycle;
        this.bridge = bridge;
        this.bus = bus;
        this.carStationWagon = carStationWagon;
        this.cliffBank = cliffBank;
        this.location1 = location1;
        this.location2 = location2;
        this.severity = severity;
        this.year = year;
        this.ditch = ditch;
        this.fatalCount = fatalCount;
        this.fence = fence;
        this.flatHill = flatHill;
        this.guardRail = guardRail;
        this.holiday = holiday;
        this.houseOrBuilding = houseOrBuilding;
        this.intersection = intersection;
        this.kerb = kerb;
        this.light = light;
        this.minorInjuryCount = minorInjuryCount;
        this.moped = moped;
        this.motorcycle = motorcycle;
        this.numberOfLanes = numberOfLanes;
        this.objectThrownOrDropped = objectThrownOrDropped;
        this.otherObject = otherObject;
        this.otherVehicleType = otherVehicleType;
        this.overBank = overBank;
        this.parkedVehicle = parkedVehicle;
        this.phoneBoxEtc = phoneBoxEtc;
        this.pedestrian = pedestrian;
        this.postOrPole = postOrPole;
        this.roadCharacter = roadCharacter;
        this.roadLane = roadLane;
        this.roadSurface = roadSurface;
        this.roadworks = roadworks;
        this.schoolBus = schoolBus;
        this.seriousInjuryCount = seriousInjuryCount;
        this.slipOrFlood = slipOrFlood;
        this.speedLimit = speedLimit;
        this.strayAnimal = strayAnimal;
        this.streetLight = streetLight;
        this.suv = suv;
        this.taxi = taxi;
        this.tlaName = tlaName;
        this.temporarySpeedLimit = temporarySpeedLimit;
        this.trafficControl = trafficControl;
        this.trafficIsland = trafficIsland;
        this.trafficSign = trafficSign;
        this.train = train;
        this.tree = tree;
        this.truck = truck;
        this.unknownVehicleType = unknownVehicleType;
        this.urban = urban;
        this.vanOrUtility = vanOrUtility;
        this.vehicle = vehicle;
        this.waterRiver = waterRiver;
        this.weatherA = weatherA;
        this.weatherB = weatherB;
        this.lat = lat;
        this.lng = lng;
    }

    /**
     * Sets database id
     * @param id database id
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * Get the ID associated with this record.
     *
     * @return The ID of the record.
     */
    public int getId() {
        return id;
    }

    /**
     * Get the advisory speed for the location.
     *
     * @return The advisory speed.
     */
    public int getAdvisorySpeed() {
        return advisorySpeed;
    }

    /**
     * Get the number of bicycle-related incidents.
     *
     * @return The count of bicycle-related incidents.
     */
    public int getBicycle() {
        return bicycle;
    }

    /**
     * Get the number of incidents involving a bridge.
     *
     * @return The count of incidents involving a bridge.
     */
    public int getBridge() {
        return bridge;
    }

    /**
     * Get the number of incidents involving a bus.
     *
     * @return The count of incidents involving a bus.
     */
    public int getBus() {
        return bus;
    }

    /**
     * Get the number of car or station wagon-related incidents.
     *
     * @return The count of car or station wagon-related incidents.
     */
    public int getCarStationWagon() {
        return carStationWagon;
    }

    /**
     * Get the number of incidents involving a cliff or bank.
     *
     * @return The count of incidents involving a cliff or bank.
     */
    public int getCliffBank() {
        return cliffBank;
    }

    /**
     * Get the first location description.
     *
     * @return The first location description.
     */
    public String getLocation1() {
        return location1;
    }

    /**
     * Get the second location description.
     *
     * @return The second location description.
     */
    public String getLocation2() {
        return location2;
    }

    /**
     * Get the severity level of the incident.
     *
     * @return The severity level.
     */
    public String getSeverity() {
        return severity;
    }

    /**
     * Get the year of the incident.
     *
     * @return The year of the incident.
     */
    public int getYear() {
        return year;
    }

    /**
     * Get the number of incidents involving a ditch.
     *
     * @return The count of incidents involving a ditch.
     */
    public int getDitch() {
        return ditch;
    }

    /**
     * Get the number of fatal injuries in the incident.
     *
     * @return The count of fatal injuries.
     */
    public int getFatalCount() {
        return fatalCount;
    }

    /**
     * Get the number of incidents involving a fence.
     *
     * @return The count of incidents involving a fence.
     */
    public int getFence() {
        return fence;
    }

    /**
     * Get the information about the terrain (flat or hilly).
     *
     * @return The terrain information.
     */
    public String getFlatHill() {
        return flatHill;
    }

    /**
     * Get the number of incidents involving a guardrail.
     *
     * @return The count of incidents involving a guardrail.
     */
    public int getGuardRail() {
        return guardRail;
    }

    /**
     * Get the information about the holiday status at the time of the incident.
     *
     * @return The holiday information.
     */
    public String getHoliday() {
        return holiday;
    }

    /**
     * Get the number of incidents involving a house or building.
     *
     * @return The count of incidents involving a house or building.
     */
    public int getHouseOrBuilding() {
        return houseOrBuilding;
    }

    /**
     * Get information about the presence of an intersection.
     *
     * @return Information about the presence of an intersection.
     */
    public String getIntersection() {
        return intersection;
    }

    /**
     * Get the number of incidents involving a kerb.
     *
     * @return The count of incidents involving a kerb.
     */
    public int getKerb() {
        return kerb;
    }

    /**
     * Get the information about lighting at the location.
     *
     * @return Information about lighting at the location.
     */
    public String getLight() {
        return light;
    }

    /**
     * Get the number of minor injury incidents.
     *
     * @return The count of minor injury incidents.
     */
    public int getMinorInjuryCount() {
        return minorInjuryCount;
    }

    /**
     * Get the number of incidents involving a moped.
     *
     * @return The count of incidents involving a moped.
     */
    public int getMoped() {
        return moped;
    }

    /**
     * Get the number of incidents involving a motorcycle.
     *
     * @return The count of incidents involving a motorcycle.
     */
    public int getMotorcycle() {
        return motorcycle;
    }

    /**
     * Get the number of lanes on the road.
     *
     * @return The number of lanes on the road.
     */
    public int getNumberOfLanes() {
        return numberOfLanes;
    }

    /**
     * Get the number of incidents involving an object thrown or dropped.
     *
     * @return The count of incidents involving an object thrown or dropped.
     */
    public int getObjectThrownOrDropped() {
        return objectThrownOrDropped;
    }

    /**
     * Get the number of incidents involving another type of object.
     *
     * @return The count of incidents involving another type of object.
     */
    public int getOtherObject() {
        return otherObject;
    }

    /**
     * Get the number of incidents involving other types of vehicles.
     *
     * @return The count of incidents involving other types of vehicles.
     */
    public int getOtherVehicleType() {
        return otherVehicleType;
    }

    /**
     * Get the number of incidents involving going over a bank.
     *
     * @return The count of incidents involving going over a bank.
     */
    public int getOverBank() {
        return overBank;
    }

    /**
     * Get the number of incidents involving a parked vehicle.
     *
     * @return The count of incidents involving a parked vehicle.
     */
    public int getParkedVehicle() {
        return parkedVehicle;
    }

    /**
     * Get the number of incidents involving a phone box or similar structure.
     *
     * @return The count of incidents involving a phone box or similar structure.
     */
    public int getPhoneBoxEtc() {
        return phoneBoxEtc;
    }

    /**
     * Get the number of pedestrian-related incidents.
     *
     * @return The count of pedestrian-related incidents.
     */
    public int getPedestrian() {
        return pedestrian;
    }

    /**
     * Get the number of incidents involving a post or pole.
     *
     * @return The count of incidents involving a post or pole.
     */
    public int getPostOrPole() {
        return postOrPole;
    }

    /**
     * Get the description of the road character.
     *
     * @return The description of the road character.
     */
    public String getRoadCharacter() {
        return roadCharacter;
    }

    /**
     * Get the description of the road lane.
     *
     * @return The description of the road lane.
     */
    public String getRoadLane() {
        return roadLane;
    }

    /**
     * Get the description of the road surface.
     *
     * @return The description of the road surface.
     */
    public String getRoadSurface() {
        return roadSurface;
    }

    /**
     * Get the number of incidents involving roadworks.
     *
     * @return The count of incidents involving roadworks.
     */
    public int getRoadworks() {
        return roadworks;
    }

    /**
     * Get the number of school bus-related incidents.
     *
     * @return The count of school bus-related incidents.
     */
    public int getSchoolBus() {
        return schoolBus;
    }

    /**
     * Get the number of serious injury incidents.
     *
     * @return The count of serious injury incidents.
     */
    public int getSeriousInjuryCount() {
        return seriousInjuryCount;
    }

    /**
     * Get the number of incidents involving a slip or flood.
     *
     * @return The count of incidents involving a slip or flood.
     */
    public int getSlipOrFlood() {
        return slipOrFlood;
    }

    /**
     * Get the speed limit at the location.
     *
     * @return The speed limit at the location.
     */
    public int getSpeedLimit() {
        return speedLimit;
    }

    /**
     * Get the number of incidents involving stray animals.
     *
     * @return The count of incidents involving stray animals.
     */
    public int getStrayAnimal() {
        return strayAnimal;
    }

    /**
     * Get information about street lighting.
     *
     * @return Information about street lighting.
     */
    public String getStreetLight() {
        return streetLight;
    }

    /**
     * Get the number of incidents involving SUVs.
     *
     * @return The count of incidents involving SUVs.
     */
    public int getSuv() {
        return suv;
    }

    /**
     * Get the number of incidents involving taxis.
     *
     * @return The count of incidents involving taxis.
     */
    public int getTaxi() {
        return taxi;
    }

    /**
     * Get the TLA (Territorial Local Authority) name.
     *
     * @return The TLA name.
     */
    public String getTlaName() {
        return tlaName;
    }

    /**
     * Get the temporary speed limit at the location.
     *
     * @return The temporary speed limit at the location.
     */
    public int getTemporarySpeedLimit() {
        return temporarySpeedLimit;
    }

    /**
     * Get information about traffic control at the location.
     *
     * @return Information about traffic control at the location.
     */
    public String getTrafficControl() {
        return trafficControl;
    }

    /**
     * Get the number of incidents involving traffic islands.
     *
     * @return The count of incidents involving traffic islands.
     */
    public int getTrafficIsland() {
        return trafficIsland;
    }

    /**
     * Get the number of incidents involving traffic signs.
     *
     * @return The count of incidents involving traffic signs.
     */
    public int getTrafficSign() {
        return trafficSign;
    }

    /**
     * Get the number of incidents involving trains.
     *
     * @return The count of incidents involving trains.
     */
    public int getTrain() {
        return train;
    }

    /**
     * Get the number of incidents involving trees.
     *
     * @return The count of incidents involving trees.
     */
    public int getTree() {
        return tree;
    }

    /**
     * Get the number of incidents involving trucks.
     *
     * @return The count of incidents involving trucks.
     */
    public int getTruck() {
        return truck;
    }

    /**
     * Get the number of incidents involving unknown vehicle types.
     *
     * @return The count of incidents involving unknown vehicle types.
     */
    public int getUnknownVehicleType() {
        return unknownVehicleType;
    }

    /**
     * Get information about the urban or rural nature of the location.
     *
     * @return Information about whether the location is urban or rural.
     */
    public String getUrban() {
        return urban;
    }

    /**
     * Get the number of incidents involving vans or utility vehicles.
     *
     * @return The count of incidents involving vans or utility vehicles.
     */
    public int getVanOrUtility() {
        return vanOrUtility;
    }

    /**
     * Get the number of incidents involving vehicles.
     *
     * @return The count of incidents involving vehicles.
     */
    public int getVehicle() {
        return vehicle;
    }

    /**
     * Get the number of incidents involving water bodies or rivers.
     *
     * @return The count of incidents involving water bodies or rivers.
     */
    public int getWaterRiver() {
        return waterRiver;
    }

    /**
     * Get weather information (A) at the location.
     *
     * @return Weather information (A) at the location.
     */
    public String getWeatherA() {
        return weatherA;
    }

    /**
     * Get weather information (B) at the location.
     *
     * @return Weather information (B) at the location.
     */
    public String getWeatherB() {
        return weatherB;
    }

    /**
     * Get the latitude coordinate of the location.
     *
     * @return The latitude coordinate.
     */
    public float getLat() {
        return lat;
    }

    /**
     * Get the longitude coordinate of the location.
     *
     * @return The longitude coordinate.
     */
    public float getLng() {
        return lng;
    }

    /**
     * Indicates whether some other object is "equal to" this Crash instance.
     *
     * @param o The reference object with which to compare.
     * @return {@code true} if this Crash is the same as the object argument; {@code false} otherwise.
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Crash crash = (Crash) o;
        return id == crash.id && Float.compare(crash.lat, lat) == 0 && Float.compare(crash.lng, lng) == 0
                && Float.compare(crash.year, year) == 0 && Objects.equals(severity, crash.severity)
                && Objects.equals(location1, crash.location1) && Objects.equals(location2, crash.location2);
    }

    /**
     * Returns a hash code value for the Crash instance.
     *
     * @return A hash code value for this Crash instance, computed based on selected fields.
     */
    @Override
    public int hashCode() {
        return Objects.hash(id, lat, lng, year, severity, location1, location2);
    }

    /**
     * Returns a string that would be appropriate to be displayed on a crash marker.
     *
     * @return a string representation of the crash
     */
    public String toMarkerString() {
        return "Crash Here";
    }

    /**
     * Function to generate a numerical value (between 0 and 1) for crash severity. 1=most severe (fatal).
     *
     * @return the double value associated with the severity of the crash
     */
    public double getNumericSeverity() {
        String severityStr = getSeverity();
        if (Objects.equals(severityStr, "Non-Injury Crash")) {
            return 0.8;
        } else if (Objects.equals(severityStr, "Minor Crash")) {
            return 0.85;
        } else if (Objects.equals(severityStr, "Serious Crash")) {
            return 0.9;
        } else if (Objects.equals(severityStr, "Fatal Crash")) {
            return 0.95;
        }
        return -1;
    }

    /**
     * Returns 1 (true) if the crash involves multiple vehicles.
     *
     * @return 1 for true, 0 for false.
     */
    public int isMultipleVehicles() {
        return (getCarStationWagon() + getBus() + getMoped() + getMotorcycle() + getTaxi() + getTruck() + getUnknownVehicleType() + getVanOrUtility() > 1) ? 1:0;
    }

}

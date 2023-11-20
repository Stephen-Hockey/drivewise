package seng202.team7.business;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import seng202.team7.model.Crash;
import seng202.team7.model.Season;
import seng202.team7.model.WeatherCondition;
import seng202.team7.model.CrashParameter;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import static java.lang.Math.exp;

/**
 * Class that generates advice based on the area/route selected. Based on weather, season, and risk of crashes
 * @author Imogen Keeling
 */
public class AdviceLogic {
    /**
     * A list of lists containing advice items in string format.
     * This field stores advice items in a nested list structure. Each inner list represents a collection of advice items.
     */
    public List<List<String>> finalAdviceList = new ArrayList<>();
    private static final Logger log = LogManager.getLogger(AdviceLogic.class);
    private final List<Crash> crashes;
    private String currentDateTime;
    private Crash riskiestCrash = null;
    private double avgCrashRisk=0;
    private double maxRisk = 0;
    private final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    private int totalCrashes = 0;
    private int recentCrashesNum = 0;

    /**
     * AdviceLogic constructor class that calls all the appropriate functions when advice is requested
     * @param crashes the list of crashes given to the function
     */
    public AdviceLogic(List<Crash> crashes) {
        this.crashes = crashes;
        finalAdviceList.add(createCrashSummary());

        finalAdviceList.addAll(checkRiskiestCrashParameters());

        List<String> seasonPair = new ArrayList<>();
        seasonPair.add("Driving in " + seasonEnumSelect().getSeasonStr());
        seasonPair.add(seasonEnumSelect().getSeasonAdviceStr());
        seasonPair.add(seasonEnumSelect().getImageURL());
        finalAdviceList.add(seasonPair);

        if (!(weatherEnumSelect() == null)) {
            List<String> weatherPair = new ArrayList<>();
            weatherPair.add("Weather Advice");
            weatherPair.add(weatherEnumSelect().getAdvice());
            weatherPair.add("/img/weather_icon.png");
            finalAdviceList.add(weatherPair);
        }
    }

    /**
     * grabbing the current date and time from the system
     */
    public void getSysDate() {
        Date currentDate = new Date();
        currentDateTime = dateFormat.format(currentDate);
    }

    /**
     * extracting the month value from the current date and time given by getSysDate()
     * @param formattedDate the formatted date
     * @param dateFormat the format that the date is in to be processed
     * @return an int value associated with the month (i.e. january=1, february=2, etc.)
     */
    public int getMonthOfDate(String formattedDate, SimpleDateFormat dateFormat) {
        try {
            // Parse the formatted date string into a Date object
            Date date = dateFormat.parse(formattedDate);

            // Create a calendar instance and set the date
            java.util.Calendar calendar = java.util.Calendar.getInstance();
            calendar.setTime(date);

            // Extract the month value (January is 0, so we add 1)
            return calendar.get(java.util.Calendar.MONTH) + 1;
        } catch (ParseException e) {
            log.error("Error parsing the date: " + e.getMessage());
            return -1;  // Return a sentinel value or handle the error as needed
        }
    }

    /**
     * extracting the year value from the current date and time (given by setSysDate()
     * @return an int value of the current year
     */
    public int getYearOfDate() {
        try {
            getSysDate();
            // Parse the formatted date string into a Date object
            Date date = dateFormat.parse(currentDateTime);

            // Create a calendar instance and set the date
            java.util.Calendar calendar = java.util.Calendar.getInstance();
            calendar.setTime(date);

            // Extract the year value
            return calendar.get(java.util.Calendar.YEAR);
        } catch (ParseException e) {
            log.error("Error parsing the date: " + e.getMessage());
            return -1;  // Return a sentinel value or handle the error as needed
        }
    }

    /**
     * Selects the highest risk crash from a list of crashes using team 7's risk equation
     * @return the highest risk crash calculated by taking the crash with the highest risk value
     */
    public Crash getRiskiestCrash()
    {
        avgCrashRisk =0;
        for (Crash crash : crashes) {
            int isMultVehicles = crash.isMultipleVehicles();
            double severity = crash.getNumericSeverity();
            getSysDate();
            int recency = getYearOfDate() - crash.getYear();

            // Risk equation judging a crash based on its severity, recency, and if multiple moving vehicles were involved.
            double risk = exp(-(recency*(1-severity)-2.2))+isMultVehicles;
            avgCrashRisk += risk;
            if (risk >= maxRisk) {
                maxRisk = risk;
                riskiestCrash = crash;
            }
        }
        avgCrashRisk = avgCrashRisk/crashes.size();

        return riskiestCrash;
    }

    /**
     * checks whether the riskiest crash has specific traffic controls, is on an unsealed road, and has an advisory speed
     * @return list with advice associated with the parameters
     */
    public List<List<String>> checkRiskiestCrashParameters() {
        List<List<String>> parameterPairs = new ArrayList<>();
        if (!(riskiestCrash == null)) {
            getRiskiestCrash();
            if (!"Nil".equals(riskiestCrash.getTrafficControl()) && !"Unknown".equals(riskiestCrash.getTrafficControl())) {
                List<String> parameterAdviceList1 = new ArrayList<>();
                String trafficControlString = riskiestCrash.getTrafficControl();
                String advice1 = Objects.requireNonNull(CrashParameter.valueFromStr(trafficControlString)).getParameterAdvice();
                String imageURL = Objects.requireNonNull(CrashParameter.valueFromStr(trafficControlString)).getImageURL();
                parameterAdviceList1.add("Road Advice");
                parameterAdviceList1.add(advice1);
                parameterAdviceList1.add(imageURL);
                parameterPairs.add(parameterAdviceList1);
            }
            if ("Unsealed".equals(riskiestCrash.getRoadSurface())) {
                List<String> parameterAdviceList2 = new ArrayList<>();
                String advice2 = Objects.requireNonNull(CrashParameter.valueFromStr("Unsealed")).getParameterAdvice();
                String imageURL = Objects.requireNonNull(CrashParameter.valueFromStr("Unsealed")).getImageURL();
                parameterAdviceList2.add("Road Advice");
                parameterAdviceList2.add(advice2);
                parameterAdviceList2.add(imageURL);
                parameterPairs.add(parameterAdviceList2);
            }
            if (riskiestCrash.getAdvisorySpeed() > 0) { //If the advisory speed is recorded, it means they have most likely crashed on a corner with an advisory speed.
                List<String> parameterAdviceList3 = new ArrayList<>();
                String advice3 = "Make sure to meet the advisory speed when moving around turns in the road along your route. Corners can be tighter than they initially appear.";
                String imageURL = "/img/speed_icon.png";
                parameterAdviceList3.add("Speed Advice");
                parameterAdviceList3.add(advice3);
                parameterAdviceList3.add(imageURL);
                parameterPairs.add(parameterAdviceList3);
            }
        }
        return parameterPairs;
    }

    /**
     * filters the crash list down to only the crashes from the last five years
     * @return a list with the crashes from the last five years (from the given list)
     */
    public List<Crash> getMostRecentFiveYears() {
        List<Crash> recentFiveYears = new ArrayList<>();
        for (Crash crash : crashes) {
            if (getYearOfDate() - crash.getYear() <= 5) {
                recentFiveYears.add(crash);
            }
        }
        return recentFiveYears;
    }

    /**
     * Creates a summary about the route/area from the list of crashes given
     * @return List with the summary about the route/area
     */
    public List<String> createCrashSummary() {
        List<String> advicePair = new ArrayList<>();
        advicePair.add("Summary");

        if (!(crashes.isEmpty())) {
            getRiskiestCrash();
            setRecentCrashesNum();
            setTotalCrashes();

            String formattedAvgWholeRoute = String.format("%.0f", avgCrashRisk);
            String formattedMaxRiskCrash = String.format("%.0f", maxRisk);
            String advice = "There have been " + totalCrashes + " crashes in total, with " + recentCrashesNum + " of these crashes in the past five years. The average risk from this search is " + formattedAvgWholeRoute + "/10, with the highest risk crash being " + formattedMaxRiskCrash + "/10.";
            advicePair.add(advice);
        }
        else {
            String noCrashAdvice = "There have not been any crashes in the search that you have chosen!";
            advicePair.add(noCrashAdvice);
        }

        advicePair.add("/img/note-2-32.png");
        return advicePair;
    }

    /**
     * Function to select the highest occurring weather in the list of crashes provided to the AdviceLogic class
     * @return Enum from WeatherCondition of the highest weather occurrence.
     */
    public WeatherCondition weatherEnumSelect() {
        Map<String, Integer> weatherCount = new HashMap<>();
        for (Crash crash : crashes) {
            String weatherA = crash.getWeatherA();

            if (weatherCount.containsKey(weatherA)) {
                weatherCount.put(weatherA, weatherCount.get(weatherA) + 1);
            } else {
                weatherCount.put(weatherA,1);
            }

            String weatherB = crash.getWeatherB();

            if (weatherCount.containsKey(weatherB)) {
                weatherCount.put(weatherB, weatherCount.get(weatherB) + 1);
            } else {
                weatherCount.put(weatherB, 1);
            }
        }
        weatherCount.remove("Null");

        String keyWithMaxValue = "Unknown";
        int maxValue = 0;
        for (Map.Entry<String, Integer> entry : weatherCount.entrySet()) {
            if (entry.getValue() > maxValue) {
                maxValue = entry.getValue();
                keyWithMaxValue = entry.getKey();
            }
        }

        return WeatherCondition.valueFromWeatherStr(keyWithMaxValue);
    }

    /**
     * Selecting the appropriate season based on the computer system's date and time
     * @return the appropriate season enum based on the date
     */
    public Season seasonEnumSelect() {
        getSysDate();
        int month = getMonthOfDate(currentDateTime, dateFormat);

        return switch (month) {
            case 1, 2, 12 -> Season.SUMMER;
            case 3, 4, 5 -> Season.AUTUMN;
            case 6, 7, 8 -> Season.WINTER;
            case 9, 10, 11 -> Season.SPRING;
            default ->
                // returns the unknown enum if the month is an invalid number
                    Season.UNKNOWN;
        };
    }

    /**
     * setter function for the totalCrashes variable
     */
    public void setTotalCrashes() {
        totalCrashes = crashes.size();
    }

    /**
     * Setter function for the recentCrashesNum variable
     */
    public void setRecentCrashesNum() {
        recentCrashesNum = getMostRecentFiveYears().size();
    }
}

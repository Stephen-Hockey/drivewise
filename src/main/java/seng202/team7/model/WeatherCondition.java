package seng202.team7.model;

import java.util.Objects;

/**
 * Weather Enum with all the different categories of weather and the advice associated with them.
 * @author Imogen Keeling
 * @author Luke Edwards
 */
public enum WeatherCondition {
    /**
     * Represents fine weather conditions.
     */
    FINE("Fine", "Turn on the windscreen air-con/heater when first starting your car to prevent your windows from fogging up."),

    /**
     * Represents misty weather conditions.
     */
    MIST("Mist", "Slow down, and turn on your low-beam (not high-beam) headlights so that vehicles may see you from in front and behind without reducing your visibility."),

    /**
     * Represents light rain weather conditions.
     */
    LIGHT_RAIN("Light rain", "Take care around corners, the faster you go, the easier it is to lose traction on a slippery surface."),

    /**
     * Represents heavy rain weather conditions.
     */
    HEAVY_RAIN("Heavy rain", "Increase your following distance from four seconds to six seconds. Drive slower as the roads are more slippery."),

    /**
     * Represents snowy weather conditions.
     */
    SNOW("Snow", "Try and avoid driving if possible."),

    /**
     * Represents frosty weather conditions.
     */
    FROST("Frost", "Try and leave at a later (warmer) hour to avoid frost. Accelerate and brake gently to prevent losing traction."),

    /**
     * Represents strong wind conditions.
     */
    STRONG_WIND("Strong Wind", "Reduce your speed and take note of the direction of the wind. You or oncoming traffic could be blown into the other lane."),

    /**
     * Represents an unknown weather condition.
     */
    UNKNOWN("Unknown", "Maintain the appropriate following-distance and consistently check mirrors.");

    private final String weatherStr;
    private final String advice;

    /**
     * Constructor class for the weatherCondition enum
     * @param weatherStr The name of the weather
     * @param advice The relevant advice for the given weather
     */
    WeatherCondition(String weatherStr, String advice) {
        this.weatherStr = weatherStr;
        this.advice = advice;
    }

    /**
     * Static method that returns the WeatherCondition enum with the associated weather string
     * @param weather the weather string
     * @return the relevant WeatherCondition
     */
    public static WeatherCondition valueFromWeatherStr(String weather) {
        for (WeatherCondition type : WeatherCondition.values()) {
            if (Objects.equals(type.weatherStr, weather)) {
                return type;
            }
        }
        return null;
    }

    /**
     * Function to convert the weather to a string.
     *
     * @return The weather string.
     */
    @Override
    public String toString() {
        return weatherStr;
    }

    /**
     * Gets the advice.
     *
     * @return The advice string.
     */
    public String getAdvice() { return advice; }
}

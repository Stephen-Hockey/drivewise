package seng202.team7.model;

import java.util.Objects;

/**
 * Enum that stores multiples types road features and the relevant data needed to implement the advice into advice cards.
 *
 * @author Imogen Keeling
 */
public enum CrashParameter {
    /**
     * Represents an unsealed road condition.
     */
    UNSEALED("Unsealed", "It appears you may be driving along some unsealed road. Reduce your speed to maintain control of your vehicle while driving here.", "/img/unsealed_road_icon.png"),

    /**
     * Represents a give way sign condition.
     */
    GIVE_WAY("Give way", "Slow right down when approaching a give way sign and be ready to stop. Give way to all other vehicles, other than those at a stop sign.", "/img/give_way_icon.png"),

    /**
     * Represents a traffic signal condition.
     */
    TRAFFIC_LIGHT("Traffic Signals", "Remember, when a traffic light is red, you must stop. When the light is green, double check the intersection is safe to drive through before you enter it.", "/img/traffic_light_icon.png"),

    /**
     * Represents a stop sign condition.
     */
    STOP("Stop", "Remember to completely stop at stop signs and give way to all vehicles.", "/img/stop_icon.png");


    private final String parameterStr;
    private final String parameterAdvice;
    private final String imageURL;

    /**
     * Constructor for CrashParameter enum.
     * @param parameterStr Name of the road feature.
     * @param parameterAdvice Advice regarding the road feature.
     * @param imageURL URL of a useful image that relates to the road feature.
     */
    CrashParameter(String parameterStr, String parameterAdvice, String imageURL) {
        this.parameterStr = parameterStr;
        this.parameterAdvice = parameterAdvice;
        this.imageURL = imageURL;
    }
    /**
     * Get the parameter advice associated with this object.
     *
     * @return The parameter advice string.
     */
    public String getParameterAdvice() {
        return parameterAdvice;
    }

    /**
     * Get the image URL associated with this object.
     *
     * @return The image URL string.
     */
    public String getImageURL() {
        return imageURL;
    }

    /**
     * Function to return the CrashParameter enum with the associated parameter string
     * @param parameter the parameter string to get the crash parameter
     * @return the appropriate crash parameter
     */
    public static CrashParameter valueFromStr(String parameter) {
        for (CrashParameter category : CrashParameter.values()) {
            if (Objects.equals(category.parameterStr, parameter)) {
                return category;
            }
        }
        return null;
    }
}

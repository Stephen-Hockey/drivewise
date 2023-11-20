package seng202.team7.model;

/**
 * Season enum class attaching advice to each season
 * @author Imogen Keeling
 */
public enum Season {

    /**
     * Represents Autumn.
     */
    AUTUMN("Autumn","Be cautious of wet, fallen leaves. They can be extremely slippery, and can conceal important road markings.", "/img/leaf-3-32.png"),

    /**
     * Represents Winter.
     */
    WINTER("Winter", "Plan your journey within the warmer hours of the day to avoid hazards such as black ice.", "/img/snowflake-35-32.png"),

    /**
     * Represents Spring.
     */
    SPRING("Spring", "Be cautious of an increase in potholes. If you cannot avoid them, slow down and release the brake when you pass over to minimize impact.", "/img/bunch-flowers-32.png"),

    /**
     * Represents Summer.
     */
    SUMMER("Summer", "Due to summer holidays, there is often more traffic on the road in summer. Keep an eye on oncoming traffic and maintain safe following distances.", "/img/sun-3-32.png"),

    /**
     * Represents an Unknown season.
     */
    UNKNOWN("Unknown", "Always try to drive during daylight hours if driving on unfamiliar roads to decrease the risk of hitting unexpected obstacles.", "/img/starTransparent.png");
    private final String seasonStr;
    private final String seasonAdviceStr;
    private final String imageURL;

    /**
     * Constructor class for the weatherCondition enum
     * @param seasonStr The name of the season
     * @param seasonAdviceStr The relevant advice for the season.
     * @param imageURL The relevant imageURL.
     */
    Season (String seasonStr, String seasonAdviceStr, String imageURL) {
        this.seasonStr = seasonStr;
        this.seasonAdviceStr = seasonAdviceStr;
        this.imageURL = imageURL;
    }

    /**
     * Getter for the string value of Season
     * @return seasonStr
     */
    public String getSeasonStr() {
        return seasonStr;
    }

    /**
     * Getter for the advice provided with each season case
     * @return seasonAdviceStr
     */
    public String getSeasonAdviceStr() {
        return seasonAdviceStr;
    }

    /**
     * Getter for the imageURL
     * @return imageURL
     */
    public String getImageURL() { return imageURL; }

}

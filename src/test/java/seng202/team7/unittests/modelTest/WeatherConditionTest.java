package seng202.team7.unittests.modelTest;

import org.junit.Test;
import seng202.team7.model.WeatherCondition;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author Imogen Keeling
 * Test class for AdviceLogic
 */
public class WeatherConditionTest {
    /**
     * Tests the getAdvice function in the weatherCondition class to make sure that the appropriate advice for the weather is given
     */
    @Test
    public void checkWeatherAdvice() {
        WeatherCondition weatherCondition = WeatherCondition.valueFromWeatherStr("Heavy rain");
        assert weatherCondition != null;
        String adviceGiven = weatherCondition.getAdvice();
        assertEquals("Increase your following distance from four seconds to six seconds. Drive slower as the roads are more slippery.", adviceGiven);
    }

    @Test
    public void checkValueFromWeatherStrInvalid() {
        assertNull(WeatherCondition.valueFromWeatherStr("Invalid String"));
    }
}

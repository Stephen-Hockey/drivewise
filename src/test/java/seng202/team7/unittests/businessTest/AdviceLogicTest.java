package seng202.team7.unittests.businessTest;

import org.junit.jupiter.api.*;
import seng202.team7.model.Crash;
import seng202.team7.business.AdviceLogic;
import seng202.team7.model.WeatherCondition;
import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;


/**
 * @author Imogen Keeling
 * Test class for AdviceLogic
 */
public class AdviceLogicTest {
    private Crash crashInstance1;
    private Crash crashInstance2;
    private AdviceLogic adviceLogic;


    /**
     * Creating crash objects for the tests that need a crash object
     */
    @BeforeEach
    public void makeCrashList() {
        List<Crash> crashList = new ArrayList<>();
        crashInstance1 = new Crash(
                30,
                0,
                0,
                0,
                1,
                0,
                "SH 1N",
                "MANUKAU OFF SBD",
                "Fatal Crash",
                2022,
                0,
                0,
                0,
                "Flat",
                0,
                null,
                0,
                null,
                0,
                "Overcast",
                0,
                0,
                0,
                3,
                0,
                0,
                0,
                0,
                0,
                0,
                0,
                0,
                "Nil",
                "1-way",
                "Unsealed",
                0,
                0,
                0,
                0,
                100,
                0,
                "On",
                0,
                0,
                "Manawatu District",
                30,
                "Stop",
                0,
                1,
                0,
                0,
                0,
                0,
                "Open",
                1,
                0,
                0,
                "Heavy rain",
                null,
                (float)-40.2569761312752,
                (float)175.388698343564
        );

        crashInstance2 = new Crash(
                0,
                0,
                0,
                0,
                1,
                0,
                "SH 1N",
                "MANUKAU OFF SBD",
                "Non-Injury Crash",
                2001,
                0,
                0,
                0,
                "Flat",
                0,
                null,
                0,
                null,
                0,
                "Overcast",
                0,
                0,
                0,
                3,
                0,
                0,
                0,
                0,
                0,
                0,
                0,
                0,
                "Nil",
                "1-way",
                "Sealed",
                0,
                0,
                0,
                0,
                100,
                0,
                "On",
                0,
                0,
                "Manawatu District",
                30,
                "Nil",
                0,
                1,
                0,
                0,
                0,
                0,
                "Open",
                1,
                0,
                0,
                "Heavy rain",
                "Light rain",
                (float)-40.2569761312752,
                (float)175.388698343564
        );
        crashList.add(crashInstance1);
        crashList.add(crashInstance2);

        adviceLogic = new AdviceLogic(crashList);
    }

    /**
     * tests the weatherEnumSelect function in the AdviceLogic class
     */
    @Test
    public void testWeatherAdviceOutput() {
        assertEquals(WeatherCondition.HEAVY_RAIN, adviceLogic.weatherEnumSelect());
    }

    /**
     * Tests getHighestRiskCrash() with the small crashList created in this AdviceLogicTest class
     */
    @Test
    public void testGetHighestRiskCrash() {
        assertEquals(crashInstance1, adviceLogic.getRiskiestCrash());
    }

    /**
     * Tests the getHighestRiskCrash() when there is less than 2 crashes in the given list
     */
    @Test
    public void testGetHighestRiskCrashSmallRoute() {
        List<Crash> smallRouteList = new ArrayList<>();
        smallRouteList.add(crashInstance1);
        AdviceLogic smallRoute = new AdviceLogic(smallRouteList);
        assertEquals(crashInstance1, smallRoute.getRiskiestCrash());
    }

    /**
     * Tests the getHighestRiskCrash() when there is no crashes in the given list
     */
    @Test
    public void testGetHighestRiskCrashNoCrashes() {
        List <Crash> noCrashRoute = new ArrayList<>();
        AdviceLogic noCrashRouteLogic = new AdviceLogic(noCrashRoute);
        assertNull(noCrashRouteLogic.getRiskiestCrash());
    }

    /**
     * Testing checkRiskiestCrashParameters() on crashInstance1 where all three parameters are mentioned.
     */
    @Test
    public void testParameterAdvice() {
        List<String> innerList1 = new ArrayList<>();
        List<String> innerList2 = new ArrayList<>();
        List<String> innerList3 = new ArrayList<>();
        List<List<String>> expectedOutcome = new ArrayList<>();

        innerList1.add("Road Advice");
        innerList1.add("Remember to completely stop at stop signs and give way to all vehicles.");
        innerList1.add("/img/stop_icon.png");
        expectedOutcome.add(innerList1);

        innerList2.add("Road Advice");
        innerList2.add("It appears you may be driving along some unsealed road. Reduce your speed to maintain control of your vehicle while driving here.");
        innerList2.add("/img/unsealed_road_icon.png");
        expectedOutcome.add(innerList2);

        innerList3.add("Speed Advice");
        innerList3.add("Make sure to meet the advisory speed when moving around turns in the road along your route. Corners can be tighter than they initially appear.");
        innerList3.add("/img/speed_icon.png");
        expectedOutcome.add(innerList3);

        assertEquals(expectedOutcome, adviceLogic.checkRiskiestCrashParameters());
    }

    /**
     * Testing checkRiskiestCrashParameters() on crashInstance2 where none of the parameters are mentioned
     */
    @Test
    public void testParameterAdviceZeroParameters() {
        List<String> expectedOutcome = new ArrayList<>();
        List<Crash> crashWithNoParameters = new ArrayList<>();
        crashWithNoParameters.add(crashInstance2);
        AdviceLogic zeroParameters = new AdviceLogic(crashWithNoParameters);
        Assertions.assertEquals(expectedOutcome, zeroParameters.checkRiskiestCrashParameters());
    }

    /**
     * Tests createCrashSummary() to check if it grabs and calculates the correct numbers (which are simplified for the user)
     */
    @Test
    public void testCreateCrashSummary() {
        List<String> expectedOutcome = new ArrayList<>();
        expectedOutcome.add("Summary");
        expectedOutcome.add("There have been 2 crashes in total, with 1 of these crashes in the past five years. The average risk from this search is 5/10, with the highest risk crash being 10/10.");
        expectedOutcome.add("/img/note-2-32.png");
        assertEquals(expectedOutcome, adviceLogic.createCrashSummary());
    }
}

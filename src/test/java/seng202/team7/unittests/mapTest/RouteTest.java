package seng202.team7.unittests.mapTest;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import seng202.team7.map.Position;
import seng202.team7.map.Route;
import org.junit.jupiter.api.Assertions;

import java.util.List;

/**
 * @author Joseph Hendry
 * @author Luke Edwards
 * @author Steven Hockey
 * Test class for AdviceLogic
 */
public class RouteTest {
    private Route route;
    @BeforeEach
    public void init() {route = new Route();}

    @Test
    public void testAppend() {
        Position position = new Position(42.0, -73.5);
        route.append(position);
        List<Position> routeList = route.getRoute();
        Assertions.assertEquals(1, routeList.size());
        Assertions.assertEquals(position, routeList.get(0));
    }

    @Test
    public void testClear() {
        route.append(new Position(42.0, -73.5));
        route.append(new Position(41.5, -74.0));
        route.clear();
        List<Position> routeList = route.getRoute();
        Assertions.assertEquals(0, routeList.size());
    }

    @Test
    public void toJSONArrayEmptyTest() {
        Route route = new Route();

        Assertions.assertEquals("[]", route.toJSONArray());

    }
    @Test
    public void toJSONArraySingleTest() {
        Position pos1 = new Position(0, 0);

        Route route = new Route(pos1);
        String JSONstring = "[{\"lat\": 0.000000, \"lng\": 0.000000}]";
        Assertions.assertEquals(JSONstring, route.toJSONArray());
    }

    @Test
    public void toJSONArrayMultipleTest() {
        Position pos1 = new Position(0, 0);
        Position pos2 = new Position(1, 1);
        Position pos3 = new Position(2.5, 3.66);

        Route route = new Route(pos1, pos2, pos3);
        String JSONstring = "[{\"lat\": 0.000000, \"lng\": 0.000000}, {\"lat\": 1.000000, \"lng\": 1.000000}, {\"lat\": 2.500000, \"lng\": 3.660000}]";
        Assertions.assertEquals(JSONstring, route.toJSONArray());
    }
}

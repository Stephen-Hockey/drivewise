package seng202.team7.map;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Simple class representing a route as any number of positions
 * @author Morgan English
 */
public class Route {
    private final List<Position> route = new ArrayList<>();

    /**
     * Route constructor with any number of positions
     * @param points points along the route in order first to last
     */
    public Route(Position ...points) {
        Collections.addAll(route, points);
    }

    /**
     * Getter for thr route.
     *
     * @return The route.
     */
    public List<Position> getRoute() {
        return route;
    }

    /**
     * Adds a point to the route.
     *
     * @param point The point to add.
     */
    public void append(Position point) {
        route.add(point);
    }

    /**
     * Clears the route.
     */
    public void clear() {
        route.clear();
    }

    /**
     * Returns the route as a JSON array string
     *
     * @return route object as JSON array string
     */
    public String toJSONArray() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("[");
        route.forEach(pos -> stringBuilder.append(
                String.format("{\"lat\": %f, \"lng\": %f}, ", pos.lat, pos.lng)));
        if (stringBuilder.length() > 2) {
            stringBuilder.setLength(stringBuilder.length() - 2);
        }
        stringBuilder.append("]");
        return stringBuilder.toString();
    }
}

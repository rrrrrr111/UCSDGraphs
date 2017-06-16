package roadgraph;

import geography.GeographicPoint;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

import static java.util.Collections.EMPTY_SET;

/**
 * Implementation of roads intersection (vertices in MapGraph)
 */
class Crossroad extends GeographicPoint {

    private Set<Road> inRoads = EMPTY_SET;
    private Set<Road> outRoads = EMPTY_SET;

    Crossroad(double latitude, double longitude) {
        super(latitude, longitude);
    }

    Crossroad(GeographicPoint location) {
        this(location.getX(), location.getY());
    }

    boolean hasLoopBackRoad() {
        for (Road inRoad : inRoads) {
            if (inRoad.isLoopBackRoad()) {
                return true;
            }
        }
        return false;
    }

    Set<Crossroad> getNeighbours() {
        return outRoads.stream()
                .map(Road::getToCrossroad)
                .collect(Collectors.toSet());
    }

    Set<Road> getInRoads() {
        return inRoads;
    }

    Set<Road> getOutRoads() {
        return outRoads;
    }

    void addOutRoad(Road road) {
        if (outRoads == EMPTY_SET) {
            outRoads = new HashSet<>();
        }
        outRoads.add(road);
    }

    void addInRoad(Road road) {
        if (inRoads == EMPTY_SET) {
            inRoads = new HashSet<>();
        }
        inRoads.add(road);
    }

    String asTextPoint() {
        return String.format("[%s,%s]", x, y);
    }

    String getNeighboursCommaSeparated() {
        StringBuilder sb = new StringBuilder();
        for (Road road : outRoads) {
            sb.append(road.getToCrossroad().asTextPoint()).append(", ");
        }
        if (sb.length() == 0) {
            return "";
        }
        return sb.delete(sb.length() - 2, sb.length()).toString();
    }

    @Override
    public String toString() {
        return "Lat: " + getX() + ", Lon: " + getY();
    }
}

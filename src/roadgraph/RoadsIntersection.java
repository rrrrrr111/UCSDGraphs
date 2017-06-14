package roadgraph;

import geography.GeographicPoint;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

import static java.util.Collections.EMPTY_SET;

/**
 * Implementation of roads intersection (vertices in MapGraph)
 */
class RoadsIntersection extends GeographicPoint {

    private Set<Road> inRoads = EMPTY_SET;
    private Set<Road> outRoads = EMPTY_SET;

    RoadsIntersection(double latitude, double longitude) {
        super(latitude, longitude);
    }

    RoadsIntersection(GeographicPoint location) {
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

    Set<RoadsIntersection> getNeighbours() {
        return outRoads.stream()
                .map(Road::getToIntersection)
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

    @Override
    public String toString() {
        return "RoadsIntersection{" +
                "x=" + x +
                ", y=" + y +
                ", inRoads=" + inRoads +
                ", outRoads=" + outRoads +
                '}';
    }
}

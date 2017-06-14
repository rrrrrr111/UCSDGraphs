package roadgraph;

import java.util.Objects;

/**
 * Implementation of Road (Edge in MapGraph)
 */
class Road {

    private final String roadName;
    private final String roadType;
    private final double length;

    private RoadsIntersection fromIntersection;
    private RoadsIntersection toIntersection;

    Road(String roadName, String roadType, double length) {
        this.roadName = roadName;
        this.roadType = roadType;
        this.length = length;
    }

    boolean isLoopBackRoad() {
        return fromIntersection.equals(toIntersection);
    }

    String getRoadName() {
        return roadName;
    }

    String getRoadType() {
        return roadType;
    }

    double getLength() {
        return length;
    }

    RoadsIntersection getFromIntersection() {
        return fromIntersection;
    }

    void setFromIntersection(RoadsIntersection fromIntersection) {
        this.fromIntersection = fromIntersection;
    }

    RoadsIntersection getToIntersection() {
        return toIntersection;
    }

    void setToIntersection(RoadsIntersection toIntersection) {
        this.toIntersection = toIntersection;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Road road = (Road) o;
        return Double.compare(road.length, length) == 0 &&
                Objects.equals(roadName, road.roadName) &&
                Objects.equals(roadType, road.roadType) &&
                Objects.equals(fromIntersection, road.fromIntersection) &&
                Objects.equals(toIntersection, road.toIntersection);
    }

    @Override
    public int hashCode() {
        return Objects.hash(roadName, roadType, length, fromIntersection, toIntersection);
    }

    @Override
    public String toString() {
        return "Road{" +
                "roadName='" + roadName + '\'' +
                ", roadType='" + roadType + '\'' +
                ", length=" + length +
                ", fromIntersection=" + fromIntersection.asTextPoint() +
                ", toIntersection=" + toIntersection.asTextPoint() +
                '}';
    }
}

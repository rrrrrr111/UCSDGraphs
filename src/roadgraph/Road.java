package roadgraph;

import java.util.Objects;

/**
 * Implementation of Road (Edge in MapGraph)
 */
class Road {

    /**
     * Road name
     */
    private final String roadName;
    /**
     * Road type
     */
    private final String roadType;
    /**
     * Road weight
     */
    private final double length;

    /**
     * Starting crossroad of the Road
     */
    private Crossroad fromCrossroad;
    /**
     * Ending crossroad of the Road
     */
    private Crossroad toCrossroad;

    Road(String roadName, String roadType, double length) {
        this.roadName = roadName;
        this.roadType = roadType;
        this.length = length;
    }

    boolean isLoopBackRoad() {
        return fromCrossroad.equals(toCrossroad);
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

    Crossroad getFromCrossroad() {
        return fromCrossroad;
    }

    void setFromCrossroad(Crossroad fromCrossroad) {
        this.fromCrossroad = fromCrossroad;
    }

    Crossroad getToCrossroad() {
        return toCrossroad;
    }

    void setToCrossroad(Crossroad toCrossroad) {
        this.toCrossroad = toCrossroad;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Road road = (Road) o;
        return Double.compare(road.length, length) == 0 &&
                Objects.equals(roadName, road.roadName) &&
                Objects.equals(roadType, road.roadType) &&
                Objects.equals(fromCrossroad, road.fromCrossroad) &&
                Objects.equals(toCrossroad, road.toCrossroad);
    }

    @Override
    public int hashCode() {
        return Objects.hash(roadName, roadType, length, fromCrossroad, toCrossroad);
    }

    @Override
    public String toString() {
        return "Road{" +
                "roadName='" + roadName + '\'' +
                ", roadType='" + roadType + '\'' +
                ", length=" + length +
                ", fromCrossroad=" + fromCrossroad.asTextPoint() +
                ", toCrossroad=" + toCrossroad.asTextPoint() +
                '}';
    }
}

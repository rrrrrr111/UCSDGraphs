/**
 * @author UCSD MOOC development team and YOU
 * <p>
 * A class which reprsents a graph of geographic locations
 * Nodes in the graph are crossroads between
 */
package roadgraph;


import geography.GeographicPoint;
import util.GraphLoader;

import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Consumer;

import static java.lang.Double.compare;

/**
 * @author UCSD MOOC development team and YOU
 *         <p>
 *         A class which represents a graph of geographic locations
 *         Nodes in the graph are crossroads between Roads
 */
public class MapGraph {

    private Map<GeographicPoint, Crossroad> crossroads = new HashMap<>();

    /**
     * Create a new empty MapGraph
     */
    public MapGraph() {
    }

    /**
     * Get the number of vertices (road crossroads) in the graph
     *
     * @return The number of vertices in the graph.
     */
    public int getNumVertices() {
        return crossroads.size();
    }

    /**
     * Return the crossroads, which are the vertices in this graph.
     *
     * @return The vertices in this graph as GeographicPoints
     */
    public Set<GeographicPoint> getVertices() {
        return new HashSet<>(crossroads.values());
    }

    /**
     * Get the number of road segments in the graph
     *
     * @return The number of edges in the graph.
     */
    public int getNumEdges() {
        int edgesNum = 0;
        for (Crossroad crossroad : crossroads.values()) {

            edgesNum += crossroad.getOutRoads().size();
            if (crossroad.hasLoopBackRoad()) {
                edgesNum++;
            }
        }
        return edgesNum;
    }

    /**
     * Add a node corresponding to an intersection at a Geographic Point
     * If the location is already in the graph or null, this method does
     * not change the graph.
     *
     * @param location The location of the intersection
     * @return true if a node was added, false if it was not (the node
     * was already in the graph, or the parameter is null).
     */
    public boolean addVertex(GeographicPoint location) {
        if (location == null || crossroads.containsKey(location)) {
            return false;
        }
        crossroads.put(location, new Crossroad(location));
        return true;
    }

    /**
     * Adds a directed edge to the graph from pt1 to pt2.
     * Precondition: Both GeographicPoints have already been added to the graph
     *
     * @param from     The starting point of the edge
     * @param to       The ending point of the edge
     * @param roadName The name of the road
     * @param roadType The type of the road
     * @param length   The length of the road, in km
     * @throws IllegalArgumentException If the points have not already been
     *                                  added as nodes to the graph, if any of the arguments is null,
     *                                  or if the length is less than 0.
     */
    public void addEdge(GeographicPoint from, GeographicPoint to, String roadName,
                        String roadType, double length) throws IllegalArgumentException {

        Road road = new Road(roadName, roadType, length);

        Crossroad fromCrossroad = find(from);
        Crossroad toCrossroad = find(to);

        toCrossroad.addInRoad(road);
        road.setToCrossroad(toCrossroad);

        fromCrossroad.addOutRoad(road);
        road.setFromCrossroad(fromCrossroad);
    }

    private Crossroad find(GeographicPoint point) {
        Crossroad crossroad = crossroads.get(point);
        if (crossroad == null) {
            throw new IllegalStateException("Crossroad in point: " + point + " not found");
        }
        return crossroad;

    }

    /**
     * Find the path from start to goal using breadth first search
     *
     * @param start The starting location
     * @param goal  The goal location
     * @return The list of crossroads that form the shortest (unweighted)
     * path from start to goal (including both start and goal).
     */
    public List<GeographicPoint> bfs(GeographicPoint start, GeographicPoint goal) {
        // Dummy variable for calling the search algorithms
        Consumer<GeographicPoint> temp = (x) -> {
        };
        return bfs(start, goal, temp);
    }

    /**
     * Find the path from start to goal using breadth first search
     *
     * @param start      The starting location
     * @param goal       The goal location
     * @param visualiser A hook for visualization.  See assignment instructions for how to use it.
     * @return The list of crossroads that form the shortest (unweighted)
     * path from start to goal (including both start and goal).
     */
    public List<GeographicPoint> bfs(GeographicPoint start,
                                     GeographicPoint goal, Consumer<GeographicPoint> visualiser) {

        Map<Crossroad, Crossroad> pathLinks = new HashMap<>();
        Queue<Crossroad> queue = new LinkedList<>();
        Crossroad current = find(start);
        Crossroad goalCrossroad = find(goal);

        while (true) {
            visualiser.accept(current);
            if (current.equals(goalCrossroad)) {
                break;
            }

            Set<Crossroad> neighbours = current.getNeighbours();
            neighbours.removeAll(pathLinks.values());

            for (Crossroad neighbour : neighbours) {
                pathLinks.put(neighbour, current);
            }

            queue.addAll(neighbours);
            Crossroad next = queue.poll();

            if (next == null) {
                return null;
            }

            current = next;
        }
        return buildPath(start, goal, pathLinks);
    }

    private List<GeographicPoint> buildPath(GeographicPoint fromPoint, GeographicPoint toPoint,
                                            Map<? extends Crossroad, ? extends Crossroad> pathLinks) {
        LinkedList<GeographicPoint> path = new LinkedList<>();
        path.addFirst(find(toPoint));

        while (true) {
            if (fromPoint.equals(toPoint)) {
                return path;
            }
            Crossroad next = pathLinks.get(toPoint);
            path.addFirst(next);
            toPoint = next;
        }
    }

    /**
     * Find the path from start to goal using Dijkstra's algorithm
     *
     * @param start The starting location
     * @param goal  The goal location
     * @return The list of crossroads that form the shortest path from
     * start to goal (including both start and goal).
     */
    public List<GeographicPoint> dijkstra(GeographicPoint start, GeographicPoint goal) {
        // Dummy variable for calling the search algorithms
        // You do not need to change this method.
        AtomicInteger counter = new AtomicInteger();
        Consumer<GeographicPoint> temp = (x) -> {

            //System.out.println("DIJKSTRA visiting [" + x + "] intersects streets: " + ((Crossroad) x).getOutRoads());
            counter.incrementAndGet();
        };
        List<GeographicPoint> path = dijkstra(start, goal, temp);
        System.out.println("DIJKSTRA " + counter.get() + " nodes visited");
        return path;
    }

    /**
     * Find the path from start to goal using Dijkstra's algorithm
     *
     * @param start      The starting location
     * @param goal       The goal location
     * @param visualiser A hook for visualization.  See assignment instructions for how to use it.
     * @return The list of crossroads that form the shortest path from
     * start to goal (including both start and goal).
     */
    public List<GeographicPoint> dijkstra(GeographicPoint start,
                                          GeographicPoint goal, Consumer<GeographicPoint> visualiser) {
        Map<PriorityCrossroad, PriorityCrossroad> fromTo = new HashMap<>();
        PriorityQueue<PriorityCrossroad> queue = new PriorityQueue<>();

        PriorityCrossroad current = new PriorityCrossroad(find(start), 0);
        Crossroad goalCrossroad = find(goal);

        queue.add(current);

        for (; ; ) {
            current = queue.poll();
            visualiser.accept(current);

            if (current.equals(goalCrossroad)) {
                break;
            }

            double distanceFromStart = current.getDistance();
            for (Road outRoad : current.getOutRoads()) {
                PriorityCrossroad neighbour = new PriorityCrossroad(
                        outRoad.getToCrossroad(), distanceFromStart + outRoad.getLength()
                );

                checkNeighbour(start, fromTo, queue, current, neighbour);
            }
        }
        return buildPath(start, goal, fromTo);
    }

    private void checkNeighbour(GeographicPoint start, Map<PriorityCrossroad, PriorityCrossroad> fromTo,
                                PriorityQueue<PriorityCrossroad> queue,
                                PriorityCrossroad current,
                                PriorityCrossroad neighbour) {
        if (!fromTo.containsKey(neighbour)
                && !start.equals(neighbour)) {
            fromTo.put(neighbour, current);
            queue.add(neighbour);

        } else {
            for (Map.Entry<PriorityCrossroad, PriorityCrossroad> entry : fromTo.entrySet()) {

                PriorityCrossroad previousNeighbour = entry.getKey();

                if (previousNeighbour.equals(neighbour)
                        && neighbour.hasHigherPriorityThan(previousNeighbour)
                        && !start.equals(neighbour)) {

                    fromTo.put(neighbour, current);
                    queue.add(neighbour);
                    break;
                }
            }
        }
    }

    /**
     * Find the path from start to goal using A-Star search
     *
     * @param start The starting location
     * @param goal  The goal location
     * @return The list of crossroads that form the shortest path from
     * start to goal (including both start and goal).
     */
    public List<GeographicPoint> aStarSearch(GeographicPoint start, GeographicPoint goal) {
        // Dummy variable for calling the search algorithms
        AtomicInteger counter = new AtomicInteger();
        Consumer<GeographicPoint> temp = (x) -> {

            //System.out.println("A* visiting [" + x + "] intersects streets: " + ((Crossroad) x).getOutRoads());
            counter.incrementAndGet();
        };

        List<GeographicPoint> path = aStarSearch(start, goal, temp);
        System.out.println("A* " + counter.get() + " nodes visited");
        return path;
    }

    /**
     * Find the path from start to goal using A-Star search
     *
     * @param start      The starting location
     * @param goal       The goal location
     * @param visualiser A hook for visualization.  See assignment instructions for how to use it.
     * @return The list of crossroads that form the shortest path from
     * start to goal (including both start and goal).
     */
    public List<GeographicPoint> aStarSearch(GeographicPoint start,
                                             GeographicPoint goal, Consumer<GeographicPoint> visualiser) {
        Map<PriorityCrossroad, PriorityCrossroad> fromTo = new HashMap<>();
        PriorityQueue<PriorityCrossroad> queue = new PriorityQueue<>();

        PriorityCrossroad current = new PriorityCrossroad(find(start), 0);
        Crossroad goalCrossroad = find(goal);

        queue.add(current);

        for (; ; ) {
            current = queue.poll();
            visualiser.accept(current);

            if (current.equals(goalCrossroad)) {
                break;
            }

            double distanceFromStart = current.getDistance();
            for (Road outRoad : current.getOutRoads()) {
                PriorityCrossroad neighbour = new PriorityCrossroad(
                        outRoad.getToCrossroad(), calculateLengthBetween(outRoad.getToCrossroad(), goalCrossroad)
                );

                checkNeighbour(start, fromTo, queue, current, neighbour);
            }
        }
        return buildPath(start, goal, fromTo);
    }

    private static double calculateLengthBetween(Crossroad fromCrossroad, Crossroad toCrossroad) {
        return Math.abs(fromCrossroad.getX() - toCrossroad.getX())
                + Math.abs(fromCrossroad.getY() - toCrossroad.getY());
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("MapGraph[\n");
        for (Crossroad crossroad : crossroads.values()) {
            sb.append('\t')
                    .append(crossroad.asTextPoint())
                    .append(" -> [")
                    .append(crossroad.getNeighboursCommaSeparated())
                    .append("]\n");
        }
        return sb.append("]\n").toString();
    }

    private static class PriorityCrossroad extends Crossroad implements Comparable<PriorityCrossroad> {
        private final double distance;

        PriorityCrossroad(Crossroad crossroad, double distance) {
            super(crossroad);
            this.distance = distance;
        }

        private double getDistance() {
            return distance;
        }

        private boolean hasHigherPriorityThan(PriorityCrossroad crossroad) {
            return compareTo(crossroad) < 0;
        }

        @Override
        public int compareTo(PriorityCrossroad o) {
            return compare(distance, o.distance);
        }
    }

    public static void main(String[] args) {
        System.out.print("Making a new map...");
        MapGraph firstMap = new MapGraph();
        System.out.print("DONE. \nLoading the map...");
        GraphLoader.loadRoadMap("data/testdata/simpletest.map", firstMap);
        System.out.println("DONE.");

        // You can use this method for testing.


		/* Here are some test cases you should try before you attempt
         * the Week 3 End of Week Quiz, EVEN IF you score 100% on the
		 * programming assignment.
		 */

        MapGraph simpleTestMap = new MapGraph();
        GraphLoader.loadRoadMap("data/testdata/simpletest.map", simpleTestMap);

        GeographicPoint testStart = new GeographicPoint(1.0, 1.0);
        GeographicPoint testEnd = new GeographicPoint(8.0, -1.0);

        List<GeographicPoint> result;
        System.out.println("Test 1 using simpletest: Dijkstra should be 9 and AStar should be 5");
        result = simpleTestMap.dijkstra(testStart, testEnd);
        result = simpleTestMap.aStarSearch(testStart, testEnd);

        MapGraph testMap = new MapGraph();
        GraphLoader.loadRoadMap("data/maps/utc.map", testMap);

        // A very simple test using real data
        testStart = new GeographicPoint(32.869423, -117.220917);
        testEnd = new GeographicPoint(32.869255, -117.216927);
        System.out.println("Test 2 using utc: Dijkstra should be 13 and AStar should be 5");
        result = testMap.dijkstra(testStart, testEnd);
        result = testMap.aStarSearch(testStart, testEnd);

        // A slightly more complex test using real data
        testStart = new GeographicPoint(32.8674388, -117.2190213);
        testEnd = new GeographicPoint(32.8697828, -117.2244506);
        System.out.println("Test 3 using utc: Dijkstra should be 37 and AStar should be 10");
        result = testMap.dijkstra(testStart, testEnd);
        result = testMap.aStarSearch(testStart, testEnd);

		/* Use this code in Week 3 End of Week Quiz */
        MapGraph theMap = new MapGraph();
        System.out.print("DONE. \nLoading the map...");
        GraphLoader.loadRoadMap("data/maps/utc.map", theMap);
        System.out.println("DONE.");

        GeographicPoint start = new GeographicPoint(32.8648772, -117.2254046);
        GeographicPoint end = new GeographicPoint(32.8660691, -117.217393);

        List<GeographicPoint> route = theMap.dijkstra(start, end);
        List<GeographicPoint> route2 = theMap.aStarSearch(start, end);
    }
}

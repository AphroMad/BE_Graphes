package org.insa.graphs.algorithm.utils;

import org.insa.graphs.algorithm.ArcInspector;
import org.insa.graphs.algorithm.ArcInspectorFactory;
import org.insa.graphs.algorithm.shortestpath.BellmanFordAlgorithm;
import org.insa.graphs.algorithm.shortestpath.ShortestPathData;
import org.insa.graphs.algorithm.shortestpath.ShortestPathSolution;
import org.insa.graphs.model.Graph;
import org.insa.graphs.model.Node;
import org.insa.graphs.model.io.BinaryGraphReader;
import org.insa.graphs.model.io.GraphReader;
import org.junit.Before;
import org.junit.Test;

import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;

import static org.junit.Assert.*;

public abstract class AlgoTest {

    private static ArcInspector filterLength, filterTime;
    protected ArrayList<PathDataset> validTestData;
    protected ArrayList<PathDataset> invalidTestData;

    // Graphs to use
    private Graph graphInsa, graphSquare, graphBretagne, graphGuadeloupe;

    /**
     * Class used to store all data related to a path and its computed solutions
     */
    class PathDataset {

        ShortestPathData pathDataTime;
        ShortestPathData pathDataLength;

        ShortestPathSolution solutionInTime;
        ShortestPathSolution solutionInLength;

        ShortestPathSolution optimalSolutionInTime;
        ShortestPathSolution optimalSolutionInLength;


        PathDataset(Graph graph, Node origin, Node destination) {
            this.pathDataTime = new ShortestPathData(graph, origin, destination, filterTime);
            this.pathDataLength = new ShortestPathData(graph, origin, destination, filterLength);
        }

        public ShortestPathData getPathDataTime() {
            return pathDataTime;
        }

        public ShortestPathData getPathDataLength() {
            return pathDataLength;
        }

        /**
         * Gets all computed solutions with different filters
         * @return An array of computed solutions
         */
        public ArrayList<ShortestPathSolution> getComputedSolutions() {
            return new ArrayList<ShortestPathSolution>() {{
                add(solutionInTime);
                add(solutionInLength);
            }};
        }

        /**
         * Gets all optimal solutions with different filters
         * @return An array of optimal solutions
         */
        public ArrayList<ShortestPathSolution> getOptimalSolutions() {
            return new ArrayList<ShortestPathSolution>() {{
                add(optimalSolutionInTime);
                add(optimalSolutionInLength);
            }};
        }

        /**
         * Gets the initial path data used to compute solutions with different filters
         * @return An array of initial path data
         */
        public ArrayList<ShortestPathData> getPathData() {
            return new ArrayList<ShortestPathData>() {{
                add(pathDataTime);
                add(pathDataLength);
            }};
        }

        public void setSolutionInLength(ShortestPathSolution solutionInLength) {
            this.solutionInLength = solutionInLength;
        }

        public void setSolutionInTime(ShortestPathSolution solutionInTime) {
            this.solutionInTime = solutionInTime;
        }

        public void setOptimalSolutionInLength(ShortestPathSolution solutionInLength) {
            this.optimalSolutionInLength = solutionInLength;
        }

        public void setOptimalSolutionInTime(ShortestPathSolution solutionInTime) {
            this.optimalSolutionInTime = solutionInTime;
        }

    }

    /**
     * Opens and reads the given graph
     *
     * @param map The map to open
     * @return The graph opened
     * @throws IOException
     */
    private Graph readGraph(String map) throws IOException {
        final GraphReader reader = new BinaryGraphReader(
                new DataInputStream(new BufferedInputStream(new FileInputStream(map))));
        return reader.read();
    }

    /**
     * Loads all graphs for later use
     * @throws IOException
     */
    private void initGraphs() throws IOException {
        graphInsa = readGraph("../Maps/insa.mapgr");
        graphSquare = readGraph("../Maps/carre.mapgr");
        graphBretagne = readGraph("../Maps/bretagne.mapgr");
        graphGuadeloupe = readGraph("../Maps/guadeloupe.mapgr");
    }

    /**
     * Loads all filters for later use
     */
    private void initFilters() {
        filterLength = ArcInspectorFactory.getAllFilters().get(0);
        filterTime = ArcInspectorFactory.getAllFilters().get(2);
    }

    private PathDataset getNewPathDataset(Graph graph, int originID, int destinationID) {
        return new PathDataset(
                graph,
                graph.getNodes().get(originID),
                graph.getNodes().get(destinationID)
        );
    }

    /**
     * Creates valid data sets
     */
    private void initValidPathList() {
        validTestData = new ArrayList<>();

        validTestData.add(getNewPathDataset(graphInsa, 512, 526));
        validTestData.add(getNewPathDataset(graphGuadeloupe, 12822, 13687));
        validTestData.add(getNewPathDataset(graphSquare, 21, 17));
    }

    /**
     * Creates invalid data sets
     */
    private void initInvalidPathList() {
        invalidTestData = new ArrayList<>();

        invalidTestData.add(getNewPathDataset(graphBretagne, 29270, 545599));
        invalidTestData.add(getNewPathDataset(graphGuadeloupe, 26963, 4307));
        invalidTestData.add(getNewPathDataset(graphGuadeloupe, 8185, 8185));
        invalidTestData.add(getNewPathDataset(graphSquare, 10, 10));
    }

    /**
     * Init all tests data before starting
     */
    @Before
    public void init() {
        initFilters();
        try {
            initGraphs();
        } catch (IOException e) {
            e.printStackTrace();
        }
        initValidPathList();
        initInvalidPathList();

        computeOptimalPathSolutions();
        computeValidPathSolutions();
        computeInvalidPathSolutions();
    }

    /**
     * Computes optimal solutions for all tests
     */
    private void computeOptimalPathSolutions() {
        for (PathDataset data : validTestData) {
            data.setOptimalSolutionInLength(new BellmanFordAlgorithm(data.getPathDataLength()).run());
            data.setOptimalSolutionInTime(new BellmanFordAlgorithm(data.getPathDataTime()).run());
        }
    }

    /**
     * Computes valid path using the chosen algorithm
     */
    protected abstract void computeValidPathSolutions();

    /**
     * Computes invalid paths using the chosen algorithm
     */
    protected abstract void computeInvalidPathSolutions();

    @Test
    public void testPathValid() {
        for (PathDataset data: validTestData) {
            for (ShortestPathSolution solution : data.getComputedSolutions()) {
                assertTrue(solution.isFeasible());
                assertTrue(solution.getPath().isValid());
            }
        }
    }

    @Test
    public void testPathInvalid() {
        for (PathDataset data: invalidTestData) {
            for (ShortestPathSolution solution : data.getComputedSolutions()) {
                assertFalse(solution.isFeasible());
            }
        }
    }

    @Test
    public void testEndNodes() {
        for (PathDataset data: validTestData) {
            ArrayList<ShortestPathData> initialData = data.getPathData();
            ArrayList<ShortestPathSolution> solutions = data.getComputedSolutions();
            for (int i = 0; i < solutions.size(); i++) {
                assertEquals(
                        initialData.get(i).getOrigin().getId(),
                        solutions.get(i).getPath().getOrigin().getId()
                );
                assertEquals(
                        initialData.get(i).getDestination().getId(),
                        solutions.get(i).getPath().getDestination().getId()
                );
            }

        }
    }

    @Test
    public void testPathOptimalWithOracle() {
        for (PathDataset data: validTestData) {
            ArrayList<ShortestPathSolution> optimalSolutions = data.getOptimalSolutions();
            ArrayList<ShortestPathSolution> computedSolutions = data.getComputedSolutions();
            for (int i = 0; i < computedSolutions.size(); i++) {
                assertEquals(
                        optimalSolutions.get(i).getPath().getLength(),
                        computedSolutions.get(i).getPath().getLength(),
                        0.0
                );
                assertEquals(
                        optimalSolutions.get(i).getPath().getMinimumTravelTime(),
                        computedSolutions.get(i).getPath().getMinimumTravelTime(),
                        0.0
                );
            }
        }
    }

    @Test
    public void testPathOptimalWithoutOracle() {
        // TODO
    }
}

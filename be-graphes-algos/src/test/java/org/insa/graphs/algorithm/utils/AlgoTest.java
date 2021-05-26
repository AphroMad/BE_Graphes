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
     * Classe utilisée pour stocker les datas en rapport avec les paths et les solutions 
     */
    class PathDataset {

        ShortestPathData pathDataTime;
        ShortestPathData pathDataLength;

        ShortestPathSolution solutionInTime; // vairblaes qui serviront pour les calculs avec l'algo utilisé 
        ShortestPathSolution solutionInLength;

        ShortestPathSolution optimalSolutionInTime; // variables qui serviront pour les calculs avec BF 
        ShortestPathSolution optimalSolutionInLength;

        // constructeur 
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
         * Calcule toutes les solutions avec les deux filtres 
         * (solution calculées  avec l'algo choisi (Dijkstra / a*) 
         * retourne un array avec les solutions 
         */
        public ArrayList<ShortestPathSolution> getComputedSolutions() {
            return new ArrayList<ShortestPathSolution>() {{
                add(solutionInTime);
                add(solutionInLength);
            }};
        }

        /**
         * Calcule les solutions optimales en fontions des filtres 
         * (solution calculées avec Bellman Ford)
         * retourne un array avec les solutions 
         */
        public ArrayList<ShortestPathSolution> getOptimalSolutions() {
            return new ArrayList<ShortestPathSolution>() {{
                add(optimalSolutionInTime);
                add(optimalSolutionInLength);
            }};
        }

        /**
         * calcule les solutions en partant du path data initial
         * retourne un array avec les solutions
         */
        public ArrayList<ShortestPathData> getPathData() {
            return new ArrayList<ShortestPathData>() {{
                add(pathDataTime);
                add(pathDataLength);
            }};
        }

        // fonctions servant a set les solutions 
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
     * Fonction qui ouvre une map
     * un chemin de map en argument
     * throw exception si problème 
     */
    private Graph LectureGraph(String map) throws IOException {
        final GraphReader reader = new BinaryGraphReader(
                new DataInputStream(new BufferedInputStream(new FileInputStream(map))));
        return reader.read();
    }

    /**
     * On charge les graphes 
     */
    private void initGraphs() throws IOException {
        graphInsa = LectureGraph("../Maps/insa.mapgr");
        graphSquare = LectureGraph("../Maps/carre.mapgr");
        graphBretagne = LectureGraph("../Maps/bretagne.mapgr");
        graphGuadeloupe = LectureGraph("../Maps/guadeloupe.mapgr");
    }

    /**
     * Charge les filtres 
     */
    private void initFilters() {
        filterLength = ArcInspectorFactory.getAllFilters().get(0);
        filterTime = ArcInspectorFactory.getAllFilters().get(2);
    }

    /**
     * Pour initialiser un chemin (le graphe, le départ et l'arrivée) 
     */
    private PathDataset getNewPathDataset(Graph graph, int originID, int destinationID) {
        return new PathDataset(
                graph,
                graph.getNodes().get(originID),
                graph.getNodes().get(destinationID)
        );
    }

    /**
     * On choisit des chemins possibles à faire 
     */
    private void initValidPathList() {
        validTestData = new ArrayList<>(); // on créé le tableau 
        // on le remplit avec différents chemins 
        validTestData.add(getNewPathDataset(graphInsa, 134, 508)); 
        validTestData.add(getNewPathDataset(graphGuadeloupe, 7627, 14971));
        validTestData.add(getNewPathDataset(graphSquare, 18, 21));
    }

    /**
     * On choisit des chemins impossibles à faire
     */
    private void initInvalidPathList() {
        invalidTestData = new ArrayList<>(); // on créé le tableau 
        // on le remplit avec différents chemins 
        invalidTestData.add(getNewPathDataset(graphBretagne, 316714, 446827));
        invalidTestData.add(getNewPathDataset(graphGuadeloupe, 23216, 13705));
        invalidTestData.add(getNewPathDataset(graphGuadeloupe, 27895, 14738));
        
        invalidTestData.add(getNewPathDataset(graphSquare, 12, 12));
    }

    /**
     * On initialise tout avant de commencer les tests
     */
    @Before
    public void init() {
        initFilters(); // on initialise les filtres 
        try {
            initGraphs(); // on charge les graphes 
        } catch (IOException e) {
            e.printStackTrace();
        }
        
        initValidPathList(); // on intialise les chemins pour les tests 
        initInvalidPathList();

        computeOptimalPathSolutions();
        computeValidPathSolutions();
        computeInvalidPathSolutions();
    }

    /**
     * Calcule la solution optimale pour chaque test (i.e. ce que calcule Bellman Ford)
     */
    private void computeOptimalPathSolutions() {
        for (PathDataset data : validTestData) {
            data.setOptimalSolutionInLength(new BellmanFordAlgorithm(data.getPathDataLength()).run());
            data.setOptimalSolutionInTime(new BellmanFordAlgorithm(data.getPathDataTime()).run());
        }
    }

    /**
     * Calcule des path juste en fonction de l'algo choisi
     */
    protected abstract void computeValidPathSolutions();

    /**
     * Calcule des faux path en fonction de l'algo choisi 
     */
    protected abstract void computeInvalidPathSolutions();

    @Test
    /**
     * Test pour voir si un path est valide
     */
    public void testPathValid() {
        for (PathDataset data: validTestData) {
            for (ShortestPathSolution solution : data.getComputedSolutions()) {
                assertTrue(solution.isFeasible());
                assertTrue(solution.getPath().isValid());
            }
        }
    }

    @Test
    /**
     * Test pour voir si un path est invalide
     */
    public void testPathInvalid() {
        for (PathDataset data: invalidTestData) {
            for (ShortestPathSolution solution : data.getComputedSolutions()) {
                assertFalse(solution.isFeasible());
            }
        }
    }


    @Test
    /**
     * Test avec oracle 
     * (on compare le résultat ce l'algo BF avec l'algo choisi)
     */
    public void testPathOptimalWithOracle() {
        for (PathDataset data: validTestData) {
            ArrayList<ShortestPathSolution> optimalSolutions = data.getOptimalSolutions();
            ArrayList<ShortestPathSolution> computedSolutions = data.getComputedSolutions();
            //System.out.println("OUI"+computedSolutions); 
            //System.out.println("Non"+optimalSolutions); 
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
  

  
}

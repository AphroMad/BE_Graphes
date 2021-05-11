package org.insa.graphs.algorithm.utils;

import org.insa.graphs.algorithm.shortestpath.DijkstraAlgorithm;

public class DijkstraTest extends AlgoTest {

    protected void computeValidPathSolutions() {
        for (PathDataset data : validTestData) {
            data.setSolutionInLength(new DijkstraAlgorithm(data.getPathDataLength()).run());
            data.setSolutionInTime(new DijkstraAlgorithm(data.getPathDataTime()).run());
        }
    }

    protected void computeInvalidPathSolutions() {
        for (PathDataset data : invalidTestData) {
            data.setSolutionInLength(new DijkstraAlgorithm(data.getPathDataLength()).run());
            data.setSolutionInTime(new DijkstraAlgorithm(data.getPathDataTime()).run());
        }
    }

}

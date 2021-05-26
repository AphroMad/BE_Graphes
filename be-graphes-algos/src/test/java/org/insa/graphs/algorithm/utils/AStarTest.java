package org.insa.graphs.algorithm.utils;

import org.insa.graphs.algorithm.shortestpath.AStarAlgorithm;

public class AStarTest extends AlgoTest {

    protected void computeValidPathSolutions() {
        for (PathDataset data : validTestData) {
            data.setSolutionInLength(new AStarAlgorithm(data.getPathDataLength()).run());
            data.setSolutionInTime(new AStarAlgorithm(data.getPathDataTime()).run());
        }
    }

    protected void computeInvalidPathSolutions() {
        for (PathDataset data : invalidTestData) {
            data.setSolutionInLength(new AStarAlgorithm(data.getPathDataLength()).run());
            data.setSolutionInTime(new AStarAlgorithm(data.getPathDataTime()).run());
        }
    }

}

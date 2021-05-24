package org.insa.graphs.algorithm.shortestpath;

import org.insa.graphs.model.Graph;

public class AStarAlgorithm extends DijkstraAlgorithm {

    public AStarAlgorithm(ShortestPathData data) {
        super(data);
    }

    private double getMaxSpeed(Graph graph, ShortestPathData inputData) {
        final double graphMaxSpeed = graph.getGraphInformation().getMaximumSpeed();
        final double inputMaxSpeed = inputData.getMaximumSpeed();
        double maxSpeed = Double.min(graphMaxSpeed, inputMaxSpeed);

        if (inputMaxSpeed < 0)
            maxSpeed = graphMaxSpeed;
        return maxSpeed;
    }

    @Override
    protected ShortestPathSolution doRun() {
        final ShortestPathData data = getInputData();
        Graph graph = data.getGraph();
        final int nbNodes = graph.size();
        LabelStar[] labels = new LabelStar[nbNodes];
        for (int i = 0; i < nbNodes; i++) {
            labels[i] = new LabelStar(i);
            final double distance = graph.getNodes().get(i).getPoint().distanceTo(
                    data.getDestination().getPoint()
            );
            final double maxSpeed = getMaxSpeed(graph, data);
            double estimatedCost = distance;
            if (data.getMode() == ShortestPathData.Mode.TIME)
                estimatedCost /= maxSpeed;
            labels[i].setcoutEstime(estimatedCost);
        }
        return this.doDijkstra(labels, data, graph);
    }
    
    
}

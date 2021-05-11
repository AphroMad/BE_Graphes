package org.insa.graphs.algorithm.shortestpath;

import org.insa.graphs.algorithm.AbstractSolution;
import org.insa.graphs.algorithm.utils.BinaryHeap;
import org.insa.graphs.algorithm.utils.ElementNotFoundException;
import org.insa.graphs.model.Arc;
import org.insa.graphs.model.Graph;
import org.insa.graphs.model.Node;
import org.insa.graphs.model.Path;

import java.util.ArrayList;
import java.util.Collections;

public class DijkstraAlgorithm extends ShortestPathAlgorithm {

    public DijkstraAlgorithm(ShortestPathData data) {
        super(data);
    }

    final private boolean DEBUG = false;

    @Override
    protected ShortestPathSolution doRun() {
        final ShortestPathData data = getInputData();
        Graph graph = data.getGraph();
        final int nbNodes = graph.size();
        Label[] labels = new Label[nbNodes];
        for (int i = 0; i < nbNodes; i++) {
            labels[i] = new Label(i);
        }
        return this.doDijkstra(labels, data, graph);
    }

    protected ShortestPathSolution doDijkstra(Label[] labels, ShortestPathData data, Graph graph) {
        final int origin = data.getOrigin().getId();
        final int destination = data.getDestination().getId();
        labels[origin].setCost(0);

        // Initialize the heap
        BinaryHeap<Label> heap = new BinaryHeap<>();
        heap.insert(labels[origin]);

        int iterationCounter = 0;

        while (!heap.isEmpty() && labels[destination].isNotMarked()) {
            iterationCounter++;

            Label parentLabel = heap.deleteMin();
            parentLabel.setMarked(true);
            Node parentNode = graph.getNodes().get(parentLabel.getAssociatedNode());
            notifyNodeMarked(parentNode);

            this.log("Cout marqué : " + parentLabel.getCost());
            this.log("Nb successeurs : " + parentNode.getNumberOfSuccessors());
            this.log("tas valide : " + heap.isValid(0));

            for (Arc arc : parentNode.getSuccessors()) {
                Label currentLabel = labels[arc.getDestination().getId()];
                notifyNodeReached(arc.getDestination());

                if (currentLabel.isNotMarked() && data.isAllowed(arc)) {
                    this.updateCost(arc, parentLabel, currentLabel, heap, data);
                }
            }
        }
        this.log("===============");
        ShortestPathSolution solution;
        // Destination has no predecessor, the solution is infeasible...
        if (labels[destination].getFatherArc() == null) {
            solution = new ShortestPathSolution(data, AbstractSolution.Status.INFEASIBLE);
        } else {
            // The destination has been found, notify the observers.
            notifyDestinationReached(data.getDestination());

            // Create the path from the array of predecessors...
            ArrayList<Arc> arcs = new ArrayList<>();
            Arc arc = labels[destination].getFatherArc();
            while (arc != null) {
                arcs.add(arc);
                arc = labels[arc.getOrigin().getId()].getFatherArc();
            }

            // Reverse the path...
            Collections.reverse(arcs);

            // Create the final solution.
            solution = new ShortestPathSolution(data, AbstractSolution.Status.OPTIMAL, new Path(graph, arcs));
            this.log("Nb arcs solution : " + solution.getPath().getArcs().size());
        }
        this.log("Nb itérations solution: " + iterationCounter);
        return solution;
    }


    private void log(String message) {
        if (this.DEBUG)
            System.out.println(message);
    }

    private void updateCost(Arc arc, Label parent, Label current, BinaryHeap<Label> heap, ShortestPathData data) {
        double newCost = parent.getCost() + data.getCost(arc);
        if (newCost < current.getCost()) {
            try {
                heap.remove(current);
            } catch (ElementNotFoundException e) {
            }
            current.setCost(newCost);
            current.setFatherArc(arc);
            heap.insert(current);
        }
    }

}

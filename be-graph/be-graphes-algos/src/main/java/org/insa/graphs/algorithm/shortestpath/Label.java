package org.insa.graphs.algorithm.shortestpath;

import org.insa.graphs.model.Arc;

public class Label implements Comparable<Label> {
    private final int associatedNode;
    private double cost;

    private boolean marked;

    private Arc fatherArc;

    public Label(int associatedNode) {
        this.associatedNode = associatedNode;
        this.marked = false;
        this.cost = Double.POSITIVE_INFINITY;
        this.fatherArc = null;
    }

    public int getAssociatedNode() {
        return associatedNode;
    }

    public double getCost() {
        return cost;
    }

    public void setCost(double cost) {
        this.cost = cost;
    }

    public boolean isNotMarked() {
        return !marked;
    }

    public void setMarked(boolean marked) {
        this.marked = marked;
    }

    public Arc getFatherArc() {
        return fatherArc;
    }

    public void setFatherArc(Arc fatherArc) {
        this.fatherArc = fatherArc;
    }

    public double getTotalCost() {
        return this.getCost();
    }

    @Override
    public int compareTo(Label label) {
        return Double.compare(this.getTotalCost(), label.getTotalCost());
    }
}

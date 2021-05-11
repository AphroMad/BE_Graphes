package org.insa.graphs.algorithm.shortestpath;

public class LabelStar extends Label {

    private double estimatedCost;

    public LabelStar(int associatedNode) {
        super(associatedNode);
        this.estimatedCost = Double.POSITIVE_INFINITY;
    }

    public double getEstimatedCost() {
        return estimatedCost;
    }

    public void setEstimatedCost(double estimatedCost) {
        this.estimatedCost = estimatedCost;
    }

    @Override
    public double getTotalCost() {
        return this.getCost() + this.getEstimatedCost();
    }

    @Override
    public int compareTo(Label label) {
        final int comparison = super.compareTo(label);

        return comparison == 0
                ? Double.compare(this.getEstimatedCost(), label.getTotalCost() - label.getCost())
                : comparison;
    }
}

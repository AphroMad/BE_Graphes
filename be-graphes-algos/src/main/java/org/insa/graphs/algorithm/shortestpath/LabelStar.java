package org.insa.graphs.algorithm.shortestpath;

public class LabelStar extends Label {

    private double coutEstime;

    public LabelStar(int associatedNode) {
        super(associatedNode);
        this.coutEstime = Double.POSITIVE_INFINITY; // on rajoute la distance estimée 
        // équivalent a une distance à vol d'oiseau pour le A star 
    }

    public double getEstimeCout() {
        return coutEstime;
    }

    public void setcoutEstime(double coutEstime) {
        this.coutEstime = coutEstime;
    }

    @Override
    public double getTotalCout() {
        return this.getCout() + this.getEstimeCout();
    }

    @Override
    public int compareTo(Label label) {
        final int comparaison = super.compareTo(label);

        return comparaison == 0
                ? Double.compare(this.getEstimeCout(), label.getTotalCout() - label.getCout())
                : comparaison;
    }
}
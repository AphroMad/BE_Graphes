package org.insa.graphs.algorithm.shortestpath;

public class LabelStar extends Label {

    private double coutEstime;

    public LabelStar(int associatedNode) {
        super(associatedNode);
        this.coutEstime = Double.POSITIVE_INFINITY; // on rajoute le cout estimé 
    }

    public double getEstimeCout() { // pour voir le cout estime 
        return coutEstime;
    }

    public void setcoutEstime(double coutEstime) { // pour le set 
        this.coutEstime = coutEstime;
    }

    @Override
    public double getTotalCout() { // on override pour avoir un total cout = au cout + cout estimé 
        return this.getCout() + this.getEstimeCout();
    }

    

}   


/* pas vraiment utile 
@Override
public int compareTo(Label label) {
    final int comparaison = super.compareTo(label);
    return comparaison == 0
            ? Double.compare(this.getEstimeCout(), label.getTotalCout() - label.getCout())
            : comparaison;
}
*/
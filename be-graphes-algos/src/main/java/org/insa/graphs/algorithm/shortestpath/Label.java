package org.insa.graphs.algorithm.shortestpath;

import org.insa.graphs.model.Arc;
import org.insa.graphs.model.Node;

public class Label implements Comparable<Label>{

	// déclaration des variables 
	private final int sommet_courant;
	private double cout;
    private boolean marque;
    private Arc papa;
    
    // Constructeur 
    public Label(int sommet_courant) {
        this.sommet_courant = sommet_courant;
        this.marque = false;
        this.cout = Double.POSITIVE_INFINITY;
        this.papa = null;
    }
    
    
    public double getCout() {
		return cout;
	}

	public void setCout(double cout) {
		this.cout = cout;
	}

	public boolean isMarque() {
		return marque;
	}

	public void setMarque(boolean marque) {
		this.marque = marque;
	}

	public Arc getPapa() {
		return papa;
	}

	public void setPapa(Arc papa) {
		this.papa = papa;
	}

	public int getSommet_courant() {
		return sommet_courant;
	}
	
    public double getTotalCout() {
        return this.getCout();
    }
	
    @Override
    public int compareTo(Label label) {
        return Double.compare(this.getTotalCout(), label.getTotalCout());
    }
}

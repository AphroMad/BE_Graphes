package org.insa.graphs.algorithm.shortestpath;

import org.insa.graphs.model.Graph;

public class AStarAlgorithm extends DijkstraAlgorithm {

    public AStarAlgorithm(ShortestPathData data) {
        super(data);
    }

    private double getMaxSpeed(Graph graph, ShortestPathData inputData) { // fonction getMaxSpeed qui renvoie maxSpeed
        final double graphMaxSpeed = graph.getGraphInformation().getMaximumSpeed(); // on prend la vitesse max du graph 
        final double inputMaxSpeed = inputData.getMaximumSpeed(); // on prend la vitesse maximale autorisée
        double maxSpeed = Double.min(graphMaxSpeed, inputMaxSpeed); // on prend la plus petite des deux
        if (inputMaxSpeed < 0) // si la plus petite est <0, alors on prend graphMaxSpeed car jamais <0
            maxSpeed = graphMaxSpeed;
        return maxSpeed; // on retourne la vitesse 
    }

    @Override
    protected ShortestPathSolution doRun() {
        final ShortestPathData data = getInputData(); // on rentre le chemin qu'on veut faire (mode / depart / arrivée / types routes)
        Graph graph = data.getGraph(); // on selectionne le graph 
        final int nbNodes = graph.size(); // on récupère la taille du graph (nombre de noeud)
        LabelStar[] labels = new LabelStar[nbNodes]; // on crée un tableau de la taille du nombre de noeud 
        for (int i = 0; i < nbNodes; i++) { // pour chaque noeud, 
            labels[i] = new LabelStar(i); // on insère son id dans le tableau 
            final double distance = graph.getNodes().get(i).getPoint().distanceTo(
                    data.getDestination().getPoint()); // on calcule la distance entre le noeud et l'arrivée
            final double maxSpeed = getMaxSpeed(graph, data); // on trouve la vitesse maximale possible
            double estimatedCost = distance; // on créé une nouvelle var estimatedCost pour pas rewrite sur distance 
            if (data.getMode() == ShortestPathData.Mode.TIME) { // si on est en mode TIME 
                estimatedCost /= maxSpeed; // on fait le calcul distance / vitesse = temps 
            }

            labels[i].setcoutEstime(estimatedCost); // on set le cout estimée avec ce que l'on vient de calculer  
        }
        return this.doDijkstra(labels, data, graph); // on run Dijsktra 
    }
    
    
}

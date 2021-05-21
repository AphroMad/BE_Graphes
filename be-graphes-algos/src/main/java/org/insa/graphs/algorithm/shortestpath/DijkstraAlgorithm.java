package org.insa.graphs.algorithm.shortestpath;

import org.insa.graphs.algorithm.AbstractSolution.Status;
import org.insa.graphs.algorithm.utils.ElementNotFoundException;
import org.insa.graphs.algorithm.utils.BinaryHeap;
import org.insa.graphs.model.Arc;
import org.insa.graphs.model.Graph;
import org.insa.graphs.model.Path;
import org.insa.graphs.model.Node;

import java.util.ArrayList;
import java.util.Collections;

public class DijkstraAlgorithm extends ShortestPathAlgorithm {

    public DijkstraAlgorithm(ShortestPathData data) {
        super(data);
    }

    @Override
    protected ShortestPathSolution doRun() {
        final ShortestPathData data = getInputData();
        ShortestPathSolution solution = null;
        
        Graph graph = data.getGraph();
        final int nbNodes = graph.size();
        Label[] labels = new Label[nbNodes];
        for (int i = 0; i < nbNodes; i++) {
            labels[i] = new Label(i);
            
        }
        
        labels[data.getOrigin().getId()].setCout(0);// pour set le cout du premier a 0  
        BinaryHeap<Label> tasBinaire = new BinaryHeap<Label>(); // déclaration du tas 
        tasBinaire.insert(labels[data.getOrigin().getId()]); // on insert le premier dedans
        int iterationCounter = 0; // on initialise le compter à 0 
        
        while(!tasBinaire.isEmpty() && labels[data.getDestination().getId()].isMarque()==false) // tant qu'il existe des sommets non marqués 
        {
           iterationCounter ++ ; // incrémentation de l'itérateur 
     	   Label courant = tasBinaire.deleteMin(); // on enlève le sommet du tas 
     	   courant.setMarque(true); // on marque le sommet a true car on l'a vu 
     	   notifyNodeMarked(graph.getNodes().get(courant.getSommet_courant()));
     	   

     	   
     	   
     	   for(Arc arc : graph.getNodes().get(courant.getSommet_courant()).getSuccessors()) { // pour tous les successeurs de x 
     		   Label successor = labels[arc.getDestination().getId()]; // on selectionne le successeur 
     		   if(successor.isMarque()==false  && data.isAllowed(arc)) { // si le successor n'a pas été visité 
     			   if(successor.getCout()> courant.getCout()+data.getCost(arc)) { // si le cout du successeur est supérieur au cout pour arriver au courant + le cout du trajet courant-successeur, alors on va mettre le cout de successeur a jour 
     				   
     				   if (successor.getPapa()==null) // si le getPapa est nul, jamais visité 
     				   {
     					   if (successor.getSommet_courant()==data.getDestination().getId()) 
     					   {notifyDestinationReached(data.getDestination());}
     					   else 
     					   {notifyNodeReached(arc.getDestination());}
     	
     				   } 
     				   else // sinon 
     				   {tasBinaire.remove(successor);} // on le supprime 

     		
     				   
     				   /*
     				   if (!Double.isInfinite(successor.getCout())) // si le cout du successeur n'est pas l'infini (cela veut dire, on l'a visité)
     				   { 
     					   System.out.println(successor);
         				   tasBinaire.remove(successor); // alors on le supprime 
         			   }
     				   */
     				   
     				   successor.setCout(courant.getCout()+data.getCost(arc)); // on set le cout pour arriver à successor
     				   //System.out.println(successor.getCout()); 
     				   successor.setPapa(arc); // on met a jour le papa 
     				   tasBinaire.insert(successor); // on met le successeur dans le tas 
     				   //System.out.println("insertion:"+successor);
     			   }
     		   }
     			   
     	   }
        }
       
        
        //System.out.println("test"); 
        ArrayList<Arc> arcs = new ArrayList<>();
        Arc arc = labels[data.getDestination().getId()].getPapa();
        double new_cout = 0 ; 
        while (arc != null) {
        	System.out.println(arc.getOrigin()); 
            arcs.add(arc);
            arc = labels[arc.getOrigin().getId()].getPapa();
            if (arc!=null)
            {
	            new_cout = new_cout + data.getCost(arc);
	            System.out.println(new_cout);
            }
        }
        // on reverse le path 
        
        Collections.reverse(arcs);

        // Create the final solution.
        solution = new ShortestPathSolution(data, Status.OPTIMAL, new Path(graph, arcs));
        
        return solution; 
    }
    
    
   
    
    
    
    
    
    
}

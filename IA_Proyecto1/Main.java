package IA_Proyecto1;

import IA.Desastres.*;

import aima.search.framework.*;
import aima.search.informed.*;
import java.util.Random;

public class Main {

    public static void main(String[] args) throws Exception {
        
        int numgrupos = 100;
        int numCentres = 5;
        int numHelicopters = 1; 

        for(int seed = 1; seed <= 10; seed++){;
            System.out.println("Llavor utilitzada: " + seed);

            Grupos grupos = new Grupos(numgrupos, seed);
            Centros centres = new Centros(numCentres, numHelicopters, seed);
            
            RescueStates estatInicial = Generator2.generate(grupos, centres);
    
            System.out.println("Temps INICIAL: " + estatInicial.toString());

            Problem problem = new Problem(
                    estatInicial,
                    /**TRIAR ENTRE HC I SA*/
                    new SuccessorHC(),
                    //new SuccessorSA(),
                    new IsGoalTest(),
                    new Heuristic2()
            );
            int steps = 100000;
            int stiter = 1000;
            int k = 1;
            double lambda = 0.001;

            //Search search = new SimulatedAnnealingSearch(steps, stiter, k, lambda);
            Search search = new HillClimbingSearch();
            
            /**SERVEIX PER MESURAR EL TEMPS D'EXECUCIO DE L'ALGORISME*/
            long startTime = System.currentTimeMillis();
            SearchAgent agent = new SearchAgent(problem, search);
            long endTime = System.currentTimeMillis();

            long tempsTrigat = endTime - startTime;

            RescueStates resultado = (RescueStates) search.getGoalState();
            
            System.out.println("Temps FINAL: " + resultado.toString());
            System.out.println("Temps d'execució del Hill Climbing: " + tempsTrigat + " ms\n");
            //System.out.println("Temps d'execució del Simulated Annealing: " + tempsTrigat + " ms\n");
        }       
    }
}

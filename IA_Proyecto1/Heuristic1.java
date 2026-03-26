package IA_Proyecto1;

import aima.search.framework.HeuristicFunction;
import IA.Desastres.*;

import java.util.ArrayList;

public class Heuristic1 implements HeuristicFunction {

    /**
     * CALCULA EL TEMPS TOTAL DE LA MISSIÓ DE RESCAT
     * PER OPTIMITZAR EL PRIMER CRITERI DE QUALITAT.
     */
    public double getHeuristicValue(Object state) {
        RescueStates s = (RescueStates) state;
        double tempsTotal = 0;
        Grupos grups = s.getGrups();
        Centros centres = s.getCentros();
        int numHelis = s.getNumHelicopters();
        
        int CAPACITAT_MAX = 15; 
        int MAX_GRUPS_PER_VIATGE = 3; 
        
        /**PER CADA HELICOPTER */
        for (int h = 0; h < numHelis; h++) {
            Centro c = centres.get(h);
            ArrayList<Integer> llista = s.getGrupsHelicopter(h);
            
            int personesHeli = 0;
            int grupsViatgeActual = 0;
            Grupo anterior = null;
            
            /**PER CADA GRUP */
            for (int i = 0; i < llista.size(); i++) {
                Grupo g = grups.get(llista.get(i));
                int numPersones = g.getNPersonas();

                /**COMPROVEM SI RECOLLIR AQUEST GRUP EXCEDEIX LA CAPACITAT O EL LÍMIT DE 3 GRUPS */
                if (personesHeli + numPersones > CAPACITAT_MAX || grupsViatgeActual == MAX_GRUPS_PER_VIATGE) {
                    /**TORNEM A LA BASE */
                    tempsTotal += distanciaCentreGrup(c, anterior) * (60.0 / 100.0);
                    tempsTotal += 10; /**TEMPS DE DESCANS */
                    
                    /**REINICIEM VALORS PEL SEGÜENT VIATGE */
                    personesHeli = 0;
                    grupsViatgeActual = 0; 
                    anterior = null;
                }

                /**ANEM A BUSCAR AL GRUP */
                if (anterior == null) tempsTotal += distanciaCentreGrup(c, g) * (60.0 / 100.0);
                else tempsTotal += distanciaGrupGrup(anterior, g) * (60.0 / 100.0);

                /**TEMPS DE RESCAT */
                int prio = g.getPrioridad();
                if (prio == 1) tempsTotal += numPersones * 2; 
                else tempsTotal += numPersones;

                personesHeli += numPersones;
                grupsViatgeActual++; 
                anterior = g;
            }
            
            /**SI HEM ACABAT I TENIEM UN VIATGE A MITGES */
            if (anterior != null) tempsTotal += distanciaCentreGrup(c, anterior) * (60.0 / 100.0);
        }

        return tempsTotal;
    }

    private double distanciaCentreGrup(Centro c, Grupo g) {
        double dx = c.getCoordX() - g.getCoordX();
        double dy = c.getCoordY() - g.getCoordY();

        return Math.sqrt(dx * dx + dy * dy);
    }

    private double distanciaGrupGrup(Grupo g1, Grupo g2) {
        double dx = g1.getCoordX() - g2.getCoordX();
        double dy = g1.getCoordY() - g2.getCoordY();

        return Math.sqrt(dx * dx + dy * dy);
    }
}

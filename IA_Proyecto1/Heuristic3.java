package IA_Proyecto1;

import aima.search.framework.HeuristicFunction;
import IA.Desastres.*;

import java.util.ArrayList;

public class Heuristic3 implements HeuristicFunction {

    /**
     * CALCULA EL TEMPS TOTAL (CRITERI 1) PERÒ PENALITZA ELS VIATGES 
     * ON L'HELICÒPTER TORNA A LA BASE SENSE ESTAR PLE.
     */
    public double getHeuristicValue(Object state) {
        RescueStates s = (RescueStates) state;

        double tempsTotal = 0;
        double penalitzacioSeientsBuits = 0;

        Grupos grups = s.getGrups();
        Centros centres = s.getCentros();
        int numHelis = s.getNumHelicopters();
        
        int CAPACITAT_MAX = 15; 
        int MAX_GRUPS_PER_VIATGE = 3; 

        for (int h = 0; h < numHelis; h++) {
            Centro c = centres.get(h);
            ArrayList<Integer> llista = s.getGrupsHelicopter(h);
            
            int personesHeli = 0;
            int grupsViatgeActual = 0;
            Grupo anterior = null;

            for (int i = 0; i < llista.size(); i++) {
                Grupo g = grups.get(llista.get(i));
                int numPersones = g.getNPersonas();

                // COMPROVEM SI HEM DE TORNAR A LA BASE
                if (personesHeli + numPersones > CAPACITAT_MAX || grupsViatgeActual == MAX_GRUPS_PER_VIATGE) {
                    
                    tempsTotal += distanciaCentreGrup(c, anterior) * (60.0 / 100.0);
                    tempsTotal += 10; 
                    
                    int seientsBuits = CAPACITAT_MAX - personesHeli;
                    penalitzacioSeientsBuits += seientsBuits;

                    personesHeli = 0;
                    grupsViatgeActual = 0; 
                    anterior = null;
                }

                // ANEM A BUSCAR AL GRUP
                if (anterior == null) tempsTotal += distanciaCentreGrup(c, g) * (60.0 / 100.0);
                else tempsTotal += distanciaGrupGrup(anterior, g) * (60.0 / 100.0);

                // TEMPS DE RESCAT
                int prio = g.getPrioridad();
                if (prio == 1) tempsTotal += numPersones * 2; 
                else tempsTotal += numPersones;

                personesHeli += numPersones;
                grupsViatgeActual++; 
                anterior = g;
            }

            // SI ENS QUEDA UN VIATGE A MITGES AL FINAL DEL DIA
            if (anterior != null) {
                tempsTotal += distanciaCentreGrup(c, anterior) * (60.0 / 100.0);
                
                int seientsBuits = CAPACITAT_MAX - personesHeli;
                penalitzacioSeientsBuits += seientsBuits;
            }
        }

        // FACTOR DE PENALITZACIÓ
        // Un valor més alt obligarà l'algorisme a agrupar gent peti qui peti,
        // encara que hagi de fer més volta de distància.
        double FACTOR_OMPLIR = 50.0; 

        return tempsTotal + (penalitzacioSeientsBuits * FACTOR_OMPLIR);
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
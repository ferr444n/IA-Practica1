package IA_Proyecto1;

import aima.search.framework.HeuristicFunction;
import IA.Desastres.*;

import java.util.ArrayList;

public class Heuristic3 implements HeuristicFunction {

    /**
     * La funció heurística per saber com de bona pot ser una solució donada.
     *
     * Criteri Heurística 3:
     * Minimitza la suma del temps total de tots els helicòpters, PERÒ
     * afegeix una penalització EXTREMA a la suma dels temps d'arribada 
     * a *CADA UN* dels grups de prioritat 1. Això força a recollir-los els primers.
     */
    public double getHeuristicValue(Object state) {
        RescueStates s = (RescueStates) state;
        
        // Variables GLOBALS acumulatives per a tota la missió
        double tiempoTotalMision = 0;
        double sumaTodosTiemposPrio1 = 0; // Acumularà cada un dels temps d'espera dels P1
        
        Grupos grupos = s.getGrupos();
        Centros centros = s.getCentros();
        int numHelis = s.getNumHelicopteros();

        int CAPACIDAD_MAX = 15;
        int MAX_GRUPOS_POR_VIAJE = 3;

        for (int h = 0; h < numHelis; h++) {
            Centro c = centros.get(h);
            ArrayList<Integer> lista = s.getGruposHelicoptero(h);

            // Variables LOCALS per a l'helicòpter actual
            double tiempoHeli = 0;
            double sumaTiemposPrio1Heli = 0; // Temps d'espera P1 acumulats D'AQUEST helicòpter
            
            int personasEnHeli = 0;
            int gruposEnViajeActual = 0; 
            Grupo anterior = null;

            for (int i = 0; i < lista.size(); i++) {
                Grupo g = grupos.get(lista.get(i));
                int numPersonas = g.getNPersonas();

                // Comprovem límits de capacitat o de nombre de grups
                if (personasEnHeli + numPersonas > CAPACIDAD_MAX || gruposEnViajeActual == MAX_GRUPOS_POR_VIAJE) {
                    tiempoHeli += distanciaCentroGrupo(c, anterior) * (60.0 / 100.0);
                    tiempoHeli += 10; // Descans a base

                    personasEnHeli = 0;
                    gruposEnViajeActual = 0;
                    anterior = null;
                }

                // Sumar temps de desplaçament
                if (anterior == null) tiempoHeli += distanciaCentroGrupo(c, g) * (60.0 / 100.0);
                else tiempoHeli += distanciaGrupoGrupo(anterior, g) * (60.0 / 100.0);

                int prio = g.getPrioridad();
                if (prio == 1) {
                    tiempoHeli += numPersonas * 2; // Rescat: 2 min/persona
                    
                    // DIFERÈNCIA CLAU AMB L'HEURÍSTICA 2: ACUMULEM!
                    // A cada grup de P1 que trobem, sumem el minut en què hem arribat.
                    // Si el recollim tard, el valor serà enorme.
                    sumaTiemposPrio1Heli += tiempoHeli; 
                } else {
                    tiempoHeli += numPersonas; // Rescat: 1 min/persona
                }

                personasEnHeli += numPersonas;
                gruposEnViajeActual++;
                anterior = g;
            }

            if (anterior != null) {
                tiempoHeli += distanciaCentroGrupo(c, anterior) * (60.0 / 100.0);
                tiempoHeli += 10;
            }

            // Sumar els càlculs d'aquest helicòpter al sumatori global
            tiempoTotalMision += tiempoHeli;
            sumaTodosTiemposPrio1 += sumaTiemposPrio1Heli;
        }

        // PESOS: Li donem una prioritat brutal al temps d'espera de P1 respecte al de la missió.
        // Tu pots ajustar el valor '100' més endavant a l'hora de fer la memòria per veure l'impacte.
        return tiempoTotalMision + 100 * sumaTodosTiemposPrio1;
    }

    private double distanciaCentroGrupo(Centro c, Grupo g) {
        double dx = c.getCoordX() - g.getCoordX();
        double dy = c.getCoordY() - g.getCoordY();
        return Math.sqrt(dx * dx + dy * dy);
    }

    private double distanciaGrupoGrupo(Grupo g1, Grupo g2) {
        double dx = g1.getCoordX() - g2.getCoordX();
        double dy = g1.getCoordY() - g2.getCoordY();
        return Math.sqrt(dx * dx + dy * dy);
    }
}
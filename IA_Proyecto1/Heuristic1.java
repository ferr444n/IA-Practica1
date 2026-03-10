package IA_Proyecto1;

import aima.search.framework.HeuristicFunction;
import IA.Desastres.*;

import java.util.ArrayList;

public class Heuristic1 implements HeuristicFunction {

    /**
     * La funcio heuristica per saber com de bona pot ser una solucio donada.
     *
     * Es basa en el temps que tarda el ultim helicopter en acabar tota la seva
     * ruta
     * 
     * IMPORTANTE: NO SE TIENE EN CUENTA EL TIEMPO QUE SE TARDA DE ACABAR 
     * EL RESCATE DE PRIORIDAD 1, SOLO EL TOTAL
     */
    public double getHeuristicValue(Object state) {
        RescueStates s = (RescueStates) state;
        double maxTiempo = 0;
        Grupos grupos = s.getGrupos();
        Centros centros = s.getCentros();
        int numHelis = s.getNumHelicopteros();
        
        int CAPACIDAD_MAX = 15; // POSA LA CAPACITAT REAL DE L'ENUNCIAT AQUÍ

        for (int h = 0; h < numHelis; h++) {
            Centro c = centros.get(h);
            ArrayList<Integer> lista = s.getGruposHelicoptero(h);
            
            double tiempoHeli = 0;
            double tiempoViajeActual = 0;
            int personasEnHeli = 0;
            Grupo anterior = null;

            // Iterem d'un en un (ja no de 3 en 3)
            for (int i = 0; i < lista.size(); i++) {
                Grupo g = grupos.get(lista.get(i));
                int numPersonas = g.getNPersonas();

                // 1. Comprovem si recollir aquest grup excedeix la capacitat
                if (personasEnHeli + numPersonas > CAPACIDAD_MAX) {
                    // TORNEM A LA BASE per finalitzar el viatge actual
                    tiempoViajeActual += distanciaCentroGrupo(c, anterior) * (60.0 / 100.0);
                    tiempoViajeActual += 10; // Temps de descans
                    tiempoHeli += tiempoViajeActual; // Acumulem el temps del viatge
                    
                    // Reiniciem l'helicòpter
                    tiempoViajeActual = 0;
                    personasEnHeli = 0;
                    anterior = null;
                }

                // 2. Anem a buscar el grup (des de la base o des de l'últim grup)
                if (anterior == null) {
                    tiempoViajeActual += distanciaCentroGrupo(c, g) * (60.0 / 100.0);
                } else {
                    tiempoViajeActual += distanciaGrupoGrupo(anterior, g) * (60.0 / 100.0);
                }

                // 3. Temps de rescat
                int prio = g.getPrioridad();
                if (prio == 1) {
                    tiempoViajeActual += numPersonas * 2; 
                } else {
                    tiempoViajeActual += numPersonas;
                }

                // 4. Actualitzem l'estat per a la següent iteració
                personasEnHeli += numPersonas;
                anterior = g;
            }

            // Si hem acabat tots els grups de la llista però estàvem a mitges d'un viatge...
            if (anterior != null) {
                // Cal tornar a la base definitivament
                tiempoViajeActual += distanciaCentroGrupo(c, anterior) * (60.0 / 100.0);
                // Segons l'enunciat potser no cal descansar al final de tot, si en cal, suma-li +10 aquí.
                tiempoHeli += tiempoViajeActual;
            }

            if (tiempoHeli > maxTiempo) {
                maxTiempo = tiempoHeli;
            }
        }

        return maxTiempo;
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

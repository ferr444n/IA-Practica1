package IA_Proyecto1;

import aima.search.framework.HeuristicFunction;
import IA.Desastres.*;

import java.util.ArrayList;

public class Heuristic2 implements HeuristicFunction {

    /**
     * La funcio heuristica per saber com de bona pot ser una solucio donada.
     *
     * Es basa en minimitzar la suma del temps total de tots els helicopters
     * i prioritza els grupos de prioritat 1 sumant el temps fins a l'últim
     * rescat de prioritat 1 de cada helicòpter.
     */
    public double getHeuristicValue(Object state) {
        RescueStates s = (RescueStates) state;
        
        // Variables GLOBALS acumulatives per a tota la missió
        double tiempoTotalMision = 0;
        double tiempoTotalPrio1 = 0;
        
        Grupos grupos = s.getGrups();
        Centros centros = s.getCentros();
        int numHelis = s.getNumHelicopters();

        int CAPACIDAD_MAX = 15; // Capacitat real segons l'enunciat
        int MAX_GRUPOS_POR_VIAJE = 3; // Màxim de grupos per sortida

        for (int h = 0; h < numHelis; h++) {
            Centro c = centros.get(h);
            ArrayList<Integer> lista = s.getGrupsHelicopter(h);

            // Variables LOCALS només per a l'helicòpter actual
            double tiempoHeli = 0;
            double tiempoHeliPrio1 = 0;
            
            int personasEnHeli = 0;
            int gruposEnViajeActual = 0; 
            Grupo anterior = null;

            // Iterem els grupos d'aquest helicòpter d'un en un
            for (int i = 0; i < lista.size(); i++) {
                Grupo g = grupos.get(lista.get(i));
                int numPersonas = g.getNPersonas();

                // Comprovem si recollir aquest grup excedeix la capacitat O si ja portem 3 grupos
                if (personasEnHeli + numPersonas > CAPACIDAD_MAX || gruposEnViajeActual == MAX_GRUPOS_POR_VIAJE) {
                    // TORNEM A LA BASE per finalitzar el viatge actual
                    tiempoHeli += distanciaCentroGrupo(c, anterior) * (60.0 / 100.0);
                    tiempoHeli += 10; // Temps de descans a la base

                    // Reiniciem els valors per al nou viatge
                    personasEnHeli = 0;
                    gruposEnViajeActual = 0; // Reiniciem el comptador de grupos
                    anterior = null;
                }

                // Anem a buscar el grup (des de la base o des de l'últim grup visitat)
                if (anterior == null) tiempoHeli += distanciaCentroGrupo(c, g) * (60.0 / 100.0);
                else tiempoHeli += distanciaGrupoGrupo(anterior, g) * (60.0 / 100.0);

                // Temps de rescat
                int prio = g.getPrioridad();
                if (prio == 1) {
                    tiempoHeli += numPersonas * 2;
                    // Guardem només el temps d'AQUEST helicòpter
                    tiempoHeliPrio1 = tiempoHeli; 
                } else {
                    tiempoHeli += numPersonas;
                }

                // Actualitzem l'estat per a la següent iteració
                personasEnHeli += numPersonas;
                gruposEnViajeActual++; // Afegim un grup al comptador del viatge actual
                anterior = g;
            }

            // Si hem acabat d'iterar tots els grupos però teníem un viatge a mitges...
            if (anterior != null) {
                // Cal tornar a la base definitivament per acabar la ruta
                tiempoHeli += distanciaCentroGrupo(c, anterior) * (60.0 / 100.0);
                tiempoHeli += 10; // descans final
            }

            // ARA SÍ: Afegim el temps calculat d'aquest helicòpter a les sumes globals
            tiempoTotalMision += tiempoHeli;
            tiempoTotalPrio1 += tiempoHeliPrio1;
        }

        // Retornem la suma de tots els temps més una penalització pel temps dels de prioritat 1
        return tiempoTotalMision + 2 * tiempoTotalPrio1;
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
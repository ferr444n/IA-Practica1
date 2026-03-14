

package IA_Proyecto1;

import aima.search.framework.HeuristicFunction;
import IA.Desastres.*;

import java.util.ArrayList;

public class Heuristic2 implements HeuristicFunction {

    /**
     * La funcio heuristica per saber com de bona pot ser una solucio donada.
     *
     * Es basa en el temps que tarda el ultim helicopter en acabar tota la seva
     * ruta,
     * pero prioritzant els grups de prioritat 1
     *per a minimitzar el seu temps de rescat
     * 
     */
    public double getHeuristicValue(Object state) {
        RescueStates s = (RescueStates) state;
        double maxTiempo = 0;
        double maxTiempoPrio1 = 0;
        Grupos grupos = s.getGrupos();
        Centros centros = s.getCentros();
        int numHelis = s.getNumHelicopteros();

        int CAPACIDAD_MAX = 15; // Capacitat real segons l'enunciat
        int MAX_GRUPOS_POR_VIAJE = 3; // Màxim de grups per sortida

        for (int h = 0; h < numHelis; h++) {
            Centro c = centros.get(h);
            ArrayList<Integer> lista = s.getGruposHelicoptero(h);

            double tiempoHeli = 0;
            double tiempoViajeActual = 0;

            double tiempoHastaUltimoPrio1 = 0;

            int personasEnHeli = 0;
            int gruposEnViajeActual = 0; // Nou comptador per controlar la restricció dels 3 grups
            Grupo anterior = null;

            // Iterem d'un en un
            for (int i = 0; i < lista.size(); i++) {
                Grupo g = grupos.get(lista.get(i));
                int numPersonas = g.getNPersonas();

                // 1. Comprovem si recollir aquest grup excedeix la capacitat O si ja portem 3 grups
                if (personasEnHeli + numPersonas > CAPACIDAD_MAX || gruposEnViajeActual == MAX_GRUPOS_POR_VIAJE) {
                    // TORNEM A LA BASE per finalitzar el viatge actual
                    tiempoViajeActual += distanciaCentroGrupo(c, anterior) * (60.0 / 100.0);
                    tiempoViajeActual += 10; // Temps de descans a la base
                    tiempoHeli += tiempoViajeActual; // Acumulem el temps del viatge a l'helicòpter

                    // Reiniciem els valors per al nou viatge
                    tiempoViajeActual = 0;
                    personasEnHeli = 0;
                    gruposEnViajeActual = 0; // Reiniciem el comptador de grups
                    anterior = null;
                }

                // 2. Anem a buscar el grup (des de la base o des de l'últim grup visitat)
                if (anterior == null) {
                    tiempoViajeActual += distanciaCentroGrupo(c, g) * (60.0 / 100.0);
                } else {
                    tiempoViajeActual += distanciaGrupoGrupo(anterior, g) * (60.0 / 100.0);
                }

                // 3. Temps de rescat
                int prio = g.getPrioridad();
                if (prio == 1) {
                    tiempoViajeActual += numPersonas * 2;

                    tiempoHastaUltimoPrio1 = tiempoHeli + tiempoViajeActual;

                } else {
                    tiempoViajeActual += numPersonas;
                }

                // 4. Actualitzem l'estat per a la següent iteració
                personasEnHeli += numPersonas;
                gruposEnViajeActual++; // Afegim un grup al comptador del viatge actual
                anterior = g;
            }

            // Si hem acabat d'iterar tots els grups però teníem un viatge a mitges...
            if (anterior != null) {
                // Cal tornar a la base definitivament per acabar la ruta
                tiempoViajeActual += distanciaCentroGrupo(c, anterior) * (60.0 / 100.0);

                tiempoViajeActual += 10;

                tiempoHeli += tiempoViajeActual;
            }

            // Actualitzem si és l'helicòpter que més triga fins ara (coll d'ampolla)
            if (tiempoHeli > maxTiempo) {
                maxTiempo = tiempoHeli;
            }

            if (tiempoHastaUltimoPrio1 > maxTiempoPrio1) {
                maxTiempoPrio1 = tiempoHastaUltimoPrio1;
            }

        }

        return maxTiempo + 2 * maxTiempoPrio1;
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

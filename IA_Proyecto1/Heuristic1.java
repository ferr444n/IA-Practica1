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

        /* agafem el estat a comprobar */
        RescueStates s = (RescueStates) state;

        /**
         * Variables utils
         */
        double maxTiempo = 0;
        double tiempoPrio1 = 0;
        Grupos grupos = s.getGrupos();
        Centros centros = s.getCentros();
        int numHelis = s.getNumHelicopteros();

        /* Per a cada helicopter calculem el que tarda i ens quedem amb el maxim. */
        for (int h = 0; h < numHelis; h++) {

            Centro c = centros.get(h);
            ArrayList<Integer> lista = s.getGruposHelicoptero(h);
            double tiempoHeli = 0;

            /* La formula ve donada per:
            * Temps = TcentreTogrup1 + Tgrup1ToGrup2 + Tgrup2ToGrup3 + Tgrup3ToBase + 
            *           Trescat1 + Trescat2 + Trescat3 + 10
            * 
            * Si 100 km/h * dist -> km/h * 1 h/60 min
            *                           => 100/60 km/min 
             */
            for (int i = 0; i < lista.size(); i += 3) {

                double tiempoRuta = 0;

                Grupo g1 = grupos.get(lista.get(i));

                /**
                 * Temps del centre aal primer grup
                 */
                tiempoRuta += distanciaCentroGrupo(c, g1) * (100 / 60);

                /**
                 * Calcular el temps de recollida
                 */
                int prio = g1.getPrioridad();
                int numPersones = g1.getNPersonas();
                if (prio == 1) {
                    tiempoRuta += numPersones * 2; 
                }else {
                    tiempoRuta += numPersones;
                }

                /**
                 * Guardar el grup anterior per a calcular la distancia
                 */
                Grupo anterior = g1;

                for (int j = 1; j < 3 && i + j < lista.size(); j++) {

                    /**
                     * Agafar el seguent grup de la llista.
                     */
                    Grupo g = grupos.get(lista.get(i + j));

                    /**
                     * Temps del grup anterior al actual
                     */
                    tiempoRuta += distanciaGrupoGrupo(anterior, g) * (100 / 60);

                    /**
                     * Temps de rescat
                     */
                    prio = g.getPrioridad();
                    numPersones = g.getNPersonas();
                    if (prio == 1) {
                        tiempoRuta += numPersones * 2; 
                    }else {
                        tiempoRuta += numPersones;
                    }

                    anterior = g;
                }

                /** Distancia del ultim grup al cnetre */
                tiempoRuta += distanciaCentroGrupo(c, anterior)*(100/60);

                //** Temps de descans */
                tiempoRuta += 10;
                tiempoHeli += tiempoRuta;
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

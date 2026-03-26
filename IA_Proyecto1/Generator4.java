package IA_Proyecto1;

import IA.Desastres.*;
import java.util.*;

public class Generator4 {

    /**
     * SEPARA ALS GRUPS PER PRIORITAT I ELS FICA A L'HELICOPTER MÉS PROPER COMENÇANT PER PRIOTAT 1
     */
    public static RescueStates generate(Grupos grups, Centros centres) {

        RescueStates state = new RescueStates(grups, centres);
        int numHelis = centres.size();

        ArrayList<Integer> prio1 = new ArrayList<>();
        ArrayList<Integer> prio2 = new ArrayList<>();
        /**SEPAREM GRUPS PER PRIORITAT*/
        for (int i = 0; i < grups.size(); i++) {
            Grupo g = grups.get(i);
            if (g.getPrioridad() == 1) prio1.add(i);
            else prio2.add(i);
        }

        /**ASSIGNEM ALS DE PRIORITAT 1 AL MÉS PROPER */
        for (int g = 0; g < prio1.size(); g++) {
            int grupIndex = prio1.get(g);
            Grupo grup = grups.get(grupIndex);

            double bestDist = Double.MAX_VALUE;
            int bestHeli = 0;

            for (int h = 0; h < numHelis; h++) {
                Centro c = centres.get(h);
                double d = distanciaCentreGrup(c, grup);

                if (d < bestDist) {
                    bestDist = d;
                    bestHeli = h;
                }
            }

            state.addGrup(bestHeli, grupIndex);
        }

        for (int g = 0; g < prio2.size(); g++) {
            int grupIndex = prio2.get(g);
            Grupo grup = grups.get(grupIndex);
            double bestDist = Double.MAX_VALUE;
            int bestHeli = 0;

            for (int h = 0; h < numHelis; h++) {
                Centro c = centres.get(h);
                double d = distanciaCentreGrup(c, grup);

                if (d < bestDist) {
                    bestDist = d;
                    bestHeli = h;
                }
            }
            state.addGrup(bestHeli, grupIndex);
        }

        return state;
    }

    private static double distanciaCentreGrup(Centro c, Grupo g) {

        double dx = c.getCoordX() - g.getCoordX();
        double dy = c.getCoordY() - g.getCoordY();

        return Math.sqrt(dx * dx + dy * dy);
    }
}
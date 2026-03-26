package IA_Proyecto1;

import IA.Desastres.*;
import java.util.*;

public class Generator2 {

    /**
     * Assigna cada grup al centre més proper tal i com arriben. 
     */
    public static RescueStates generate(Grupos grups, Centros centres) {

        RescueStates state = new RescueStates(grups, centres);
        int numHelis = centres.size();

        for (int g = 0; g < grups.size(); g++) {
            Grupo grupo = grups.get(g);
            double bestDist = Double.MAX_VALUE;
            int bestHeli = 0;

            /**ES MIRA QUIN HELICOPTER ÉS EL MÉS PROPER */
            for (int h = 0; h < numHelis; h++) {
                Centro c = centres.get(h);
                double d = distanciaCentreGrup(c, grupo);
                if (d < bestDist) {
                    bestDist = d;
                    bestHeli = h;
                }
            }

            state.addGrup(bestHeli, g);
        }

        return state;
    }

    private static double distanciaCentreGrup(Centro c, Grupo g) {
        double dx = c.getCoordX() - g.getCoordX();
        double dy = c.getCoordY() - g.getCoordY();
        return Math.sqrt(dx * dx + dy * dy);
    }
}
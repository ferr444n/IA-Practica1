package IA_Proyecto1;

import IA.Desastres.*;
import java.util.*;

public class Generator4 {

    /**
     * ORDENA ELS GRUPS PER NOMBRE DE PASSATGERS (MÉS A MENYS) 
     * I ELS ASSIGNA A L'HELICÒPTER MÉS PROPER
     */
    public static RescueStates generate(Grupos grups, Centros centres) {

        RescueStates state = new RescueStates(grups, centres);
        int numHelis = centres.size();

        List<Integer> indexosGrups = new ArrayList<>();
        for (int i = 0; i < grups.size(); i++) {
            indexosGrups.add(i);
        }

        indexosGrups.sort((id1, id2) -> {
            int persones1 = grups.get(id1).getNPersonas();
            int persones2 = grups.get(id2).getNPersonas();
            return Integer.compare(persones2, persones1);
        });

        for (int grupIndex : indexosGrups) {
            Grupo g = grups.get(grupIndex);
            
            int bestHeli = 0;
            double minDist = Double.MAX_VALUE;

            for (int h = 0; h < numHelis; h++) {
                Centro c = centres.get(h);
                double dist = distanciaCentreGrup(c, g);
                if (dist < minDist) {
                    minDist = dist;
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
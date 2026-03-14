package IA_Proyecto1;

import IA.Desastres.*;
import java.util.*;

public class Generator2 {

    /**
     * Generador inicial [optimizado].
     * 
     * Basicamente se asignan los grupos al centro mas cercano
     * 
     * es per comprobar que al fer aixo es fan menys passos
     * i que ademes no es de tan bona qualitat la resposta.
     * 
     *      */
    public static RescueStates generate(Grupos grupos, Centros centros) {

        /** Es genera un nou estat que sera e lninicial*/
        RescueStates state = new RescueStates(grupos, centros);
        int numHelis = centros.size();

        /* Per a cada centre, es busca el que estigui mes a prop */
        for (int g = 0; g < grupos.size(); g++) {

            Grupo grupo = grupos.get(g);

            double bestDist = Double.MAX_VALUE;
            int bestHeli = 0;

            /** Es mira quin helicopter es mes proper */
            for (int h = 0; h < numHelis; h++) {

                Centro c = centros.get(h);

                double d = distance(c, grupo);

                if (d < bestDist) {
                    bestDist = d;
                    bestHeli = h;
                }
            }

            state.addGrupo(bestHeli, g);
        }

        return state;
    }

    /**
     * La distancias entre centre i grup.
     */
    private static double distance(Centro c, Grupo g) {

        double dx = c.getCoordX() - g.getCoordX();
        double dy = c.getCoordY() - g.getCoordY();

        return Math.sqrt(dx * dx + dy * dy);
    }
}
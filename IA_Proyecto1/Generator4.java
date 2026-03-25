package IA_Proyecto1;

import IA.Desastres.*;
import java.util.*;

public class Generator4 {

    /**
     * Basicamente se asignan los grupos al centro mas cercano poniendo primero a los de prioriad 1.
     * 
     * es per comprobar que al fer aixo es fan menys passos
     * i que ademes no es de tan bona qualitat la resposta.
     * 
     */
    public static RescueStates generate(Grupos grupos, Centros centros) {

        /** Es genera un nou estat que sera el inicial*/
        RescueStates state = new RescueStates(grupos, centros);
        int numHelis = centros.size();

        /* Es fa la separacion de grups per prioritat */
        ArrayList<Integer> prio1 = new ArrayList<>();
        ArrayList<Integer> prio2 = new ArrayList<>();
        for (int i = 0; i < grupos.size(); i++) {
            Grupo g = grupos.get(i);
            if (g.getPrioridad() == 1) prio1.add(i);
            else prio2.add(i);
        }

        /* Per a cada grup de prioritat 1, es busca el que estigui mes aprop */
        for (int g = 0; g < prio1.size(); g++) {
            int grupoIndex = prio1.get(g);
            Grupo grupo = grupos.get(grupoIndex);

            double bestDist = Double.MAX_VALUE;
            int bestHeli = 0;

            /** Es mira quin helicopter és més proper */
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

        /* Lo mateix amb la prio 2 */
        for (int g = 0; g < prio2.size(); g++) {
            int grupoIndex = prio2.get(g);
            Grupo grupo = grupos.get(grupoIndex);
            double bestDist = Double.MAX_VALUE;
            int bestHeli = 0;
            for (int h = 0; h < numHelis; h++) {

                Centro c = centros.get(h);

                double d = distance(c, grupo);

                if (d < bestDist) {
                    bestDist = d;
                    bestHeli = h;
                }
            }
            state.addGrupo(bestHeli, grupoIndex);
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
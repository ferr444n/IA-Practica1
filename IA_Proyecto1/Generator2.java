package IA_Proyecto1;

import IA.Desastres.*;
import java.util.*;

public class Generator2 {


    /**
     * 
     * Ara a fer un generador mes complex per a tindre el problema casi resolt desde
     * el inici.
     * 
     * La idea es la mateixa pero ordenant els grups per distancia al centre
     * i assignant als helicpters els grups mes propers. 
     *  
     * Així part del problema estara solucionat.
     * 
     * */

    public static RescueStates generate(Grupos grupos, Centros centros) {

        /**Es genera un estat buit */
        RescueStates state = new RescueStates(grupos, centros);

        int numHelis = centros.size();

        /* Com ara no recorrem el vector de grupos, si no que el ordenem
        tenim que anar amb compte de no assignar el mateix grup dues vegades. */
        boolean[] assigned = new boolean[grupos.size()];

        for (int h = 0; h < numHelis; h++) {
            /* Per a cada centre, agafem la seva posicio */
            Centro c = centros.get(h);
            ArrayList<Integer> order = new ArrayList<>();

            /**Ficar tots els grups possibles dins del vector per a orenar */
            for (int i = 0; i < grupos.size(); i++) {
                order.add(i);
            }

            /**Ordenar per distancia al centre */
            order.sort((g1, g2) -> {

                Grupo gr1 = grupos.get(g1);
                Grupo gr2 = grupos.get(g2);

                double d1 = distance(c, gr1);
                double d2 = distance(c, gr2);

                return Double.compare(d1, d2);
            });

            /**Assignem els grups propers al helicopter del centre. */
            for (Integer g : order) {

                if (!assigned[g]) {

                    state.addGrupo(h, g);
                    assigned[g] = true;

                    /**limit per a no saturar helicpoters */
                    if (state.getGruposHelicoptero(h).size() >= 6) break;
                }
            }
        }

        return state;
    }

    // distancia euclidiana
    private static double distance(Centro c, Grupo g) {

        double dx = c.getCoordX() - g.getCoordX();
        double dy = c.getCoordY() - g.getCoordY();

        return Math.sqrt(dx * dx + dy * dy);
    }
}
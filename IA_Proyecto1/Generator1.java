package IA_Proyecto1;

import IA.Desastres.*;
import java.util.ArrayList;

/**
 * 
 * Generador simple de l problema.
 * 
 * Es simple i se suposa que normalment dona una solucio suboptima
 * pera que doni espai al hill climbing a fer de les seves.
 * 
 * Basicament, com hem de prioritzar la gent de prioritat1, la idea del generador simple
 * es separar els grups en prio1 i prio2 i assignar primer els de prio 1, ja que
 * una de les coses a minimitzar era el temps de rescat de tosts els grupos de prio1.
 * 
 */

public class Generator1 {

    public static RescueStates generate(Grupos grupos, Centros centros) {

        /* Es construeix un estat buit */
        RescueStates state = new RescueStates(grupos, centros);

        int numHelis = centros.size();

        /* Es fa la separacion de grups per prioritat */
        ArrayList<Integer> prio1 = new ArrayList<>();
        ArrayList<Integer> prio2 = new ArrayList<>();
        for (int i = 0; i < grupos.size(); i++) {

            Grupo g = grupos.get(i);

            if (g.getPrioridad() == 1) {
                prio1.add(i);
            } else {
                prio2.add(i);
            }
        }

        int heli = 0;

        /* S'assignen de forma lineal un grup a un helicopter rotant una vegada acabats els 
        helicopters, fins a acabar els grups de prio1 */
        for (Integer g : prio1) {
            state.addGrupo(heli, g);
            heli = (heli + 1) % numHelis;
        }

        /* Lo mateix amb la prio 2 */
        for (Integer g : prio2) {
            state.addGrupo(heli, g);
            heli = (heli + 1) % numHelis;
        }

        return state;
    }
}
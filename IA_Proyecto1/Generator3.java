package IA_Proyecto1;

import IA.Desastres.*;
import java.util.ArrayList;

/**
 * Separa per prioritats i fica primer als de prioritat 1 a qualsevol helicòpter de forma cíclica.
 */

public class Generator3 {

    public static RescueStates generate(Grupos grupos, Centros centros) {

        RescueStates state = new RescueStates(grupos, centros);

        int numHelis = centros.size();

        ArrayList<Integer> prio1 = new ArrayList<>();
        ArrayList<Integer> prio2 = new ArrayList<>();
        /**SEPAREM PER PRIORITAT */
        for (int i = 0; i < grupos.size(); i++) {
            Grupo g = grupos.get(i);
            if (g.getPrioridad() == 1) prio1.add(i);
            else prio2.add(i);
        }

        int heli = 0;
        /**ASSIGNEM PRIORITAT 1 PRIMER */
        for (Integer g : prio1) {
            state.addGrup(heli, g);
            heli = (heli + 1) % numHelis;
        }

        for (Integer g : prio2) {
            state.addGrup(heli, g);
            heli = (heli + 1) % numHelis;
        }

        return state;
    }
}
package IA_Proyecto1;

import IA.Desastres.*;
import java.util.ArrayList;

/**
 * Separa els grups per prioritat en l'ordre que es troben i 
 * assigna primer els de prioritat 1 a un helicòpter de forma cíclica, 
 * seguit dels de prioritat 2.
 */
public class Generator5 {

    public static RescueStates generate(Grupos grups, Centros centres) {
        RescueStates state = new RescueStates(grups, centres);

        int numHelis = centres.size();
        
        ArrayList<Integer> prio1 = new ArrayList<>();
        ArrayList<Integer> prio2 = new ArrayList<>();

        for(int i = 0; i < grups.size(); ++i) {
            Grupo g = grups.get(i);
            if (g.getPrioridad() == 1) {
                prio1.add(i);
            } else {
                prio2.add(i);
            }
        }

        int heli = 0;

        for (int grupIndex : prio1) {
            state.addGrup(heli, grupIndex); 
            heli = (heli + 1) % numHelis;
        }

        for (int grupIndex : prio2) {
            state.addGrup(heli, grupIndex);
            heli = (heli + 1) % numHelis;
        }

        return state;
    }
}
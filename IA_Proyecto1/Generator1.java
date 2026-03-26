package IA_Proyecto1;

import IA.Desastres.*;
import java.util.ArrayList;

/**
 * Agafa grups en l'ordre que es troben i els assigna a un helicopter qualsevol
 */

public class Generator1 {

    public static RescueStates generate(Grupos grups, Centros centres) {
        RescueStates state = new RescueStates(grups, centres);

        int numHelis = centres.size();
        int heli = 0;

        for(int i = 0; i < grups.size(); ++i) {
            Grupo g = grups.get(i);
            state.addGrup(heli, i);
            heli = (heli + 1) % numHelis;
        }

        return state;
    }
}
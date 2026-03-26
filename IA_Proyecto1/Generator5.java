package IA_Proyecto1;

import IA.Desastres.*;
import java.util.*;

public class Generator5 {

    /**
     * ORDENA ELS GRUPS PER NOMBRE DE PASSATGERS (MÉS A MENYS) 
     * I ELS ASSIGNA CICLICAMENT ALS HELICÒPTERS
     */
    public static RescueStates generate(Grupos grups, Centros centres) {

        RescueStates state = new RescueStates(grups, centres);
        int numHelis = centres.size();

        List<Integer> indicesGrupos = new ArrayList<>();
        for (int i = 0; i < grups.size(); i++) {
            indicesGrupos.add(i);
        }

        indicesGrupos.sort((id1, id2) -> {
            int persones1 = grups.get(id1).getNPersonas();
            int persones2 = grups.get(id2).getNPersonas();
            return Integer.compare(persones2, persones1);
        });

        int heli = 0; 
        for (int grupoIndex : indicesGrupos) {
            state.addGrup(heli, grupoIndex); 
            heli = (heli + 1) % numHelis;
        }

        return state;
    }
}
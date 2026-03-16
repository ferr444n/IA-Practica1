package IA_Proyecto1;

import IA.Desastres.*;
import java.util.ArrayList;

/**
 * 
 * Generador aun mas simple.
 * genera los helicopteros y sus grupos sin
 * tener nada en cuenta. Solo los asigna sin más.
 * 
 * 
 */

public class Generator3 {

    public static RescueStates generate(Grupos grupos, Centros centros) {

        /* Es construeix un estat buit */
        RescueStates state = new RescueStates(grupos, centros);

        /** S'agafa el nombre d'helicopters */
        int numHelis = centros.size();
        int heli = 0;

        /** Es reparteixen equitativament sense cap precomput */
        for(int i = 0; i < grupos.size(); ++i)
        {
            Grupo g = grupos.get(i);
            state.addGrupo(heli, i);
            heli = (heli + 1) % numHelis;
        }

        return state;
    }
}
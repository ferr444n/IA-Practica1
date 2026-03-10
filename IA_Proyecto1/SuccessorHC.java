package IA_Proyecto1;

import aima.search.framework.Successor;
import aima.search.framework.SuccessorFunction;
import java.util.ArrayList;
import java.util.List;

/**
 * 
 * Funcio que fa els successors utilitzant el hillclimbing strategy.
 * 
 * 
 *  (experimentar utilitzant els altres operadors sobre grups)
 */
public class SuccessorHC implements SuccessorFunction {

    public List getSuccessors(Object state) {

        /**Es crea un array de tots els successors per a guardar. */
        ArrayList<Successor> successors = new ArrayList<>();
        /*Agafem una copa del estat actual. */
        RescueStates s = (RescueStates) state;
        int numHelis = s.getNumHelicopteros();

        /**Per a cada helicopter, s'agafa un del seu grup */
        for (int h1 = 0; h1 < numHelis; h1++) {

            for (int g1 : s.getGruposHelicoptero(h1)) {

                /** I es mou cap a un altre helicopter  */
                for (int h2 = 0; h2 < numHelis; h2++) {

                    if (h1 != h2) {

                        RescueStates newState = new RescueStates(s);

                        /**Es mou d'un a un altre */
                        newState.moveGrupo(g1, h1, h2);
                        successors.add(new Successor("move", newState));
                    }
                }
            }
        }

        return successors;
    }
}
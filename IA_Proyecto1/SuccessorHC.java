package IA_Proyecto1;

import aima.search.framework.Successor;
import aima.search.framework.SuccessorFunction;
import java.util.ArrayList;
import java.util.List;

/**
 * Funcio que fa els successors utilitzant el hillclimbing strategy.
 */
public class SuccessorHC implements SuccessorFunction {

    public List getSuccessors(Object state) {
        ArrayList<Successor> successors = new ArrayList<>();
        RescueStates s = (RescueStates) state;
        int numHelis = s.getNumHelicopteros();

        /**
        * ==========================================
        * OPERADOR 1: MOVE (Moure de posició/helicòpter)
        * ==========================================
        */
        for (int h1 = 0; h1 < numHelis; h1++) {
            int size1 = s.getGruposHelicoptero(h1).size();
            for (int p1 = 0; p1 < size1; p1++) {
                for (int h2 = 0; h2 < numHelis; h2++) {
                    int size2 = s.getGruposHelicoptero(h2).size();
                    for (int p2 = 0; p2 <= size2; p2++) {
                        if(h1 == h2 && (p2 == p1 || p2 == p1 + 1)) continue; // Evitem moure a la mateixa posició
                        RescueStates newState = new RescueStates(s);
                        newState.moveGrupo(h1, p1, h2, p2);
                        successors.add(new Successor("Move H" + h1 + " to H" + h2, newState));
                    }
                }
            }
        }

        /** 
         * ==========================================
         * OPERADOR 2: SWAP (Intercanvi entre rutes)
         * ==========================================
         */
        for (int h1 = 0; h1 < numHelis; h1++) {
            int size1 = s.getGruposHelicoptero(h1).size();
            for (int p1 = 0; p1 < size1; p1++) {
                for (int h2 = h1; h2 < numHelis; h2++) { 
                    int startP2 = (h1 == h2) ? p1 + 1 : 0;
                    int size2 = s.getGruposHelicoptero(h2).size();
                    
                    for (int p2 = startP2; p2 < size2; p2++) {
                        RescueStates newState = new RescueStates(s);
                        newState.swapGrupos(h1, p1, h2, p2);
                        successors.add(new Successor("Swap", newState));
                    }
                }
            }
        }
        return successors;
    }
}
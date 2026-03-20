

package IA_Proyecto1;

import aima.search.framework.Successor;
import aima.search.framework.SuccessorFunction;

import java.util.*;

public class SuccessorSA implements SuccessorFunction {

    /** Necessitem utilitzar la vuncio random 
     * per a escollir la operacio que utilitzarem 
     * de manera aleatoria
     * 
     * OJO: Simulated annealing nomes genera UN succesor de forma aleatoria!!!
     */
    private Random rand = new Random();

    public List getSuccessors(Object state) {

        /** Fem una copia del estat */
        RescueStates s = (RescueStates) state;
        RescueStates newState = new RescueStates(s); 

        int numHelis = s.getNumHelicopteros();

        /** S'escolleig un operador aleatori. */
        int operador = rand.nextInt(2);
        if (operador == 0) { 
            /** S'intercanvien de forma aleatoria */
            int h1 = rand.nextInt(numHelis);
            int h2 = rand.nextInt(numHelis);

            if (s.getGruposHelicoptero(h1).size() > 0 &&
                s.getGruposHelicoptero(h2).size() > 0) {
                int p1 = rand.nextInt(s.getGruposHelicoptero(h1).size());
                int p2 = rand.nextInt(s.getGruposHelicoptero(h2).size());
                newState.swapGrupos(h1, p1, h2, p2);
            }

        } else {
            /** De nou, s'agafen de forma aleatoria 
             */
            int h1 = rand.nextInt(numHelis);
            int h2 = rand.nextInt(numHelis);

            if (s.getGruposHelicoptero(h1).size() > 0) {

                int p1 = rand.nextInt(s.getGruposHelicoptero(h1).size());
                int p2 = rand.nextInt(s.getGruposHelicoptero(h2).size() + 1);

                newState.moveGrupo(h1, p1, h2, p2);
            }

        }

        List<Successor> ret = new ArrayList<>();
        ret.add(new Successor("SA neighbor", newState));

        return ret;
    }
}
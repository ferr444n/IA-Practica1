

package IA_Proyecto1;

import aima.search.framework.Successor;
import aima.search.framework.SuccessorFunction;

import java.util.*;

public class SuccessorSA implements SuccessorFunction {

    private Random rand = new Random();

    public List getSuccessors(Object state) {

        RescueStates s = (RescueStates) state;
        RescueStates newState = new RescueStates(s); 

        int numHelis = s.getNumHelicopters();

        int operador = rand.nextInt(2);
        if (operador == 0) { 
            /** S'ESCOLLEIXEN ALEATORIAMENT */
            int h1 = rand.nextInt(numHelis);
            int h2 = rand.nextInt(numHelis);

            if (s.getGrupsHelicopter(h1).size() > 0 && s.getGrupsHelicopter(h2).size() > 0) {
                int p1 = rand.nextInt(s.getGrupsHelicopter(h1).size());
                int p2 = rand.nextInt(s.getGrupsHelicopter(h2).size());
                newState.swap(h1, p1, h2, p2);
            }

        } else {
            /**S'ESCOLLEIXEN ALEATORIAMENT */
            int h1 = rand.nextInt(numHelis);
            int h2 = rand.nextInt(numHelis);

            if (s.getGrupsHelicopter(h1).size() > 0) {
                int p1 = rand.nextInt(s.getGrupsHelicopter(h1).size());
                int p2 = rand.nextInt(s.getGrupsHelicopter(h2).size() + 1);
                newState.move(h1, p1, h2, p2);
            }
        }

        List<Successor> ret = new ArrayList<>();
        ret.add(new Successor("SA neighbor", newState));

        return ret;
    }
}
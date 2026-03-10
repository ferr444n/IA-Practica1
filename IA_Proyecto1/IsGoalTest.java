package IA_Proyecto1;

import aima.search.framework.GoalTest;

/**Com es hillclimbing o sa, no es necessita cumplir cap 
 * situacio final, nomes ha d'arribar a un maxim o un minim,
 * aixi que aixo es bastant inutil.
 */
public class IsGoalTest implements GoalTest {

    public boolean isGoalState(Object state) {
        return false;
    }
}
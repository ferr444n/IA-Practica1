package IA_Proyecto1;

import aima.search.framework.HeuristicFunction;
import IA.Desastres.*;

import java.util.ArrayList;

public class Heuristic1 implements HeuristicFunction {

    /**Calcula el temps total a recollir tots els grupos.
     * No te en compte els de prioritat 1.
     */
    public double getHeuristicValue(Object state) {
    RescueStates s = (RescueStates) state;
    double tempsTotal = 0;
    Grupos grupos = s.getGrups();
    Centros centros = s.getCentros();
    int numHelis = s.getNumHelicopters();
    
    int CAPACITAT_MAX = 15; 
    int MAX_GRUPS_PER_VIATGE = 3; 

    for (int h = 0; h < numHelis; h++) {
        Centro c = centros.get(h);
        ArrayList<Integer> lista = s.getGrupsHelicopter(h);
        
        int personasEnHeli = 0;
        int gruposEnViajeActual = 0;
        Grupo anterior = null;

        for (int i = 0; i < lista.size(); i++) {
            Grupo g = grupos.get(lista.get(i));
            int numPersonas = g.getNPersonas();

            if (personasEnHeli + numPersonas > CAPACITAT_MAX || gruposEnViajeActual == MAX_GRUPS_PER_VIATGE) {
                // TORNEM A LA BASE
                tempsTotal += distanciaCentroGrupo(c, anterior) * (60.0 / 100.0);
                tempsTotal += 10; // Temps de descans
                
                // Reiniciem els valors per al nou viatge
                personasEnHeli = 0;
                gruposEnViajeActual = 0; // Reiniciem el comptador de grupos
                anterior = null;
            }

            // Anem a buscar el grup (des de la base o des de l'últim grup visitat)
            if (anterior == null) tempsTotal += distanciaCentroGrupo(c, g) * (60.0 / 100.0);
            else tempsTotal += distanciaGrupoGrupo(anterior, g) * (60.0 / 100.0);

            // Temps de rescat
            int prio = g.getPrioridad();
            if (prio == 1) tempsTotal += numPersonas * 2; 
            else tempsTotal += numPersonas;

            // Actualitzem l'estat per a la següent iteració
            personasEnHeli += numPersonas;
            gruposEnViajeActual++; // Afegim un grup al comptador del viatge actual
            anterior = g;
        }

        // Si hem acabat d'iterar tots els grupos però teníem un viatge a mitges...
        if (anterior != null) {
            // Cal tornar a la base definitivament per acabar la ruta
            tempsTotal += distanciaCentroGrupo(c, anterior) * (60.0 / 100.0);
        }

    }

    return tempsTotal;
}

    private double distanciaCentroGrupo(Centro c, Grupo g) {

        double dx = c.getCoordX() - g.getCoordX();
        double dy = c.getCoordY() - g.getCoordY();

        return Math.sqrt(dx * dx + dy * dy);
    }

    private double distanciaGrupoGrupo(Grupo g1, Grupo g2) {

    double dx = g1.getCoordX() - g2.getCoordX();
    double dy = g1.getCoordY() - g2.getCoordY();

    return Math.sqrt(dx * dx + dy * dy);
}
}

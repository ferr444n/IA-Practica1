package IA_Proyecto1;

import aima.search.framework.HeuristicFunction;
import IA.Desastres.*;

import java.util.ArrayList;

public class Heuristic3 implements HeuristicFunction {

    public double getHeuristicValue(Object state) {
        RescueStates s = (RescueStates) state;
        double tempsTotal = 0;
        Grupos grupos = s.getGrups();
        Centros centros = s.getCentros();
        int numHelis = s.getNumHelicopters();
        
        int CAPACITAT_MAX = 15; 
        int MAX_GRUPS_PER_VIATGE = 3; 

        // Vector per guardar el temps individual de cada helicòpter
        double[] tempsHelis = new double[numHelis];

        for (int h = 0; h < numHelis; h++) {
            Centro c = centros.get(h);
            ArrayList<Integer> lista = s.getGrupsHelicopter(h);
            
            int personasEnHeli = 0;
            int gruposEnViajeActual = 0;
            Grupo anterior = null;
            
            double tempsHeliActual = 0; // Temps només d'aquest helicòpter

            for (int i = 0; i < lista.size(); i++) {
                Grupo g = grupos.get(lista.get(i));
                int numPersonas = g.getNPersonas();

                if (personasEnHeli + numPersonas > CAPACITAT_MAX || gruposEnViajeActual == MAX_GRUPS_PER_VIATGE) {
                    tempsHeliActual += distanciaCentroGrupo(c, anterior) * (60.0 / 100.0);
                    tempsHeliActual += 10; 
                    
                    personasEnHeli = 0;
                    gruposEnViajeActual = 0; 
                    anterior = null;
                }

                if (anterior == null) tempsHeliActual += distanciaCentroGrupo(c, g) * (60.0 / 100.0);
                else tempsHeliActual += distanciaGrupoGrupo(anterior, g) * (60.0 / 100.0);

                int prio = g.getPrioridad();
                if (prio == 1) tempsHeliActual += numPersonas * 2; 
                else tempsHeliActual += numPersonas;

                personasEnHeli += numPersonas;
                gruposEnViajeActual++; 
                anterior = g;
            }

            if (anterior != null) {
                tempsHeliActual += distanciaCentroGrupo(c, anterior) * (60.0 / 100.0);
            }

            // Guardem el temps de l'helicòpter i l'acumulem al total
            tempsHelis[h] = tempsHeliActual;
            tempsTotal += tempsHeliActual;
        }

        // ==========================================
        // CÀLCUL DE L'ENTROPIA DE SHANNON
        // ==========================================
        double entropiaActual = 0.0;
        
        if (tempsTotal > 0) {
            for (int h = 0; h < numHelis; h++) {
                if (tempsHelis[h] > 0) { // Evitem el log(0)
                    double p_i = tempsHelis[h] / tempsTotal;
                    // Fórmula: - p * log2(p)
                    entropiaActual -= p_i * (Math.log(p_i) / Math.log(2)); 
                }
            }
        }

        // L'entropia màxima en base 2 per 'N' elements és log2(N)
        double entropiaMaxima = Math.log(numHelis) / Math.log(2);
        
        // Calculem com de lluny estem del balanç perfecte (0 = perfecte, >0 = desequilibrat)
        double desequilibri = entropiaMaxima - entropiaActual;

        // FACTOR DE PES: 
        // L'entropia és un valor molt petit (ex. entre 0 i 2.3 per a 5 helis). 
        // Multipliquem per un factor gran perquè la penalització afecti als minuts reals de l'heurística.
        double FACTOR_ENTROPIA = 1000.0; // Pots ajustar aquest valor als teus experiments

        // Retornem el temps total + la penalització per no ser equitatius
        return tempsTotal + (desequilibri * FACTOR_ENTROPIA);
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
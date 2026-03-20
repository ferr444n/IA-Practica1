package IA_Proyecto1;

import IA.Desastres.*;

import aima.search.framework.*;
import aima.search.informed.*;
import java.util.Random;

/*
    Para compilar el codigo desde.. creo que root:

    java -cp .:dist/AIMA.jar:src/IA/Desastres/Desastres.jar IA_Proyecto1.Main

    Y para ejecutar:

    javac -cp dist/AIMA.jar:src/IA/Desastres/Desastres.jar IA_Proyecto1/*.java

    (es posible que esten intercanviadas y que sea al reves, comprobarlo luego)

 */
public class Main {

    public static void main(String[] args) throws Exception {

        int numGrupos = 100;//(((int) System.currentTimeMillis())%1000) + 1;
        int numCentros = 5;//(((int) System.currentTimeMillis())%10) + 1;
        int numHelicopteros = 1; 

        /* Seed random. */
        int seed = -321518597; //(int) System.currentTimeMillis();
        System.out.println("Seed usada: " + seed);

        Grupos grupos = new Grupos(numGrupos, seed);
        Centros centros = new Centros(numCentros, numHelicopteros, seed);

        RescueStates estadoInicial = Generator1.generate(grupos, centros);

        System.out.println(estadoInicial.toString());

        Problem problem = new Problem(
                estadoInicial,
                /** HC o SA
                 */
                new SuccessorHC(),
                //new SuccessorSA(),
                new IsGoalTest(),
                new Heuristic2()
        );

        /* Merdes de l'aima */
        
        /** Comentar / descomentar els dos searchs per
         * cambiar de hilclimbing a SA
         */
        Search search = new HillClimbingSearch();

        //Search search = new SimulatedAnnealingSearch(
        //        100000, /** PASOS totales */
        //        100, /** Iteracions per temperatura */
        //        10, /** Temperatura inicial */
        //        0.005 /** Velocitat de refrigeracio */
        //);

        SearchAgent agent = new SearchAgent(problem, search);

        RescueStates resultado = (RescueStates) search.getGoalState();

        System.out.println(resultado.toString());
    }
}

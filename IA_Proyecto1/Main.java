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

        /* Nombre de grups es un nombre random entre 1 i 1000 */
        /* Pero es pot cambiar nomes modificant el nombre del modul  */
        int numGrupos = 100;//(((int) System.currentTimeMillis())%1000) + 1;
        /* Nombre de centres segons el modul tambe */
        int numCentros = 3;//(((int) System.currentTimeMillis())%10) + 1;

        /* Seed random. */
        int seed = 123456789;
        System.out.println("Seed usada: " + seed);

        /* Se usa la cosa de desastres para crear grupos randoms y centros randoms con 
        los parametros que hemos dicho antes */
        Grupos grupos = new Grupos(numGrupos, seed);
        Centros centros = new Centros(numCentros, 1, seed);

        /* Se genera el estado inicial (mirar de optimizar la data structure para luego) */
        RescueStates estadoInicial = Generator1.generate(grupos, centros);

        /* Es fa un print per a verue l'estat inicial */
        System.out.println(estadoInicial.toString());

        /* Es crea le problema amb tot lo necessari per a que aima faci la seva magia 
        que no entenc */
        Problem problem = new Problem(
                estadoInicial,
                new SuccessorHC(),
                new IsGoalTest(),
                new Heuristic1()
        );

        /* Merdes de l'aima */
        Search search = new HillClimbingSearch();
        SearchAgent agent = new SearchAgent(problem, search);

        /* Resultat de la execucio del hillclibing (posar el simulated annealing despue)  */
        RescueStates resultado = (RescueStates) search.getGoalState();

        /* Fem un altre pint del resultat. per a comparar els canvis */
        System.out.println(resultado.toString());
    }
}

package IA_Proyecto1;

import IA.Desastres.*;
import java.util.ArrayList;

public class RescueStates {

    
    private Grupos grupos;
    private Centros centros;

    /* +-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+ */
    /*                   Estructura de dades                         */
    /* +-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+ */

    /* Per a cada helicopter guardem la llista de grups als que va a recollir en ordre: 
    es a dir podem tenir [1,5,3,4,6,7,96] i els viatges seran el màxim de grups sencers
    seguits que poguem agafar amb un màxim de 3. */
    private ArrayList<ArrayList<Integer>> helicopteros;

    /* +-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+ */
    /*                    Constructors                               */
    /* +-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+ */

    public RescueStates(Grupos grupos, Centros centros) {
        this.grupos = grupos;
        this.centros = centros;
    
        int numHelicopteros = centros.getFirst().getNHelicopteros() * centros.size(); 
        helicopteros = new ArrayList<>();

        for (int i = 0; i < numHelicopteros; i++) 
            helicopteros.add(new ArrayList<>());
    }

    /* Constructor de còpia per a generar els successors a partir d'un estat inicial. */
    public RescueStates(RescueStates state) {
        this.grupos = state.grupos;
        this.centros = state.centros;

        helicopteros = new ArrayList<>();

        /* Es copien les rutes del estat inical. */
        for (ArrayList<Integer> lista : state.helicopteros) 
            helicopteros.add(new ArrayList<>(lista));
    }

    /* +-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+ */
    /*                          OPERADORS                            */
    /* +-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+ */

    /* Afegir un grup a la ruta d'un helicopter, al final de tot.*/
    public void addGrupo(int helicoptero, int grupo) {
        helicopteros.get(helicoptero).add(grupo);
    }

    /* Moure un grup d'una posició a una altra. Entre helicopters diferents o el mateix.*/
    public void moveGrupo(int heliOrigen, int posOrigen, int heliDestino, int posDestino) {
        int grupo = helicopteros.get(heliOrigen).remove(posOrigen);

        ArrayList<Integer> destino = helicopteros.get(heliDestino);
        if (posDestino > destino.size()) posDestino = destino.size();

        destino.add(posDestino, grupo);
    }

    /* Intercanviar dos grups dins del mateix helicopter o diferents */
    public void swapGrupos(int heli1, int pos1, int heli2, int pos2) {
        int grupo1 = helicopteros.get(heli1).get(pos1);
        int grupo2 = helicopteros.get(heli2).get(pos2);

        helicopteros.get(heli1).set(pos1, grupo2);
        helicopteros.get(heli2).set(pos2, grupo1);
    }


    /* +-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+ */
    /*                 GETTERS y SETTERS                             */
    /* +-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+ */

    public ArrayList<Integer> getGruposHelicoptero(int heli) {
        return helicopteros.get(heli);
    }

    public int getNumHelicopteros() {
        return helicopteros.size();
    }

    public Grupos getGrupos() {
        return grupos;
    }

    public Centros getCentros() {
        return centros;
    }

    @Override
    public String toString() {

        StringBuilder s = new StringBuilder();

        // Variables GLOBALS per a tota la missió
        double tiempoTotalMision = 0;
        double tiempoTotalPrio1 = 0;
        
        int CAPACIDAD_MAX = 15;
        int MAX_GRUPOS_POR_VIAJE = 3;

        for (int h = 0; h < helicopteros.size(); h++) {

            ArrayList<Integer> lista = helicopteros.get(h);
            Centro c = centros.get(h);

            s.append("Helicoptero ").append(h).append("\n");
            s.append("Grupos: ").append(lista).append("\n");

            s.append("Prioridad:\n");
            for (int i = 0; i < lista.size(); i++) {
                Grupo g = grupos.get(lista.get(i));
                s.append(g.getPrioridad()).append(" ");
            }
            s.append("\n");

            // Variables LOCALS només per a l'helicòpter actual
            double tiempoHeli = 0;
            double tiempoHeliPrio1 = 0;

            int personasEnHeli = 0;
            int gruposEnViajeActual = 0;
            Grupo anterior = null;

            for (int i = 0; i < lista.size(); i++) {
                Grupo g = grupos.get(lista.get(i));
                int numPersonas = g.getNPersonas();

                // 1. Comprovem si recollir aquest grup excedeix la capacitat O si ja portem 3 grups
                if (personasEnHeli + numPersonas > CAPACIDAD_MAX || gruposEnViajeActual == MAX_GRUPOS_POR_VIAJE) {
                    // TORNEM A LA BASE per finalitzar el viatge actual
                    tiempoHeli += distanciaCentroGrupo(c, anterior) * (60.0 / 100.0);
                    tiempoHeli += 10; // Temps de descans a la base

                    // Reiniciem els valors per al nou viatge
                    personasEnHeli = 0;
                    gruposEnViajeActual = 0;
                    anterior = null;
                }

                // 2. Anem a buscar el grup
                if (anterior == null) tiempoHeli += distanciaCentroGrupo(c, g) * (60.0 / 100.0);
                else tiempoHeli += distanciaGrupoGrupo(anterior, g) * (60.0 / 100.0);
                

                // 3. Temps de rescat
                int prio = g.getPrioridad();
                if (prio == 1) {
                    tiempoHeli += numPersonas * 2;
                    tiempoHeliPrio1 = tiempoHeli; // Guardem només el temps d'AQUEST helicòpter
                } else {
                    tiempoHeli += numPersonas;
                }

                // 4. Actualitzem l'estat per a la següent iteració
                personasEnHeli += numPersonas;
                gruposEnViajeActual++;
                anterior = g;
            }

            // Si hem acabat d'iterar tots els grups però teníem un viatge a mitges...
            if (anterior != null) {
                tiempoHeli += distanciaCentroGrupo(c, anterior) * (60.0 / 100.0);
                tiempoHeli += 10; // descans final
            }

            // Sumem el temps calculat d'aquest helicòpter als totals globals
            tiempoTotalMision += tiempoHeli;
            tiempoTotalPrio1 += tiempoHeliPrio1;

            // Mostrem les estadístiques individuals d'aquest helicòpter
            s.append("Tiempo de este helicoptero: ").append(tiempoHeli).append("\n");
            s.append("Tiempo hasta ultimo prio 1 (H").append(h).append("): ").append(tiempoHeliPrio1).append("\n\n");
        }

        s.append("===============================================\n");
        s.append("Tiempo TOTAL de la mision: ").append(tiempoTotalMision).append("\n");
        s.append("Tiempo TOTAL rescate prioridad 1: ").append(tiempoTotalPrio1).append("\n");
        s.append("===============================================\n");

        return s.toString();
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

package IA_Proyecto1;

import IA.Desastres.*;
import java.util.ArrayList;

public class RescueStates {

    // Header per copiar despres
    /* +-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+ */
 /*                                                               */
 /* +-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+ */
 /* +-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+ */
 /*                   Estructura de dades                         */
 /* +-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+ */
    private Grupos grupos;
    private Centros centros;

    // OPTIMITZAT
    /* Per a cada helicopter guardem la llista de grups als que va a recollir: es a dir
    podem tenir [1,5,3,4,6,7,96] i cada 3 elements, compta com un viatge. Aixi fem una 
    estructura de dades minima. Despres es pot calcular el temps de fer un viatge etc */
    private ArrayList<ArrayList<Integer>> helicopteros;

    /* +-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+ */
 /*                    Constructores                              */
 /* +-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+ */

 /* Es genera un nou estat amb els grups i centres donats */
    public RescueStates(Grupos grupos, Centros centros) {
        this.grupos = grupos;
        this.centros = centros;

        /* Tans helicopters com centres */
        int numHelicopteros = centros.size();
        helicopteros = new ArrayList<>();

        /* Es prepara un array buit per a cada viatge de cada helicopter */
        for (int i = 0; i < numHelicopteros; i++) {
            helicopteros.add(new ArrayList<>());
        }
    }

    /* Constructor, pero es copia, es copia un estat per a modificar.
    S¡utilitza per a generar fills. */
    public RescueStates(RescueStates state) {
        this.grupos = state.grupos;
        this.centros = state.centros;

        helicopteros = new ArrayList<>();

        /* Es copien les rutes del estat inical. */
        for (ArrayList<Integer> lista : state.helicopteros) {
            helicopteros.add(new ArrayList<>(lista));
        }
    }

    /* +-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+ */
 /*              Operaciones per a modificar el estat dels helic  */
 /* +-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+ */

 /* Asignar un grup a un vector d'un helicpoter */
 /* 
        H1 = [1,2,5,4] 
        addGrupo(H1,100)
        
        H1 = [1,2,5,4,100]
    
     */
    public void addGrupo(int helicoptero, int grupo) {
        helicopteros.get(helicoptero).add(grupo);
    }

    /* Moure un grup des d'una posició d'un helicòpter a una posició d'un altre */
    public void moveGrupo(int heliOrigen, int posOrigen, int heliDestino, int posDestino) {

        int grupo = helicopteros.get(heliOrigen).remove(posOrigen);

        ArrayList<Integer> destino = helicopteros.get(heliDestino);

        if (posDestino > destino.size()) {
            posDestino = destino.size();
        }

        destino.add(posDestino, grupo);
    }

    /* Intercanviar els grups que estan en dues posicions concretes */
    public void swapGrupos(int heli1, int pos1, int heli2, int pos2) {
        // Agafem els IDs dels grups
        int grupo1 = helicopteros.get(heli1).get(pos1);
        int grupo2 = helicopteros.get(heli2).get(pos2);

        // Els sobreescrivim en les posicions intercanviades
        helicopteros.get(heli1).set(pos1, grupo2);
        helicopteros.get(heli2).set(pos2, grupo1);
    }

    /**
     * Es fa swap de dos grups en la cua d'un mateix helicopter
     *
     * H1 [1,2,3,4,5]
     *
     * SwapMateixHelicopter(h1,2,3)
     *
     * H1 [1,3,2,4,5]
     */
    public void swapMateixHelicopter(int helic, int grup1, int grup2) {
        int g1 = helicopteros.get(helic).get(grup1);
        int g2 = helicopteros.get(helic).get(grup2);
        helicopteros.get(helic).set(grup1, g2);
        helicopteros.get(helic).set(grup2, g1);
    }

    /* +-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+ */
 /*                 GETTERS y SETTERS                             */
 /* +-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+ */

 /* Obtenir els grups d'un helicopter especific */
    public ArrayList<Integer> getGruposHelicoptero(int heli) {
        return helicopteros.get(heli);
    }

    /* Get el tamany del vector helicopters */
    public int getNumHelicopteros() {
        return helicopteros.size();
    }

    /* Retorna els grups a rescatar del problema */
    public Grupos getGrupos() {
        return grupos;
    }

    /* Retorna els centres de rescat del problema */
    public Centros getCentros() {
        return centros;
    }

    /* +-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+ */
 /*                      Chivatos para ver el problema y el estado*/
 /* +-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+ */
    @Override
    public String toString() {

        StringBuilder s = new StringBuilder();

        double tiempoTotal = 0;
        double tiempoTotalPrio1 = 0;

        for (int h = 0; h < helicopteros.size(); h++) {

            ArrayList<Integer> lista = helicopteros.get(h);
            Centro c = centros.get(h);

            s.append("Helicoptero ").append(h).append("\n");
            s.append("Grupos: ").append(lista).append("\n");

            double tiempoHeli = 0;
            double tiempoHastaUltimoPrio1 = 0;

            s.append("Prioridad:\n");
            for (int i = 0; i < lista.size(); i++) {
                Grupo g = grupos.get(lista.get(i));
                s.append(g.getPrioridad()).append(" ");
            }
            s.append("\n");

            for (int i = 0; i < lista.size(); i += 3) {

                double tiempoRuta = 0;

                Grupo g1 = grupos.get(lista.get(i));

                /* centro -> primer grupo */
                tiempoRuta += distanciaCentroGrupo(c, g1) * 60.0 / 100.0;

                int prio = g1.getPrioridad();
                int personas = g1.getNPersonas();

                tiempoRuta += (prio == 1) ? personas * 2 : personas;

                if (prio == 1) {
                    tiempoHastaUltimoPrio1 = tiempoHeli + tiempoRuta;
                }

                Grupo anterior = g1;

                for (int j = 1; j < 3 && i + j < lista.size(); j++) {

                    Grupo g = grupos.get(lista.get(i + j));

                    tiempoRuta += distanciaGrupoGrupo(anterior, g) * 60.0 / 100.0;

                    prio = g.getPrioridad();
                    personas = g.getNPersonas();

                    tiempoRuta += (prio == 1) ? personas * 2 : personas;

                    if (prio == 1) {
                        tiempoHastaUltimoPrio1 = tiempoHeli + tiempoRuta;
                    }

                    anterior = g;
                }

                /* último grupo -> centro */
                tiempoRuta += distanciaCentroGrupo(c, anterior) * 60.0 / 100.0;

                /* descanso */
                tiempoRuta += 10;

                tiempoHeli += tiempoRuta;
            }

            s.append("Tiempo helicoptero: ").append(tiempoHeli).append("\n");
            s.append("Tiempo ultimo rescate prioridad 1: ").append(tiempoHastaUltimoPrio1).append("\n\n");

            if (tiempoHeli > tiempoTotal) {
                tiempoTotal = tiempoHeli;
            }

            if (tiempoHastaUltimoPrio1 > tiempoTotalPrio1) {
                tiempoTotalPrio1 = tiempoHastaUltimoPrio1;
            }
        }

        s.append("Tiempo total mision: ").append(tiempoTotal).append("\n");
        s.append("Tiempo total rescate prioridad 1: ").append(tiempoTotalPrio1).append("\n");

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

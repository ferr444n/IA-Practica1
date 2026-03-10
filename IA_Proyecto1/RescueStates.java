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

    /* Moure un grup d'un helicopter a un altre */
 /*
        H1 = [1,2,3]
        H5 = []
        moveGrupo(2,H1,H5)
        H1 = [1,3]
        H5 = [2]
     */
    public void moveGrupo(int grupo, int heliOrigen, int heliDestino) {

        helicopteros.get(heliOrigen).remove(Integer.valueOf(grupo));
        helicopteros.get(heliDestino).add(grupo);
    }

    /* Intercambiar grupos entre dos helicopteros */
 /*
        H1 = [1,2,3]
        H5 = [6,100]
        swapGrupos(2,H1,6,H5)
        H1 = [1,3,100]
        H5 = [6,2]
     */
    public void swapGrupos(int grupo1, int heli1, int grupo2, int heli2) {

        helicopteros.get(heli1).remove(Integer.valueOf(grupo1));
        helicopteros.get(heli2).remove(Integer.valueOf(grupo2));

        helicopteros.get(heli1).add(grupo2);
        helicopteros.get(heli2).add(grupo1);
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

        /** Construimos un string enorme con toda la info */

        StringBuilder s = new StringBuilder();
        double tiempoTotal = 0;

        for (int h = 0; h < helicopteros.size(); h++) {

            ArrayList<Integer> lista = helicopteros.get(h);
            Centro c = centros.get(h);

            s.append("Helicoptero ").append(h).append("\n");
            s.append("Grupos: ").append(lista).append("\n");

            double tiempoHeli = 0;

            s.append("Prioridad:\n");
            for (int i = 0; i < lista.size(); i++) {
                Grupo g = grupos.get(lista.get(i));
                s.append(g.getPrioridad()).append(" ");
            }
            s.append("\n");

            /** Pillamos rutas de tres en tres */
            for (int i = 0; i < lista.size(); i += 3) {

                double tiempoRuta = 0;

                Grupo g1 = grupos.get(lista.get(i));

                /* tiempo del centro 2 primer grupo */
                tiempoRuta += distanciaCentroGrupo(c, g1) * 60.0 / 100.0;

                int prio = g1.getPrioridad();
                int personas = g1.getNPersonas();

                tiempoRuta += (prio == 1) ? personas * 2 : personas;

                Grupo anterior = g1;

                /* Tiempo de grupo a grupo siguiente */
                for (int j = 1; j < 3 && i + j < lista.size(); j++) {

                    Grupo g = grupos.get(lista.get(i + j));

                    tiempoRuta += distanciaGrupoGrupo(anterior, g) * 60.0 / 100.0;

                    prio = g.getPrioridad();
                    personas = g.getNPersonas();

                    tiempoRuta += (prio == 1) ? personas * 2 : personas;

                    anterior = g;
                }

                /* Last grupo de nuevo hacia el centro centro */
                tiempoRuta += distanciaCentroGrupo(c, anterior) * 60.0 / 100.0;

                /* descanso entre rescates*/
                tiempoRuta += 10;

                tiempoHeli += tiempoRuta;
            }

            s.append("Tiempo helicoptero: ").append(tiempoHeli).append("\n\n");

            if (tiempoHeli > tiempoTotal) {
                tiempoTotal = tiempoHeli;
            }
        }

        s.append("Tiempo total mision: ").append(tiempoTotal).append("\n");

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

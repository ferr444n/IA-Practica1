package IA_Proyecto1;

import IA.Desastres.*;
import java.util.ArrayList;

public class RescueStates {

    private Grupos grups;
    private Centros centres;

    /* Per a cada helicopter guardem la llista de grups als que va a recollir en ordre: 
    es a dir podem tenir [1,5,3,4,6,7,96] i els viatges seran el màxim de grups sencers
    seguits que poguem agafar amb un màxim de 3. */
    private ArrayList<ArrayList<Integer>> helicopters;

    public RescueStates(Grupos grups, Centros centres) {
        this.grups = grups;
        this.centres = centres;

        int numHelicopteros = centres.getFirst().getNHelicopteros() * centres.size();
        helicopters = new ArrayList<>();

        for (int i = 0; i < numHelicopteros; i++) {
            helicopters.add(new ArrayList<>());
        }
    }

    /* Constructor de còpia per a generar els successors a partir d'un estat inicial. */
    public RescueStates(RescueStates state) {
        this.grups = state.grups;
        this.centres = state.centres;

        helicopters = new ArrayList<>();

        for (ArrayList<Integer> llista : state.helicopters) {
            helicopters.add(new ArrayList<>(llista));
        }
    }

    /* Afegir un grup a la ruta d'un helicopter, al final de tot.*/
    public void addGrup(int helicopter, int grup) {
        helicopters.get(helicopter).add(grup);
    }

    /* Moure un grup d'una posició a una altra. Entre helicopters diferents o el mateix.*/
    public void move(int heliOrigen, int posOrigen, int heliDesti, int posDesti) {
        int grup = helicopters.get(heliOrigen).remove(posOrigen);

        ArrayList<Integer> destino = helicopters.get(heliDesti);
        if (posDesti > destino.size()) {
            posDesti = destino.size();
        }

        destino.add(posDesti, grup);
    }

    /* Intercanviar dos grups dins del mateix helicopter o diferents */
    public void swap(int heli1, int pos1, int heli2, int pos2) {
        int grup1 = helicopters.get(heli1).get(pos1);
        int grup2 = helicopters.get(heli2).get(pos2);

        helicopters.get(heli1).set(pos1, grup2);
        helicopters.get(heli2).set(pos2, grup1);
    }

    public ArrayList<Integer> getGrupsHelicopter(int heli) {
        return helicopters.get(heli);
    }

    public int getNumHelicopters() {
        return helicopters.size();
    }

    public Grupos getGrups() {
        return grups;
    }

    public Centros getCentros() {
        return centres;
    }

    @Override
    public String toString() {
        StringBuilder s = new StringBuilder();

        double tempsTotalMisio = 0;
        double tempsTotalPrio1 = 0;

        int CAPACITAT_MAX = 15;
        int MAX_GRUPS_PER_VIATGE = 3;

        /**
         * PER CADA HELICOPTER
         */
        for (int h = 0; h < helicopters.size(); h++) {

            ArrayList<Integer> llista = helicopters.get(h);
            int helicPorCentro = centres.get(0).getNHelicopteros();
            Centro c = centres.get(h / helicPorCentro);

            for (int i = 0; i < llista.size(); i++) {
                Grupo g = grups.get(llista.get(i));
            }

            double tempsHeli = 0;
            double tempsHeliPrio1 = 0;

            int personesHeli = 0;
            int grupsViatgeActual = 0;
            Grupo anterior = null;

            /**
             * PER CADA GRUP
             */
            for (int i = 0; i < llista.size(); i++) {
                Grupo g = grups.get(llista.get(i));
                int numPersonas = g.getNPersonas();

                /**
                 * COMPROVEM SI RECOLLIR AQUEST GRUP EXCEDEIX LA CAPACITAT O EL
                 * LÍMIT DE 3 GRUPS
                 */
                if (personesHeli + numPersonas > CAPACITAT_MAX || grupsViatgeActual == MAX_GRUPS_PER_VIATGE) {
                    /**
                     * TORNEM A LA BASE
                     */
                    tempsHeli += distanciaCentreGrup(c, anterior) * (60.0 / 100.0);
                    tempsHeli += 10;
                    /**
                     * TEMPS DE DESCANS
                     */

                    /**
                     * REINICIEM VALORS PEL SEGÜENT VIATGE
                     */
                    personesHeli = 0;
                    grupsViatgeActual = 0;
                    anterior = null;
                }

                /**
                 * ANEM A BUSCAR AL GRUP
                 */
                if (anterior == null) {
                    tempsHeli += distanciaCentreGrup(c, g) * (60.0 / 100.0); 
                }else {
                    tempsHeli += distanciaGrupGrup(anterior, g) * (60.0 / 100.0);
                }

                /**
                 * TEMPS DE RESCAT
                 */
                int prio = g.getPrioridad();
                if (prio == 1) {
                    tempsHeli += numPersonas * 2;
                    tempsHeliPrio1 = tempsHeli;
                } else {
                    tempsHeli += numPersonas;
                }

                personesHeli += numPersonas;
                grupsViatgeActual++;
                anterior = g;
            }

            /**
             * SI HEM ACABAT I TENIEM UN VIATGE A MITGES
             */
            if (anterior != null) {
                tempsHeli += distanciaCentreGrup(c, anterior) * (60.0 / 100.0);
            }

            /**
             * SUMEM AQUEST HELICOPTER AL TOTAL
             */
            tempsTotalMisio += tempsHeli;
            tempsTotalPrio1 += tempsHeliPrio1;
        }

        s.append(tempsTotalMisio);

        return s.toString();
    }

    private double distanciaCentreGrup(Centro c, Grupo g) {

        double dx = c.getCoordX() - g.getCoordX();
        double dy = c.getCoordY() - g.getCoordY();

        return Math.sqrt(dx * dx + dy * dy);
    }

    private double distanciaGrupGrup(Grupo g1, Grupo g2) {

        double dx = g1.getCoordX() - g2.getCoordX();
        double dy = g1.getCoordY() - g2.getCoordY();

        return Math.sqrt(dx * dx + dy * dy);
    }
}

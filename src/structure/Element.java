package structure;

import java.io.FileNotFoundException;

/**
 * Created by Dell on 04.11.2017.
 */

public class Element {

    Globaldata data;

    Node ND[];
    Powierzchnia POW[];

    int globalNodeID[];

    private int n_pow; // liczba powierzchni na których jest warunek brzegowy
    private int[] a_pow; // lokalne numery powierzchni kontaktowych elementu


    public Element(int i,int j, Node[] nodes) throws FileNotFoundException {
        ND=new Node[4];
        POW=new Powierzchnia[4];
        globalNodeID =new int[4];
        data=Globaldata.getInstance();

        //współrzędne węzłów w elemencie
        ND[0] = nodes[0];
        ND[1] = nodes[1];
        ND[2] = nodes[2];
        ND[3] = nodes[3];

        //globalne ID węzłów w elemencie
        globalNodeID[0] = nodes[0].getLicznik();
        globalNodeID[1] = nodes[1].getLicznik();
        globalNodeID[2] = nodes[2].getLicznik();
        globalNodeID[3] = nodes[3].getLicznik();

        //wezły na powierzchniach
        POW[0] = new Powierzchnia(ND[3], ND[0]);
        POW[1] = new Powierzchnia(ND[0], ND[1]);
        POW[2] = new Powierzchnia(ND[1], ND[2]);
        POW[3] = new Powierzchnia(ND[2], ND[3]);

        //liczenie ilości powierzchni w elemencie na których jest warunek brzegowy
        n_pow = 0;
        for (int k = 0; k < 4; k++) {
            //sprawdzanie czy na powierzchni jest warunek brzegowy
            if (POW[k].getNodes()[0].isStatus() && POW[k].getNodes()[1].isStatus()) {
                n_pow++;
            }
        }
        a_pow = new int[n_pow];

        int counter = 0;
        for (int k = 0; k < 4; k++) {
            if (POW[k].getNodes()[0].isStatus() && POW[k].getNodes()[1].isStatus()) {
                a_pow[counter++] = k;
            }
        }
    }

    public int getN_pow() {
        return n_pow;
    }
    public int[] getA_pow() {
        return a_pow;
    }
}

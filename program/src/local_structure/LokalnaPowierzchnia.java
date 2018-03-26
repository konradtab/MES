package local_structure;

/**
 * Created by Dell on 28.12.2017.
 */

public class LokalnaPowierzchnia {

    public final LokalnyNode[] ND;

    //tablica funkcji kształtu
    public final double N[][];

    public LokalnaPowierzchnia(LokalnyNode node1, LokalnyNode node2) {
        ND = new LokalnyNode[2];

        ND[0] = node1;
        ND[1] = node2;

        //2 punkty całkowania i 4 funkcje kształtu dla powierzchni
        N = new double[2][4];
    }

}

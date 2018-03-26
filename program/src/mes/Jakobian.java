package mes;

import local_structure.LokalnyElement;

/**
 * Created by Dell on 28.12.2017.
 */

public class Jakobian {
    private double J[][];
    private double J_inverted[][];
    private double det;
    private int pktCalkowania;
    public static LokalnyElement lokalnyElement2p=LokalnyElement.getInstance();


    public Jakobian(int punkt, double x[], double y[]) {

        this.pktCalkowania = punkt;

        J = new double[2][2];
        J[0][0] = lokalnyElement2p.getdN_dKsi()[pktCalkowania][0] * x[0] + lokalnyElement2p.getdN_dKsi()[pktCalkowania][1] * x[1] + lokalnyElement2p.getdN_dKsi()[pktCalkowania][2] * x[2] + lokalnyElement2p.getdN_dKsi()[pktCalkowania][3] * x[3];
        J[0][1] = lokalnyElement2p.getdN_dKsi()[pktCalkowania][0] * y[0] + lokalnyElement2p.getdN_dKsi()[pktCalkowania][1] * y[1] + lokalnyElement2p.getdN_dKsi()[pktCalkowania][2] * y[2] + lokalnyElement2p.getdN_dKsi()[pktCalkowania][3] * y[3];
        J[1][0] = lokalnyElement2p.getdN_dEta()[pktCalkowania][0] * x[0] + lokalnyElement2p.getdN_dEta()[pktCalkowania][1] * x[1] + lokalnyElement2p.getdN_dEta()[pktCalkowania][2] * x[2] + lokalnyElement2p.getdN_dEta()[pktCalkowania][3] * x[3];
        J[1][1] = lokalnyElement2p.getdN_dEta()[pktCalkowania][0] * y[0] + lokalnyElement2p.getdN_dEta()[pktCalkowania][1] * y[1] + lokalnyElement2p.getdN_dEta()[pktCalkowania][2] * y[2] + lokalnyElement2p.getdN_dEta()[pktCalkowania][3] * y[3];

        det = J[0][0] * J[1][1] - J[0][1] * J[1][0];

        J_inverted = new double[2][2];

        J_inverted[0][0] = J[1][1];
        J_inverted[0][1] = -J[0][1];
        J_inverted[1][0] = -J[1][0];
        J_inverted[1][1] = J[0][0];
    }

    public void wypiszJakobian() {
        System.out.println("Jakobian dla punktu calkowania: " + pktCalkowania);
        for (int i = 0; i < 2; i++) {
            for (int j = 0; j < 2; j++) {
                System.out.print(+J[i][j] + "\t");
            }
            System.out.println("");
        }
        System.out.println("Det: " + det + "\n");
    }
    public double getDet() {
        return det;
    }
    public double[][] getJ_inverted() {
        return J_inverted;
    }
}
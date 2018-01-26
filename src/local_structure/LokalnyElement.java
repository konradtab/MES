package local_structure;

/**
 * Created by Dell on 28.12.2017.
 */

public class LokalnyElement {

    LokalnyNode[] lokalnyNodes = { //punkty calkowania gaussa
            new LokalnyNode(-1.0 / Math.sqrt(3.0), -1.0 / Math.sqrt(3.0)),
            new LokalnyNode(1.0 / Math.sqrt(3.0), -1.0 / Math.sqrt(3.0)),
            new LokalnyNode(1.0 / Math.sqrt(3.0), 1.0 / Math.sqrt(3.0)),
            new LokalnyNode(-1.0 / Math.sqrt(3.0), 1.0 / Math.sqrt(3.0))
    };

    LokalnaPowierzchnia[] lokalnaPowierzchnias = { //punkty calkowania gaussa dla powierzchni
            new LokalnaPowierzchnia(new LokalnyNode(-1.0, 1.0 / Math.sqrt(3.0)), new LokalnyNode(-1.0, -1.0 / Math.sqrt(3.0))),
            new LokalnaPowierzchnia(new LokalnyNode(-1.0 / Math.sqrt(3.0), -1.0), new LokalnyNode(1.0 / Math.sqrt(3.0), -1.0)),
            new LokalnaPowierzchnia(new LokalnyNode(1.0, -1.0 / Math.sqrt(3.0)), new LokalnyNode(1.0, 1.0 / Math.sqrt(3.0))),
            new LokalnaPowierzchnia(new LokalnyNode(1.0 / Math.sqrt(3.0), 1.0), new LokalnyNode(-1.0 / Math.sqrt(3.0), 1.0))
    };

    //pochodne funkcji kształtu
    double dN_dKsi[][];
    double dN_dEta[][];

    //funkcja kształtu dla elementu dla objętości
    double N[][];

    private static LokalnyElement lokalnyElement = null;

    public LokalnyElement() {

        dN_dKsi = new double[4][4];
        dN_dEta = new double[4][4];
        N = new double[4][4];

        for (int i = 0; i < 4; i++) { //wypelniamy macierze funkcji ksztaltu
            N[i][0] = N1(lokalnyNodes[i].getKsi(), lokalnyNodes[i].getEta());
            N[i][1] = N2(lokalnyNodes[i].getKsi(), lokalnyNodes[i].getEta());
            N[i][2] = N3(lokalnyNodes[i].getKsi(), lokalnyNodes[i].getEta());
            N[i][3] = N4(lokalnyNodes[i].getKsi(), lokalnyNodes[i].getEta());

            //pochodne macierzy funkcji kształtu po ksi
            dN_dKsi[i][0] = dN1dKsi(lokalnyNodes[i].getKsi(), lokalnyNodes[i].getEta());
            dN_dKsi[i][1] = dN2dKsi(lokalnyNodes[i].getKsi(), lokalnyNodes[i].getEta());
            dN_dKsi[i][2] = dN3dKsi(lokalnyNodes[i].getKsi(), lokalnyNodes[i].getEta());
            dN_dKsi[i][3] = dN4dKsi(lokalnyNodes[i].getKsi(), lokalnyNodes[i].getEta());

            //pochodne macierzy funkcji kształtu po eta
            dN_dEta[i][0] = dN1dEta(lokalnyNodes[i].getKsi(), lokalnyNodes[i].getEta());
            dN_dEta[i][1] = dN2dEta(lokalnyNodes[i].getKsi(), lokalnyNodes[i].getEta());
            dN_dEta[i][2] = dN3dEta(lokalnyNodes[i].getKsi(), lokalnyNodes[i].getEta());
            dN_dEta[i][3] = dN4dEta(lokalnyNodes[i].getKsi(), lokalnyNodes[i].getEta());

        }

        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 2; j++) { //macierze funkcji ksztaltu dla powierzchni
                lokalnaPowierzchnias[i].N[j][0] = N1(lokalnaPowierzchnias[i].ND[j].getKsi(), lokalnaPowierzchnias[i].ND[j].getEta());
                lokalnaPowierzchnias[i].N[j][1] = N2(lokalnaPowierzchnias[i].ND[j].getKsi(), lokalnaPowierzchnias[i].ND[j].getEta());
                lokalnaPowierzchnias[i].N[j][2] = N3(lokalnaPowierzchnias[i].ND[j].getKsi(), lokalnaPowierzchnias[i].ND[j].getEta());
                lokalnaPowierzchnias[i].N[j][3] = N4(lokalnaPowierzchnias[i].ND[j].getKsi(), lokalnaPowierzchnias[i].ND[j].getEta());
            }
        }
    }

    public static LokalnyElement getInstance() {
        if (lokalnyElement == null) {
            lokalnyElement = new LokalnyElement();
        }
        return lokalnyElement;
    }

    private double N1(double ksi, double eta) {
        return 0.25 * (1 - ksi) * (1 - eta);
    }
    private double N2(double ksi, double eta) {
        return 0.25 * (1 + ksi) * (1 - eta);
    }
    private double N3(double ksi, double eta) {
        return 0.25 * (1 + ksi) * (1 + eta);
    }
    private double N4(double ksi, double eta) {
        return 0.25 * (1 - ksi) * (1 + eta);
    }
    private double dN1dKsi(double ksi, double eta) {
        return -0.25 * (1 - eta);
    }
    private double dN2dKsi(double ksi, double eta) {
        return 0.25 * (1 - eta);
    }
    private double dN3dKsi(double ksi, double eta) {
        return 0.25 * (1 + eta);
    }
    private double dN4dKsi(double ksi, double eta) {
        return -0.25 * (1 + eta);
    }
    private double dN1dEta(double ksi, double eta) {
        return -0.25 * (1 - ksi);
    }
    private double dN2dEta(double ksi, double eta) {
        return -0.25 * (1 + ksi);
    }
    private double dN3dEta(double ksi, double eta) {
        return 0.25 * (1 + ksi);
    }
    private double dN4dEta(double ksi, double eta) {
        return 0.25 * (1 - ksi);
    }

    public double[][] getdN_dKsi() {
        return dN_dKsi;
    }
    public double[][] getdN_dEta() {
        return dN_dEta;
    }
    public double[][] getN() {
        return N;
    }
    public LokalnaPowierzchnia[] getPOW() {
        return lokalnaPowierzchnias;
    }
    }

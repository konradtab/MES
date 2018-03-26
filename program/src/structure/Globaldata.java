package structure;

import local_structure.LokalnyElement;
import mes.Jakobian;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

/**
 * Created by Dell on 04.11.2017.
 */

public class Globaldata {
    double H, B;        //wysokość i szerokość siatki
    int nH, nB;         //liczba elementów na wysokości i szerokości siatki
    int nh, ne;         //liczba węzłów i liczba elementów

    double t_begin; //temperatura poczatkowa
    double tau; //czas procesu
    double dtau; //poczatkowa wartosc przyrostu czasu
    double t_otoczenia; //temperatura otoczenia
    double alfa; //wspolczynnik wymiany ciepla
    double c; //cieplo wlasciwe
    double k; //wspolczynnik przewodzenia ciepla
    double ro; //gestosc

    LokalnyElement el_lok;
    double[][] H_current;
    double[] P_current;
    double[][] H_global;
    double[] P_global;

    private static Globaldata globalData;

    public Globaldata() throws FileNotFoundException {
        Scanner input = new Scanner(new File("data.txt"));
        this.H = input.nextDouble();
        input.findInLine(";");
        this.B = input.nextDouble();
        input.findInLine(";");
        this.nH = input.nextInt();
        input.findInLine(";");
        this.nB = input.nextInt();

        ne = (nH - 1) * (nB - 1);
        nh = nH * nB;

        input.findInLine(";");
        this.t_begin = input.nextDouble();
        input.findInLine(";");
        this.tau = input.nextDouble();
        input.findInLine(";");
        this.dtau = input.nextDouble();
        input.findInLine(";");
        this.t_otoczenia = input.nextDouble();
        input.findInLine(";");
        this.alfa = input.nextDouble();
        input.findInLine(";");
        this.c = input.nextDouble();
        input.findInLine(";");
        this.k = input.nextDouble();
        input.findInLine(";");
        this.ro = input.nextDouble();


        el_lok = new LokalnyElement();
        H_current = new double[4][4];
        P_current = new double[4];
        H_global = new double[nh][nh];
        P_global = new double[nh];
    }

    public void compute() throws FileNotFoundException {

        //zerowanie macierzy
        for (int i = 0; i < nh; i++) {
            for (int j = 0; j < nh; j++) {
                H_global[i][j] = 0;
            }
            P_global[i] = 0;
        }

        Grid grid = Grid.getInstance();
        Jakobian jakobian;

        //szukane(pochodne funkcji kształtu po x i y)
        double[] dNdx = new double[4];
        double[] dNdy = new double[4];

        //współrzędne nodów z elementu (globalne)
        double[] x = new double[4];
        double[] y = new double[4];

        //temperatura początkowa
        double[] temp_0 = new double[4];
        //t0p-temp zinterpolowana z węzłów do punktów całkowania
        //cij-macierz C w konkretnej komórce i,j
        double t0p, cij;
        int id;
        double detj = 0;

        //pętla po wszystkich elementach w siatce(16)
        for (int el_nr = 0; el_nr < ne; el_nr++) {

            //zerowanie macierzy
            for (int i = 0; i < 4; i++) {
                for (int j = 0; j < 4; j++) {
                    H_current[i][j] = 0;
                }
                P_current[i] = 0;
            }

            //wyciagnięcie danych z siatki
            for (int i = 0; i < 4; i++) {
                id = grid.elemEL[el_nr].globalNodeID[i];
                x[i] = grid.nodeNB[id].getX();
                y[i] = grid.nodeNB[id].getY();
                temp_0[i] = grid.nodeNB[id].getT();
            }

            //pc-punkty całkowania(sposób dwupunktowy)
            for (int pc = 0; pc < 4; pc++) { // 4 - liczba punktow calkowania po powierzchni w elemencie
                jakobian = new Jakobian(pc, x, y);
                t0p = 0;

                //pętla po węzłach
                for (int j = 0; j < 4; j++) { // 4 - liczba wezlow w wykorzystywanym elemencie skonczonym
                    //wzór z zajęć
                    dNdx[j] = 1.0 / jakobian.getDet() * (jakobian.getJ_inverted()[0][0] * el_lok.getdN_dKsi()[pc][j]
                            + jakobian.getJ_inverted()[0][1] * el_lok.getdN_dEta()[pc][j]);

                    dNdy[j] = 1.0 / jakobian.getDet() * (jakobian.getJ_inverted()[1][0] * el_lok.getdN_dKsi()[pc][j]
                            + jakobian.getJ_inverted()[1][1] * el_lok.getdN_dEta()[pc][j]);

                    //interpolacja
                    t0p += temp_0[j] * el_lok.getN()[pc][j];
                }

                //wartość wyznacznika
                detj = Math.abs(jakobian.getDet());
                //dla każdego punktu całkowania macierz 4x4
                for (int i = 0; i < 4; i++) {
                    for (int j = 0; j < 4; j++) {
                        cij = c * ro * el_lok.getN()[pc][i] * el_lok.getN()[pc][j] * detj;
                        H_current[i][j] += k * (dNdx[i] * dNdx[j] + dNdy[i] * dNdy[j]) * detj + cij / dtau;
                        P_current[i] += cij / dtau * t0p;
                    }
                }
            }
            //warunki brzegowe
            for (int ipow = 0; ipow < grid.elemEL[el_nr].getN_pow(); ipow++) {
                //id powierzchni
                id = grid.elemEL[el_nr].getA_pow()[ipow];
                switch (id) {
                    case 0:
                        //detj=delta x/2 , dla 1D
                        detj = Math.sqrt(Math.pow(grid.elemEL[el_nr].ND[3].getX() - grid.elemEL[el_nr].ND[0].getX(), 2)
                                + Math.pow(grid.elemEL[el_nr].ND[3].getY() - grid.elemEL[el_nr].ND[0].getY(), 2)) / 2.0;
                        break;
                    case 1:
                        detj = Math.sqrt(Math.pow(grid.elemEL[el_nr].ND[0].getX() - grid.elemEL[el_nr].ND[1].getX(), 2)
                                + Math.pow(grid.elemEL[el_nr].ND[0].getY() - grid.elemEL[el_nr].ND[1].getY(), 2)) / 2.0;
                        break;
                    case 2:
                        detj = Math.sqrt(Math.pow(grid.elemEL[el_nr].ND[1].getX() - grid.elemEL[el_nr].ND[2].getX(), 2)
                                + Math.pow(grid.elemEL[el_nr].ND[1].getY() - grid.elemEL[el_nr].ND[2].getY(), 2)) / 2.0;
                        break;
                    case 3:
                        detj = Math.sqrt(Math.pow(grid.elemEL[el_nr].ND[2].getX() - grid.elemEL[el_nr].ND[3].getX(), 2)
                                + Math.pow(grid.elemEL[el_nr].ND[2].getY() - grid.elemEL[el_nr].ND[3].getY(), 2)) / 2.0;
                        break;
                }

                //2 punkty całkowania na powierzchni
                for (int p = 0; p < 2; p++) {
                    for (int n = 0; n < 4; n++) {
                        for (int i = 0; i < 4; i++) {
                            //dodanie warunku brzegowego do macierzy H
                            H_current[n][i] += alfa * el_lok.getPOW()[id].N[p][n] * el_lok.getPOW()[id].N[p][i] * detj;
                        }
                        P_current[n] += alfa * t_otoczenia * el_lok.getPOW()[id].N[p][n] * detj;
                    }
                }
            }
            //agregacja, wpisanie do macierzy globalnych
            for (int i = 0; i < 4; i++) {
                for (int j = 0; j < 4; j++) {
                    H_global[grid.elemEL[el_nr].globalNodeID[i]][grid.elemEL[el_nr].globalNodeID[j]] += H_current[i][j];
                }
                P_global[grid.elemEL[el_nr].globalNodeID[i]] += P_current[i];

            }
        }
    }
    public static Globaldata getInstance() throws FileNotFoundException {
        if (globalData == null) {
            globalData = new Globaldata();
        }
        return globalData;
    }
    public void showData(){
        System.out.println("Wysokość: "+ H+"\tSzerokość: "+B+
                "\nIlość węzłów na wysokości: " + nH+" oraz na wysokości: "+nB+
                "\nliczba węzłów: " + nh +" liczba elementów: "+ne+
                "\ntemperatura początkowa: " +t_begin+
                "\nczas procesu: " +tau+
                "\npoczątkowa wartość przyrostu czasu: " +dtau+
                "\ntemperatura otoczenia: " + t_otoczenia+
                "\nwspółczynnik wymiany ciepła: " + alfa+
                "\nciepło właściwe: " +c+
                "\nwspółczynnik przewodzenia ciepła: " +k+
                "\ngęstość: " + ro+"\n\n" );
    }
    public void showVectorP(){
        for (int j = 0; j < nh; j++) {
            System.out.printf("%.3f \t", P_global[j]);
        }
    }
    public void showMatrixH(){
        for(int i=0;i<nh;i++) {
            for (int j = 0; j < nh; j++) {
                System.out.printf("%.3f \t", H_global[i][j]);
            }
            System.out.println("");
        }
    }
    public double getTau() {
        return tau;
    }
    public double getDtau() {
        return dtau;
    }
    public double[][] getH_global() {
        return H_global;
    }
    public double[] getP_global() {
        return P_global;
    }
    public int getnB() {
        return nB;
    }
    public int getNh() {
        return nh;
    }
    public int getnH() {
        return nH;
    }
}

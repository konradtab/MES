package mes;

import structure.Globaldata;
import structure.Grid;

import java.io.FileNotFoundException;
import java.util.Scanner;

/**
 * Created by Dell on 04.11.2017.
 */

public class Main {
    public static void main(String[] args) throws FileNotFoundException {
        Globaldata data = Globaldata.getInstance();
        Grid grid = Grid.getInstance();
        double[] t;

        int tmp=0;
        for (int itau = 0; itau < data.getTau(); itau += data.getDtau()) {
            System.out.println("Iteracja: "+tmp++ +"\tCzas: "+ itau);
            int count = 0;
            for (int i = 0; i < data.getnB(); i++) {
                for (int j = 0; j < data.getnH(); j++) {
                    System.out.printf("%.3f \t", grid.nodeNB[count++].getT());
                }
                System.out.println("");
            }
            System.out.println("\n\n");
            data.compute();
            t = MetodaEliminacjiGaussa.gaussElimination(data.getNh(), data.getH_global(), data.getP_global());
            for (int i = 0; i < data.getNh(); i++) {
                grid.nodeNB[i].setT(t[i]);
            }
        }
        System.out.println("Iteracja: "+tmp +"\tCzas: "+ data.getTau());
        int count1 = 0;
        for (int i = 0; i < data.getnB(); i++) {
            for (int j = 0; j < data.getnH(); j++) {
                System.out.printf("%.3f \t", grid.nodeNB[count1++].getT());
            }
            System.out.println("");
        }
        System.out.println("\n\n");


        boolean status = true;

        while (status) {
            System.out.println("MENU");
            Scanner dane = new Scanner(System.in);
            System.out.println("1-wypisz węzły z warunkiem brzegowym\n" +
                    "2-wypisz narożniki\n" +
                    "3-oblicz całkę\n" +
                    "4-wypisz dane\n" +
                    "5-wypisz macierz H\n" +
                    "6-rozkład temperatur\n" +
                    "8-wypisz macierze H i vectory P dla kroków czasowych\n" +
                    "9-wypisz wszystkie węzły\n" +
                    "10-wyjdź\n");
            int option = dane.nextInt();
            switch (option) {
                case 1:
                    // wypisywanie węzłów z nałozonym warunkiem brzegowym
                    grid.showNodesBrzeg();
                    break;

                case 2:
                    //wypisywanie narożników siatki MES
                    grid.wypiszNarozniki();
                    break;

                case 3:
                    //Licznie całki 2 punktowej oraz trzypunktowej
                    Calka calka = new Calka();

                    double wynik1 = calka.oblicz_calka_2p();
                    double wynik2 = calka.oblicz_calka_3p();

                    System.out.println("Wynik 2p = " + wynik1);
                    System.out.println("Wynik 3p = " + wynik2);
                    break;

                case 4:
                    //wypisanie danych
                    data.showData();
                    break;

                case 5:
                    //wypisywanie macierzy H
                    data.showMatrixH();
                    break;

                case 6:
                    System.out.println("Rozkład temperatur końcowych");
                    int count = 0;
                    for (int i = 0; i < data.getnB(); i++) {
                        for (int j = 0; j < data.getnH(); j++) {
                            System.out.printf("%.3f \t", grid.nodeNB[count++].getT());
                        }
                        System.out.println("");
                    }
                    System.out.println("\n\n");
                break;

                case 8:
                    int iter =0;
                    for (int itau = 0; itau < data.getTau(); itau += data.getDtau()) {
                        data.compute();
                        t = MetodaEliminacjiGaussa.gaussElimination(data.getNh(), data.getH_global(), data.getP_global());
                        for (int i = 0; i < data.getNh(); i++) {
                            grid.nodeNB[i].setT(t[i]);
                        }
                        System.out.println("Iteracja: "+ iter++);
                        System.out.println("-----Matrix([H]+[C]/dT-----");
                        data.showMatrixH();
                        System.out.println("");
                        System.out.println("------Vector({P}+{[C]/dT}*{T0})-----");
                        data.showVectorP();
                        System.out.println("\n------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------");
                    }
                break;

                case 9:
                    //wypisywanie wszystkich elementów
                    grid.wypiszND();
                break;

                case 10:
                    status=false;
                    break;

                default:
                    System.out.println("brak opcji");
                break;
            }
        }
    }
}
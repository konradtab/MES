package structure;

import java.io.FileNotFoundException;

/**
 * Created by Dell on 04.11.2017.
 */

public class Grid {
    public Node nodeNB[];
    Element elemEL[];
    Globaldata data;

    private static Grid grid = null;

    public Grid() throws FileNotFoundException {
        data =Globaldata.getInstance();
        nodeNB=new Node[data.nh];
        elemEL=new Element[data.ne];

        double dx=data.B/(data.nB-1);
        double dy=data.H/(data.nH-1);
        int licznik=0;

        for(int i=0;i<data.nB;i++){
            for(int j=0;j<data.nH;j++)
            {
                this.nodeNB[licznik]=new Node(i*dx,j*dy);
                this.nodeNB[licznik].setLicznik(licznik);
                licznik++;
            }
        }

        licznik=0;
        for (int i = 0; i < data.nB-1; i++) {
            for (int j = 0; j < data.nH-1; j++ ) {
                elemEL[licznik++] = new Element(i, j, new Node[]{nodeNB[data.nH*i+j], nodeNB[data.nH * (i + 1) + j], nodeNB[data.nH * (i + 1) + (j + 1)], nodeNB[data.nH * i + (j + 1)]});
            }
        }
    }

    public void wypiszND() {
        System.out.println("Węzły:");
        for (int i = 0; i < data.nh; i++) {
            double xx = grid.nodeNB[i].getX();
            double yy = grid.nodeNB[i].getY();
            java.text.DecimalFormat df = new java.text.DecimalFormat();
            df.setMaximumFractionDigits(3);
            df.setMinimumFractionDigits(3);
            System.out.println("x=" + df.format(xx) + "\t y=" + df.format(yy) + "\t\tNumer węzła=" + grid.nodeNB[i].getLicznik()  + "\t\tStatus:" + nodeNB[i].isStatus());

        }
    }

    public void showNodesBrzeg(){
        System.out.println("Iteracja przesunięta o 1 w celu lepszej czytelności");
        System.out.println("Węzły ze statusem true:");
        for (int i = 0; i < data.nh; i++) {
            if (grid.nodeNB[i].isStatus() == true) {
                double xx = grid.nodeNB[i].getX();
                double yy = grid.nodeNB[i].getY();
                java.text.DecimalFormat df = new java.text.DecimalFormat();
                df.setMaximumFractionDigits(3);
                df.setMinimumFractionDigits(3);
                System.out.println("x=" + df.format(xx) + "\t y=" + df.format(yy) + "\t\tNumer węzła=" + (grid.nodeNB[i].getLicznik() + 1));
            }
        }
    }

    public void wypiszNarozniki(){
        System.out.println("\nNarożniki:");
        for (int i = 0; i < data.nh; i++) {
            if ((grid.nodeNB[i].getX() == 0 && grid.nodeNB[i].getY() == 0) || (grid.nodeNB[i].getX() == 0 && grid.nodeNB[i].getY() == data.H) || (grid.nodeNB[i].getX() == data.B && grid.nodeNB[i].getY() == 0) || (grid.nodeNB[i].getX() == data.H && grid.nodeNB[i].getY() == data.B)) {
                System.out.println("x=" + grid.nodeNB[i].getX() + "\ty=" + grid.nodeNB[i].getY());
            }
        }
    }

    public static Grid getInstance() throws FileNotFoundException {
        if (grid == null) {
            grid = new Grid();
        }
        return grid;
    }
}

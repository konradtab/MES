package structure;

import java.io.FileNotFoundException;

/**
 * Created by Dell on 04.11.2017.
 */

public class Node {
    private double x,y;         //współrzędne węzła
    private double t;           //temperatura
    private int licznik;        //numer węzła
    private boolean status;     //true gdy nakładany jest warunek brzegowy

    private Globaldata data;

    public Node(double x, double y) throws FileNotFoundException {
        data = Globaldata.getInstance();

        this.x=x;
        this.y=y;
        this.t=data.t_begin;

        //Nakładanie warunku brzegowego
        if(((this.x==0.0)||(this.y==0.0))||(this.x==data.B)||(this.y==data.H))
        {
            this.status=true;
        }else this.status=false;
    }

    public double getX() {
        return x;
    }
    public double getY() {
        return y;
    }
    public double getT() {
        return t;
    }
    public void setT(double t) {
        this.t = t;
    }
    public int getLicznik() {
        return licznik;
    }
    public boolean isStatus() {
        return status;
    }
    public void setLicznik(int licznik) {
        this.licznik = licznik;
    }

}

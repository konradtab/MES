package mes;

/**
 * Created by Dell on 09.11.2017.
 */

public class Calka {

    private double[] tab2p = { -0.577, 0.577 };
    private double[] w2p = { 1, 1 };

    private double[] tab3p = { -0.7745, 0, 0.7745 };
    private double[] w3p = { 5.0/9.0, 8.0/9.0, 5.0/9.0 };

    private double funkcja ( double x, double y ) {
        return ( 2 * Math.pow( x, 2 ) * Math.pow( y, 2 ) + 6 * x + 5 );
    }

    public double oblicz_calka_2p(  ) {
        double suma = 0.0;

        for ( int i = 0; i < 2; i ++ )
            for ( int j = 0; j < 2; j ++ )
                suma += funkcja( tab2p[i], tab2p[j] ) * w2p[i] * w2p[j];

        return suma;
    }


    public double oblicz_calka_3p(  ) {
        double suma = 0.0;

        for ( int i = 0; i < 3; i ++ )
            for ( int j = 0; j < 3; j ++ )
                suma += funkcja( tab3p[i], tab3p[j] ) * w3p[i] * w3p[j];

        return suma;
    }

}

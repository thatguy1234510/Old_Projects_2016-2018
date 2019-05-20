package Work_from_2017.matrix_stuff.graphics_stuff;

import Work_from_2017.matrix_stuff.Convert;
import Work_from_2017.matrix_stuff.Matrix;
import Work_from_2017.matrix_stuff.Matrix_ops;

import java.awt.*;

public class Transform {
    public static Polygon rotate(Polygon pent, double theta) {
        Matrix arb = new Matrix(Convert.toArray(pent), 2, pent.npoints);
        double[] oper_array = {Math.cos(theta), Math.sin(theta), -Math.sin(theta), Math.cos(theta)};
        Matrix oper = new Matrix(oper_array, 2, 2);
        Matrix result = Matrix_ops.product(oper, arb);
        return Convert.toPolygon(result);
    }

    public static Polygon translate(Polygon pent, int xp, int yp) {
        double[] arb = Convert.toArray(pent);
        for (int i = 0; i < pent.npoints * 2; i++) {
            if (i % 2 == 0) {
                arb[i] += xp;
            } else {
                arb[i] += yp;
            }
        }
        return Convert.toPolygon(new Matrix(arb, 2, pent.npoints));
    }

    public static Polygon scale(Polygon pent, double sb) {
        double[] arb_array = Convert.toArray(pent);
        for (int i = 0; i < pent.npoints * 2; i++) {
            arb_array[i] *= sb;
        }
        return Convert.toPolygon(new Matrix(arb_array, 2, pent.npoints));
    }

    public static Polygon reflect(Polygon pent, double m) {
        m = -m;
        double[] reflect_array = {1 - Math.pow(m, 2), 2 * m, 2 * m, Math.pow(m, 2) - 1};
        for (int i1 = 0; i1 < reflect_array.length; i1++) {
            reflect_array[i1] *= 1 / (1 + Math.pow(m, 2));
        }
        Matrix reflect_mat =
                Matrix_ops.product(
                        new Matrix(reflect_array, 2, 2),
                        new Matrix(Convert.toArray(pent), 2, pent.npoints)
                );
        return Convert.toPolygon(reflect_mat);
    }
}

package Work_from_2017.matrix_stuff;

import Work_from_2017.complex_nums.Complex;

import java.awt.*;
import java.util.Collections;
import java.util.Vector;

public class Convert {
    public static double[] toArray(Vector<Double> arm) {
        double[] dd = new double[arm.size()];
        for (int i = 0; i < arm.size(); i++) {
            dd[i] = arm.get(i);
        }
        return dd;
    }

    public static double[] toArray(Point arb) {
        double[] dd = {arb.x, arb.y};
        return dd;
    }

    public static double[] toArray(Polygon arb) {

        //double[] arbarray= new double[arb.npoints*2];
        Vector<Double> arbvect = new Vector<Double>();
        for (int i = 0; i < arb.npoints; i++) {
            arbvect.add((double) arb.xpoints[i]);
            arbvect.add((double) arb.ypoints[i]);
        }
        return toArray(arbvect);
    }

    public static Complex toComplex(Matrix arb) {
        return new Complex(arb.els[3], arb.els[2]);
    }

    public static Polygon toPolygon(Matrix arb) {
        int[] xs = new int[arb.els.length / 2];
        int[] ys = new int[arb.els.length / 2];
        for (int i = 0; i < arb.els.length / 2; i++) {
            xs[i] = (int) arb.els[2 * i];
            ys[i] = (int) arb.els[2 * i + 1];

        }
        return new Polygon(xs, ys, xs.length);
    }

    public static Matrix toMatrix(Polygon arb){
        int rows=2;
        int cols=arb.npoints;
        double[] mat = new double[rows * cols];
        double[] tempmat = new double[rows*cols];
        //for()
        for (int ar = 0; ar < Math.floor(tempmat.length/2); ar++) {
            mat[ar] = arb.xpoints[ar];
        }
        for (int ar = (int)Math.ceil(tempmat.length/2); ar < tempmat.length; ar++){
            mat[ar] = arb.ypoints[ar];
        }
        Vector<Double> b_mat = new Vector<Double>();
        for (int start = 0; start < cols; start++) {
            int i = 0;
            for (int skip = start; i < rows; skip += cols) {
                i++;
                b_mat.add(mat[skip]);
            }
        }
        return new Matrix(mat, rows, cols);
    }

    public static double[] urv_to_orv(double[] mat, int rows, int cols) {
        Vector<Double> b_mat = new Vector<Double>();
        for (int start = 0; start < cols; start++) {
            int i = 0;
            for (int skip = start; i < rows; skip += cols) {
                i++;
                b_mat.add(mat[skip]);
            }
        }
        return toArray(b_mat);
    }

    public static Vector<Vector<Double>> bmat_to_2d(Vector<Double> bmat, int rows, int cols) {
        Vector<Vector<Double>> two_d = new Vector<Vector<Double>>(Collections.nCopies(rows, new Vector<Double>(Collections.nCopies(cols, 0.0))));
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                two_d.get(i).set(j, bmat.get(i + (cols * j)));
            }
        }
        return two_d;

    }

    public static double[][] twoDvect_to_twoDarray(Vector<Vector<Double>> vect_mat) {
        double[][] t_mat = new double[vect_mat.size()][vect_mat.get(0).size()];
        for (int i = 0; i < vect_mat.size(); i++) {
            for (int j = 0; j < vect_mat.get(0).size(); j++) {
                t_mat[i][j] = vect_mat.get(i).get(j);
            }
        }
        return t_mat;
    }

    public static double[] actual_coords(int frame_width, int frame_height, double xmin, double xmax, double ymin, double ymax, int cx, int cy) {
        //takes in coords and outputs the corrisponding vals from 2,-2
        double scaleh = (ymax - ymin) / frame_height;
        //what one px corresponds to
        double scalew = (xmax - xmin) / frame_width;
        double true_x;
        double true_y;
        true_x = xmin + (cx * scalew);
        true_y = ymax - (cy * scaleh);
        return new double[]{true_x, true_y};
    }

    public static int[] computer_coords(int frame_width, int frame_height, double xmin, double xmax, double ymin, double ymax, double tx, double ty) {
        double scaleh = (ymax - ymin) / frame_height;
        //what one px corresponds to
        double scalew = (xmax - xmin) / frame_width;
        int c_x;
        int c_y;
        c_x = (int) ((tx - xmin) / scalew);
        c_y = (int) ((-1 * (ty - ymax)) / scaleh);
        return new int[]{c_x, c_y};
    }
}
package Work_from_2017.matrix_stuff;

import java.util.EmptyStackException;

public class Matrix_ops {
    public static Matrix difference(Matrix un, Matrix dos) {
        if (un.rows != dos.rows || un.cols != dos.cols) {
            // if the mats are not of the same dim then stop
            throw new EmptyStackException();
            // the error is arbitrary i just want it to stop
        }
        for (int i = 0; i < un.els.length; i++) {
            dos.els[i] = dos.els[i] * -1;
        }
        return sum(un, dos);
    }

    public static Matrix sum(Matrix un, Matrix dos) {
        if (un.rows != dos.rows || un.cols != dos.cols) {
            // if the mats are not of the same dim then stop
            throw new EmptyStackException();
            // the error is arbitrary i just want it to stop
        }
        double[] sum_els = new double[un.els.length];
        for (int i = 0; i < un.els.length; i++) {
            sum_els[i] = un.els[i] + dos.els[i];
            //iterate through and add
        }
        Matrix sum = new Matrix(sum_els, un.cols, un.rows);
        return sum;
    }

    public static Matrix product(Matrix Q1, Matrix Q2) {
        if (Q1.cols != Q2.rows) {
            // if it does not work throw it out
            throw new EmptyStackException();
        }
        double[] prdct = new double[Q1.rows * Q2.cols];
        // new matrix of correct length
        for (int i = 0; i < Q1.rows; i++) {
            boolean was_run;
            for (int j = 0; j < Q2.cols; j++) {
                was_run = true;
                // i is the row it takes and j is the col it takes
                // it then stores a dot product of those and then appends that val to the product mat
                double dot_prdct = 0;
                for (int dotLoop = 0; dotLoop < Q1.cols; dotLoop++) {
                    dot_prdct += Q1.els[Q1.rows * dotLoop + i] * Q2.els[dotLoop + (j * Q2.rows)];
                }
                if (was_run) {
                    prdct[i + (j * Q1.rows)] = dot_prdct;
                    // append dot product
                }
                // only if the j loop is not done already
            }

        }
        Matrix arb = new Matrix(prdct, Q1.rows, Q2.cols);
        return arb;
    }

    public static Matrix pow(Matrix mat, int n) {

        Matrix u_mat = new Matrix(mat);
        for (int i = 0; i < n - 1; i++) {
            u_mat = product(u_mat, mat);
        }
        return u_mat;
    }
}
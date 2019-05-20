package Work_from_2017.matrix_stuff;

//import org.jetbrains.annotations.NotNull;

import java.util.EmptyStackException;
import java.util.Scanner;
import java.util.Vector;

public class Matrix {

    // TODO: finish a 3D graphics package

    ///////////construction\\\\\\\\\\\\
    public double[] els;
    public int cols;
    public int rows;

    public Matrix(double[] els, int rows, int cols) {
        this.els = els;
        this.rows = rows;
        this.cols = cols;
        // the basic constructor
    }

    public Matrix(Matrix cc) {
        System.arraycopy(cc.els, 0, this.els, 0, cc.els.length);
        this.rows = cc.rows;
        this.cols = cc.cols;
        // this is a copy constructor
        // if i feed a matrix into this i get a copy
    }

    //////////public stuff\\\\\\\\\\\
   
    public static Matrix mat_input() {
        @SuppressWarnings("resource")
        Scanner input = new Scanner(System.in);
        System.out.println("how many rows? ");
        int rows = Integer.parseInt(input.nextLine());
        System.out.println("how many columns? ");
        int cols = Integer.parseInt(input.nextLine());
        String tempmat = "";
        for (int i = 0; i < rows; i++) {
            System.out.println("input row " + (i + 1));
            String temrow = input.nextLine();
            if (i == rows - 1) {
                tempmat += temrow;
            } else {
                tempmat += temrow + ",";
            }
        }
        // this part gets you to input each row and adds them into one string
        double[] mat = new double[rows * cols];
        for (int ar = 0; ar < tempmat.split(",").length; ar++) {
            double arb = Double.parseDouble(tempmat.split(",")[ar]);
            mat[ar] = arb;
            // parse doubles from the list and add them to a row
        }
        Vector<Double> b_mat = new Vector<Double>();
        for (int start = 0; start < cols; start++) {
            int i = 0;
            for (int skip = start; i < rows; skip += cols) {
                i++;
                b_mat.add(mat[skip]);
            }
        }

        // b_mat is the properly ordered matrix
        /*
         * for the algorithm to work the matix:
		 * [1,2,3][1,2,3][1,2,3]
		 * must be ordered into one vector as
		 * [1,1,1,2,2,2,3,3,3]
		 * then to get to any one element the alg is mat[i][j]=vect[i+(row_size * j)]
		 */
        return new Matrix(Convert.toArray(b_mat), rows, cols);
        //return new Matrix(mat,rows,cols);
    }

    public static void print_mat(Matrix mat) {
        for (int i = 0; i < mat.rows; i++) {

            for (int j = 0; j < mat.cols; j++) {
                System.out.print(mat.els[i + (mat.rows * j)] + "  ");
            }
            System.out.print("\n");
        }
        //this prints matrices
    }

    public double get(int row, int col) {
        return els[row + (col * cols)];
    }

    public double det() throws Exception {
        if (els.length % cols == 0) {
            // throws error if not square
            if (els.length == 4) {
                double det = (els[0] * els[3] - (els[2] * els[1]));
                return det;
                //if els is 2x2 then just solve it
            }
            double det = 0.0;
            for (int i = 0; i < els.length / cols; i++) {
                if (i % 2 != 0) {
                    det -= els[cols * i] * minor(els, 0, i, cols);
                    // the minor func breaks the mat into smaller parts and returns the det of those
                    //System.out.println((double)els.get(0).get(i) * sub_det(els, i));
                } else {
                    det += els[cols * i] * minor(els, 0, i, cols);
                    //System.out.println((double)els.get(0).get(i) * sub_det(els, i));
                }
            }
            return det;
        }

        throw new Exception("the matrix is not square");

        //("the matrix is not square");
    }

    public Matrix mat_of_minors() {
        double[] min_mat = new double[els.length];

        for (int i = 0; i < els.length / cols; i++) {
            for (int j = 0; j < els.length / cols; j++) {
                min_mat[i + (j * cols)] = minor(els, i, j, cols);
            }
        }
        return new Matrix(min_mat, cols, rows);
        //this makes a matrix of minors
    }

    public Matrix cofactors() {
        //this makes a mat of cofs
        for (int i = 0; i < els.length / cols; i++) {

            for (int j = 0; j < els.length / cols; j++) {
                els[i + (cols * j)] = els[i + (cols * j)] * Math.pow(-1, i + j + 2);
                //you have to add 2 because the first element is 0,0 not 1,1

            }
        }
        return new Matrix(els, cols, rows);

    }

    public Matrix transpose() {
        // calling this function is bad practice and it is preferable to just move through it differently and get the same result
        double[] mat_t = new double[els.length];
        for (int i = 0; i < els.length / cols; i++) {
            for (int j = 0; j < els.length / cols; j++) {
                mat_t[j + (cols * i)] = els[i + (cols * j)];
            }
        }
        return new Matrix(mat_t, cols, rows);
    }
    // it is bad practice to use the transpose func because it is faster to just index the mat differently

    public Matrix Inverse() {
        double[] inv_mat = cof(min_mat(els));
        double oodet = 1 / determinate(els, cols);
        //inv_mat = trans(cof(min_mat(els,row_size),row_size),row_size);
        // I know it would be faster to just index it differently instead of transposing it but it is easier to represent if you actually transpose it
        for (int i = 0; i < els.length / cols; i++) {
            for (int j = 0; j < els.length / cols; j++) {
                //inv_mat[i + (row_size*j)] = inv_mat[i + (row_size*j)] * (1 / determinate(els, row_size));
                inv_mat[j + (cols * i)] = inv_mat[j + (cols * i)] * oodet;
                // this makes it effectively transposed
            }
        }
        return new Matrix(inv_mat, cols, rows);
        // i know i don't have to have the trans func here because i already did it just indexing it diff
        // but i want to be able to print it more easily
    }

    public double minor(double[] els, int row_avoid, int col_avoid, int row_size) {
        Vector<Double> smlr_mat = new Vector<Double>();
        for (int i = 0; i < els.length / row_size; i++) {
            for (int j = 0; j < els.length / row_size; j++) {
                if (j != col_avoid && i != row_avoid) {
                    smlr_mat.add(els[i + (row_size * j)]);
                }
            }

        }
        // this makes a smaller matrix and returns the det of that
        return determinate(Convert.toArray(smlr_mat), row_size - 1);
    }

    ///////////////////helper and private stuff for inverse\\\\\\\\\\\\\\\\
    //these funcs are faster cause they just have a list and not a matrix
    private double determinate(double[] mat, int row_len) {
        // this is what the minor func calls
        if (mat.length % row_len == 0) {
            // throws error if not square
            if (mat.length == 4) {
                double det = (mat[0] * mat[3] - (mat[2] * mat[1]));
                return det;
                //if mat is 2x2 then just solve it
            }
            double det = 0.0;
            for (int i = 0; i < mat.length / row_len; i++) {
                if (i % 2 != 0) {
                    det -= mat[cols * i] * minor(mat, 0, i, cols);
                    // the minor func breaks the mat into smaller parts and returns the det of those
                    //System.out.println((double)mat.get(0).get(i) * sub_det(mat, i));
                } else {
                    det += mat[cols * i] * minor(mat, 0, i, cols);
                    //System.out.println((double)mat.get(0).get(i) * sub_det(mat, i));
                }
            }
            return det;
        }
        throw new EmptyStackException();
    }

    private double[] min_mat(double[] mat) {
        double[] min_mat = new double[mat.length];
        for (int i = 0; i < mat.length / cols; i++) {
            for (int j = 0; j < mat.length / cols; j++) {
                min_mat[i + (j * cols)] = minor(mat, i, j, cols);
            }
        }
        return min_mat;
    }

    private double[] cof(double[] mat) {
        for (int i = 0; i < mat.length / cols; i++) {
            for (int j = 0; j < mat.length / cols; j++) {
                mat[i + (cols * j)] = mat[i + (cols * j)] * (Math.pow(-1, i + j + 2));
                //you have to add 2 because the first element is 0,0 not 1,1
            }
        }
        return mat;
    }

}

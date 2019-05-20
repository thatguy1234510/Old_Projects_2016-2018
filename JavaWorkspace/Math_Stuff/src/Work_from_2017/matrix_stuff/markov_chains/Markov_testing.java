package Work_from_2017.matrix_stuff.markov_chains;

import Work_from_2017.matrix_stuff.Matrix;
import Work_from_2017.matrix_stuff.Matrix_ops;

import java.util.Scanner;

@SuppressWarnings("resource")
public class Markov_testing {
    public static void main(String args[]) {
        System.out.println("Input the transition matrix: ");
        Matrix trans_mat = Matrix.mat_input();
        System.out.println("input the starting vector: ");
        Matrix s_vect = Matrix.mat_input();
        System.out.println("the number of state changes that have occured: ");
        Scanner input = new Scanner(System.in);
        int pow = input.nextInt();
        System.out.println("the trans mat after " + pow + " powers is equal to: ");
        Matrix arb = Matrix_ops.pow(trans_mat, pow);
        Matrix.print_mat(arb);
        System.out.println("the prob vector is equal to: ");
        Matrix.print_mat(Matrix_ops.product(s_vect, arb));
        System.exit(0);
    }
}

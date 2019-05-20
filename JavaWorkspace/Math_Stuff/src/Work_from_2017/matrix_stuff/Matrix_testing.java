package Work_from_2017.matrix_stuff;

public class Matrix_testing {
    public static void main(String args[]) {
        System.out.println("input mat: ");
        Matrix mat1 = Matrix.mat_input();
        System.out.println("input mat2: ");
        Matrix mat2 = Matrix.mat_input();
        Matrix.print_mat(Matrix_ops.product(mat1, mat2));


    }
}

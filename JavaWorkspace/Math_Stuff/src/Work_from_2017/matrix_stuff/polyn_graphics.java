package Work_from_2017.matrix_stuff;

import Work_from_2017.matrix_stuff.graphics_stuff.Create;

import javax.swing.*;
import java.awt.*;


public class polyn_graphics extends JFrame {
    int h = 1440;
    int w = 1920;

    // these vars are arbitrary
    class polyn {
        double[] cof;
        String name;

        public polyn(double[] cof) {
            this.cof = cof;
            for (int i = 1; i < cof.length; i++) {
                this.name += Double.toString(cof[cof.length - i]) + "x^" + Integer.toString(i);
            }
            this.name += Double.toString(cof[cof.length - 1]);
        }

        public polyn(int[] x, int[] y) {
            double[] xa = new double[x.length];
            double[] ya = new double[y.length];
            for (int i = 0; i < x.length; i++) {
                xa[i] = Convert.actual_coords(w, h, -10, 10, -5, 5, x[i], y[i])[0];
                ya[i] = Convert.actual_coords(w, h, -10, 10, -5, 5, x[i], y[i])[1];
            }
            Matrix ys = new Matrix(ya, ya.length, 1);
            double[] xma = new double[y.length * y.length];
            for (int i = 0; i < x.length; i++) {
                for (int j = 0; j < x.length; j++) {
                    xma[j + (x.length * i)] = Math.pow(x[j], i);
                }
            }
            Matrix xs = new Matrix(xma, x.length, x.length);
            this.cof = Matrix_ops.product(ys, xs.Inverse()).els;
            for (int i = 1; i < cof.length; i++) {
                this.name += Double.toString(cof[cof.length - i]) + "x^" + Integer.toString(i);
            }
            this.name += Double.toString(cof[cof.length - 1]);
        }

        public polyn(double[] x, double[] y) {
            Matrix ys = new Matrix(y, y.length, 1);
            double[] xa = new double[y.length * y.length];
            for (int i = 0; i < x.length; i++) {
                for (int j = 0; j < x.length; j++) {
                    xa[j + (x.length * i)] = Math.pow(x[j], i);
                }
            }
            Matrix xs = new Matrix(xa, x.length, x.length);
            this.cof = Matrix_ops.product(ys, xs.Inverse()).els;
            for (int i = 1; i < cof.length; i++) {
                this.name += Double.toString(cof[cof.length - i]) + "x^" + Integer.toString(i);
            }
            this.name += Double.toString(cof[cof.length - 1]);
        }

    }

    //TODO: actually put in the work to make the animation work instead of just the math

    public polyn_graphics() {
        setLayout(new BorderLayout());
        setBackground(Color.black);
        add(new draw_stuff(), BorderLayout.CENTER);
        setSize(1920, 1080);
        setTitle("java_graphics");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        //setResizable(false);
        setVisible(true);

    }

    public static void main(String[] args) {

        polyn_graphics frame = new polyn_graphics();


    }

    class draw_stuff extends JPanel {

        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            //TODO: write this


        }

    }
}


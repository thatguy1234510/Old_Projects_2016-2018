package Work_from_2017.complex_nums;

import Work_from_2017.complex_nums.graphics.julia_set_research.helpers.ScreenData;

public class Complex_ops {

    public static double[] actual_coords(ScreenData screen,int cx, int cy) {
        //takes in coords and outputs the corrisponding vals from 2,-2
        double scaleh = (screen.zoom_y[1] - screen.zoom_y[0]) / screen.Moniter_height;
        //what one px corresponds to
        double scalew = (screen.zoom_x[1] - screen.zoom_x[0]) / screen.Moniter_width;
        double true_x;
        double true_y;
        true_x = screen.zoom_x[0] + (cx * scalew);
        true_y = screen.zoom_y[1] - (cy * scaleh);
        // because computer coords have 0,0 in the top left
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

    public static Complex sum(Complex s1, Complex s2) {
        Complex s3 = new Complex(s1.re + s2.re, s1.im + s2.im);
        return s3;
    }

    public static Complex diff(Complex s1, Complex s2) {
        Complex s3 = new Complex(s1.re - s2.re, s1.im - s2.im);
        return s3;
    }

    public static Complex product(Complex s1, Complex s2) {
        return new Complex(s1.re * s2.re - (s1.im * s2.im), s1.re * s2.im + (s1.im * s2.re));
    }

    public static Complex quotient(Complex s1, Complex s2) {
        Complex conj = s2.conjugate();
        double quo = product(s2, conj).re;
        return new Complex(s1.re / quo, s1.im / quo);
    }

    public static Complex pow(Complex s1, int pow) {
        Complex n = new Complex(s1);
        for (int i = 1; i < pow; i++) {
            n = product(n, s1);
        }
        return n;
    }

}

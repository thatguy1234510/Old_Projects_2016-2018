package Work_from_2017.complex_nums.graphics;

import Work_from_2017.complex_nums.Complex;
import Work_from_2017.complex_nums.Complex_ops;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;

public class Mandelbrot_sets {
    int frame_height = 1500;
    int frame_width = 1500;

    public static void main(String args[]) {
        BufferedImage img = mandelbrot_picture(1824, 1824, -2.5, 0.5, -1.5, 1.5);
        JFrame frame = new JFrame();
        //frame.setBackground(Color.black);
        frame.getContentPane().setLayout(new FlowLayout());
        frame.getContentPane().add(new JLabel(new ImageIcon(img)));
        frame.pack();
        frame.setResizable(false);
        frame.setVisible(true);
    }

    public static BufferedImage mandelbrot_picture(int frame_height, int frame_width, double xmin, double xmax, double ymin, double ymax) {
        BufferedImage img = new BufferedImage(frame_width, frame_height, BufferedImage.TYPE_INT_ARGB);
        for (int i = 0; i < frame_width; i++) {
            for (int j = 0; j < frame_height; j++) {
                double escape_velocity = escape_velocity_from_mandelbrot_set(new Complex(actual_coords(frame_height, frame_width, xmin, xmax, ymin, ymax, i, j)[0],
                                actual_coords(frame_height, frame_width, xmin, xmax, ymin, ymax, i, j)[1])
                        , 500);

                int a = 0; //alpha
                Color blueish = new Color((int) escape_velocity / 2, (int) (escape_velocity / 16), (int) (1 / 32 * escape_velocity));
                int r = 0;
                int g = 0;//1111111/escape_velocity); //green
                int b = blueish.getRGB(); //blue

                int p = (a << 24) | (r << 16) | (g << 8) | b; //pixel

                img.setRGB(i, j, p * 20);
                if (escape_velocity == 0) {
                    img.setRGB(i, j, Color.black.getRGB());
                }

            }
        }
        //RescaleOp rescaleOp = new RescaleOp(2.0f, 15, null);
        //rescaleOp.filter(img, img);
        return img;
    }

    private static double escape_velocity_from_mandelbrot_set(Complex p_val, int bailout_val) {
        Complex mb_iter = new Complex(0, 0);
        for (int i = 0; i < bailout_val; i++) {
            mb_iter = Complex_ops.sum(Complex_ops.product(mb_iter, mb_iter), p_val);
            if (mb_iter.re > 10 || mb_iter.re < -10 || mb_iter.im > 10 || mb_iter.im < -10) {
                return i;
            }
        }
        return 0;
    }

    private static double[] actual_coords(int frame_height, int frame_width, double xmin, double xmax, double ymin, double ymax, int cx, int cy) {
        //takes in coords and outputs the corrisponding vals from 2,-2
        double scaleh = (ymax - ymin) / frame_height;
        //what one px corresponds to
        double scalew = (xmax - xmin) / frame_width;
        double true_x;
        double true_y;
        true_x = xmin + (cx * scalew);
        true_y = ymax - (cy * scaleh);
        double[] coords = {true_x, true_y};
        return coords;
    }

}


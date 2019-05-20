package Work_from_2017.complex_nums.graphics.julia_set_research.old;

import Work_from_2017.complex_nums.Complex;
import Work_from_2017.complex_nums.Complex_ops;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.RescaleOp;
import java.io.File;
import java.io.IOException;
import java.util.Scanner;

public class julia_sets_V1 {
    public static void main(String args[]) {
        Scanner input = new Scanner(System.in);
        System.out.println("Please input the real part of the critical point for the julia set");
        double re = Double.parseDouble(input.nextLine());
        System.out.println("Please input the imaginary part of the critical point for the julia set");
        double im = Double.parseDouble(input.nextLine());
        Complex crit = new Complex(re, im);
        long start = System.nanoTime();
        BufferedImage img = julia_picture(crit, 1824, 2736, -2.5, 2.5, -1.675, 1.675);
        long stop = System.nanoTime();
        System.out.println("took: " + (stop - start) / 1000000000 + " seconds");
        JFrame frame = new JFrame();
        //frame.setBackground(Color.black);
        frame.getContentPane().setLayout(new FlowLayout());
        frame.getContentPane().add(new JLabel(new ImageIcon(img)));
        frame.pack();
        frame.setResizable(false);
        frame.setVisible(true);
    }

    public static BufferedImage julia_picture(Complex critical_val, int frame_height, int frame_width, double xmin, double xmax, double ymin, double ymax) {
        BufferedImage img = new BufferedImage(frame_width, frame_height, BufferedImage.TYPE_INT_ARGB);
        try {
            BufferedImage colormap = ImageIO.read(new File("C:\\Users\\proff\\workspace\\hello_world\\src\\Work_from_2017.complex_nums\\graphics\\julia_set_research\\colormaps\\Colormap250px.png"));
            //the colormap is just an image that was made in a css gradient builder
            //Colormap1 is better for images with lower escape vals but Colormap2 is better for higher escape vals
            for (int i = 0; i < frame_width; i++) {
                for (int j = 0; j < frame_height; j++) {
                    int escape_velocity = escape_velocity_from_julia_set(new Complex(actual_coords(frame_height, frame_width, xmin, xmax, ymin, ymax, i, j)[0],
                                    actual_coords(frame_height, frame_width, xmin, xmax, ymin, ymax, i, j)[1])
                            , 250,
                            critical_val);

                    ///////////do not change color settings!\\\\\\\\\\\\\

                    img.setRGB(i, j, colormap.getRGB(escape_velocity, 1));

                    if (escape_velocity == 0) {
                        img.setRGB(i, j, Color.black.getRGB());
                    }
                }
            }
        } catch (IOException e) {
        }
        RescaleOp rescaleOp = new RescaleOp(1.2f, 15, null);
        //rescaleOp.filter(img, img);
        return img;
    }

    private static int escape_velocity_from_julia_set(Complex p_val, int bailout_val, Complex critical_point) {
        Complex jlia_iter = p_val;
        for (int i = 0; i < bailout_val; i++) {
            jlia_iter = Complex_ops.sum(Complex_ops.product(jlia_iter, jlia_iter), critical_point);
            if (jlia_iter.re > 10 || jlia_iter.re < -10 || jlia_iter.im > 10 || jlia_iter.im < -10) {
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
        return new double[]{true_x, true_y};
    }
}


package Work_from_2017.complex_nums.graphics.julia_set_research;

import Work_from_2017.complex_nums.Complex;
import Work_from_2017.complex_nums.Complex_ops;
import Work_from_2017.complex_nums.graphics.julia_set_research.helpers.ScreenData;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.EmptyStackException;
import java.util.Vector;

public class julia_set {
    public Complex C;
    public int bail;
    private boolean firstRun;
    private boolean waiting;


    public julia_set(Complex crit) {
        this.C = crit;
        this.bail = 500;
        this.firstRun = true;
    }

    public boolean is_in_set(Complex test, int bailout_val) {
        return escape_velocity(test, bailout_val) == 0;
    }

    /**
     * returns a picture of the set based on the screendata
     *
     * @param screen
     * @return
     * @throws Exception
     */
    public BufferedImage picture(ScreenData screen) throws Exception {

        System.out.println("picture was called!");
        System.out.println(screen);
        // for debugging
        BufferedImage img = new BufferedImage(screen.picture_width, screen.picture_height, BufferedImage.TYPE_INT_ARGB);
        BufferedImage colormap = ImageIO.read(new File(Paths.get(".").toAbsolutePath().normalize().toString()
                + "\\src\\Work_from_2017\\complex_nums\\graphics\\julia_set_research\\colormaps\\" + "Colormap500px.png"));

        // makes it so it find its working src directory in order to access the
        // file even if the workspace gets moved
        // the color-map is just an nice gradient image that was made in photoshop


        int[][] IntermediaryMatrix = new int[screen.picture_width][screen.picture_height];

        for (int[] row : IntermediaryMatrix)
            Arrays.fill(row, -1);


        class ComplexCalculationThread extends Thread {
            public int PartWidth;
            public int PartHeight;
            public int PartStartX;
            public int PartStartY;
            public String threadName;
            boolean prevglobalwaitval;
            private boolean Done;
            private Thread t;
            private Complex escape = new Complex(0, 0);

            // init all of the neccessary things for writing to the matrix
            public ComplexCalculationThread(String name, int Pw, int Ph, int pX, int pY) {
                this.PartWidth = Pw;
                this.PartHeight = Ph;
                this.PartStartX = pX;
                this.PartStartY = pY;
                this.threadName = name;

                this.prevglobalwaitval = false;
                this.Done = false;
                // System.out.println("Created new ComplexCalculationThread Thread writing " + name +
                // " starting at " + pX + "," + pY + " and going to " + (pX + Pw) + "," + (pY +
                // Ph));
                // for debugging
            }

            @Override
            public String toString() {
                return ("Created new ComplexCalculationThread Thread writing " + threadName + " starting at " + PartStartX + "," + PartStartY + " and going to " + (PartStartX + PartWidth) + "," + (PartStartY + PartHeight));
            }

            public Complex sum(Complex s1, Complex s2) {
                Complex s3 = new Complex(s1.re + s2.re, s1.im + s2.im);
                return s3;
            }

            public Complex product(Complex s1, Complex s2) {
                return new Complex(s1.re * s2.re - (s1.im * s2.im), s1.re * s2.im + (s1.im * s2.re));
            }

            public int escape_velocity(Complex test, int bailout_val) {

                Complex jlia_iter = test;
                for (int i = 0; i < bailout_val; i++) {
                    jlia_iter = sum(product(jlia_iter, jlia_iter), C);
                    if (jlia_iter.mag > 2) {
                        return bailout_val - i;
                    }
                }
                return 0;
                // calculates how quickly a point diverges

            }

            public double[] actual_coords(ScreenData screen, int cx, int cy) {
                // takes in coords and outputs the corrisponding vals from 2,-2
                double scaleh = (screen.zoom_y[1] - screen.zoom_y[0]) / screen.picture_height;
                // what one px corresponds to
                double scalew = (screen.zoom_x[1] - screen.zoom_x[0]) / screen.picture_width;
                double true_x;
                double true_y;
                true_x = screen.zoom_x[0] + (cx * scalew);
                true_y = screen.zoom_y[1] - (cy * scaleh);
                // because computer coords have 0,0 in the top left
                return new double[]{true_x, true_y};
            }

            public void run() {
                try {

                    while (!Done) {
                        int i = PartStartX;
                        int j = PartStartY;

                        // Complex escape = new Complex(0, 0);
                        while (i < PartStartX + PartWidth && !Done) {
                            while (j < PartStartY + PartHeight && !Done) {
                                // System.out.println("bitmap thread iterated, i am
                                // " + this.threadName);

                                escape.re = actual_coords(screen, i, j)[0];
                                escape.im = actual_coords(screen, i, j)[1];

                                IntermediaryMatrix[i][j] = escape_velocity(escape, bail);
                                // System.out.println(threadName+" calculated "+IntermediaryMatrix[i][j]);
                                // write the escape velocity to the matrix

                                j++;
                                sleep(10);
                            }
                            i++;

                            //wait and restart later
                            if (i == PartStartX + PartWidth || i> PartStartX+PartStartY) {
                                synchronized (this) {
                                    while (!waiting) {
                                        this.wait();
                                    }
                                }
                                i = PartStartX;
                                j = PartStartY;
                            }

                        }
                        //Done=true;
                    }

                } catch (InterruptedException e) {
                    System.out.println("Thread " + threadName + " interrupted.");
                }
                // System.out.println("Thread " + threadName + " exiting.");
            }

            public void start() {
                // System.out.println("Starting " + threadName);
                if (t == null) {
                    t = new Thread(this, threadName);
                    t.start();
                }
            }
            // restart the thread if it panics
        }

        class ImageWriter extends Thread {

            public int PartWidth;
            public int PartHeight;
            public int PartStartX;
            public int PartStartY;
            public String threadName;
            int black = Color.black.getRGB();
            private boolean prevglobalwaitval = false;
            private boolean Done = false;
            private Thread t;

            public ImageWriter(String name, int Pw, int Ph, int pX, int pY) {
                this.PartWidth = Pw;
                this.PartHeight = Ph;
                this.PartStartX = pX;
                this.PartStartY = pY;
                this.threadName = name;
                // System.out.println("Created new ImageWriter Thread writing " + name + "
                // starting at " + pX + "," + pY + " and going to " + (pX + Pw) + "," + (pY +
                // Ph));
            }

            public void run() {

                try {

                    while (!Done) {
                        int i = PartStartX;
                        int j = PartStartY;
                        int velocity;
                        while (i < PartStartX + PartWidth) {
                            while (j < PartStartY + PartHeight && !Done) {
                                velocity = IntermediaryMatrix[i][j];
                                if (velocity != -1) {
                                    if (velocity == 0) {

                                        // System.out.println("found a 0 point in the bitmap");
                                        img.setRGB(i, j, black);
                                    }
                                    // if it never escapes color it black

                                    else {
                                        //set the color based on gradient with a wraparound
                                        img.setRGB(i, j, colormap.getRGB((bail - velocity) % 500, 5));
                                    }
                                    sleep(15);
                                }
                                // protect from getting garbage data
                                else {
                                    sleep(25);
                                    velocity = IntermediaryMatrix[i][j];
                                    if (velocity == 0) {

                                        // System.out.println("found a 0 point in the intermed Matrix");
                                        img.setRGB(i, j, black);
                                    }
                                    // if it never escapes color it black

                                    else {
                                        img.setRGB(i, j, colormap.getRGB(bail - velocity, 5));
                                    }
                                    sleep(10);
                                }
                                j++;
                            }
                            i++;
                            if (i == PartStartX + PartWidth || i> PartStartX+PartStartY) {
                                synchronized (this) {
                                    while (!waiting) {
                                        this.wait();
                                    }
                                }
                                i = PartStartX;
                                j = PartStartY;
                            }


                        }
                        Done = true;


                    }


                } catch (InterruptedException e) {
                    System.out.println(threadName + " interrupted.");
                }

                // System.out.println("Thread " + threadName + " exiting.");
            }

            public void start() {
                // System.out.println("Starting " + threadName);
                if (t == null) {
                    t = new Thread(this, threadName);
                    t.start();
                }
                // if given a null thread restart a new one
            }

        }

        // throws exception if the threads will leave weird rows of dead pixels
        if (screen.picture_width % screen.threads != 0) {
            throw new Exception("the number of threads does not evenly divide the screen resolution");
        }
        // only init threads on first run

            ComplexCalculationThread[] ComplexCalculationThread_threads = new ComplexCalculationThread[screen.threads];
            ImageWriter[] ImageWriter_threads = new ImageWriter[screen.threads];
            // initiate the thread arrays
            int Xpartition_dist = screen.picture_width / screen.threads;
            int Ypartition_dist = screen.picture_height - 1;
            // set for how big the sections are
            int Xstart = 0;

            for (int i = 0; i < screen.threads; i++) {
                ComplexCalculationThread_threads[i] = new ComplexCalculationThread(("ComplexCalculationThread - " + i), Xpartition_dist,
                        Ypartition_dist, Xstart, 0);
                ComplexCalculationThread_threads[i].setPriority(10);
                ComplexCalculationThread_threads[i].start();
                Thread.sleep(5);
                // let the Intermediary Matrix writer get a head-start to avoid the image writer from reading null or garbage data
                ImageWriter_threads[i] = new ImageWriter(("ImageWriterThread - " + i), Xpartition_dist, Ypartition_dist,
                        Xstart, 0);
                ImageWriter_threads[i].setPriority(9);
                // we don't want the ImageWriters to get ahead and access unwritten data
                ImageWriter_threads[i].start();
                // start the threads
                if (Xstart + Xpartition_dist != screen.picture_width) {
                    Xstart += Xpartition_dist;
                } else {
                    Xstart += Xpartition_dist - 1;
                }
                // move to next partition start position
        }


        // return a pointer to the picture as the threads continue to write into it, is a bit slower but creates cool effect
        System.out.println("picture was returned!");
        return img;

    }

    public int escape_velocity(Complex test, int bailout_val) {

        Complex jlia_iter = test;
        for (int i = 0; i < bailout_val; i++) {
            jlia_iter = Complex_ops.sum(Complex_ops.product(jlia_iter, jlia_iter), this.C);
            if (jlia_iter.mag > 2) {
                return bailout_val - i;
            }
        }
        return 0;
        // calculates how quickly a point diverges

    }

    public Complex[] escape_points_Complex_nums(Complex test, int bail) {
        Vector<Complex> nums = new Vector<Complex>();
        Complex es_iter = test;
        int c = 0;
        while (es_iter.mag < 2 && c < bail) {
            c++;
            nums.add(es_iter);

            es_iter = Complex_ops.sum(Complex_ops.product(es_iter, es_iter), this.C);
        }
        if (c > 250) {
            throw new EmptyStackException();
        }
        Complex[] ca = new Complex[nums.size()];
        for (int i = 0; i < nums.size(); i++) {
            ca[i] = nums.get(i);
        }
        return ca;
    }

    public Polygon escape_points_computer_coords(Complex test, ScreenData screen) {
        Vector<Double> xs = new Vector<Double>();
        Vector<Double> ys = new Vector<Double>();
        Complex es_iter = test;
        int c = 0;
        while (es_iter.mag < 2 && c < 500) {
            c++;
            xs.add(es_iter.re);
            ys.add(es_iter.im);
            es_iter = Complex_ops.sum(Complex_ops.product(es_iter, es_iter), this.C);

            if (Complex_ops.sum(Complex_ops.product(es_iter, test), this.C).mag >= 2) {
                es_iter = Complex_ops.sum(Complex_ops.product(es_iter, test), this.C);
                xs.add(es_iter.re);
                ys.add(es_iter.im);
            }
        }
        // does a round of iteration saving all the points

        int[] xss = new int[xs.size()];
        int[] yss = new int[ys.size()];
        for (int i = 0; i < xs.size(); i++) {
            xss[i] = Complex_ops.computer_coords(screen.Moniter_width, screen.Moniter_height, screen.zoom_x[0],
                    screen.zoom_x[1], screen.zoom_y[0], screen.zoom_y[1], xs.get(i), ys.get(i))[0];
            yss[i] = Complex_ops.computer_coords(screen.Moniter_width, screen.Moniter_height, screen.zoom_x[0],
                    screen.zoom_x[1], screen.zoom_y[0], screen.zoom_y[1], xs.get(i), ys.get(i))[1];
        }
        // converts to polyline friendly datatypes

        for (int i = 0; i < xss.length; i++) {
            if (xss[i] > screen.Moniter_width) {
                xss[i] = screen.Moniter_width - 1;
            }
            if (xss[i] < 0) {
                xss[i] = 0;
            }
            if (yss[i] > screen.Moniter_height) {
                yss[i] = screen.Moniter_height - 1;
            }
            if (yss[i] < 0) {
                yss[i] = 0;
            }
        }
        // makes sure there are no out of bounds problems

        // System.out.println("escape points triggered! ");
        // for debuging
        return new Polygon(xss, yss, xss.length);
    }

    public boolean is_connected() {
        Complex iter = new Complex(0, 0);
        for (int i = 0; i < 100; i++) {
            iter = Complex_ops.sum(Complex_ops.product(iter, iter), this.C);
            if (iter.mag > 2) {
                return false;
            }
        }
        return true;
    }
}

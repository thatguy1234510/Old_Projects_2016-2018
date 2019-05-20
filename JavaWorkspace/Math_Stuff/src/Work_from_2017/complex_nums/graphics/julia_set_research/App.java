package Work_from_2017.complex_nums.graphics.julia_set_research;

import Work_from_2017.complex_nums.Complex;
import Work_from_2017.complex_nums.Complex_ops;
import Work_from_2017.complex_nums.graphics.julia_set_research.helpers.ScreenData;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.util.Scanner;

//DONE: speed up program with multithreading
//TODO: integrate julia sets into the app
//TODO: move imagewriter threads to the julia_research, have them continuesly update based on a matrix and pass the func a reference to it
//TODO: have imagewriter threads run a difference on the bitmap and only update the necessary pixels
//TODO: finish the zooming function on the mouselistner
//TODO: code a right click mouselistener to pan
//TODO: make pan just shift the image then render the remaining stuff;
//TODO: make better gradient
//DONE: code the gradient wraparound for the zooming
//TODO: integrate into a webpage with this as a backend

/*
* alternative plotting method:
* make a program that iterates through all pixels
* have it calculate escape points to circ of rad 2
* make a list of the escape points
* color escape points based on how many there are after it in the list
* make a note of which pixels you have visited and don't test them again
* this would mean you can color every pixel without testing every pixel
* I DONT KNOW WHY BUT THIS IS SLOWER!
* I MUST FIND OUT WHY!
* -writing to the same pixel twice due to the complex numbers having greater density then the pixelgrid
*/
//960 0,0 48s
public class App extends JFrame {

    private int Moniter_width = 2736;
    private int Moniter_height = 1824;
    // the res it gets scaled to
    private int picture_width = 2736; //960;
    private int picture_height = 1823; //640;
    // the render resolution
    private double[] zoom_x = { -2.25, 2.25 };
    private double[] zoom_y = { -1.5, 1.5 };
    private ScreenData MasterScreen = new ScreenData(Moniter_width, Moniter_height, picture_width, picture_height,
            zoom_x, zoom_y, 1836);
    // helper wrapper class to pass to other methods
    // private boolean MouseJustClicked=true;
    private boolean ReRender = false;
    int mx;
    int my;
    julia_set julia;
    private Image dbimage;
    private Graphics dbg;
    private Image pic;
    Complex M = new Complex(Complex_ops.actual_coords(MasterScreen, mx, my)[0],
            Complex_ops.actual_coords(MasterScreen, mx, my)[1]);

    public class AL extends KeyAdapter {
        public void keyPressed(KeyEvent e) {
            int keycode = e.getKeyCode();
            if (keycode == KeyEvent.VK_UP) {
                my -= 50;
            }
            if (keycode == KeyEvent.VK_DOWN) {
                my += 50;
            }
            if (keycode == KeyEvent.VK_LEFT) {
                mx -= 50;
            }
            if (keycode == KeyEvent.VK_RIGHT) {
                mx += 50;
            }
            if (keycode == KeyEvent.VK_SPACE) {
                mx = MouseInfo.getPointerInfo().getLocation().x;
                my = MouseInfo.getPointerInfo().getLocation().y;
            }
            if (keycode == KeyEvent.VK_R) {
                ReRender = true;
            }
            if (keycode == KeyEvent.VK_Z) {

            }

        }

        public void keyReleased(KeyEvent e) {

        }
    }

    public class MML extends MouseAdapter {
        public void mousePressed(MouseEvent ME) {

            mx = MouseInfo.getPointerInfo().getLocation().x;
            my = MouseInfo.getPointerInfo().getLocation().y;
            // MouseJustClicked=true;
        }

    }

    public class MWL implements MouseWheelListener {


        @Override
        public void mouseWheelMoved(MouseWheelEvent e) {

            zoom_x[0]+=Complex_ops.actual_coords(MasterScreen, mx, my)[0];
            zoom_x[1]+=Complex_ops.actual_coords(MasterScreen, mx, my)[0];
            zoom_y[0]+=Complex_ops.actual_coords(MasterScreen, mx, my)[1];
            zoom_y[1]+=Complex_ops.actual_coords(MasterScreen, mx, my)[1];
            //translate the screen to center on the point

            zoom_x[0]+=(0.1*e.getWheelRotation())*(zoom_x[1]-zoom_x[0]);
            zoom_x[1]-=(0.1*e.getWheelRotation())*(zoom_x[1]-zoom_x[0]);
            zoom_y[0]+=(0.1*e.getWheelRotation())*(zoom_y[1]-zoom_y[0]);
            zoom_y[1]-=(0.1*e.getWheelRotation())*(zoom_y[1]-zoom_y[0]);
            // it seems to be zooming out regardless of direction

            //reduce the view by 10% per scroll wheel notch
            ReRender=true;
            //re-render the frame
            System.out.println("wheel was triggered");
        }
    }
    // not working yet

    public App(julia_set j) throws Exception {

        this.julia = j;
        addMouseListener(new Work_from_2017.complex_nums.graphics.julia_set_research.App.MML());
        addKeyListener(new Work_from_2017.complex_nums.graphics.julia_set_research.App.AL());
        //addMouseWheelListener(new MWL());
        //isnt working right now

        // adds listners for events to change things about the image
        BufferedImage img = julia.picture(MasterScreen);

        this.pic = img;
        setLayout(new BorderLayout());
        setSize(Moniter_width, Moniter_height);
        setTitle("julia set explorer");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);

        setVisible(true);
        mx = 0;
        my = 0;

    }

    /* the paint function is called every loop and renders to the screen */
    public void paint(Graphics g) {
        //double buffering for rendering that doesn't overlap
        Image dbimage = createImage(getWidth(), getHeight());
        dbg = dbimage.getGraphics();
        dbg.drawImage(pic, 0, 0, MasterScreen.Moniter_width, MasterScreen.Moniter_height, this);
        try {
            paintComponent(dbg);
        } catch (Exception e) {
            e.printStackTrace();
        }
        g.drawImage(dbimage, 0, 0, MasterScreen.Moniter_width, MasterScreen.Moniter_height, this);

    }


    /* renders the interactive components */
    Polygon escape=new Polygon();
    Complex prevM= new Complex(0,0);

    public void RenderInteractivity(Graphics g){
        M = new Complex(Complex_ops.actual_coords(MasterScreen, mx, my)[0],
                Complex_ops.actual_coords(MasterScreen, mx, my)[1]);
        escape= julia.escape_points_computer_coords(M,MasterScreen);
        // the complex number corresponding to the mouse cursor position
        g.setColor(Color.WHITE);

        g.drawPolyline(
                escape.xpoints,
                escape.ypoints,
                escape.npoints);

        g.setColor(Color.WHITE);
        g.setFont(new Font("TimesRoman", Font.PLAIN, 18));
        g.drawString(
                "Number of points: "
                        + Integer.toString(escape.npoints),
                mx, my + 100);
        g.setFont(new Font("TimesRoman", Font.PLAIN, 18));
        g.drawString(M.name, mx, my);
        if(!M.equals(prevM)){
        if(escape.npoints<julia.bail){
        System.out.println("The point "+M+" takes "+escape.npoints+" iterations under the function: X^2 + "+julia.C+" to escape a circle of radius 2");
        }
        else{
            System.out.println("The point "+ M +"does not escape a circle of radius 2" +" under the function: X^2 + " + julia.C + " after "+julia.bail+" iterations");
        }
        }
        prevM=new Complex(M);
    }


    /* renders all the components together */
    public void paintComponent(Graphics g) throws Exception {
        if (ReRender) {
            System.out.println("rerender triggered");
            pic = julia.picture(MasterScreen);
            g.drawImage(pic, 0, 0, Moniter_width, Moniter_height, this);

            ReRender = false;
        }

        RenderInteractivity(g);

        Thread.sleep(100);
        repaint();

    }

    public static void main(String[] args) throws Exception {

        Scanner input = new Scanner(System.in);
        System.out.println("Please input the critical point in the form of A+Bi: ");

        Complex crit = new Complex(input);
        julia_set julia = new julia_set(crit);

        long start = System.nanoTime();
        new App(julia);
        long stop = System.nanoTime();
        System.out.println("took: " + (stop - start) / 1000000000 + " seconds to initialize all threads");

        input.close();

    }

}

package Work_from_2017.matrix_stuff.graphics_stuff;

import javax.swing.*;
import java.awt.*;

public class vector_graphics_gui extends JFrame {

    public vector_graphics_gui() {
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

        vector_graphics_gui frame = new vector_graphics_gui();


    }

    class draw_stuff extends JPanel {

        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            //TODO: write this

        }

    }
}

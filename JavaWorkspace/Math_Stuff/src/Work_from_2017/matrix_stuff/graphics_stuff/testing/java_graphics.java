package Work_from_2017.matrix_stuff.graphics_stuff.testing;

import Work_from_2017.matrix_stuff.graphics_stuff.Create;
import Work_from_2017.matrix_stuff.graphics_stuff.Transform;

import javax.swing.*;
import java.awt.*;

public class java_graphics extends JFrame {

    public java_graphics() {
        setLayout(new BorderLayout());
        add(new test_stuff(), BorderLayout.CENTER);
    }

    public static void main(String[] args) {
        java_graphics frame = new java_graphics();
        frame.setSize(1920, 1080);
        frame.setTitle("java_graphics");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null);
        frame.setResizable(false);
        frame.setVisible(true);

    }

    class test_stuff extends JPanel {

        protected void paintComponent(Graphics g) {

            Polygon pent = Create.reg_polygon(5, 200);

            g.translate(getWidth() / 2, getHeight() / 2);
            g.drawString("0,0", 0, 0);
            g.drawPolygon(pent);
            g.setColor(Color.blue);
            g.drawPolygon(Transform.scale(Transform.rotate(pent, Math.PI), 1.5));
            g.setColor(Color.red);
            g.drawPolygon(Transform.scale(Transform.reflect(pent, 1), 2));
            g.setColor(Color.green);
            g.drawPolygon(Transform.scale(Transform.translate(pent,200,200), 3));
        }

    }
}
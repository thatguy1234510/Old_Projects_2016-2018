package Work_from_2017.matrix_stuff.graphics_stuff;

import java.awt.*;

public class Create {

    public static Polygon reg_polygon(int sides, double r) {
        Polygon reg_poly = new Polygon();
        for (int i = 1; i < sides + 1; i++) {
            double theta = (2 * Math.PI) / sides * i;
            reg_poly.addPoint((int) getCart(r, theta)[0], (int) getCart(r, theta)[1]);
            // goes around a circle and adds points 2pi/sides apart
        }
        return Transform.rotate(reg_poly, 3 * Math.PI / 2);
        // i rotate 90 deg because it just looks more ascetically pleasing

    }

    private static double[] getCart(double r, double theta) {
        double[] rectcoords = new double[2];
        rectcoords[0] = r * Math.cos(theta);
        rectcoords[1] = r * Math.sin(theta);
        return rectcoords;
    }

}

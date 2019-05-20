package Work_from_2017.complex_nums.graphics.julia_set_research.helpers;

import java.util.Arrays;

/**
 * Created by proff on 6/28/2017.
 */
public class ScreenData {
    public int Moniter_width = 2736;
    public int Moniter_height = 1824;
    // the res it gets scaled to
    public int picture_width = 2736;
    public int picture_height = 1824;
    // the render resolution
    public double[] zoom_x = {-3, 3};
    public double[] zoom_y = {-2, 2};
    // the screen bounds
    public int threads;
    public ScreenData(int Mw, int Mh, int Pw, int Ph, double[] Zx, double[] Zy){
        this.Moniter_height=Mh;
        this.Moniter_width=Mw;
        this.picture_height=Ph;
        this.picture_width=Pw;
        this.zoom_x=Zx;
        this.zoom_y=Zy;
        this.threads=1;
    }
    public ScreenData(int Mw, int Mh, int Pw, int Ph, double[] Zx, double[] Zy,int t){
        this.Moniter_height=Mh;
        this.Moniter_width=Mw;
        this.picture_height=Ph;
        this.picture_width=Pw;
        this.zoom_x=Zx;
        this.zoom_y=Zy;
        this.threads=t;
    }

    @Override public String toString() {
        return String.format("The monitor is %s by %s and I am making a %s by %s picture, with xrange %s and yrange %s on %s thread(s) \n", Moniter_width, Moniter_height,picture_width,picture_height, Arrays.toString(zoom_x),Arrays.toString(zoom_y),threads);
    }
}

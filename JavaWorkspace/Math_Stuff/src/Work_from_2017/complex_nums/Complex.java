package Work_from_2017.complex_nums;
import java.util.Scanner;
import Work_from_2017.matrix_stuff.Matrix;

public class Complex {
    public double re;
    public double im;
    public double arg;
    public double mag;
    public String name;

    public Complex(double r, double i) {
        this.re = r;
        this.im = i;
        this.arg = Math.atan(i / r);
        this.mag = Math.sqrt(Math.pow(r, 2) + Math.pow(i, 2));

        //round to 4 decimal places for the printing
        double printRe=Math.round(this.re*10000);
        printRe/=10000;
        double printIm=Math.round(this.im*10000);
        printIm/=10000;
        this.name = printRe + "+" + printIm + "i";

    }
    /* Copy constructor: */
    public Complex(Complex cc) {
        this.re = cc.re;
        this.im = cc.im;
        this.arg = cc.arg;
        this.mag = cc.mag;
        this.name = cc.name;
    }

    /*Format is: a+bi */
    public Complex(String cmplxString){
        this.re=Double.parseDouble(cmplxString.split("\\+")[0]);
        this.im=Double.parseDouble(cmplxString.split("\\+")[1].split("i")[0]);
        this.arg = Math.atan(this.im / this.re);
        this.mag = Math.sqrt(Math.pow(this.re, 2) + Math.pow(this.im, 2));

        //round to 4 decimal places for the printing
        double printRe=Math.round(this.re*10000);
        printRe/=10000;
        double printIm=Math.round(this.im*10000);
        printIm/=10000;
        this.name = printRe + "+" + printIm + "i";
    }

    //remember to close your inputs!
    public Complex(Scanner input){
        this(input.nextLine());
    }

    @Override public String toString(){ return this.name;}


    public Complex conjugate() {
        Complex w = new Complex(re, -im);
        return w;
    }

    public Matrix get_mat() {
        double[] matarray = {re, -im, im, re};
        return new Matrix(matarray, 2, 2);
    }


}

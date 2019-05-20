package Work_from_2017.pyth_tripples;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Pythagorean_triples {
    public static void main(String[] args) {
        Scanner input = new Scanner(System.in);
        System.out.println("please input the upper limit for triples");
        int lim = Integer.parseInt(input.nextLine());
        long startTime = System.currentTimeMillis();
        check_elementals(try_pyths(lim));
        long stopTime = System.currentTimeMillis();

        System.out.println("Elapsed time was " + (stopTime - startTime) + " miliseconds.");
    }

    public static List<int[]> try_pyths(int lim) {
        int a = 3;
        int b = a;
        List<int[]> pyths = new ArrayList<>();
        while (a < lim) {
            b++;
            if (check_int(a, b)) {
                int[] pyth = {a, b, (int) Math.sqrt(Math.pow(a, 2) + (Math.pow(b, 2)))};
                pyths.add(pyth);
                //print_list(pyth);
            }
            if (b > ((Math.pow(a, 2) - 1) / 2)) {
                a++;
                b = a;

            }
        }
        return pyths;
    }

    public static void print_list(int[] pyths) {
        int i = 0;
        while (i < pyths.length) {

            i++;

        }

    }

    public static boolean check_int(double a, double b) {
        double c = Math.sqrt(Math.pow(a, 2) + (Math.pow(b, 2)));
        return Math.floor(c) == Math.ceil(c);
    }

    public static List<int[]> check_elementals(List<int[]> pyths) {
        List<int[]> elementals = new ArrayList<>();
        for (int i = 0; i < pyths.size(); i++) {
            int[] p_el = pyths.get(i);
            if (test_el(p_el)) {
                elementals.add(p_el);
            }
        }
        return elementals;
    }

    public static boolean test_el(int[] p_el) {
        int n = 1;
        while (n < p_el[0]) {
            n++;
            if (p_el[0] % n == 0 && p_el[1] % n == 0 && p_el[2] % n == 0) {
                return false;
            }
        }
        return true;
    }

}
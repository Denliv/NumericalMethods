package service;

import java.util.Arrays;

public class VectorService {
    public static double getVectorNorm(double[] vector) {
        double sum = 0;
        for (var i : vector) {
            sum += i * i;
        }
        return Math.sqrt(sum);
    }

    public static double[] getVectorsDiff(double[] a, double[] b) {
        double[] v = new double[a.length];
        for (int i = 0; i < a.length; ++i) {
            v[i] = a[i] - b[i];
        }
        return v;
    }

    public static double[] divideVector(double[] vector, double number) {
        return Arrays.stream(vector).map( o -> o / number).toArray();
    }
}

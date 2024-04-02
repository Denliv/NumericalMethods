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

    public static void getVectorsDiff(double[] a, double[] b, double[] result) {
        for (int i = 0; i < a.length; ++i) {
            result[i] = a[i] - b[i];
        }
    }

    public static void getVectorsDiff(double[] a, double[] b, boolean saveInA) {
        if (saveInA) {
            for (int i = 0; i < a.length; ++i) {
                a[i] = a[i] - b[i];
            }
        }
        else {
            for (int i = 0; i < a.length; ++i) {
                b[i] = a[i] - b[i];
            }
        }
    }

    public static double[] divideVector(double[] vector, double number) {
        return Arrays.stream(vector).map( o -> o / number).toArray();
    }

    public static double getScalar(double[] vect1, double[] vect2) {
        double result = 0;
        for (int i = 0; i < vect1.length; ++i) {
            result += vect1[i] * vect2[i];
        }
        return result;
    }
}

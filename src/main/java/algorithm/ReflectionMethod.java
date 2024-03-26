package algorithm;

import service.MatrixService;
import service.VectorService;

public class ReflectionMethod {
    public static double[][] getHouseholderMatrix(double[][] A, int j) {
        j--;
        int n = A.length - 1;
        double[][] Hj = MatrixService.eyePartial(n + 1, j);
        double[] aj = new double[n - j + 1];
        for (int i = j; i < n + 1; ++i) {
            aj[i - j] = -A[i][j];
        }
        double b0 = (aj[0] >= 0 ? 1 : -1) * VectorService.getVectorNorm(aj);
        aj[0] += b0;
        double[] w = VectorService.divideVector(aj, VectorService.getVectorNorm(aj));
        MatrixService.insertMatrix(Hj, MatrixService.getHjWithTop(n - j + 1, w), j);
        return Hj;
    }

    public static void getAjAndHouseholderSequence(double[][] A, double[][] H, int j, int[] permutation) {
        j--;
        int n = A.length - 1;
        double[][] Hj = MatrixService.eye(n + 1);
        double maxSum = Double.MIN_VALUE;
        for (int i = j; i < n + 1; ++i) {
            double sum = 0;
            for (int t = j; t < n + 1; ++t) {
                sum += A[t][i] * A[t][i];
            }
            if (sum > maxSum) {
                maxSum = sum;
                permutation[j] = i;
            }
        }
        MatrixService.changeColumns(A, j, permutation[j]);

        double[] aj = new double[n - j + 1];
        for (int i = j; i < n + 1; ++i) {
            aj[i - j] = -A[i][j];
        }
        double b0 = (aj[0] >= 0 ? 1 : -1) * VectorService.getVectorNorm(aj);
        aj[0] += b0;
        double[] w = VectorService.divideVector(aj, VectorService.getVectorNorm(aj));
        MatrixService.getHj(Hj, w, j);
        MatrixService.multiplyMatrix(Hj, H, false);
        MatrixService.multiplyMatrix(Hj, A, false);
    }

    public static void inverseMatrix(double[][] A) {
        var H = MatrixService.eye(A.length);
        var permutation = new int[A.length - 1];
        for (int i = 1; i < A.length; ++i) {
            getAjAndHouseholderSequence(A, H, i, permutation);
        }
        MatrixService.reverseUpperTriangular(A);
        MatrixService.multiplyMatrix(A, H, true);
        for (int i = A.length - 2; i >= 0; --i) {
            MatrixService.changeRows(A, permutation[i], i);
        }
    }
}

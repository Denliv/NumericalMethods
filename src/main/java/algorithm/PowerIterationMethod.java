package algorithm;

import static algorithm.ReflectionMethod.inverseMatrix;
import static service.MatrixService.*;
import static service.VectorService.*;

public class PowerIterationMethod {
    private static final double _epsilon = 1e-7;
    /*private static final int SIZE = 3;
    private static final double RANGE = 10;

    public static double[][] generateMatrix() {
        double[][] matrix = new double[SIZE][SIZE];
        for (int i = 0; i < SIZE; i++) {
            for (int j = i + 1; j < SIZE; j++) {
                matrix[i][j] = Math.floor(Math.random() * 2 * RANGE - RANGE);
                matrix[j][i] = matrix[i][j];
            }
            matrix[i][i] = Math.floor(Math.random() * 2 * RANGE - RANGE);
        }
        return matrix;
    }*/

    public static Double solve(double[][] matrix, double[] x) {
        int iter_num = 0;
        double[] uk = multiplyVectorOnNumber(x.clone(), 1 / getVectorNorm(x));
        double[] xk1 = multiplyMatrixOnVector(matrix, uk);
        double lambda = getScalar(xk1, uk);

        double[] uk1 = multiplyVectorOnNumber(xk1, 1 / getVectorNorm(xk1));
        while (getVectorNorm(residual(matrix, uk1, lambda)) > _epsilon) {
            if (iter_num > 1000) {
                return null;
            } else {
                iter_num++;
                uk = multiplyVectorOnNumber(xk1.clone(), 1 / getVectorNorm(xk1));
                xk1 = multiplyMatrixOnVector(matrix, uk);
                lambda = getScalar(xk1, uk);
                uk1 = multiplyVectorOnNumber(xk1, 1 / getVectorNorm(xk1));
            }
        }
        return lambda;
    }

    public static double[] residual(double[][] matrix, double[] u, double lambda) {
        double[] A1 = multiplyMatrixOnVector(matrix, u);
        double[] A2 = multiplyVectorOnNumber(u, lambda);
        getVectorsDiff(A1, A2, true);
        return A1;
    }

    public static void main(String[] args) {
        double[][] matrix = {{2, 2, 3}, {4, 5, 6}, {7, 8, 9}};
        double[] x0 = {1, 2, 3};
        System.out.print("x0 = ");
        printVector(x0);
        System.out.print("Matrix:");
        printMatrix(matrix);
        Double result = solve(matrix, x0);
        if (result != null)
            System.out.println("Максимальное по модулю собственное значение: " + result);
        else {
            System.out.println("Итерационный процесс расходится");
            return;
        }
        double[][] inv_matrix = copy(matrix);
        inverseMatrix(inv_matrix);
        System.out.print("Inv Matrix:");
        printMatrix(inv_matrix);
        result = solve(inv_matrix, x0);
        if (result != null)
            System.out.println("Минимальное по модулю собственное значение: " + 1/result);
        else {
            System.out.println("Итерационный процесс расходится");
            return;
        }
    }
}
package generator;

import algorithm.ReflectionMethod;
import org.junit.jupiter.api.Test;
import service.MatrixService;

import java.io.Console;

public class GeneratorTest {
    @Test
    void MatrixInversion_HouseholderMethodTest() {
        double[][] A1 = {{4, 1, -2, 2}, {1, 2, 0, 1}, {-2, 0, 3, -2}, {2, 1, -2, -1}};
        double[][] B1 = {{4, 1, -2, 2}, {1, 2, 0, 1}, {-2, 0, 3, -2}, {2, 1, -2, -1}};
        ReflectionMethod.inverseMatrix(A1);
        //Check
        MatrixService.multiplyMatrix(A1, B1, false);
        MatrixService.printMatrix(B1);
    }

    private static void getTableRow(double ALPHA, double BETA, double[][] a, double[][] aInv) {
        int n = 10;
        Generator generator = new Generator();
        double[] values = generator.myGen(a, aInv, n, ALPHA, BETA, 1, 2, 0, 1);
        double[][] aCopyInv = MatrixService.copy(a);
        ReflectionMethod.inverseMatrix(aCopyInv);

        double[][] z = new double[a.length][a.length];
        MatrixService.getMatrixSum(aCopyInv, aInv, z, false);
        double zNorm = MatrixService.getMatrixNorm(z);
        double[][] composition = new double[a.length][a.length];
        MatrixService.multiplyMatrix(aCopyInv, a, composition);
        MatrixService.getMatrixDifferenceWithEye(composition);

        System.out.printf("%17.8e", values[0]);
        System.out.printf("%23.8e", values[1]);
        System.out.printf("%25.8e", values[2]);
        System.out.printf("%22.8e", values[3]);
        System.out.printf("%19.8e", zNorm);
        System.out.printf("%20.8e", zNorm / MatrixService.getMatrixNorm(aInv));
        System.out.printf("%17.8e", MatrixService.getMatrixNorm(composition));
        System.out.printf("%17.8e", ALPHA);
        System.out.printf("%17.8e\n\n\n", BETA);
//        System.out.println("CHECK");
//        MatrixService.multiplyMatrix(a, aCopyInv, false);
//        MatrixService.printMatrix(aCopyInv);
//        System.out.println("Сгенерированная матрица");
//        MatrixService.printMatrix(a);
//        System.out.println("Обратная матрица, найденная генератором");
//        MatrixService.printMatrix(aInv);
//        System.out.println("Обратная матрица, найденная методом отражения");
//        MatrixService.printMatrix(aCopyInv);
    }

    @Test
    void MatrixInversion_OrdinaryMethod() {
        int n = 10;
        double ALPHA = 1.0;
        double BETA = 1000.0;
        double[][] a = new double[n][n];
        double[][] aInv = new double[n][n];
        System.out.println(
                "||      A      ||      ||    A_inv    ||        //    obusl    //  " +
                        "   ||    R_gen    ||      ||    Z    ||" +
                        "             кси          p                    alpha     beta");
        while (BETA <= 1e17) {
            getTableRow(ALPHA, BETA, a, aInv);
            BETA *= 10;
        }
        BETA = 1000;
        System.out.println("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~Уменьшаем alpha~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~\n\n");
        System.out.println(
                "||      A      ||      ||    A_inv    ||        //    obusl    //  " +
                        "   ||    R_gen    ||      ||    Z    ||" +
                        "             кси          p                    alpha     beta");
        while (ALPHA >= 1e-17) {
            getTableRow(ALPHA, BETA, a, aInv);
            ALPHA /= 10;
        }

    }
}


package generator;

import algorithm.ReflectionMethod;
import algorithm.SDM;
import org.junit.jupiter.api.Test;
import service.MatrixService;
import service.VectorService;

import java.io.Console;
import java.util.Arrays;
import java.util.Random;

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

    private static void getTableRowIter(double ALPHA, double BETA, double[][] a, double[][] aInv, double[] x_real, double eps) {
        int n = 10;
        Generator generator = new Generator();
        double[] values = generator.myGen( a, aInv, n, ALPHA, BETA, 1, 2, 0, 1 );

        double[] b = new double[a.length];
        MatrixService.multiplyMatrixOnColumn(a, x_real, b);
        double[] x_temp = SDM.calculate(a, b, eps);

        double[] z = VectorService.getVectorsDiff(x_real, x_temp);
        double zNorm = VectorService.getVectorNorm(z);


        double[] composition = new double[a.length];
        MatrixService.multiplyMatrixOnColumn(a, x_temp, composition);
        VectorService.getVectorsDiff(composition, b, true);

        System.out.printf("%17.8e", values[0]);
        System.out.printf("%23.8e", values[1]);
        System.out.printf("%25.8e", values[2]);
        System.out.printf("%22.8e", values[3]);
        System.out.printf("%19.8e", zNorm);
        System.out.printf("%20.8e", zNorm / VectorService.getVectorNorm(x_real));
        System.out.printf("%17.8e", VectorService.getVectorNorm(composition));
        System.out.printf("%17.8e", ALPHA);
        System.out.printf("%17.8e\n\n\n", BETA);
        System.out.println("CHECK");
        System.out.println(Arrays.toString(z));
    }

    @Test
    void MatrixInversion_OrdinaryMethod() {
        int n = 10;
        double ALPHA = 1.0;
        double BETA = 1000.0;
        double[][] a = new double[n][n];
        double[][] aInv = new double[n][n];
        System.out.println("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~Увеличиваем beta~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~\n\n");
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

    @Test
    void SDM_Test() {
        Generator generator = new Generator();
        double ALPHA = 1.0;
        double BETA = 1000.0;
        int n = 4;
        double[][] A = new double[n][n];
        double[][] A_Inv = new double[n][n];
        generator.myGen( A, A_Inv, n, ALPHA, BETA, 1, 2, 0, 1 );
        MatrixService.printMatrix(A);
        double[] x = {1, 1, 1, 1};
        double[] b = new double[A.length];
        MatrixService.multiplyMatrixOnColumn(A, x, b);
        var x_temp = SDM.calculate(A, b, 1e-9);
        System.out.println("Real: " + Arrays.toString(x));
        System.out.println("Fact: " + Arrays.toString(x_temp));
    }

    @Test
    void MatrixInversion_IterationMethod() {
        int n = 10;
        double ALPHA = 1.0;
        double BETA = 1000.0;
        double[][] a = new double[n][n];
        double[][] aInv = new double[n][n];
        double[] x = new double[n];
        Random random = new Random();
        for (int i = 0; i < n; ++i) {
            x[i] = random.nextInt(10) + 1;
        }
        double eps = 1e-9;
        System.out.println("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~Увеличиваем beta~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~\n\n");
        System.out.println(
                "||      A      ||      ||    A_inv    ||        //    obusl    //  " +
                        "   ||    R_gen    ||      ||    Z    ||" +
                        "             кси          p                    alpha     beta");
        while (BETA <= 1e17) {
            getTableRowIter(ALPHA, BETA, a, aInv, x, eps);
            BETA *= 10;
        }
        BETA = 1000;
        System.out.println("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~Уменьшаем alpha~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~\n\n");
        System.out.println(
                "||      A      ||      ||    A_inv    ||        //    obusl    //  " +
                        "   ||    R_gen    ||      ||    Z    ||" +
                        "             кси          p                    alpha     beta");
        while (ALPHA >= 1e-17) {
            getTableRowIter(ALPHA, BETA, a, aInv, x, eps);
            ALPHA /= 10;
        }
    }
}


package algorithm;

import service.MatrixService;
import service.VectorService;

public class SDM {
    public static double[] calculate(double[][] A, double[] b, double eps) {
        int count = 0;
        double maxi = 0;
        double[] sj = new double[A.length];
        double[] xj = new double[A.length];
        for (int i = 0; i < A.length; ++i) {
            xj[i] = 0;
        }
        double[] xjp1 = new double[A.length];
        double TjNorm = 0;
        do {
            calculateSj(A, xj, b, sj);
            double[] Asj = new double[A.length];
            MatrixService.multiplyMatrixOnColumn(A, sj, Asj);
            double tau = calculateTau(sj, Asj);

            double[][] Tj = new double[A.length][A.length];
            calculateTj(tau, A, Tj);
            TjNorm = MatrixService.getMatrixNorm(Tj);

            for (int i = 0; i < A.length; ++i) {
                xjp1[i] = xj[i] - tau * sj[i];
            }
            maxi = Math.abs(xjp1[0] - xj[0]);
            for (int i = 0; i < A.length; ++i) {
                if (Math.abs(xjp1[i] - xj[i]) > maxi) {
                    maxi = Math.abs(xjp1[i] - xj[i]);
                }
                xj[i] = xjp1[i];
            }
            count++;
//        } while ((TjNorm * maxi) / (1 - TjNorm) >= eps);
        } while (maxi >= eps);
        return xj;
    }

    private static double calculateTau(double[] sj, double[] Asj) {
        var temp = VectorService.getScalar(Asj, sj);
        return Math.abs(temp) < 1e-15 ? 0 : VectorService.getScalar(sj, sj) / temp;
    }

    private static void calculateTj(double tau, double[][] A, double[][] Tj) {
        MatrixService.getEyeDifferenceWithMatrix(A, Tj, tau);
    }

    private static void calculateSj(double[][] A, double[] xj, double[] b, double[] sj) {
        MatrixService.multiplyMatrixOnColumn(A, xj, sj);
        VectorService.getVectorsDiff(sj, b, true);
    }
}

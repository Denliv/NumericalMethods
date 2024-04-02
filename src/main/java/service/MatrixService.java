package service;

import java.util.Arrays;

public class MatrixService {
    public static double[][] copy(double[][] arr) {
        double[][] copy = new double[arr.length][];
        for (int i = 0; i < arr.length; i++) {
            copy[i] = Arrays.copyOf(arr[i], arr[i].length);
        }
        return copy;
    }

    public static double[][] eye(int n) {
        double[][] matrix = new double[n][n];
        for (int i = 0; i < n; ++i) {
            for (int j = 0; j < n; ++j) {
                matrix[i][j] = 0;
            }
        }
        for (int i = 0; i < n; ++i) {
            matrix[i][i] = 1;
        }
        return matrix;
    }

    public static double[][] eyePartial(int n, int t) {
        double[][] matrix = new double[n][n];
        for (int i = 0; i < n; ++i) {
            for (int j = 0; j < n; ++j) {
                matrix[i][j] = 0;
            }
        }
        for (int i = 0; i < t; ++i) {
            matrix[i][i] = 1;
        }
        return matrix;
    }

    public static void insertMatrix(double[][] originalMatrix, double[][] partMatrix, int position) {
        if (partMatrix.length + position > originalMatrix.length)
            throw new IllegalArgumentException("partMatrix size or it's position in originalMatrix is illegal\n");
        int n = originalMatrix.length;
        for (int i = position; i < n; ++i) {
            for (int j = position; j < n; ++j) {
                originalMatrix[i][j] = partMatrix[i - position][j - position];
            }
        }
    }

    public static double[][] getMatrixFromVectors(double[] a, double[] b) {
        int n = a.length;
        double[][] matrix = new double[n][n];
        for (int i = 0; i < n; ++i) {
            for (int j = 0; j < n; ++j) {
                matrix[i][j] = a[i] * b[j];
            }
        }
        return matrix;
    }

    public static double[][] getHjWithTop(int n, double[] w) {
        double[][] matrix = new double[n][n];
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                matrix[i][j] = (i == j ? 1 : 0) - 2 * w[i] * w[j];
            }
        }
        return matrix;
    }

    public static void getHj(double[][] matrix, double[] w, int j) {
        int n = matrix.length;
        for (int i = j; i < n; ++i) {
            for (int t = j; t < n; ++t) {
                matrix[i][t] -= 2 * w[i - j] * w[t - j];
            }
        }
    }

//    public static void getXjp1(double[][] Tj, double[] xj, double tau, double[] b, double[] result) {
//        for (int i = 0; i < Tj.length; i++) {
//            result[i] = tau * b[i];
//            for (int j = 0; j < Tj.length; j++) {
//                result[i] += Tj[i][j] * xj[j];
//            }
//        }
//    }

    public static void multiplyMatrix(double[][] matrixA, double[][] matrixB, double[][] matrixResult) {
        for (int i = 0; i < matrixA.length; i++) {
            for (int j = 0; j < matrixA.length; j++) {
                matrixResult[i][j] = 0;
                for (int k = 0; k < matrixA.length; k++) {
                    matrixResult[i][j] += matrixA[i][k] * matrixB[k][j];
                }
            }
        }
    }

    public static void multiplyMatrix(double[][] matrixA, double[][] matrixB, boolean saveToMatrixA) {
        if (saveToMatrixA) {
            for (int i = 0; i < matrixA.length; i++) {
                double[] temp = new double[matrixA.length];
                for (int j = 0; j < matrixA.length; j++) {
                    temp[j] = 0;
                    for (int k = 0; k < matrixA.length; k++) {
                        temp[j] += matrixA[i][k] * matrixB[k][j];
                    }
                }
                for (int j = 0; j < matrixA.length; ++j) {
                    matrixA[i][j] = temp[j];
                }
            }
        } else {
            for (int i = 0; i < matrixA.length; i++) {
                double[] temp = new double[matrixA.length];
                for (int j = 0; j < matrixA.length; j++) {
                    temp[j] = 0;
                    for (int k = 0; k < matrixA.length; k++) {
                        temp[j] += matrixA[j][k] * matrixB[k][i];
                    }
                }
                for (int j = 0; j < matrixA.length; ++j) {
                    matrixB[j][i] = temp[j];
                }
            }
        }
    }

    public static void multiplyMatrixOnColumn(double[][] matrixA, double[] column, double[] columnRes) {
        for (int i = 0; i < matrixA.length; i++) {
            columnRes[i] = 0;
            for (int j = 0; j < matrixA.length; j++) {
                columnRes[i] += matrixA[i][j] * column[j];
            }
        }
    }

    public static void multiplyMatrixOnColumn(double[][] matrixA, double[] column, int columnNum) {
        for (int i = 0; i < matrixA.length; i++) {
            double res = 0;
            for (int j = 0; j < matrixA.length; j++) {
                res += matrixA[i][j] * column[j];
            }
            matrixA[i][columnNum] = res;
        }
    }

    public static void multiplyMatrixOnNumber(double[][] matrixA, double number) {
        for (int i = 0; i < matrixA.length; i++) {
            for (int j = 0; j < matrixA.length; j++) {
                matrixA[i][j] *= number;
            }
        }
    }

    public static void reverseUpperTriangular(double[][] matrix) {
        double[] copyElements = new double[matrix.length];
        for (int i = 0; i < matrix.length; i++) {
            copyElements[i] = matrix[i][i];
            matrix[i][i] = 1 / matrix[i][i];
        }
        for (int i = 0; i < matrix.length; i++) {
            for (int j = i + 1; j < matrix.length; j++) {
                double newElem = 0;
                for (int k = i; k <= j - 1; k++) {
                    newElem += matrix[i][k] * matrix[k][j];
                }
                newElem *= (-1 / copyElements[j]);
                matrix[i][j] = newElem;
            }
        }
    }

    public static void getMatrixSum(double[][] matrixA, double[][] matrixB, double[][] matrixResult, boolean sum) {
        for (int i = 0; i < matrixA.length; i++) {
            for (int j = 0; j < matrixA.length; j++) {
                if (sum)
                    matrixResult[i][j] = matrixA[i][j] + matrixB[i][j];
                else
                    matrixResult[i][j] = matrixA[i][j] - matrixB[i][j];
            }
        }
    }

    public static void getMatrixDiff(double[][] matrixA, double[][] matrixB) {
        for (int i = 0; i < matrixA.length; i++) {
            for (int j = 0; j < matrixA.length; j++) {
                matrixA[i][j] -= matrixB[i][j];
            }
        }
    }

    public static void getMatrixDifferenceWithEye(double[][] matrix) {
        for (int i = 0; i < matrix.length; i++) {
            matrix[i][i] -= 1;
        }
    }

    public static void getEyeDifferenceWithMatrix(double[][] matrix, double[][] result, double koeff) {
        for (int i = 0; i < matrix.length; i++) {
            for (int j = 0; j < matrix.length; ++j) {
                result[i][j] = (i == j ? 1 : 0) - koeff * matrix[i][j];
            }
        }
    }

    public static double[][] getEyeDifferenceWithMatrix(double[][] matrix, double koeff) {
        double[][] result = new double[matrix.length][matrix.length];
        for (int i = 0; i < matrix.length; i++) {
            for (int j = 0; j < matrix.length; ++j) {
                result[i][j] = (i == j ? 1 : 0) - koeff * matrix[i][j];
            }
        }
        return result;
    }

    public static double getMatrixNorm(double[][] matrix) {
        double norm = 0;
        for (double[] doubles : matrix) {
            double buff = 0;
            for (double aDouble : doubles) {
                buff += Math.abs(aDouble);
            }
            if (buff > norm)
                norm = buff;
        }
        return norm;
    }

    public static void changeRows(double[][] matrix, int i, int j)
    {
        if (i != j)
        {
            double[] buff = matrix[i];
            matrix[i] = matrix[j];
            matrix[j] = buff;
        }
    }

    public static void changeColumns(double[][] matrix, int firstColumn, int secondColumn)
    {
        if (firstColumn != secondColumn)
        {
            for (int i = 0; i < matrix.length; i++) {
                double buff = matrix[i][firstColumn];
                matrix[i][firstColumn] = matrix[i][secondColumn];
                matrix[i][secondColumn] = buff;
            }
        }
    }

    public static void printMatrix(double[][] matrix) {
        System.out.println();
        for (double[] doubles : matrix) {
            for (int j = 0; j < matrix.length; ++j) {
                System.out.printf("%.2f  ", doubles[j]);
            }
            System.out.println();
        }
    }
}
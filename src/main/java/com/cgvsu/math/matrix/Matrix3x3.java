package com.cgvsu.math.matrix;

import com.cgvsu.math.vector.Vector3f;

public class Matrix3x3 {
    private float[][] matrix;

    public Matrix3x3() {
        matrix = new float[3][3];
    }

    public Matrix3x3(final float[][] matrix) {
        if (matrix.length != 3) {
            //throw new Exception("No valid matrix size!");
        }

        this.matrix = matrix;
    }

    public float get(int i, int j) {
        return matrix[i][j];
    }

    public float[][] getMatrix() {
        return matrix;
    }

    public Matrix3x3 sum(final Matrix3x3 m) throws Exception {
        float[][] result = new float[3][3];
        for (int i = 0; i < matrix.length; i++) {
            for (int j = 0; j < matrix[i].length; j++) {
               result[i][j] = matrix[i][j] + m.matrix[i][j];
            }
        }
        return new Matrix3x3(result);
    }

    public Matrix3x3 subtraction(final Matrix3x3 m) throws Exception {
        float[][] result = new float[3][3];
        for (int i = 0; i < matrix.length; i++) {
            for (int j = 0; j < matrix[i].length; j++) {
                result[i][j] = matrix[i][j] - m.matrix[i][j];
            }
        }
        return new Matrix3x3(result);
    }

    public Vector3f multiplyOnVector(final Vector3f v) {
        float x = matrix[0][0] * v.getX() + matrix[0][1] * v.getX() + matrix[0][2] * v.getX();
        float y = matrix[1][0] * v.getY() + matrix[1][1] * v.getY() + matrix[1][2] * v.getY();
        float z = matrix[2][0] * v.getZ() + matrix[2][1] * v.getZ() + matrix[2][2] * v.getZ();
        return new Vector3f(x, y, z);
    }

    public Matrix3x3 mul(Matrix3x3 m) {
        float[][] result = new float[3][3];
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                for (int k = 0; k < 3; k++) {
                    result[i][j] += matrix[i][k] * m.matrix[k][j];
                }
            }
        }
        return new Matrix3x3(result);
    }

    public Matrix3x3 transpose() throws Exception {
        float[][] transposeMatrix = new float[3][3];
        for (int i = 0; i < matrix.length; i++) {
            for (int j = 0; j < matrix[i].length; j++) {
                transposeMatrix[i][j] = matrix[j][i];
            }
        }
        return new Matrix3x3(transposeMatrix);
    }

    public Matrix3x3 getZeroMatrix() {
        return new Matrix3x3();
    }

    public Matrix3x3 getOneMatrix() throws Exception {
        float[][] one = {
                {1, 0, 0},
                {0, 1, 0},
                {0, 0, 1}
        };
        return new Matrix3x3(one);
    }

    public boolean equals(Matrix3x3 matrix3x3) {
        boolean b = true;
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                b = Float.valueOf(matrix[i][j]).equals(matrix3x3.matrix[i][j]);
            }
        }
        return b;
    }

}

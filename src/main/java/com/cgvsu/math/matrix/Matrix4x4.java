package com.cgvsu.math.matrix;

import com.cgvsu.math.vector.Vector4f;

public class Matrix4x4 {
    private float[][] matrix;

    public Matrix4x4() {
        matrix = new float[4][4];
    }

    public Matrix4x4(final float[][] matrix){
        this.matrix = matrix;
    }

    public float get(int row, int column) {
        return matrix[row][column];
    }

    public Matrix4x4 clone()  {
        return new Matrix4x4(matrix);
    }

    public Matrix4x4 sum(final Matrix4x4 m) throws Exception {
        float[][] result = new float[4][4];
        for (int i = 0; i < matrix.length; i++) {
            for (int j = 0; j < matrix[i].length; j++) {
                result[i][j] = matrix[i][j] + m.matrix[i][j];
            }
        }
        return new Matrix4x4(result);
    }


    public Matrix4x4 subtraction(final Matrix4x4 m) throws Exception {
        float[][] result = new float[4][4];
        for (int i = 0; i < matrix.length; i++) {
            for (int j = 0; j < matrix[i].length; j++) {
                result[i][j] = matrix[i][j] - m.matrix[i][j];
            }
        }
        return new Matrix4x4(result);
    }


    public Vector4f multiplyOnVector(final Vector4f v) {
        float x = matrix[0][0] * v.getX() + matrix[0][1] * v.getX() + matrix[0][2] * v.getX() + matrix[0][3] * v.getX();
        float y = matrix[1][0] * v.getY() + matrix[1][1] * v.getY() + matrix[1][2] * v.getY() + matrix[1][3] * v.getY();
        float z = matrix[2][0] * v.getZ() + matrix[2][1] * v.getZ() + matrix[2][2] * v.getZ() + matrix[2][3] * v.getZ();
        float k = matrix[3][0] * v.getK() + matrix[3][1] * v.getK() + matrix[3][2] * v.getK() + matrix[3][3] * v.getK();
        return new Vector4f(x, y, z, k);
    }

    public Matrix4x4 mul(Matrix4x4 m) {
        float[][] result = new float[4][4];
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                for (int k = 0; k < 4; k++) {
                    result[i][j] += matrix[i][k] * m.matrix[k][j];
                }
            }
        }
        return new Matrix4x4(result);
    }

    public Matrix4x4 transpose() throws Exception {
        float[][] transposeMatrix = new float[4][4];
        for (int i = 0; i < matrix.length; i++) {
            for (int j = 0; j < matrix[i].length; j++) {
                transposeMatrix[i][j] = matrix[j][i];
            }
        }
        return new Matrix4x4(transposeMatrix);
    }

    public static Matrix4x4 getZeroMatrix() {
        return new Matrix4x4();
    }

    public static Matrix4x4 getOneMatrix() throws Exception {
        float[][] one = {
                {1, 0, 0, 0},
                {0, 1, 0, 0},
                {0, 0, 1, 0},
                {0, 0, 0, 1}
        };
        return new Matrix4x4(one);
    }

}

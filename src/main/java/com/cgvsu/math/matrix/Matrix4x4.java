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

    public float[][] getMatrix() {
        return matrix;
    }
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < matrix.length; i++) {
            for (int j = 0; j < matrix[0].length; j++) {
                stringBuilder.append(j + " ");
            }
            stringBuilder.append('\n');
        }
        return stringBuilder.toString();
    }

    public static Matrix4x4 invert(float[][] a) {

        int n = a.length;

        float[][] x = new float[n][n];

        float[][] b = new float[n][n];

        int[] index = new int[n];

        for (int i=0; i<n; ++i)

            b[i][i] = 1;



        // Transform the matrix into an upper triangle

        gaussian(a, index);



        // Update the matrix b[i][j] with the ratios stored

        for (int i=0; i<n-1; ++i)

            for (int j=i+1; j<n; ++j)

                for (int k=0; k<n; ++k)

                    b[index[j]][k]

                            -= a[index[j]][i]*b[index[i]][k];



        // Perform backward substitutions

        for (int i=0; i<n; ++i)

        {

            x[n-1][i] = b[index[n-1]][i]/a[index[n-1]][n-1];

            for (int j=n-2; j>=0; --j)

            {

                x[j][i] = b[index[j]][i];

                for (int k=j+1; k<n; ++k)

                {

                    x[j][i] -= a[index[j]][k]*x[k][i];

                }

                x[j][i] /= a[index[j]][j];

            }

        }

        return new Matrix4x4(x);

    }



// Method to carry out the partial-pivoting Gaussian

// elimination.  Here index[] stores pivoting order.



    private static void gaussian(float[][] a, int[] index)

    {

        int n = index.length;

        float[] c = new float[n];



        // Initialize the index

        for (int i=0; i<n; ++i)

            index[i] = i;



        // Find the rescaling factors, one from each row

        for (int i=0; i<n; ++i)

        {

            float c1 = 0;

            for (int j=0; j<n; ++j)

            {

                float c0 = Math.abs(a[i][j]);

                if (c0 > c1) c1 = c0;

            }

            c[i] = c1;

        }



        // Search the pivoting element from each column

        int k = 0;

        for (int j=0; j<n-1; ++j)

        {

            double pi1 = 0;

            for (int i=j; i<n; ++i)

            {

                double pi0 = Math.abs(a[index[i]][j]);

                pi0 /= c[index[i]];

                if (pi0 > pi1)

                {

                    pi1 = pi0;

                    k = i;

                }

            }



            // Interchange rows according to the pivoting order

            int itmp = index[j];

            index[j] = index[k];

            index[k] = itmp;

            for (int i=j+1; i<n; ++i)

            {

                float pj = a[index[i]][j]/a[index[j]][j];



                // Record pivoting ratios below the diagonal

                a[index[i]][j] = pj;



                // Modify other elements accordingly

                for (int l=j+1; l<n; ++l)

                    a[index[i]][l] -= pj*a[index[j]][l];

            }

        }

    }
}

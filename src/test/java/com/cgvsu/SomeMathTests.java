package com.cgvsu;

import com.cgvsu.math.matrix.Matrix4x4;
import com.cgvsu.math.vector.Vector3f;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import static com.cgvsu.render_engine.GraphicConveyor.multiplyMatrix4ByVector3;

public class SomeMathTests {

    @Test
    public void multiplyMatrix4ByVector3Test01() {
        float[][] matr = new float[][] {
                {1.732f, 1,  0, 10},
                {-1,  1.732f,0, 10},
                {0,      0,  2, 0},
                {0,      0,  0, 1}
        };
        Matrix4x4 matrix4x4 = new Matrix4x4(matr);
        Vector3f vector3f = new Vector3f(10, 10, 10);
        Vector3f expectedResult = new Vector3f(37.32f, 17.32f, 20);
        Vector3f result = multiplyMatrix4ByVector3(matrix4x4, vector3f);
        Assertions.assertEquals(result, expectedResult);
    }

    @Test
    public void multiplyMatrix4ByVector3Test02() {
        float[][] matr = new float[][] {
                {1.732f, 1,  0, 10},
                {-1,  1.732f,0, 10},
                {0,      0,  2, 0},
                {0,      0,  0, 1}
        };
        Matrix4x4 matrix4x4 = new Matrix4x4(matr);
        Vector3f vector3f = new Vector3f(10, 0, 0);
        Vector3f expectedResult = new Vector3f(27.32f, 0, 0);
        Vector3f result = multiplyMatrix4ByVector3(matrix4x4, vector3f);
        Assertions.assertEquals(result, expectedResult);
    }

    @Test
    public void multiplyMatrix4ByVector3Test03() {
        float[][] matr = new float[][] {
                {1.732f, 1,  0, 10},
                {-1,  1.732f,0, 10},
                {0,      0,  2, 0},
                {0,      0,  0, 1}
        };
        Matrix4x4 matrix4x4 = new Matrix4x4(matr);
        Vector3f vector3f = new Vector3f(0, 10, 0);
        Vector3f expectedResult = new Vector3f(20f, 27.32f, 0);
        Vector3f result = multiplyMatrix4ByVector3(matrix4x4, vector3f);
        Assertions.assertEquals(result, expectedResult);
    }

    @Test
    public void multiplyMatrix4ByVector3Test04() {
        float[][] matr = new float[][] {
                {1.732f, 1,  0, 10},
                {-1,  1.732f,0, 10},
                {0,      0,  2, 0},
                {0,      0,  0, 1}
        };
        Matrix4x4 matrix4x4 = new Matrix4x4(matr);
        Vector3f vector3f = new Vector3f(0, 0, 10);
        Vector3f expectedResult = new Vector3f(10f, 10f, 20);
        Vector3f result = multiplyMatrix4ByVector3(matrix4x4, vector3f);
        Assertions.assertEquals(result, expectedResult);
    }
}

package com.cgvsu;

import com.cgvsu.math.matrix.Matrix4x4;
import com.cgvsu.math.point.Point2f;
import com.cgvsu.math.vector.Vector3f;
import com.cgvsu.render_engine.GraphicConveyor;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;


import static com.cgvsu.render_engine.GraphicConveyor.multiplyMatrix4ByVector3;

public class SomeMathTests {

    @Test
    public void multiplyMatrix4ByVector3Test01() {
        float[][] matrix = new float[][] {
                {1.732f, 1,  0, 10},
                {-1,  1.732f,0, 10},
                {0,      0,  2, 0},
                {0,      0,  0, 1}
        };
        Matrix4x4 matrix4x4 = new Matrix4x4(matrix);
        Vector3f vector3f = new Vector3f(10, 10, 10);
        Vector3f expectedResult = new Vector3f(37.32f, 17.32f, 20);
        Vector3f result = multiplyMatrix4ByVector3(matrix4x4, vector3f);
        Assertions.assertEquals(result, expectedResult);
    }

    @Test
    public void multiplyMatrix4ByVector3Test02() {
        float[][] matrix = new float[][] {
                {1.732f, 1,  0, 10},
                {-1,  1.732f,0, 10},
                {0,      0,  2, 0},
                {0,      0,  0, 1}
        };
        Matrix4x4 matrix4x4 = new Matrix4x4(matrix);
        Vector3f vector3f = new Vector3f(10, 0, 0);
        Vector3f expectedResult = new Vector3f(27.32f, 0, 0);
        Vector3f result = multiplyMatrix4ByVector3(matrix4x4, vector3f);
        Assertions.assertEquals(result, expectedResult);
    }

    @Test
    public void multiplyMatrix4ByVector3Test03() {
        float[][] matrix = new float[][] {
                {1.732f, 1,  0, 10},
                {-1,  1.732f,0, 10},
                {0,      0,  2, 0},
                {0,      0,  0, 1}
        };
        Matrix4x4 matrix4x4 = new Matrix4x4(matrix);
        Vector3f vector3f = new Vector3f(0, 10, 0);
        Vector3f expectedResult = new Vector3f(20f, 27.32f, 0);
        Vector3f result = multiplyMatrix4ByVector3(matrix4x4, vector3f);
        Assertions.assertEquals(result, expectedResult);
    }

    @Test
    public void multiplyMatrix4ByVector3Test04() {
        float[][] matrix = new float[][] {
                {1.732f, 1,  0, 10},
                {-1,  1.732f,0, 10},
                {0,      0,  2, 0},
                {0,      0,  0, 1}
        };
        Matrix4x4 matrix4x4 = new Matrix4x4(matrix);
        Vector3f vector3f = new Vector3f(0, 0, 10);
        Vector3f expectedResult = new Vector3f(10f, 10f, 20);
        Vector3f result = multiplyMatrix4ByVector3(matrix4x4, vector3f);
        Assertions.assertEquals(result, expectedResult);
    }

    @Test
    public void lookAtTest() throws Exception {
        Matrix4x4 result = GraphicConveyor.lookAt(new Vector3f(100, 210, 30), new Vector3f(20, 100, 10), new Vector3f(0, 1, 0));
        float[][] temp = new float[][] {
                {-0.242F, -0.77F, -0.58F, -4.85F},
                {-0.0F, 0.59F, -0.14F, -42.51F},
                {0.97F, -0.194F, -0.145F, 230.583F},
                {0.0F, 0.0F, 0.0F, 1.0F}
        };
        Matrix4x4 expectedResult = new Matrix4x4(temp);
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                Assertions.assertTrue(almostEqual(expectedResult.get(i, j), result.get(i, j), 0.1F));
            }
        }
    }

    @Test
    public void perspectiveTest() throws Exception {
        Matrix4x4 result = GraphicConveyor.perspective(-1, 1.8F, 0.01F, 100F);
        float[][] temp = new float[][] {
                {-1.0F, 0.0F, 0.0F, 0.0F},
                {0.0F, -1.830F, 0.0F, 0.0F},
                {0.0F, 0.0F, 1.0F, -0.02F},
                {0.0F, 0.0F, 1.0F, 0.0F}
        };
        Matrix4x4 expectedResult = new Matrix4x4(temp);
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                Assertions.assertTrue(almostEqual(expectedResult.get(i, j), result.get(i, j), 0.1F));
            }
        }
    }

    @Test
    public void vertexToPointTest() {
        Point2f result = GraphicConveyor.vertexToPoint(new Vector3f(-0.12F, 2.6F, 1.0F), 1584, 870);
        Point2f expectedResult = new Point2f(601.92F, -1827.0F);
        Assertions.assertEquals(result.toString(), expectedResult.toString());
    }

    private static boolean almostEqual(float firstValue, float secondValue, final float accuracy) {
        return Math.abs(firstValue - secondValue) < accuracy;
    }
}

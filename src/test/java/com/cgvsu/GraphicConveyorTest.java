package com.cgvsu;

import com.cgvsu.math.matrix.Matrix4x4;
import com.cgvsu.math.vector.Vector3f;
import com.cgvsu.render_engine.GraphicConveyor;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import static com.cgvsu.render_engine.GraphicConveyor.rotateScaleTranslate;

public class GraphicConveyorTest {


    @Test
    public void  rotateScaleTranslateTest01() throws Exception {
        Matrix4x4 modelMatrix = rotateScaleTranslate(2, 2, 'a', new Vector3f(3, 0, 1));
        float[][] expectedResult = new float[][] {
                {2.0f, 0.0f, 0.0f, 3.0f},
                {0.0f, 2.0f, 0.0f, 0.0f},
                {0.0f, 0.0f, 2.0f, 1.0f},
                {0.0f, 0.0f, 0.0f, 1.0f}
        };
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                Assertions.assertTrue(modelMatrix.get(i, j) == expectedResult[i][j]);
            }
        }
    }

    @Test
    public void  rotateScaleTranslateTest02() throws Exception {
        Matrix4x4 modelMatrix = rotateScaleTranslate(1, 30, 'z', new Vector3f(3, 0, 1));
        float[][] expectedResult = new float[][] {
                {0.8660254f, 0.5f, 0.0f, 3.0f},
                {-0.5f, 0.8660254f, 0.0f, 0.0f},
                {0.0f, 0.0f, 1.0f, 1.0f},
                {0.0f, 0.0f, 0.0f, 1.0f}
        };
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                Assertions.assertTrue(modelMatrix.get(i, j) == expectedResult[i][j]);
            }
        }
    }

    @Test
    public void  rotateScaleTranslateTest03() throws Exception {
        Matrix4x4 modelMatrix = rotateScaleTranslate(1, 30, 'y', new Vector3f(3, 0, 1));
        float[][] expectedResult = new float[][] {
                {0.8660254f, 0.0f, 0.5f, 3.0f},
                {0.0f, 1.0f, 0.0f, 0.0f},
                {-0.5f, 0.0f, 0.8660254f, 1.0f},
                {0.0f, 0.0f, 0.0f, 1.0f}
        };
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                Assertions.assertTrue(modelMatrix.get(i, j) == expectedResult[i][j]);
            }
        }
    }

    @Test
    public void  rotateScaleTranslateTest04() throws Exception {
        Matrix4x4 modelMatrix = rotateScaleTranslate(1, 30, 'x', new Vector3f(3, 0, 1));
        float[][] expectedResult = new float[][] {
                {1.0f, 0.0f, 0.0f, 3.0f},
                {0.0f, 0.8660254f, 0.5f, 0.0f},
                {0.0f, -0.5f, 0.8660254f, 1.0f},
                {0.0f, 0.0f, 0.0f, 1.0f}
        };
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                Assertions.assertTrue(modelMatrix.get(i, j) == expectedResult[i][j]);
            }
        }
    }

    @Test
    public void perspectiveTest01() throws Exception {
        Matrix4x4 result = GraphicConveyor.perspective(1.0f, 1.833f, 0.01f, 100.0f);
        float[][] expectedResult = new float[][] {
                {0.998f, 0.0f, 0.0f, 0.0f},
                {0.0f, 1.8304877f, 0.0f, 0.0f},
                {0.0f, 0.0f, 1.0f, 1.0f},
                {0.0f, 0.0f, 0.0f, 0.0f}
        };

        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                Assertions.assertTrue(almostEqual(result.get(i, j), expectedResult[i][j], 0.03));
            }
        }
    }

    @Test
    public void lookAttest01() throws Exception {
        Matrix4x4 result = GraphicConveyor.lookAt(new Vector3f(0, 0, 100), new Vector3f(0, 0, 0));
        float[][] expectedResult = {
                {-1.0f, 0.0f, 0.0f, 0.0f},
                {0.0f, 1.0f, 0.0f, 0.0f},
                {0.0f, 0.0f, -1.0f, 0.0f},
                {-0.0f, -0.0f, 100.0f, 1.0f}
        };
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                Assertions.assertTrue(almostEqual(result.get(i, j), expectedResult[i][j], 0.03));
            }
        }
    }
    
    public static boolean almostEqual(double firstValue, double secondValue, double accuracy) {
        return Math.abs(firstValue - secondValue) < accuracy;
    }
}
package com.cgvsu.render_engine;


import com.cgvsu.math.matrix.Matrix3x3;
import com.cgvsu.math.matrix.Matrix4x4;
import com.cgvsu.math.point.Point2f;
import com.cgvsu.math.vector.Vector3f;

public class GraphicConveyor {

    // todo Do scale for every vertex. (x, y, z)
    public static Matrix4x4 scaleRotateTranslate(float percent, float alpha, char token, Vector3f target) throws Exception {
        Matrix3x3 s = scale(percent);
                Matrix3x3 r = switch (token) {
            case 'x' -> rotateX(alpha);
            case 'y' -> rotateY(alpha);
            case 'z' -> rotateZ(alpha);
            default -> Matrix3x3.getOneMatrix();
        };

        Matrix4x4 t = translate(target);
        Matrix3x3 rs = r.mul(s);
        float[][] rs4 = new float[][] {
                {rs.get(0,0), rs.get(0, 1) , rs.get(0, 2), 0},
                {rs.get(1,0), rs.get(1, 1) , rs.get(1, 2), 0},
                {rs.get(2,0), rs.get(2, 1) , rs.get(2, 2), 0},
                {0, 0, 0, 1},
        };
        Matrix4x4 result = t.mul(new Matrix4x4(rs4));
        return result;
        //return Matrix4x4.getOneMatrix();
    }


    public static Matrix3x3 scale(float percent) {
        float[][] matrix = new float[][] {
                {percent, 0, 0},
                {0, percent, 0},
                {0, 0, percent}
        };
        return new Matrix3x3(matrix);
    }

    public static Matrix3x3 rotateZ(float alpha) { //Пока относительно только z
        double radians = Math.toRadians(alpha);
        float[][] matrix = new float[][] {
                {(float) Math.cos(radians), (float) Math.sin(radians), 0},
                {(float) (-Math.sin(radians)),(float) Math.cos(radians), 0},
                {0, 0, 1}
        };
        return new Matrix3x3(matrix);
    }
    public static Matrix3x3 rotateX(float alpha) {
        double radians = Math.toRadians(alpha);
        float[][] matrix = new float[][] {
                {1, 0, 0},
                {0, (float) Math.cos(radians), (float) Math.sin(radians)},
                {0, (float) (-Math.sin(radians)),(float) Math.cos(radians)},
        };
        return new Matrix3x3(matrix);
    }
    public static Matrix3x3 rotateY(float alpha) {
        double radians = Math.toRadians(alpha);
        float[][] matrix = new float[][] {
                {(float)Math.cos(radians), 0, (float) Math.sin(radians)},
                {0, 1, 0},
                {(float) (-Math.sin(radians)), 0, (float) Math.cos(radians)}
        };
        return new Matrix3x3(matrix);
    }

    public static Matrix4x4 translate(Vector3f t) {
        float[][] matrix = new float[][] {
                {1, 0, 0, t.getX()},
                {0, 1, 0, t.getY()},
                {0, 0, 1, t.getZ()},
                {0, 0, 0, 1}
        };
        return new Matrix4x4(matrix);
    }

    public static Matrix4x4 lookAt(Vector3f eye, Vector3f target) throws Exception {
        return lookAt(eye, target, new Vector3f(0F, 1.0F, 0F));
    }

    public static Matrix4x4 lookAt(Vector3f eye, Vector3f target, Vector3f up) throws Exception {
        Vector3f resultZ = (Vector3f) target.subtraction(eye);
        Vector3f resultX = up.vectorMultiply(resultZ);
        Vector3f resultY = resultZ.vectorMultiply(resultX);

        resultX = (Vector3f) resultX.normal();
        resultY = (Vector3f) resultY.normal();
        resultZ = (Vector3f) resultZ.normal();

        float[][] matrix = new float[][]{
                {resultX.getX(), resultY.getX(), resultZ.getX(), 0},
                {resultX.getY(), resultY.getY(), resultZ.getY(), 0},
                {resultX.getZ(), resultY.getZ(), resultZ.getZ(), 0},
                {-resultX.dot(eye), -resultY.dot(eye), -resultZ.dot(eye), 1}};
        return new Matrix4x4(matrix);
    }

    public static Matrix4x4 perspective(
            final float fov,
            final float aspectRatio,
            final float nearPlane,
            final float farPlane) throws Exception {
        float tangentMinusOnDegree = (float) (1.0F / (Math.tan(fov * 0.5F)));
        float[][] result1 = new float[][]{
                {tangentMinusOnDegree / aspectRatio, 0, 0, 0},
                {0, tangentMinusOnDegree, 0, 0},
                {0, 0, (farPlane + nearPlane) / (farPlane - nearPlane), 1.0F},
                {0, 0, 2 * (nearPlane * farPlane) / (nearPlane - farPlane), 0}
        };
        return new Matrix4x4(result1);
    }

//    public static Vector3f multiplyMatrix4ByVector3(final Matrix4x4 matrix, final Vector3f vertex) {
//        final float x = matrix.get(0, 0) * vertex.getX() + matrix.get(0, 1) * vertex.getY() + matrix.get(0, 2) * vertex.getZ() + matrix.get(0, 3);
//        final float y = matrix.get(1, 0) * vertex.getX() + matrix.get(1, 1) * vertex.getY() + matrix.get(1, 2) * vertex.getZ() + matrix.get(1, 3);
//        final float z = matrix.get(2, 0) * vertex.getX() + matrix.get(2, 1) * vertex.getY() + matrix.get(2, 2) * vertex.getZ() + matrix.get(2, 3);
//
//        return new Vector3f(x, y, z);
//    }

    public static Vector3f multiplyMatrix4ByVector3(final Matrix4x4 matrix, final Vector3f vertex) {
        final float x = (vertex.getX() * matrix.get(0,0)) + (vertex.getY() * matrix.get(0,1)) + (vertex.getZ() * matrix.get(0, 2)) + matrix.get(0, 3);
        final float y = (vertex.getX() * matrix.get(1,0)) + (vertex.getY() * matrix.get(1,1)) + (vertex.getZ() * matrix.get(1, 2)) + matrix.get(1, 3);
        final float z = (vertex.getX() * matrix.get(2,0)) + (vertex.getY() * matrix.get(2,1)) + (vertex.getZ() * matrix.get(2, 2)) + matrix.get(2, 3);
        final float w = (vertex.getX() * matrix.get(3,0)) + (vertex.getY() * matrix.get(3,1)) + (vertex.getZ() * matrix.get(3, 2)) + matrix.get(3, 3);
        return new Vector3f(x / w, y / w, z / w);
    }

    public static Point2f vertexToPoint(final Vector3f vertex, final int width, final int height) {
        return new Point2f(vertex.getX() * width + width / 2.0F, -vertex.getY() * height + height / 2.0F);
    }
}

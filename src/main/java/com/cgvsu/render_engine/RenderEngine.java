package com.cgvsu.render_engine;

import java.util.ArrayList;
import java.util.HashMap;

import com.cgvsu.math.matrix.Matrix4x4;
import com.cgvsu.math.point.Point2f;
import com.cgvsu.math.point.Point2fInt;
import com.cgvsu.math.vector.Vector3f;
import com.cgvsu.model.MyModel;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;


import static com.cgvsu.render_engine.GraphicConveyor.*;

public class RenderEngine {
    public static void render(
            final GraphicsContext graphicsContext,
            final Camera camera,
            final MyModel model,
            final int width,
            final int height,
            final float percentX,
            final float percentY,
            final float percentZ,
            final float alpha,
            final Vector3f target,
            final char token) throws Exception {
        Matrix4x4 modelMatrix = scaleRotateTranslate(percentX, percentY, percentZ, alpha, token, target);
        Matrix4x4 viewMatrix = camera.getViewMatrix();
        Matrix4x4 projectionMatrix = camera.getProjectionMatrix();
        Matrix4x4 modelViewProjectionMatrix = (projectionMatrix.mul(viewMatrix)).mul(modelMatrix);

        final int nPolygons = model.getFaces().size();
        for (int polygonInd = 0; polygonInd < nPolygons; polygonInd++) {
            final int nVerticesInPolygon = model.getFaces().get(polygonInd).vertexIndexes.size();

            ArrayList<Point2f> resultPoints = new ArrayList<>();
            for (int vertexInPolygonInd = 0; vertexInPolygonInd < nVerticesInPolygon; ++vertexInPolygonInd) {
                Vector3f vertex = model.getVertexes().get((model.getFaces().get(polygonInd).vertexIndexes.get(vertexInPolygonInd)) - 1);

                Point2f resultPoint = vertexToPoint(multiplyMatrix4ByVector3(modelViewProjectionMatrix, vertex), width, height);
                resultPoints.add(resultPoint);
            }

            if (nVerticesInPolygon == 3) {
                Point2fInt p1 = new Point2fInt(Math.round(resultPoints.get(0).getX()), Math.round(resultPoints.get(0).getY()));
                Point2fInt p2 = new Point2fInt(Math.round(resultPoints.get(1).getX()), Math.round(resultPoints.get(1).getY()));
                Point2fInt p3 = new Point2fInt(Math.round(resultPoints.get(2).getX()), Math.round(resultPoints.get(2).getY()));

                drawFilledTriangle(
                        toNormCoordinate(p1, (int) graphicsContext.getCanvas().getHeight(), (int) graphicsContext.getCanvas().getWidth()),
                            toNormCoordinate(p2, (int) graphicsContext.getCanvas().getHeight(), (int) graphicsContext.getCanvas().getWidth()),
                                toNormCoordinate(p3, (int) graphicsContext.getCanvas().getHeight(), (int) graphicsContext.getCanvas().getWidth()),
                                    graphicsContext);

            }
            graphicsContext.setStroke(Color.BLACK);
            for (int vertexInPolygonInd = 1; vertexInPolygonInd < nVerticesInPolygon; ++vertexInPolygonInd) {
                graphicsContext.strokeLine(
                        resultPoints.get(vertexInPolygonInd - 1).getX(),
                        resultPoints.get(vertexInPolygonInd - 1).getY(),
                        resultPoints.get(vertexInPolygonInd).getX(),
                        resultPoints.get(vertexInPolygonInd).getY());
            }

            if (nVerticesInPolygon > 0)
                graphicsContext.strokeLine(
                        resultPoints.get(nVerticesInPolygon - 1).getX(),
                        resultPoints.get(nVerticesInPolygon - 1).getY(),
                        resultPoints.get(0).getX(),
                        resultPoints.get(0).getY());


        }
    }

    /* Перегон из координат с началом в центре экрана в кооординаты из левого угла */
    private static Point2fInt toNormCoordinate(Point2fInt p, int height, int width) {
        Point2fInt result = new Point2fInt();
        result.setX(width / 2 + p.getX());
        result.setY(height / 2 - p.getY());
        return result;
    }

    /*Перегон из экрана в центр*/
    private static Point2fInt toCenter(Point2fInt p, int height, int width) {
        Point2fInt result = new Point2fInt();
        result.setX(p.getX() - width / 2);
        result.setY(height / 2 - p.getY());
        return result;
    }

    private static void drawFilledTriangle(Point2fInt p1, Point2fInt p2, Point2fInt p3, GraphicsContext context) {
        context.setStroke(Color.GREEN);
        if (p2.getY() <  p1.getY()) {
            swap(p2, p1);
        }
        if (p3.getY() <  p1.getY()) {
            swap(p3, p1);
        }
        if (p3.getY() < p2.getY()) {
            swap(p3, p2);
        }

        ArrayList<Integer> x01 = interpolate(p1.getY(), p1.getX(), p2.getY(), p2.getX());
        ArrayList<Integer> x12 = interpolate(p2.getY(), p2.getX(), p3.getY(), p3.getX());
        ArrayList<Integer> x02 = interpolate(p1.getY(), p1.getX(), p3.getY(), p3.getX());

        x12.remove(0);
        x01.addAll(x12);

        int m = x01.size() / 2;
        ArrayList<Integer> xLeft = null;
        ArrayList<Integer> xRight = null;
        if (x02.get(m) < x01.get(m)) {
            xRight = x01;
            xLeft = x02;
        } else {
            xLeft = x01;
            xRight = x02;
        }

        for (int y = p1.getY(); y < p3.getY(); y++) {
            for (int x = xLeft.get(y - p1.getY()); x < xRight.get(y - p1.getY()); x++) {
                Point2fInt p = toCenter(new Point2fInt(x, y), (int) context.getCanvas().getHeight(), (int) context.getCanvas().getWidth());
                context.strokeLine(p.getX(), p.getY(), p.getX(), p.getY());
            }
        }
    }

    private static void swap(Point2fInt p0, Point2fInt p1) {
        int tmp = p0.getX();
        p0.setX(p1.getX());
        p1.setX(tmp);

        tmp = p0.getY();
        p0.setY(p1.getY());
        p1.setY(tmp);
    }

    private static ArrayList<Integer> interpolate(int i0, int d0, int i1, int d1) {
        ArrayList<Integer> values = new ArrayList<>();
        if (i0 == i1) {
            values.add(d0);
            return values;
        }
        int a = (d1 - d0) / (i1 - i0);
        int d = d0;
        for(int i = i0; i <= i1; i++) {
            values.add(d);
            d += a;
        }

        return values;
    }
}

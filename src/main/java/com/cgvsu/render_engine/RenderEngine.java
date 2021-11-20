package com.cgvsu.render_engine;

import java.util.ArrayList;

import com.cgvsu.math.matrix.Matrix4x4;
import com.cgvsu.math.point.Point2f;
import com.cgvsu.math.vector.Vector3f;
import com.cgvsu.model.MyModel;
import javafx.scene.canvas.GraphicsContext;


import static com.cgvsu.render_engine.GraphicConveyor.*;

public class RenderEngine {

    public static void render(
            final GraphicsContext graphicsContext,
            final Camera camera,
            final MyModel model,
            final int width,
            final int height,
            final float percent,
            final float alpha,
            final Vector3f target,
            final char token) throws Exception {
        Matrix4x4 modelMatrix = scaleRotateTranslate(percent, alpha, token, target);
        Matrix4x4 viewMatrix = camera.getViewMatrix();
        Matrix4x4 projectionMatrix = camera.getProjectionMatrix();

        Matrix4x4 transposedProjectionMatrix = projectionMatrix.transpose();

        Matrix4x4 modelViewProjectionMatrix = (transposedProjectionMatrix.mul(viewMatrix)).mul(modelMatrix);

        final int nPolygons = model.getFaces().size();
        for (int polygonInd = 0; polygonInd < nPolygons; polygonInd++) {
            final int nVerticesInPolygon = model.getFaces().get(polygonInd).vertexIndexes.size();

            ArrayList<Point2f> resultPoints = new ArrayList<>();
            for (int vertexInPolygonInd = 0; vertexInPolygonInd < nVerticesInPolygon; ++vertexInPolygonInd) {
                Vector3f vertex = model.getVertexes().get((model.getFaces().get(polygonInd).vertexIndexes.get(vertexInPolygonInd)) - 1);

                Point2f resultPoint = vertexToPoint(multiplyMatrix4ByVector3(modelViewProjectionMatrix, vertex), width, height);
                resultPoints.add(resultPoint);
            }

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
}

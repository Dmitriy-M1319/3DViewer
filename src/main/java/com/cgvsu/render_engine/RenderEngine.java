package com.cgvsu.render_engine;

import java.util.ArrayList;

import com.cgvsu.math.matrix.Matrix4x4;
import com.cgvsu.math.point.Point2f;
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
            final float alphaX,
            final float alphaY,
            final float alphaZ,
            final Vector3f target) throws Exception {
        Matrix4x4 modelMatrix = scaleRotateTranslate(percentX, percentY, percentZ, alphaX, alphaY, alphaZ, target);
        Matrix4x4 viewMatrix = camera.getViewMatrix();
        Matrix4x4 projectionMatrix = camera.getProjectionMatrix();
        Matrix4x4 modelViewMatrix = new Matrix4x4(modelMatrix.getMatrix()).mul(viewMatrix);
        Matrix4x4 modelViewProjectionMatrix = (projectionMatrix.mul(viewMatrix)).mul(modelMatrix);

        final int nPolygons = model.getFaces().size();
        for (int polygonInd = 0; polygonInd < nPolygons; polygonInd++) {
            final int nVerticesInPolygon = model.getFaces().get(polygonInd).vertexIndexes.size();

            ArrayList<Vector3f> vertexes = new ArrayList<>();
            ArrayList<Point2f> resultPoints = new ArrayList<>();
            for (int vertexInPolygonInd = 0; vertexInPolygonInd < nVerticesInPolygon; ++vertexInPolygonInd) {
                Vector3f vertex = model.getVertexes().get((model.getFaces().get(polygonInd).vertexIndexes.get(vertexInPolygonInd)) - 1);
                Vector3f projectionVertex = multiplyMatrix4ByVector3(modelViewProjectionMatrix, vertex);
                vertexes.add(vertex);
                Point2f resultPoint = vertexToPoint(multiplyMatrix4ByVector3(modelViewProjectionMatrix, vertex), width, height);
                resultPoints.add(resultPoint);
            }

            if (nVerticesInPolygon == 3) {
                Matrix4x4 normalMatrix = Matrix4x4.invert(modelViewMatrix.getMatrix()).transpose();
                float lambert = diffuseLightLambert(new Vector3f(1000, 1000, 1000),
                        model.getNormals().get(model.getFaces().get(polygonInd).normalIndexes.get(0)),
                        vertexes.get(0), normalMatrix, modelViewMatrix);

                Vector3f rawColor = new Vector3f(0, 255, 0);
                Color pixelColor = Color.rgb((int) (rawColor.getX() * lambert), (int) (rawColor.getY() * lambert),
                        (int) (rawColor.getZ() * lambert));
                Drawing.drawFilledTriangle(resultPoints.get(0), resultPoints.get(1), resultPoints.get(2), graphicsContext,
                        pixelColor);
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

    private static float diffuseLightLambert(Vector3f lightCoords, Vector3f normal,
                                             Vector3f vertexCoords, Matrix4x4 normalMatrix, Matrix4x4 modelViewMatrix){
        Vector3f p = multiplyMatrix4ByVector3(modelViewMatrix, vertexCoords);
        Vector3f l = (Vector3f) lightCoords.subtraction(p).normal();
        Vector3f n = (Vector3f) multiplyMatrix4ByVector3(normalMatrix, normal).normal();

        return Math.max(0, n.dot(l));
//        Vector3f worldLightDir = (Vector3f) lightCoords.normal();
//        Vector3f worldNormal = (Vector3f) normal.normal();
//        return Math.max(0, worldNormal.dot(worldLightDir));
    }
}

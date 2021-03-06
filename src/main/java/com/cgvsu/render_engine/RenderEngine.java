package com.cgvsu.render_engine;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;
import java.util.Vector;

import com.cgvsu.math.matrix.Matrix4x4;
import com.cgvsu.math.point.Point2f;
import com.cgvsu.math.point.Point2fInt;
import com.cgvsu.math.vector.Vector2f;
import com.cgvsu.math.vector.Vector3f;
import com.cgvsu.model.MyModel;
import com.cgvsu.model.Texture;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.shape.VertexFormat;


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
            final Vector3f target,
            final Texture texture) throws Exception {
        Matrix4x4 modelMatrix = scaleRotateTranslate(percentX, percentY, percentZ, alphaX, alphaY, alphaZ, target);
        Matrix4x4 viewMatrix = camera.getViewMatrix();
        Matrix4x4 projectionMatrix = camera.getProjectionMatrix();
        Matrix4x4 modelViewProjectionMatrix = (projectionMatrix.mul(viewMatrix)).mul(modelMatrix);

        final int nPolygons = model.getFaces().size();
        for (int polygonInd = 0; polygonInd < nPolygons; polygonInd++) {
            final int nVerticesInPolygon = model.getFaces().get(polygonInd).vertexIndexes.size();

            ArrayList<Point2f> resultPoints = new ArrayList<>();
            ArrayList<Vector3f> projectionVertexes = new ArrayList<>();
            ArrayList<Vector2f> textureCoords = new ArrayList<>();

            for (int vertexInPolygonInd = 0; vertexInPolygonInd < nVerticesInPolygon; ++vertexInPolygonInd) {
                Vector3f vertex = model.getVertexes().get((model.getFaces().get(polygonInd).vertexIndexes.get(vertexInPolygonInd)) - 1);
                Vector3f projectionVertex = multiplyMatrix4ByVector3(modelViewProjectionMatrix, vertex);
                projectionVertexes.add(projectionVertex);

                Point2f resultPoint = vertexToPoint(multiplyMatrix4ByVector3(modelViewProjectionMatrix, vertex), width, height);
                resultPoints.add(resultPoint);

                Vector2f textureVertex = model.getTextures().get((model.getFaces().get(polygonInd).textureVertexIndexes.get(vertexInPolygonInd)) - 1);
                textureCoords.add(textureVertex);
            }

            if (nVerticesInPolygon == 3) {
                Drawing.initDepthBuffer(width, height);
                Color color = Color.GREEN;
                Drawing.drawTriangleWithZBuffer(projectionVertexes, resultPoints, graphicsContext, color, textureCoords, texture);
            }
//            graphicsContext.setStroke(Color.BLACK);
//            for (int vertexInPolygonInd = 1; vertexInPolygonInd < nVerticesInPolygon; ++vertexInPolygonInd) {
//                graphicsContext.strokeLine(
//                        resultPoints.get(vertexInPolygonInd - 1).getX(),
//                        resultPoints.get(vertexInPolygonInd - 1).getY(),
//                        resultPoints.get(vertexInPolygonInd).getX(),
//                        resultPoints.get(vertexInPolygonInd).getY());
//            }
//
//            if (nVerticesInPolygon > 0)
//                graphicsContext.strokeLine(
//                        resultPoints.get(nVerticesInPolygon - 1).getX(),
//                        resultPoints.get(nVerticesInPolygon - 1).getY(),
//                        resultPoints.get(0).getX(),
//                        resultPoints.get(0).getY());


        }
    }

}

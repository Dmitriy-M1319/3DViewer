package com.cgvsu.render_engine;

import com.cgvsu.math.point.Point2f;
import com.cgvsu.math.point.Point2fInt;
import com.cgvsu.math.vector.Vector3f;
import com.cgvsu.model.Normals;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.WritableImage;
import javafx.scene.paint.Color;

import java.util.ArrayList;

public class Drawing {
    public static float[][] depth_buffer;

    public static void initDepthBuffer(int width, int height) {
        depth_buffer = new float[width][height];
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                depth_buffer[i][j] = Float.MAX_VALUE;
            }
        }
    }

    private static void swap(Point2f p0, Point2f p1) {
        float tmp = p0.getX();
        p0.setX(p1.getX());

        p1.setX(tmp);
        tmp = p0.getY();
        p0.setY(p1.getY());
        p1.setY(tmp);
    }

    public static void drawFilledTriangle(Point2f v1, Point2f v2, Point2f v3, GraphicsContext context, Color pixelColor) {
        float minX = findMinOrMax(v1.getX(), v2.getX(), v3.getX(), true);
        float maxX = findMinOrMax(v1.getX(), v2.getX(), v3.getX(), false);
        float minY = findMinOrMax(v1.getY(), v2.getY(), v3.getY(), true);
        float maxY = findMinOrMax(v1.getY(), v2.getY(), v3.getY(), false);

        minX = Math.max(minX, 0);
        minY = Math.max(minY, 0);
        maxX = Math.min(maxX, (float) context.getCanvas().getWidth() - 1);
        maxY = Math.min(maxY, (float) context.getCanvas().getHeight() - 1);

        Point2f p = new Point2f();
        for (float x = minX; x <= maxX; x++) {
            for (float y = minY; y <= maxY; y++) {
                p.setX(x);
                p.setY(y);

                float w0 = orient(v2, v3, p);
                float w1 = orient(v3, v1, p);
                float w2 = orient(v1, v2, p);

                if (w0 >= 0 && w1 >= 0 && w2 >= 0) {
                    context.getPixelWriter().setColor((int) x, (int) y, pixelColor);
                }
            }
        }
    }

    private static float findMinOrMax(float v1, float v2, float v3, boolean isMin) {
        if(v1 > v2) {
            float tmp = v1;
            v1 = v2;
            v2 = tmp;
        }
        if(v1 > v3) {
            float tmp = v1;
            v1 = v3;
            v3 = tmp;
        }
        if(v2 > v3) {
            float tmp = v2;
            v2 = v3;
            v3 = tmp;
        }

        if (isMin) {
            return v1;
        } else {
            return v3;
        }
    }
    private static float orient(Point2f p1, Point2f p2, Point2f p3) {
        return (p2.getX() - p1.getX()) * (p3.getY() - p1.getY()) - ((p2.getY()) - p1.getY()) * (p3.getX() - p1.getX());
    }

    public static void Triangle(Point2f v1 , Point2f v2 , Point2f v3, GraphicsContext context)
    {
        if (v1.getX() > v2.getX()) { swap(v1, v2); }
        if (v1.getX() > v3.getX()) { swap(v1, v3); }
        if (v2.getX() > v3.getX()) { swap(v2, v3); }

        var steps12 = Math.max(v2.getX() - v1.getX() , 1);
        var steps13 = Math.max(v3.getX() - v1.getX() , 1);
        var steps32 = Math.max(v3.getX() - v2.getX() , 1);
        var steps31 = Math.max(v1.getX() - v3.getX() , 1);

        var upDelta = (v2.getY() - v1.getY()) / steps12;
        var downDelta = (v3.getY() - v1.getY()) / steps13;
        if (upDelta < downDelta) {
            float tmp = downDelta;
            downDelta = upDelta;
            upDelta = tmp;
        };

        TrianglePart(v1.getX() , v2.getX() , v1.getY() , upDelta , downDelta, context, Color.GREEN);

        upDelta = (v3.getY() - v2.getY()) / steps32;
        downDelta = (v1.getY() - v3.getY()) / steps31;
        if (upDelta < downDelta) {
            float tmp = downDelta;
            downDelta = upDelta;
            upDelta = tmp;
        }

        float up = v2.getY();
        float down = v3.getY();

        for (int i = (int)v2.getX(); i <= (int)v3.getX(); i++)
        {
            for (int g = (int)up; g <= (int)down; g++)
            {
                context.getPixelWriter().setColor(i, g, Color.BLUE);
            }
            up += upDelta;
            down += downDelta;
        }

        //TrianglePart(v3.getX(), v2.getX(), v3.getY(), upDelta, downDelta, context, Color.BLUE );
    }

    private static void TrianglePart(float x1 , float x2 , float y1  , float upDelta , float downDelta, GraphicsContext context, Color color)
    {
        float up = y1, down = y1;
        for (int i = (int)x1; i <= (int)x2; i++)
        {
            for (int g = (int)down; g <= (int)up; g++)
            {
                context.getPixelWriter().setColor(i, g, color);
            }
            up += upDelta; down += downDelta;
        }
    }
}

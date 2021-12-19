package com.cgvsu.render_engine;

import com.cgvsu.math.point.Point2f;
import com.cgvsu.math.point.Point2fInt;
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

    public static void Triangle(Point2f v1 , Point2f v2 , Point2f v3, GraphicsContext context)
    {
        if (v1.getX() > v2.getX()) { swap(v1, v2); }
        if (v2.getX() > v3.getX()) { swap(v2, v3); }
        if (v1.getX() > v2.getX()) { swap(v1, v2); }

        var steps12 = Math.max(v2.getX() - v1.getX() , 1);
        var steps13 = Math.max(v3.getX() - v1.getX() , 1);
        var steps32 = Math.max(v2.getX() - v3.getX() , 1);
        var steps31 = Math.max(v1.getX() - v3.getX() , 1);

        var upDelta = (v2.getY() - v1.getY()) / steps12;
        var downDelta = (v3.getY() - v1.getY()) / steps13;
        if (upDelta < downDelta) {
            float tmp = downDelta;
            downDelta = upDelta;
            upDelta = tmp;
        };

        TrianglePart(v1.getX() , v2.getX() , v1.getY() , upDelta , downDelta, context);

        upDelta = (v2.getY() - v3.getY()) / steps32;
        downDelta = (v1.getY() - v3.getY()) / steps31;
        if (upDelta < downDelta) {
            float tmp = downDelta;
            downDelta = upDelta;
            upDelta = tmp;
        }

        TrianglePart(v3.getX(), v2.getX(), v3.getY(), upDelta, downDelta,context );
    }

    private static void TrianglePart(float x1 , float x2 , float y1  , float upDelta , float downDelta, GraphicsContext context)
    {
        float up = y1, down = y1;
        for (int i = (int)x1; i <= (int)x2; i++)
        {
            for (int g = (int)down; g <= (int)up; g++)
            {
                context.getPixelWriter().setColor(i, g, Color.GREEN);
            }
            up += upDelta; down += downDelta;
        }
    }
}

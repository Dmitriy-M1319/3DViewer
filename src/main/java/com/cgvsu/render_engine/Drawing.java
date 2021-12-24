package com.cgvsu.render_engine;

import com.cgvsu.math.point.Point2f;
import com.cgvsu.math.vector.Vector2f;
import com.cgvsu.math.vector.Vector3f;
import com.cgvsu.model.Texture;
import javafx.scene.canvas.GraphicsContext;
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

    public static void drawTriangleWithZBuffer(ArrayList<Vector3f> vertexes,
                                               ArrayList<Point2f> points,
                                               GraphicsContext context,
                                               Color color,
                                               ArrayList<Vector2f> textureCoords,
                                               Texture texture) {
        Point2f p1 = points.get(0);
        Point2f p2 = points.get(1);
        Point2f p3 = points.get(2);

        float minX = findMinOrMax(p1.getX(), p2.getX(), p3.getX(), true);
        float maxX = findMinOrMax(p1.getX(), p2.getX(), p3.getX(), false);
        float minY = findMinOrMax(p1.getY(), p2.getY(), p3.getY(), true);
        float maxY = findMinOrMax(p1.getY(), p2.getY(), p3.getY(), false);


        for (float x = minX; x <= maxX; x++) {
            for (float y = minY; y <= maxY; y++) {
                float w0 = orient(p2, p3, new Point2f(x, y));
                float w1 = orient(p3, p1, new Point2f(x, y));
                float w2 = orient(p1, p2, new Point2f(x, y));

                if (w0 >= 0 && w1 >= 0 && w2 >= 0) {
                    //Считаем z координату
                    Vector3f bary = barycentric(vertexes.get(0), vertexes.get(1), vertexes.get(2), new Vector3f(x, y, 0));
                    if (bary == null) {
                        continue;
                    }

                    float currentZ = vertexes.get(0).getZ() * bary.getZ() + vertexes.get(1).getZ() * bary.getX() + vertexes.get(2).getZ() * bary.getY();
                    //Проверка на z буфер
                    if (currentZ < depth_buffer[(int) x][(int) y]) {
                        if(texture == null) {
                            context.getPixelWriter().setColor((int) x, (int) y, color);
                        } else {
                            int width = texture.getTextureWidth();
                            int height = texture.getTextureHeight();
                            //Перегнали из координат экрана в координаты от -1 до 1
                            Vector2f vertex = GraphicConveyor.pointToVertex(new Point2f(x, y), (int) context.getCanvas().getWidth(), (int) context.getCanvas().getHeight());
                            //Рассчитали барицентрические координаты
                            Vector3f baryc = barycentric(vertexes.get(0), vertexes.get(1), vertexes.get(2), new Vector3f(vertex.getX(), vertex.getY(), 0));
                            //Теперь перегоняем координаты текстуры в ее экранные координаты
                            Point2f t1 = new Point2f(textureCoords.get(0).getX() * (float) width, textureCoords.get(0).getY() * (float) height);
                            Point2f t2 = new Point2f(textureCoords.get(1).getX() * (float) width, textureCoords.get(1).getY() * (float) height);
                            Point2f t3 = new Point2f(textureCoords.get(2).getX() * (float) width, textureCoords.get(2).getY() * (float) height);

                            //Считаем x и y по полученным барицентрикам
                            float xt = t1.getX() * baryc.getZ() + t2.getX() * baryc.getX() + t3.getX() * baryc.getY();
                            float yt = t1.getY() * baryc.getZ() + t2.getY() * baryc.getX() + t3.getY() * baryc.getY();

                            //Берем пиксель и рисуем им
                            java.awt.Color c = texture.getColors((int) xt, (int) yt);
                            Color col = Color.rgb(c.getRed(), c.getGreen(), c.getBlue());

                            context.getPixelWriter().setColor((int) x, (int) y, col);
                        }
                        depth_buffer[(int) x][(int) y] = currentZ;
                    }
                }
            }
        }
    }

    public static Vector3f barycentric(Vector3f v1, Vector3f v2, Vector3f v3, Vector3f p) {
        //Считаем векторы осей
        Vector3f v2v1 = new Vector3f(v2.getX() - v1.getX(), v2.getY() - v1.getY(), v2.getZ() - v1.getZ());
        Vector3f v3v1 = new Vector3f(v3.getX() - v1.getX(), v3.getY() - v1.getY(), v3.getZ() - v1.getZ());
        Vector3f v1p = new Vector3f(v1.getX() - p.getX(), v1.getY() - p.getY(), v1.getZ() - p.getZ());

        //Считаем проекции на эти оси
        Vector3f pX = new Vector3f(v2v1.getX(), v3v1.getX(), v1p.getX());
        Vector3f pY = new Vector3f(v2v1.getY(), v3v1.getY(), v1p.getY());
        Vector3f pZ = pX.vectorMultiply(pY);

        if(pZ.getZ() == 0) {
            return null;
        }

        //Считаем коэфы для точки
        float alpha = pZ.getX() / pZ.getZ();
        float beta = pZ.getY() / pZ.getZ();
        float gamma = 1 - (alpha + beta);

        return new Vector3f(alpha, beta, gamma);

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
}

package com.cgvsu.myreader;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class ImageReader {

    public static Color[][] loadPixelsFromImage(File file) throws IOException {
        BufferedImage image = ImageIO.read(file);
        int imageWidth = image.getWidth();
        int imageHeight = image.getHeight();

        Color[][] colors = new Color[imageWidth][imageHeight];

        for (int x = 0; x < imageWidth; x++) {
            for (int y = 0; y < imageHeight; y++) {
                colors[x][y] = new Color(image.getRGB(x, y));
            }
        }

        return colors;
    }
}

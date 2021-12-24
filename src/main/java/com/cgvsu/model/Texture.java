package com.cgvsu.model;

import com.cgvsu.myreader.ImageReader;

import java.awt.*;
import java.io.File;
import java.io.IOException;

public class Texture {
    private final Color[][] colors;

    // toDo: Catch FileNotFoundException (work for Vladik)
    public Texture(String path) throws IOException {
        File imageFile = new File(path);
        colors = ImageReader.loadPixelsFromImage(imageFile);
    }

    public int getTextureWidth() {
        return colors[0].length;
    }

    public int getTextureHeight() {
        return colors.length;
    }

    public Color getColors(int x, int y) {
        if (x == getTextureWidth() || y == getTextureHeight()) {
            return Color.BLACK;
        }
        return colors[x][y];
    }
}

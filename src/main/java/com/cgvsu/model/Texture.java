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

    public Color getColors(int x, int y) {
        return colors[x][y];
    }
}
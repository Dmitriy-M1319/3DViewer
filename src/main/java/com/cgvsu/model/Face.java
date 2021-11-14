package com.cgvsu.model;

import java.util.ArrayList;

public class Face {
    public ArrayList<Integer> vertexIndexes;
    public ArrayList<Integer> textureVertexIndexes;
    public ArrayList<Integer> normalIndexes;

    public Face() {
        vertexIndexes = new ArrayList<>();
        textureVertexIndexes = new ArrayList<>();
        normalIndexes = new ArrayList<>();
    }

    public Face(ArrayList<Integer> vertexIndexes, ArrayList<Integer> textureVertexIndexes, ArrayList<Integer> normalIndexes) {
        this.normalIndexes = normalIndexes;
        this.textureVertexIndexes = textureVertexIndexes;
        this.vertexIndexes = vertexIndexes;
    }
}

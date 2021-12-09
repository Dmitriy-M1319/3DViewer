package com.cgvsu.model;

import java.util.ArrayList;
import java.util.Arrays;

import com.cgvsu.math.vector.Vector2f;
import com.cgvsu.math.vector.Vector3f;

public class MyModel {
    /**
     * Point list
     */
    ArrayList<Vector3f> vertexes = new ArrayList<>();
    /**
     * Normals list
     */
    ArrayList<Vector3f> normals = new ArrayList<>();
    /**
     * Texture coordinates list
     */
    ArrayList<Vector2f> textures = new ArrayList<>();
    /**
     * Face list
     */
    ArrayList<Face> faces = new ArrayList<>();

    public ArrayList<Vector3f> getVertexes() {
        return vertexes;
    }

    public void setVertexes(ArrayList<Vector3f> vertexes) {
        this.vertexes = vertexes;
    }

    public ArrayList<Vector3f> getNormals() {
        return normals;
    }

    public void setNormals(ArrayList<Vector3f> normals) {
        this.normals = normals;
    }

    public ArrayList<Vector2f> getTextures() {
        return textures;
    }

    public void setTextures(ArrayList<Vector2f> textures) {
        this.textures = textures;
    }

    public ArrayList<Face> getFaces() {
        return faces;
    }

    public void setFaces(ArrayList<Face> faces) {
        this.faces = faces;
    }

    public MyModel() {

    }

    public void triangulateFaces() {
        int oldCount = faces.size();
        for (int i = 0; i < oldCount; i++) {
            ArrayList<Face> newFaces = Face.triangulate(faces.get(i));
            if (newFaces.size() == 1) {
                continue;
            } else {
                faces.remove(i);
                for (Face f: newFaces) {
                    faces.add(0, f);
                }
                i += newFaces.size() - 1;
            }
        }
    }
}

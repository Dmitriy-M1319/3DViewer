package com.cgvsu.model;

import java.util.*;
import javax.vecmath.Vector3f;
import javax.vecmath.Vector2f;

public class Mesh {

    public Mesh() {}

    public ArrayList<Vector3f> vertices = new ArrayList<>();
    public ArrayList<Vector2f> textureVertices = new ArrayList<>();
    public ArrayList<Vector3f> normals = new ArrayList<>();

    public ArrayList<ArrayList<Integer>> polygonVertexIndices = new ArrayList<>();
    public ArrayList<ArrayList<Integer>> polygonTextureVertexIndices = new ArrayList<>();
    public ArrayList<ArrayList<Integer>> polygonNormalIndices = new ArrayList<>();
}

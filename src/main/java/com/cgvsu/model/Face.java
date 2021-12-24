package com.cgvsu.model;

import java.util.ArrayList;
import java.util.Arrays;

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

    public static ArrayList<Face> triangulate(Face face) {
        ArrayList<ArrayList<Integer>> vertexInd = null;
        ArrayList<ArrayList<Integer>> textureVertexInd = null;

        if (!face.vertexIndexes.isEmpty()) {
            vertexInd = triangulateListOfIndexes(face.vertexIndexes);
        }
        if (!face.textureVertexIndexes.isEmpty()) {
            textureVertexInd = triangulateListOfIndexes(face.textureVertexIndexes);
        }

        ArrayList<Face> result = new ArrayList<>();
        for (int i = 0; i < vertexInd.size(); i++) {
            Face newFace = new Face(vertexInd.get(i), textureVertexInd.get(i), new ArrayList<>());
            result.add(newFace);
        }

        return result;
    }

    private static ArrayList<ArrayList<Integer>> triangulateListOfIndexes(ArrayList<Integer> indexes) {
        ArrayList<ArrayList<Integer>> newVertexes = new ArrayList<>();
        Integer[] newPolygon = new Integer[3];
        int countVer = indexes.size();
        if (countVer == 3) {
            newVertexes.add(indexes);
            return newVertexes;
        }

        newPolygon[0] = indexes.get(0);
        for (int j = 1, count = 1; j < countVer; j++, ++count) {
            newPolygon[count] = indexes.get(j);
            if (count == 2) {
                count = 0;
                j--;
                newVertexes.add(new ArrayList<>(Arrays.asList(newPolygon)));
            }
        }
        return newVertexes;
    }

}

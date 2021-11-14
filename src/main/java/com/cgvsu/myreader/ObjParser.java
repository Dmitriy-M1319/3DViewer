package com.cgvsu.myreader;

import com.cgvsu.math.vector.Vector2f;
import com.cgvsu.math.vector.Vector3f;
import com.cgvsu.model.Face;

import java.util.ArrayList;

public class ObjParser {

	/**
	 * Parse 3f vector or point from line
	 */
	static Vector3f parseVector3f(final String strVector) throws Exception {
		String[] points = strVector.split("\s+");
		if(points.length != 3) {
			throw new Exception("Non-valid 3f coordinates in line!");
		}
		
        return new Vector3f(Float.parseFloat(points[0]), Float.parseFloat(points[1]), Float.parseFloat(points[2]));
	}

	/**
	 * Parse 2f vector from line
	 */
	static Vector2f parseVector2f(final String strVector) throws Exception{
		String[] points = strVector.split("\s+");
        if (points.length != 2) {
            throw new Exception("Non-valid 2f coordinates in line!");
        }

        return new Vector2f(Float.parseFloat(points[0]), Float.parseFloat(points[1]));
	}
    /**
     * Parse point from line
     * @param strVertex
     * @return
     * @throws Exception
     */
    public static Vector3f parseVertex(final String strVertex) throws Exception {
        String[] points = strVertex.split(" ");
        if (points.length != 3) {
            throw new Exception("Non-valid 3f coordinates in line!");
        }

        return new Vector3f(Float.parseFloat(points[0]), Float.parseFloat(points[1]), Float.parseFloat(points[2]));
    }

    /**
     * Parse normal from line
     * @param strNormal
     * @return
     * @throws Exception
     */
    public static Vector3f parseNormal(final String strNormal) throws Exception {
        String[] points = strNormal.split(" ");
        if (points.length != 3) {
            throw new Exception("Non-valid normal coordinates!");
        }

        return new Vector3f(Float.parseFloat(points[0]), Float.parseFloat(points[1]), Float.parseFloat(points[2]));
    }

    /**
     * Parse texture from line
     * @param strTexture
     * @return
     * @throws Exception
     */
    public static Vector2f parseTextureVertex(final String strTexture) throws Exception {
        String[] points = strTexture.split(" ");
        if (points.length != 2) {
            throw new Exception("Non-valid texture coordinates!");
        }

        return new Vector2f(Float.parseFloat(points[0]), Float.parseFloat(points[1]));
    }

    /**
     * Parse face from line
     * @param face
     * @return
     */
    public static Face parseFace(final String face, final int maxIndex) throws Exception {
        ArrayList<Integer> vertexIndexes = new ArrayList<>();
        ArrayList<Integer> textureVertexIndexes = new ArrayList<>();
        ArrayList<Integer> normals = new ArrayList<>();

        String[] points = face.split("\s+");
        for (String point: points) {
            int vertex;
            if (point.contains("/")) {
                String[] indexes;
                if (point.contains("//")) { //if there is not texture
                    indexes = point.split("//");
                    vertex = Integer.parseInt(indexes[0]);
                    if (vertex > maxIndex) {
                        throw new Exception(String.format("Error: index %d, max index %d", vertex, maxIndex));
                    }
                    vertexIndexes.add(Integer.parseInt(indexes[0]));
                    normals.add(Integer.parseInt(indexes[1]));
                    continue;
                }
                indexes = point.split("/+");

                switch (indexes.length) {
                    case 2:
                        vertex = Integer.parseInt(indexes[0]);
                        if (vertex > maxIndex) {
                            throw new Exception(String.format("Error: index %d, max index %d", vertex, maxIndex));
                        }
                        vertexIndexes.add(vertex);
                        textureVertexIndexes.add(Integer.parseInt(indexes[1]));
                        break;
                    case 3:
                        vertex = Integer.parseInt(indexes[0]);
                        if (vertex > maxIndex) {
                            throw new Exception(String.format("Error: index %d, max index %d", vertex, maxIndex));
                        }
                        vertexIndexes.add(vertex);
                        textureVertexIndexes.add(Integer.parseInt(indexes[1]));
                        normals.add(Integer.parseInt(indexes[2]));
                        break;
                }
            }
            else {
                vertex = Integer.parseInt(point);
                if (vertex > maxIndex) {
                    throw new Exception(String.format("Error: index %d, max index %d", vertex, maxIndex));
                }
                vertexIndexes.add(vertex);
            }
        }

        return new Face(vertexIndexes, textureVertexIndexes, normals);
    }
}

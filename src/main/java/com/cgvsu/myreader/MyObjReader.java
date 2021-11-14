package com.cgvsu.myreader;

import com.cgvsu.math.vector.Vector2f;
import com.cgvsu.math.vector.Vector3f;
import com.cgvsu.model.Face;
import com.cgvsu.model.MyModel;

import java.io.File;
import java.util.ArrayList;
import java.util.Scanner;

public class MyObjReader {

    private static final String OBJ_VERTEX_TOKEN = "v";
    private static final String OBJ_TEXTURE_TOKEN = "vt";
    private static final String OBJ_NORMAL_TOKEN = "vn";
    private static final String OBJ_FACE_TOKEN = "f";

    //Throw FileNotFoundException and default Exception
    public static MyModel read(final String filename) throws Exception {
        ArrayList<Vector3f> vertexes = new ArrayList<>();
        ArrayList<Vector3f> normals = new ArrayList<>();
        ArrayList<Vector2f> textures = new ArrayList<>();

        ArrayList<Face> faces = new ArrayList<>();

        Scanner scanner = new Scanner(new File(filename));

        while (scanner.hasNext()) {
            String str = scanner.nextLine();
	        if(str.isEmpty()) {
		        continue;
	        }

            String token = str.substring(0, 2).trim();
            switch (token) {
                case OBJ_VERTEX_TOKEN -> vertexes.add(ObjParser.parseVertex(str.substring(1).trim()));
                case OBJ_TEXTURE_TOKEN -> textures.add(ObjParser.parseTextureVertex(str.substring(2).trim()));
                case OBJ_NORMAL_TOKEN -> normals.add(ObjParser.parseNormal(str.substring(2).trim()));
                case OBJ_FACE_TOKEN -> faces.add(ObjParser.parseFace(str.substring(2).trim(), vertexes.size()));
            }
        }

        MyModel result = new MyModel();
        result.setNormals(normals);
        result.setVertexes(vertexes);
        result.setTextures(textures);
        result.setFaces(faces);

        return result;
    }
}

package com.cgvsu.obj_writer;



import com.cgvsu.math.vector.Vector2f;
import com.cgvsu.math.vector.Vector3f;
import com.cgvsu.model.Face;
import com.cgvsu.model.MyModel;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.util.ArrayList;


public class ObjWriter {
    private static String writeVector3f(final ArrayList<Vector3f> vectors, String token){
        StringBuilder str = new StringBuilder();
        for (Vector3f vector : vectors) {
            str.append(token).append(vector.toString());
        }
        return str.toString();
    }

    private static String writeVector2f(final ArrayList<Vector2f> vectors){
        StringBuilder str = new StringBuilder();
        for (Vector2f vector : vectors) {
            str.append("vt ").append(vector.toString());
        }
        return str.toString();
    }

    private static String writeFace(final Face face) {
        StringBuilder str = new StringBuilder();
        str.append("f");
        for (int i = 0; i < face.vertexIndexes.size(); i++) {
            str.append(" ").append(face.vertexIndexes.get(i));
            if (!face.normalIndexes.isEmpty() || !face.textureVertexIndexes.isEmpty()){
                str.append("/");
            }
            if (!face.textureVertexIndexes.isEmpty()) {
                str.append(face.textureVertexIndexes.get(i));
            }
            if (!face.normalIndexes.isEmpty()) {
                str.append("/").append(face.normalIndexes.get(i));
            }
        }
        str.append("\n");
        return str.toString();
    }

    public static void write(MyModel model, String pathFile) throws Exception {
        for (Face face : model.getFaces()) {
            check(face, model);
        }

        File file = new File(pathFile);

        FileWriter fw;
        fw = new FileWriter(file.getAbsoluteFile());
        BufferedWriter writer = new BufferedWriter(fw);

        writer.write(writeVector3f(model.getVertexes(), "v "));
        writer.write(writeVector2f(model.getTextures()));
        writer.write(writeVector3f(model.getNormals(), "vn "));
        for (Face face : model.getFaces()) {
            writer.write(writeFace(face));
        }
        writer.flush();
    }

    private static void check(final Face face, MyModel model) throws Exception {
        if (face.vertexIndexes.size() < 3) {
            throw new Exception("Error saving polygon, insufficient vertex indices.");
        }
        for (int i = 0; i < face.vertexIndexes.size(); i++) {
            if (face.vertexIndexes.get(i) > model.getVertexes().size()) {
                throw new Exception("Error saving polygon, there is no such vertex index.");
            }
        }
        if (!face.textureVertexIndexes.isEmpty()) {
            if (face.textureVertexIndexes.size() < 3) {
                throw new Exception("Error saving texture polygon, insufficient vertex indices.");
            }
            for (int i = 0; i < face.textureVertexIndexes.size(); i++) {
                if (face.textureVertexIndexes.get(i) > model.getTextures().size() + model.getVertexes().size()) {
                    throw new Exception("Error saving texture polygon, there is no such vertex index.");
                }
            }
        }
        if (!face.normalIndexes.isEmpty()) {
            if (face.normalIndexes.size() < 3) {
                throw new Exception("Error saving normal polygon, insufficient vertex indices.");
            }
        }
        for (int i = 0; i < face.normalIndexes.size(); i++) {
            if (face.normalIndexes.get(i) > model.getTextures().size() + model.getVertexes().size() + model.getNormals().size()) {
                throw new Exception("Error saving normal polygon, there is no such vertex index.");
            }
        }
    }
}

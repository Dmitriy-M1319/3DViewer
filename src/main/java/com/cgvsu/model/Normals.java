package com.cgvsu.model;

import com.cgvsu.math.vector.Vector3f;

public class Normals {
    public static void calculateNormals (MyModel model){
        for (int faceNumber = 0; faceNumber < model.getFaces().size(); faceNumber++) {
            Face face = model.getFaces().get(faceNumber);
            Vector3f firstPoint = model.getVertexes().get(face.vertexIndexes.get(0) - 1);
            Vector3f secondPoint = model.getVertexes().get(face.vertexIndexes.get(1) - 1);
            Vector3f thirdPoint = model.getVertexes().get(face.vertexIndexes.get(2) - 1);
            Vector3f normal = calculateNormal(firstPoint, secondPoint, thirdPoint);
            model.getNormals().add(normal);
            face.normalIndexes.add(faceNumber);
        }
    }

    public static Vector3f calculateNormal (Vector3f firstPoint, Vector3f secondPoint, Vector3f thirdPoint){
        Vector3f firstVector = vectorByTwoPoints(firstPoint, secondPoint);
        Vector3f secondVector = vectorByTwoPoints(secondPoint, thirdPoint);
        return new Vector3f((firstVector.getY() * secondVector.getZ()) - (secondVector.getY() * firstVector.getZ()),
                -1 * ((firstVector.getX() * secondVector.getZ()) - (secondVector.getX() * firstVector.getZ())),
                firstVector.getX() * secondVector.getY() - secondVector.getX() * firstVector.getY());
    }

    private static Vector3f vectorByTwoPoints(Vector3f firstPoint, Vector3f secondPoint){
        float newX = Math.abs(secondPoint.getX() - firstPoint.getX());
        float newY = Math.abs(secondPoint.getY() - firstPoint.getY());
        float newZ = Math.abs(secondPoint.getZ() - firstPoint.getZ());
        return new Vector3f(newX, newY, newZ);
    }
}

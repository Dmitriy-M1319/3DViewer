package com.cgvsu.model;

import com.cgvsu.math.vector.Vector3f;

public class ModelSettings {
    private MyModel model;
    private float percentX;
    private float percentY;
    private float percentZ;
    private float alphaX;
    private float alphaY;
    private float alphaZ;
    private Vector3f target;

    public ModelSettings(MyModel model) {
        this.model = model;
        percentX = 2;
        percentY = 2;
        percentZ = 2;
        alphaX = 90;
        alphaY = 90;
        alphaZ = 90;
        target = new Vector3f(0 ,0,0);
    }

    public MyModel getModel() {
        return model;
    }

    public void increaseScale(float value) {
        this.percentX *= value;
        this.percentY *= value;
        this.percentZ *= value;
    }

    public float getPercentX() {
        return percentX;
    }

    public float getAlphaX() {
        return alphaX;
    }

    public float getAlphaY() {
        return alphaY;
    }

    public float getAlphaZ() {
        return alphaZ;
    }


    public Vector3f getTarget() {
        return target;
    }

    public void addX(float additionX) {
        this.target = new Vector3f(target.getX() + additionX, target.getY(), target.getZ());
    }

    public void addY(float additionY) {
        this.target = new Vector3f(target.getX(), target.getY() + additionY, target.getZ());
    }

    public void addZ(float additionZ) {
        this.target = new Vector3f(target.getX(), target.getY(), target.getZ() + additionZ);

    }

    public float getPercentY() {
        return percentY;
    }

    public void setPercentZ(float percentZ) {
        this.percentZ = percentZ;
    }

    public void setPercentY(float percentY) {
        this.percentY = percentY;
    }

    public void setPercentX(float percentX) {
        this.percentX = percentX;
    }

    public float getPercentZ() {
        return percentZ;
    }

    public void plusAlphaX() { alphaX++; }
    public void plusAlphaY() { alphaY++; }
    public void plusAlphaZ() { alphaZ++; }
    public void minusAlphaX() { alphaX--; }
    public void minusAlphaY() { alphaY--; }
    public void minusAlphaZ() { alphaZ--; }

    public void setTarget(Vector3f vector) {
        target = vector;
    }

//    public void setCameraPos(Vector3f cameraPos) {
//        this.cameraPos = cameraPos;
//    }

//    public Vector3f getCameraPos() {
//        return cameraPos;
//    }
}

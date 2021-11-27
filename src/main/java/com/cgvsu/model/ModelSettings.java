package com.cgvsu.model;

import com.cgvsu.math.vector.Vector3f;

public class ModelSettings {
    private MyModel model;
    private float percentX;
    private float percentY;
    private float percentZ;
    private float alpha;
    private Vector3f target;
    private Vector3f cameraPos;

    public ModelSettings(MyModel model, Vector3f cameraPos) {
        this.model = model;
        percentX = 1;
        percentY = 1;
        percentZ = 1;
        alpha = 0;
        target = new Vector3f(0 ,0,0);
        this.cameraPos = cameraPos;
    }

    public MyModel getModel() {
        return model;
    }

    public float getPercentX() {
        return percentX;
    }

    public float getAlpha() {
        return alpha;
    }

    public Vector3f getTarget() {
        return target;
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

    public void plusAlpha() { alpha++; }
    public void minusAlpha() { alpha--; }

    public void setTarget(Vector3f vector) {
        target = vector;
    }

    public void setCameraPos(Vector3f cameraPos) {
        this.cameraPos = cameraPos;
    }

    public Vector3f getCameraPos() {
        return cameraPos;
    }
}

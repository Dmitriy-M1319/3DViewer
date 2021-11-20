package com.cgvsu.model;

import com.cgvsu.math.vector.Vector3f;

public class ModelSettings {
    private MyModel model;
    private float percent;
    private float alpha;
    private Vector3f target;

    public ModelSettings(MyModel model) {
        this.model = model;
        percent = 0;
        alpha = 0;
        target = new Vector3f(0 ,0,0);
    }

    public MyModel getModel() {
        return model;
    }

    public float getPercent() {
        return percent;
    }

    public float getAlpha() {
        return alpha;
    }

    public Vector3f getTarget() {
        return target;
    }

    public void plusPercent() {
        percent += 0.05F;
    }

    public void minusPercent() {
        if (percent > 0.05F) {
            percent -= 0.05F;
        }
    }

    public void plusAlpha() { alpha++; }
    public void minusAlpha() { alpha--; }

    public void setTarget(Vector3f vector) {
        target = vector;
    }

}

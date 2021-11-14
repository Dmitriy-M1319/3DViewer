package com.cgvsu.math.vector;

public class Vector4f  implements Vector {
    private float x;
    private float y;
    private float z;
    private float k;

    public float getX() {
        return x;
    }

    public float getY() {
        return y;
    }

    public float getZ() {
        return z;
    }

    public float getK() {
        return k;
    }

    public Vector4f(float x, float y, float z, float k) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.k = k;
    }

    @Override
    public Vector sum(Vector v) {
        return new Vector4f(this.x + ((Vector4f)v).getX(), this.y + ((Vector4f)v).getY(), this.z + ((Vector4f)v).getZ(), this.k + ((Vector4f)v).getK());
    }

    @Override
    public Vector subtraction(Vector v) {
        return new Vector4f(this.x - ((Vector4f)v).getX(), this.y - ((Vector4f)v).getY(), this.z - ((Vector4f)v).getZ(), this.k - ((Vector4f)v).getK());
    }

    @Override
    public Vector multiply(float a) {
        return new Vector4f(this.x * a, this.y * a, this.z * a, this.k * a);
    }

    @Override
    public float countLine() {
        return (float) Math.sqrt(x * x + y * y + z * z + k * k);
    }

    @Override
    public Vector normal() {
        float l = countLine();
        return new Vector4f(this.x / l, this.y / l, this.z / l, this.k / l);
    }

    @Override
    public float dot(Vector v) {
        return ((Vector4f)v).x * x + ((Vector4f)v).y * y + ((Vector4f)v).z * z + ((Vector4f)v).k * k;
    }
}

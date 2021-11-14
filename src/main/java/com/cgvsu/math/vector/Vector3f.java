package com.cgvsu.math.vector;

public class Vector3f implements Vector {
    /*
        Vector coordinates i, j, k
     */
    private float x;
    private float y;
    private float z;

    public float getX() {
        return x;
    }

    public float getY() {
        return y;
    }

    public float getZ() {
        return z;
    }

    public Vector3f(float x, float y, float z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public Vector sum(final Vector v ) {
        return new Vector3f(this.x + ((Vector3f)v).getX(), this.y + ((Vector3f)v).getY(), this.z + ((Vector3f)v).getZ());
    }

    public Vector subtraction(final Vector v) {
        return new Vector3f(this.x - ((Vector3f)v).getX(), this.y - ((Vector3f)v).getY(), this.z - ((Vector3f)v).getZ());
    }

    public Vector multiply(final float a) {
        return new Vector3f(this.x * a, this.y * a, this.z * a);
    }

    public float countLine() {
        return (float) Math.sqrt(x * x + y * y + z * z);
    }

    public Vector normal() {
        float l = countLine();
        return new Vector3f(this.x / l, this.y / l, this.z / l);
    }

    public float dot(final Vector v) {
        return ((Vector3f)v).x * x + ((Vector3f)v).y * y + ((Vector3f)v).z * z;
    }

    public Vector3f vectorMultiply(final Vector3f v) {
        float newZ = this.x * v.getY() - this.y * v.getX();
        float newX = this.y * v.getZ() - this.z * v.getY();
        float newY =  - (this.x * v.getZ() - this.z * v.getX());
        return new Vector3f(newX, newY, newZ);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Vector3f vector3f = (Vector3f) o;
        return Float.compare(vector3f.x, x) == 0 && Float.compare(vector3f.y, y) == 0 && Float.compare(vector3f.z, z) == 0;
    }

    public String toString() {
        return String.format("%f %f %f", x, y, z);
    }
}

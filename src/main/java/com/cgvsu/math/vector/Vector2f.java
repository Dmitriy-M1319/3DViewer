package com.cgvsu.math.vector;

public class Vector2f implements Vector {
    /*
        Vector coordinates i, j
     */
    private float x;
    private float y;

    public float getX() {
        return x;
    }

    public float getY() {
        return y;
    }

    public Vector2f(float x, float y) {
        this.x = x;
        this.y = y;
    }

    public Vector sum(final Vector v ) {
        return new Vector2f(this.x + ((Vector2f)v).getX(), this.y + ((Vector2f)v).getY());
    }

    public Vector subtraction(final Vector v) {
        return new Vector2f(this.x - ((Vector2f)v).getX(), this.y - ((Vector2f)v).getY());
    }

    public Vector multiply(float a) {
        return new Vector2f(this.x * a, this.y * a);
    }

    public float countLine() {
        return (float) Math.sqrt(x * x + y * y);
    }

    public Vector normal() {
        float l = countLine();
        return new Vector2f(this.x / l, this.y / l);
    }

    public float dot(Vector v) {
        return ((Vector2f)v).x * x + ((Vector2f)v).y * y;
    }

    public String toString() {
        return String.format("%f %f", x, y);
    }
}

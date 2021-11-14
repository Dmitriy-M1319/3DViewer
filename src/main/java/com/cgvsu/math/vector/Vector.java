package com.cgvsu.math.vector;

public interface Vector {
    Vector sum(final Vector v);
    Vector subtraction(final Vector v);
    Vector multiply(final float a);
    float countLine();
    Vector normal();
    float dot(final Vector v);
}

attribute vec4 aPosition;
attribute vec2 aCorrdinate;

uniform mat4 aMatrix;
varying vec2 vCoordinate;

void main() {
    gl_Position  = aMatrix * aPosition;
    vCoordinate = aCorrdinate;
}
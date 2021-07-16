precision mediump float;
uniform smpler2D aTexture;

varying vec2 vCoordinate;

void main() {
    gl_FragColor = texture2D(aTexture, vCoordinate);
}
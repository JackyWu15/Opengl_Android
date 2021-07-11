package com.hechuangwu.openglandroid.shape.touch;

import android.opengl.GLES20;

import com.he.chuangwu.libpraticles.base.BaseGLSL;


/**
 * Created by cwh on 2019/7/1 0001.
 * 功能:
 */
public class Point extends BaseGLSL {
    private String vertexShaderCode =
            "attribute vec3 aPosition;\n" +
                    "void main(){\n" +
                    "gl_Position = vec4(aPosition,1.0);\n" +
                    "gl_PointSize = 100.0;\n" +//点的大小
                    "}";

    private String fragmentShaderCode =
            "precision mediump float;\n" +
                    "uniform vec4 aColor;\n" +
                    "void main(){\n" +
                    "gl_FragColor = aColor;\n" +
                    "}";

    private float[] mPointPos = {0.0f, 0.0f};
    private float[] mColor = {1.0f, 0.0f, 0.0f, 1.0f};
    private int width,height;


    public void setPosition(float[] pointPos) {
        mPointPos = pointPos;
    }

    public void setColor(float[] color) {
        this.mColor = color;
    }

    public int[] getViewPort(){
        return new int[]{this.width,this.height};
    }

    public Point() {
        initProgram();
    }

    @Override
    public void onSurfaceCreated() {
        super.onSurfaceCreated();
        GLES20.glClearColor( 1.0f, 1.0f, 1.0f, 1.0f );
    }

    @Override
    public void onSurfaceChanged(int width, int height) {
        super.onSurfaceChanged( width, height );
        GLES20.glViewport( 0, 0, width, height );
        this.width = width;
        this.height = height;
    }

    @Override
    protected void allocateBuffer() {
    }

    @Override
    protected void initProgram() {
        mProgram = createProgram( vertexShaderCode, fragmentShaderCode );
    }

    @Override
    public void draw() {
        GLES20.glClear( GLES20.GL_COLOR_BUFFER_BIT );
        GLES20.glUseProgram( mProgram );
        int aPosition = GLES20.glGetAttribLocation( mProgram, "aPosition" );
        GLES20.glVertexAttrib3f( aPosition, mPointPos[0], mPointPos[1], 0.0f );

        int aColor = GLES20.glGetUniformLocation( mProgram, "aColor" );
        GLES20.glUniform4fv( aColor, 1, mColor, 0 );

        GLES20.glDrawArrays( GLES20.GL_POINTS, 0, 1 );

    }
}

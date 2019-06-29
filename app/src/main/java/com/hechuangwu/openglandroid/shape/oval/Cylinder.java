package com.hechuangwu.openglandroid.shape.oval;

import android.opengl.GLES20;
import android.opengl.Matrix;

import com.hechuangwu.openglandroid.base.BaseGLSL;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.ArrayList;

/**
 * Created by cwh on 2019/6/29 0029.
 * 功能:
 */
public class Cylinder extends BaseGLSL {
    private int n = 360;  //切割份数
    private float height = 2.0f;  //圆锥高度
    private float radius = 1.0f;  //圆锥底面半径
    private final static String vertexShaderCode =
            "attribute vec4 aPosition;\n" +
                    "uniform mat4 aMatrix;\n" +
                    "varying vec4 aColor;\n" +
                    "void main(){\n" +
                    "    gl_Position=aMatrix * aPosition;\n" +
                    "    if(aPosition.z!=0.0){\n" +
                    "        aColor = vec4(0.0,0.0,0.0,1.0);\n" +
                    "    }else{\n" +
                    "        aColor = vec4(0.9,0.9,0.9,1.0);\n" +
                    "    }\n" +
                    "}";

    private final static String fragmentShaderCode =
            "precision mediump float;\n" +
                    "varying vec4 aColor;\n" +
                    "void main(){\n" +
                    "    gl_FragColor = aColor;\n" +
                    "}";


    public Cylinder(){
        allocateBuffer();
        initProgram();
    }

    private float[] createCylinderPos(){
        ArrayList<Float> pos = new ArrayList<>();
        float angDegSpan = 360f / n;
        for (float i = 0; i < 360 + angDegSpan; i += angDegSpan) {
            pos.add((float) (radius * Math.sin(i * Math.PI / 180f)));
            pos.add((float) (radius * Math.cos(i * Math.PI / 180f)));
            pos.add(height);
            pos.add((float) (radius * Math.sin(i * Math.PI / 180f)));
            pos.add((float) (radius * Math.cos(i * Math.PI / 180f)));
            pos.add(0.0f);
        }
        float[] cylinderPos = new float[pos.size()];
        for (int i = 0; i < cylinderPos.length; i++) {
            cylinderPos[i] = pos.get(i);
        }

        return cylinderPos;
    }
    @Override
    protected void allocateBuffer() {
        float[] cylinderPos = createCylinderPos();
        ByteBuffer buffer = ByteBuffer.allocateDirect(cylinderPos.length * FLOAT_BYTE_SIZE);
        buffer.order( ByteOrder.nativeOrder());
        mVertexBuffer = buffer.asFloatBuffer();
        mVertexBuffer.put(cylinderPos);
        mVertexBuffer.position(0);
        mCoordsSize = cylinderPos.length / COORDS_COMPONENT;
    }


    @Override
    public void onSurfaceChanged(int width, int height) {
        super.onSurfaceChanged( width, height );
        Matrix.setLookAtM(mView, 0, 1.0f, -10.0f, -4.0f, 0f, 0f, 0f, 0f, 1.0f, 0.0f);

        float ratio = (float) width / height;
        Matrix.frustumM(mProjection, 0, -ratio, ratio, -1, 1, 3, 20);

        Matrix.multiplyMM(mMVPMatrix, 0, mProjection, 0, mView, 0);
    }


    @Override
    protected void initProgram() {
        mProgram = createProgram( vertexShaderCode, fragmentShaderCode );
    }
    @Override
    public void onSurfaceCreated() {
        super.onSurfaceCreated();
        GLES20.glEnable( GLES20.GL_DEPTH_TEST );
    }


    @Override
    public void draw() {
        GLES20.glClearColor(1.0f, 1.0f, 1.0f, 1.0f);
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT | GLES20.GL_DEPTH_BUFFER_BIT);
        GLES20.glUseProgram(mProgram);

        int aPosition = GLES20.glGetAttribLocation( mProgram, "aPosition" );
        GLES20.glVertexAttribPointer( aPosition,COORDS_COMPONENT,GLES20.GL_FLOAT,false,0,mVertexBuffer );
        GLES20.glEnableVertexAttribArray( aPosition );

        int aMatrix = GLES20.glGetUniformLocation( mProgram, "aMatrix" );
        GLES20.glUniformMatrix4fv( aMatrix,1,false,mMVPMatrix,0 );

        GLES20.glDrawArrays(GLES20.GL_TRIANGLE_STRIP, 0, mCoordsSize);
        GLES20.glDisableVertexAttribArray( aPosition );
    }
}

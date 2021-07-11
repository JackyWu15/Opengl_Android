package com.hechuangwu.openglandroid.shape.oval;

import android.opengl.GLES20;
import android.opengl.Matrix;

import com.he.chuangwu.libpraticles.base.BaseGLSL;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.ArrayList;

/**
 * Created by cwh on 2019/6/24 0024.
 * 功能:
 */
public class Oval extends BaseGLSL {
    private float radius = 1.0f;
    private int n = 180;  //切割份数
    private float height = 0.0f;

    private String vertexShaderCode =
            "attribute vec4 aPosition;\n" +
                    "uniform mat4 aMatrix;\n" +
                    "void main(){\n" +
                        "gl_Position = aMatrix * aPosition;\n" +
                    "}"
            ;

    private String fragmentShaderCode =
            "precision mediump float;\n" +
                    "uniform vec4 aColor;\n" +
                    "void main(){\n" +
                      "gl_FragColor = aColor;\n" +
                    "}";

    //设置颜色，依次为红绿蓝和透明通道
    float color[] = {1.0f, 1.0f, 1.0f, 1.0f};
    private float[] mOvalPos;


    //修改半径
    public void setRadius(float radius){
        this.radius = radius;
    }
    /**
     * 圆形顶点数据
     * @return
     */
    private float[] createPositions() {
        ArrayList<Float> data = new ArrayList<>();
        data.add(0.0f);
        data.add(0.0f);
        data.add(height);
        float angDegSpan = 360f / n;
        for (float i = 0; i < 360 + angDegSpan; i += angDegSpan) {
            data.add((float) (radius * Math.sin(i * Math.PI / 180f)));
            data.add((float) (radius * Math.cos(i * Math.PI / 180f)));
            data.add(height);
        }
        float[] f = new float[data.size()];
        for (int i = 0; i < f.length; i++) {
            f[i] = data.get(i);
        }
        return f;
    }

    public Oval(){
        allocateBuffer();
        initProgram();
    }

    @Override
    protected void allocateBuffer() {
        mOvalPos = createPositions();
        ByteBuffer byteBuffer = ByteBuffer.allocateDirect( mOvalPos.length * FLOAT_BYTE_SIZE );
        byteBuffer.order( ByteOrder.nativeOrder() );
        mVertexBuffer = byteBuffer.asFloatBuffer();
        mVertexBuffer.put( mOvalPos );
        mVertexBuffer.position(0);
    }

    @Override
    protected void initProgram() {
        mProgram = createProgram( vertexShaderCode,fragmentShaderCode );
    }

    @Override
    public void onSurfaceChanged(int width, int height) {
        super.onSurfaceChanged( width, height );
        Matrix.setLookAtM(mView, 0, 0, 0, 7.0f, 0f, 0f, 0f, 0f, 1.0f, 0.0f);
        float ratio = (float)width/height;
        Matrix.frustumM( mProjection,0,-ratio,ratio,-1,1,3,100 );
        Matrix.multiplyMM( mMVPMatrix,0,mProjection,0,mView,0);

    }

    @Override
    public void draw() {
        GLES20.glUseProgram( mProgram );
        int aPosition = GLES20.glGetAttribLocation( mProgram, "aPosition" );
        GLES20.glVertexAttribPointer( aPosition,COORDS_COMPONENT,GLES20.GL_FLOAT,false,COORDS_COMPONENT*FLOAT_BYTE_SIZE,mVertexBuffer );
        GLES20.glEnableVertexAttribArray( aPosition );

        int aMatrix = GLES20.glGetUniformLocation( mProgram, "aMatrix" );
        GLES20.glUniformMatrix4fv( aMatrix,1,false,mMVPMatrix,0 );

        int aColor = GLES20.glGetUniformLocation( mProgram, "aColor" );
        GLES20.glUniform4fv( aColor,1,color,0 );

        GLES20.glDrawArrays( GLES20.GL_TRIANGLE_FAN,0,mOvalPos.length/COORDS_COMPONENT );

    }
}

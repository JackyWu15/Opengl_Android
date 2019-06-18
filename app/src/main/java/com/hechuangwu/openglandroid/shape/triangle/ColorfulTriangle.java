package com.hechuangwu.openglandroid.shape.triangle;

import android.opengl.GLES20;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

/**
 * Created by cwh on 2019/6/17 0017.
 * 功能:
 */
public class ColorfulTriangle extends CameraTriangle {
    protected  final String vertexColorShaderCode =
            "attribute vec4 aPosition;\n" +
                    "uniform mat4 aMatrix;\n" +
                    "attribute vec4 aColor;\n" +
                    "varying vec4 aFragColor;\n" +
                    "void main(){\n" +
                    "gl_Position = aMatrix * aPosition;\n" +
                    "aFragColor = aColor;\n"+
                    "}";

    protected  final String fragmentColorShaderCode =
            "precision mediump float;\n" +
                    "varying vec4 aFragColor;\n" +
                    "void main(){\n" +
                    "gl_FragColor = aFragColor;\n" +
                    "}";

    //设置颜色
    private static float colorful[] = {
            0.0f, 0.0f, 1.0f, 1.0f,
            0.0f, 1.0f, 0.0f, 1.0f,
            1.0f, 0.0f, 0.0f, 1.0f
    };
    //颜色分量个数
    private final int COLOR_COMPONENT = 4;

    protected static FloatBuffer mColorBuffer=null;


    public ColorfulTriangle(){
        super();
    }

    @Override
    protected void allocateBuffer() {
        super.allocateBuffer();
        ByteBuffer byteBuffer1 = ByteBuffer.allocateDirect( colorful.length * FLOAT_BYTE_SIZE );
        byteBuffer1.order( ByteOrder.nativeOrder() );
        mColorBuffer = byteBuffer1.asFloatBuffer();
        mColorBuffer.put( colorful );
        mColorBuffer.position(0);
    }

    @Override
    protected void initProgram() {
        mProgram = createProgram( vertexColorShaderCode, fragmentColorShaderCode );
    }

    @Override
    public void draw() {
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT | GLES20.GL_DEPTH_BUFFER_BIT);
        GLES20.glUseProgram( mProgram );
        int aPosition = GLES20.glGetAttribLocation( mProgram, "aPosition" );
        GLES20.glVertexAttribPointer( aPosition,COORDS_COMPONENT,GLES20.GL_FLOAT,true,COORDS_COMPONENT*FLOAT_BYTE_SIZE,mVertexBuffer );
        GLES20.glEnableVertexAttribArray( aPosition );

        int aMatrix = GLES20.glGetUniformLocation( mProgram, "aMatrix" );
        GLES20.glUniformMatrix4fv( aMatrix,1,false,mMVPMatrix,0 );

        int aColor = GLES20.glGetAttribLocation( mProgram, "aColor" );
        GLES20.glVertexAttribPointer( aColor,COLOR_COMPONENT,GLES20.GL_FLOAT,true,0,mColorBuffer );
        GLES20.glEnableVertexAttribArray( aColor );

        GLES20.glDrawArrays( GLES20.GL_TRIANGLES,0,COORDS_SIZE );

        GLES20.glDisableVertexAttribArray( aPosition );
        GLES20.glDisableVertexAttribArray( aColor );

    }
}

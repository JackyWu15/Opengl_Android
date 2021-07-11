package com.hechuangwu.openglandroid.shape.square;

import android.opengl.GLES20;
import android.opengl.Matrix;

import com.he.chuangwu.libpraticles.base.BaseGLSL;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.ShortBuffer;

/**
 * Created by cwh on 2019/6/18 0018.
 * 功能:
 */
public class Cube extends BaseGLSL {
    protected final String vertexShaderCode =
            "attribute vec4 aPosition;\n" +
                    "attribute vec4 aColor;\n" +
                    "uniform mat4 aMatrix;\n" +
                    "varying vec4 aFragColor;\n" +
                    "void main (){\n" +
                    "gl_Position = aMatrix * aPosition;\n" +
                    "aFragColor = aColor;\n" +
                    "}";

    protected final String fragmentShaderCode =
            "precision mediump float;\n" +
                    "varying vec4 aFragColor;\n" +
                    "void main(){\n" +
                    "gl_FragColor = aFragColor;\n" +
                    "}";

    protected final float cubeCoords[] = {
            -1.0f, 1.0f, 1.0f,    //正面左上0
            -1.0f, -1.0f, 1.0f,   //正面左下1
            1.0f, -1.0f, 1.0f,    //正面右下2
            1.0f, 1.0f, 1.0f,     //正面右上3
            -1.0f, 1.0f, -1.0f,    //反面左上4
            -1.0f, -1.0f, -1.0f,   //反面左下5
            1.0f, -1.0f, -1.0f,    //反面右下6
            1.0f, 1.0f, -1.0f,     //反面右上7
    };
    final short index[] = {
            6, 7, 4, 6, 4, 5,    //后面
            6, 3, 7, 6, 2, 3,    //右面
            6, 5, 1, 6, 1, 2,    //下面
            0, 3, 2, 0, 2, 1,    //正面
            0, 1, 5, 0, 5, 4,    //左面
            0, 7, 3, 0, 4, 7,    //上面
    };

    //坐标分量个数
    protected  final int COORDS_COMPONENT = 3;

    //顶点颜色
    protected final float colorful[] = {
            0.0f, 1.0f, 0.0f, 0.5f,
            0.0f, 1.0f, 0.0f, 0.5f,
            0.0f, 1.0f, 0.0f, 0.5f,
            0.0f, 1.0f, 0.0f, 0.5f,
            0.0f, 0.0f, 1.0f, 0.5f,
            0.0f, 0.0f, 1.0f, 0.5f,
            0.0f, 0.0f, 1.0f, 0.5f,
            0.0f, 0.0f, 1.0f, 0.5f,
    };
    //颜色分量
    protected final int COLOR_COMPONENT = 4;

    private FloatBuffer mColorBuffer;
    private ShortBuffer mIndexBuffer;

    public Cube(){
        allocateBuffer();
        initProgram();
    }

    @Override
    public void onSurfaceCreated() {
        super.onSurfaceCreated();
        GLES20.glEnable( GLES20.GL_DEPTH_TEST );
    }

    @Override
    protected void allocateBuffer() {
        ByteBuffer byteBuffer = ByteBuffer.allocateDirect( cubeCoords.length * FLOAT_BYTE_SIZE );
        byteBuffer.order( ByteOrder.nativeOrder() );
        mVertexBuffer = byteBuffer.asFloatBuffer();
        mVertexBuffer.put( cubeCoords );
        mVertexBuffer.position(0);

        ByteBuffer byteBuffer1 = ByteBuffer.allocateDirect( colorful.length * FLOAT_BYTE_SIZE );
        byteBuffer1.order( ByteOrder.nativeOrder() );
        mColorBuffer = byteBuffer1.asFloatBuffer();
        mColorBuffer.put( colorful );
        mColorBuffer.position(0);

        ByteBuffer byteBuffer2 = ByteBuffer.allocateDirect( index.length * SHORT_BYTE_SIZE );
        byteBuffer2.order( ByteOrder.nativeOrder() );
        mIndexBuffer = byteBuffer2.asShortBuffer();
        mIndexBuffer.put( index );
        mIndexBuffer.position(0);

    }

    @Override
    protected void initProgram() {
        mProgram = createProgram( vertexShaderCode, fragmentShaderCode );
    }

    @Override
    public void onSurfaceChanged(int width, int height) {
        super.onSurfaceChanged( width, height );
        Matrix.setLookAtM( mView,0,5.0f,5.0f,7.0f,0.0f,0.0f,0.0f,0.0f,1.0f,0.0f);
        float ratio = (float) width / height;
        Matrix.frustumM( mProjection,0,-ratio,ratio,-1.0f,1.0f,3.0f,100.0f );
        Matrix.multiplyMM( mMVPMatrix,0,mProjection,0,mView,0 );
    }

    @Override
    public void draw() {
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT | GLES20.GL_DEPTH_BUFFER_BIT);
        GLES20.glUseProgram( mProgram );

        int aPosition = GLES20.glGetAttribLocation( mProgram, "aPosition" );
        GLES20.glVertexAttribPointer( aPosition,COORDS_COMPONENT,GLES20.GL_FLOAT,true,COORDS_COMPONENT*FLOAT_BYTE_SIZE,mVertexBuffer );
        GLES20.glEnableVertexAttribArray( aPosition );

        int aColor = GLES20.glGetAttribLocation( mProgram, "aColor" );
        GLES20.glVertexAttribPointer(aColor, COLOR_COMPONENT, GLES20.GL_FLOAT, false, COLOR_COMPONENT*FLOAT_BYTE_SIZE, mColorBuffer);
        GLES20.glEnableVertexAttribArray(aColor);

        int aMatrix = GLES20.glGetUniformLocation( mProgram, "aMatrix" );
        GLES20.glUniformMatrix4fv( aMatrix,1,false,mMVPMatrix,0 );

        GLES20.glDrawElements( GLES20.GL_TRIANGLES,index.length,GLES20.GL_UNSIGNED_SHORT,mIndexBuffer );

        GLES20.glDisableVertexAttribArray( aPosition );
        GLES20.glDisableVertexAttribArray( aColor );



    }

}

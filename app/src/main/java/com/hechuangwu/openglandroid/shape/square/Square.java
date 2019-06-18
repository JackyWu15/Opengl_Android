package com.hechuangwu.openglandroid.shape.square;

import android.opengl.GLES20;
import android.opengl.Matrix;

import com.hechuangwu.openglandroid.base.BaseGLSL;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.ShortBuffer;

/**
 * Created by cwh on 2019/6/17 0017.
 * 功能:
 */
public class Square extends BaseGLSL {
    protected final String vertexShaderCode =
            "attribute vec4 aPosition;\n" +
                    "uniform mat4 aMatrix;\n" +
                    "void main(){\n" +
                    "gl_Position = aMatrix * aPosition;\n" +
                    "}";

    //片元着色器
    protected final String fragmentShaderCode =
            "precision mediump float;\n" +
                    "uniform vec4 aFragColor;\n" +
                    "void main(){\n" +
                    "gl_FragColor = aFragColor;\n" +
                    "}";

    //顶点数据
    protected final float squareCoords[] = {
            0.5f,0.5f,0.0f,
            0.5f,-0.5f,0.0f,
            -0.5f,-0.5f,0.0f,
            -0.5f,0.5f,0.0f
    };
    //索引顺序
    protected short  index[] = {
            0,1,2,0,3,2
    };

    //坐标分量数
    protected final int COORDS_COMPONENT = 3;

    //颜色
    protected final float []colorful = {1.0f, 0.5f, 1.0f, 0.5f};
    private static  FloatBuffer mVertexBuffer;
    private int mProgram;
    private ShortBuffer mIndexBuffer;

    public Square(){
        allocateBuffer();
        initProgram();
    }

    @Override
    protected void allocateBuffer(){
        ByteBuffer byteBuffer = ByteBuffer.allocateDirect( squareCoords.length * FLOAT_BYTE_SIZE );
        byteBuffer.order( ByteOrder.nativeOrder() );
        mVertexBuffer = byteBuffer.asFloatBuffer();
        mVertexBuffer.put( squareCoords );
        mVertexBuffer.position(0);

        ByteBuffer byteBuffer1 = ByteBuffer.allocateDirect( index.length * SHORT_BYTE_SIZE );
        byteBuffer1.order( ByteOrder.nativeOrder() );
        mIndexBuffer = byteBuffer1.asShortBuffer();
        mIndexBuffer.put( index );
        mIndexBuffer.position(0);

    }

    @Override
    protected void initProgram(){
        mProgram = createProgram( vertexShaderCode, fragmentShaderCode );
    }

    public void onSurfaceChanged(int width,int height){
        //视图矩阵变换，可以通过改变物体位置或相机位置，来改变视图，这里改变相机位置
        Matrix.setLookAtM( mView,0,0.0f,0.0f,7.0f,0.0f,0.0f,0.0f,0.0f,1.0f,0.0f);
        //透视变换
        float ratio = (float) width / height;
        Matrix.frustumM( mProjection,0,-ratio,ratio,-1.0f,1.0f,3.0f,7.0f );
        //矩阵运算
        Matrix.multiplyMM( mMVPMatrix,0,mProjection,0,mView,0 );
    }


    public void draw(){
        GLES20.glUseProgram( mProgram );

        int aPosition = GLES20.glGetAttribLocation( mProgram, "aPosition" );
        GLES20.glVertexAttribPointer( aPosition,COORDS_COMPONENT,GLES20.GL_FLOAT,true,COORDS_COMPONENT*FLOAT_BYTE_SIZE,mVertexBuffer );
        GLES20.glEnableVertexAttribArray( aPosition );

        int aMatrix = GLES20.glGetUniformLocation( mProgram, "aMatrix" );
        GLES20.glUniformMatrix4fv( aMatrix,1,false,mMVPMatrix,0 );

        int aFragColor = GLES20.glGetUniformLocation( mProgram, "aFragColor" );
        GLES20.glUniform4fv( aFragColor,1,colorful,0 );

        //通过索引绘制图形，opengl只能画点、线、三角形
        GLES20.glDrawElements( GLES20.GL_TRIANGLES,index.length,GLES20.GL_UNSIGNED_SHORT,mIndexBuffer );

        GLES20.glDisableVertexAttribArray( aPosition );

    }
}

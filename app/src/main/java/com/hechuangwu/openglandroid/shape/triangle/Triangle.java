package com.hechuangwu.openglandroid.shape.triangle;

import android.opengl.GLES20;

import com.hechuangwu.openglandroid.base.BaseGLSL;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

/**
 * Created by cwh on 2019/6/4 0004.
 * 功能:
 */
public class Triangle extends BaseGLSL{

    // 顶点着色器
    protected  final String vertexShaderCode =
            "attribute vec4 aPosition;\n" +
                    " void main() {\n" +
                    "     gl_Position   = aPosition;\n" +
                    " }";

    // 片元着色器
    protected  final String fragmentShaderCode =
            " precision mediump float;\n" +
                    " uniform vec4 aColor;\n" +
                    " void main() {\n" +
                    "     gl_FragColor = aColor;\n" +
                    " }";
    //顶点坐标
    protected  float triangleCoords[] = {
            0.0f, 0.622008459f, 0.0f,
            -0.5f, -0.311004243f, 0.0f,
            0.5f, -0.311004243f, 0.0f
    };

    //顶点的个数
    protected  final int COORDS_SIZE = triangleCoords.length/COORDS_COMPONENT;
    //填充颜色
    protected  float color[] = {1.0f,0.5f,0.5f,1.0f};


    public Triangle() {
        allocateBuffer();
        initProgram();
    }

    @Override
    protected void allocateBuffer(){
        GLES20.glClearColor( 1.0f, 1.0f, 1.0f, 1.0f );
        //为顶点数据分配内存
        ByteBuffer byteBuffer = ByteBuffer.allocateDirect( triangleCoords.length * FLOAT_BYTE_SIZE );
        byteBuffer.order( ByteOrder.nativeOrder() );
        mVertexBuffer = byteBuffer.asFloatBuffer();
        mVertexBuffer.put( triangleCoords );
        mVertexBuffer.position(0);
    }

    @Override
    protected void initProgram(){
        mProgram = createProgram( vertexShaderCode, fragmentShaderCode );
    }

    @Override
    public void draw(){
            //开启程序
            GLES20.glUseProgram( mProgram );
            //顶点着色器坐标入口
            int aPosition = GLES20.glGetAttribLocation( mProgram, "aPosition" );
            //指向着色器内存，并指定步长和数据，存入数据
            GLES20.glVertexAttribPointer( aPosition,COORDS_COMPONENT,GLES20.GL_FLOAT,true,COORDS_COMPONENT*FLOAT_BYTE_SIZE, mVertexBuffer);
            //顶点着色器默认关闭，需启动
            GLES20.glEnableVertexAttribArray( aPosition );

            //片元着色器，注意使用前必须开启程序
            int aColor = GLES20.glGetUniformLocation( mProgram, "aColor" );
            //设置颜色
            GLES20.glUniform4fv( aColor,1, color,0);
//            GLES20.glUniform4f( vColor,1.0f,0.5f,0.5f,1.0f);

            //画三角形
            GLES20.glDrawArrays( GLES20.GL_TRIANGLES,0,COORDS_SIZE);
            //关闭顶点着色器
            GLES20.glDisableVertexAttribArray( aPosition );
    }
}

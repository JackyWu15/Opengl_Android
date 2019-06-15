package com.hechuangwu.openglandroid.shape.triangle;

import android.opengl.GLES20;

import com.hechuangwu.openglandroid.base.BaseGLSL;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

/**
 * Created by cwh on 2019/6/4 0004.
 * 功能:
 */
public class Triangle extends BaseGLSL{

    // 简单的顶点着色器
    public static final String vertexShaderCode =
            "attribute vec4 vPosition;\n" +
                    " void main() {\n" +
                    "     gl_Position   = vPosition;\n" +
                    " }";

    // 简单的片元着色器
    public static final String fragmentShaderCode =
            " precision mediump float;\n" +
                    " uniform vec4 vColor;\n" +
                    " void main() {\n" +
                    "     gl_FragColor = vColor;\n" +
                    " }";
    //顶点坐标
    public static float triangleCoords[] = {
            0.0f, 0.5f, 0.0f,
            -0.5f, -0.5f, 0.0f,
            0.5f, -0.5f, 0.0f,
    };

    //坐标分量个数
    private static final int COORDS_COMPONENT = 3;
    //顶点个数
    private static final int COORDS_SIZE = triangleCoords.length/COORDS_COMPONENT;
    //数据类型占用字节数
    private static final int BYTE_SIZE = Float.SIZE/Byte.SIZE;
    //填充颜色
//    public static float color[] = {1.0f,1.0f,1.0f,1.0f};
    private final int mProgram;
    //顶点数组
    private final FloatBuffer mVertexBuffer;


    public Triangle() {
        GLES20.glClearColor( 0.5f, 0.5f, 0.5f, 1.0f );
        //为顶点数据分配内存
        ByteBuffer byteBuffer = ByteBuffer.allocateDirect( triangleCoords.length * BYTE_SIZE );
        byteBuffer.order( ByteOrder.nativeOrder() );
        mVertexBuffer = byteBuffer.asFloatBuffer();
        mVertexBuffer.put( triangleCoords );
        mVertexBuffer.position(0);

        mProgram = createProgram( vertexShaderCode, fragmentShaderCode );
    }

    public void draw(){
        if(mProgram!=0){
            //开启程序
            GLES20.glUseProgram( mProgram );
            //顶点着色器坐标入口
            int vPosition = GLES20.glGetAttribLocation( mProgram, "vPosition" );
            //指向着色器内存，并指定步长和数据，存入数据
            GLES20.glVertexAttribPointer( vPosition,COORDS_COMPONENT,GLES20.GL_FLOAT,true,COORDS_COMPONENT*(BYTE_SIZE), mVertexBuffer);
            //顶点着色器默认关闭，需启动
            GLES20.glEnableVertexAttribArray( vPosition );

            //片元着色器，注意使用前必须开启程序
            int vColor = GLES20.glGetUniformLocation( mProgram, "vColor" );
            //设置颜色
//            GLES20.glUniform4fv( vColor,1, color,0);
            GLES20.glUniform4f( vColor,1.0f,0.5f,0.5f,1.0f);

            //画三角形
            GLES20.glDrawArrays( GLES20.GL_TRIANGLES,0,COORDS_SIZE);
            //关闭顶点着色器
            GLES20.glDisableVertexAttribArray( vPosition );
        }
    }
}

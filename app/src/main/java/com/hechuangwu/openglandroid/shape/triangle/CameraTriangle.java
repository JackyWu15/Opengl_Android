package com.hechuangwu.openglandroid.shape.triangle;

import android.opengl.GLES20;
import android.opengl.GLES30;
import android.opengl.Matrix;

/**
 * Created by cwh on 2019/6/17 0017.
 * 功能:
 */
public class CameraTriangle extends Triangle {
    //加入矩阵变换的顶点着色器
    public static final String vertexMatrixShaderCode =
            "attribute vec4 aPosition;\n" +
                    "uniform mat4 aMatrix;\n" +
                    "void main(){" +
                    "gl_Position = aMatrix*aPosition;\n" +
                    "}";

    //模型矩阵
    protected float[] mModel= new float[16];
    //视图矩阵
    protected float[] mView = new float[16];
    //透视矩阵
    protected float[] mProjection = new float[16];
    //运算后的矩阵
    protected float[] mMVPMatrix = new float[16];

    public CameraTriangle(){
        super();
        initProgram();
    }

    @Override
    protected void initProgram() {
        mProgram = createProgram( vertexMatrixShaderCode, fragmentShaderCode );
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

    @Override
    public void draw() {
        GLES20.glUseProgram( mProgram );

        //设置顶点着色器数据
        int aPosition = GLES20.glGetAttribLocation( mProgram, "aPosition" );
        GLES20.glVertexAttribPointer( aPosition,COORDS_COMPONENT, GLES30.GL_FLOAT,true,COORDS_COMPONENT*BYTE_SIZE,mVertexBuffer );
        GLES20.glEnableVertexAttribArray( aPosition );

        //设置矩阵
        int aMatrix = GLES20.glGetUniformLocation( mProgram, "aMatrix" );
        GLES20.glUniformMatrix4fv(aMatrix,1,false,mMVPMatrix,0  );

        //设置片元着色器颜色
        int aColor = GLES20.glGetUniformLocation( mProgram, "aColor" );
        GLES20.glUniform4fv( aColor,1,color,0 );

        //绘制三角形
        GLES20.glDrawArrays( GLES20.GL_TRIANGLES,0,COORDS_SIZE);

        //关闭顶点着色器
        GLES20.glDisableVertexAttribArray( aPosition );


    }


}

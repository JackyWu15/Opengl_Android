package com.hechuangwu.openglandroid.shape.touch;

import android.opengl.GLES20;
import android.opengl.Matrix;

import com.hechuangwu.openglandroid.shape.triangle.Triangle;

/**
 * Created by cwh on 2019/7/1 0001.
 * 功能:
 */
public class RotateTriangle extends Triangle {

    // 顶点着色器
    protected  final String vertexShaderCode =
            "attribute vec4 aPosition;\n" +
                    "uniform mat4 aMatrix;\n" +
                    " void main() {\n" +
                    "     gl_Position   = aMatrix * aPosition;\n" +
                    " }";

    private float angle;
    private final float[] mRotationMatrix = new float[16];
    private int width,height;
    public RotateTriangle(){
        allocateBuffer();
        initProgram();
    }

    @Override
    protected void initProgram() {
        mProgram = createProgram( vertexShaderCode, fragmentShaderCode );
    }

    @Override
    public void onSurfaceCreated() {
        super.onSurfaceCreated();
        GLES20.glClearColor(0.0f, 0.0f, 0.0f, 1.0f);

    }

    @Override
    public void onSurfaceChanged(int width, int height) {
        super.onSurfaceChanged( width, height );
        GLES20.glViewport(0, 0, width, height);
        float ratio = (float) width / height;
        Matrix.frustumM(mProjection, 0, -ratio, ratio, -1, 1, 3, 7);
        this.width = width;
        this.height = height;
    }

    public int[] getViewPort(){
        return new int[]{this.width,this.height};
    }
    @Override
    public void draw() {
        float[] scratch = new float[16];
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT | GLES20.GL_DEPTH_BUFFER_BIT);
        Matrix.setLookAtM(mView, 0, 0, 0, -3, 0f, 0f, 0f, 0f, 1.0f, 0.0f);
        Matrix.multiplyMM(mMVPMatrix, 0, mProjection, 0, mView, 0);

        //启用自动渲染模式，三角形自动旋转
//         long time = SystemClock.uptimeMillis() % 4000L;
//         float angle = 0.090f * ((int) time);
        Matrix.setRotateM(mRotationMatrix, 0, angle, 0, 0, 1.0f);//旋转矩阵
        Matrix.multiplyMM(scratch, 0, mMVPMatrix, 0, mRotationMatrix, 0);//计算旋转后的矩阵

        GLES20.glUseProgram( mProgram );
        int aPosition = GLES20.glGetAttribLocation( mProgram, "aPosition" );
        GLES20.glVertexAttribPointer( aPosition,COORDS_COMPONENT,GLES20.GL_FLOAT,true,COORDS_COMPONENT*FLOAT_BYTE_SIZE, mVertexBuffer);
        GLES20.glEnableVertexAttribArray( aPosition );

        int aMatrix = GLES20.glGetUniformLocation( mProgram, "aMatrix" );
        GLES20.glUniformMatrix4fv( aMatrix,1,false,scratch,0 );

        int aColor = GLES20.glGetUniformLocation( mProgram, "aColor" );
        GLES20.glUniform4fv( aColor,1, color,0);
        GLES20.glDrawArrays( GLES20.GL_TRIANGLES,0,COORDS_SIZE);
        GLES20.glDisableVertexAttribArray( aPosition );
    }

    public float getAngle() {
        return angle;
    }

    public void setAngle(float angle){
        this.angle = angle;
    }
}

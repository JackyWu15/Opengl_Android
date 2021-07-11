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
public class Ball extends BaseGLSL {
    //处理成沿中心向x轴两边扩散，越来越亮
    protected String vertexShaderCode =
            "attribute vec4 aPosition;\n" +
                    "uniform mat4 aMatrix;\n" +
                    "varying vec4 aColor;\n" +
                    "void main(){\n" +
                        "gl_Position = aMatrix * aPosition;\n" +
                        "float color;\n" +
                        "if(aPosition.x>0.0){\n" +
                            "color = aPosition.x;\n" +
                        "}else{\n" +
                            "color = -aPosition.x;\n" +
                        "}\n" +
                        "aColor = vec4(color,color,color,1.0f);\n" +
                    "}";

    protected String fragmentShaderCode =
            "precision mediump float;\n" +
                    "varying vec4 aColor;\n" +
                    "void main(){\n" +
                        "gl_FragColor = aColor;\n" +
                    "}";



    private float step = 1.0f;

    //球以(0,0,0)为中心，以R为半径，则球上任意一点的坐标为
    // ( R * cos(a) * sin(b),y0 = R * sin(a),R * cos(a) * cos(b))
    // 其中，a为圆心到点的线段与xz平面的夹角，b为圆心到点的线段在xz平面的投影与z轴的夹角
    protected float[] createBallPos(){
        ArrayList<Float> data = new ArrayList<>();
        float r1, r2;
        float h1, h2;
        float sin, cos;
        for (float i = -90; i < 90 + step; i += step) {
            r1 = (float) Math.cos(i * Math.PI / 180.0);
            r2 = (float) Math.cos((i + step) * Math.PI / 180.0);
            h1 = (float) Math.sin(i * Math.PI / 180.0);
            h2 = (float) Math.sin((i + step) * Math.PI / 180.0);
            // 固定纬度, 360 度旋转遍历一条纬线
            float step2 = step * 2;
            for (float j = 0.0f; j < 360.0f + step; j += step2) {
                cos = (float) Math.cos(j * Math.PI / 180.0);
                sin = -(float) Math.sin(j * Math.PI / 180.0);

                data.add(r2 * cos);
                data.add(h2);
                data.add(r2 * sin);
                data.add(r1 * cos);
                data.add(h1);
                data.add(r1 * sin);
            }
        }
        float[] f = new float[data.size()];
        for (int i = 0; i < f.length; i++) {
            f[i] = data.get(i);
        }
        return f;

    }

    public Ball(){
        allocateBuffer();
        initProgram();
    }
    @Override
    protected void allocateBuffer() {
        float[] ballPos = createBallPos();
        ByteBuffer byteBuffer = ByteBuffer.allocateDirect( ballPos.length * FLOAT_BYTE_SIZE );
        byteBuffer.order( ByteOrder.nativeOrder() );
        mVertexBuffer = byteBuffer.asFloatBuffer();
        mVertexBuffer.put( ballPos );
        mVertexBuffer.position(0);
        mCoordsSize = ballPos.length / COORDS_COMPONENT;
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
    public void onSurfaceChanged(int width, int height) {
        super.onSurfaceChanged( width, height );
        Matrix.setLookAtM( mView,0,1.0f,-5.0f,1.0f, 0f, 0f, 0f, 0f, 1.0f, 0.0f);
        float ratio = (float)width/height;
        Matrix.frustumM( mProjection,0,-ratio,ratio,-1.0f,1.0f,2.0f,100.0f );
        Matrix.multiplyMM( mMVPMatrix,0,mProjection,0,mView,0 );
    }

    @Override
    public void draw() {
        GLES20.glClearColor( 1.0f,1.0f,0.5f,0.5f );
        GLES20.glClear( GLES20.GL_COLOR_BUFFER_BIT|GLES20.GL_DEPTH_BUFFER_BIT );
        GLES20.glUseProgram( mProgram );

        int aPosition = GLES20.glGetAttribLocation( mProgram, "aPosition" );
        GLES20.glVertexAttribPointer( aPosition,COORDS_COMPONENT,GLES20.GL_FLOAT,false,0,mVertexBuffer );
        GLES20.glEnableVertexAttribArray( aPosition );

        int aMatrix = GLES20.glGetUniformLocation( mProgram, "aMatrix" );
        GLES20.glUniformMatrix4fv( aMatrix,1,false,mMVPMatrix,0 );

        GLES20.glDrawArrays( GLES20.GL_TRIANGLE_FAN,0,mCoordsSize );

        GLES20.glDisableVertexAttribArray( aPosition );


    }
}

package com.hechuangwu.openglandroid.shape.image;

import android.graphics.Bitmap;
import android.opengl.GLES20;
import android.opengl.GLUtils;
import android.opengl.Matrix;

import com.hechuangwu.openglandroid.base.BaseGLSL;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

/**
 * Created by cwh on 2019/7/1 0001.
 * 功能:
 */
public class Image extends BaseGLSL {
    private String vertexShaderCode =
            "attribute vec4 aPosition;\n" +
                    "attribute vec2 aCoordinate;\n" +
                    "uniform mat4 aMatrix;\n" +
                    "varying vec2 Coordinate;\n" +
                    "void main(){\n" +
                        "gl_Position = aMatrix * aPosition;\n" +
                        "Coordinate = aCoordinate;\n" +
                    "}";
    private String fragmentShaderCode =
            "precision mediump float;\n" +
                    "uniform sampler2D aTexture;\n" +
                    "varying vec2 Coordinate;\n" +
                    "void main(){\n" +
                    "gl_FragColor = texture2D(aTexture,Coordinate);\n" +//底层会激活纹理单元，将纹理数据采集到指定的顶点位置
                    "}";

    private Bitmap mBitmap;
    private FloatBuffer mTextureCoordBuffer;

    //顶点坐标
    private float[] vertexCoords = {
            -1.0f, 1.0f,
            -1.0f, -1.0f,
            1.0f, 1.0f,
            1.0f, -1.0f
    };
    //纹理坐标(纹理坐标和顶点坐标上下相反）
    private float[] textureCoord = {
            0.0f, 0.0f,
            0.0f, 1.0f,
            1.0f, 0.0f,
            1.0f, 1.0f,
    };

    //坐标分量个数
    protected   int COORDS_COMPONENT = 2;
    public Image(){
        allocateBuffer();
        initProgram();
    }
    public void setBitmap(Bitmap bitmap) {
        mBitmap = bitmap;
    }

    @Override
    protected void allocateBuffer() {
        ByteBuffer byteBuffer = ByteBuffer.allocateDirect( vertexCoords.length * FLOAT_BYTE_SIZE );
        byteBuffer.order( ByteOrder.nativeOrder() );
        mVertexBuffer = byteBuffer.asFloatBuffer();
        mVertexBuffer.put( vertexCoords );
        mVertexBuffer.position( 0 );
        ByteBuffer cc = ByteBuffer.allocateDirect( textureCoord.length * FLOAT_BYTE_SIZE );
        cc.order( ByteOrder.nativeOrder() );
        mTextureCoordBuffer = cc.asFloatBuffer();
        mTextureCoordBuffer.put( textureCoord );
        mTextureCoordBuffer.position( 0 );
        mCoordsSize  = vertexCoords.length / COORDS_COMPONENT;
    }

    @Override
    public void onSurfaceCreated() {
        super.onSurfaceCreated();
        GLES20.glClearColor( 1.0f, 1.0f, 1.0f, 1.0f );
        GLES20.glEnable( GLES20.GL_TEXTURE_2D );
    }

    @Override
    protected void initProgram() {
        mProgram = createProgram( vertexShaderCode, fragmentShaderCode );
    }

    @Override
    public void onSurfaceChanged(int width, int height) {
        super.onSurfaceChanged( width, height );
        GLES20.glViewport(0, 0, width, height);
        Matrix.setLookAtM(mView, 0, 0, 0, 7.0f, 0f, 0f, 0f, 0f, 1.0f, 0.0f);

        int bitmapWidth = mBitmap.getWidth();
        int bitmapHeight = mBitmap.getHeight();
        float bitmapRatio = (float) bitmapWidth / bitmapHeight;
        float screenRatio = (float) width / height;

        //正交投影
        if(width>height){
            if(bitmapRatio>screenRatio){
                Matrix.orthoM( mProjection,0,-screenRatio*bitmapRatio,screenRatio*bitmapRatio,-1,1,3,7 );
            }else {
                Matrix.orthoM( mProjection,0,-screenRatio/bitmapRatio,screenRatio/bitmapRatio,-1,1,3,7 );
            }
        }else {
            if(bitmapRatio>screenRatio){
                Matrix.orthoM( mProjection,0,-1,1,-bitmapRatio/screenRatio,bitmapRatio/screenRatio,3,7 );
            }else {
                Matrix.orthoM( mProjection,0,-1,1,-screenRatio/bitmapRatio,screenRatio/bitmapRatio,3,7 );
            }
        }

        Matrix.multiplyMM( mMVPMatrix,0,mProjection,0,mView,0 );
    }

    //生成纹理
    private int createTexture(){
        int[] texture;
        if(mBitmap!=null&&!mBitmap.isRecycled()){
            texture = new int[1];
            //开辟内存，生成一块纹理空间
            GLES20.glGenTextures( 1,texture,0 );
            //绑定纹理空间，指定为纹理类型，纹理生成
            GLES20.glBindTexture( GLES20.GL_TEXTURE_2D,texture[0] );
            //纹理范围是0-1，横向和纵向超过了都以图像重复显示
            GLES20.glTexParameterf( GLES20.GL_TEXTURE_2D,GLES20.GL_TEXTURE_WRAP_S, GLES20.GL_REPEAT );
            GLES20.glTexParameterf( GLES20.GL_TEXTURE_2D,GLES20.GL_TEXTURE_WRAP_T, GLES20.GL_REPEAT );
            //取顶点坐标附近的颜色，进行混合
            GLES20.glTexParameterf( GLES20.GL_TEXTURE_2D,GLES20.GL_TEXTURE_MIN_FILTER,GLES20.GL_LINEAR );
            GLES20.glTexParameterf( GLES20.GL_TEXTURE_2D,GLES20.GL_TEXTURE_MAG_FILTER,GLES20.GL_LINEAR );
            //将图片填充到纹理中，并传递到片元着色器
            GLUtils.texImage2D( GLES20.GL_TEXTURE_2D,0,mBitmap,0 );
            return texture[0];
        }
        return -1;
    }

    @Override
    public void draw() {
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT | GLES20.GL_DEPTH_BUFFER_BIT);
        GLES20.glUseProgram(mProgram);
        int aPosition = GLES20.glGetAttribLocation( mProgram, "aPosition" );
        GLES20.glVertexAttribPointer( aPosition,COORDS_COMPONENT,GLES20.GL_FLOAT,false,COORDS_COMPONENT*FLOAT_BYTE_SIZE, mVertexBuffer);
        GLES20.glEnableVertexAttribArray( aPosition );

        int aMatrix = GLES20.glGetUniformLocation( mProgram, "aMatrix" );
        GLES20.glUniformMatrix4fv(aMatrix,1,false,mMVPMatrix,0  );

        int aCoordinate = GLES20.glGetAttribLocation( mProgram, "aCoordinate" );
        GLES20.glVertexAttribPointer( aCoordinate,COORDS_COMPONENT,GLES20.GL_FLOAT,false,COORDS_COMPONENT*FLOAT_BYTE_SIZE, mTextureCoordBuffer);
        GLES20.glEnableVertexAttribArray( aCoordinate );

        //为采样器分配纹理单元，不设置也会自动分配一个
        int aTexture = GLES20.glGetUniformLocation( mProgram, "aTexture" );
        GLES20.glUniform1i(aTexture,0);
        int textureId = createTexture();
        if(textureId!=-1){
            GLES20.glDrawArrays( GLES20.GL_TRIANGLE_STRIP,0,mCoordsSize);
        }

    }


}

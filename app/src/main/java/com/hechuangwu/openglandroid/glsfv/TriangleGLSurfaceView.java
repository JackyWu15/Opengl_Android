package com.hechuangwu.openglandroid.glsfv;

import android.content.Context;
import android.opengl.GLES20;

import com.hechuangwu.openglandroid.base.BaseGLSurfaceView;
import com.hechuangwu.openglandroid.shape.square.MatrixCube;
import com.hechuangwu.openglandroid.shape.triangle.CameraTriangle;
import com.hechuangwu.openglandroid.shape.triangle.ColorfulTriangle;
import com.hechuangwu.openglandroid.shape.triangle.Triangle;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

/**
 * Created by cwh on 2019/6/4 0004.
 * 功能:
 */
public class TriangleGLSurfaceView extends BaseGLSurfaceView {
    public TriangleGLSurfaceView(Context context) {
        super( context );

        setRenderer( new TriangleRenderer() );//普通三角形
//        setRenderer( new CameraTriangleRenderer() );//矩阵变换下三角形
//        setRenderer( new ColorTriangleRenderer() );//顶点颜色三角形
    }


    class  TriangleRenderer implements Renderer{
        Triangle triangle;
        @Override
        public void onSurfaceCreated(GL10 gl, EGLConfig config) {
            triangle = new Triangle();

        }

        @Override
        public void onSurfaceChanged(GL10 gl, int width, int height) {
            GLES20.glViewport( 0,0,width,height );
        }

        @Override
        public void onDrawFrame(GL10 gl) {
            triangle.draw();
        }
    }


    class CameraTriangleRenderer implements Renderer{
        CameraTriangle cameraTriangle;
        @Override
        public void onSurfaceCreated(GL10 gl, EGLConfig config) {
             cameraTriangle = new CameraTriangle();
        }
        @Override
        public void onSurfaceChanged(GL10 gl, int width, int height) {
            cameraTriangle.onSurfaceChanged( width,height );
        }
        @Override
        public void onDrawFrame(GL10 gl) {
            cameraTriangle.draw();
        }
    }

    class ColorTriangleRenderer implements Renderer{
        ColorfulTriangle colorTriangle;
        @Override
        public void onSurfaceCreated(GL10 gl, EGLConfig config) {
            colorTriangle = new ColorfulTriangle();
        }
        @Override
        public void onSurfaceChanged(GL10 gl, int width, int height) {
            colorTriangle.onSurfaceChanged( width,height );
        }
        @Override
        public void onDrawFrame(GL10 gl) {
            colorTriangle.draw();
        }
    }

    class MatrixRenderer implements Renderer{
        MatrixCube matrixCube;
        @Override
        public void onSurfaceCreated(GL10 gl, EGLConfig config) {
            matrixCube = new MatrixCube();
            matrixCube.onSurfaceCreated();
        }

        @Override
        public void onSurfaceChanged(GL10 gl, int width, int height) {
            matrixCube.onSurfaceChanged(width,height);
        }
        @Override
        public void onDrawFrame(GL10 gl) {
            matrixCube.draw();
        }




    }

}

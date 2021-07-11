package com.hechuangwu.openglandroid.glsfv;

import android.content.Context;

import com.he.chuangwu.libpraticles.base.BaseGLSurfaceView;
import com.hechuangwu.openglandroid.shape.square.Cube;
import com.hechuangwu.openglandroid.shape.square.MatrixCube;
import com.hechuangwu.openglandroid.shape.square.Square;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

/**
 * Created by cwh on 2019/6/17 0017.
 * 功能:
 */
public class SquareGLSurfaceView extends BaseGLSurfaceView {
    public SquareGLSurfaceView(Context context) {
        super( context );
//        setRenderer( new SquareRenderer() );//普通正方形
//        setRenderer( new CubeRenderer() );//立方体
        setRenderer( new MatrixCubeRenderer() );//平移旋转缩放立方体
    }

    class SquareRenderer implements Renderer{
        Square square;
        @Override
        public void onSurfaceCreated(GL10 gl, EGLConfig config) {
            square = new Square();
        }
        @Override
        public void onSurfaceChanged(GL10 gl, int width, int height) {
            square.onSurfaceChanged( width,height );
        }

        @Override
        public void onDrawFrame(GL10 gl) {
            square.draw();
        }
    }


    class CubeRenderer implements Renderer{
        Cube cube;
        @Override
        public void onSurfaceCreated(GL10 gl, EGLConfig config) {
             cube = new Cube();
             cube.onSurfaceCreated();
        }

        @Override
        public void onSurfaceChanged(GL10 gl, int width, int height) {
            cube.onSurfaceChanged( width,height );
        }

        @Override
        public void onDrawFrame(GL10 gl) {
            cube.draw();
        }
    }

    class MatrixCubeRenderer implements Renderer{
        MatrixCube matrixCube;
        @Override
        public void onDrawFrame(GL10 gl) {
            matrixCube.draw();
        }

        @Override
        public void onSurfaceChanged(GL10 gl, int width, int height) {
            matrixCube.onSurfaceChanged( width,height );
        }

        @Override
        public void onSurfaceCreated(GL10 gl, EGLConfig config) {
            matrixCube = new MatrixCube();
            matrixCube.onSurfaceCreated();
        }
    }

}

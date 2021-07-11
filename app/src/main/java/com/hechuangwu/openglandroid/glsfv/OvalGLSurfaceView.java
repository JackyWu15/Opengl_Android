package com.hechuangwu.openglandroid.glsfv;

import android.content.Context;

import com.he.chuangwu.libpraticles.base.BaseGLSurfaceView;
import com.hechuangwu.openglandroid.shape.oval.Ball;
import com.hechuangwu.openglandroid.shape.oval.BallWithLight;
import com.hechuangwu.openglandroid.shape.oval.Cone;
import com.hechuangwu.openglandroid.shape.oval.Cylinder;
import com.hechuangwu.openglandroid.shape.oval.Oval;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

/**
 * Created by cwh on 2019/6/24 0024.
 * 功能:
 */
public class OvalGLSurfaceView extends BaseGLSurfaceView {
    public OvalGLSurfaceView(Context context) {
        super( context );
        //        setRenderer( new OvalRenderer() );//圆
//                setRenderer( new BallRenderer() );//球体
//                setRenderer( new BallWithLightRenderer() );//带光球体
//                setRenderer( new ConeRenderer() );//圆锥体
                setRenderer( new CylinderRenderer() );//圆柱体
        setRenderMode( RENDERMODE_WHEN_DIRTY );//只有在创建和调用requestRender()时才会刷新。
    }

    class OvalRenderer implements Renderer {
        Oval oval;

        @Override
        public void onDrawFrame(GL10 gl) {
            oval.draw();
        }

        @Override
        public void onSurfaceChanged(GL10 gl, int width, int height) {
            oval.onSurfaceChanged( width, height );
        }

        @Override
        public void onSurfaceCreated(GL10 gl, EGLConfig config) {
            oval = new Oval();
        }
    }

    class BallRenderer implements Renderer {
        Ball ball;

        @Override
        public void onDrawFrame(GL10 gl) {
            
            ball.draw();
        }

        @Override
        public void onSurfaceChanged(GL10 gl, int width, int height) {
            ball.onSurfaceChanged( width, height );
        }

        @Override
        public void onSurfaceCreated(GL10 gl, EGLConfig config) {
            ball = new Ball();
            ball.onSurfaceCreated();
        }
    }


    class BallWithLightRenderer implements Renderer {
        BallWithLight ballWithLight;
        @Override
        public void onDrawFrame(GL10 gl) {
            ballWithLight.draw();
        }

        @Override
        public void onSurfaceChanged(GL10 gl, int width, int height) {
            ballWithLight.onSurfaceChanged( width,height );
            ballWithLight.onSurfaceCreated();
        }

        @Override
        public void onSurfaceCreated(GL10 gl, EGLConfig config) {
           ballWithLight = new BallWithLight();
        }
    }

    class ConeRenderer implements Renderer{
        Cone cone;
        @Override
        public void onDrawFrame(GL10 gl) {
            cone.draw();
        }

        @Override
        public void onSurfaceChanged(GL10 gl, int width, int height) {
            cone.onSurfaceChanged( width,height );
        }

        @Override
        public void onSurfaceCreated(GL10 gl, EGLConfig config) {
            cone = new Cone();
            cone.onSurfaceCreated();
        }
    }

    class CylinderRenderer implements Renderer {
        Cylinder cylinder;

        @Override
        public void onDrawFrame(GL10 gl) {
            cylinder.draw();
        }

        @Override
        public void onSurfaceChanged(GL10 gl, int width, int height) {
            cylinder.onSurfaceChanged( width, height );
        }

        @Override
        public void onSurfaceCreated(GL10 gl, EGLConfig config) {
            cylinder = new Cylinder();
            cylinder.onSurfaceCreated();
        }
    }

}

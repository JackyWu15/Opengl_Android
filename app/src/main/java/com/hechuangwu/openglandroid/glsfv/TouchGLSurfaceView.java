package com.hechuangwu.openglandroid.glsfv;

import android.content.Context;
import android.view.MotionEvent;

import com.he.chuangwu.libpraticles.base.BaseGLSurfaceView;
import com.hechuangwu.openglandroid.shape.touch.Point;
import com.hechuangwu.openglandroid.shape.touch.RotateTriangle;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

/**
 * Created by cwh on 2019/7/1 0001.
 * 功能:
 */
public class TouchGLSurfaceView extends BaseGLSurfaceView {
    Point mPoint;
    RotateTriangle mRotateTriangle;
    private final float TOUCH_SCALE_FACTOR = 180.0f / 360;

    public TouchGLSurfaceView(Context context) {
        super( context );
                setRenderer( new PointRenderer() );//画点
//        setRenderer( new RotateTriangleRenderer() );//旋转三角形
        setRenderMode( RENDERMODE_WHEN_DIRTY );
    }

    float lastX = 0;
    float lastY = 0;
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        float x = event.getX();
        float y = event.getY();

        switch (event.getAction()) {
            case MotionEvent.ACTION_MOVE:
                if (mPoint != null) {
                    mPoint.setPosition( new float[]{x / mPoint.getViewPort()[0] * 2 - 1.0f, 1 - y / mPoint.getViewPort()[1] * 2} );
                    mPoint.setColor( new float[]{0.0f, 1.0f, 0.0f, 1.0f} );
                    requestRender();
                } else if (mRotateTriangle != null) {
                    float dx = x - lastX;
                    float dy = y - lastY;
                    if (y > mRotateTriangle.getViewPort()[1] / 2) {
                        dx = -dx;
                    }

                    if (x < mRotateTriangle.getViewPort()[0] / 2) {
                        dy = -dy;
                    }

                    mRotateTriangle.setAngle( mRotateTriangle.getAngle() + (dx + dy) * TOUCH_SCALE_FACTOR );
                    requestRender();

                }
                break;
        }
        lastX = x;
        lastY = y;
        return true;
    }

    class PointRenderer implements Renderer {

        @Override
        public void onDrawFrame(GL10 gl) {
            mPoint.draw();
        }

        @Override
        public void onSurfaceChanged(GL10 gl, int width, int height) {
            mPoint.onSurfaceChanged( width, height );

        }

        @Override
        public void onSurfaceCreated(GL10 gl, EGLConfig config) {
            mPoint = new Point();
            mPoint.onSurfaceCreated();
        }
    }

    class RotateTriangleRenderer implements Renderer {

        @Override
        public void onDrawFrame(GL10 gl) {
            mRotateTriangle.draw();
        }

        @Override
        public void onSurfaceChanged(GL10 gl, int width, int height) {
            mRotateTriangle.onSurfaceChanged( width, height );
        }

        @Override
        public void onSurfaceCreated(GL10 gl, EGLConfig config) {
            mRotateTriangle = new RotateTriangle();
            mRotateTriangle.onSurfaceCreated();
        }
    }
}

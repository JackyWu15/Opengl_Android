package com.hechuangwu.openglandroid.glsfv;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.hechuangwu.openglandroid.base.BaseGLSurfaceView;
import com.hechuangwu.openglandroid.shape.image.Image;

import java.io.IOException;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

/**
 * Created by cwh on 2019/7/1 0001.
 * 功能:
 */
public class ImageGLSurfaceView extends BaseGLSurfaceView {

    private final Bitmap mBitmap;

    public ImageGLSurfaceView(Context context) throws IOException {
        super( context );
        mBitmap = BitmapFactory.decodeStream( context.getResources().getAssets().open( "texture/Einstein.jpg" ) );
        setRenderer( new ImageRenderer() );
        setRenderMode( RENDERMODE_WHEN_DIRTY );
    }



    class ImageRenderer implements Renderer {
        Image image;
        @Override
        public void onDrawFrame(GL10 gl) {
            image.draw();
        }

        @Override
        public void onSurfaceChanged(GL10 gl, int width, int height) {
            image.onSurfaceChanged( width,height );
        }

        @Override
        public void onSurfaceCreated(GL10 gl, EGLConfig config) {
            image  = new Image();
            image.onSurfaceCreated();
            image.setBitmap( mBitmap );
        }
    }


}

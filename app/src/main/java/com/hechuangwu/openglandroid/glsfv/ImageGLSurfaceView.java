package com.hechuangwu.openglandroid.glsfv;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.hechuangwu.openglandroid.base.BaseGLSurfaceView;
import com.hechuangwu.openglandroid.shape.image.Image;
import com.hechuangwu.openglandroid.shape.image.ImageTransform;

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
        mBitmap = BitmapFactory.decodeStream( context.getResources().getAssets().open( "texture/Einstein1.jpg" ) );
//        setRenderer( new ImageRenderer() );//纹理贴图
        setRenderer( new ImageTransformRenderer( ImageTransform.ImageFilter.MAGN) );//图片滤镜特效
        setRenderMode( RENDERMODE_WHEN_DIRTY );
    }

    class ImageTransformRenderer implements Renderer{
        ImageTransform.ImageFilter mImageFilter;
        ImageTransform imageTransform;
        ImageTransformRenderer(ImageTransform.ImageFilter imageFilter){
            this.mImageFilter = imageFilter;
        }
        @Override
        public void onDrawFrame(GL10 gl) {
            imageTransform.draw();
        }

        @Override
        public void onSurfaceChanged(GL10 gl, int width, int height) {
            imageTransform.onSurfaceChanged( width,height );
        }

        @Override
        public void onSurfaceCreated(GL10 gl, EGLConfig config) {
            imageTransform  = new ImageTransform( mImageFilter );
            imageTransform.onSurfaceCreated();
            imageTransform.setBitmap( mBitmap );
        }
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

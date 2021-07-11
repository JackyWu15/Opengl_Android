package com.he.chuangwu.libpraticles.base;

import android.content.Context;
import android.opengl.GLSurfaceView;
import android.util.AttributeSet;

/**
 * Created by cwh on 2019/6/4 0004.
 * 功能:
 */
public class BaseGLSurfaceView extends GLSurfaceView {
    public BaseGLSurfaceView(Context context) {
        this( context,null );
    }

    public BaseGLSurfaceView(Context context, AttributeSet attrs) {
        super( context, attrs );
        initEGLContext();
    }

    void initEGLContext() {
        setEGLContextClientVersion( 2 );
    }
}

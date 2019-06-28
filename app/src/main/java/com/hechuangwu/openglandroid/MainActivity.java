package com.hechuangwu.openglandroid;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.hechuangwu.openglandroid.glsfv.OvalGLSurfaceView;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
//        setContentView( new TriangleGLSurfaceView( this ) );//三角形
//        setContentView( new SquareGLSurfaceView( this ) );//正方形
        setContentView( new OvalGLSurfaceView( this ) );//圆形

    }
}

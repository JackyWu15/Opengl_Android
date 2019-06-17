package com.hechuangwu.openglandroid;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.hechuangwu.openglandroid.glsfv.TriangleGLSurfaceView;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( new TriangleGLSurfaceView( this ) );
    }
}
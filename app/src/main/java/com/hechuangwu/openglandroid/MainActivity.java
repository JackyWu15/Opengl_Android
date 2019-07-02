package com.hechuangwu.openglandroid;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.hechuangwu.openglandroid.glsfv.ImageGLSurfaceView;

import java.io.IOException;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        //        setContentView( new TriangleGLSurfaceView( this ) );//三角形
        //        setContentView( new SquareGLSurfaceView( this ) );//正方形
        //        setContentView( new OvalGLSurfaceView( this ) );//圆形
//        setContentView( new TouchGLSurfaceView( this ) );//根据触摸位置画点
        try {
            setContentView( new ImageGLSurfaceView( this ) );//纹理贴图和滤镜处理
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

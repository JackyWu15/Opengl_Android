package com.hechuangwu.openglandroid;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.hechuangwu.openglandroid.glsfv.ImageGLSurfaceView;
import com.hechuangwu.openglandroid.glsfv.TriangleGLSurfaceView;

import java.io.IOException;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );

        setContentView(R.layout.activity_main);

//        findViewById(R.id.particles).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                startActivity(new Intent(MainActivity.this, ParticlesActivity.class));
//            }
//        });
//        pratice();
    }



    private void pratice() {
                setContentView( new TriangleGLSurfaceView( this ) );//三角形
//                setContentView( new SquareGLSurfaceView( this ) );//正方形
//        setContentView( new OvalGLSurfaceView( this ) );//圆形
//        setContentView( new TouchGLSurfaceView( this ) );//根据触摸位置画点
        try {
            setContentView( new ImageGLSurfaceView( this ) );//纹理贴图和滤镜处理
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

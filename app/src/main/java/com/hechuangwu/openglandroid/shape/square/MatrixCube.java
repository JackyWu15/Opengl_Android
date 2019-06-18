package com.hechuangwu.openglandroid.shape.square;

import android.opengl.Matrix;

/**
 * Created by cwh on 2019/6/18 0018.
 * 功能:
 */
public class MatrixCube extends Cube {
    //单位矩阵
    protected float[] mModel = {
            1.0f, 0.0f, 0.0f, 0.0f,
            0.0f, 1.0f, 0.0f, 0.0f,
            0.0f, 0.0f, 1.0f, 0.0f,
            0.0f, 0.0f, 0.0f, 1.0f
    };

    @Override
    public void onSurfaceChanged(int width, int height) {
        //注意：矩阵乘法不遵循交换律，所以建议以平移-》旋转-》缩放顺序进行变换，否则结果会产生错误
        //模型矩阵变换->向左上平移
        Matrix.translateM( mModel,0,-2.0f,5.0f,0.0f );
        //沿z轴指向旋转，正为顺时针，负为逆时针
        Matrix.rotateM( mModel,0,-30f,0.0f,0.0f,1.0f );
        //缩小一半
        Matrix.scaleM( mModel,0,0.5f,0.5f,0.5f );

        //视图矩阵
        Matrix.setLookAtM( mView,0,10.0f,10.0f,20.0f,0.0f,0.0f,0.0f,0.0f,1.0f,0.0f );
        //透视矩阵
        float ratio  = (float) width / height;
        Matrix.frustumM( mProjection,0,-ratio,ratio,-1.0f,1.0f,3.0f,100.f );

        //运算矩阵
        Matrix.multiplyMM( mMVPMatrix,0,mView,0,mModel,0 );
        Matrix.multiplyMM( mMVPMatrix,0,mProjection,0,mMVPMatrix,0 );

    }
}

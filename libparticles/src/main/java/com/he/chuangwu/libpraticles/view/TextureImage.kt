package com.he.chuangwu.libpraticles.view

import android.graphics.Bitmap
import android.opengl.GLES20.*
import android.opengl.Matrix
import android.util.Log
import com.he.chuangwu.libpraticles.base.BaseGLSL

class TextureImage(private val bitmap: Bitmap) : BaseGLSL() {


    override fun onSurfaceCreated() {
        super.onSurfaceCreated()
        glClearColor(1.0f, 1.0f, 1.0f, 1.0f)
        glEnable(GL_TEXTURE_2D)

    }

    override fun onSurfaceChanged(width: Int, height: Int) {
        super.onSurfaceChanged(width, height)
        glViewport(0, 0, width, height)
//        Matrix.setLookAtM(mView, )
        val bitmapWidth = bitmap.width
        val bitmapHeight = bitmap.height
        val bitmapRatio = (bitmapWidth / bitmapHeight).toFloat()
        val screenRatio = (width / height).toFloat()
//        // 横屏正交投影
//        if(width > height) {
//            // 如果图片更大，则矩阵距离扩大
//            if(bitmapRatio > screenRatio) {
//                //m: 至少16元素数组，用于存放正交投影矩阵；mOffset：结果矩阵偏移值；left：x轴的最小范围；right：x轴的最大范围
//                //bottom：y轴的最小范围；top：y轴的最大范围；near：z轴的最小方位；far：z轴的最大范围
//                Matrix.orthoM(mProjection, 0, -screenRatio * bitmapRatio,screenRatio * bitmapRatio, -1.0f,1.0f,-1.0f,1.0f )

//            } else {
//                Matrix.orthoM(mProjection, 0, -screenRatio / bitmapRatio,screenRatio / bitmapRatio, -1.0f,1.0f,-1.0f,1.0f )
//            }
//        } else {
//            if (bitmapRatio > screenRatio) {
//                Matrix.orthoM(mProjection, 0, -1.0f,1.0f,-bitmapRatio / screenRatio,bitmapRatio / screenRatio, -1.0f,1.0f )
//            } else {
//                Matrix.orthoM(mProjection, 0, -1.0f,1.0f,-screenRatio / bitmapRatio,screenRatio / bitmapRatio, -1.0f,1.0f )
//            }
//        }
    }

    override fun allocateBuffer() {

    }

    override fun initProgram() {
        TODO("Not yet implemented")
    }

    override fun draw() {
        TODO("Not yet implemented")
    }


}
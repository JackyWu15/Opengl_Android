package com.he.chuangwu.libpraticles.view

import android.content.Context
import android.opengl.GLES20
import android.opengl.GLSurfaceView
import javax.microedition.khronos.egl.EGLConfig
import javax.microedition.khronos.opengles.GL10
import android.opengl.GLES20.*
import com.he.chuangwu.libpraticles.R
import com.he.chuangwu.libpraticles.utils.TextResourceReader

/**
 *  GLSurfaceView 会在一个单独的后台线程中调用渲染器的方法，因此，要确保只能在
 *  这个渲染线程中调用OpenGL。在UI线程中的GLSurfaceView实例，可以使用queueEvent()
 *  方法，传递一个Runnable给后台渲染线程，渲染线程则可以调用runOnUIThread来传递给主线程
 */
class ParticlesRenderer(private val context: Context) : GLSurfaceView.Renderer {

    private var mParticlesGLSL: ParticlesGLSL? = null

    // 当Surface第一次被创建时，回调这个方法，设备被唤醒，或从其他Activity跳转过来时
    // 也可能会被调用。GL10属于1.0遗留下来的，如果使用1.0则需要使用到，使用2.0不需要
    // 使用到，而2.0已经全部封装为静态方法，通过调用GLES20.xxx调用
    override fun onSurfaceCreated(gl: GL10?, config: EGLConfig?) {
        // 用于清空屏幕时填充的颜色
        glClearColor(0.0f, 0.0f, 0.0f, 1.0f)
        mParticlesGLSL = ParticlesGLSL(context)

    }

    // 窗口大小改变时回调，横竖屏切换时调用
    override fun onSurfaceChanged(gl: GL10?, width: Int, height: Int) {
        // 告诉OpenGL可以用来渲染的surface大小
        glViewport(0, 0, width, height)
    }

    // 绘制一帧时被调用，当此调用结束后，渲染缓存区会交换并显示在屏幕上
    override fun onDrawFrame(gl: GL10?) {
        // 擦除了屏幕颜色，并用上面onSurfaceCreated里设置的颜色填充屏幕
        glClear(GL_COLOR_BUFFER_BIT)
        mParticlesGLSL?.draw()
    }
}
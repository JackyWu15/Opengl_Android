package com.he.chuangwu.libpraticles.view

import android.content.Context
import com.he.chuangwu.libpraticles.base.BaseGLSurfaceView

class ParticlesGLSurfaceView(context: Context) : BaseGLSurfaceView(context) {

    init {
        setRenderer(ParticlesRenderer(context))
    }
}
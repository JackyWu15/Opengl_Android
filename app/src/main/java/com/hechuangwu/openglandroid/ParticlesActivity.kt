package com.hechuangwu.openglandroid

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.he.chuangwu.libpraticles.view.ParticlesGLSurfaceView

class ParticlesActivity :AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(ParticlesGLSurfaceView(this))
    }
}
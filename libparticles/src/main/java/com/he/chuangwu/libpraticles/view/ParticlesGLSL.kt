package com.he.chuangwu.libpraticles.view

import android.content.Context
import android.opengl.GLES20.*
import com.he.chuangwu.libpraticles.R
import com.he.chuangwu.libpraticles.base.BaseGLSL
import com.he.chuangwu.libpraticles.utils.TextResourceReader
import java.nio.ByteBuffer
import java.nio.ByteOrder
import java.nio.FloatBuffer

/**
 * OpenGL运行于本地环境，Java运行于本地环境之上的虚拟环境，虚拟环境有内存回收机制，本地环境则没有
 * 有两种方式将数据传给本地环境运行：
 *   一，通过JNI调用(如，GLES20.xxx静态方式实际已封装了JNI调用);
 *   二，使用Java的特殊集合ByteBuffer，分配本地内存块，将数据复制到本地内存，
 *       本地内存则可以被本地环境使用，不受垃圾回收器控制
 */
class ParticlesGLSL(private val context: Context) : BaseGLSL() {
    // 使用逆时针(卷曲顺序),可以指出一个三角形属于任何给定物体的前面或者后面，
    // OpenGL 可以忽略后面看不到的三角形，可以优化性能

    // 注意使用单精度浮点数
    val mParticlesVertex = floatArrayOf(
            // 第一個三角形
            -0.5f, -0.5f, 0.0f,
            0.5f, 0.5f, 0.0f,
            -0.5f, 0.5f, 0.0f,
            // 第二個三角形
            -0.5f,-0.5f, 0.0f,
            0.5f, -0.5f, 0.0f,
            0.5f, 0.5f, 0.0f,

            // 中間綫
            -0.5f, 0.0f, 0.0f,
            0.5f, 0.0f, 0.0f

    )

    var mProgram: Int = 0
    var mVerTexData: FloatBuffer? = null
    var mUColorLocation: Int = 0


    init {
        allocateBuffer()
        initProgram()
    }

    override fun allocateBuffer() {
        mVerTexData = ByteBuffer.allocateDirect(mParticlesVertex.size * FLOAT_BYTE_SIZE)
                .order(ByteOrder.nativeOrder())
                .asFloatBuffer()
                .put(mParticlesVertex).apply {
                    // 保證從起始位置讀取,如果不設置可能會從末尾讀
                    position(0)
                }
    }

    override fun initProgram() {
        val vertexShaderSource = TextResourceReader.readTextFileFromResource(context, R.raw.particles_vertex_shader)
        val fragmentShaderSource = TextResourceReader.readTextFileFromResource(context, R.raw.particles_fragment_shader)
        val createProgram = createProgram(vertexShaderSource, fragmentShaderSource)
        // 在繪製任何圖像到屏幕上時都必須使用這個程序
        glUseProgram(createProgram)
        val aPositionLocation = glGetAttribLocation(createProgram, "aPosition")
        // index : 指向著色器裏的屬性變量
        // size: 從數組中以多少個數據為單位去讀取,即幾個分量為一個屬性的數據,
        //       著色器裏的變量為4個分量,如果指定的個數少於分量的個數,
        //       則填充時前3個中沒有指定的默認為0,最後一個w為1
        // type: 數據類型
        // normalized: 只有使用整型時才有意義
        // stride: 當有多個屬性時,用於指定屬性的步長,以字節為單位
        // ptr: 緩衝區數據
        glVertexAttribPointer(aPositionLocation, COORDS_COMPONENT, GL_FLOAT, false, 0, mVerTexData)
        // 上面只是指定讀取方式,頂點著色器默認關閉,需啓動
        glEnableVertexAttribArray(aPositionLocation)

        mUColorLocation = glGetUniformLocation(createProgram, "aColor")

    }

    override fun draw() {
        // 畫三角形
        // uniform 分量沒有默認值,必須全部指定
        glUniform4f(mUColorLocation, 1.0f, 1.0f , 1.0f, 1.0f)
        // mode: 畫三角形
        // first: 起始位置
        // 讀取6個頂點數據
        glDrawArrays(GL_TRIANGLES, 0, 6)
        // 有幾個基本圖元就執行幾次片段著色其,上面2次,劃綫1次,總共3次
        glUniform4f(mUColorLocation, 1.0f, 0.0f, 0.0f ,1.0f)
        glDrawArrays(GL_LINEAR, 6, 2)
    }
}
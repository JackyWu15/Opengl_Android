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
 *
 * w分量：gl_Position -> 透视法 -> 归一化坐标 ->视口变换 -> 窗口坐标.
 * 在将顶点输入的坐标转为归一化坐标之前，会经过透视法，透视法阶段会将x,y,z除以w分量，w分量越大，距离屏幕越远（即越靠近集中点）
 * 通过这种方式达到透视效果
 */
class ParticlesGLSL(private val context: Context) : BaseGLSL() {
    // 使用逆时针(卷曲顺序),可以指出一个三角形属于任何给定物体的前面或者后面，
    // OpenGL 可以忽略后面看不到的三角形，可以优化性能

    // 注意使用单精度浮点数
//    val mParticlesVertex = floatArrayOf(
//            // 第一個三角形
//            -0.5f, -0.5f, 0.0f,
//            0.5f, 0.5f, 0.0f,
//            -0.5f, 0.5f, 0.0f,
//            // 第二個三角形
//            -0.5f,-0.5f, 0.0f,
//            0.5f, -0.5f, 0.0f,
//            0.5f, 0.5f, 0.0f,
//
//            // 中間綫
//            -0.5f, 0.0f, 0.0f,
//            0.5f, 0.0f, 0.0f
//    )

    val mParticlesVertex = floatArrayOf(
        0.0f,0.0f,0.0f,      1.0f,1.0f,1.0f,
        -0.5f,-0.5f,0.0f,    0.7f,0.7f,0.7f,
        0.5f, -0.5f,0.0f,    0.7f,0.7f,0.7f,
        0.5f, -0.5f,0.0f,    0.7f,0.7f,0.7f,
        -0.5f, 0.5f,0.0f,    0.7f,0.7f,0.7f,
        -0.5f,-0.5f,0.0f,    0.7f,0.7f,0.7f
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
                    // 指定读取属性的起始位置，如果有多个属性，可以设置为其他，
                    // 初始位置则保證從0讀取,如果不設置可能會從末尾讀
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
        //       則填充時前3個中沒有指定的默認為0,最後一個w分量為1
        // type: 數據類型
        // normalized: 只有使用整型時才有意義
        // stride: 如果顶点附带其他属性，如颜色，在读不同顶点位置时，需告诉opengl跳过多少的步长去读取下一个位置数据
        // ptr: 緩衝區數據
        val stride = COORDS_COMPONENT * 2;
        glVertexAttribPointer(aPositionLocation, COORDS_COMPONENT, GL_FLOAT, false, stride, mVerTexData)
        // 上面只是指定讀取方式,頂點著色器默認關閉,需啓動
        glEnableVertexAttribArray(aPositionLocation)

//        mUColorLocation = glGetUniformLocation(createProgram, "aColor")

        val aColorLocation = glGetAttribLocation(createProgram, "aColor");
        mVerTexData?.position(COORDS_COMPONENT)
        glVertexAttribPointer(aColorLocation, COORDS_COMPONENT, GL_FLOAT, false, stride, mVerTexData)
        glEnableVertexAttribArray(aColorLocation)

    }

    override fun draw() {
        // uniform 分量沒有默認值,必須全部指定，attribute则有默认
//        glUniform4f(mUColorLocation, 1.0f, 1.0f , 1.0f, 1.0f)
        // first: 起始位置
        // 讀取6個頂點數據
        glDrawArrays(GL_TRIANGLE_FAN, 0, 12) // TODO: 2021/7/12 GL_TRIANGLE_FAN 绘制无效 

        // 有幾個基本圖元就執行幾次片段著色器,上面2次,劃綫1次,總共3次
//        glUniform4f(mUColorLocation, 1.0f, 0.0f, 0.0f ,1.0f)
//        glDrawArrays(GL_LINEAR, 6, 2)
    }
}
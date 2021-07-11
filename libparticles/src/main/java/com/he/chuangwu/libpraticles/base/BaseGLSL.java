package com.he.chuangwu.libpraticles.base;

import android.opengl.GLES20;
import android.util.Log;

import java.nio.FloatBuffer;

import static android.opengl.GLES20.*;

/**
 * Created by cwh on 2019/6/4 0004.
 * 功能:
 */
public abstract class BaseGLSL {
    private static final String TAG = "BaseGLSL";
    //数据类型占用字节数
    protected final int FLOAT_BYTE_SIZE = Float.SIZE / Byte.SIZE;
    protected final int SHORT_BYTE_SIZE = Short.SIZE / Byte.SIZE;

    //模型矩阵
    protected float[] mModel = new float[16];
    //视图矩阵
    protected float[] mView = new float[16];
    //透视矩阵
    protected float[] mProjection = new float[16];
    //运算后的矩阵
    protected float[] mMVPMatrix = new float[16];
    //运行程序
    protected int mProgram;
    //顶点数组
    protected FloatBuffer mVertexBuffer;
    //坐标分量个数
    protected int COORDS_COMPONENT = 3;
    //顶点个数
    protected int mCoordsSize;

    /**
     * 创建着色器，编译代码
     *
     * @param type       着色器类型
     * @param shaderCode 着色器代码
     * @return
     */
    private static int loadShader(int type, String shaderCode) {
        int shader = glCreateShader(type);
        glShaderSource(shader, shaderCode);
        glCompileShader(shader);
        return shader;
    }

    /**
     * 驗證當前程序是否低效率的,獲取低效日志信息
     *
     * @param programObjectId 程序id
     * @return 是否有效
     */
    private static boolean validateProgram(int programObjectId) {
        glValidateProgram(programObjectId);
        final int[] validateStatus = new int[1];
        glGetProgramiv(programObjectId, GL_VALIDATE_STATUS, validateStatus, 0);
        Log.e(TAG, "result of validating program : " + validateStatus[0] + ", log:" +
                glGetProgramInfoLog(programObjectId));
        return validateStatus[0] != 0;
    }

    /**
     * 创建运行程序，关联着色器，并将顶点着色器和片元着色器进行连接
     *
     * @param vertexSource   顶点着色器代码
     * @param fragmentSource 片元着色器代码
     * @return 程序id
     */
    public static int createProgram(String vertexSource, String fragmentSource) {
        int vertexShader = loadShader(GLES20.GL_VERTEX_SHADER, vertexSource);
        if (vertexShader == GL_FALSE) {
            Log.e(TAG, "loadShader vertex failed" + glGetShaderInfoLog(vertexShader));
            return 0;
        }
        int fragmentShader = loadShader(GLES20.GL_FRAGMENT_SHADER, fragmentSource);
        if (fragmentShader == GL_FALSE) {
            Log.e(TAG, "loadShader fragment failed" + glGetShaderInfoLog(fragmentShader));
            return 0;
        }

        int program = GLES20.glCreateProgram();
        if (program != GL_FALSE) {
            GLES20.glAttachShader(program, vertexShader);
            GLES20.glAttachShader(program, fragmentShader);
            GLES20.glLinkProgram(program);

            int[] linkStatus = new int[1];
            glGetProgramiv(program, GLES20.GL_LINK_STATUS, linkStatus, 0);
            if (linkStatus[0] != GLES20.GL_TRUE) {
                Log.e(TAG, "Could not link program:" + glGetProgramInfoLog(program));
                GLES20.glDeleteProgram(program);
                validateProgram(program);
                program = GL_FALSE;
            }
        }
        GLES20.glDeleteShader(vertexShader);
        GLES20.glDeleteShader(fragmentShader);
        return program;
    }


    protected abstract void allocateBuffer();//显存分配

    protected abstract void initProgram();//编译着色器生成程序

    public abstract void draw();//绘制

    public void onSurfaceCreated() {
    }//用于初始化某些状态

    public void onSurfaceChanged(int width, int height) {
    }//矩阵运算
}

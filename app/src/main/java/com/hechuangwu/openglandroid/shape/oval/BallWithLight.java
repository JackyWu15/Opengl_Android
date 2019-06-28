package com.hechuangwu.openglandroid.shape.oval;

import android.opengl.GLES20;
import android.opengl.Matrix;

import com.hechuangwu.openglandroid.base.BaseGLSL;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.ArrayList;

/**
 * Created by cwh on 2019/6/27 0027.
 * 功能:
 */
public class BallWithLight extends BaseGLSL {
////模型变换后的位置
//    vec3 fragPos=(uMatrix*aPosition).xyz;
//    //光照方向
//    vec3 lightDirection=normalize(uLightPosition-fragPos);
//    //模型变换后的法线向量
//    vec3 normal=normalize(mat3(uMatrix)*aNormal);
//    //观察方向，这里将观察点固定在（0，0，uLightPosition.z）处
//    vec3 viewDirection=normalize(vec3(0,0,uLightPosition.z)-fragPos);
//    //观察向量与光照向量的半向量
//    vec3 hafVector=normalize(lightDirection+viewDirection);
//    //max(0,cos(半向量与法向量的夹角)^粗糙度
//    float diff=pow(max(dot(normal,hafVector),0.0),4.0);
//    //材质的镜面反射系数*max(0,cos(反射向量与观察向量夹角)^粗糙度*光照颜色
//    //材质的镜面反射系数*max(0,cos(半向量与法向量的夹角)^粗糙度*光照颜色
//    vec3 specular=uSpecularStrength*diff*uLightColor;
//    return vec4(specular,1.0);
    protected String vertexShaderCode =
            "attribute vec3 aPosition;\n" +//顶点数据
                    "uniform mat4 aMatrix;\n" +//矩阵
                    "uniform vec3 aLightLocation;\n" +//光源位置
                    "uniform vec4 aLightColor;\n" +//光源颜色
                    "uniform vec3 aCameraLocation;\n"+//相机位置
                    "varying vec4 aDiffuse;\n" +//漫反射系数
                      "//获取漫反射系数\n" +
                        "vec4 pointLight(vec3 normalPosition ,vec3 lightLocation,vec4 lightColor ){\n" +
                            "vec3 normal = normalize((aMatrix * vec4(normalPosition + aPosition,1)).xyz-(aMatrix * vec4(aPosition,1)).xyz);\n" +//根据顶点数据，求法向量
                            "vec3 lightDir = normalize(lightLocation - (aMatrix * vec4(aPosition,1)).xyz);\n" +//求光源照射方向反方向
                            "vec4 diffuse = lightColor * max(dot(normal,lightDir),0.0);\n" +//光源和法向量进行点乘，得到漫反射系数

                    // TODO: 2019/6/28 0028 镜面光照无效，待解决
//                            "float specularStrength = 0.5;\n" +
//                            "vec3 viewDir = normalize(aCameraLocation - (aMatrix * vec4(aPosition,1)).xyz);\n" +
//                    "vec3 hafVector=normalize(lightDir+viewDir);\n" +
//                    " float diff=pow(max(dot(normal,hafVector),0.0),4.0);\n" +//和视觉方向相反的方向向量
////                            "vec3 reflectDir = reflect(-lightDir,normal);\n" +//光反射方向
////                            "float spec = pow(max(dot(viewDir, reflectDir), 0.0), 32.0);\n" +//光反射方向和视觉相反方向的夹角影响
//                            "vec4 specular = specularStrength * diff * lightColor;\n" +//镜面光源系数
//                            "vec4 coefficient = specular + diffuse\n" +
                            "return diffuse ;\n" +
                        "}\n" +
                    "void main(){\n" +
                        "gl_Position = aMatrix * vec4(aPosition,1.0);\n" +
                        "aDiffuse = pointLight(normalize(aPosition),aLightLocation,aLightColor);\n" +
                    "}";

    protected String fragmentShaderCode =
            "precision mediump float;\n" +
                    "varying vec4 aDiffuse;\n" +
                    "uniform vec4 aObjectColor;\n" +
                    "void main(){\n" +
                    "gl_FragColor = aDiffuse * aObjectColor;\n" +
                    "}";

    protected float[] lightLocation = {80.0f, 80.0f, 80.0f};//光源位置
    protected float[] cameraLocation = {0.0f, 0.0f, 10.0f};//相机颜色
    protected float[] lightColor = {1.0f, 1.0f, 1.0f, 1.0f};//光源颜色
    protected float[] objectColor = {1f, 0.5f, 0.3f, 1.0f};//物体颜色


    private float step = 2f;

    private float[] mViewMatrix = new float[16];
    private float[] mProjectMatrix = new float[16];
    private float[] mMVPMatrix = new float[16];
    protected int mCoordsSize;

    public BallWithLight() {
        allocateBuffer();
        initProgram();
    }

    protected float[] createBallPos() {
        //球以(0,0,0)为中心，以R为半径，则球上任意一点的坐标为
        // ( R * cos(a) * sin(b),y0 = R * sin(a),R * cos(a) * cos(b))
        // 其中，a为圆心到点的线段与xz平面的夹角，b为圆心到点的线段在xz平面的投影与z轴的夹角
        ArrayList<Float> data = new ArrayList<>();
        float r1, r2;
        float h1, h2;
        float sin, cos;
        for (float i = -90; i < 90 + step; i += step) {
            r1 = (float) Math.cos( i * Math.PI / 180.0 );
            r2 = (float) Math.cos( (i + step) * Math.PI / 180.0 );
            h1 = (float) Math.sin( i * Math.PI / 180.0 );
            h2 = (float) Math.sin( (i + step) * Math.PI / 180.0 );
            // 固定纬度, 360 度旋转遍历一条纬线
            float step2 = step * 2;
            for (float j = 0.0f; j < 360.0f + step; j += step2) {
                cos = (float) Math.cos( j * Math.PI / 180.0 );
                sin = -(float) Math.sin( j * Math.PI / 180.0 );

                data.add( r2 * cos );
                data.add( h2 );
                data.add( r2 * sin );
                data.add( r1 * cos );
                data.add( h1 );
                data.add( r1 * sin );
            }
        }
        float[] f = new float[data.size()];
        for (int i = 0; i < f.length; i++) {
            f[i] = data.get( i );
        }
        return f;
    }

    @Override
    public void onSurfaceCreated() {
        GLES20.glEnable( GLES20.GL_DEPTH_TEST );
    }

    @Override
    public void onSurfaceChanged(int width, int height) {
        //计算宽高比
        float ratio = (float) width / height;
        //设置透视投影
        Matrix.frustumM( mProjectMatrix, 0, -ratio, ratio, -1, 1, 3, 20 );
        //设置相机位置
        Matrix.setLookAtM( mViewMatrix, 0, 0.0f, 0.0f, 10.0f, 0f, 0f, 0f, 0f, 1.0f, 0.0f );
        //计算变换矩阵
        Matrix.multiplyMM( mMVPMatrix, 0, mProjectMatrix, 0, mViewMatrix, 0 );
    }

    @Override
    protected void allocateBuffer() {
        float[] dataPos = createBallPos();
        ByteBuffer buffer = ByteBuffer.allocateDirect( dataPos.length * 4 );
        buffer.order( ByteOrder.nativeOrder() );
        mVertexBuffer = buffer.asFloatBuffer();
        mVertexBuffer.put( dataPos );
        mVertexBuffer.position( 0 );
        mCoordsSize = dataPos.length / 3;
    }

    @Override
    protected void initProgram() {
        mProgram = createProgram( vertexShaderCode, fragmentShaderCode );
    }

    public void draw() {
        GLES20.glClearColor( 1.0f, 1.0f, 0.6f, 1.0f );
        GLES20.glClear( GLES20.GL_COLOR_BUFFER_BIT | GLES20.GL_DEPTH_BUFFER_BIT );
        GLES20.glUseProgram( mProgram );

        int aPosition = GLES20.glGetAttribLocation( mProgram, "aPosition" );
        GLES20.glVertexAttribPointer( aPosition, COORDS_COMPONENT, GLES20.GL_FLOAT, false, COORDS_COMPONENT * FLOAT_BYTE_SIZE, mVertexBuffer );
        GLES20.glEnableVertexAttribArray( aPosition );

        int aMatrix = GLES20.glGetUniformLocation( mProgram, "aMatrix" );
        GLES20.glUniformMatrix4fv( aMatrix, 1, false, mMVPMatrix, 0 );

        int aLightLocation = GLES20.glGetUniformLocation( mProgram, "aLightLocation" );
        GLES20.glUniform3fv( aLightLocation, 1, lightLocation, 0 );
        int aLightColor = GLES20.glGetUniformLocation( mProgram, "aLightColor" );
        GLES20.glUniform4fv( aLightColor, 1, lightColor, 0 );

        int aCameraLocation = GLES20.glGetUniformLocation( mProgram, "aCameraLocation" );
        GLES20.glUniform3fv( aCameraLocation, 1, cameraLocation, 0 );

        int aObjectColor = GLES20.glGetUniformLocation( mProgram, "aObjectColor" );
        GLES20.glUniform4fv( aObjectColor, 1, objectColor, 0 );

        GLES20.glDrawArrays( GLES20.GL_TRIANGLE_FAN, 0, mCoordsSize );
    }
}

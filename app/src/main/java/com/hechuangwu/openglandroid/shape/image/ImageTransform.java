package com.hechuangwu.openglandroid.shape.image;

import android.opengl.GLES20;

/**
 * Created by cwh on 2019/7/2 0002.
 * 功能:
 */
public class ImageTransform extends Image {
    private static String vertexShaderCode =
            "attribute vec4 aPosition;\n" +
                    "attribute vec2 aCoordinate;\n" +
                    "uniform mat4 aMatrix;\n" +
                    "\n" +
                    "varying vec4 Position;\n" +
                    "varying vec2 Coordinate;\n" +
                    "varying vec4 glPosition;\n" +
                    "\n" +
                    "void main(){\n" +
                    "    gl_Position = aMatrix * aPosition;\n" +
                    "    Position = aPosition;\n" +
                    "    Coordinate = aCoordinate;\n" +
                    "    glPosition = aMatrix * aPosition;\n" +
                    "}";

    private static String fragmentShaderCode =
            "precision mediump float;\n" +
                    "uniform sampler2D aTexture;\n" +//纹理
                    "uniform int aChangeType;\n" +//枚举滤镜类型
                    "uniform vec3 aChangeColor;\n" +//枚举颜色
                    "uniform int aIsHalf;\n" +//>0处理一半
                    "uniform float uXY;\n" +//屏幕宽高比
                    "varying vec4 Position;\n" +//顶点坐标
                    "varying vec2 Coordinate;\n" +//纹理坐标
                    "varying vec4 glPosition;\n" +//矩阵变换后坐标
                    "\n" +
                    "void modifyColor(vec4 color){\n" +
                    "    color.r = max(min(color.r,1.0),0.0);\n" +
                    "    color.g = max(min(color.g,1.0),0.0);\n" +
                    "    color.b = max(min(color.b,1.0),0.0);\n" +
                    "    color.a = max(min(color.a,1.0),0.0);\n" +
                    "}\n" +
                    "void main(){\n" +
                    "    vec4 nColor = texture2D(aTexture,Coordinate);\n" +
                    "    if(Position.x > 0.0 || aIsHalf == 0){\n" +
                    "        if(aChangeType==1){\n" +//黑白图片
                    "            float c = nColor.r * aChangeColor.r + nColor.g * aChangeColor.g + nColor.b * aChangeColor.b;\n" +
                    "            gl_FragColor=vec4(c,c,c,nColor.a);\n" +
                    "        }else if(aChangeType==2){\n" +//简单色彩处理，冷暖色调、增加亮度、降低亮度等
                    "            vec4 deltaColor = nColor + vec4(aChangeColor,0.0);\n" +
                    "            modifyColor(deltaColor);\n" +
                    "            gl_FragColor = deltaColor;\n" +
                    "        }else if(aChangeType==3){\n" +//毛玻璃处理
                    "            nColor += texture2D(aTexture,vec2( Coordinate.x - aChangeColor.r, Coordinate.y - aChangeColor.r));\n" +
                    "            nColor += texture2D(aTexture,vec2( Coordinate.x - aChangeColor.r, Coordinate.y + aChangeColor.r));\n" +
                    "            nColor += texture2D(aTexture,vec2( Coordinate.x + aChangeColor.r, Coordinate.y - aChangeColor.r));\n" +
                    "            nColor += texture2D(aTexture,vec2( Coordinate.x + aChangeColor.r, Coordinate.y + aChangeColor.r));\n" +
                    "            nColor += texture2D(aTexture,vec2( Coordinate.x - aChangeColor.g, Coordinate.y - aChangeColor.g));\n" +
                    "            nColor += texture2D(aTexture,vec2( Coordinate.x - aChangeColor.g, Coordinate.y + aChangeColor.g));\n" +
                    "            nColor += texture2D(aTexture,vec2( Coordinate.x + aChangeColor.g, Coordinate.y - aChangeColor.g));\n" +
                    "            nColor += texture2D(aTexture,vec2( Coordinate.x + aChangeColor.g, Coordinate.y + aChangeColor.g));\n" +
                    "            nColor += texture2D(aTexture,vec2( Coordinate.x - aChangeColor.b, Coordinate.y - aChangeColor.b));\n" +
                    "            nColor += texture2D(aTexture,vec2( Coordinate.x - aChangeColor.b, Coordinate.y + aChangeColor.b));\n" +
                    "            nColor += texture2D(aTexture,vec2( Coordinate.x + aChangeColor.b, Coordinate.y - aChangeColor.b));\n" +
                    "            nColor += texture2D(aTexture,vec2( Coordinate.x + aChangeColor.b, Coordinate.y + aChangeColor.b));\n" +
                    "            nColor/=13.0;\n" +
                    "            gl_FragColor=nColor;\n" +
                    "        }else if(aChangeType==4){  \n" +//放大镜效果
                    "            float dis = distance(vec2(glPosition.x,glPosition.y/uXY),vec2(aChangeColor.r,aChangeColor.g));\n" +
                    "            if(dis < aChangeColor.b){\n" +//在半径范围内的就以下面的数据填充
                    "                nColor = texture2D(aTexture,vec2(Coordinate.x/2.0+0.25,Coordinate.y/2.0+0.1));\n" +
                    "            }\n" +
                    "            gl_FragColor=nColor;\n" +
                    "        }else{\n" +
                    "            gl_FragColor=nColor;\n" +
                    "        }\n" +
                    "    }else{\n" +
                    "        gl_FragColor=nColor;\n" +
                    "    }\n" +
                    "}";

    private ImageFilter imageFilter;

    public ImageTransform(ImageFilter imageFilter) {
        this.imageFilter = imageFilter;
        allocateBuffer();
        initProgram();
    }

    @Override
    protected void initProgram() {
        mProgram = createProgram( vertexShaderCode, fragmentShaderCode );
    }


    @Override
    public void draw() {
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT | GLES20.GL_DEPTH_BUFFER_BIT);
        GLES20.glUseProgram(mProgram);
        int aPosition = GLES20.glGetAttribLocation( mProgram, "aPosition" );
        GLES20.glVertexAttribPointer( aPosition,COORDS_COMPONENT,GLES20.GL_FLOAT,false,COORDS_COMPONENT*FLOAT_BYTE_SIZE, mVertexBuffer);
        GLES20.glEnableVertexAttribArray( aPosition );

        int aMatrix = GLES20.glGetUniformLocation( mProgram, "aMatrix" );
        GLES20.glUniformMatrix4fv(aMatrix,1,false,mMVPMatrix,0  );

        int aCoordinate = GLES20.glGetAttribLocation( mProgram, "aCoordinate" );
        GLES20.glVertexAttribPointer( aCoordinate,COORDS_COMPONENT,GLES20.GL_FLOAT,false,COORDS_COMPONENT*FLOAT_BYTE_SIZE, mTextureCoordBuffer);
        GLES20.glEnableVertexAttribArray( aCoordinate );

        //为采样器分配纹理单元，不设置也会自动分配一个
        int aTexture = GLES20.glGetUniformLocation( mProgram, "aTexture" );
        GLES20.glUniform1i(aTexture,0);

        int uXY = GLES20.glGetUniformLocation( mProgram, "uXY" );
        GLES20.glUniform1f( uXY, mScreenRatio );

        int aChangeType = GLES20.glGetUniformLocation( mProgram, "aChangeType" );
        GLES20.glUniform1i( aChangeType, imageFilter.getChangeType() );

        int aChangeColor = GLES20.glGetUniformLocation( mProgram, "aChangeColor" );
        GLES20.glUniform3fv( aChangeColor, 1, imageFilter.getData(), 0 );

        int textureId = createTexture();
        if (textureId != -1) {
            GLES20.glDrawArrays( GLES20.GL_TRIANGLE_STRIP, 0, mCoordsSize );
        }
    }

    public enum ImageFilter {
        NONE( 0, new float[]{0.0f, 0.0f, 0.0f} ), // 不做处理
        GRAY( 1, new float[]{0.299f, 0.587f, 0.114f} ), // 黑白效果
        COOL( 2, new float[]{0.0f, 0.0f, 0.1f} ), // 冷色调
        WARM( 2, new float[]{0.1f, 0.1f, 0.0f} ), // 暖色调
        BLUR( 3, new float[]{0.006f, 0.004f, 0.002f} ), // 模糊（毛玻璃效果）
        MAGN( 4, new float[]{0.1f, 0.3f, 0.4f} );  // 放大镜

        private int changeType;
        private float[] data;

        ImageFilter(int changeType, float[] data) {
            this.changeType = changeType;
            this.data = data;
        }

        public int getChangeType() {
            return changeType;
        }

        public float[] getData() {
            return data;
        }

    }
}

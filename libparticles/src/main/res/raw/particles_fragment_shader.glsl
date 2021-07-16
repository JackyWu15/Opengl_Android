/**
 * 片段着色器用于告诉GPU每个片段最终的颜色是什么，这里的片段最小是一个像素，
 * 但如果屏幕分辩率较大，片段可以有很多个像素组合，以减少GPU工作负荷。
 * 对于基本图元的每个片段，片段着色器都会执行一次，如果有10000个片段，则执行10000次
 */

// 定义所有浮点数的默认精度
// 精度的定义有三种：highp mediump lowp ，只有部分硬件支持highp，
// 顶点着色器也可指定精度，但因为位置需要精确，所以默认使用highp
// 而片元着色器为了兼容性能和质量，则通常使用mediump精度
precision mediump float;
//uniform vec4 aColor;        // uniform 可为每个顶点公用，这里分量对应红绿蓝透明度
varying vec4 vColor;
void main() {
    gl_FragColor = vColor;
}


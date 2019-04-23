package com.example.fallingsnow

class FallObject{
    /*private int initX;
    private int initY;
    private Random random;
    private int parentWidth;//父容器宽度
    private int parentHeight;//父容器高度
    private float objectWidth;//下落物体宽度
    private float objectHeight;//下落物体高度

    public int initSpeed;//初始下降速度

    public float presentX;//当前位置X坐标
    public float presentY;//当前位置Y坐标
    public float presentSpeed;//当前下降速度

    private Bitmap bitmap;
    public Builder builder;

    private static final int defaultSpeed = 10;//默认下降速度
    */

    private var initX
    private var initY
    private var random
    private var parentWith
    private var parentHeight
    private var objectWidth
    private var objectHeight
    private var mBitmap

    var initSpeed
    var presentX
    var presentY
    var presentSpeed
    var builder

    companion object {
        private const val defaultSpeed = 10
    }

}
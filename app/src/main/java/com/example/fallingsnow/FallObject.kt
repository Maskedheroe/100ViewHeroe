package com.example.fallingsnow

import android.graphics.Bitmap
import android.graphics.Canvas
import java.util.*

class FallObject {

    private var initX: Int? = null
    private var initY: Int? = null
    private var random: Random? = null
    private var parentWith: Int? = null//父容器宽度
    private var parentHeight: Int? = null//父容器高度
    private var objectWidth: Int? = null //下落物体宽度
    private var objectHeight: Int? = null//下落物体高度
    private var mBitmap: Bitmap

    var initSpeed: Int      //初始下降速度
    var presentX: Float? = null     //当前位置x坐标
    var presentY: Float? = null    //当前位置y坐标
    var presentSpeed: Float? = null //当前下降速度
    var builder: Builder? = null    //建造者

    constructor(builder: Builder, width: Int, height: Int) {
        //随机物体Y坐标，并让物体一开始从屏幕顶部下落
        this.random = Random()
        initX = random!!.nextInt(width)
        initY = random!!.nextInt(height) - height
        presentX = initX!!.toFloat()
        presentY = initY!!.toFloat()
        initSpeed = builder.getInitSpeed()
        presentSpeed = initSpeed.toFloat()
        mBitmap = builder.getBitMap()
        objectWidth = mBitmap.width
        objectHeight = mBitmap.height
        parentHeight = height
        parentWith = width
    }

    private constructor(builder: Builder) {
        this.builder = builder
        this.initSpeed = builder.getInitSpeed()
        this.mBitmap = builder.getBitMap()
    }

    companion object {
        private const val defaultSpeed = 10


    }

    public fun drawObject(canvas: Canvas){
        moveObject()
        canvas.drawBitmap(mBitmap,presentX!!,presentY!!,null)
    }

    private fun moveObject(){
        moveY()
        if (presentY!! >parentHeight!!){
            reset()
        }
    }

    private fun moveY(){
        presentY = presentY?.plus(presentSpeed!!)
    }

    private fun reset() {
        presentY = - objectHeight!!.toFloat()
        presentSpeed = initSpeed.toFloat()
    }

    class Builder {
        private var initSpeed: Int
        private var bitmap: Bitmap

        constructor(bitmap: Bitmap) {
            this.initSpeed = defaultSpeed
            this.bitmap = bitmap
        }

        /**
         * 设置物体的初始下落速度
         * @param speed
         * @return
         */
        fun setSpeed(speed: Int): Builder {
            this.initSpeed = speed
            return this
        }

        fun build(): FallObject {
            return FallObject(this)
        }

        fun getInitSpeed(): Int {
            return initSpeed
        }

        fun getBitMap(): Bitmap {
            return bitmap
        }
    }

}
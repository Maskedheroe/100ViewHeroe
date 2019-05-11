package com.example.fallingsnow

import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.PixelFormat
import android.graphics.drawable.Drawable
import java.util.*
import android.graphics.Matrix


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

    var isSpeedRandom: Boolean? = null
    var isSizeRandom: Boolean? = null

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

        this.builder = builder
        isSpeedRandom = builder.getSpeedRandom()
        isSizeRandom = builder.getSizeRandom()

        initSpeed = builder.getInitSpeed()
        randomSpeed()
        randomSize()

    }


    private constructor(builder: Builder) {
        this.builder = builder
        this.initSpeed = builder.getInitSpeed()
        this.mBitmap = builder.getBitMap()
        this.isSpeedRandom = builder.getSpeedRandom()
        this.isSizeRandom = builder.getSizeRandom()
    }

    companion object {
        private const val defaultSpeed = 10


    }

    public fun drawObject(canvas: Canvas) {
        moveObject()
        canvas.drawBitmap(mBitmap, presentX!!, presentY!!, null)
    }

    private fun moveObject() {
        moveY()
        if (presentY!! > parentHeight!!) {
            reset()
        }
    }

    private fun moveY() {
        presentY = presentY?.plus(presentSpeed!!)
    }


    class Builder {
        private var isSizeRandom: Boolean? = null
        private var isSpeedRandom: Boolean? = null
        private var initSpeed: Int
        private var bitmap: Bitmap

        constructor(bitmap: Bitmap) {
            this.initSpeed = defaultSpeed
            this.bitmap = bitmap
            this.isSpeedRandom = false
            this.isSizeRandom = false
        }

        constructor(drawable: Drawable) {
            this.initSpeed = defaultSpeed
            this.bitmap = drawableToBitMap(drawable)
            this.isSpeedRandom = false
            this.isSizeRandom = false
        }

        private fun drawableToBitMap(drawable: Drawable): Bitmap {
            val bitmap = Bitmap.createBitmap(drawable.intrinsicWidth,
                    drawable.intrinsicHeight,
                    if (drawable.opacity != PixelFormat.OPAQUE)
                        Bitmap.Config.ARGB_8888 else Bitmap.Config.RGB_565)
            val canvas = Canvas(bitmap)
            drawable.setBounds(0, 0, drawable.intrinsicWidth, drawable.intrinsicHeight)
            drawable.draw(canvas)
            return bitmap
        }

        /**
         * 改变bitmap的大小
         * @param bitmap 目标bitmap
         * @param newW 目标宽度
         * @param newH 目标高度
         * @return
         */
        private fun changeBitmapSize(bitmap: Bitmap, newW: Int, newH: Int): Bitmap {
            val oldW = bitmap.width
            val oldH = bitmap.height

            //计算缩放比例
            val scaleWidth = newW / oldW.toFloat()
            val scaleHeight = newH / oldH.toFloat()

            //取得想要缩放的matrix参数
            val matrix = Matrix()
            matrix.postScale(scaleWidth, scaleHeight)

            //得到新的图片
            return Bitmap.createBitmap(bitmap, 0, 0, oldW, oldH, matrix, true)
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

        fun setSize(w: Int, h: Int, isRandomSize: Boolean): Builder {
            this.bitmap = changeBitmapSize(this.bitmap, w, h)
            this.isSizeRandom = isRandomSize
            return this
        }

        public fun setSpeed(speed: Int, isRandomSpeed: Boolean): Builder {
            this.initSpeed = speed
            this.isSpeedRandom = isRandomSpeed
            return this
        }

        fun getSpeedRandom(): Boolean? {
            return isSpeedRandom!!
        }

        fun getSizeRandom(): Boolean? {
            return isSizeRandom!!
        }

    }

    /*
    /**
     * 重置object位置
     */
    private void reset(){
        presentY = -objectHeight;
        randomSpeed();//记得重置时速度也一起重置，这样效果会好很多
    }

    /**
     * 随机物体初始下落速度
     */
    private void randomSpeed(){
        if(isSpeedRandom){
            presentSpeed = (float)((random.nextInt(3)+1)*0.1+1)* initSpeed;//这些随机数大家可以按自己的需要进行调整
        }else {
            presentSpeed = initSpeed;
        }
    }

    /**
     * 随机物体初始大小比例
     */
    private void randomSize(){
        if(isSizeRandom){
            float r = (random.nextInt(10)+1)*0.1f;
            float rW = r * builder.bitmap.getWidth();
            float rH = r * builder.bitmap.getHeight();
            bitmap = changeBitmapSize(builder.bitmap,(int)rW,(int)rH);
        }else {
            bitmap = builder.bitmap;
        }
        objectWidth = bitmap.getWidth();
        objectHeight = bitmap.getHeight();
    }
    */

    private fun reset() {
        presentY = -1 * objectHeight!!.toFloat()
        randomSpeed()//记得重置时速度也一起重置，这样效果会好很多
    }


    private fun randomSize() {
        mBitmap = if (isSizeRandom!!) {
            val r = (random!!.nextInt(10) + 1) * 0.1f
            val rW = r * builder!!.getBitMap().width
            val rH = r * builder!!.getBitMap().height
            changeBitmapSize(builder!!.getBitMap(), rW.toInt(), rH.toInt())
        } else {
            builder!!.getBitMap()
        }
        objectWidth = mBitmap.width
        objectHeight = mBitmap.height
    }

    private fun randomSpeed() {

        if (isSpeedRandom!!) {
            presentSpeed = ((random!!.nextInt(3) + 1) * 0.1 + 1).toFloat() * initSpeed.toFloat()//这些随机数大家可以按自己的需要进行调整
        } else {
            presentSpeed = initSpeed.toFloat()
        }
    }


    /**
     * 改变bitmap的大小
     * @param bitmap 目标bitmap
     * @param newW 目标宽度
     * @param newH 目标高度
     * @return
     */
    fun changeBitmapSize(bitmap: Bitmap, newW: Int, newH: Int): Bitmap {
        var bitmap = bitmap
        val oldW = bitmap.width
        val oldH = bitmap.height
        // 计算缩放比例
        val scaleWidth = newW.toFloat() / oldW
        val scaleHeight = newH.toFloat() / oldH
        // 取得想要缩放的matrix参数
        val matrix = Matrix()
        matrix.postScale(scaleWidth, scaleHeight)
        // 得到新的图片
        bitmap = Bitmap.createBitmap(bitmap, 0, 0, oldW, oldH, matrix, true)
        return bitmap
    }
}
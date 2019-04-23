package com.example.fallingsnow

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.view.View
/*
*
* 要点：
* 1、周期性绘制要用handler（在onDraw中）
*
*
*
*
* */
class FallingView(context: Context?, attrs: AttributeSet?) : View(context, attrs) {

    private val mContext: Context

    private val mAttrs: AttributeSet

    private val mPaint: Paint

    private var mViewWidth: Int? = null
    private var mViewHeight: Int? = null

    private var mTestPaint: Paint? = null
    private var mSnoY: Int? = null

    companion object {
        private const val DEFAULT_WIDTH = 600  //默认宽度
        private const val DEFAULT_HEIGHT = 1000  //默认高度
        private const val INTERVALTIME = 5L //重绘间隔时间
    }

    init {
        mContext = context!!
        mAttrs = attrs!!
        mPaint = Paint()
        mPaint.color = Color.WHITE
        mPaint.style = Paint.Style.FILL
        mSnoY = 0
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        val height = measureSize(DEFAULT_HEIGHT, heightMeasureSpec)
        val width = measureSize(DEFAULT_WIDTH, heightMeasureSpec)
        setMeasuredDimension(width, height)

        mViewWidth = width
        mViewHeight = height
    }

    private fun measureSize(defaultSize: Int, measureSpec: Int): Int {
        var result = defaultSize
        val specMode = View.MeasureSpec.getMode(measureSpec)
        val specSize = View.MeasureSpec.getSize(measureSpec)
        if (specMode == View.MeasureSpec.EXACTLY) {
            result = specSize
        } else if (specMode == View.MeasureSpec.AT_MOST) {
            result = Math.min(result, specSize)
        }
        return result
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        canvas!!.drawCircle(100f, mSnoY!!.toFloat(), 25f, mTestPaint!!)
        handler.postDelayed(runnable, INTERVALTIME)  //间隔一段时间再重绘
    }

    private val runnable : Runnable = object :Runnable{
        override fun run() {
            mSnoY = mSnoY!! + 15
            if(mSnoY!! > mViewHeight!!){
                //超出屏幕则重置雪球位置
                mSnoY = 0
            }
            invalidate()
        }
    }
}
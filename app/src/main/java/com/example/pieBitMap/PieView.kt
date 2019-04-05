package com.example.pieBitMap

import android.content.Context
import android.graphics.Canvas
import android.graphics.RectF
import android.util.AttributeSet
import android.view.View

class PieEntry {
    //颜色
    private var color: Int? = null

    //百分比
    private var percentAge: Float? = null

    //条目名
    private var label: String? = null

    //扇区起始角度
    private var currentStartAngle: Float? = null

    //扇区总角度
    private var sweepAngle: Float? = null
}

class MyPieView : View {

    private var mTotalWidth: Int? = null

    private var mTotalHeight: Int? = null

    private var mRadius: Int? = null

    private lateinit var mRectF: RectF

    constructor(context: Context) : super(context)
    constructor(context: Context, attr: AttributeSet?) : super(context, attr) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr)

    private fun init() {

    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)

        canvas!!.translate(mTotalWidth!! / 2.toFloat(), mTotalHeight!! / 2.toFloat())
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        //获取实际的View宽高

        //!!!!!处理padding的方法
        mTotalWidth = w - paddingStart - paddingEnd
        mTotalHeight = h - paddingTop - paddingBottom

        initRectF()
    }


    private fun initRectF() {
        val shortSideLength = if (mTotalHeight!! < mTotalWidth!!) mTotalHeight else mTotalWidth

        mRadius = shortSideLength!!.shr(2)

        mRectF = RectF(-mRadius!!.toFloat(),-mRadius!!.toFloat(),mRadius!!.toFloat(),mRadius!!.toFloat())
    }

}

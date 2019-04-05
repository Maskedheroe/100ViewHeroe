package com.example.pieBitMap

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.RectF
import android.util.AttributeSet
import android.view.View

data class PieEntry(var color: Int,var percentage:Float,var label: String,var currentStartAngle: Float,var sweepAngle: Float) {
}

class MyPieView : View {

    private var mTotalWidth: Int? = null

    private var mTotalHeight: Int? = null

    private var mRadius: Int? = null

    private lateinit var mRectF: RectF

    private var mPieLists : ArrayList<PieEntry>? = null

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

    /*  private void initData() {
        //默认的起始角度为-90°
        float currentStartAngle = -90;
        for (int i = 0; i < mPieLists.size(); i++) {
            PieEntry pie = mPieLists.get(i);
            pie.setCurrentStartAngle(currentStartAngle);
            //每个数据百分比对应的角度
            float sweepAngle = pie.getPercentage() / 100 * 360;
            pie.setSweepAngle(sweepAngle);
            //起始角度不断增加
            currentStartAngle += sweepAngle;
            //添加颜色
            pie.setColor(mColorLists.get(i));
        }
    }
*/


    private fun initData(){

        var currentStartAngle = -90f //默认起始角度为-90°

        for (i in mPieLists!!.indices){
            val pie = mPieLists!![i]
            pie.currentStartAngle = currentStartAngle
            val sweepAngle = pie.percentage /100 * 360
            pie.sweepAngle = sweepAngle
            //每个数据百分比对应的角度

            //起始角度不断增加
            currentStartAngle += sweepAngle

            //添加颜色
            pie.color = mColorLists!![i]

        }

    }
}

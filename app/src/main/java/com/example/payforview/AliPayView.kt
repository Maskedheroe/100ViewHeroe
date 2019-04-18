package com.example.payforview

import android.animation.ValueAnimator
import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.View

class AliPayView(context: Context?, attrs: AttributeSet?) : View(context, attrs) {

    private var mPaint: Paint? = null
    private var mDstPath: Path? = null
    private var mCirclePath: Path? = null
    private var mPathMeasure: PathMeasure? = null
    private var mCurAnimValue: Float? = null

    private val mCentX = 300f
    private val mCentY = 300f
    private val mRadius = 100f

    init {
        setLayerType(LAYER_TYPE_SOFTWARE, null) //什么意思？ -->关闭硬件加速
        mCurAnimValue = 0f
        mPaint = Paint(Paint.ANTI_ALIAS_FLAG)
        mPaint!!.style = Paint.Style.STROKE
        mPaint!!.strokeWidth = 4f
        mPaint!!.color = Color.BLACK
        mDstPath = Path()

        mCirclePath = Path()
        mCirclePath!!.addCircle(mCentX, mCentY, mRadius, Path.Direction.CW)
        mCirclePath!!.moveTo(mCentX - mCentY / 5f, mCentY)
        mCirclePath!!.lineTo(mCentX, mCentY + mRadius / 2)
        mCirclePath!!.lineTo(mCentX + mRadius / 1.3f, mCentY - mRadius / 2.5f)

        mPathMeasure = PathMeasure(mCirclePath!!, false)
        val animator = ValueAnimator.ofFloat(0f, 2f)
        animator.addUpdateListener {
            mCurAnimValue = animator.animatedValue as Float
            invalidate()
        }
        animator.duration = 4000
        animator.start()
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        canvas?.drawColor(Color.WHITE)

        if (mCurAnimValue!! < 1) {  //画外圆的圆形
            val stop = mPathMeasure!!.length * mCurAnimValue!!
            mPathMeasure!!.getSegment(0f, stop, mDstPath, true)
        } else if (mCurAnimValue!! == 1f) {   //说明圆已经画完
            mPathMeasure!!.getSegment(0f, mPathMeasure!!.length, mDstPath!!, true)
            mPathMeasure!!.nextContour()   //将路径转到 对钩的路径上
        } else {   //已经在对钩路径上了
            val stop = mPathMeasure!!.length * (mCurAnimValue!! - 1f)   //求终点需要减去1
            mPathMeasure!!.getSegment(0f, stop, mDstPath, true)
        }

        canvas!!.drawPath(mDstPath!!, mPaint!!)
    }

}
/**
 * PathMeasure的nextContour函数
 * 由于一个path可以是由多个封闭的path组成的，所以要想跳转下一个path可用nextContour函数
 * */
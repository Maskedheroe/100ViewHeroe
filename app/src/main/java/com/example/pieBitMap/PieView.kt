package com.example.pieBitMap

import android.content.Context
import android.content.res.Resources
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.RectF
import android.util.AttributeSet
import android.view.View

data class PieEntry(var percentage: Float, var label: String) {
    var color: Int? = null
    var currentStartAngle: Float? = null
    var sweepAngle: Float? = null
}

class MyPieView : View {

    private var mTotalWidth: Int? = null

    private var mTotalHeight: Int? = null

    private var mRadius: Int? = null

    private lateinit var mRectF: RectF

    private var mPieLists: ArrayList<PieEntry>? = null

    private var mPaint: Paint? = null

    constructor(context: Context) : super(context)
    constructor(context: Context, attr: AttributeSet?) : super(context, attr) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr)

    private fun init() {
        initPaint()
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)

        //将坐标的中心投影到View的中心
        canvas!!.translate(mTotalWidth!! / 2.toFloat(), mTotalHeight!! / 2.toFloat())

        if (isPieListsNull()) {
            mPaint!!.color = Color.BLACK
            canvas.drawText("请通过setData添加数据", -120f, 0f, mPaint)
        } else {
            //绘制饼状图
            drawPie(canvas)
            //绘制中心空洞
            drawHole(canvas)

            drawPoint(canvas)
        }

    }

    private fun drawHole(canvas: Canvas) {
        /* //饼图中间的空洞占据的比例
         float holeRadiusProportion = 59;
         canvas.drawCircle(0, 0, mRadius * holeRadiusProportion / 100, mPaint);
 */
        mPaint!!.color = Color.WHITE
        val holeRadiusProportion = 59
        canvas.drawCircle(0f, 0f, mRadius!! * holeRadiusProportion / 100.toFloat(), mPaint)
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
        //取短边 作为饼状图所在正方形的边长
        val shortSideLength = if (mTotalHeight!! < mTotalWidth!!) mTotalHeight else mTotalWidth

        //除以2即为饼状图的半径
        mRadius = shortSideLength!!.shr(2)

        //设置RectF的坐标
        mRectF = RectF(-mRadius!!.toFloat(), -mRadius!!.toFloat(), mRadius!!.toFloat(), mRadius!!.toFloat())
    }


    private var mColorLists: ArrayList<Int>? = null

    private fun initData() {


        var currentStartAngle = -90f //默认起始角度为-90°

        for (i in mPieLists!!.indices) {
            val pie = mPieLists!![i]
            pie.currentStartAngle = currentStartAngle
            val sweepAngle = pie.percentage / 100 * 360
            pie.sweepAngle = sweepAngle
            //每个数据百分比对应的角度

            //起始角度不断增加
            currentStartAngle += sweepAngle

            //添加颜色
            pie.color = mColorLists!![i]

        }

    }

    public fun setData(list: List<PieEntry>) {
        this.mPieLists = list as ArrayList<PieEntry>
        initData()
        invalidate()
    }

    private fun initPaint() {
        mPaint = Paint()
        mPaint?.isAntiAlias = true
        mPaint?.color = Color.WHITE
        mPaint?.textSize = 12.sptoPx.toFloat()

    }

    private fun drawPie(canvas: Canvas) {
        mPieLists?.forEach {
            mPaint?.color = it.color!!
            canvas.drawArc(mRectF, it.currentStartAngle!!, it.sweepAngle!!, true, mPaint)
        }
    }

    //判断数据是否为空
    private fun isPieListsNull(): Boolean {
        return mPieLists == null || mPieLists!!.size == 0
    }

    fun setColors(createColors: ArrayList<Int>) {
        mColorLists = createColors
        initColors()
        invalidate()
    }

    private fun initColors() {

        if (isPieListsNull()) {
            return
        }
        for (i in 0 until mPieLists!!.size) {
            mPieLists!!.get(i).color = mColorLists!!.get(i)
        }
    }

    private fun drawPoint(canvas: Canvas) {
        for (pie in mPieLists!!) {
            val halfAngle = pie.currentStartAngle!! + pie.sweepAngle!! / 2
            val cos = Math.cos(Math.toRadians(halfAngle.toDouble())).toFloat()
            val sin = Math.sin(Math.toRadians(halfAngle.toDouble())).toFloat()

            //通过正余弦算出延长点的坐标
            val xCirclePoint = (mRadius!! + DISTANCE) * cos
            val yCirclePoint = (mRadius!! + DISTANCE) * sin

            mPaint!!.color = pie.color!!

            //绘制延长点
            canvas.drawCircle(xCirclePoint, yCirclePoint, SMALLCIRCLERADIUS, mPaint!!)

            //绘制同心圆环
            mPaint!!.style = Paint.Style.STROKE

            canvas.drawCircle(xCirclePoint, yCirclePoint, SMALLCIRCLERADIUS, mPaint!!)
            mPaint!!.style = Paint.Style.FILL
        }
    }

    companion object {
        private const val DISTANCE = 14f
        private const val SMALLCIRCLERADIUS = 3f
    }
}


//拓展函数的使用
private val Int.sptoPx: Int
    get() {
        val fontScale = Resources.getSystem().getDisplayMetrics().scaledDensity
        return (this.toFloat() * fontScale + 0.5f).toInt()
    }


package com.example.pieBitMap

import android.content.Context
import android.content.res.Resources
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.RectF
import android.util.AttributeSet
import android.view.View
import java.text.DecimalFormat

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

    private val yOffset: Float = 14f
    private val xOffset: Float = 7f
    private val extend: Float = 180f

    //延长点的大小
    private val smallCircleRadius = 3f

    //延长点上同心圆环的大小
    private val bigCircleRadius = 7f

    //长宽比
    private var mScale: Float? = null

    constructor(context: Context) : super(context)
    constructor(context: Context, attr: AttributeSet?) : super(context, attr) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr)

    private fun init() {
        initPaint()
    }

    //处理wrap_content的方法
    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        if (mScale!!.toInt() != 0 && MeasureSpec.getMode(heightMeasureSpec) == MeasureSpec.AT_MOST) {
            val height: Int = (mTotalWidth!! / mScale!!).toInt() //设置默认高度
            setMeasuredDimension(widthMeasureSpec, height)
        }
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
            //绘制延长点
            drawPoint(canvas)
            //绘制延长线及文字
            drawTitleText(canvas)
        }

    }

    private fun drawHole(canvas: Canvas) {
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


    /*
        private void initRectF() {

            Paint.FontMetrics fontMetrics = mPaint.getFontMetrics();
            //文字的高度
            float textHeight = fontMetrics.bottom - fontMetrics.top + fontMetrics.leading;
            //延长线的纵向长度
            float lineHeight = distance + bigCircleRadius + yOffset;
            //延长线的横向长度
            float lineWidth = distance + bigCircleRadius + xOffset + extend;
            //求出饼状图加延长线和文字 所有内容需要的长方形空间的长宽比
            mScale = mTotalWidth / (mTotalWidth + lineHeight * 2 + textHeight * 2 - lineWidth * 2);

            //长方形空间其短边的长度
            float shortSideLength;
            //通过宽高比选择短边
            if (mTotalWidth / mTotalHeight >= mScale) {
                shortSideLength = mTotalHeight;
            } else {
                shortSideLength = mTotalWidth / mScale;
            }
            //饼图所在的区域为正方形，处于长方形空间的中心
            //空间的高度减去上下两部分文字显示需要的高度，除以2即为饼图的半径
            mRadius = shortSideLength / 2 - lineHeight - textHeight;
            //设置RectF的坐标
            mRectF = new RectF(-mRadius, -mRadius, mRadius, mRadius);
        }

        */
    private fun initRectF() {

        val fontMetrics = mPaint!!.fontMetrics

        //获取文字的高度
        val textHeight = fontMetrics.bottom - fontMetrics.top + fontMetrics.leading

        //延长线的纵向宽度
        val lineHeight = DISTANCE + bigCircleRadius + yOffset

        //延长线的横向长度
        val lineWidth = DISTANCE + bigCircleRadius + xOffset + extend

        //求出饼图加延长线和文字 所有内容需要的长方形空间的长宽比
        mScale = mTotalWidth!! / (mTotalWidth!! + lineHeight * 2 + textHeight * 2 - lineWidth * 2)

        //取短边 作为饼状图所在正方形的边长
        val shortSideLength = if (mTotalWidth!! / mTotalHeight!! >= mScale!!) mTotalHeight else mTotalWidth!! / mScale!!.toInt()

        //饼图所在的区域为正方形，处于长方形空间的中心
        //空间的高度减去上下两部分文字显示需要的高度，除以2即为饼图的半径
        mRadius = shortSideLength!!.shr(2) - lineHeight.toInt() - textHeight.toInt()

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
            val halfAngle = pie.currentStartAngle!! + pie.sweepAngle!!.toInt().shr(1)
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


    private fun drawTitleText(canvas: Canvas) {
        val offsetRadians = Math.atan(yOffset / xOffset.toDouble())
        val cosOffset = Math.cos(offsetRadians)
        val sinOffset = Math.sin(offsetRadians)

        for (pie in mPieLists!!) {
            val halfAngle = pie.currentStartAngle!! + pie.sweepAngle?.toInt()?.shr(1)!!
            val cos = Math.cos(Math.toRadians(halfAngle.toDouble())).toFloat()
            val sin = Math.sin(Math.toRadians(halfAngle.toDouble())).toFloat()

            //通过正余弦算出延长点的位置
            val xCirclePoint = (mRadius!! + DISTANCE) * cos
            val yCirclePoint = (mRadius!! + DISTANCE) * sin

            mPaint!!.color = pie.color!!
            canvas.drawCircle(xCirclePoint, yCirclePoint, smallCircleRadius, mPaint!!)
            //绘制同心圆环
            mPaint!!.style = Paint.Style.STROKE
            canvas.drawCircle(xCirclePoint, yCirclePoint, bigCircleRadius, mPaint!!)
            mPaint!!.style = Paint.Style.FILL

            //将饼图分为四个象限，从右上角开始顺时针，每90度分为一个象限
            val quadrant: Int = ((halfAngle + 90) / 90).toInt()

            //初始化延长线的起点、转折点、终点
            var xLineStartPoint: Float = 0f
            var yLineStartPoint: Float = 0f
            var xLineEndPoint: Float = 0f
            var yLineEndPoint: Float = 0f
            var xLineTurningPoint: Float = 0f
            var yLineTurningPoint: Float = 0f

            val text = pie.label + " " + DecimalFormat("#.#").format(pie.percentage) + "%"

            val cosLength = bigCircleRadius * cosOffset
            val sinLength = bigCircleRadius * sinOffset

            when (quadrant) {
                0 -> {
                    xLineStartPoint = xCirclePoint + cosLength.toFloat()
                    yLineStartPoint = yCirclePoint - sinLength.toFloat()
                    xLineTurningPoint = xLineStartPoint + xOffset
                    yLineTurningPoint = yLineStartPoint - yOffset
                    xLineEndPoint = xLineTurningPoint + extend
                    yLineEndPoint = yLineTurningPoint
                    mPaint!!.textAlign = Paint.Align.RIGHT
                    canvas.drawText(text, xLineEndPoint, yLineEndPoint - 5, mPaint!!)
                }
                1 -> {
                    xLineStartPoint = xCirclePoint + cosLength.toFloat()
                    yLineStartPoint = yCirclePoint + sinLength.toFloat()
                    xLineTurningPoint = xLineStartPoint + xOffset
                    yLineTurningPoint = yLineStartPoint + yOffset
                    xLineEndPoint = xLineTurningPoint + extend
                    yLineEndPoint = yLineTurningPoint
                    mPaint!!.textAlign = Paint.Align.RIGHT
                    canvas.drawText(text, xLineEndPoint + 20, yLineEndPoint - 5, mPaint!!) //此处写死了x的位置，是否可以开放接口给用户进行灵活设置？
                }
                2 -> {
                    xLineStartPoint = xCirclePoint - cosLength.toFloat()
                    yLineStartPoint = yCirclePoint + sinLength.toFloat()
                    xLineTurningPoint = xLineStartPoint - xOffset
                    yLineTurningPoint = yLineStartPoint + yOffset
                    xLineEndPoint = xLineTurningPoint - extend
                    yLineEndPoint = yLineTurningPoint
                    mPaint!!.textAlign = Paint.Align.LEFT
                    val offset = text.length * 10 / 2    //此处如何处理大长字符串??
                    canvas.drawText(text, xLineEndPoint - offset, yLineEndPoint - 5, mPaint!!)
                }
                3 -> {
                    xLineStartPoint = xCirclePoint - cosLength.toFloat()
                    yLineStartPoint = yCirclePoint - sinLength.toFloat()
                    xLineTurningPoint = xLineStartPoint - xOffset
                    yLineTurningPoint = yLineStartPoint - yOffset
                    xLineEndPoint = xLineTurningPoint - extend
                    yLineEndPoint = yLineTurningPoint
                    mPaint!!.textAlign = Paint.Align.LEFT
                    canvas.drawText(text, xLineEndPoint, yLineEndPoint - 5, mPaint!!)
                }
            }
            //绘制延长线
            canvas.drawLine(xLineStartPoint, yLineStartPoint, xLineTurningPoint, yLineTurningPoint, mPaint!!)
            canvas.drawLine(xLineTurningPoint, yLineTurningPoint, xLineEndPoint, yLineEndPoint, mPaint!!)
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


package com.example.bookview

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.View
import android.view.animation.LinearInterpolator
import android.widget.Scroller
import com.example.app.R


class BookPageView : View {
    private var pointPaint: Paint? = null//绘制各标识点的画笔
    private var bgPaint: Paint? = null//背景画笔
    private var pathAPaint: Paint? = null //绘制A区域画笔
    private var pathA: Path? = null
    private var bitmap: Bitmap? = null //缓存bitMap
    private var bitmapCanvas: Canvas? = null

    private var pathCPaint: Paint? = null //绘制A区域画笔
    private var pathC: Path? = null

    private var pathBPaint: Paint? = null
    private var pathB: Path? = null

    private var mStyle: String? = null

    private var mScroller: Scroller? = null  //接住scroller来实现滑落效果

    companion object {
        public const val STYLE_TOP_RIGHT = "STYLE_TOP_RIGHT"
        public const val STYLE_LOWER_RIGHT = "STYLE_LOWER_RIGHT"
        public const val STYLE_MIDDLE = "STYLE_MIDDLE"
        public const val STYLE_RIGHT = "STYLE_RIGHT"
        public const val STYLE_LEFT = "STYLE_LEFT"
    }

    private var a: MyPoint? = null
    private var f: MyPoint? = null
    private var g: MyPoint? = null
    private var e: MyPoint? = null
    private var h: MyPoint? = null
    private var c: MyPoint? = null
    private var j: MyPoint? = null
    private var b: MyPoint? = null
    private var k: MyPoint? = null
    private var d: MyPoint? = null
    private var i: MyPoint? = null

    private var defaultWidth: Int = 0//默认宽度
    private var defaultHeight: Int = 0//默认高度
    private var viewWidth: Int = 0
    private var viewHeight: Int = 0

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        init(context, attrs)
    }

    constructor(context: Context) : super(context) {}

    constructor(context: Context,
                attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
    }

    private fun init(context: Context, attrs: AttributeSet?) {
        defaultWidth = 600
        defaultHeight = 1000

        viewWidth = defaultWidth
        viewHeight = defaultHeight

        a = MyPoint()
        f = MyPoint()
        g = MyPoint()
        e = MyPoint()
        h = MyPoint()
        c = MyPoint()
        j = MyPoint()
        b = MyPoint()
        k = MyPoint()
        d = MyPoint()
        i = MyPoint()
        calcPointsXY(a!!, f!!)

        pointPaint = Paint()
        pointPaint!!.color = Color.RED
        pointPaint!!.textSize = 25f

        bgPaint = Paint()
        bgPaint!!.color = Color.WHITE

        pathAPaint = Paint()
        pathAPaint?.color = Color.GREEN
        pathAPaint?.isAntiAlias = true  //设置抗锯齿
        pathA = Path()

        pathCPaint = Paint()
        pathCPaint?.color = Color.YELLOW
        pathCPaint?.isAntiAlias = true
        pathCPaint?.xfermode = PorterDuffXfermode(PorterDuff.Mode.DST_ATOP)
        pathC = Path()

        pathBPaint = Paint()
        pathBPaint?.color = resources.getColor(R.color.blue_light)
        pathBPaint?.isAntiAlias = true
        pathBPaint?.xfermode = PorterDuffXfermode(PorterDuff.Mode.DST_ATOP)
        pathB = Path()

        mScroller = Scroller(context, LinearInterpolator())  //正常速率滑动
    }


    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        val height = measureSize(defaultHeight, heightMeasureSpec)
        val width = measureSize(defaultWidth, widthMeasureSpec)
        setMeasuredDimension(width, height)

        viewWidth = width
        viewHeight = height
        a?.x = -1f
        a?.y = -1f
    }

    private fun measureSize(defaultSize: Int, measureSpec: Int): Int {
        var result: Int = defaultSize
        val specMode = View.MeasureSpec.getMode(measureSpec)
        val specSize = View.MeasureSpec.getSize(measureSpec)

        if (specMode == View.MeasureSpec.EXACTLY) {
            result = specSize
        } else if (specMode == View.MeasureSpec.AT_MOST) {
            result = Math.min(result, specSize)
        }
        return result
    }


    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        bitmap = Bitmap.createBitmap(viewWidth.toInt(), viewHeight.toInt(), Bitmap.Config.ARGB_8888)
        bitmapCanvas = Canvas(bitmap)
        if (a?.x == -1f && a?.y == -1f) {
            bitmapCanvas?.drawPath(getPathDefault(), pathAPaint);
        } else {
            if (f?.x == viewWidth.toFloat() && f?.y == 0f) {
                bitmapCanvas?.drawPath(getPathAFromTopRight(), pathAPaint)
            } else if (f?.x == viewWidth.toFloat() && f?.y == viewHeight.toFloat()) {
                bitmapCanvas?.drawPath(getPathAFromLowerRight(), pathAPaint)
            }
            bitmapCanvas?.drawPath(getPathC(), pathCPaint)
            bitmapCanvas?.drawPath(getpathB(), pathBPaint)
        }
        canvas.drawBitmap(bitmap, 0f, 0f, null)
    }

    private fun getPathAFromTopRight(): Path {
        pathA?.reset()
        pathA?.lineTo(c?.x!!, c?.y!!);//移动到c点
        pathA?.quadTo(e?.x!!, e?.y!!, b?.x!!, b?.y!!)//从c到b画贝塞尔曲线，控制点为e
        pathA?.lineTo(a?.x!!, a?.y!!);//移动到a点
        pathA?.lineTo(k?.x!!, k?.y!!);//移动到k点
        pathA?.quadTo(h?.x!!, h?.y!!, j?.x!!, j?.y!!)//从k到j画贝塞尔曲线，控制点为h
        pathA?.lineTo(viewWidth.toFloat(), viewHeight.toFloat())//移动到右下角
        pathA?.lineTo(0f, viewHeight.toFloat())//移动到左下角
        pathA?.close()
        return pathA!!
    }


    /**
     * 设置触摸点
     * @param x
     * @param y
     * @param style 判断是左上角还是右下角开始翻页的。
     */
    fun setTouchPoint(x: Float, y: Float, style: String) {
        /*MyPoint touchPoint = new MyPoint();
	a.x = x;
	a.y = y;
	this.style = style;*/
        var touchPoint: MyPoint = MyPoint()
        a?.x = x
        a?.y = y
        this.mStyle = style
        when (mStyle) {
            STYLE_TOP_RIGHT -> {
                f?.x = viewWidth.toFloat()
                f?.y = 0f
                calcPointsXY(a!!, f!!)
                touchPoint = MyPoint(x, y)
                if (calcPointsCX(touchPoint, f!!) < 0) {
                    //如果c点x坐标小于0则重新测量a点坐标
                    calcPointAByTouchPoint()
                    calcPointsXY(a!!, f!!)
                }
                postInvalidate()
            }
            STYLE_LEFT, STYLE_RIGHT -> {
                a?.y = viewHeight - 1f
                f?.x = viewWidth.toFloat()
                f?.y = viewHeight.toFloat()
                calcPointsXY(a!!, f!!)
                postInvalidate()
            }
            STYLE_LOWER_RIGHT -> {
                f?.x = viewWidth.toFloat()
                f?.y = viewHeight.toFloat()
                calcPointsXY(a!!, f!!)
                touchPoint = MyPoint(x, y)
                if (calcPointsCX(touchPoint, f!!) < 0) { //如果c点x坐标小于0则重新测量a点坐标
                    calcPointAByTouchPoint()
                    calcPointsXY(a!!, f!!)
                }
                postInvalidate()
            }
        }
    }


    private fun calcPointAByTouchPoint() {
        val w0 = viewWidth - c?.x!!
        val w1 = Math.abs(f?.x!! - a?.x!!)
        val w2 = viewWidth * w1 / w0
        a?.x = Math.abs(f?.x!! - w2)

        val h1 = Math.abs(f?.y!! - a?.y!!)
        val h2 = w2 * h1 / w1
        a?.y = Math.abs(f?.y!! - h2)
    }

    /**
     * 计算C点的X值
     * @param a
     * @param f
     * @return
     */
    private fun calcPointsCX(a: MyPoint, f: MyPoint): Float {
        val g = MyPoint()
        val e = MyPoint()
        g.x = (a.x + f.x) / 2
        g.y = (a.y + f.y) / 2

        e.x = g.x - (f.y - g.y) * (f.y - g.y) / (f.x - g.x)
        e.y = f.y
        return e.x - (f.x - e.x) / 2
    }

    /**
     * 回到默认状态
     */
    fun setDefaultPath() {
        a?.x = -1f
        a?.y = -1f
        postInvalidate()
    }

    /**
     * 绘制默认的界面
     * @return
     */
    private fun getPathDefault(): Path {
        pathA?.reset()
        pathA?.lineTo(0f, viewHeight.toFloat())
        pathA?.lineTo(viewWidth.toFloat(), viewHeight.toFloat())
        pathA?.lineTo(viewWidth.toFloat(), 0f)
        pathA?.close()
        return pathA!!
    }

    private fun getpathB(): Path {
        pathB?.reset()
        pathB?.lineTo(0f, viewHeight.toFloat())
        pathB?.lineTo(viewWidth.toFloat(), viewHeight.toFloat())
        pathB?.lineTo(viewWidth.toFloat(), 0f)
        pathB?.close()
        return pathB!!
    }

    private fun getPathC(): Path {
        pathC?.reset()
        pathC?.moveTo(i!!.x, i!!.y)
        pathC?.lineTo(d!!.x, d!!.y)
        pathC?.lineTo(b!!.x, b!!.y)
        pathC?.lineTo(a!!.x, a!!.y)
        pathC?.lineTo(k!!.x, k!!.y)
        pathC?.close()
        return pathC!!
    }

    private fun getPathAFromLowerRight(): Path {
        pathA?.reset()
        pathA?.lineTo(0f, viewHeight.toFloat())
        pathA?.lineTo(c!!.x, c!!.y)//移动到c点
        pathA?.quadTo(e!!.x, e!!.y, b!!.x, b!!.y)
        pathA?.lineTo(a!!.x, a!!.y)
        pathA?.lineTo(k!!.x, k!!.y)
        pathA?.quadTo(h!!.x, h!!.y, j!!.x, j!!.y)
        pathA?.lineTo(viewWidth.toFloat(), 0f)
        pathA?.close()
        return pathA!!

    }

    public fun getViewWidth(): Float = viewWidth.toFloat()

    public fun getViewHeight(): Float = viewHeight.toFloat()

    /**
     * 计算各点坐标
     *
     * @param a
     * @param f
     */
    private fun calcPointsXY(a: MyPoint, f: MyPoint) {
        g!!.x = (a.x + f.x) / 2
        g!!.y = (a.y + f.y) / 2

        e!!.x = g!!.x - (f.y - g!!.y) * (f.y - g!!.y) / (f.x - g!!.x)
        e!!.y = f.y

        h!!.x = f.x
        h!!.y = g!!.y - (f.x - g!!.x) * (f.x - g!!.x) / (f.y - g!!.y)

        c!!.x = e!!.x - (f.x - e!!.x) / 2
        c!!.y = f.y

        j!!.x = f.x
        j!!.y = h!!.y - (f.y - h!!.y) / 2

        b = getIntersectionPoint(a, e!!, c!!, j!!)
        k = getIntersectionPoint(a, h!!, c!!, j!!)

        d!!.x = (c!!.x + 2 * e!!.x + b!!.x) / 4
        d!!.y = (2 * e!!.y + c!!.y + b!!.y) / 4

        i!!.x = (j!!.x + 2 * h!!.x + k!!.x) / 4
        i!!.y = (2 * h!!.y + j!!.y + k!!.y) / 4

    }

    /**
     * 计算两线段相交点坐标
     *
     * @param lineOne_My_pointOne
     * @param lineOne_My_pointTwo
     * @param lineTwo_My_pointOne
     * @param lineTwo_My_pointTwo
     * @return 返回该点
     */
    private fun getIntersectionPoint(lineOne_My_pointOne: MyPoint, lineOne_My_pointTwo: MyPoint, lineTwo_My_pointOne: MyPoint, lineTwo_My_pointTwo: MyPoint): MyPoint {
        val x1 = lineOne_My_pointOne.x
        val y1 = lineOne_My_pointOne.y
        val x2 = lineOne_My_pointTwo.x
        val y2 = lineOne_My_pointTwo.y
        val x3 = lineTwo_My_pointOne.x
        val y3 = lineTwo_My_pointOne.y
        val x4 = lineTwo_My_pointTwo.x
        val y4 = lineTwo_My_pointTwo.y

        val pointX = ((x1 - x2) * (x3 * y4 - x4 * y3) - (x3 - x4) * (x1 * y2 - x2 * y1)) / ((x3 - x4) * (y1 - y2) - (x1 - x2) * (y3 - y4))
        val pointY = ((y1 - y2) * (x3 * y4 - x4 * y3) - (x1 * y2 - x2 * y1) * (y3 - y4)) / ((y1 - y2) * (x3 - x4) - (x1 - x2) * (y3 - y4))

        return MyPoint(pointX, pointY)
    }

    override fun computeScroll() {
        if (mScroller?.computeScrollOffset()!!) {
            val x: Float = mScroller!!.currX.toFloat()
            val y: Float = mScroller!!.currY.toFloat()

            if (mStyle.equals(STYLE_TOP_RIGHT)) {
                setTouchPoint(x, y, STYLE_TOP_RIGHT)
            } else {
                setTouchPoint(x, y, STYLE_LOWER_RIGHT)
            }
            if (mScroller!!.finalX.toFloat() == x && mScroller!!.finalY.toFloat() == y) {
                setDefaultPath()
            }
        }
    }
    public fun startCancelAnim() {
        var dx = 0
        var dy = 0
        if (mStyle.equals(STYLE_TOP_RIGHT)) {
            dx = viewWidth - 1 - a?.x?.toInt()!!
            dy = 1 - a?.y!!.toInt()
        } else {
            dx = viewWidth - 1 - a?.x!!.toInt()
            dy = viewHeight - 1 - a?.y!!.toInt()
        }
        mScroller!!.startScroll(a!!.x.toInt(),a!!.y.toInt(),dx, dy,400)
    }
}

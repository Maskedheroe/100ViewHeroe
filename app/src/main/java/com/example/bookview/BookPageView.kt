package com.example.bookview

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.View

class BookPageView : View {
    private var pointPaint: Paint? = null//绘制各标识点的画笔
    private var bgPaint: Paint? = null//背景画笔
    private var pathAPaint:Paint? = null //绘制A区域画笔
    private var pathA:Path? = null
    private var bitmap:Bitmap? = null //缓存bitMap
    private var bitmapCanvas :Canvas? = null

    private var pathCPaint:Paint? = null //绘制A区域画笔
    private var pathC:Path? = null

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

        a = MyPoint(400f, 800f)
        f = MyPoint(viewWidth.toFloat(), viewHeight.toFloat())
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
        pathCPaint?.color  = Color.YELLOW
        pathCPaint?.isAntiAlias = true
        pathCPaint?.xfermode = PorterDuffXfermode(PorterDuff.Mode.DST_ATOP)
        pathC = Path()


    }


    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        //为了看清楚点与View的位置关系绘制一个背景
        canvas.drawRect(0f, 0f, viewWidth.toFloat(), viewHeight.toFloat(), bgPaint!!)
        //绘制各标识点
        canvas.drawText("a", a!!.x, a!!.y, pointPaint!!)
        canvas.drawText("f", f!!.x, f!!.y, pointPaint!!)
        canvas.drawText("g", g!!.x, g!!.y, pointPaint!!)

        canvas.drawText("e", e!!.x, e!!.y, pointPaint!!)
        canvas.drawText("h", h!!.x, h!!.y, pointPaint!!)

        canvas.drawText("c", c!!.x, c!!.y, pointPaint!!)
        canvas.drawText("j", j!!.x, j!!.y, pointPaint!!)

        canvas.drawText("b", b!!.x, b!!.y, pointPaint!!)
        canvas.drawText("k", k!!.x, k!!.y, pointPaint!!)

        canvas.drawText("d", d!!.x, d!!.y, pointPaint!!)
        canvas.drawText("i", i!!.x, i!!.y, pointPaint!!)

        bitmap = Bitmap.createBitmap(viewWidth.toInt(),viewHeight.toInt(),Bitmap.Config.ARGB_8888)
        bitmapCanvas = Canvas(bitmap)
        bitmapCanvas?.drawPath(getPathAFromLowerRight(),pathAPaint)
        bitmapCanvas?.drawPath(getPathC(),pathCPaint)
        canvas.drawBitmap(bitmap,0f,0f,null)


    }

    private fun getPathC(): Path {
        pathC?.reset()
        pathC?.moveTo(i!!.x,i!!.y)
        pathC?.lineTo(d!!.x,d!!.y)
        pathC?.lineTo(b!!.x,b!!.y)
        pathC?.lineTo(a!!.x,a!!.y)
        pathC?.lineTo(k!!.x,k!!.y)
        pathC?.close()
        return pathC!!
    }

    private fun getPathAFromLowerRight(): Path {
        pathA?.reset()
        pathA?.lineTo(0f,viewHeight.toFloat())
        pathA?.lineTo(c!!.x,c!!.y)//移动到c点
        pathA?.quadTo(e!!.x,e!!.y,b!!.x,b!!.y)
        pathA?.lineTo(a!!.x,a!!.y)
        pathA?.lineTo(k!!.x,k!!.y)
        pathA?.quadTo(h!!.x,h!!.y,j!!.x,j!!.y)
        pathA?.lineTo(viewWidth.toFloat(),0f)
        pathA?.close()
        return pathA!!

    }

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
}

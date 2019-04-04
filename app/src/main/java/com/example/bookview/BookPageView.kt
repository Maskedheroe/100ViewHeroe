package com.example.bookview

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.*
import android.graphics.drawable.GradientDrawable
import android.util.AttributeSet
import android.view.MotionEvent
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
    private var bitmapCanvas: Canvas? = null     //区域C的绘制画笔
    private var style: String? = null

    private var pathCContentPaint: Paint? = null
    private var textPaint: Paint? = null  //文字画笔

    private var pathCPaint: Paint? = null //绘制A区域画笔
    private var pathC: Path? = null

    private var pathBPaint: Paint? = null
    private var pathB: Path? = null

    private var mStyle: String? = null

    private var mScroller: Scroller? = null  //接住scroller来实现滑落效果

    private var mLPathAShadowDis: Float? = 0f  //A区域左阴影矩形短边长度参考值
    private var mRPathAShadowDis: Float? = 0f //A区域右阴影矩形短边长度参考值


    private var mMatrixArray = floatArrayOf(0f, 0f, 0f, 0f, 0f, 0f, 0f, 0f, 1.0f)
    private var mMatrix: Matrix? = null


    companion object {
        const val STYLE_TOP_RIGHT = "STYLE_TOP_RIGHT"
        const val STYLE_LOWER_RIGHT = "STYLE_LOWER_RIGHT"
        const val STYLE_MIDDLE = "STYLE_MIDDLE"
        const val STYLE_RIGHT = "STYLE_RIGHT"
        const val STYLE_LEFT = "STYLE_LEFT"
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


    private var drawableLeftTopRight: GradientDrawable? = null
    private var drawableLeftLowerRight: GradientDrawable? = null
    private var drawableRightTopRight: GradientDrawable? = null
    private var drawableRightLowerRight: GradientDrawable? = null
    private var drawableHorizontalLowerRigh: GradientDrawable? = null
    private var drawableBTopRight: GradientDrawable? = null
    private var drawableBLowerRight: GradientDrawable? = null
    private var drawableCTopRight: GradientDrawable? = null
    private var drawableCLowerRight: GradientDrawable? = null


    private var pathAContentBitmap: Bitmap? = null
    private var pathBContentBitmap: Bitmap? = null
    private var pathCContentBitmap: Bitmap? = null


    constructor(context: Context) : super(context)

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        init(context, attrs)
    }

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr)

    private fun init(context: Context, attrs: AttributeSet?) {
        defaultWidth = 600
        defaultHeight = 1000

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

        pointPaint = Paint()
        pointPaint!!.color = Color.RED
        pointPaint!!.textSize = 25f
        pointPaint!!.style = Paint.Style.STROKE

        bgPaint = Paint()
        bgPaint!!.color = Color.WHITE

        pathAPaint = Paint()
        pathAPaint?.color = Color.GREEN
        pathAPaint?.isAntiAlias = true  //设置抗锯齿
        pathA = Path()

        pathCPaint = Paint()
        pathCPaint?.color = Color.YELLOW
        pathCPaint?.isAntiAlias = true
        pathC = Path()

        pathBPaint = Paint()
        pathBPaint?.color = resources.getColor(R.color.blue_light)
        pathBPaint?.isAntiAlias = true
        pathB = Path()

        mScroller = Scroller(context, LinearInterpolator())  //正常速率滑动


        //初始化画笔
        textPaint = Paint()
        textPaint!!.color = Color.BLACK
        textPaint!!.textAlign = Paint.Align.CENTER
        textPaint!!.isSubpixelText = true
        textPaint!!.textSize = 30f

        //初始化C区域画笔
        pathCContentPaint = Paint()
        pathCContentPaint?.color = Color.YELLOW
        pathCContentPaint?.isAntiAlias = true

        style = STYLE_LOWER_RIGHT
        mMatrix = Matrix()
        createGradientDrawable()
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
        pathAContentBitmap = Bitmap.createBitmap(viewWidth, viewHeight, Bitmap.Config.RGB_565)
        drawPathAContentBitmap(pathAContentBitmap!!, pathAPaint!!)

        pathBContentBitmap = Bitmap.createBitmap(viewWidth, viewHeight, Bitmap.Config.RGB_565)
        drawPathBContentBitmap(pathBContentBitmap!!, pathBPaint!!)

        pathCContentBitmap = Bitmap.createBitmap(viewWidth, viewHeight, Bitmap.Config.RGB_565)
        drawPathCContentBitmap(pathCContentBitmap!!, pathCPaint!!)
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        if (a!!.x == -1f && a!!.y == -1f) {
            drawPathAContent(canvas, getPathDefault())
        } else {
            if (f!!.x == viewWidth.toFloat() && f!!.y == 0f) {
                drawPathAContent(canvas, getPathAFromTopRight())
                drawPathBContent(canvas, getPathAFromTopRight())
                drawPathCContent(canvas, getPathAFromTopRight())
            } else if (f!!.x == viewWidth.toFloat() && f!!.y == viewHeight.toFloat()) {
                drawPathAContent(canvas, getPathAFromLowerRight())
                drawPathBContent(canvas, getPathAFromLowerRight())
                drawPathCContent(canvas, getPathAFromLowerRight())

            }
        }

    }

    private fun drawPathAContentBitmap(bitmap: Bitmap, pathPaint: Paint) {
        val canvas = Canvas(bitmap)
        //TODO 是否可以策略模式修改??

        //绘制区域A内的内容
        canvas.drawPath(getPathDefault(), pathPaint)
        canvas.drawText("AAAAAAA区域", viewWidth - 260.toFloat(), viewHeight - 100.toFloat(), textPaint)
        //结束区域内的绘制
    }

    private fun drawPathBContentBitmap(bitmap: Bitmap, pathPaint: Paint) {
        val canvas = Canvas(bitmap)

        //绘制区域B内的内容
        canvas.drawPath(getPathDefault(), pathPaint)
        canvas.drawText("BBBBBB", viewWidth - 260.toFloat(), viewHeight - 100.toFloat(), textPaint)
        //结束区域内的绘制
    }

    private fun drawPathCContentBitmap(bitmap: Bitmap, pathPaint: Paint) {
        val canvas = Canvas(bitmap)

        //绘制区域C内的内容
        canvas.drawPath(getPathDefault(), pathPaint)
        canvas.drawText("CCCCCCC", viewWidth - 260.toFloat(), viewHeight - 100.toFloat(), textPaint)
        //结束区域内的绘制
    }


    private fun createGradientDrawable() {
        var deepColor = 0x33333333
        var lightColor = 0x01333333
        var gradientColors = intArrayOf(lightColor, deepColor)//渐变颜色数组
        drawableLeftTopRight = GradientDrawable(GradientDrawable.Orientation.LEFT_RIGHT, gradientColors)
        drawableLeftTopRight!!.setGradientType(GradientDrawable.LINEAR_GRADIENT)
        drawableLeftLowerRight = GradientDrawable(GradientDrawable.Orientation.RIGHT_LEFT, gradientColors)
        drawableLeftLowerRight!!.setGradientType(GradientDrawable.LINEAR_GRADIENT)
        deepColor = 0x22333333
        lightColor = 0x01333333
        gradientColors = intArrayOf(deepColor, lightColor, lightColor)
        drawableRightTopRight = GradientDrawable(GradientDrawable.Orientation.BOTTOM_TOP, gradientColors)
        drawableRightTopRight!!.setGradientType(GradientDrawable.LINEAR_GRADIENT)
        drawableRightLowerRight = GradientDrawable(GradientDrawable.Orientation.TOP_BOTTOM, gradientColors)
        drawableRightLowerRight!!.setGradientType(GradientDrawable.LINEAR_GRADIENT)
        deepColor = 0x44333333
        lightColor = 0x01333333
        gradientColors = intArrayOf(lightColor, deepColor)//渐变颜色数
        drawableHorizontalLowerRigh = GradientDrawable(GradientDrawable.Orientation.LEFT_RIGHT, gradientColors);
        drawableHorizontalLowerRigh!!.setGradientType(GradientDrawable.LINEAR_GRADIENT)
        deepColor = 0x55111111
        lightColor = 0x00111111
        gradientColors = intArrayOf(deepColor, lightColor)//渐变颜色数
        drawableBTopRight = GradientDrawable(GradientDrawable.Orientation.LEFT_RIGHT, gradientColors)
        drawableBTopRight!!.setGradientType(GradientDrawable.LINEAR_GRADIENT);//线性渐
        drawableBLowerRight = GradientDrawable(GradientDrawable.Orientation.RIGHT_LEFT, gradientColors)
        drawableBLowerRight!!.setGradientType(GradientDrawable.LINEAR_GRADIENT)
        deepColor = 0x55333333
        lightColor = 0x00333333
        gradientColors = intArrayOf(lightColor, deepColor)//渐变颜色数
        drawableCTopRight = GradientDrawable(GradientDrawable.Orientation.LEFT_RIGHT, gradientColors)
        drawableCTopRight!!.setGradientType(GradientDrawable.LINEAR_GRADIENT)
        drawableCLowerRight = GradientDrawable(GradientDrawable.Orientation.RIGHT_LEFT, gradientColors)
        drawableCLowerRight!!.setGradientType(GradientDrawable.LINEAR_GRADIENT)


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

    @SuppressLint("ClickableViewAccessibility")
    override fun onTouchEvent(event: MotionEvent?): Boolean {
        super.onTouchEvent(event)
        when (event!!.action) {
            MotionEvent.ACTION_DOWN -> {
                var x = event.getX()
                val y = event.getY()
                if (x <= viewWidth / 3) { //触摸在左边
                    mStyle = STYLE_LEFT
                    setTouchPoint(x, y, mStyle!!)
                } else if (x > viewWidth / 3 && y <= viewHeight / 3) {//上边触摸
                    mStyle = STYLE_TOP_RIGHT
                    setTouchPoint(x, y, mStyle!!)
                } else if (x > viewWidth * 2 / 3 && y > viewHeight / 3 && y <= viewHeight * 2 / 3) {//在右边触摸
                    mStyle = STYLE_RIGHT
                    setTouchPoint(x, y, mStyle!!)
                } else if (x > viewWidth / 3 && y > viewHeight * 2 / 3) {  //下边触摸
                    mStyle = STYLE_LOWER_RIGHT
                    setTouchPoint(x, y, mStyle!!)
                } else if (x > viewWidth / 3 && x < viewWidth * 2 / 3 && y > viewHeight / 3 && y < viewHeight * 2 / 3) { //中部触摸
                    mStyle = STYLE_MIDDLE
                }
            }
            MotionEvent.ACTION_MOVE -> {
                setTouchPoint(event.getX(), event.getY(), mStyle!!)
            }
            MotionEvent.ACTION_UP -> {
                startCancelAnim()
            }
        }
        return true
    }


    private fun drawPathBContent(bitmapCanvas: Canvas, pathAFromTopRight: Path) {
        bitmapCanvas.save()
        bitmapCanvas.clipPath(pathAFromTopRight)  //裁剪出A区域
        bitmapCanvas.clipPath(getPathC(), Region.Op.UNION) //裁剪出A和C区域的全集
        bitmapCanvas.clipPath(getpathB(), Region.Op.REVERSE_DIFFERENCE) //裁剪出B区域中不同于与AC区域的部分
        bitmapCanvas.drawBitmap(pathBContentBitmap!!, 0f, 0f, null)
        drawPathBShadow(bitmapCanvas)
        bitmapCanvas.restore()
    }

    private fun drawPathBShadow(bitmapCanvas: Canvas) {  //绘制B区域阴影
        val deepOffset = 0f //深色端的偏移值
        val lightOffset = 0f
        val aTof = Math.hypot((a?.x!!.toDouble() - f?.x!!.toDouble()), (a?.y!!.toDouble() - f?.y!!.toDouble())).toFloat()
        val viewDiagonalLength = Math.hypot(viewWidth.toDouble(), viewHeight.toDouble()).toFloat()

        var left = 0
        var right = 0
        val top = c?.y?.toInt()!!
        val bottom = (viewDiagonalLength + c?.y!!).toInt()
        var gradientDrawable: GradientDrawable? = null
        if (mStyle.equals(STYLE_TOP_RIGHT)) {
            gradientDrawable = drawableBTopRight
            left = (c?.x!! - deepOffset).toInt()
            right = (c?.x!! + aTof / 4 + lightOffset).toInt()
        } else {
            gradientDrawable = drawableBLowerRight
            left = (c?.x!! - aTof / 4 - lightOffset).toInt()
            right = (c?.x!! + deepOffset).toInt()
        }
        gradientDrawable!!.setBounds(left, top, right, bottom)  //设置阴影矩形
        val rotateDegrees = Math.toDegrees(Math.atan2(e?.x!!.toDouble() - f?.x!!, h?.y!!.toDouble() - f?.y!!)) //旋转角度
        bitmapCanvas.rotate(rotateDegrees.toFloat(), c?.x!!, c?.y!!)
        gradientDrawable.draw(bitmapCanvas)
    }


    private fun drawPathCContent(bitmapCanvas: Canvas, pathAFromTopRight: Path) {
        bitmapCanvas.save()
        bitmapCanvas.clipPath(pathAFromTopRight)
        bitmapCanvas.clipPath(getPathC(), Region.Op.REVERSE_DIFFERENCE)
        bitmapCanvas.drawPath(getPathC(), pathCPaint)

        val eh: Float = Math.hypot((f?.x!! - e!!.x).toDouble(), (h!!.y - f!!.y).toDouble()).toFloat()
        val sin0 = (f!!.x - e!!.x) / eh
        val cos0 = (h!!.y - f!!.y) / eh

        //设置翻转和旋转矩阵
        mMatrixArray[0] = -(1 - (2 * sin0 * sin0))
        mMatrixArray[1] = 2 * sin0 * cos0
        mMatrixArray[3] = 2 * sin0 * cos0
        mMatrixArray[4] = 1 - 2 * sin0 * sin0

        mMatrix?.reset()
        mMatrix?.setValues(mMatrixArray) //翻转和旋转
        mMatrix?.preTranslate(-e?.x!!, -e?.y!!)
        mMatrix?.postTranslate(e?.x!!, e?.y!!)
        bitmapCanvas.drawBitmap(pathCContentBitmap, mMatrix, null)

        drawPathCShadow(bitmapCanvas)
        bitmapCanvas.restore()
    }

    /**
     * 绘制A区域内容
     * @param canvas
     * @param pathA
     * @param pathPaint
     */
    private fun drawPathAContent(bitmapCanvas: Canvas, pathDefault: Path) {

        bitmapCanvas.save()
        bitmapCanvas.clipPath(pathDefault, Region.Op.INTERSECT)
        bitmapCanvas.drawBitmap(pathAContentBitmap, 0f, 0f, null)
        if (mStyle.equals(STYLE_LEFT) || mStyle.equals(STYLE_RIGHT)) {//左右水平翻页
            drawPathAHorizontalShadow(bitmapCanvas, pathDefault)
        } else {//上下翻页
            drawPathALeftShadow(bitmapCanvas, pathDefault)
            drawPathARightShadow(bitmapCanvas, pathDefault)
        }
        bitmapCanvas.restore()

    }

    private fun drawPathAHorizontalShadow(bitmapCanvas: Canvas, pathA: Path) {
        bitmapCanvas.restore()
        bitmapCanvas.save()
        bitmapCanvas.clipPath(pathA, Region.Op.INTERSECT)
        val maxShadowWidth = 30

        val left = (a?.x!! - Math.min(maxShadowWidth, (mRPathAShadowDis!!.toInt().shr(1)))).toInt()
        val right = a?.x!!.toInt()
        val top = 0
        val bottom = viewHeight
        val gradientDrawable = drawableHorizontalLowerRigh
        gradientDrawable!!.setBounds(left, top, right, bottom)
        val mDegrees = Math.toDegrees(Math.atan2(f?.x!!.toDouble() - a?.x!!, f?.y!!.toDouble() - h?.y!!))
        bitmapCanvas.rotate(mDegrees.toFloat(), a?.x!!, a?.y!!)
        gradientDrawable.draw(bitmapCanvas)

    }

    /*  /**
     * 绘制A区域右阴影
     * @param canvas
     */
     */
    private fun drawPathARightShadow(bitmapCanvas: Canvas, pathA: Path) {
        bitmapCanvas.restore()
        bitmapCanvas.save()

        val viewDiagonalLength: Float = Math.hypot(viewWidth.toDouble(), viewHeight.toDouble()).toFloat()
        val left = h?.x!!.toInt()
        val right = (h?.x!! + viewDiagonalLength * 10).toInt() // 需要足够长的长度
        var top = 0
        var bottom = 0
        val gradientDrawable: GradientDrawable
        if (mStyle.equals(STYLE_TOP_RIGHT)) {
            gradientDrawable = drawableRightTopRight!!
            top = (h?.y!!.toInt() - mRPathAShadowDis!!.toInt().shr(1))
            bottom = h?.y!!.toInt()
        } else {
            gradientDrawable = drawableRightLowerRight!!
            top = h?.y!!.toInt()
            bottom = h?.y!!.toInt() + mRPathAShadowDis!!.toInt().shr(1)
        }
        //裁剪出我们需要的区域
        gradientDrawable.setBounds(left, top, right, bottom)
        val mPath = Path()
        mPath.moveTo(a?.x!! - Math.max(mRPathAShadowDis!!, mLPathAShadowDis!!).toInt().shr(1), a?.y!!)
        mPath.lineTo(h?.x!!, h?.y!!)
        mPath.lineTo(a?.x!!, a?.y!!)
        mPath.close()
        bitmapCanvas.clipPath(pathA)
        bitmapCanvas.clipPath(mPath, Region.Op.INTERSECT)
        val mDegrees = Math.toDegrees(Math.atan2(a?.y!!.toDouble() - h?.y!!, a?.x!!.toDouble() - h?.x!!))
        bitmapCanvas.rotate(mDegrees.toFloat(), h?.x!!, h?.y!!)
        gradientDrawable.draw(bitmapCanvas)
    }

    private fun drawPathALeftShadow(bitmapCanvas: Canvas, pathA: Path) {
        bitmapCanvas.restore()
        bitmapCanvas.save()

        var left = 0
        var right = 0
        val top = e?.y!!.toInt()
        val bottom = (e?.y!! + viewHeight).toInt()
        val gradientDrawable: GradientDrawable?
        if (mStyle.equals(STYLE_TOP_RIGHT)) {
            gradientDrawable = drawableLeftTopRight
            left = (e?.x!! - mLPathAShadowDis!! / 2).toInt()
            right = e!!.x.toInt()
        } else {
            gradientDrawable = drawableLeftLowerRight
            left = e!!.x.toInt()
            right = (e?.x!! + mLPathAShadowDis!! / 2).toInt()
        }
        val path = Path()
        path.moveTo(a?.x!! - Math.max(mRPathAShadowDis!!, mLPathAShadowDis!!) / 2, a?.y!!)
        path.lineTo(d?.x!!, d?.y!!)
        path.lineTo(e?.x!!, e?.y!!)
        path.lineTo(a?.x!!, a?.y!!)
        path.close()
        bitmapCanvas.clipPath(pathA)
        bitmapCanvas.clipPath(path, Region.Op.INTERSECT)

        val mDegress = Math.toDegrees(Math.atan2(e?.x!!.toDouble() - a?.x!!, a?.y!!.toDouble() - e?.y!!))
        bitmapCanvas.rotate(mDegress.toFloat(), e?.x!!, e?.y!!)

        gradientDrawable!!.setBounds(left, top, right, bottom)
        gradientDrawable.draw(bitmapCanvas)
    }

    private fun drawPathCShadow(bitmapCanvas: Canvas) {
        val deepOffset = 1  //深色端的偏移值
        val lithtOffset = -30  //浅色端的偏移值
        val viewDiagonalLength = Math.hypot(viewWidth.toDouble(), viewHeight.toDouble()).toFloat() //计算对角线长度
        val midpoint_ce = (c?.x!! + e?.x!!).toInt() / 2
        val midpoint_jh = (j?.y!! + h?.y!!).toInt() / 2

        val minDisToControlPoint = Math.min(Math.abs(midpoint_ce - e?.x!!), Math.abs(midpoint_jh - h?.y!!))//中点到控制点的最小值

        var left = 0
        var right = 0
        val top = c?.y?.toInt()
        val bottom = (viewDiagonalLength + c?.y!!).toInt()

        var gradientDrawable: GradientDrawable
        if (mStyle.equals(STYLE_TOP_RIGHT)) {
            //如果从右上角翻页
            gradientDrawable = drawableCTopRight!!
            left = (c?.x!! - lithtOffset).toInt()
            right = (c?.x!! + minDisToControlPoint + deepOffset).toInt()
        } else {
            gradientDrawable = drawableCLowerRight!!
            left = (c?.x!! - minDisToControlPoint - deepOffset).toInt()
            right = (c?.x!! + lithtOffset).toInt()
        }
        gradientDrawable.setBounds(left, top!!, right, bottom)

        val mDegrees = Math.toDegrees(Math.atan2(e?.x!! - f?.x!!.toDouble(), h?.y!! - f?.y!!.toDouble())).toFloat()
        bitmapCanvas.rotate(mDegrees, c?.x!!, c?.y!!)
        gradientDrawable.draw(bitmapCanvas)
    }


    private fun getPathAFromTopRight(): Path {
        pathA?.reset()
        pathA?.lineTo(c?.x!!, c?.y!!) //移动到c点
        pathA?.quadTo(e?.x!!, e?.y!!, b?.x!!, b?.y!!)//从c到b画贝塞尔曲线，控制点为e
        pathA?.lineTo(a?.x!!, a?.y!!) //移动到a点
        pathA?.lineTo(k?.x!!, k?.y!!) //移动到k点
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
        val touchPoint: MyPoint
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
    private fun setDefaultPath() {
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

        val lA = a.y - e!!.y
        val lB = e?.x!! - a.x
        val lC = a.x * e!!.y - e!!.x * a.y
        mLPathAShadowDis = Math.abs(lA * d!!.x + lB * d!!.y + lC) / Math.hypot(lA.toDouble(), lB.toDouble()).toFloat()

        val rA = a.y - h!!.y
        val rB = h?.x!! - a.x
        val rC = a.x * h!!.y - h!!.x * a.y
        mRPathAShadowDis = Math.abs((rA * i?.x!! + rB * i?.y!! + rC) / Math.hypot(rA.toDouble(), rB.toDouble()).toFloat())
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

    private fun startCancelAnim() {
        var dx = 0
        var dy = 0
        if (mStyle.equals(STYLE_TOP_RIGHT)) {
            dx = viewWidth - 1 - a?.x?.toInt()!!
            dy = 1 - a?.y!!.toInt()
        } else {
            dx = viewWidth - 1 - a?.x!!.toInt()
            dy = viewHeight - 1 - a?.y!!.toInt()
        }
        mScroller!!.startScroll(a!!.x.toInt(), a!!.y.toInt(), dx, dy, 400)
    }
}

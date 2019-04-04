package com.example.pieBitMap

import android.content.Context
import android.graphics.Canvas
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
class MyPieView:View{

    constructor(context: Context):super(context)
    constructor(context: Context,attr:AttributeSet?):super(context,attr){
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
    }
}

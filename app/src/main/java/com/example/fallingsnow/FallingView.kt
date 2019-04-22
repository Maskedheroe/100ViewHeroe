package com.example.fallingsnow

import android.content.Context
import android.graphics.Paint
import android.util.AttributeSet
import android.view.View

class FallingView(context: Context?, attrs: AttributeSet?) : View(context, attrs) {

    private val mContext: Context

    private val mAttrs: AttributeSet

    private var mViewWidth: Int? = null
    private var mViewHeight: Int? = null

    private var mTestPaint: Paint? = null
    private var mSonwY: Paint? = null

    companion object {
        private const val DEFAULT_WIDTH = 600  //默认宽度
        private const val DEFAULT_HEIGHT = 1000  //默认高度
        private const val INTERVALTIME = 5 //重绘间隔时间
    }

    init {
        mContext = context!!
        mAttrs = attrs!!
    }

}
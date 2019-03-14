package com.example.app

import android.annotation.SuppressLint
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.MotionEvent
import android.view.View
import android.widget.Button
import android.widget.Toast
import com.example.bookview.BookPageView

class MainActivity : AppCompatActivity() {
    private var bookPageView: BookPageView? = null
    private var style: String? = null

    @SuppressLint("ClickableViewAccessibility")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        bookPageView = findViewById<View>(R.id.view_book_page) as BookPageView
        bookPageView!!.setOnTouchListener(View.OnTouchListener { v, event ->
            when (event.action) {
                MotionEvent.ACTION_DOWN -> {
                    val x = event.x
                    val y = event.y
                    val width = bookPageView!!.getViewWidth()
                    val height = bookPageView!!.getViewHeight()
                    if (x <= width / 3) {//左
                        style = BookPageView!!.STYLE_LEFT
                        //                            Toast.makeText(PageActivity.this,"点击了左部",Toast.LENGTH_SHORT).show();
                        bookPageView!!.setTouchPoint(x, y, style!!)

                    } else if (x > width / 3 && y <= height / 3) {//上
                        style = BookPageView!!.STYLE_TOP_RIGHT
                        //                            Toast.makeText(PageActivity.this,"点击了上部",Toast.LENGTH_SHORT).show();
                        bookPageView!!.setTouchPoint(x, y, style!!)

                    } else if (x > width * 2 / 3 && y > height / 3 && y <= height * 2 / 3) {//右
                        style = BookPageView!!.STYLE_RIGHT
                        //                            Toast.makeText(PageActivity.this,"点击了右部",Toast.LENGTH_SHORT).show();
                        bookPageView!!.setTouchPoint(x, y, style!!)

                    } else if (x > width / 3 && y > height * 2 / 3) {//下
                        style = BookPageView!!.STYLE_LOWER_RIGHT
                        //                            Toast.makeText(PageActivity.this,"点击了下部",Toast.LENGTH_SHORT).show();
                        bookPageView!!.setTouchPoint(x, y, style!!)

                    } else if (x > width / 3 && x < width * 2 / 3 && y > height / 3 && y < height * 2 / 3) {//中
                        style = BookPageView!!.STYLE_MIDDLE
                        //                            Toast.makeText(PageActivity.this,"点击了中部",Toast.LENGTH_SHORT).show();
                        //                            bookPageView.setTouchPoint(x,y,bookPageView.STYLE_MIDDLE);
                    }
                }
                MotionEvent.ACTION_MOVE -> bookPageView!!.setTouchPoint(event.x, event.y, style!!)
                MotionEvent.ACTION_UP -> bookPageView!!.startCancelAnim()
            }
            false
        })
    }
}

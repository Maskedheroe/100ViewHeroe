package com.example.app

import android.annotation.SuppressLint
import android.graphics.Color
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.MotionEvent
import android.view.View
import android.widget.Button
import android.widget.Toast
import com.example.bookview.BookPageView
import com.example.pieBitMap.MyPieView
import android.graphics.Color.parseColor
import com.example.pieBitMap.PieEntry


class MainActivity : AppCompatActivity() {


    private var bookPageView: BookPageView? = null
    private var style: String? = null

    @SuppressLint("ClickableViewAccessibility")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

//        showPieView()
    }

 /*   private fun showPieView() {
        val pieView = findViewById<MyPieView>(R.id.pv_pieView)
        pieView.setColors(createColors())
        pieView.setData(createData())
        bookPageView = findViewById<View>(R.id.view_book_page) as BookPageView
    }


    private fun createData(): ArrayList<PieEntry> {
        val pieLists: ArrayList<PieEntry> = ArrayList()
        pieLists.add(PieEntry(20.00f, "服装"))
        pieLists.add(PieEntry(20.00f, "数码产品"))
        pieLists.add(PieEntry(20.00f, "保健品"))
        pieLists.add(PieEntry(20.00f, "户外运动用品"))
        pieLists.add(PieEntry(20.00f, "其他"))
        return pieLists
    }

    private fun createColors(): ArrayList<Int> {
        val colorLists = ArrayList<Int>()
        colorLists.add(Color.parseColor("#EBBF03"))
        colorLists.add(Color.parseColor("#ff4d4d"))
        colorLists.add(Color.parseColor("#8d5ff5"))
        colorLists.add(Color.parseColor("#41D230"))
        colorLists.add(Color.parseColor("#4FAAFF"))
        return colorLists
    }*/
}

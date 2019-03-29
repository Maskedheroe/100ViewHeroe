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
    }
}

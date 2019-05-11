package com.example.app

import android.annotation.SuppressLint
import android.graphics.Bitmap
import android.graphics.Canvas
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
import android.graphics.Paint
import com.example.fallingsnow.FallObject
import com.example.pieBitMap.PieEntry
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() {


    private var bookPageView: BookPageView? = null
    private var style: String? = null

    @SuppressLint("ClickableViewAccessibility")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val snowPaint = Paint()
        snowPaint.color = Color.WHITE
        snowPaint.style = Paint.Style.FILL
        val bitmap = Bitmap.createBitmap(50, 50, Bitmap.Config.ARGB_8888)
        val bitmapCanvas = Canvas(bitmap)
        bitmapCanvas.drawCircle(25f, 25f, 25f, snowPaint)

        val builder = FallObject.Builder(resources.getDrawable(R.mipmap.ic_snow,null))
        val fallObject = builder
                .setSpeed(10,true)
                .setSize(50,50,true)
                .build()
        fallingView.addFallObject(fallObject,50)  //50个雪球
//        showPieView()

    }
    

    /*//绘制雪球bitmap
snowPaint = new Paint();
snowPaint.setColor(Color.WHITE);
snowPaint.setStyle(Paint.Style.FILL);
bitmap = Bitmap.createBitmap(50, 50, Bitmap.Config.ARGB_8888);
bitmapCanvas = new Canvas(bitmap);
bitmapCanvas.drawCircle(25,25,25,snowPaint);

//初始化一个雪球样式的fallObject
FallObject.Builder builder = new FallObject.Builder(bitmap);
FallObject fallObject = builder
		.setSpeed(10)
		.build();

fallingView = (FallingView) findViewById(R.id.fallingView);
fallingView.addFallObject(fallObject,50);//添加50个雪球对象
*/
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

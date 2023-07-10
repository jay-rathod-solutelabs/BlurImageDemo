package com.solutelabs.blurimagedemo

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.renderscript.Allocation
import android.renderscript.Element
import android.renderscript.RenderScript
import android.renderscript.ScriptIntrinsicBlur
import android.view.View
import android.widget.ImageView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class MainActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView

    private val newsItems = mutableListOf<String>()
    private lateinit var newsAdapter: NewsAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        val viewLayer = findViewById<ImageView>(R.id.viewLayer)
        recyclerView = findViewById(R.id.recyclerView)

        calledRecycleView()

//        val backgroundBitmap = getBitmapFromDrawable(viewLayer.drawable)
//        val blurredBitmap = blurBitmap(backgroundBitmap, applicationContext)
//        viewLayer.setImageBitmap(blurredBitmap)



    }


    fun blurBitmap(bitmap: Bitmap, context: Context): Bitmap {
        val rs = RenderScript.create(context)
        val input = Allocation.createFromBitmap(
            rs,
            bitmap,
            Allocation.MipmapControl.MIPMAP_NONE,
            Allocation.USAGE_SCRIPT
        )
        val output = Allocation.createTyped(rs, input.type)
        val script = ScriptIntrinsicBlur.create(rs, Element.U8_4(rs))
        script.setRadius(25f)
        script.setInput(input)
        script.forEach(output)
        val blurredBitmap = Bitmap.createBitmap(bitmap.width, bitmap.height, bitmap.config)
        output.copyTo(blurredBitmap)
        input.destroy()
        output.destroy()
        script.destroy()
        rs.destroy()

        return blurredBitmap
    }


    private fun calledRecycleView() {
        newsAdapter = NewsAdapter(newsItems)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = newsAdapter
        for (i in 1..10) {
            newsItems.add("News $i")
        }
        newsAdapter.notifyDataSetChanged()
    }
}
package com.example.kotlinview.ui

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Path
import android.util.AttributeSet
import android.view.View
import com.example.kotlinview.R
import com.example.kotlinview.util.UIUtil

class CurveChart(context: Context, attrs: AttributeSet): View(context, attrs) {
    private lateinit var dataList: Array<Float>
    private val curveChartPaint = Paint()
    private val seekBarHeight = resources.getDimension(R.dimen.seekbar_bar_height)
    private val seekBarInterval = resources.getDimension(R.dimen.panel_width) / UIUtil().controlBarCount

    private val calDataHeight = { data: Float ->
        (seekBarHeight - (data * seekBarHeight / 100f)) + resources.getDimension(R.dimen.grid_padding_top)
    }

    private val calDataWidth = { index: Int ->
        index * seekBarInterval + seekBarInterval / 2f
    }

    fun initList(size: Int) {
        dataList = Array(size) { 50f }
        curveChartPaint.strokeWidth = 5f
        curveChartPaint.color = Color.BLACK
        curveChartPaint.style = Paint.Style.STROKE
    }

    fun setData(index: Int, data: Float) {
        if(index < 0 || index >= dataList.size) return

        dataList[index] = data
        invalidate()
    }

    @SuppressLint("DrawAllocation")
    override fun onDraw(canvas: Canvas) {
        val path = Path()

        var (cX, cY, nX, nY) = arrayOf(0f, calDataHeight(dataList[0]), 0f, calDataHeight(dataList[0]))

        path.moveTo(calDataWidth(0), calDataHeight(dataList[0]))
        for( i in 0 until dataList.size - 1){
            cY = nY
            nY = calDataHeight(dataList[i + 1])

            cX = calDataWidth(i)
            nX = calDataWidth(i + 1)

            path.cubicTo(nX - 30f, cY,
                cX + 30f, nY,
                nX, nY)
//            quadTo를 사용하고 싶다면
//            path.quadTo(nX, cY, nX, nY)
        }
        canvas.drawPath(path, curveChartPaint)
    }
}
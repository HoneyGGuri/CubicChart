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
import kotlin.math.abs
import kotlin.math.sign

class CurveChart(context: Context, attrs: AttributeSet) : View(context, attrs) {
    private lateinit var dataList: Array<Float>
    private val curveChartPaint = Paint()
    private val seekBarHeight = resources.getDimension(R.dimen.seekbar_bar_height)
    private val seekBarInterval =
        resources.getDimension(R.dimen.panel_width) / UIUtil().controlBarCount

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
        if (index < 0 || index >= dataList.size) return

        dataList[index] = data
        invalidate()
    }

    private fun slope2(x1: Float, y1: Float, x0: Float, y0: Float, t: Float): Float {
        val h = x1 - x0
        return if (h > 0f) (3f * (y1 - y0) / h - t) / 2f else t
    }

    private fun slope3(
        x2: Float,
        y2: Float,
        x1: Float,
        y1: Float,
        x0: Float,
        y0: Float
    ): Float {
        val h0 = x1 - x0
        val h1 = x2 - x1
        val s0 = (y1 - y0) / h0
        val s1 = (y2 - y1) / h1
        val p = (s0 * h1 + s1 * h0) / (h0 + h1)
        return (sign(s0) + sign(s1)) * minOf(abs(s0), abs(s1), 0.5f * abs(p))
    }

    private fun drawMonotoneCubic(
        x1: Float,
        y1: Float,
        x0: Float,
        y0: Float,
        t1: Float,
        t0: Float,
        path: Path
    ) {
        val dx = (x1 - x0) / 3f
        path.cubicTo(x0 + dx, y0 + dx * t0, x1 - dx, y1 - dx * t1, x1, y1)
    }

    @SuppressLint("DrawAllocation")
    override fun onDraw(canvas: Canvas) {
        val path = Path()

        var (cX, cY) = arrayOf(0f, calDataHeight(dataList[0]))
        var (pX, pY, ppX, ppY) = Array(4) { 0f }
        var (t0, t1, t2) = Array(3) { 0f }

        path.moveTo(calDataWidth(0), calDataHeight(dataList[0]))
        for (i in dataList.indices) {
            cX = calDataWidth(i)
            cY = calDataHeight(dataList[i])

            when (i) {
                0 -> path.moveTo(cX, cY)
                1 -> {}
                2 -> {
                    t1 = slope3(cX, cY, pX, pY, ppX, ppY)
                    drawMonotoneCubic(pX, pY, ppX, ppY, t1, slope2(pX, pY, ppX, ppY, t1), path)
                }
                else -> {
                    t1 = slope3(cX, cY, pX, pY, ppX, ppY)
                    drawMonotoneCubic(pX, pY, ppX, ppY, t1, t0, path)
                }
            }


            ppX = pX
            ppY = pY

            pX = cX
            pY = cY

        }

        drawMonotoneCubic(pX, pY, ppX, ppY, slope2(pX, pY, ppX, ppY, t0), t0, path)
        canvas.drawPath(path, curveChartPaint)
    }
}
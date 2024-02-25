package com.example.kotlinview.ui

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.view.View
import com.example.kotlinview.R
import com.example.kotlinview.util.UIUtil

class GridText: View {
    private var gridTextContext: Context
    private var gridTextPaint: Paint
    private val controlBarHeight = resources.getDimension(R.dimen.seekbar_bar_height)

    constructor(context: Context): super(context) {
        gridTextContext = context
        gridTextPaint = Paint(Paint.ANTI_ALIAS_FLAG)
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        var linePosition: Float
        val paddingTop = resources.getDimension(R.dimen.grid_padding_top) + resources.getDimension(R.dimen.grid_text_padding_top)
        val textWidth = resources.getDimension(R.dimen.grid_text_width) - resources.getDimension(R.dimen.grid_text_padding_width)


        canvas.save()
        gridTextPaint.textSize = 35f
        gridTextPaint.textAlign = Paint.Align.RIGHT

        val maxLineCount = UIUtil().controlBarCount
        for(i in 0..maxLineCount) {
            linePosition = i * controlBarHeight / maxLineCount
            canvas.drawText("${maxLineCount - i}", textWidth, linePosition + paddingTop, gridTextPaint)
        }
        canvas.restore()
    }
}
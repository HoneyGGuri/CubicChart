package com.example.kotlinview.ui

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.view.View
import com.example.kotlinview.R
import com.example.kotlinview.util.UIUtil

// GridLine.kt
class GridLine: View{
    private var gridLineContext: Context
    private var gridLinePaint: Paint

    private val controlBarHeight = resources.getDimension(R.dimen.seekbar_bar_height)
    private val panelWidth = resources.getDimension(R.dimen.panel_width)

    constructor(context: Context): super(context){
        gridLineContext = context
        gridLinePaint = Paint(Paint.ANTI_ALIAS_FLAG)
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        var linePosition: Float

        gridLinePaint.color = Color.GRAY

        for(i in 0..UIUtil().controlBarCount) {
            gridLinePaint.strokeWidth = when(i) { // 선 굵기 설정
                0, UIUtil().controlBarCount -> 5f // 양끝은 굵게
                else -> 2f // 사이 라인들은 적당히
            }
            linePosition = i * controlBarHeight / UIUtil().controlBarCount// 위에서 아래로 그림
            canvas.drawLine(0f, linePosition, panelWidth, linePosition, gridLinePaint)
        }
    }
}

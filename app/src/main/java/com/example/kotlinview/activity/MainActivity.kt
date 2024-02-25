package com.example.kotlinview.activity

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.MotionEvent
import android.view.View
import android.widget.RelativeLayout
import android.widget.SeekBar
import android.widget.SeekBar.OnSeekBarChangeListener
import android.widget.TextView
import com.example.kotlinview.R
import com.example.kotlinview.util.UIUtil
import com.example.kotlinview.databinding.ActivityMainBinding
import com.example.kotlinview.ui.GridLine
import com.example.kotlinview.ui.GridText

class MainActivity : AppCompatActivity() {
    // Variables
    private val tag = "MainActivity"

    private lateinit var activityMainBinding: ActivityMainBinding

    private lateinit var seekBars: Array<SeekBar?> // Seekbar
    private lateinit var seekBarValues: Array<Int?> // SeekBar의 progress값을 저장
    private lateinit var controlBars: Array<View?> // Seekbar + Textview(layout_controlbar)

    private val controlBarId = arrayOf(
        R.id.layout_controlbar1,
        R.id.layout_controlbar2,
        R.id.layout_controlbar3,
        R.id.layout_controlbar4,
        R.id.layout_controlbar5,
        R.id.layout_controlbar6,
        R.id.layout_controlbar7,
        R.id.layout_controlbar8,
        R.id.layout_controlbar9,
        R.id.layout_controlbar10,
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activityMainBinding = ActivityMainBinding.inflate(layoutInflater) // 뷰바인딩
        setContentView(activityMainBinding.root)

        initPanelLayout()
        initEvent()
    }

    /**
     * 패널 관련 변수 및 텍스트 초기화
     */
    private fun initPanelLayout() {
        val layoutChart = activityMainBinding.layoutChart

        controlBars = arrayOfNulls(UIUtil().controlBarCount)
        seekBars = arrayOfNulls(UIUtil().controlBarCount)
        seekBarValues = arrayOfNulls(UIUtil().controlBarCount)

        for (i in 0 until UIUtil().controlBarCount) {
            controlBars[i] = layoutChart.findViewById(controlBarId[i])
            controlBars[i]?.findViewById<TextView>(R.id.xlabel_controlbar)?.text = when(i) {
                0 -> "${i + 1}st"
                1 -> "${i + 1}nd"
                2 -> "${i + 1}rd"
                else -> "${i + 1}th"
            }
            seekBars[i] = controlBars[i]?.findViewById(R.id.seekbar_controlbar)
        }

        val layoutGridLine = layoutChart.findViewById<RelativeLayout>(R.id.layout_grid_line)
        val gridLine = GridLine(this)
        layoutGridLine.addView(gridLine)

        val layoutGridText = layoutChart.findViewById<RelativeLayout>(R.id.layout_grid_text)
        val gridText = GridText(this)
        layoutGridText.addView(gridText)
    }

    @SuppressLint("ClickableViewAccessibility") // setOnTouchListener warning 제거
    private fun initEvent() {
        for( i in 0 until UIUtil().controlBarCount) {
            val seekBarChangeListener = SeekbarChangeListener()

            seekBars[i]?.tag = i
            seekBars[i]?.setOnSeekBarChangeListener(seekBarChangeListener)
            seekBars[i]?.setOnTouchListener{ view: View, event: MotionEvent ->
                when(event.action){
                    MotionEvent.ACTION_DOWN -> {
                        Log.d(tag, "initEvent() : Action down!")
                    }
                    MotionEvent.ACTION_UP -> {
                        view.performClick()
                        Log.d(tag, "initEvent() : Action up!")
                    }
                }
                false// true인 경우 조작되지 않음
            }
        }
    }

    inner class SeekbarChangeListener: OnSeekBarChangeListener {
        override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
            val xValue = seekBar?.tag as Int
            Log.d(tag, "$seekBar.id")
            val yValue = seekBar.progress.toFloat()
            Log.d(tag, "onProgressChanged() : (x, y) = ($xValue, $yValue)")
        }

        override fun onStartTrackingTouch(seekBar: SeekBar?) {
            Log.d(tag, "onStartTrackingTouch() : Tracking start")
        }

        override fun onStopTrackingTouch(seekBar: SeekBar?) {
            Log.d(tag, "onStopTrackingTouch() : Tracking stop")
        }
    }
}
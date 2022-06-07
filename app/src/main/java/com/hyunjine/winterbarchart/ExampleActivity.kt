package com.hyunjine.winterbarchart

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.snackbar.Snackbar
import com.hyunjine.winterbarchart.databinding.ActivityExampleBinding
import com.hyunjine.wtbarchart.WTUnit


public class ExampleActivity : AppCompatActivity() {
    private lateinit var binding: ActivityExampleBinding
    private var count = 60f
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityExampleBinding.inflate(layoutInflater)
        setContentView(binding.root)

        custom()
    }
    private fun custom() {
        binding.run {
            wtItem.setAllItemText(arrayOf("1/1", "1/2", "1/3", "1/4", "1/5", "1/6", "1/7" ))
            for (i in WTUnit.getAll()) {
                wtItem.getItem(i).textSize = 12f
            }
            wtChart.apply {
                setAllChartValue(arrayOf(100f, 50f, 40f, 70f, 80f, 30f, 20f))
                setMaxValue(100f)
                setRecommendValue(60f)
                getRecommendLine().setBackgroundResource(R.drawable.dotted_line)
                getRecommendBox().run {
                    text = "${getRecommendValue().toInt()}h"
                    setTextColor(Color.parseColor("#7C7C7C"))
                    background = ColorDrawable(Color.TRANSPARENT)
                }
                for (i in WTUnit.getAll()) {
                    getUpChart(i).setBackgroundResource(R.drawable.bg_up_chart)
                    if (getRecommendValue() < getChartValue(i)) {
                        getDownChart(i).setBackgroundResource(R.drawable.bg_down_chart)
                    } else {
                        getDownChart(i).setBackgroundResource(R.drawable.bg_down_chart_down)
                    }
                }
                setOnChartClickListener {
                    Snackbar.make(this, "Chart: ${it.name}\nValue: ${getChartValue(it)}", Snackbar.LENGTH_SHORT).show()
                }
            }

        }
    }
}
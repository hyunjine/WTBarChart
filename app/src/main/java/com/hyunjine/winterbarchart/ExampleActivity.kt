package com.hyunjine.winterbarchart

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.content.res.AppCompatResources
import androidx.core.view.setPadding
import com.hyunjine.winterbarchart.databinding.ActivityExampleBinding
import com.hyunjine.wtbarchart.*

class ExampleActivity : AppCompatActivity() {
    private lateinit var binding: ActivityExampleBinding

    private var a  = 3f
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityExampleBinding.inflate(layoutInflater)
        setContentView(binding.root)

        with(binding) {
            wtItem.setAllItemText(arrayOf("M", "T", "W", "T", "I", "T", "N"))
//            wtItem.setAllItemText(arrayOf("월", "화", "수", "목", "금", "토", "일"))
            wtChart.apply {
                setAllChartValue(arrayOf(10f, 5f, 4f, 7f, 8f, 1f, 2f))
                setRecommendValue(3f)
//                onChangeRecommendValue()

            }
//            wtItem.setAllItemText(arrayOf("Mon", "Tue", "Wen", "Thu", "Fri", "Sat", "Sun"))
//
//            wtChart.apply {
//                setAllChartValue(arrayOf(10f, 5f, 4f, 7f, 8f, 1f, 2f))
//                setRecommendValue(3f)
//                onChangeRecommendValue()
//                getRecommendBox().run {
//                    setBackgroundResource(R.drawable.bg_recommend_box)
//                    setPadding(30,30,50,30)
//                    text = "30분"
//                    setTextColor(getColor(R.color.white))
//                }
//
//            }
//            wtChart.getRecommendBox().visibility = View.GONE
//            btn.setOnClickListener {
//                wtChart.apply {
//                    setRecommendValue(++a)
//                    onChangeRecommendValue()
//                }
//            }
//            //typeface = Typeface.createFromAsset(context.assets, "pretendard_m.otf")
//            wtChart.setChartClickListener(object : OnChartClickListener {
//                override fun onChartClick(view: View) {
//                    view.background = AppCompatResources.getDrawable(this@ExampleActivity, R.drawable.bg_up_chart)
//                }
//            })
        }
    }

    private fun onChangeRecommendValue() {
        with(binding.wtChart) {
            for (chart in WTBaseUnit.chartSet) {
                getUpChart(chart).setBackgroundResource(R.drawable.bg_up_chart)
                if (chart.value <= getRecommendValue())
                    getDownChart(chart).setBackgroundResource(R.drawable.bg_down_chart_down)
                else
                    getDownChart(chart).setBackgroundResource(R.drawable.bg_down_chart)
            }
        }
    }

    private fun showToast(text: String) = Toast.makeText(this, text, Toast.LENGTH_LONG).show()
}
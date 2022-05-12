package com.hyunjine.winterbarchart

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.hyunjine.winterbarchart.databinding.ActivityExampleBinding
import com.hyunjine.wtbarchart.ChartItemIdSet
import com.hyunjine.wtbarchart.WTBarChart

class ExampleActivity : AppCompatActivity() {
    private lateinit var binding: ActivityExampleBinding

    private var a  = 5f
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityExampleBinding.inflate(layoutInflater)
        setContentView(binding.root)

        with(binding) {
            wtItem.setAllItemText(arrayOf("월", "화", "수", "목", "금", "토", "일"))
            wtChart.apply {
                setAllChartValue(arrayOf(10f, 5f, 4f, 7f, 8f, 1f, 2f))
                setRecommendValue(5f)
//                setDownChartBackground(ChartItemIdSet.COMPONENT1, R.drawable.bg_down_chart)
//                setUpChartBackground(ChartItemIdSet.COMPONENT1, R.drawable.bg_up_chart)
//                setRecommendBoxBackground(R.drawable.bg_recommend)
//                setRecommendLineBackground(R.drawable.dotted_line)
            }
            btn.setOnClickListener {
                wtChart.setChartValue(ChartItemIdSet.COMPONENT3, a++)
            }
            wtChart.setChartClickListener {
                showToast(it.name)
            }
        }
    }

    private fun showToast(text: String) = Toast.makeText(this, text, Toast.LENGTH_LONG).show()
}
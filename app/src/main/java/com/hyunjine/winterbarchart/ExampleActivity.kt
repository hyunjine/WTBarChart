package com.hyunjine.winterbarchart

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.content.res.AppCompatResources
import com.hyunjine.winterbarchart.databinding.ActivityExampleBinding
import com.hyunjine.wtbarchart.*

class ExampleActivity : AppCompatActivity() {
    private lateinit var binding: ActivityExampleBinding

    private var a  = 5f
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityExampleBinding.inflate(layoutInflater)
        setContentView(binding.root)

        with(binding) {
            wtItem.setAllItemText(arrayOf("월", "화", "수", "목", "금", "토", "일"))
            wtItem.setItemText(ItemSet.COMPONENT1, "섹")
            wtChart.apply {
                setAllChartValue(arrayOf(10f, 5f, 4f, 7f, 8f, 1f, 2f))
                setRecommendValue(3f)
//                setUpChartBackground(ChartItemIdSet.COMPONENT1, R.drawable.bg_up_chart)
//                setRecommendBoxBackground(R.drawable.bg_recommend)
//                setRecommendLineBackground(R.drawable.dotted_line_user)
            }
            btn.setOnClickListener {
                wtChart.apply {
                    setChartValue(ChartSet.COMPONENT3, a++)
                    downChart(ChartSet.COMPONENT1).setBackgroundResource(R.drawable.bg_up_chart)
                    recommendText.text = "gdgd"
                }
            }
            wtChart.setChartClickListener(object : OnChartClickListener {
                override fun onChartClick(view: View) {
                    view.background = AppCompatResources.getDrawable(this@ExampleActivity, R.drawable.bg_up_chart)
                }
            })
        }
    }

    private fun showToast(text: String) = Toast.makeText(this, text, Toast.LENGTH_LONG).show()
}
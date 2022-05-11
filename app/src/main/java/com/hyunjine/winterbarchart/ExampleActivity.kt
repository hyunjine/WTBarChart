package com.hyunjine.winterbarchart

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.hyunjine.winterbarchart.databinding.ActivityExampleBinding

class ExampleActivity : AppCompatActivity() {
    private lateinit var binding: ActivityExampleBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityExampleBinding.inflate(layoutInflater)
        setContentView(binding.root)

        with(binding) {
            wtItem.setAllItemText(arrayOf("월", "화", "수", "목", "금", "토", "일"))
            wtChart.apply {
                setAllChartValue(arrayOf(10f, 5f, 4f, 7f, 8f, 1f, 2f))
                setRecommendValue(5f)
            }
        }
    }
}
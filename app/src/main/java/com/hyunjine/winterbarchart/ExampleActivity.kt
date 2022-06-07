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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityExampleBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.run {
            wtItem.setAllItemText(arrayOf("Mon", "Tue", "Wed", "Thu", "Fri", "Sat", "Sun"))
            wtChart.apply {
                setAllChartValue(arrayOf(100f, 50f, 40f, 70f, 80f, 30f, 20f))
                setMaxValue(48f)
                setRecommendValue(15f)
                recommendBox.text = "${getRecommendValue().toInt()}m"
            }
        }
    }
}
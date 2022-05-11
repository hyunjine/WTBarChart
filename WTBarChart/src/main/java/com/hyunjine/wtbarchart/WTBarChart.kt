package com.hyunjine.wtbarchart

import android.content.Context
import android.graphics.Typeface
import android.util.AttributeSet
import android.util.Log
import android.util.TypedValue
import android.view.Gravity
import android.view.View
import android.widget.TextView
import androidx.appcompat.content.res.AppCompatResources

class WTBarChart : WTBaseUnit {
    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context, attrs: AttributeSet?, defStyle: Int) : super(
        context,
        attrs,
        defStyle
    )

    private var maxValue: Float = DEFAULT_MAX_VALUE
    private var recommendValue: Float = DEFAULT_RECOMMEND_VALUE
    private val chartData = HashMap<ChartItemIdSet, WTChartData>().apply {
        put(ChartItemIdSet.COMPONENT1, WTChartData(0f, R.id.down_chart1, R.id.up_chart1))
        put(ChartItemIdSet.COMPONENT2, WTChartData(0f, R.id.down_chart2, R.id.up_chart2))
        put(ChartItemIdSet.COMPONENT3, WTChartData(0f, R.id.down_chart3, R.id.up_chart3))
        put(ChartItemIdSet.COMPONENT4, WTChartData(0f, R.id.down_chart4, R.id.up_chart4))
        put(ChartItemIdSet.COMPONENT5, WTChartData(0f, R.id.down_chart5, R.id.up_chart5))
        put(ChartItemIdSet.COMPONENT6, WTChartData(0f, R.id.down_chart6, R.id.up_chart6))
        put(ChartItemIdSet.COMPONENT7, WTChartData(0f, R.id.down_chart7, R.id.up_chart7))
    }
    private val recommendLineWidth: Int by lazy {
        TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP,
            3f,
            resources.displayMetrics
        ).toInt()
    }

    init {
        addGuideLine()
        for (chart in chartData) {
            makeChart(View(context), chart.value.downChartId, R.color.bar_chart_down)
            makeChart(View(context), chart.value.upChartId, R.color.bar_chart_up)
        }
        makeRecommendLine(View(context))
        makeRecommendBox(TextView(context))
    }

    private fun makeChart(view: View, chartId: Int, colorId: Int) {
        with(view) {
            id = chartId
            layoutParams = LayoutParams(0 , 0)
            setBackgroundColor(context.getColor(colorId))
            addView(this)
        }
    }

    private fun makeRecommendLine(view: View) {
        with(view) {
            id = R.id.recommend_line
            background = AppCompatResources.getDrawable(context, R.drawable.dotted_line)
            layoutParams = LayoutParams(0, recommendLineWidth).apply {
                topToTop = LayoutParams.PARENT_ID
                bottomToBottom = LayoutParams.PARENT_ID
                startToEnd = R.id.recommend_box
                verticalBias = 1f
            }
            addView(this)
        }
    }

    private fun makeRecommendBox(view: TextView) {
        with(view) {
            id = R.id.recommend_box
            text = context.getString(R.string.recommend_text, recommendValue.toInt())
            textSize = RECOMMEND_TEXT_SIZE
            gravity = Gravity.CENTER
            typeface = Typeface.createFromAsset(context.assets, "pretendard_m.otf")
            setTextColor(context.getColor(R.color.recommend_text))
            background = AppCompatResources.getDrawable(context, R.drawable.bg_recommend)
            layoutParams = LayoutParams(0, LayoutParams.WRAP_CONTENT).apply {
                bottomToBottom = LayoutParams.PARENT_ID
                startToStart = LayoutParams.PARENT_ID
                matchConstraintPercentWidth = RECOMMEND_BOX_WIDTH / WHOLE_WIDTH
            }
            addView(this)
        }
    }

    fun setMaxValue(value: Float) {
        maxValue = checkNegative(value) ?: return
        changeAll(value, recommendValue)
    }

    fun setRecommendValue(value: Float) {
        recommendValue = checkNegative(value) ?: return
        changeAll(maxValue, value)
    }

    private fun changeAll(maxValue: Float, recommendValue: Float) {
        changeRecommendLine(maxValue, recommendValue)
        changeRecommendBox()
        for(data in chartData) {
            changeChart(data.key, data.value.chartValue)
        }
    }

    fun setAllChartValue(list: Array<Float>) {
        try {
            for ((idx, value) in list.withIndex()) {
                val id = when(idx) {
                    0 -> ChartItemIdSet.COMPONENT1
                    1 -> ChartItemIdSet.COMPONENT2
                    2 -> ChartItemIdSet.COMPONENT3
                    3 -> ChartItemIdSet.COMPONENT4
                    4 -> ChartItemIdSet.COMPONENT5
                    5 -> ChartItemIdSet.COMPONENT6
                    6 -> ChartItemIdSet.COMPONENT7
                    else -> throw ArrayIndexOutOfBoundsException()
                }
                setChartValue(id, checkNegative(value, id.name) ?: continue)
            }
        } catch (e: ArrayIndexOutOfBoundsException) {
            Log.e(TAG, "Array of value size must be until 7")
        }
    }

    fun setChartValue(chartId: ChartItemIdSet, value: Float) {
        chartData[chartId]?.chartValue = checkNegative(value) ?: return
        changeChart(chartId, value)
    }

    private fun changeRecommendLine(maxValue: Float, recommendValue: Float) {
        val verticalBias = when {
            recommendValue >= maxValue ->
                1f - (MAX_CHART_HEIGHT / WHOLE_HEIGHT)
            recommendValue <= 0 ->
                1f
            else ->
                1f - ((MAX_CHART_HEIGHT / WHOLE_HEIGHT)*(recommendValue / maxValue))
        }
        getView<View>(R.id.recommend_line).layoutParams = LayoutParams(0, recommendLineWidth).apply {
            topToTop = LayoutParams.PARENT_ID
            bottomToBottom = LayoutParams.PARENT_ID
            startToEnd = R.id.recommend_box
            this.verticalBias = verticalBias
        }
    }

    private fun changeRecommendBox() {
        with(getView<TextView>(R.id.recommend_box)) {
            text = context.getString(R.string.recommend_text, recommendValue.toInt())
            layoutParams = LayoutParams(0, LayoutParams.WRAP_CONTENT).apply {
                if (recommendValue <= 0) {
                    bottomToBottom = LayoutParams.PARENT_ID
                } else {
                    topToTop = R.id.recommend_line
                    bottomToBottom = R.id.recommend_line
                }
                startToStart = LayoutParams.PARENT_ID
                matchConstraintPercentWidth = RECOMMEND_BOX_WIDTH / WHOLE_WIDTH
            }
        }
    }

    private fun changeChart(id: ChartItemIdSet, chartValue: Float) {
        val pair = when {
            chartValue > recommendValue && chartValue < maxValue ->
                Pair(changeToRatio(recommendValue), changeToRatio((chartValue - recommendValue)))
            chartValue > recommendValue && chartValue >= maxValue ->
                Pair(changeToRatio(recommendValue), changeToRatio((maxValue - recommendValue)))
            chartValue <= recommendValue && chartValue <= maxValue ->
                Pair(changeToRatio(chartValue), 0f)
            else -> return
        }
        chartData[id]?.let { data ->
            setChartLayoutParam(data.downChartId, true, id, pair.first)
            setChartLayoutParam(data.upChartId, false, id, pair.second)
        }
    }

    private fun setChartLayoutParam(viewId: Int, isDownChart: Boolean, id: ChartItemIdSet, value: Float) {
        getView<View>(viewId).layoutParams = getChartLayoutParam(isDownChart, id, value)
    }

    private fun getChartLayoutParam(isDownChart: Boolean, id: ChartItemIdSet, value: Float): LayoutParams {
        return LayoutParams(0, 0).apply {
            matchConstraintPercentWidth = CHART_WIDTH / WHOLE_WIDTH
            matchConstraintPercentHeight = value
            chartData[id]?.let { data ->
                if (!isDownChart) bottomToTop = data.downChartId
                else bottomToBottom = LayoutParams.PARENT_ID
                if (id == ChartItemIdSet.COMPONENT7 || id == ChartItemIdSet.COMPONENT1) {
                    startToStart = getIdsForChart(id).first
                    endToEnd = getIdsForChart(id).second
                } else {
                    startToEnd = getIdsForChart(id).first
                    endToStart = getIdsForChart(id).second
                }
            }
        }
    }

    private fun getIdsForChart(id: ChartItemIdSet): Pair<Int, Int> {
        return when (id) {
            ChartItemIdSet.COMPONENT1 -> Pair(R.id.start_guide, R.id.start_guide)
            ChartItemIdSet.COMPONENT7 -> Pair(R.id.end_guide, R.id.end_guide)
            ChartItemIdSet.COMPONENT2 -> Pair(R.id.down_chart1, R.id.down_chart3)
            ChartItemIdSet.COMPONENT3 -> Pair(R.id.down_chart2, R.id.down_chart4)
            ChartItemIdSet.COMPONENT4 -> Pair(R.id.down_chart3, R.id.down_chart5)
            ChartItemIdSet.COMPONENT5 -> Pair(R.id.down_chart4, R.id.down_chart6)
            ChartItemIdSet.COMPONENT6 -> Pair(R.id.down_chart5, R.id.down_chart7)
        }
    }

    private fun checkNegative(num: Float): Float? {
        try {
            if(num.isNegative()) throw Exception("Can not put negative number in value")
            else return num
        } catch (e: Exception) {
            Log.e(TAG, "checkNegative: ${e.message}")
        }
        return null
    }

    private fun checkNegative(num: Float, message: String): Float? {
        try {
            if(num.isNegative()) throw Exception("Can not put negative number in value $message")
            else return num
        } catch (e: Exception) {
            Log.e(TAG, "checkNegative: ${e.message}")
        }
        return null
    }

    private fun changeToRatio(value: Float): Float = value * (MAX_CHART_HEIGHT / WHOLE_HEIGHT) / maxValue

    fun setDownChartColor(id: ChartItemIdSet, colorId: Int) =
        chartData[id]?.let { getView<View>(it.downChartId).setBackgroundColor(colorId) }

    fun setUpChartColor(id: ChartItemIdSet, colorId: Int) =
        chartData[id]?.let { getView<View>(it.upChartId).setBackgroundColor(colorId) }

    fun setRecommendBoxBackground(drawableId: Int) =
        AppCompatResources.getDrawable(context, drawableId)
            .also { getView<View>(R.id.recommend_box).background = it }
}
package com.hyunjine.wtbarchart

import android.content.Context
import android.graphics.Color
import android.graphics.Typeface
import android.util.AttributeSet
import android.util.Log
import android.util.TypedValue
import android.view.Gravity
import android.view.View
import android.widget.TextView
import androidx.appcompat.content.res.AppCompatResources
import androidx.core.view.marginEnd

class WTBarChart : WTBaseUnit {

    private lateinit var listener: ((View) -> Unit)
    private lateinit var l: OnChartClickListener
    fun setChartClickListener(l: ((View) -> Unit)) {
        this.listener = l
    }
    fun setChartClickListener(l: OnChartClickListener) {
        this.l = l
    }
    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context, attrs: AttributeSet?, defStyle: Int) : super(
        context,
        attrs,
        defStyle
    )

    private var maxValue: Float = DEFAULT_MAX_VALUE
    private var recommendValue: Float = DEFAULT_RECOMMEND_VALUE

    private val chartList: Array<ChartSet> = enumValues()
    private val recommendLineWidth: Int by lazy {
        TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP,
            3f,
            resources.displayMetrics
        ).toInt()
    }

    init {
        addGuideLine()
        for (chart in chartList) {
            makeChart(View(context), chart, chart.downId, R.color.bar_chart_down)
            makeChart(View(context), chart, chart.upId, R.color.bar_chart_up)
        }
        makeRecommendLine(View(context))
        makeRecommendBox(TextView(context))
    }

    private fun makeChart(view: View, chart: ChartSet, viewId: Int, colorId: Int) {
        with(view) {
            id = viewId
            setBackgroundColor(context.getColor(colorId))
            layoutParams = LayoutParams(0, 0)
            addView(this)
            setOnClickListener {
                if (::listener.isInitialized) listener(this)
                if (::l.isInitialized) l.onChartClick(this)
            }
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
//            background = AppCompatResources.getDrawable(context, R.drawable.bg_recommend)
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
        for(chart in chartList) changeChart(chart, chart.value)
    }

    fun setAllChartValue(list: Array<Float>) {
        try {
            for ((idx, value) in list.withIndex()) {
                val chart = chartList[idx]
                setChartValue(chart, checkNegative(value, chart.name) ?: continue)
            }
        } catch (e: ArrayIndexOutOfBoundsException) {
            Log.e(TAG, "Array of value size must be until 7")
        }
    }

    fun setChartValue(chart: ChartSet, value: Float) {
        chart.value = checkNegative(value) ?: return
        changeChart(chart, value)
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
            layoutParams = LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT).apply {
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

    private fun changeChart(chart: ChartSet, chartValue: Float) {
        val pair = when {
            chartValue > recommendValue && chartValue < maxValue ->
                Pair(changeToRatio(recommendValue), changeToRatio((chartValue - recommendValue)))
            chartValue > recommendValue && chartValue >= maxValue ->
                Pair(changeToRatio(recommendValue), changeToRatio((maxValue - recommendValue)))
            chartValue <= recommendValue && chartValue <= maxValue ->
                Pair(changeToRatio(chartValue), 0f)
            else -> return
        }
        chart.apply {
            setChartLayoutParam(downId, true, this, pair.first)
            setChartLayoutParam(upId, false, this, pair.second)
        }
    }

    private fun setChartLayoutParam(viewId: Int, isDownChart: Boolean, chart: ChartSet, value: Float) {
        getView<View>(viewId).layoutParams = getChartLayoutParam(isDownChart, chart, value)
    }

    private fun getChartLayoutParam(isDownChart: Boolean, chart: ChartSet, value: Float): LayoutParams =
        LayoutParams(0, 0).apply {
            matchConstraintPercentWidth = CHART_WIDTH / WHOLE_WIDTH
            matchConstraintPercentHeight = value
            chart.apply {
                if (!isDownChart) bottomToTop = downId
                else bottomToBottom = LayoutParams.PARENT_ID
                if (chart == ChartSet.COMPONENT1 || chart == ChartSet.COMPONENT7) {
                    startToStart = getIdsForChart(chart).first
                    endToEnd = getIdsForChart(chart).second
                } else {
                    startToEnd = getIdsForChart(chart).first
                    endToStart = getIdsForChart(chart).second
                }
            }
        }

    private fun getIdsForChart(chart: ChartSet): Pair<Int, Int> {
        return when (chart) {
            ChartSet.COMPONENT1 -> Pair(R.id.start_guide, R.id.start_guide)
            ChartSet.COMPONENT7 -> Pair(R.id.end_guide, R.id.end_guide)
            ChartSet.COMPONENT2 -> Pair(R.id.down_chart1, R.id.down_chart3)
            ChartSet.COMPONENT3 -> Pair(R.id.down_chart2, R.id.down_chart4)
            ChartSet.COMPONENT4 -> Pair(R.id.down_chart3, R.id.down_chart5)
            ChartSet.COMPONENT5 -> Pair(R.id.down_chart4, R.id.down_chart6)
            ChartSet.COMPONENT6 -> Pair(R.id.down_chart5, R.id.down_chart7)
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

    /**
     * @param ChartSet it is a view that you will use under recommend line
     */
    fun getDownChart(chart: ChartSet): View = getView(chart.downId)

    /**
     * @param - it a view that you will use above recommend line
     */
    fun getUpChart(chart: ChartSet): View = getView(chart.upId)

    fun getRecommendBox(): TextView = getView(R.id.recommend_box)
    fun getRecommendLine(): TextView = getView(R.id.recommend_line)
    fun getRecommendValue(): Float = recommendValue
}
package com.hyunjine.wtbarchart

import android.content.Context
import android.util.AttributeSet
import android.util.Log
import android.util.TypedValue
import android.view.Gravity
import android.view.View
import android.widget.TextView
import androidx.appcompat.content.res.AppCompatResources

public class WTBarChart : WTBase {

    private lateinit var listener: ((WTUnit) -> Unit)
    private lateinit var l: OnChartClickListener

    public fun setOnChartClickListener(l: ((WTUnit) -> Unit)) {
        this.listener = l
    }
    public fun setOnChartClickListener(l: OnChartClickListener) {
        this.l = l
    }
    public constructor(context: Context) : super(context)
    public constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)
    public constructor(context: Context, attrs: AttributeSet?, defStyle: Int) : super(
        context,
        attrs,
        defStyle
    )

    private var maxValue: Float = DEFAULT_MAX_VALUE
    private var recommendValue: Float = DEFAULT_RECOMMEND_VALUE
    private var chartWidth: Float = CHART_WIDTH

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
            makeChart(View(context), chart, chart.downId, R.color.wtbar_bar_chart_down)
            makeChart(View(context), chart, chart.upId, R.color.wtbar_bar_chart_up)
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
                if (::listener.isInitialized) listener(matchChartSetToWTUnit(chart))
                if (::l.isInitialized) l.onChartClick(matchChartSetToWTUnit(chart))
            }
        }
    }

    private fun makeRecommendLine(view: View) {
        with(view) {
            id = R.id.wtbar_recommend_line
            background = AppCompatResources.getDrawable(context, R.drawable.wtbar_dotted_line)
            layoutParams = LayoutParams(0, recommendLineWidth).apply {
                topToTop = LayoutParams.PARENT_ID
                bottomToBottom = LayoutParams.PARENT_ID
                startToEnd = R.id.wtbar_recommend_box
                verticalBias = 1f
            }
            addView(this)
        }
    }

    private fun makeRecommendBox(view: TextView) {
        with(view) {
            id = R.id.wtbar_recommend_box
            text = recommendValue.toInt().toString()
            textSize = RECOMMEND_TEXT_SIZE
            gravity = Gravity.CENTER
            setTextColor(context.getColor(R.color.wtbar_recommend_text))
            setBackgroundResource(R.drawable.wtbar_bg_recommend)
            setPadding(
                RECOMMEND_BOX_MARGIN_LEFT,
                RECOMMEND_BOX_MARGIN_TOP,
                RECOMMEND_BOX_MARGIN_RIGHT,
                RECOMMEND_BOX_MARGIN_BOTTOM
            )
            layoutParams = LayoutParams(0, LayoutParams.WRAP_CONTENT).apply {
                bottomToBottom = LayoutParams.PARENT_ID
                startToStart = LayoutParams.PARENT_ID
            }
            addView(this)
        }
    }

    private fun changeAll(maxValue: Float, recommendValue: Float) {
        changeRecommendLine(maxValue, recommendValue)
        changeRecommendBox()
        for(chart in chartList) changeChart(chart, chart.value)
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
        getView<View>(R.id.wtbar_recommend_line).layoutParams = LayoutParams(0, recommendLineWidth).apply {
            topToTop = LayoutParams.PARENT_ID
            bottomToBottom = LayoutParams.PARENT_ID
            startToEnd = R.id.wtbar_recommend_box
            this.verticalBias = verticalBias
        }
    }

    private fun changeRecommendBox() {
        with(getView<TextView>(R.id.wtbar_recommend_box)) {
            text = recommendValue.toInt().toString()
            layoutParams = LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT).apply {
                if (recommendValue <= 0 || recommendValue / maxValue < 0.05f) {
                    bottomToBottom = LayoutParams.PARENT_ID
                } else {
                    topToTop = R.id.wtbar_recommend_line
                    bottomToBottom = R.id.wtbar_recommend_line
                }
                startToStart = LayoutParams.PARENT_ID
            }
        }
    }

    private fun changeChart(chart: ChartSet, chartValue: Float) {
        val (downValue, upValue) = when {
            chartValue > recommendValue && chartValue < maxValue ->
                Pair(changeToRatio(recommendValue), changeToRatio((chartValue - recommendValue)))
            chartValue > recommendValue && chartValue >= maxValue && recommendValue < maxValue ->
                Pair(changeToRatio(recommendValue), changeToRatio((maxValue - recommendValue)))
            chartValue > recommendValue && chartValue >= maxValue && recommendValue >= maxValue ->
                Pair(changeToRatio(maxValue), changeToRatio((0f)))
            chartValue <= recommendValue && chartValue < maxValue ->
                Pair(changeToRatio(chartValue), 0f)
            chartValue in maxValue..recommendValue ->
                Pair(changeToRatio(maxValue), 0f)
            else -> return
        }
        chart.apply {
            setChartLayoutParam(downId, true, this, downValue)
            setChartLayoutParam(upId, false, this, upValue)
        }
    }

    private fun setChartLayoutParam(viewId: Int, isDownChart: Boolean, chart: ChartSet, value: Float) {
        getView<View>(viewId).layoutParams = getChartLayoutParam(isDownChart, chart, value)
    }

    private fun getChartLayoutParam(isDownChart: Boolean, chart: ChartSet, value: Float): LayoutParams =
        LayoutParams(0, 0).apply {
            matchConstraintPercentWidth = chartWidth / WHOLE_WIDTH
            matchConstraintPercentHeight = value
            horizontalChainStyle = LayoutParams.CHAIN_SPREAD
            chart.apply {
                if (!isDownChart) bottomToTop = downId
                else bottomToBottom = LayoutParams.PARENT_ID
                val guideline = getIdsForChart(chart)
                startToStart = guideline
                endToEnd = guideline
            }
        }

    private fun getIdsForChart(chart: ChartSet): Int {
        return when (chart) {
            ChartSet.COMPONENT1 -> R.id.wtbar_start_guide
            ChartSet.COMPONENT7 -> R.id.wtbar_end_guide
            ChartSet.COMPONENT2 -> R.id.wtbar_guide_line_2
            ChartSet.COMPONENT3 -> R.id.wtbar_guide_line_3
            ChartSet.COMPONENT4 -> R.id.wtbar_guide_line_4
            ChartSet.COMPONENT5 -> R.id.wtbar_guide_line_5
            ChartSet.COMPONENT6 -> R.id.wtbar_guide_line_6
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

    public fun setMaxValue(value: Float) {
        maxValue = checkNegative(value) ?: return
        changeAll(value, recommendValue)
    }

    public fun setRecommendValue(value: Float) {
        recommendValue = checkNegative(value) ?: return
        changeAll(maxValue, value)
    }

    public fun setChartValue(unit: WTUnit, value: Float) {
        matchWTUnitToChartSet(unit).value = checkNegative(value) ?: return
        changeChart(matchWTUnitToChartSet(unit), value)
    }

    private fun setChartValue(chart: ChartSet, value: Float) {
        chart.value = checkNegative(value) ?: return
        changeChart(chart, value)
    }

    public fun setAllChartValue(list: Array<Float>) {
        try {
            for ((idx, value) in list.withIndex()) {
                val chart = chartList[idx]
                setChartValue(chart, checkNegative(value, chart.name) ?: continue)
            }
        } catch (e: ArrayIndexOutOfBoundsException) {
            Log.e(TAG, "Array of value size must be until 7")
        }
    }

    public fun setChartWidth(width: Float) {
        chartWidth = width
        changeAll(maxValue, recommendValue)
    }

    public fun getRecommendBox(): TextView = getView(R.id.wtbar_recommend_box)
    public fun getRecommendLine(): View = getView(R.id.wtbar_recommend_line)
    public fun getDownChart(unit: WTUnit): View = getView(matchWTUnitToChartSet(unit).downId)
    public fun getUpChart(unit: WTUnit): View = getView(matchWTUnitToChartSet(unit).upId)
    public fun getChartValue(unit: WTUnit): Float = matchWTUnitToChartSet(unit).value
    public fun getRecommendValue(): Float = recommendValue

    private fun matchWTUnitToChartSet(unit: WTUnit): ChartSet =
        when(unit) {
            WTUnit.COMPONENT1 -> ChartSet.COMPONENT1
            WTUnit.COMPONENT2 -> ChartSet.COMPONENT2
            WTUnit.COMPONENT3 -> ChartSet.COMPONENT3
            WTUnit.COMPONENT4 -> ChartSet.COMPONENT4
            WTUnit.COMPONENT5 -> ChartSet.COMPONENT5
            WTUnit.COMPONENT6 -> ChartSet.COMPONENT6
            WTUnit.COMPONENT7 -> ChartSet.COMPONENT7
        }

    private fun matchChartSetToWTUnit(chart: ChartSet): WTUnit =
        when(chart) {
            ChartSet.COMPONENT1 -> WTUnit.COMPONENT1
            ChartSet.COMPONENT2 -> WTUnit.COMPONENT2
            ChartSet.COMPONENT3 -> WTUnit.COMPONENT3
            ChartSet.COMPONENT4 -> WTUnit.COMPONENT4
            ChartSet.COMPONENT5 -> WTUnit.COMPONENT5
            ChartSet.COMPONENT6 -> WTUnit.COMPONENT6
            ChartSet.COMPONENT7 -> WTUnit.COMPONENT7
        }
}
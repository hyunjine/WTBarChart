package com.hyunjine.wtbarchart

import android.content.Context
import android.util.AttributeSet
import android.util.Log
import android.view.View
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.Guideline

abstract class WTBaseUnit : ConstraintLayout {
    companion object {
        const val TAG = "winter"

        const val ITEM_COUNT: Int = 7
        const val WHOLE_WIDTH: Float = 297f
        const val WHOLE_HEIGHT: Float = 152f
        const val START_GUIDE: Float = 84.5f
        const val END_GUIDE: Float = 283.5f

        const val RECOMMEND_BOX_WIDTH: Float = 52f
        const val CHART_WIDTH: Float = 7f
        const val MAX_CHART_HEIGHT: Float = 137f

        const val INTERVAL: Float = (END_GUIDE - START_GUIDE) / 6f
        const val ITEM_TEXT_SIZE: Float = 15f
        const val RECOMMEND_TEXT_SIZE: Float = 12f

        const val DEFAULT_MAX_VALUE: Float = 10f
        const val DEFAULT_RECOMMEND_VALUE: Float = 0f

        val chartSet = enumValues<ChartSet>()
    }

    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context, attrs: AttributeSet?, defStyle: Int) : super(
        context,
        attrs,
        defStyle
    )

    private val startGuideline = Guideline(context).apply {
        id = R.id.start_guide
        layoutParams = getGuideLineLayoutParams(START_GUIDE / WHOLE_WIDTH)
    }

    private val endGuideline = Guideline(context).apply {
        id = R.id.end_guide
        layoutParams = getGuideLineLayoutParams(END_GUIDE / WHOLE_WIDTH)
    }

    private val guideLine2 = Guideline(context).apply {
        id = R.id.guide_line_2
        layoutParams = getGuideLineLayoutParams(getGuidePercentPlusInterval(1))
    }

    private val guideLine3 = Guideline(context).apply {
        id = R.id.guide_line_3
        layoutParams = getGuideLineLayoutParams(getGuidePercentPlusInterval(2))
    }

    private val guideLine4 = Guideline(context).apply {
        id = R.id.guide_line_4
        layoutParams = getGuideLineLayoutParams(getGuidePercentPlusInterval(3))
    }

    private val guideLine5 = Guideline(context).apply {
        id = R.id.guide_line_5
        layoutParams = getGuideLineLayoutParams(getGuidePercentPlusInterval(4))
    }

    private val guideLine6 = Guideline(context).apply {
        id = R.id.guide_line_6
        layoutParams = getGuideLineLayoutParams(getGuidePercentPlusInterval(5))
    }

    protected fun addGuideLine() {
        addView(startGuideline)
        addView(endGuideline)
    }

    protected inline fun<reified T: View> getView(id: Int): T  {
        if (getViewById(id) is T) return getViewById(id) as T
        else throw TypeCastException("TextView or View are casted only")
    }

    private fun getGuidePercentPlusInterval(count: Int): Float =
        (START_GUIDE + (INTERVAL * count)) / WHOLE_WIDTH

    private fun getGuideLineLayoutParams(guidePercent: Float): LayoutParams {
        return LayoutParams(width, height).apply {
            orientation = LayoutParams.VERTICAL
            this.guidePercent = guidePercent
        }
    }

    protected fun Float.isNegative(): Boolean = this < 0f
}

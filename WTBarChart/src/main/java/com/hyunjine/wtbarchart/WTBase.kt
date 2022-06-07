package com.hyunjine.wtbarchart

import android.content.Context
import android.util.AttributeSet
import android.util.Log
import android.view.View
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.Guideline

public abstract class WTBase : ConstraintLayout {
    public companion object {
        internal const val TAG = "winter"

        public const val WHOLE_WIDTH: Float = 297f
        public const val WHOLE_HEIGHT: Float = 152f
        public const val START_GUIDE: Float = 65f
        public const val END_GUIDE: Float = 270f

        public const val RECOMMEND_BOX_MARGIN_LEFT: Int = 30
        public const val RECOMMEND_BOX_MARGIN_RIGHT: Int = 40
        public const val RECOMMEND_BOX_MARGIN_TOP: Int = 25
        public const val RECOMMEND_BOX_MARGIN_BOTTOM: Int = 25

        public const val MAX_CHART_HEIGHT: Float = 137f

        public const val CHART_WIDTH: Float = 8f
        public const val INTERVAL: Float = (END_GUIDE - START_GUIDE) / 6f
        public const val ITEM_TEXT_SIZE: Float = 15f
        public const val RECOMMEND_TEXT_SIZE: Float = 12f

        public const val DEFAULT_MAX_VALUE: Float = 10f
        public const val DEFAULT_RECOMMEND_VALUE: Float = 0f
    }

    public constructor(context: Context) : super(context)
    public constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)
    public constructor(context: Context, attrs: AttributeSet?, defStyle: Int) : super(
        context,
        attrs,
        defStyle
    )

    private val startGuideline = Guideline(context).apply {
        id = R.id.wtbar_start_guide
        layoutParams = getGuideLineLayoutParams(START_GUIDE / WHOLE_WIDTH)
    }

    private val endGuideline = Guideline(context).apply {
        id = R.id.wtbar_end_guide
        layoutParams = getGuideLineLayoutParams(END_GUIDE / WHOLE_WIDTH)
    }

    private val guideLine2 = Guideline(context).apply {
        id = R.id.wtbar_guide_line_2
        layoutParams = getGuideLineLayoutParams(getGuidePercentPlusInterval(1))
    }

    private val guideLine3 = Guideline(context).apply {
        id = R.id.wtbar_guide_line_3
        layoutParams = getGuideLineLayoutParams(getGuidePercentPlusInterval(2))
    }

    private val guideLine4 = Guideline(context).apply {
        id = R.id.wtbar_guide_line_4
        layoutParams = getGuideLineLayoutParams(getGuidePercentPlusInterval(3))
    }

    private val guideLine5 = Guideline(context).apply {
        id = R.id.wtbar_guide_line_5
        layoutParams = getGuideLineLayoutParams(getGuidePercentPlusInterval(4))
    }

    private val guideLine6 = Guideline(context).apply {
        id = R.id.wtbar_guide_line_6
        layoutParams = getGuideLineLayoutParams(getGuidePercentPlusInterval(5))
    }

    protected fun addGuideLine() {
        addView(startGuideline)
        addView(endGuideline)
        addView(guideLine2)
        addView(guideLine3)
        addView(guideLine4)
        addView(guideLine5)
        addView(guideLine6)
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

    @JvmSynthetic
    protected fun Float.isNegative(): Boolean = this < 0f
}
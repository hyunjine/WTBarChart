package com.hyunjine.wtbarchart

import android.content.Context
import android.graphics.Color
import android.graphics.Typeface
import android.util.AttributeSet
import android.util.Log
import android.view.Gravity
import android.widget.TextView

class WTChartItem: WTBaseUnit {
    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context, attrs: AttributeSet?, defStyle: Int) : super(
        context,
        attrs,
        defStyle
    )

    private val itemIdList = HashMap<ChartItemIdSet, Int>().apply {
        put(ChartItemIdSet.COMPONENT1, R.id.item1)
        put(ChartItemIdSet.COMPONENT2, R.id.item2)
        put(ChartItemIdSet.COMPONENT3, R.id.item3)
        put(ChartItemIdSet.COMPONENT4, R.id.item4)
        put(ChartItemIdSet.COMPONENT5, R.id.item5)
        put(ChartItemIdSet.COMPONENT6, R.id.item6)
        put(ChartItemIdSet.COMPONENT7, R.id.item7)
    }

    init {
        addGuideLine()
        for (id in itemIdList) {
            TextView(context).apply {
                this.id = id.value
                setItem(id.key, this)
            }
        }
    }

    private fun setItem(itemIdSet: ChartItemIdSet, view: TextView) {
        with(view) {
            textSize = ITEM_TEXT_SIZE
            setTextColor(context.getColor(R.color.bar_chart_item))
            typeface = Typeface.createFromAsset(context.assets, "pretendard_r.otf")
            gravity = Gravity.CENTER
            layoutParams = LayoutParams(
                LayoutParams.WRAP_CONTENT,
                LayoutParams.WRAP_CONTENT
            ).apply {
                horizontalChainStyle = LayoutParams.CHAIN_SPREAD
                topToTop = LayoutParams.PARENT_ID
                val pair = getIdsForItem(itemIdSet)
                if (itemIdSet == ChartItemIdSet.COMPONENT1 || itemIdSet == ChartItemIdSet.COMPONENT7) {
                    startToStart = pair.first
                    endToEnd = pair.second
                } else {
                    startToEnd = pair.first
                    endToStart = pair.second
                }
            }
            addView(this)

        }
    }

    private fun getIdsForItem(itemIdSet: ChartItemIdSet): Pair<Int, Int> {
        return when (itemIdSet) {
            ChartItemIdSet.COMPONENT1 -> Pair(R.id.start_guide, R.id.start_guide)
            ChartItemIdSet.COMPONENT7 -> Pair(R.id.end_guide, R.id.end_guide)
            ChartItemIdSet.COMPONENT2 -> Pair(R.id.item1, R.id.item3)
            ChartItemIdSet.COMPONENT3 -> Pair(R.id.item2, R.id.item4)
            ChartItemIdSet.COMPONENT4 -> Pair(R.id.item3, R.id.item5)
            ChartItemIdSet.COMPONENT5 -> Pair(R.id.item4, R.id.item6)
            ChartItemIdSet.COMPONENT6 -> Pair(R.id.item5, R.id.item7)
        }
    }

    fun setAllItemText(list: Array<String>) {
        try {
            for ((idx, text) in list.withIndex()) {
                getView<TextView>(
                    when(idx) {
                        0 -> R.id.item1
                        1 -> R.id.item2
                        2 -> R.id.item3
                        3 -> R.id.item4
                        4 -> R.id.item5
                        5 -> R.id.item6
                        6 -> R.id.item7
                        else -> throw ArrayIndexOutOfBoundsException()
                    }
                ).text = text
            }
        } catch (e: ArrayIndexOutOfBoundsException) {
            Log.e(TAG, "Array of text size must be until 7")
        }
    }

    fun setItemText(id: ChartItemIdSet, text: String) =
        itemIdList[id]?.let { getView<TextView>(it).text = text }

    fun setItemTextSize(id: ChartItemIdSet, size: Float) =
        itemIdList[id]?.let { getView<TextView>(it).textSize = size }

    fun setItemTextFont(id: ChartItemIdSet, typeface: Typeface) =
        itemIdList[id]?.let { getView<TextView>(it).typeface = typeface }

    fun setItemTextColor(id: ChartItemIdSet, colorId: Int) =
        itemIdList[id]?.let { getView<TextView>(it).setTextColor(colorId) }

    fun setItemTextColor(id: ChartItemIdSet, color: String) =
        itemIdList[id]?.let { getView<TextView>(it).setTextColor(Color.parseColor(color)) }

}
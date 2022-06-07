package com.hyunjine.wtbarchart

import android.content.Context
import android.util.AttributeSet
import android.util.Log
import android.view.Gravity
import android.widget.TextView

public class WTChartItem: WTBase {
    public constructor(context: Context) : super(context)
    public constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)
    public constructor(context: Context, attrs: AttributeSet?, defStyle: Int) : super(
        context,
        attrs,
        defStyle
    )

    private val itemList: Array<ItemSet> = enumValues()
    init {
        addGuideLine()
        for (item in itemList) {
            TextView(context).apply {
                this.id = item.viewId
                setItem(item, this)
            }
        }
    }

    private fun setItem(item: ItemSet, view: TextView) {
        with(view) {
            textSize = ITEM_TEXT_SIZE
            setTextColor(context.getColor(R.color.wtbar_bar_chart_item))
            gravity = Gravity.CENTER
            layoutParams = LayoutParams(
                LayoutParams.WRAP_CONTENT,
                LayoutParams.WRAP_CONTENT
            ).apply {
                horizontalChainStyle = LayoutParams.CHAIN_SPREAD
                topToTop = LayoutParams.PARENT_ID
                val id = getIdsForItem(item)
                startToStart = id
                endToEnd = id
            }
            addView(this)
        }
    }

    private fun getIdsForItem(item: ItemSet): Int =
        when(item) {
            ItemSet.COMPONENT1 -> R.id.wtbar_start_guide
            ItemSet.COMPONENT7 -> R.id.wtbar_end_guide
            ItemSet.COMPONENT2 -> R.id.wtbar_guide_line_2
            ItemSet.COMPONENT3 -> R.id.wtbar_guide_line_3
            ItemSet.COMPONENT4 -> R.id.wtbar_guide_line_4
            ItemSet.COMPONENT5 -> R.id.wtbar_guide_line_5
            ItemSet.COMPONENT6 -> R.id.wtbar_guide_line_6
        }

    public fun setAllItemText(list: Array<String>) {
        try {
            for ((idx, text) in list.withIndex()) {
                getView<TextView>(itemList[idx].viewId).text = text
            }
        } catch (e: ArrayIndexOutOfBoundsException) {
            Log.e(TAG, "Array of text size must be until 7")
        }
    }

    private fun matchWTUnitToItemSet(unit: WTUnit): ItemSet =
        when(unit) {
            WTUnit.COMPONENT1 -> ItemSet.COMPONENT1
            WTUnit.COMPONENT2 -> ItemSet.COMPONENT2
            WTUnit.COMPONENT3 -> ItemSet.COMPONENT3
            WTUnit.COMPONENT4 -> ItemSet.COMPONENT4
            WTUnit.COMPONENT5 -> ItemSet.COMPONENT5
            WTUnit.COMPONENT6 -> ItemSet.COMPONENT6
            WTUnit.COMPONENT7 -> ItemSet.COMPONENT7
        }

    public fun getItem(unit: WTUnit): TextView =
        getView(matchWTUnitToItemSet(unit).viewId)
}
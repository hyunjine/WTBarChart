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
            setTextColor(context.getColor(R.color.bar_chart_item))
            typeface = Typeface.createFromAsset(context.assets, "pretendard_r.otf")
            gravity = Gravity.CENTER
            layoutParams = LayoutParams(
                LayoutParams.WRAP_CONTENT,
                LayoutParams.WRAP_CONTENT
            ).apply {
                horizontalChainStyle = LayoutParams.CHAIN_SPREAD
                topToTop = LayoutParams.PARENT_ID
                val pair = getIdsForItem(item)
                if (item == ItemSet.COMPONENT1 || item == ItemSet.COMPONENT7) {
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

    private fun getIdsForItem(item: ItemSet): Pair<Int, Int> {
        return when (item) {
            ItemSet.COMPONENT1 -> Pair(R.id.start_guide, R.id.start_guide)
            ItemSet.COMPONENT7 -> Pair(R.id.end_guide, R.id.end_guide)
            ItemSet.COMPONENT2 -> Pair(R.id.item1, R.id.item3)
            ItemSet.COMPONENT3 -> Pair(R.id.item2, R.id.item4)
            ItemSet.COMPONENT4 -> Pair(R.id.item3, R.id.item5)
            ItemSet.COMPONENT5 -> Pair(R.id.item4, R.id.item6)
            ItemSet.COMPONENT6 -> Pair(R.id.item5, R.id.item7)
        }
    }

    fun setAllItemText(list: Array<String>) {
        try {
            for ((idx, text) in list.withIndex()) {
                getView<TextView>(itemList[idx].viewId).text = text
            }
        } catch (e: ArrayIndexOutOfBoundsException) {
            Log.e(TAG, "Array of text size must be until 7")
        }
    }

    fun setItemText(item: ItemSet, text: String) =
        text.also { getView<TextView>(item.viewId).text = it }


    fun setItemTextSize(item: ItemSet, size: Float) =
        size.also { getView<TextView>(item.viewId).textSize = it }


    fun setItemTextFont(item: ItemSet, typeface: Typeface) =
        typeface.also { getView<TextView>(item.viewId).typeface = typeface }

    fun setItemTextColor(item: ItemSet, colorId: Int) =
        getView<TextView>(item.viewId).setTextColor(colorId)

    fun setItemTextColor(item: ItemSet, color: String) =
        getView<TextView>(item.viewId).setTextColor(Color.parseColor(color))

}
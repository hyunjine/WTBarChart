package com.hyunjine.wtbarchart

enum class ChartSet(
    var value: Float,
    val downId: Int,
    val upId: Int
) {
    COMPONENT1(0f, R.id.down_chart1, R.id.up_chart1),
    COMPONENT2(0f, R.id.down_chart2, R.id.up_chart2),
    COMPONENT3(0f, R.id.down_chart3, R.id.up_chart3),
    COMPONENT4(0f, R.id.down_chart4, R.id.up_chart4),
    COMPONENT5(0f, R.id.down_chart5, R.id.up_chart5),
    COMPONENT6(0f, R.id.down_chart6, R.id.up_chart6),
    COMPONENT7(0f, R.id.down_chart7, R.id.up_chart7)
}

enum class ItemSet(val viewId: Int) {
    COMPONENT1(R.id.item1),
    COMPONENT2(R.id.item2),
    COMPONENT3(R.id.item3),
    COMPONENT4(R.id.item4),
    COMPONENT5(R.id.item5),
    COMPONENT6(R.id.item6),
    COMPONENT7(R.id.item7)
}
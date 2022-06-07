package com.hyunjine.wtbarchart

internal enum class ChartSet(
    var value: Float,
    val downId: Int,
    val upId: Int
) {
    COMPONENT1(0f, R.id.wtbar_down_chart1, R.id.wtbar_up_chart1),
    COMPONENT2(0f, R.id.wtbar_down_chart2, R.id.wtbar_up_chart2),
    COMPONENT3(0f, R.id.wtbar_down_chart3, R.id.wtbar_up_chart3),
    COMPONENT4(0f, R.id.wtbar_down_chart4, R.id.wtbar_up_chart4),
    COMPONENT5(0f, R.id.wtbar_down_chart5, R.id.wtbar_up_chart5),
    COMPONENT6(0f, R.id.wtbar_down_chart6, R.id.wtbar_up_chart6),
    COMPONENT7(0f, R.id.wtbar_down_chart7, R.id.wtbar_up_chart7)
}

internal enum class ItemSet(val viewId: Int) {
    COMPONENT1(R.id.wtbar_item1),
    COMPONENT2(R.id.wtbar_item2),
    COMPONENT3(R.id.wtbar_item3),
    COMPONENT4(R.id.wtbar_item4),
    COMPONENT5(R.id.wtbar_item5),
    COMPONENT6(R.id.wtbar_item6),
    COMPONENT7(R.id.wtbar_item7)
}

public enum class WTUnit {
    COMPONENT1,
    COMPONENT2,
    COMPONENT3,
    COMPONENT4,
    COMPONENT5,
    COMPONENT6,
    COMPONENT7;

    public companion object {
        public fun getAll(): Array<WTUnit> = enumValues()
    }
}
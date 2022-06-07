<h1 align="center">WTBarChart</h1>
<h3 align="center">📊 7 unit oriented bar chart library suitable for TODO and more. </br>
Anyway, Thank you for your interest and use!</h3>

![version](https://img.shields.io/badge/version-v1.0.0-brightgreen.svg)
![version](https://img.shields.io/badge/API-23+-blue.svg)

# Preview
<p align="center">
<img src="https://user-images.githubusercontent.com/92709137/172373528-4f0240ab-7af0-4844-9919-a3a594a1f611.png" width="45%"/>
<img src="https://user-images.githubusercontent.com/92709137/172381580-7b68f057-7de7-4393-ba60-8e42317dc869.png" width="45% />
</p>
                                                                                                                        
<p align="center">
<img src="https://user-images.githubusercontent.com/92709137/172385745-559e2ef9-2d96-4189-9c87-82906317cd93.gif" width="45%" />
</p>

---
# Including in your project
**setting.gradle**
```java
allprojects {
    repositories {
        ...
        maven { url 'https://jitpack.io' }
    }
}
```
**build.gradle(Module)**
```java
dependencies {
    ...
    implementation 'com.github.hyunjine.WTBarChart:wtbarchart:1.0.0'
}
```
# How to Use
### In .xml                                                                                                                            
Add two layouts to the view you want to use.

Each layout has an area for charts and an area for chart items.  
The reason for dividing the two is to allow different background properties to be specified for each.  
**The default background is white.**
                                                                                                                            
```xml
<com.hyunjine.wtbarchart.WTBarChart
    android:id="@+id/wt_chart"
    android:layout_width="0dp"
    android:background="@drawable/bg_chart_area"
    android:layout_height="152dp"
    app:layout_constraintBottom_toBottomOf="parent"
    app:layout_constraintEnd_toEndOf="parent"
    app:layout_constraintStart_toStartOf="parent"
    app:layout_constraintTop_toTopOf="parent"
    app:layout_constraintWidth_percent="0.8" />

<com.hyunjine.wtbarchart.WTChartItem
    android:id="@+id/wt_item"
    android:layout_width="0dp"
    android:layout_height="wrap_content"
    android:background="@drawable/bg_item_area"
    android:paddingTop="5dp"
    android:paddingBottom="5dp"
    app:layout_constraintEnd_toEndOf="@id/wt_chart"
    app:layout_constraintStart_toStartOf="@id/wt_chart"
    app:layout_constraintTop_toBottomOf="@id/wt_chart" />
```
### In code
### Normal

In order to use this library, basically, you need to enter three values.  
You must specify the values of each chart, the names of chart items, and the recommended values.  
The order in which they appear in the view is from left to right.

When less than 7 values are entered, only the corresponding values are displayed in the view, **but if they are exceeded, an exception is raised.**

In addition, **a max value can be specified.**  
If the difference in values between charts exceeds the normal range, charts with small values are rarely seen.  
To prepare for this, the setMaxValue() method exists so that the max value is specified, and when this max value is exceeded, the maximum value can be displayed consistently.
```kotlin
binding.run {
    wtItem.setAllItemText(arrayOf("Mon", "Tue", "Wed", "Thu", "Fri", "Sat", "Sun"))
    wtChart.apply {
        setAllChartValue(arrayOf(100f, 50f, 40f, 70f, 80f, 30f, 20f))
        setRecommendValue(60f)
        setMaxValue(100f)
    }
}
```
If you want to handle the event through chart click, you can use the following function.
```kotlin
binding.run {
    ...
    wtChart.setOnChartClickListener { wtUnit, value ->
        // What you want
    }

    // or
    ...
    wtChart.setOnChartClickListener(object : OnChartClickListener {
        override fun onChartClick(unit: WTUnit, value: Float) {
            // What you want
        }
    })
}
```

The parameter **"unit"** means the enum value of the clicked chart, and **"value"** means the current value of the clicked chart.  

### Custom
All elements composing this library inherit View, so the process of changing it to the desired shape is the same as usual.  
- Chart - **View**
- Recommend Line - **View**
- Recommend Box - **TextView**
- Chart Item - **TextView**

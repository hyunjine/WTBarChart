<h1 align="center">WTBarChart</h1>
<h3 align="center">📊 7 unit oriented bar chart library suitable for TODO and more.
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
    implementation 'com.github.hyunjine.WTBarChart:wtbarchart:0.0.3'
}
```
# How to Use
사용하고자

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

package com.iue.projectgastosapp.views.composable.home

import android.view.ViewGroup
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import com.github.mikephil.charting.charts.PieChart
import com.github.mikephil.charting.components.Legend
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import com.github.mikephil.charting.utils.ColorTemplate
import com.iue.projectgastosapp.views.composable.CustomValueFormatter

@Composable
fun PieChartStatistics(
    dataChart: List<PieChartData>
) {
    AndroidView(
        modifier = Modifier
            .fillMaxWidth()
            .height(300.dp)
            .padding(8.dp),
        factory = { context ->
            PieChart(context).apply {
                layoutParams = ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.MATCH_PARENT
                )
                description.isEnabled = false
                val dataList = dataChart.map { PieEntry(it.percentage, it.label) }
                val dataSet = PieDataSet(dataList, "Categorías")
                dataSet.colors = ColorTemplate.COLORFUL_COLORS.toList()
                val pieData = PieData(dataSet)
                pieData.setValueFormatter(CustomValueFormatter())
                dataSet.valueTextSize = 14f
                data = pieData

                legend.isEnabled = true
                legend.horizontalAlignment = Legend.LegendHorizontalAlignment.RIGHT
                legend.verticalAlignment = Legend.LegendVerticalAlignment.CENTER
                legend.orientation = Legend.LegendOrientation.VERTICAL
                legend.textSize = 12f
                legend.textColor = Color.Black.hashCode()
                legend.formSize = 12f
                isDrawHoleEnabled = false
                setDrawEntryLabels(false)
                setDrawMarkers(false)
            }
        }
    )
}

@Preview(showBackground = true)
@Composable
fun PieChartPreview() {
    val data = listOf(
        PieChartData("Entretenimiento", Color(0xFF109619), 25f),
        PieChartData("Transporte", Color(0xFF3366cc), 30f),
        PieChartData("Alimentación", Color(0xFFdc3912), 20f),
        PieChartData("Otros", Color(0xFFff9900), 25f)
    )
    PieChartStatistics(dataChart = data)
}
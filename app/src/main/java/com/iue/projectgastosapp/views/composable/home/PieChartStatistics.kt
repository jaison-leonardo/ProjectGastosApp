package com.iue.projectgastosapp.views.composable.home

import android.util.Log
import android.view.ViewGroup
import android.widget.Toast
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import com.github.mikephil.charting.charts.PieChart
import com.github.mikephil.charting.components.Legend
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import com.github.mikephil.charting.highlight.Highlight
import com.github.mikephil.charting.listener.OnChartValueSelectedListener
import com.github.mikephil.charting.utils.ColorTemplate
import com.iue.projectgastosapp.views.composable.CustomValueFormatter


@Composable
fun PieChartStatistics(
    dataChart: List<PieChartData>
) {
    val pieEntries by remember { mutableStateOf(generatePieEntries(dataChart)) }
    val context = LocalContext.current
    val chart = remember { PieChart(context) }

    DisposableEffect(chart) {
        val listener = object : OnChartValueSelectedListener {
            override fun onValueSelected(e: Entry?, h: Highlight?) {
                Log.i("Pie_Chart", "selected value ${e?.y} ${e?.data}")
                if (e is PieEntry && e.data is PieChartData) {
                    val pieChartData = e.data as PieChartData
                    val detail = pieChartData.detail
                    Toast.makeText(context, detail, Toast.LENGTH_SHORT).show()
                }
            }

            override fun onNothingSelected() {
                Log.i("Pie_Chart", "nothing selected")
            }

        }
        chart.setOnChartValueSelectedListener(listener)
        onDispose {
            chart.setOnChartValueSelectedListener(null)
        }
    }

    AndroidView(
        modifier = Modifier
            .fillMaxWidth()
            .height(300.dp)
            .padding(8.dp),
        factory = {
            chart.apply {
                layoutParams = ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.MATCH_PARENT
                )
                description.isEnabled = false
                val dataSet = PieDataSet(pieEntries, "")
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
                this.setCenterTextOffset(0f, 0f)
            }
        }
    )
}

private fun generatePieEntries(dataChart: List<PieChartData>): List<PieEntry> {
    return dataChart.map {
        PieEntry(it.value, it.label, it) }
}

@Preview(showBackground = true)
@Composable
fun PieChartPreview() {
    val data = listOf(
        PieChartData(value = 25f, label = "Entretenimiento", detail = "$50.000\nrestantes"),
        PieChartData(value = 30f, label = "Transporte", detail = "$45.000\nrestantes"),
        PieChartData(value = 20f, label = "Alimentación", detail = "$40.000\nrestantes"),
        PieChartData(value = 25f, label = "Otros", detail = "$25.000\nrestantes"),
    )
    PieChartStatistics(dataChart = data)
}
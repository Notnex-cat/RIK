package com.notnex.rik.ui.statistics.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.drawscope.Stroke
import com.notnex.rik.data.statistics.model.VisitorsChartPoint

@Composable
fun VisitorsChart(points: List<VisitorsChartPoint>) {
    if (points.isEmpty()) return

    Canvas(modifier = Modifier.fillMaxSize()) {
        val maxY = points.maxOf { it.count }.toFloat().coerceAtLeast(1f)
        val minY = points.minOf { it.count }.toFloat()
        val stepX = size.width / (points.size - 1).coerceAtLeast(1)
        val padding = 12f
        val chartHeight = size.height - 2 * padding
        size.width - 2 * padding
        val rangeY = (maxY - minY).coerceAtLeast(1f)

        // Основной путь линии
        val path = Path()
        points.forEachIndexed { i, p ->
            val x = padding + i * stepX
            val y = padding + chartHeight - ((p.count - minY) / rangeY) * chartHeight
            if (i == 0) path.moveTo(x, y) else path.lineTo(x, y)
        }

        drawPath(
            path = path,
            color = Color(0xFFFB5607), // Оранжевый из Figma
            style = Stroke(
                width = 3f,
                cap = StrokeCap.Round,
                join = StrokeJoin.Round
            )
        )

        // Круглые точки на линии
        points.forEachIndexed { i, p ->
            val x = padding + i * stepX
            val y = padding + chartHeight - ((p.count - minY) / rangeY) * chartHeight
            drawCircle(
                color = Color(0xFFFB5607),
                radius = 5f,
                center = Offset(x, y)
            )
        }
    }
}

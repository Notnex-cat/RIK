package com.notnex.rik.ui.statistics.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.input.pointer.pointerInput
import com.notnex.rik.data.statistics.model.VisitorsChartPoint

@Composable
fun VisitorsChart(points: List<VisitorsChartPoint>) {
    if (points.isEmpty()) return

    var selectedIndex by remember { mutableIntStateOf(-1) }

    Canvas(
        modifier = Modifier
            .fillMaxSize()
            .pointerInput(points) {
                detectTapGestures { offset ->
                    val padding = 32f
                    val chartWidth = size.width - 2 * padding
                    val stepX = chartWidth / (points.size - 1)
                    val tappedIndex = ((offset.x - padding + stepX / 2) / stepX).toInt()
                        .coerceIn(0, points.lastIndex)
                    selectedIndex = tappedIndex
                }
            }
    ) {
        val padding = 32f
        val chartWidth = size.width - 2 * padding
        val chartHeight = size.height - 2 * padding
        val maxY = points.maxOf { it.count }.toFloat().coerceAtLeast(1f)
        val rangeY = (maxY).coerceAtLeast(1f)
        val stepX = chartWidth / (points.size - 1)

        // Сетка
        repeat(4) {
            val y = padding + it * chartHeight / 3
            drawLine(Color(0xFFE0E0E0), Offset(padding, y), Offset(size.width - padding, y), 1f)
        }

        // Линия
        val path = Path()
        points.forEachIndexed { i, p ->
            val x = padding + i * stepX
            val y = padding + chartHeight - (p.count / rangeY) * chartHeight
            if (i == 0) path.moveTo(x, y) else path.lineTo(x, y)
        }
        drawPath(
            path = path,
            color = Color(0xFFFF3D00),
            style = Stroke(width = 3f, cap = StrokeCap.Round, join = StrokeJoin.Round)
        )


        // Точки
        points.forEachIndexed { i, p ->
            val x = padding + i * stepX
            val y = padding + chartHeight - (p.count / rangeY) * chartHeight
            drawCircle(Color.White, 7f, Offset(x, y)) // белая подложка
            drawCircle(Color(0xFFFF3D00), 5f, Offset(x, y))
        }

        // Выделенная точка
        if (selectedIndex in points.indices) {
            val p = points[selectedIndex]
            val x = padding + selectedIndex * stepX
            val y = padding + chartHeight - (p.count / rangeY) * chartHeight

            // Пунктир
            drawLine(Color(0xFFFF3D00), Offset(x, padding), Offset(x, size.height - padding), strokeWidth = 1f)

            // Tooltip
            val tooltipText = "${p.count} посетител${if (p.count == 1) "ь" else "ей"}"
            val date = p.date
            val tooltipWidth = 120f
            val tooltipHeight = 60f
            val tooltipX = x.coerceIn(tooltipWidth / 2, size.width - tooltipWidth / 2)
            val tooltipY = padding

            drawRoundRect(
                color = Color.White,
                topLeft = Offset(tooltipX - tooltipWidth / 2, tooltipY),
                size = androidx.compose.ui.geometry.Size(tooltipWidth, tooltipHeight),
                cornerRadius = androidx.compose.ui.geometry.CornerRadius(12f, 12f)
            )
            drawContext.canvas.nativeCanvas.apply {
                val paint = android.graphics.Paint().apply {
                    color = android.graphics.Color.RED
                    textSize = 28f
                    isAntiAlias = true
                    textAlign = android.graphics.Paint.Align.CENTER
                }
                drawText(
                    tooltipText,
                    tooltipX,
                    tooltipY + 25f,
                    paint
                )
                paint.color = android.graphics.Color.GRAY
                paint.textSize = 24f
                drawText(date, tooltipX, tooltipY + 50f, paint)
            }
        }
    }
}

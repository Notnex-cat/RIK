package com.notnex.rik.ui.statistics.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.notnex.rik.data.statistics.model.VisitorsChartPoint

@Composable
fun VisitorsCard(
    visitorsCount: Int,
    visitorsGrowth: Boolean,
    chartPoints: List<VisitorsChartPoint>
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {

            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(bottom = 4.dp)
            ) {
                Text(
                    text = visitorsCount.toString(),
                    style = MaterialTheme.typography.headlineSmall
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = if (visitorsGrowth) "↑" else "↓",
                    color = if (visitorsGrowth) Color(0xFF4CAF50) else Color(0xFFF44336),
                    style = MaterialTheme.typography.headlineSmall
                )
            }

            Text(
                text = if (visitorsGrowth)
                    "Количество посетителей в этом месяце выросло"
                else
                    "Количество посетителей в этом месяце уменьшилось",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Spacer(modifier = Modifier.height(16.dp))

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(60.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(Color(0xFFF1F8E9)), // мягкий фон для графика
                contentAlignment = Alignment.Center
            ) {
                VisitorsChart(points = chartPoints)
            }
        }
    }
}

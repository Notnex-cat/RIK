package com.notnex.rik.ui.statistics.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.unit.dp
import com.notnex.rik.ui.theme.SegmentedTabRow

@Composable
fun GenderAgeChart(
    malePercent: Int,
    femalePercent: Int,
    ageStats: List<AgeStatUi>
) {
    val arcStartAngle = 150f
    val totalArcAngle = 240f

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Круговая диаграмма
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(120.dp),
                contentAlignment = Alignment.Center
            ) {
                Canvas(modifier = Modifier.size(100.dp)) {
                    val sweepMale = 360f * malePercent / 100f
                    drawArc(
                        color = Color(0xFFFF3D00),
                        startAngle = -90f,
                        sweepAngle = sweepMale,
                        useCenter = false,
                        style = Stroke(width = 12f)
                    )
                    drawArc(
                        color = Color(0xFFFF9100),
                        startAngle = -90f + sweepMale,
                        sweepAngle = 360f - sweepMale,
                        useCenter = false,
                        style = Stroke(width = 12f)
                    )
                }
            }


            // Подписи: Мужчины / Женщины (только кружок и текст)
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.padding(horizontal = 8.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .size(10.dp)
                            .clip(CircleShape)
                            .background(Color(0xFFFF3D00))
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = "Мужчины $malePercent%",
                        style = MaterialTheme.typography.labelMedium,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                }
                Spacer(modifier = Modifier.width(12.dp))
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.padding(horizontal = 8.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .size(10.dp)
                            .clip(CircleShape)
                            .background(Color(0xFFFF9100))
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = "Женщины $femalePercent%",
                        style = MaterialTheme.typography.labelMedium,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                }
            }

            // Возрастные категории
            Column(
                verticalArrangement = Arrangement.spacedBy(10.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                ageStats.forEach { stat ->
                    val total = stat.malePercent + stat.femalePercent
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(
                            text = stat.range,
                            style = MaterialTheme.typography.labelMedium,
                            modifier = Modifier.width(50.dp),
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        Column (
                            modifier = Modifier.weight(1f),
                           // horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            if (stat.malePercent > 0) {
                                Box(
                                    modifier = Modifier
                                        .height(6.dp)
                                        .width((stat.malePercent * 1.5f).dp)
                                        .clip(RoundedCornerShape(3.dp))
                                        .background(Color(0xFFFF3D00))
                                )
                            }
                            if (stat.femalePercent > 0) {
                                Spacer(modifier = Modifier.width(4.dp))
                                Box(
                                    modifier = Modifier
                                        .height(6.dp)
                                        .width((stat.femalePercent * 1.5f).dp)
                                        .clip(RoundedCornerShape(3.dp))
                                        .background(Color(0xFFFF9100))
                                )
                            }
                        }
                        Spacer(modifier = Modifier.width(8.dp))
                        Column {
                            Text(
                                text = "${stat.malePercent}%",
                                style = MaterialTheme.typography.labelSmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                            Text(
                                text = "${stat.femalePercent}%",
                                style = MaterialTheme.typography.labelSmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                }
            }
        }
    }
}

data class AgeStatUi(
    val range: String,
    val malePercent: Int,
    val femalePercent: Int
)

@Composable
fun GenderAgeTabSelector(
    selected: Int,
    onTabSelected: (Int) -> Unit
) {
    val titles = listOf("Сегодня", "Неделя", "Месяц", "Все время")
    SegmentedTabRow(
        titles = titles,
        selectedIndex = selected,
        onSelectedChange = onTabSelected
    )
}
package com.notnex.rik.ui.statistics.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.unit.dp

@Composable
fun GenderAgeChart(
    malePercent: Int,
    femalePercent: Int,
    ageStats: List<AgeStatUi>
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
            Text(
                text = "Пол и возраст",
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier.padding(bottom = 12.dp)
            )
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(bottom = 16.dp)
            ) {
                Box(modifier = Modifier.size(80.dp)) {
                    Canvas(modifier = Modifier.fillMaxSize()) {
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
                    Text(
                        text = "$malePercent% / $femalePercent%",
                        modifier = Modifier.align(Alignment.Center),
                        style = MaterialTheme.typography.labelLarge
                    )
                }
                Spacer(modifier = Modifier.width(16.dp))
                Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier
                                .size(10.dp)
                                .clip(CircleShape)
                                .background(Color(0xFFFF3D00))
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Мужчины $malePercent%", style = MaterialTheme.typography.bodyMedium)
                    }
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier
                                .size(10.dp)
                                .clip(CircleShape)
                                .background(Color(0xFFFF9100))
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Женщины $femalePercent%", style = MaterialTheme.typography.bodyMedium)
                    }
                }
            }
            Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                ageStats.forEach { stat ->
                    Column {
                        Text(
                            text = stat.range,
                            style = MaterialTheme.typography.labelMedium,
                            modifier = Modifier.padding(bottom = 4.dp)
                        )
                        Row {
                            Box(
                                modifier = Modifier
                                    .height(8.dp)
                                    .width((stat.malePercent * 2).coerceAtLeast(1).dp)
                                    .clip(RoundedCornerShape(4.dp))
                                    .background(Color(0xFFFF3D00))
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Box(
                                modifier = Modifier
                                    .height(8.dp)
                                    .width((stat.femalePercent * 2).coerceAtLeast(1).dp)
                                    .clip(RoundedCornerShape(4.dp))
                                    .background(Color(0xFFFF9100))
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

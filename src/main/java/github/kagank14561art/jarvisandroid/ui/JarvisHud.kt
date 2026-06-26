package github.kagank14561art.jarvisandroid.ui

import androidx.compose.animation.core.*
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.withTransform
import androidx.compose.ui.unit.dp
import kotlin.math.cos
import kotlin.math.sin
import kotlin.random.Random

// Colors from the original UI
val ColorBg = Color(0xFF020C0C)
val ColorPri = Color(0xFF00D4C0)
val ColorMuted = Color(0xFF006A62)
val ColorDim = Color(0xFF0A2A28)
val ColorText = Color(0xFF7DFFF6)
val ColorGreen = Color(0xFF00FF88)
val ColorBlue = Color(0xFF4488FF)
val ColorGold = Color(0xFFFFCC00)
val ColorRed = Color(0xFFFF3344)

@Composable
fun JarvisHud(
    modifier: Modifier = Modifier,
    state: String = "LISTENING",
    logs: List<String> = emptyList(),
    batteryLevel: Int = 100
) {
    val infiniteTransition = rememberInfiniteTransition(label = "jarvis")
    
    val rotation by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(
            animation = tween(10000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "rotation"
    )

    val pulse by infiniteTransition.animateFloat(
        initialValue = 0.8f,
        targetValue = 1.2f,
        animationSpec = infiniteRepeatable(
            animation = tween(2000, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "pulse"
    )

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(ColorBg)
    ) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            val center = Offset(size.width / 2, size.height / 2)
            val baseRadius = size.width.coerceAtMost(size.height) * 0.25f
            
            // Draw background grid (dots)
            val step = 48.dp.toPx()
            for (x in 0..(size.width / step).toInt()) {
                for (y in 0..(size.height / step).toInt()) {
                    drawCircle(
                        color = Color(0xFF061414),
                        radius = 1f,
                        center = Offset(x * step, y * step)
                    )
                }
            }

            // Draw Side Panel Dividers
            val leftPanelWidth = size.width * 0.25f
            val rightPanelWidth = size.width * 0.25f
            
            drawLine(
                color = ColorDim,
                start = Offset(leftPanelWidth, 0f),
                end = Offset(leftPanelWidth, size.height),
                strokeWidth = 2f
            )
            drawLine(
                color = ColorDim,
                start = Offset(size.width - rightPanelWidth, 0f),
                end = Offset(size.width - rightPanelWidth, size.height),
                strokeWidth = 2f
            )

            // Draw Orb
            val orbColor = when (state) {
                "SPEAKING" -> ColorBlue
                "THINKING" -> ColorGold
                "ERROR" -> ColorRed
                else -> ColorGreen
            }

            // Outer Glow
            drawCircle(
                brush = Brush.radialGradient(
                    colors = listOf(orbColor.copy(alpha = 0.15f), Color.Transparent),
                    center = center,
                    radius = baseRadius * 1.6f * pulse
                ),
                center = center,
                radius = baseRadius * 1.6f * pulse
            )

            // Structural circles
            drawCircle(
                color = orbColor.copy(alpha = 0.3f),
                center = center,
                radius = baseRadius * 1.0f,
                style = Stroke(width = 2f)
            )
            drawCircle(
                color = orbColor.copy(alpha = 0.1f),
                center = center,
                radius = baseRadius * 0.85f,
                style = Stroke(width = 1f)
            )

            // Rotating arcs
            withTransform({
                rotate(rotation, center)
            }) {
                drawArc(
                    color = orbColor,
                    startAngle = 0f,
                    sweepAngle = 60f,
                    useCenter = false,
                    topLeft = Offset(center.x - baseRadius, center.y - baseRadius),
                    size = androidx.compose.ui.geometry.Size(baseRadius * 2, baseRadius * 2),
                    style = Stroke(width = 4f)
                )
                drawArc(
                    color = orbColor,
                    startAngle = 180f,
                    sweepAngle = 45f,
                    useCenter = false,
                    topLeft = Offset(center.x - baseRadius, center.y - baseRadius),
                    size = androidx.compose.ui.geometry.Size(baseRadius * 2, baseRadius * 2),
                    style = Stroke(width = 4f)
                )
            }
        }

        // Overlay Text Info (Simplified)
        Column(
            modifier = Modifier
                .fillMaxHeight()
                .width(200.dp)
                .padding(16.dp)
        ) {
            androidx.compose.material3.Text("SYSTEM STATUS", color = ColorPri, style = androidx.compose.material3.MaterialTheme.typography.labelSmall)
            androidx.compose.material3.Text("BATTERY: $batteryLevel%", color = ColorText)
            Spacer(modifier = Modifier.height(16.dp))
            androidx.compose.material3.Text("TIME", color = ColorPri, style = androidx.compose.material3.MaterialTheme.typography.labelSmall)
            val currentTime = java.text.SimpleDateFormat("HH:mm", java.util.Locale.getDefault()).format(java.util.Date())
            androidx.compose.material3.Text(currentTime, color = ColorGold, style = androidx.compose.material3.MaterialTheme.typography.headlineLarge)
        }

        // Conversation Log (Right side)
        Column(
            modifier = Modifier
                .fillMaxHeight()
                .width(200.dp)
                .align(androidx.compose.ui.Alignment.TopEnd)
                .padding(16.dp)
        ) {
            androidx.compose.material3.Text("CONVERSATION", color = ColorPri, style = androidx.compose.material3.MaterialTheme.typography.labelSmall)
            Spacer(modifier = Modifier.height(8.dp))
            logs.takeLast(10).forEach { log ->
                androidx.compose.material3.Text(
                    text = log,
                    color = if (log.startsWith("Siz:")) ColorText else ColorPri,
                    style = androidx.compose.material3.MaterialTheme.typography.bodySmall,
                    modifier = Modifier.padding(bottom = 4.dp)
                )
            }
        }
    }
}

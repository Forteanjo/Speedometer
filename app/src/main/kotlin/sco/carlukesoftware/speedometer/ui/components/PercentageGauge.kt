package sco.carlukesoftware.speedometer.ui.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import sco.carlukesoftware.speedometer.ui.theme.SpeedometerTheme
import sco.carlukesoftware.speedometer.ui.theme.Track
import kotlin.math.cos
import kotlin.math.sin

/**
 * A composable function that creates a percentage gauge.
 *
 * The gauge displays a visual representation of a percentage value using an arc
 * and a needle. It also displays the percentage value and a descriptive text
 * below the gauge.
 *
 * @param modifier The modifier to be applied to the gauge.
 * @param percentage The percentage value to be displayed (0-100).
 * @param trackColor The color of the gauge's track (background arc). Defaults to `Track`.
 * @param progressColors A list of colors used to create a horizontal gradient for the progress arc.
 * @param innerGradient The color used for the inner radial gradient effect.
 * @param percentageTextColor The color of the percentage text. Defaults to `Color.White`.
 * @param gaugeSize The size of the gauge (width and height). Defaults to `240.dp`.
 * @param trackWidth The width of the gauge's track. Defaults to `50f`.
 * @param needleLength The length of the gauge's needle. Defaults to `160f`.
 * @param needleBaseWidth The width of the needle's base. Defaults to */
@Composable
fun PercentageGauge(
    modifier: Modifier = Modifier,
    percentage: Int,
    trackColor: Color = Track,
    progressColors: List<Color>,
    innerGradient: Color,
    percentageTextColor: Color = Color.White,
    gaugeSize: Dp = 240.dp,
    trackWidth: Float = 50f,
    needleLength: Float = 160f,
    needleBaseWidth: Float = 10f,
    sweepAngle: Float = 240f,
    startAngle: Float = 150f,
    percentageText: String = "Percentage"
) {
    // Validate the input percentage
    val validatedPercentage = percentage.coerceIn(0, 100)
    val fillSweepAngle = (validatedPercentage / 100f) * sweepAngle

    Box(
        modifier = modifier
            .size(gaugeSize)
    ) {
        Canvas(
            modifier = Modifier
                .fillMaxSize()
        ) {
            val height = size.height
            val width = size.width
            val arcHeight = height - 20.dp.toPx()

            drawArc(
                color = trackColor,
                startAngle = startAngle,
                sweepAngle = sweepAngle,
                useCenter = false,
                topLeft = Offset(
                    x = (width - height + 60f) / 2f,
                    y = (height - arcHeight) / 2f
                ),
                size = Size(
                    width = arcHeight,
                    height = arcHeight
                ),
                style = Stroke(
                    width = trackWidth,
                    cap = StrokeCap.Round
                )
            )

            drawArc(
                brush = Brush
                    .horizontalGradient(progressColors),
                startAngle = startAngle,
                sweepAngle = fillSweepAngle,
                useCenter = false,
                topLeft = Offset(
                    x = (width - height + 60f) / 2f,
                    y = (height - arcHeight) / 2
                ),
                size = Size(
                    width = arcHeight,
                    height = arcHeight
                ),
                style = Stroke(
                    width = 50f,
                    cap = StrokeCap.Round
                )
            )

            drawCircle(
                brush = Brush
                    .radialGradient(
                        colors = listOf(
                            innerGradient
                                .copy(alpha = 0.2f),
                            Color.Transparent
                        )
                    ),
                radius = width / 2f
            )

            // Draw the center circle
            val centerOffset = Offset(
                x = width / 2f,
                y = height / 2.09f
            )
            drawCircle(
                color = Color.White,
                radius = 24f,
                center = centerOffset
            )

            drawCircle(
                color = Color.White,
                radius = 24f,
                center = centerOffset
            )

            // Draw the needle
            drawNeedle(
                center = centerOffset,
                needleAngle = (validatedPercentage / 100f) * sweepAngle + startAngle,
                needleLength = needleLength,
                needleBaseWidth = needleBaseWidth,
                needleColor = Color.White
            )
        }

        Column(
            modifier = Modifier
                .padding(
                    bottom = 5.dp
                )
                .align(
                    Alignment
                        .BottomCenter
                ),
            horizontalAlignment = Alignment
                .CenterHorizontally
        ) {
            Text(
                text = "$validatedPercentage %",
                style = TextStyle(
                    fontSize = 20.sp,
                    lineHeight = 28.sp,
                    color = percentageTextColor,
                    fontWeight = FontWeight.Bold
                )
            )

            Text(
                text = percentageText,
                fontSize = 16.sp,
                lineHeight = 24.sp,
                color = Color(0xFFB0B4CD)
            )
        }
    }
}

/**
 * Draws a needle shape on a given DrawScope.
 *
 * The needle is represented as a triangle, with the tip extending outwards from the
 * center and the base at the center. The needle is drawn based on the provided angle,
 * length, and base width.
 *
 * @param center The center point from which the needle extends.
 * @param needleAngle The angle of the needle in degrees, where 0 degrees is to the right,
 *                    90 degrees is downwards, and so on, in a clockwise direction.
 * @param needleLength The length of the needle from the center to its tip.
 * @param needleBaseWidth The width of the needle at its base.
 * @param needleColor The color of the needle.
 */
private fun DrawScope.drawNeedle(
    center: Offset,
    needleAngle: Float,
    needleLength: Float,
    needleBaseWidth: Float,
    needleColor: Color
) {
    val needlePath = Path()
        .apply {
            val topX = center.x + needleLength * cos(needleAngle.toRadians())
            val topY = center.y + needleLength * sin(needleAngle.toRadians())

            val baseLeftX = center.x + needleBaseWidth * cos((needleAngle - 90).toRadians())
            val baseLeftY = center.y + needleBaseWidth * sin((needleAngle - 90).toRadians())
            val baseRightX = center.x + needleBaseWidth * cos((needleAngle + 90).toRadians())
            val baseRightY = center.y + needleBaseWidth * sin((needleAngle + 90).toRadians())

            moveTo(x = topX, y = topY)
            lineTo(x = baseLeftX, y = baseLeftY)
            lineTo(x = baseRightX, y = baseRightY)
            close()
        }

    drawPath(
        color = needleColor,
        path = needlePath
    )
}

fun Float.toRadians(): Float = Math.toRadians(this.toDouble()).toFloat()

@Preview(showBackground = true)
@Composable
private fun PercentageGaugePreview() {
    SpeedometerTheme {
        Surface {
            PercentageGauge(
                modifier = Modifier
                    .background(color = Color.Black
                    ),
                percentage = 75,
                progressColors = listOf(Color.Blue, Color.Red),
                innerGradient = Color.Green
            )
        }
    }
}

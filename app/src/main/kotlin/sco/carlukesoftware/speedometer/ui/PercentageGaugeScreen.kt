package sco.carlukesoftware.speedometer.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import sco.carlukesoftware.speedometer.ui.components.PercentageGauge

@Composable
fun PercentageGaugeScreen(
    modifier: Modifier = Modifier
) {
    var currentPercentage by remember {
        mutableIntStateOf(0)
    }

    Column(
        modifier = modifier
            .fillMaxSize(),
        verticalArrangement = Arrangement
            .Center,
        horizontalAlignment = Alignment
            .CenterHorizontally
    ) {
        PercentageGauge(
            percentage = currentPercentage,
            progressColors = listOf(
                Color.Blue,
                Color.Red
            ),
            innerGradient = Color.Green
        )

        Spacer(
            modifier = Modifier
                .height(20.dp)
        )

        Row(
            horizontalArrangement = Arrangement
                .Center
        ) {
            // Decrease percentage button
            Button(
                onClick = {
                    currentPercentage = maxOf(0, currentPercentage - 10)
                }
            ) {
                Text(
                    text = "Increase percentage"
                )
            }

            Spacer(
                modifier = Modifier
                    .width(24.dp)
                    .height(20.dp)
            )

            // Increase percentage button
            Button(
                onClick = {
                    currentPercentage = minOf(100, currentPercentage + 10)
                }
            ) {
                Text(
                    text = "Increase Percentage"
                )
            }
        }
    }
}

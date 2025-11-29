package com.honeycomb.disciplineapp.presentation.utils

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.animateOffsetAsState
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.awaitFirstDown
import androidx.compose.foundation.gestures.waitForUpOrCancellation
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.BlurEffect
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import com.honeycomb.disciplineapp.AccentBackgroundGradient
import com.honeycomb.disciplineapp.LightBackgroundColor
import com.honeycomb.disciplineapp.WhiteColor
import kotlin.math.roundToInt

enum class ButtonState { Pressed, Idle }

fun Modifier.accentBackgroundColor() = this.then(
    Modifier
        .background(
            brush = Brush.verticalGradient(
                colors = AccentBackgroundGradient
            )
        )
)
fun Modifier.addStandardTopPadding() = this.then(
    Modifier
        .padding(
            top = 16.dp
        )
)
fun Modifier.addStandardHorizontalPadding() = this.then(
    Modifier
        .padding(
            horizontal = 20.dp
        )
)
fun Modifier.addMediumHeight() = this.then(
    Modifier
        .height(44.dp)
)
fun Modifier.addBlurBackground() = this.then(
    Modifier
        .clip(
            shape = RoundedCornerShape(14.dp)
        )
        .border(
            width = 1.dp,
            color = WhiteColor.copy(
                alpha = 0.1f
            ),
            shape = RoundedCornerShape(14.dp)
        )
        .graphicsLayer {
            // Apply blur effect
            renderEffect = BlurEffect(
                radiusX = 10f, radiusY = 10f
            )
        }
        .background(
            color = WhiteColor.copy(
                alpha = 0.02f
            ),
            shape = RoundedCornerShape(14.dp)
        )
)


fun Modifier.bounceClick(
    scaleDown: Float = 0.96f,
    enabled: Boolean = true,
    ignoreOffset: Boolean = true,
    onClick: () -> Unit,
) = composed {
    if (enabled.not()) return@composed Modifier

    var buttonState by remember { mutableStateOf(ButtonState.Idle) }
    val scale by animateFloatAsState(if (buttonState == ButtonState.Pressed) scaleDown else 1f)
    val offsetY by animateOffsetAsState(if (buttonState == ButtonState.Pressed) Offset(0f, 14f) else Offset.Zero)
    val haptic = LocalHapticFeedback.current

    this
        .graphicsLayer {
            scaleX = scale
            scaleY = scale
        }
        .then(
            if (ignoreOffset) Modifier else Modifier.offset {
                IntOffset(offsetY.x.roundToInt(), offsetY.y.roundToInt())
            }
        )
        .clickable(
            enabled = enabled,
            interactionSource = remember { MutableInteractionSource() },
            indication = null,
            onClick = {
                haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                onClick()
            }
        )
        .pointerInput(buttonState) {
            awaitPointerEventScope {
                buttonState = if (buttonState == ButtonState.Pressed) {
                    waitForUpOrCancellation()
                    ButtonState.Idle
                } else {
                    awaitFirstDown(false)
                    ButtonState.Pressed
                }
            }
        }
}

fun Modifier.shimmer(
    shimmerColors: List<Color> = listOf(
        Color(0xFF1C1C1C),
        LightBackgroundColor,
        Color(0xFF1C1C1C),
    )
) = composed {
    val transition = rememberInfiniteTransition(label = "")
    val translateAnim = transition.animateFloat(
        initialValue = 0f,
        targetValue = 1000f,
        animationSpec = infiniteRepeatable(
            animation = tween(
                durationMillis = 1000,
                easing = FastOutSlowInEasing
            ),
            repeatMode = RepeatMode.Reverse
        ), label = ""
    )

    val brush = Brush.linearGradient(
        colors = shimmerColors,
        start = Offset.Zero,
        end = Offset(x = translateAnim.value, y = translateAnim.value)
    )

    Modifier.background(brush)
}



fun Modifier.dashedBorder(
    strokeWidth: Dp,
    color: Color,
    cornerRadius: Dp,
    dashOn: Float = 10f,
    dashOff: Float = 8f
): Modifier = this.then(
    Modifier.drawBehind {
        val stroke = strokeWidth.toPx()
        val corner = cornerRadius.toPx()
        val halfStroke = stroke / 2f
        val pathEffect = PathEffect.dashPathEffect(floatArrayOf(dashOn, dashOff))
        val rectSize = Size(
            width = size.width - stroke,
            height = size.height - stroke
        )
        drawRoundRect(
            color = color,
            topLeft = Offset(halfStroke, halfStroke),
            size = rectSize,
            cornerRadius = CornerRadius(corner, corner),
            style = Stroke(width = stroke, pathEffect = pathEffect)
        )
    }
)

@Composable
fun Modifier.clickableWithoutRipple(onClick: () -> Unit): Modifier = this.then(
    Modifier
        .background(Color.Transparent)
        .noRippleClickable(onClick)
)

@Composable
fun Modifier.noRippleClickable(onClick: () -> Unit): Modifier {
    val interaction = remember { androidx.compose.foundation.interaction.MutableInteractionSource() }
    return this.then(
        Modifier
            .clickable(
                indication = null,
                interactionSource = interaction
            ) {
                onClick()
            }
    )
}
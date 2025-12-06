package com.honeycomb.disciplineapp.presentation.ui.common

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.honeycomb.disciplineapp.LightBackgroundColor
import com.honeycomb.disciplineapp.WhiteColor
import com.honeycomb.disciplineapp.data.dto.ButtonDto

@Composable
fun ButtonComponent(
    button: ButtonDto?,
    onClick: () -> Unit,
    endIconComposable: @Composable () -> Unit = {},
    startIconComposable: @Composable () -> Unit = {},
    modifier: Modifier = Modifier,
    state: CustomButtonState = CustomButtonState.Enabled
) {
    val type = ButtonDto.ButtonType.fromString(button?.type)
    when (type) {
        ButtonDto.ButtonType.PRIMARY_BUTTON -> {
            CustomButton(
                type = type,
                modifier = modifier,
                text = button?.title.orEmpty(),
                endIconComposable = endIconComposable,
                startIconComposable = startIconComposable,
                onClick = {
                    onClick()
                },
                state = state
            )
        }
        ButtonDto.ButtonType.SECONDARY_BUTTON -> {
            CustomButton(
                type = type,
                modifier = modifier,
                text = button?.title.orEmpty(),
                endIconComposable = endIconComposable,
                startIconComposable = startIconComposable,
                onClick = {
                    onClick()
                },
                backgroundColor = LightBackgroundColor,
                state = state,
                borderColor = LightBackgroundColor
            )
        }
        ButtonDto.ButtonType.TERTIARY_BUTTON -> {
            CustomButton(
                type = type,
                modifier = modifier,
                text = button?.title.orEmpty(),
                endIconComposable = endIconComposable,
                startIconComposable = startIconComposable,
                onClick = {
                    onClick()
                },
                backgroundColor = WhiteColor,
                state = state,
                borderColor = WhiteColor,
                textColor = Color(0xFF121212)
            )
        }
        ButtonDto.ButtonType.PRIMARY_SMALL_BUTTON -> {
            CustomSmallButton(
                text = button?.title.orEmpty(),
                endIconComposable = endIconComposable,
                startIconComposable = startIconComposable,
                onClick = onClick,
                state = state
            )
        }
        ButtonDto.ButtonType.SECONDARY_SMALL_BUTTON -> {
            CustomSmallButton(
                text = button?.title.orEmpty(),
                endIconComposable = endIconComposable,
                startIconComposable = startIconComposable,
                backgroundColor = LightBackgroundColor,
                onClick = onClick,
                state = state
            )
        }
        null, ButtonDto.ButtonType.NONE -> Unit
    }
}
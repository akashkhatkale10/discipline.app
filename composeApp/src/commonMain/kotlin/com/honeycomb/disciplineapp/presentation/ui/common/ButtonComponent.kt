package com.honeycomb.disciplineapp.presentation.ui.common

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.honeycomb.disciplineapp.LightBackgroundColor
import com.honeycomb.disciplineapp.data.dto.ButtonDto

@Composable
fun ButtonComponent(
    button: ButtonDto?,
    onClick: () -> Unit,
    endIconComposable: @Composable () -> Unit = {},
    startIconComposable: @Composable () -> Unit = {},
    modifier: Modifier = Modifier,
) {
    val type = ButtonDto.ButtonType.fromString(button?.type)
    when (type) {
        ButtonDto.ButtonType.PRIMARY_BUTTON -> {
            CustomButton(
                modifier = modifier,
                text = button?.type.orEmpty(),
                endIconComposable = endIconComposable,
                startIconComposable = startIconComposable,
                onClick = {
                    onClick()
                }
            )
        }
        ButtonDto.ButtonType.SECONDARY_BUTTON -> {
            CustomButton(
                modifier = modifier,
                text = button?.type.orEmpty(),
                endIconComposable = endIconComposable,
                startIconComposable = startIconComposable,
                onClick = {
                    onClick()
                },
                backgroundColor = LightBackgroundColor
            )
        }
        ButtonDto.ButtonType.PRIMARY_SMALL_BUTTON -> {
            CustomSmallButton(
                text = button?.title.orEmpty(),
                endIconComposable = endIconComposable,
                startIconComposable = startIconComposable,
            )
        }
        ButtonDto.ButtonType.SECONDARY_SMALL_BUTTON -> {
            CustomSmallButton(
                text = button?.title.orEmpty(),
                endIconComposable = endIconComposable,
                startIconComposable = startIconComposable,
                backgroundColor = LightBackgroundColor
            )
        }
        null, ButtonDto.ButtonType.NONE -> Unit
    }
}
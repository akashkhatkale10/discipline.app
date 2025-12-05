package com.honeycomb.disciplineapp.presentation.ui.common

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.sp
import androidx.compose.ui.util.fastForEachIndexed
import com.honeycomb.disciplineapp.CustomTextStyle
import com.honeycomb.disciplineapp.data.dto.StyleText
import com.honeycomb.disciplineapp.nunitoFontFamily

@Composable
fun StyleText(
    modifier: Modifier = Modifier,
    style: List<StyleText>?,
    delimiter: String? = null,
    concatenator: String? = null,
    textAlign: TextAlign = TextAlign.Start,
    lineHeight: Int = 30,
    textDecoration: TextDecoration? = null
) {
    style?.let {
        val string = buildAnnotatedString {
            it.fastForEachIndexed { index, item ->
                item?.let {
                    withStyle(
                        style = SpanStyle(
                            color = Color(item.color?.toColorInt()?.toLong() ?: 0xFFFFFFFF),
                            fontWeight = getFontWeight(item.fontWeight ?: FONT_WEIGHT_MEDIUM),
                            fontSize = item.fontSize?.sp ?: 0.sp,
                            fontFamily = nunitoFontFamily
                        )
                    ) {
                        append(concatenator?.let { "$it " } ?: "")
                        append(item.text)
                        if (index < style.size - 1) {
                            delimiter?.let { append(it) }
                        }
                    }
                }
            }
        }
        Text(
            text = string,
            style = CustomTextStyle.copy(
                textAlign = textAlign,
                lineHeight = lineHeight.sp,
                textDecoration = textDecoration
            ),
            modifier = modifier
        )
    }
}

fun String.toColorInt(): Int? {
    try {
        if (this[0] == '#') {
            var color = substring(1).toLong(16)
            if (length == 7) {
                color = color or 0x00000000ff000000L
            } else if (length != 9) {
                return null
            }
            return color.toInt()
        } else {
            return null
        }
    } catch (e: Exception) {
        return null
    }
}

const val FONT_WEIGHT_MEDIUM = "medium"
const val FONT_WEIGHT_NORMAL = "regular"
const val FONT_WEIGHT_SEMIBOLD = "semibold"
const val FONT_WEIGHT_LIGHT = "light"
const val FONT_WEIGHT_BOLD = "bold"

fun getFontWeight(
    weight: String
): FontWeight {
    return when (weight) {
        FONT_WEIGHT_BOLD -> FontWeight.Bold
        FONT_WEIGHT_MEDIUM -> FontWeight.Medium
        FONT_WEIGHT_NORMAL -> FontWeight.Normal
        FONT_WEIGHT_SEMIBOLD -> FontWeight.SemiBold
        FONT_WEIGHT_LIGHT -> FontWeight.Light
        else -> FontWeight.Medium
    }
}
package com.mefy.platemate.presentation.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import com.mefy.platemate.R
import com.mefy.platemate.presentation.theme.PMTheme
import com.mefy.platemate.presentation.theme.PlateMateTheme

@Composable
fun PMCommentField(
    value: String,
    onValueChange: (String) -> Unit,
    maxLength: Int,
    modifier: Modifier = Modifier,
    placeholder: String? = null,
    height: Dp = PMTheme.sizing.commentFieldHeight,
) {
    val spacing = PMTheme.spacing
    val colors = PMTheme.colors
    val fontSize = PMTheme.fontSize

    val isMaxReached = value.length >= maxLength

    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(spacing.s4)
    ) {
        PMTextField(
            value = value,
            onValueChange = onValueChange,
            modifier = Modifier
                .fillMaxWidth()
                .height(height),
            placeholder = placeholder,
            singleLine = false,
        )

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = spacing.s4),
            verticalAlignment = Alignment.CenterVertically
        ) {
            if (isMaxReached) {
                PMText(
                    text = stringResource(R.string.review_comment_max_reached),
                    color = colors.error,
                    fontSize = fontSize.sm,
                    modifier = Modifier.weight(1f)
                )
            } else {
                Spacer(modifier = Modifier.weight(1f))
            }
            Spacer(modifier = Modifier.width(spacing.s8))
            PMText(
                text = stringResource(R.string.review_comment_counter, value.length, maxLength),
                color = if (isMaxReached) colors.error else colors.textLabel
            )
        }
    }
}

@Preview(name = "PMCommentField Light", showBackground = true, backgroundColor = 0xFFFFFFFF)
@Composable
private fun PMCommentFieldLightPreview() {
    val spacing = PMTheme.spacing
    PlateMateTheme(darkTheme = false, dynamicColor = false) {
        PMCommentField(
            value = "Çok nazik bir sürücü, yol verdi.",
            onValueChange = {},
            maxLength = 160,
            modifier = Modifier.padding(spacing.s16)
        )
    }
}

@Preview(name = "PMCommentField Max", showBackground = true, backgroundColor = 0xFFFFFFFF)
@Composable
private fun PMCommentFieldMaxPreview() {
    val spacing = PMTheme.spacing
    PlateMateTheme(darkTheme = false, dynamicColor = false) {
        PMCommentField(
            value = "a".repeat(160),
            onValueChange = {},
            maxLength = 160,
            modifier = Modifier.padding(spacing.s16)
        )
    }
}

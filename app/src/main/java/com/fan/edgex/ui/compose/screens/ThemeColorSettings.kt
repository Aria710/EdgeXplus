package com.fan.edgex.ui.compose.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.fan.edgex.R
import com.fan.edgex.config.ThemeColorResolver
import com.fan.edgex.ui.compose.components.EdgeXListGroup
import com.fan.edgex.ui.compose.components.EdgeXIcons
import com.fan.edgex.ui.compose.components.EdgeXRow
import com.fan.edgex.ui.compose.theme.LocalEdgeXColors

@Composable
internal fun ThemeColorSettingsCard(
    configKey: String,
    customColorTitle: String,
    followThemeColor: Boolean,
    customColor: Color,
    onFollowThemeColorChange: (Boolean) -> Unit,
    onCustomColorChange: (Color) -> Unit,
    testTagPrefix: String,
    modifier: Modifier = Modifier,
) {
    EdgeXListGroup(modifier = modifier.testTag("${testTagPrefix}_color_settings")) {
        ThemeColorSettingRow(
            configKey = configKey,
            customColorTitle = customColorTitle,
            followThemeColor = followThemeColor,
            customColor = customColor,
            onFollowThemeColorChange = onFollowThemeColorChange,
            onCustomColorChange = onCustomColorChange,
            testTagPrefix = testTagPrefix,
        )
    }
}

@Composable
internal fun ThemeColorSettingRow(
    configKey: String,
    customColorTitle: String,
    followThemeColor: Boolean,
    customColor: Color,
    onFollowThemeColorChange: (Boolean) -> Unit,
    onCustomColorChange: (Color) -> Unit,
    testTagPrefix: String,
) {
    val context = LocalContext.current
    val colors = LocalEdgeXColors.current
    val displayedColor = if (followThemeColor) colors.accent else customColor
    EdgeXRow(
        title = customColorTitle,
        subtitle = if (followThemeColor) {
            stringResource(R.string.compose_panel_color_follow_theme)
        } else {
            customColor.toArgbHex()
        },
        icon = EdgeXIcons.Theme,
        modifier = Modifier.testTag("${testTagPrefix}_color_setting"),
        onClick = {
            ColorPickerDialog.show(
                context = context,
                title = customColorTitle,
                configKey = configKey,
                defaultColor = colors.accent.toArgbHex(),
                allowReset = true,
                resetLabelRes = R.string.compose_panel_color_follow_theme,
                onReset = { onFollowThemeColorChange(true) },
            ) { picked -> onCustomColorChange(Color(picked)) }
        },
    ) {
        Box(
            modifier = Modifier
                .testTag("${testTagPrefix}_color_swatch")
                .size(36.dp)
                .clip(RoundedCornerShape(12.dp))
                .background(displayedColor),
        )
    }
}

internal fun Color.toArgbHex(): String = ThemeColorResolver.formatArgb(toArgb())

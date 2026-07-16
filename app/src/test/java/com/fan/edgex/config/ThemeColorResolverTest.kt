package com.fan.edgex.config

import org.junit.Assert.assertEquals
import org.junit.Test

class ThemeColorResolverTest {
    @Test
    fun emptySurfaceColorFollowsCurrentUiAccent() {
        val values = mapOf(AppConfig.UI_ACCENT to "blue")

        assertEquals(0xFF3B6CE5.toInt(), resolve(AppConfig.CUSTOM_PANEL_COLOR, values))
    }

    @Test
    fun customArgbPreservesAlpha() {
        val values = mapOf(AppConfig.CUSTOM_PANEL_COLOR to "#80336699")

        assertEquals(0x80336699.toInt(), resolve(AppConfig.CUSTOM_PANEL_COLOR, values))
    }

    @Test
    fun invalidSurfaceColorFallsBackToLegacyThemePreset() {
        val values = mapOf(
            AppConfig.CUSTOM_PANEL_COLOR to "not-a-color",
            AppConfig.THEME_PRESET to "ocean",
        )

        assertEquals(0xFF2F6F8F.toInt(), resolve(AppConfig.CUSTOM_PANEL_COLOR, values))
    }

    @Test
    fun panelColorKeysRemainIndependent() {
        val values = mapOf(
            AppConfig.UI_ACCENT to "green",
            AppConfig.CUSTOM_PANEL_COLOR to "#40112233",
            AppConfig.SIDE_BAR_LEFT_COLOR to "#C0445566",
            AppConfig.SIDE_BAR_RIGHT_COLOR to "#E0778899",
        )

        assertEquals(0x40112233, resolve(AppConfig.CUSTOM_PANEL_COLOR, values))
        assertEquals(0xC0445566.toInt(), resolve(AppConfig.SIDE_BAR_LEFT_COLOR, values))
        assertEquals(0xE0778899.toInt(), resolve(AppConfig.SIDE_BAR_RIGHT_COLOR, values))
        assertEquals("custom_panel_color", AppConfig.CUSTOM_PANEL_COLOR)
        assertEquals("side_bar_left_color", AppConfig.SIDE_BAR_LEFT_COLOR)
        assertEquals("side_bar_right_color", AppConfig.SIDE_BAR_RIGHT_COLOR)
    }

    private fun resolve(key: String, values: Map<String, String>): Int =
        ThemeColorResolver.resolveConfiguredColor(key) { values[it].orEmpty() }
}

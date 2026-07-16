package com.fan.edgex.config

/** Resolves UI theme and optional per-surface colors in both app and hook processes. */
object ThemeColorResolver {
    private const val DEFAULT_ACCENT = 0xFF2F8A3E.toInt()

    private val uiAccents = mapOf(
        "green" to DEFAULT_ACCENT,
        "blue" to 0xFF3B6CE5.toInt(),
        "coral" to 0xFFDD5A48.toInt(),
        "violet" to 0xFF7B4FE0.toInt(),
        "amber" to 0xFFC68A1A.toInt(),
    )

    private val legacyPresets = mapOf(
        "default" to 0xFF326D32.toInt(),
        "classic" to 0xFF00796B.toInt(),
        "cedar" to 0xFF496B3D.toInt(),
        "ocean" to 0xFF2F6F8F.toInt(),
        "ember" to 0xFFC56B2A.toInt(),
    )

    fun resolveConfiguredColor(configKey: String, resolveConfig: (String) -> String): Int =
        parseColorOrNull(resolveConfig(configKey)) ?: resolveThemeColor(resolveConfig)

    fun resolveThemeColor(resolveConfig: (String) -> String): Int {
        val uiAccent = resolveConfig(AppConfig.UI_ACCENT)
        if (uiAccent == "custom") {
            parseColorOrNull(resolveConfig(AppConfig.THEME_CUSTOM_COLOR))?.let { return it }
        } else {
            uiAccents[uiAccent]?.let { return it }
        }

        val preset = resolveConfig(AppConfig.THEME_PRESET).ifBlank { "default" }
        if (preset == "custom") {
            parseColorOrNull(resolveConfig(AppConfig.THEME_CUSTOM_COLOR))?.let { return it }
        }
        return legacyPresets[preset] ?: DEFAULT_ACCENT
    }

    fun parseColorOrNull(value: String): Int? {
        val hex = value.trim().removePrefix("#")
        return runCatching {
            when (hex.length) {
                6 -> (0xFF000000L or hex.toLong(16)).toInt()
                8 -> hex.toLong(16).toInt()
                else -> null
            }
        }.getOrNull()
    }

    fun formatArgb(color: Int): String =
        "#%08X".format(color.toLong() and 0xFFFFFFFFL)
}

package com.accu.cityweather.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material.MaterialTheme
import androidx.compose.material.darkColors
import androidx.compose.material.lightColors
import androidx.compose.runtime.Composable

private val DarkColorPalette = darkColors(
    primary = DarkOrange,
    primaryVariant = MainOrangeVariant,
    secondary = OrangeSecondary
)

private val LightColorPalette = lightColors(
    primary = MainOrange,
    primaryVariant = MainOrangeVariant,
    secondary = OrangeSecondary

)

@Composable
fun CityWeatherTheme(darkTheme: Boolean = isSystemInDarkTheme(), content: @Composable () -> Unit) {
    val colors = if (darkTheme) {
        DarkColorPalette
    } else {
        LightColorPalette
    }

    MaterialTheme(
        colors = colors,
        shapes = Shapes,
        content = content
    )
}

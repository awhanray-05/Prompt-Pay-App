package com.example.promptpayapp.ui.theme

import android.app.Activity
import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext

private val DarkColorScheme = darkColorScheme(
    primary = Purple80,
    secondary = PurpleGrey80,
    tertiary = Pink80
)

private val LightColorScheme = lightColorScheme(
    primary = Purple40,
    secondary = PurpleGrey40,
    tertiary = Pink40

    /* Other default colors to override
    background = Color(0xFFFFFBFE),
    surface = Color(0xFFFFFBFE),
    onPrimary = Color.White,
    onSecondary = Color.White,
    onTertiary = Color.White,
    onBackground = Color(0xFF1C1B1F),
    onSurface = Color(0xFF1C1B1F),
    */
)

// 🌙 Dark Theme Color Palette
private val DarkColorPalette = darkColorScheme(
    primary = Color(0xFF212121),  // Dark Gray
    secondary = Color(0xFF424242), // Medium Gray
    background = Color(0xFF0A192F), // Almost Black with a Blue Tint
    surface = Color(0xFF112240),  // Dark Blue Surface for UI Elements
    onPrimary = Color(0xFFE0E0E0),  // Light text on dark
    onSecondary = Color(0xFFBBE1FA), // Soft Blue Accent for Highlights
    onBackground = Color(0xFFDFE7F2), // Light Blue-Gray for Readable Text
    onSurface = Color(0xFFEAEAEA),  // Very Light Gray for Text on Surfaces
    primaryContainer = Color(0xFF233554), // Slightly Lighter Blue for UI Elements
    secondaryContainer = Color(0xFF30475E) // Muted Dark Blue for Subtle Accents
)

@Composable
fun PromptPayAppTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    // Dynamic color is available on Android 12+
    dynamicColor: Boolean = true,
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }

        darkTheme -> DarkColorPalette // Your Dark Color Theme
        else -> LightColorScheme
    }

    MaterialTheme(
//        colorScheme = colorScheme,]
        colorScheme = DarkColorPalette,
        typography = Typography,
        content = content
    )
}
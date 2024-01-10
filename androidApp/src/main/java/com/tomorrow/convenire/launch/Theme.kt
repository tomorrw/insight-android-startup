package com.tomorrow.convenire.launch

import android.app.Activity
import android.content.pm.ActivityInfo
import android.content.res.Configuration
import androidx.appcompat.app.AppCompatDelegate
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material.Typography
import androidx.compose.material.darkColors
import androidx.compose.material.lightColors
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Shapes
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.core.view.WindowCompat
import com.tomorrow.convenire.R

val appColors = lightColors(
    primary = Color(0xFF113F67),
    primaryVariant = Color(0xFF6594BD),
    secondary = Color(0xFF46BFD3),
    secondaryVariant = Color(0xFF5495AA),
    background = Color(0xFFEEF5FC),
    surface = Color(0xFFEEF5FC),
    error = Color(0xFFFF9494)
)

val appColorsMaterial3 = lightColorScheme(
    primary = Color(0xFF113F67),
    onPrimary = Color(0xFFFFFFFF),
    primaryContainer = Color(0xFF2F3D45),
    onPrimaryContainer = Color(0xFF0E191E),
    secondary = Color(0xFF6594BD),
    onSecondary = Color(0xFFFFFFFF),
    secondaryContainer = Color(0xFF5495AA),
    onSecondaryContainer = Color(0xFF5495AA),
    background = Color(0xFFFFFFFF),
    error = Color(0xFFFF9494),
    surface = Color(0xFFEEF5FC),
    onSurface = Color(0xFF113F67),
    surfaceVariant = Color(0xFF46BFD3),
    onSurfaceVariant = Color(0xFF6594BD),
    surfaceTint = Color(0xFF113F67),
    outline = Color(0xFFDAE6F1),

    )
val appColorsDark = darkColors(
    primary = Color(0xFFFFFFFF),
    primaryVariant = Color(0xFF959EAD),
    secondary = Color(0xFF56C6C1),
    secondaryVariant = Color(0xFF46BFD3),
    background = Color(0xFF2F3D45),
    surface = Color(0xFF0E191E),
    error = Color(0xFFFF9494)
)

val appColorsMaterial3Dark = darkColorScheme(
    primary = Color(0xFFFFFFFF),
    onPrimary = Color(0xFF2F3D45),
    primaryContainer = Color(0xFF2F3D45),
    onPrimaryContainer = Color(0xFF0E191E),
    secondary = Color(0xFF56C6C1),
    onSecondary = Color(0xFF2F3D45),
    secondaryContainer = Color(0xFF5495AA),
    onSecondaryContainer = Color(0xFF5495AA),
    background = Color(0xFF1E2B32),
    error = Color(0xFFFF9494),
    surface = Color(0xFF0E191E),
    onSurface = Color(0xFFFFFFFF),
    surfaceVariant = Color(0xFF46BFD3),
    onSurfaceVariant = Color(0xFF959EAD),
    surfaceTint = Color(0xFF13242C),
    outline = Color(0xFF959EAD)
)

val typographyMaterial3 = androidx.compose.material3.Typography().let {
    it.copy(
        displayLarge = it.displayLarge.copy(fontFamily = FontFamily(Font(R.font.product_sans_regular))),
        displayMedium = it.displayMedium.copy(fontFamily = FontFamily(Font(R.font.product_sans_regular))),
        displaySmall = it.displaySmall.copy(fontFamily = FontFamily(Font(R.font.product_sans_regular))),
        headlineLarge = it.headlineLarge.copy(fontFamily = FontFamily(Font(R.font.product_sans_regular))),
        headlineMedium = it.headlineMedium.copy(fontFamily = FontFamily(Font(R.font.product_sans_regular))),
        headlineSmall = it.headlineSmall.copy(fontFamily = FontFamily(Font(R.font.product_sans_regular))),
        titleLarge = it.titleLarge.copy(fontFamily = FontFamily(Font(R.font.product_sans_regular))),
        titleMedium = it.titleMedium.copy(fontFamily = FontFamily(Font(R.font.product_sans_regular))),
        titleSmall = it.titleSmall.copy(fontFamily = FontFamily(Font(R.font.product_sans_regular))),
        bodyLarge = it.bodyLarge.copy(fontFamily = FontFamily(Font(R.font.product_sans_regular))),
        bodyMedium = it.bodyMedium.copy(fontFamily = FontFamily(Font(R.font.product_sans_regular))),
        bodySmall = it.bodySmall.copy(fontFamily = FontFamily(Font(R.font.product_sans_regular))),
        labelLarge = it.labelLarge.copy(fontFamily = FontFamily(Font(R.font.product_sans_regular))),
        labelMedium = it.labelMedium.copy(fontFamily = FontFamily(Font(R.font.product_sans_regular))),
        labelSmall = it.labelSmall.copy(fontFamily = FontFamily(Font(R.font.product_sans_regular)))
    )
}

val typography = Typography(defaultFontFamily = FontFamily(Font(R.font.product_sans_regular)))

@Composable
fun JetpackComposeDarkThemeTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colors = if (darkTheme) {
        appColorsMaterial3Dark
    } else {
        appColorsMaterial3
    }
    val materialThemeColors = if (darkTheme) {
        appColorsDark
    } else {
        appColors
    }
    MaterialTheme(
        colorScheme = colors,
        typography = typographyMaterial3,
    ) {
        androidx.compose.material.MaterialTheme(materialThemeColors, typography = typography) {
            content()
        }
    }

    val view = LocalView.current
    val context = LocalContext.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            window.statusBarColor = colors.surface.toArgb()
            window.navigationBarColor = colors.surface.toArgb()
            window.colorMode = if (darkTheme)
                ActivityInfo.COLOR_MODE_DEFAULT
            else
                ActivityInfo.COLOR_MODE_DEFAULT

            WindowCompat.getInsetsController(window, view).let {
                it.isAppearanceLightNavigationBars = !darkTheme
                it.isAppearanceLightStatusBars = !darkTheme

            }
            AppCompatDelegate.setDefaultNightMode(
                if (!darkTheme) AppCompatDelegate.MODE_NIGHT_YES else AppCompatDelegate.MODE_NIGHT_NO
            )
        }
    }

}
package com.tomorrow.convenire.launch

import androidx.compose.material.Typography
import androidx.compose.material.lightColors
import androidx.compose.material3.lightColorScheme
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import com.tomorrow.convenire.R

val appColors = lightColors(
    primary = Color(0xFF113F67),
    primaryVariant = Color(0xFF6594BD),
    secondary = Color(0xFF46BFD3),
    secondaryVariant = Color(0xFF5495AA),
    background = Color(0xFFFFFFFF),
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
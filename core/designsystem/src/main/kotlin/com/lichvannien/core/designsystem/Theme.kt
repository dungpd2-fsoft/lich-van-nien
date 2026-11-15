package com.lichvannien.core.designsystem

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import com.lichvannien.core.model.ThemeMode

@Composable
fun LvnTheme(mode: ThemeMode = ThemeMode.SYSTEM, content: @Composable () -> Unit) {
    val dark = mode == ThemeMode.DARK || mode == ThemeMode.SYSTEM && isSystemInDarkTheme()
    val colors = if (dark) darkColorScheme(primary = Color(0xFFD0BCFF), onPrimary = Color(0xFF381E72), primaryContainer = Color(0xFF4F378B), onPrimaryContainer = Color(0xFFEADDFF), background = Color(0xFF141218), surface = Color(0xFF211F26))
    else lightColorScheme(primary = Color(0xFF6750A4), onPrimary = Color.White, primaryContainer = Color(0xFFEADDFF), onPrimaryContainer = Color(0xFF21005D), background = Color(0xFFFFFBFE), surface = Color(0xFFFFFBFE))
    MaterialTheme(colorScheme = colors, typography = Typography(), content = content)
}

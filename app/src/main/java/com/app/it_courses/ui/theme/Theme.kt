package com.app.it_courses.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Typography
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.colorResource
import com.app.it_courses.R

@Composable
fun ITCoursesTheme(
    content: @Composable () -> Unit
) {
    val colorScheme = darkColorScheme(
        primary = colorResource(id = R.color.green),
        onPrimary = colorResource(id = R.color.white),
        secondary = colorResource(id = R.color.white),
        onSecondary = colorResource(id = R.color.dark),
        background = colorResource(id = R.color.dark),
        onBackground = colorResource(id = R.color.white),
        surface = colorResource(id = R.color.dark_gray),
        onSurface = colorResource(id = R.color.white),
        surfaceVariant = colorResource(id = R.color.light_gray),
        outline = colorResource(id = R.color.stroke)
    )

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography(),
        content = content
    )
}

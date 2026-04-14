package com.app.it_courses

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.app.it_courses.navigation.ITCoursesApp
import com.app.it_courses.ui.theme.ITCoursesTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        val appComponent = (application as ITCoursesApplication).appComponent
        setContent {
            ITCoursesTheme {
                ITCoursesApp(appComponent = appComponent)
            }
        }
    }
}

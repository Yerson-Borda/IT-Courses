package com.app.it_courses.navigation

import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.annotation.StringRes
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.BookmarkBorder
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.app.feature.course.ui.CourseScreen
import com.app.feature.favorites.ui.FavoritesRoute
import com.app.feature.home.ui.HomeRoute
import com.app.feature.login.ui.LoginRoute
import com.app.feature.profile.ui.ProfileScreen
import com.app.it_courses.R
import com.app.it_courses.di.AppComponent

private const val HOME_ROUTE = "home"
private const val FAVORITES_ROUTE = "favorites"
private const val PROFILE_ROUTE = "profile"
private const val COURSE_ROUTE = "course"

private data class BottomBarItem(
    val route: String,
    @StringRes val labelRes: Int,
    val icon: ImageVector
)

@Composable
fun ITCoursesApp(appComponent: AppComponent) {
    val context = LocalContext.current
    var isLoggedIn by rememberSaveable { mutableStateOf(false) }

    if (!isLoggedIn) {
        val loginFactory = remember(appComponent) { appComponent.loginViewModelFactory() }
        LoginRoute(
            viewModelFactory = loginFactory,
            onLoginSuccess = { isLoggedIn = true },
            onOpenVk = { openUrl(context, "https://vk.com/") },
            onOpenOk = { openUrl(context, "https://ok.ru/") }
        )
        return
    }

    val navController = rememberNavController()
    val homeFactory = remember(appComponent) { appComponent.homeViewModelFactory() }
    val favoritesFactory = remember(appComponent) { appComponent.favoritesViewModelFactory() }
    val bottomItems = remember {
        listOf(
            BottomBarItem(
                route = HOME_ROUTE,
                labelRes = R.string.nav_home,
                icon = Icons.Outlined.Home
            ),
            BottomBarItem(
                route = FAVORITES_ROUTE,
                labelRes = R.string.nav_favorites,
                icon = Icons.Outlined.BookmarkBorder
            ),
            BottomBarItem(
                route = PROFILE_ROUTE,
                labelRes = R.string.nav_profile,
                icon = Icons.Outlined.Person
            )
        )
    }

    Scaffold(
        containerColor = MaterialTheme.colorScheme.background,
        bottomBar = {
            AppBottomBar(
                navController = navController,
                items = bottomItems
            )
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = HOME_ROUTE,
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            composable(HOME_ROUTE) {
                HomeRoute(
                    viewModelFactory = homeFactory,
                    onCourseClick = { courseId ->
                        navController.navigate("$COURSE_ROUTE/$courseId")
                    }
                )
            }
            composable(FAVORITES_ROUTE) {
                FavoritesRoute(
                    viewModelFactory = favoritesFactory,
                    onCourseClick = { courseId ->
                        navController.navigate("$COURSE_ROUTE/$courseId")
                    }
                )
            }
            composable(PROFILE_ROUTE) {
                ProfileScreen()
            }
            composable("$COURSE_ROUTE/{courseId}") {
                CourseScreen()
            }
        }
    }
}

@Composable
private fun AppBottomBar(
    navController: NavHostController,
    items: List<BottomBarItem>
) {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route.orEmpty()
    val selectedRoute = when {
        currentRoute.startsWith(FAVORITES_ROUTE) -> FAVORITES_ROUTE
        currentRoute.startsWith(PROFILE_ROUTE) -> PROFILE_ROUTE
        else -> HOME_ROUTE
    }

    Column {
        HorizontalDivider(
            color = Color(0xFF4D555E),
            thickness = 1.5.dp
        )
        NavigationBar(containerColor = Color(0xFF24252A)) {
            items.forEach { item ->
                NavigationBarItem(
                    selected = selectedRoute == item.route,
                    onClick = {
                        if (selectedRoute == item.route) return@NavigationBarItem
                        navController.navigate(item.route) {
                            popUpTo(HOME_ROUTE)
                            launchSingleTop = true
                        }
                    },
                    icon = {
                        Icon(
                            imageVector = item.icon,
                            contentDescription = null
                        )
                    },
                    label = {
                        Text(text = stringResource(item.labelRes))
                    },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = MaterialTheme.colorScheme.primary,
                        selectedTextColor = MaterialTheme.colorScheme.primary,
                        unselectedIconColor = MaterialTheme.colorScheme.onBackground,
                        unselectedTextColor = MaterialTheme.colorScheme.onBackground,
                        indicatorColor = MaterialTheme.colorScheme.background
                    )
                )
            }
        }
    }
}

private fun openUrl(context: Context, url: String) {
    val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
    context.startActivity(intent)
}

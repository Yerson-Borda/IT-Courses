package com.app.it_courses.di

import android.app.Application
import com.app.data.di.RepositoryBindings
import com.app.feature.favorites.ui.FavoritesViewModel
import com.app.feature.home.ui.HomeViewModel
import com.app.feature.login.ui.LoginViewModel
import dagger.BindsInstance
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(
    modules = [
        NetworkModule::class,
        DatabaseModule::class,
        RepositoryBindings::class
    ]
)
interface AppComponent {
    fun loginViewModelFactory(): LoginViewModel.Factory
    fun homeViewModelFactory(): HomeViewModel.Factory
    fun favoritesViewModelFactory(): FavoritesViewModel.Factory

    @Component.Factory
    interface Factory {
        fun create(@BindsInstance application: Application): AppComponent
    }
}

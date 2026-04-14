package com.app.data.di

import com.app.data.repository.AuthRepositoryImpl
import com.app.data.repository.CoursesRepositoryImpl
import com.app.domain.repository.AuthRepository
import com.app.domain.repository.CoursesRepository
import dagger.Binds
import dagger.Module
import javax.inject.Singleton

@Module
interface RepositoryBindings {
    @Binds
    @Singleton
    fun bindAuthRepository(impl: AuthRepositoryImpl): AuthRepository

    @Binds
    @Singleton
    fun bindCoursesRepository(impl: CoursesRepositoryImpl): CoursesRepository
}

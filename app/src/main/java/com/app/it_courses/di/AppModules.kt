package com.app.it_courses.di

import android.app.Application
import androidx.room.Room
import com.app.core.database.AppDatabase
import com.app.core.database.dao.FavoriteCourseDao
import com.app.core.network.AssetCoursesInterceptor
import com.app.core.network.CoursesApi
import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
object NetworkModule {
    @Provides
    @Singleton
    fun provideOkHttpClient(application: Application): OkHttpClient {
        val loggingInterceptor = HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BASIC
        }
        return OkHttpClient.Builder()
            .addInterceptor(AssetCoursesInterceptor(application.applicationContext))
            .addInterceptor(loggingInterceptor)
            .build()
    }

    @Provides
    @Singleton
    fun provideRetrofit(client: OkHttpClient): Retrofit {
        return Retrofit.Builder()
            .baseUrl("https://it-courses.local/")
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    @Provides
    @Singleton
    fun provideCoursesApi(retrofit: Retrofit): CoursesApi {
        return retrofit.create(CoursesApi::class.java)
    }
}

@Module
object DatabaseModule {
    @Provides
    @Singleton
    fun provideDatabase(application: Application): AppDatabase {
        return Room.databaseBuilder(
            application,
            AppDatabase::class.java,
            "it_courses.db"
        ).fallbackToDestructiveMigration().build()
    }

    @Provides
    fun provideFavoriteCourseDao(database: AppDatabase): FavoriteCourseDao {
        return database.favoriteCourseDao()
    }
}

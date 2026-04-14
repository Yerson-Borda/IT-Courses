package com.app.core.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.app.core.database.dao.FavoriteCourseDao
import com.app.core.database.entity.FavoriteCourseEntity

@Database(
    entities = [FavoriteCourseEntity::class],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun favoriteCourseDao(): FavoriteCourseDao
}

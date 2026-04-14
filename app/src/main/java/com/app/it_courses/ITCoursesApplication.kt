package com.app.it_courses

import android.app.Application
import com.app.it_courses.di.AppComponent
import com.app.it_courses.di.DaggerAppComponent

class ITCoursesApplication : Application() {
    lateinit var appComponent: AppComponent
        private set

    override fun onCreate() {
        super.onCreate()
        appComponent = DaggerAppComponent.factory().create(this)
    }
}

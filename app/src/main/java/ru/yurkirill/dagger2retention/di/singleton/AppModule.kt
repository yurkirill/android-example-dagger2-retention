package ru.yurkirill.dagger2retention.di.singleton

import android.app.Application
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class AppModule(private val context: Application) {

    @Singleton
    @Provides
    fun provideApplicationContext() = context.applicationContext!!
}

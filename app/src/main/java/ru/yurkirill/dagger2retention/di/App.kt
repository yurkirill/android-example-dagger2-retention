package ru.yurkirill.dagger2retention.di

import android.app.Application
import ru.yurkirill.dagger2retention.di.singleton.AppModule

/**
 * Точка входа в приложение.
 */
class App : Application() {

    override fun onCreate() {
        super.onCreate()
        _appModule = AppModule(this)
    }
}

private var _appModule: AppModule? = null

val appModule get() = _appModule!!

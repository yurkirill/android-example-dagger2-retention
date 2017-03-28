package ru.yurkirill.dagger2retention

import android.app.Application
import ru.yurkirill.dagger2retention.di.appModule
import ru.yurkirill.dagger2retention.di.singleton.AppModule

/**
 * Точка входа в приложение.
 */
class App : Application() {

    override fun onCreate() {
        super.onCreate()
        appModule = AppModule(this)
    }
}

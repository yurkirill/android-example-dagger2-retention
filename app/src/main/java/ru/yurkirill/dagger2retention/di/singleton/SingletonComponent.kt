package ru.yurkirill.dagger2retention.di.singleton

import dagger.Component
import ru.yurkirill.dagger2retention.di.activity.ActivityComponent
import javax.inject.Singleton

@Singleton
@Component(modules = arrayOf(AppModule::class, NavigationModule::class))
interface SingletonComponent {
    fun activityComponentBuilder(): ActivityComponent.Builder
}

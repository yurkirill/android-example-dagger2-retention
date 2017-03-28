package ru.yurkirill.dagger2retention.di.singleton

import dagger.Binds
import dagger.Module
import ru.yurkirill.dagger2retention.ui.base.navigation.NavigationContract
import ru.yurkirill.dagger2retention.ui.base.navigation.NavigationPresenter
import javax.inject.Singleton

@Module
interface NavigationModule {

    @Singleton
    @Binds
    fun provideNavigationPresenter(presenter: NavigationPresenter): NavigationContract.Presenter
}

package ru.yurkirill.dagger2retention.di.activity

import dagger.Module
import dagger.Provides

@Module
class ActivityModule {

    @ActivityScope
    @Provides
    fun provideString() = String(charArrayOf('a', 'b', 'c'))
}

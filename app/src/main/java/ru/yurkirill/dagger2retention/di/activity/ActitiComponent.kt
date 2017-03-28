package ru.yurkirill.dagger2retention.di.activity

import dagger.Subcomponent
import ru.yurkirill.dagger2retention.di.Component
import ru.yurkirill.dagger2retention.ui.DiaryActivity
import ru.yurkirill.dagger2retention.ui.HomeActivity
import ru.yurkirill.dagger2retention.ui.StatisticActivity

@ActivityScope
@Subcomponent(modules = arrayOf(ActivityModule::class))
interface ActivityComponent : Component {

    @Subcomponent.Builder
    interface Builder {
        fun build(): ActivityComponent
    }

    fun inject(activity: HomeActivity)
    fun inject(activity: DiaryActivity)
    fun inject(activity: StatisticActivity)
}

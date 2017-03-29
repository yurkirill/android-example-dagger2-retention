package ru.yurkirill.dagger2retention.di

import ru.yurkirill.dagger2retention.ui.DiaryActivity
import ru.yurkirill.dagger2retention.ui.HomeActivity
import ru.yurkirill.dagger2retention.ui.StatisticActivity

/**
 * Получение экземпляра ActivityComponent.
 */
private val activityComponent
    get() = singletonComponent.activityComponentBuilder().build()

/**
 * Получение сохраненного экземпляра ActivityComponent.
 * @param retentionId Идентификатор сохранения.
 */
private fun getActivityComponent(retentionId: Long) =
        getComponent(retentionId) { singletonComponent.activityComponentBuilder().build() }

/**
 * Инжектор новых зависимостей в целевые объекты.
 */
fun <T> T.inject() {
    when (this) {
        is DiaryActivity -> activityComponent.inject(this)
    }
}

/**
 * Инжектор сохраненных зависимостей в целевые объекты.
 * @param retentionId Идентификатор сохранения.
 */
fun <T> T.inject(retentionId: Long) {
    when (this) {
        is HomeActivity -> getActivityComponent(retentionId).inject(this)
        is StatisticActivity -> getActivityComponent(retentionId).inject(this)
    }
}

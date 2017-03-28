package ru.yurkirill.dagger2retention.di

import ru.yurkirill.dagger2retention.di.singleton.AppModule
import ru.yurkirill.dagger2retention.di.singleton.DaggerSingletonComponent
import ru.yurkirill.dagger2retention.di.singleton.SingletonComponent
import ru.yurkirill.dagger2retention.ui.DiaryActivity
import ru.yurkirill.dagger2retention.ui.HomeActivity
import ru.yurkirill.dagger2retention.ui.StatisticActivity
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.atomic.AtomicLong

/**
 * Dagger singleton-компонент уровня приложения.
 */
private var _singletonComponent: SingletonComponent =
        DaggerSingletonComponent
                .builder()
//                .appModule(appModule)
                .build()

var appModule: AppModule? = null

/**
 * Генератор идентификаторов удерживаемых компонентов Dagger.
 */
val newRetentionId get() = _retentionId.incrementAndGet()

private val _retentionId = AtomicLong()

/**
 * Хранилище удерживаемых компонентов Dagger.
 */
private val _components = ConcurrentHashMap<Long, Component>()

/**
 * Удаление Dagger-компонента из хранилища при завершении им жизненного цикла.
 * @param retentionId Идентификатор сохранения.
 */
fun remove(retentionId: Long) {
    _components.remove(retentionId)
}

/**
 * Получение экземпляра компонента Dagger.
 * @param componentProvider Функция создания компонента.
 */
private inline fun <reified T: Component> getComponent(componentProvider: () -> T): T {
    return componentProvider()
}

/**
 * Получение сохраненного экземпляра компонента Dagger.
 * @param retentionId Идентификатор сохранения.
 * @param componentProvider Функция создания компонента.
 */
private inline fun <reified T: Component> getComponent(retentionId: Long, componentProvider: () -> T): T {
    var component = _components[retentionId] as? T
    if (component != null) return component
    component = componentProvider()
    _components[retentionId] = component
    return component
}

/**
 * Получение экземпляра ActivityComponent.
 */
fun getActivityComponent() =
        getComponent { _singletonComponent.activityComponentBuilder().build() }

/**
 * Получение сохраненного экземпляра ActivityComponent.
 * @param retentionId Идентификатор сохранения.
 */
fun getActivityComponent(retentionId: Long) =
        getComponent(retentionId) { _singletonComponent.activityComponentBuilder().build() }

/**
 * Менеджер инъекции зависимостей в целевые объекты.
 */
fun <T> T.inject() {
    when (this) {
        is HomeActivity -> getActivityComponent().inject(this)
        is DiaryActivity -> getActivityComponent().inject(this)
        is StatisticActivity -> getActivityComponent().inject(this)
    }
}

/**
 * Менеджер инъекции сохраненных зависимостей в целевые объекты.
 * @param retentionId Идентификатор сохранения.
 */
fun <T> T.inject(retentionId: Long) {
    when (this) {
        is HomeActivity -> getActivityComponent(retentionId).inject(this)
        is DiaryActivity -> getActivityComponent(retentionId).inject(this)
        is StatisticActivity -> getActivityComponent(retentionId).inject(this)
    }
}

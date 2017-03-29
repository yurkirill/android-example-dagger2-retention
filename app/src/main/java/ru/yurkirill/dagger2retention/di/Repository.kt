package ru.yurkirill.dagger2retention.di

import ru.yurkirill.dagger2retention.di.singleton.DaggerSingletonComponent
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.atomic.AtomicLong

/**
 * Корневой компонент уровня приложения.
 */
var singletonComponent =
        DaggerSingletonComponent
                .builder()
                .appModule(appModule)
                .build()!!

/**
 * Генератор идентификаторов удерживаемых компонентов.
 */
val newRetentionId
    get() = _retentionId.incrementAndGet()

private val _retentionId = AtomicLong()

/**
 * Хранилище удерживаемых компонентов.
 */
val components = ConcurrentHashMap<Long, Component>()

/**
 * Удаление завершившего свой жизненный цикл компонента из хранилища.
 * @param retentionId Идентификатор удерживаемого компонента.
 */
fun remove(retentionId: Long) {
    components.remove(retentionId)
}

/**
 * Получение экземпляра компонента предварительно сохраненного или сгенерированного с помощью функции-провайдера.
 * @param retentionId Идентификатор удерживаемого компонента.
 * @param componentProvider Функция создания компонента.
 */
inline fun <reified T: Component> getComponent(retentionId: Long, componentProvider: () -> T): T {
    var component = components[retentionId] as? T
    if (component != null) return component
    component = componentProvider()
    components[retentionId] = component
    return component
}

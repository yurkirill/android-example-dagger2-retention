package ru.yurkirill.dagger2retention.util

import android.util.Log
import ru.yurkirill.dagger2retention.BuildConfig

fun Any.logd(msg: String) {
    if (!BuildConfig.DEBUG) return
    Log.d(this::class.simpleName, msg)
}

inline fun Any.logd(msg: () -> String) {
    if (!BuildConfig.DEBUG) return
    Log.d(this::class.simpleName, msg())
}

fun Any.logi(msg: String) {
    Log.i(this::class.simpleName, msg)
}

inline fun Any.logi(msg: () -> String) {
    Log.i(this::class.simpleName, msg())
}

fun Any.logw(msg: String) {
    Log.w(this::class.simpleName, msg)
}

inline fun Any.logw(msg: () -> String) {
    Log.w(this::class.simpleName, msg())
}

fun Any.loge(msg: String) {
    Log.e(this::class.simpleName, msg)
}

inline fun Any.loge(msg: () -> String) {
    Log.e(this::class.simpleName, msg())
}

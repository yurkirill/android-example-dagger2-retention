package ru.yurkirill.dagger2retention.ui

import android.os.Bundle
import ru.yurkirill.dagger2retention.R
import ru.yurkirill.dagger2retention.ui.base.BaseActivity

class SettingsActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)
    }
}

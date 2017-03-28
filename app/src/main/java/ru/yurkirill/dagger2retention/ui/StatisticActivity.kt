package ru.yurkirill.dagger2retention.ui

import android.os.Bundle
import kotlinx.android.synthetic.main.content_home.*
import ru.yurkirill.dagger2retention.R
import ru.yurkirill.dagger2retention.di.inject
import ru.yurkirill.dagger2retention.ui.base.navigation.NavigationActivity
import javax.inject.Inject

class StatisticActivity : NavigationActivity() {

    @Inject
    lateinit var str: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        inject(retentionId)
        setContentView(R.layout.activity_statistic)

        retentionIdTv.text = retentionId.toString()
        objectIdTv.text = System.identityHashCode(str).toString()
        valueTv.text = str
    }
}

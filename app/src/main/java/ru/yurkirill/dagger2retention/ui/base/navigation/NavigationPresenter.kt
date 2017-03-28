package ru.yurkirill.dagger2retention.ui.base.navigation

import ru.yurkirill.dagger2retention.R
import ru.yurkirill.dagger2retention.ui.*
import javax.inject.Inject
import javax.inject.Singleton

/**
 * [NavigationContract.Presenter] для взаимодействия с меню навигации.
 */
@Singleton
class NavigationPresenter @Inject constructor() : NavigationContract.Presenter {

    private data class Item(val cls: Class<out Any>, val toSwitch: Boolean)

    private val items: Map<Int, Item> = mapOf(
            R.id.nav_home to Item(HomeActivity::class.java, true),
            R.id.nav_diary to Item(DiaryActivity::class.java, true),
            R.id.nav_statistic to Item(StatisticActivity::class.java, true),
            R.id.nav_impexp to Item(ImpexpActivity::class.java, false),
            R.id.nav_settings to Item(SettingsActivity::class.java, false)
    )

    private var itemId = R.id.nav_home

    private var view: NavigationContract.View? = null

    override val selectedItemId get() = itemId

    override fun attachView(view: NavigationContract.View) {
        if (this.view != null) throw IllegalStateException()
        this.view = view
    }

    override fun detachView() {
        if (view == null) throw IllegalStateException()
        this.view = null
    }

    override fun selectItem(itemId: Int): Boolean {
        if (itemId == this.itemId) return false
        val view = this.view ?: return false
        val item = items[itemId] ?: throw IllegalArgumentException()
        view.startActivity(item.cls)
        if (item.toSwitch) {
            view.finish()
            this.itemId = itemId
        }
        return true
    }

    override fun goHome(): Boolean {
        return selectItem(R.id.nav_home)
    }
}

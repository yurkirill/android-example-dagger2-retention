package ru.yurkirill.dagger2retention.ui.base.navigation

import android.app.Activity
import android.content.Intent
import android.content.res.ColorStateList
import android.graphics.Color
import android.support.design.widget.NavigationView
import android.support.v4.view.GravityCompat
import android.support.v4.widget.DrawerLayout
import android.view.MenuItem
import android.view.View
import ru.yurkirill.dagger2retention.R
import ru.yurkirill.dagger2retention.ui.base.BaseActivity
import ru.yurkirill.dagger2retention.util.logw
import javax.inject.Inject

/**
 * [Activity] с поддержкой [NavigationView].
 */
abstract class NavigationActivity : BaseActivity(), NavigationContract.View {

    @Inject
    lateinit var navigationPresenter: NavigationContract.Presenter

    private var drawerLayout: DrawerLayout? = null
    private var itemId = 0

    override fun setContentView(layoutResID: Int) {
        super.setContentView(layoutResID)

        // Поиск NavigationView в разметке.
        val navigationView = findViewById(R.id.navigation_view) as? NavigationView
        if (navigationView == null) {
            logw("NavigationView not found")
            return
        }

        // Поиск DrawerLayout в разметке.
        val drawerLayout = findViewById(R.id.drawer_layout) as? DrawerLayout
        if (drawerLayout == null) {
            logw("DrawerLayout not found")
            return
        }
        this.drawerLayout = drawerLayout

        // Тонирование элемнтов меню навигации.
        menuTint(navigationView)

        // Листенер отслеживания движения панели меню навигации.
        setDrawerListener(drawerLayout)

        // Листенер выбора пункта в меню навигации.
        setMenuListener(navigationView, drawerLayout)

        // Выделение текущего элемента в меню навигации.
        navigationView.menu?.findItem(navigationPresenter.selectedItemId)?.isChecked = true
    }

    /**
     * Тонирование элементов меню навигации.
     */
    private fun menuTint(navigationView: NavigationView) {

        // Определение значения ColorPrimary заданного темой.
        val attrs = intArrayOf(android.support.design.R.attr.colorPrimary)
        val array = theme.obtainStyledAttributes(attrs)
        val colorPrimary = array.getColor(0, Color.BLACK)
        array.recycle()

        val states = arrayOf(intArrayOf(android.R.attr.state_checked), intArrayOf())

        // Установка цветового селектора для колеровки иконок меню.
        @Suppress("DEPRECATION")
        val colorsIcon = intArrayOf(colorPrimary, resources.getColor(R.color.nav_icon))
        navigationView.itemIconTintList = ColorStateList(states, colorsIcon)

        // Установка цветового селектора для колеровки текста меню.
        @Suppress("DEPRECATION")
        val colorsText = intArrayOf(colorPrimary, resources.getColor(R.color.nav_text))
        navigationView.itemTextColor = ColorStateList(states, colorsText)
    }

    /**
     * Листенер отслеживания движения панели меню навигации.
     */
    private fun setDrawerListener(drawerLayout: DrawerLayout) {
        drawerLayout.addDrawerListener(object : DrawerLayout.DrawerListener {
            override fun onDrawerStateChanged(newState: Int) {}
            override fun onDrawerSlide(drawerView: View?, slideOffset: Float) {}
            override fun onDrawerOpened(drawerView: View?) {}

            // Обработка выбора пункта меню.
            override fun onDrawerClosed(drawerView: View?) {
                if (itemId > 0) {
                    if (itemId != navigationPresenter.selectedItemId && !navigationPresenter.selectItem(itemId)) {
                        logw("Not found handler for item ID=$itemId")
                    }
                    itemId = 0
                }
            }
        })
    }

    /**
     * Листенер выбора пункта в меню навигации.
     */
    private fun setMenuListener(navigationView: NavigationView, drawerLayout: DrawerLayout) {
        navigationView.setNavigationItemSelectedListener {

            // Закрытие панели меню навигации.
            drawerLayout.closeDrawer(GravityCompat.START)
            itemId = it.itemId
            true
        }
    }

    override fun onResume() {
        super.onResume()
        navigationPresenter.attachView(this)
    }

    override fun onPause() {
        navigationPresenter.detachView()
        super.onPause()
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {

        // Открытие панели меню навигации при нажатии на HomeButton.
        if (item?.itemId == android.R.id.home && drawerLayout != null) {
            drawerLayout?.openDrawer(GravityCompat.START)
            return true
        }

        return super.onOptionsItemSelected(item)
    }

    override fun onBackPressed() {
        if (drawerLayout?.isDrawerOpen(GravityCompat.START) ?: false) {
            drawerLayout?.closeDrawer(GravityCompat.START)
        } else if (!performBackPressed() && !navigationPresenter.goHome()) {
            super.onBackPressed()
        }
    }

    /**
     * Выполнение действий при нажатии на кнопку Back.
     * @return True - действие выполнено, false - действие не выполнено.
     */
    protected open fun performBackPressed(): Boolean = false

    override fun startActivity(cls: Class<out Any>) {
        startActivity(Intent(this, cls))
    }
}

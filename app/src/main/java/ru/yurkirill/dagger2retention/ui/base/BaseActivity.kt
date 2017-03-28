package ru.yurkirill.dagger2retention.ui.base

import android.app.Activity
import android.graphics.Color
import android.graphics.PorterDuff
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar
import android.view.Menu
import android.view.MenuItem
import ru.yurkirill.dagger2retention.R
import ru.yurkirill.dagger2retention.di.newRetentionId
import ru.yurkirill.dagger2retention.di.remove
import ru.yurkirill.dagger2retention.util.logw

const val KEY_THEME = "theme"
const val KEY_TITLE = "title"
const val KEY_RETENTION_ID = "retentionId"

/**
 * [Activity] с поддержкой [Toolbar].
 *
 * - Тонирование иконки в цвет, заданный атрибутом colorControlNormal.
 * - Установка темы из интента (параметр KEY_THEME).
 * - Установка заголовка из интента (параметр KEY_TITLE).
 */
abstract class BaseActivity : AppCompatActivity() {

    /**
     * Идентификатор удержания компонента Dagger.
     */
    protected var retentionId = 0L

    private var colorControlNormal = Color.BLACK

    override fun setContentView(layoutResID: Int) {
        super.setContentView(layoutResID)

        // Поиск Toolbar в разметке.
        val toolbar = findViewById(R.id.toolbar) as? Toolbar
        if (toolbar == null) {
            logw("Toolbar not found")
            return
        }

        // Определение значения ColorControlNormal заданного темой.
        val attrs = intArrayOf(android.support.design.R.attr.colorControlNormal)
        val array = toolbar.context.theme.obtainStyledAttributes(attrs)
        colorControlNormal = array.getColor(0, Color.BLACK)
        array.recycle()

        // Тонирование иконки Home.
        toolbar.navigationIcon?.setColorFilter(colorControlNormal, PorterDuff.Mode.SRC_IN)

        // Настройка ActionBar.
        setSupportActionBar(toolbar)
        val actionbar = supportActionBar
        if (actionbar == null) {
            logw("Actionbar not found")
            return
        }
        actionbar.setDisplayHomeAsUpEnabled(true)

        // Установка заголовка из интента.
        val extras = intent.extras ?: return
        if (extras.containsKey(KEY_TITLE)) {
            actionbar.title = extras.getCharSequence(KEY_TITLE)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {

        // Установка темы из интента.
        if (intent.extras?.containsKey(KEY_THEME) ?: false) {
            setTheme(intent.extras.getInt(KEY_THEME))
        }

        // Получение идентификатора удержания сохраняющего свое значение при сменах конфигурации.
        retentionId = savedInstanceState?.getLong(KEY_RETENTION_ID) ?: newRetentionId

        super.onCreate(savedInstanceState)
    }

    override fun onCreatePanelMenu(featureId: Int, menu: Menu?): Boolean {

        // Тонирование иконки опционального меню в colorControlNormal.
        if (menu != null) {
            var index = menu.size()
            while (index-- > 0) {
                menu.getItem(index).icon?.setColorFilter(colorControlNormal, PorterDuff.Mode.SRC_IN)
            }
        }

        return super.onCreatePanelMenu(featureId, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {

        // Выход из активити при нажатии на кнопку Home.
        if (item?.itemId == android.R.id.home) {
            finish()
            return true
        }

        return super.onOptionsItemSelected(item)
    }

    override fun onSaveInstanceState(outState: Bundle?) {
        super.onSaveInstanceState(outState)

        // Сохранение идентификатора удержания при смене конфигурации.
        outState?.putLong(KEY_RETENTION_ID, retentionId)
    }

    override fun onDestroy() {
        super.onDestroy()

        // Утилизация DI-компонента при окончательном завершении активити.
        if (isFinishing) remove(retentionId)
    }
}

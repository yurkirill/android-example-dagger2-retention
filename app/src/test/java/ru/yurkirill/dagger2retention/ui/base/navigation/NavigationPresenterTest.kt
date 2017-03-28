package ru.yurkirill.dagger2retention.ui.base.navigation

import org.junit.Test
import ru.yurkirill.dagger2retention.R
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertNotEquals
import kotlin.test.assertTrue

enum class State {INITIAL, CALL, SWITCH}

class FakeView : NavigationContract.View {
    var state = State.INITIAL

    override fun startActivity(cls: java.lang.Class<out kotlin.Any>) {
        state = State.CALL
    }

    override fun finish() {
        if (state == State.INITIAL) throw IllegalStateException()
        state = State.SWITCH
    }
}

class NavigationPresenterTest {

    /**
     * Начальное значение выбранного пункта меню должно соответствовать Home.
     */
    @Test
    fun getItemId_InitialValue_ReturnHome() {
        val presenter = NavigationPresenter()

        val id = presenter.selectedItemId

        assertEquals(R.id.nav_home, id)
    }

    /**
     * При возврате на домашний экран значение выбранного пукта меню должно соответствовать Home.
     */
    @Test
    fun getItemId_AfterGoHome_ReturnHome() {
        val presenter = NavigationPresenter()
        val view = FakeView()
        presenter.attachView(view)
        val idBefore = presenter.selectedItemId
        presenter.selectItem(R.id.nav_diary)

        presenter.goHome()
        val idAfter = presenter.selectedItemId

        assertEquals(idBefore, idAfter)
    }

    /**
     * При выборе пункта меню типа "вызов" значение выбранного пукта меню не должно измениться.
     */
    @Test
    fun getItemId_AfterSelectCallType_TheSameValue() {
        val presenter = NavigationPresenter()
        val view = FakeView()
        presenter.attachView(view)
        val idBefore = presenter.selectedItemId

        presenter.selectItem(R.id.nav_settings)
        val idAfter = presenter.selectedItemId

        assertEquals(idBefore, idAfter)
    }

    /**
     * При выборе пункта меню типа "переключение" значение выбранного пукта меню должно измениться.
     */
    @Test
    fun getItemId_AfterSelectSwitchType_ChangedValue() {
        val presenter = NavigationPresenter()
        val view = FakeView()
        presenter.attachView(view)
        val idBefore = presenter.selectedItemId

        presenter.selectItem(R.id.nav_diary)
        val idAfter = presenter.selectedItemId

        assertNotEquals(idBefore, idAfter)
    }

    /**
     * Повторная привязка View без предшествующей отвязки должна вызывать исключение.
     */
    @Test(expected = IllegalStateException::class)
    fun attachView_WhenReattachedWithoutDetached_ThrowException() {
        val presenter = NavigationPresenter()
        val view = FakeView()

        presenter.attachView(view)
        presenter.attachView(view)
    }

    /**
     * Повторная привязка View с предшествующей отвязкой не должна вызывать проблем.
     */
    @Test
    fun attachView_WhenDetachedAndReattached_OK() {
        val presenter = NavigationPresenter()
        val view = FakeView()

        presenter.attachView(view)
        presenter.detachView()
        presenter.attachView(view)
    }

    /**
     * Отвязка View без предшествующей привязки должна вызывать исключение.
     */
    @Test(expected = IllegalStateException::class)
    fun detachView_WhenDetached_IllegalStateException() {
        val presenter = NavigationPresenter()

        presenter.detachView()
    }

    /**
     * При выборе нового пункта меню без предшествующей привязки должно возвращаться false.
     */
    @Test
    fun selectItem_WithoutAttach_ReturnFalse() {
        val presenter = NavigationPresenter()

        val ret = presenter.selectItem(R.id.nav_diary)

        assertFalse(ret)
    }

    /**
     * При выборе того же самого пункта меню должно возвращаться false.
     */
    @Test
    fun selectItem_WhenTheSame_ReturnFalse() {
        val presenter = NavigationPresenter()
        val view = FakeView()
        presenter.attachView(view)

        val ret = presenter.selectItem(R.id.nav_home)

        assertEquals(State.INITIAL, view.state)
        assertFalse(ret)
    }

    /**
     * При выборе иного пункта меню должно возвращаться true.
     */
    @Test
    fun selectItem_WhenAnother_ReturnTrue() {
        val presenter = NavigationPresenter()
        val view = FakeView()
        presenter.attachView(view)

        val ret = presenter.selectItem(R.id.nav_settings)

        assertTrue(ret)
    }

    /**
     * При выборе пункта меню типа "вызов", соответствующее состояние должно быть зафиксировано во View.
     */
    @Test
    fun selectItem_WhenCall_DoCall() {
        val presenter = NavigationPresenter()
        val view = FakeView()
        presenter.attachView(view)

        presenter.selectItem(R.id.nav_settings)

        assertEquals(State.CALL, view.state)
    }

    /**
     * При выборе пункта меню типа "переключение", соответствующее состояние должно быть зафиксировано во View.
     */
    @Test
    fun selectItem_WhenSwitch_DoSwitch() {
        val presenter = NavigationPresenter()
        val view = FakeView()
        presenter.attachView(view)

        presenter.selectItem(R.id.nav_diary)

        assertEquals(State.SWITCH, view.state)
    }

    /**
     * При выборе несуществующего пункта меню должно вызываться исключение.
     */
    @Test(expected = IllegalArgumentException::class)
    fun selectItem_WhenIllegalItemId_ThrowException() {
        val presenter = NavigationPresenter()
        val view = FakeView()
        presenter.attachView(view)

        presenter.selectItem(R.id.text)
    }

    /**
     * При возврате на домашний экран без предшествующей привязки должно возвращаться false.
     */
    @Test
    fun goHome_WithoutAttach_ReturnFalse() {
        val presenter = NavigationPresenter()

        val ret = presenter.goHome()

        assertFalse(ret)
    }

    /**
     * При возврате на домашний экран из домашнего должно возвращаться false.
     */
    @Test
    fun goHome_FromHome_ReturnFalse() {
        val presenter = NavigationPresenter()
        val view = FakeView()
        presenter.attachView(view)

        val ret = presenter.goHome()

        assertFalse(ret)
    }

    /**
     * При возврате на домашний экран из другого экрана должно возвращаться true.
     */
    @Test
    fun goHome_FromAnother_ReturnTrue() {
        val presenter = NavigationPresenter()
        presenter.attachView(FakeView())
        presenter.selectItem(R.id.nav_diary)
        presenter.detachView()
        val view = FakeView()
        presenter.attachView(view)

        val ret = presenter.goHome()

        assertEquals(State.SWITCH, view.state)
        assertTrue(ret)
    }
}

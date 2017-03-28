package ru.yurkirill.dagger2retention.ui.base.navigation

interface NavigationContract {

    /**
     * View, поддерживающая меню навигации.
     */
    interface View {

        /**
         * Запуск новой View.
         * @param cls Тип запускаемого View.
         */
        fun startActivity(cls: Class<out Any>)

        /**
         * Завершение текущей View.
         */
        fun finish()
    }

    /**
     * Presenter для взаимодействия с меню навигации.
     */
    interface Presenter {

        /**
         * Идентификатор выбранного пункта меню навигации.
         */
        val selectedItemId: Int
            get

        /**
         * Подключение View.
         * @param view Подключаемое view.
         */
        fun attachView(view: View)

        /**
         * Отключение ранее подключенного View.
         */
        fun detachView()

        /**
         * Выбор нового пункта меню навигации.
         * @param itemId Идентификатор пункта меню навигации.
         * @return True - успешная обработка, false - обработчик не найден.
         */
        fun selectItem(itemId: Int): Boolean

        /**
         * Переключение на домашний View.
         * @return True - произведено переключение, false - переключение не требуется.
         */
        fun goHome(): Boolean
    }
}

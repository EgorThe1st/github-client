package ru.android.github.presentation.abstraction


interface LoginPresenter : BasePresenter {

    fun authorize(name: String, password: String)

    interface View : BaseView {

        fun startNextActivity()

        fun showDeviceError()

        fun showNoConnectionError()

    }
}
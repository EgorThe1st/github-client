package ru.android.github.presentation.abstraction

interface SettingsPresenter : BasePresenter {

    fun updateUser(userData: Triple<String, String, String>)

    interface View : BaseView {

        fun showUserInfo(info: Triple<String, String, String>)

        fun showNetError()
    }
}
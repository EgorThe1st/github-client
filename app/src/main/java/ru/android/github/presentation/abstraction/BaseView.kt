package ru.android.github.presentation.abstraction


interface BaseView {

    fun showLoading()

    fun hideLoading()

    fun showError(error: String)

    fun hideKeyboard() {}
}
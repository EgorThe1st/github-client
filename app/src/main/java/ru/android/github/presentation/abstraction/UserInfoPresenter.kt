package ru.android.github.presentation.abstraction

import ru.android.github.domain.model.User


interface UserInfoPresenter : BasePresenter {

    fun getInfo(fromDB: Boolean)

    fun deAuthorizeUser()

    interface View : BaseView {

        fun showUserInfo(user: User)

        fun hideProgressBar()
    }
}